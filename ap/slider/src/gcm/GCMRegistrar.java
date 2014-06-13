/*     */ package puzzle.slider.vn.gcm;
/*     */ 
/*     */ import android.app.PendingIntent;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.content.SharedPreferences;
/*     */ import android.content.SharedPreferences.Editor;
/*     */ import android.content.pm.ActivityInfo;
/*     */ import android.content.pm.PackageInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.content.pm.PackageManager.NameNotFoundException;
/*     */ import android.content.pm.ResolveInfo;
/*     */ import android.os.Build;
/*     */ import android.os.Build.VERSION;
/*     */ import android.util.Log;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
import java.util.Set;
/*     */ 
/*     */ @SuppressWarnings("unused")
public final class GCMRegistrar
/*     */ {
/*     */   private static final String TAG = "GCMRegistrar";
/*     */   private static final String BACKOFF_MS = "backoff_ms";
/*     */   private static final String GSF_PACKAGE = "com.google.android.gsf";
/*     */   private static final String PREFERENCES = "com.google.android.gcm";
/*     */   private static final int DEFAULT_BACKOFF_MS = 3000;
/*     */   private static final String PROPERTY_REG_ID = "regId";
/*     */   private static final String PROPERTY_APP_VERSION = "appVersion";
/*     */   private static final String PROPERTY_ON_SERVER = "onServer";
/*     */   private static GCMBroadcastReceiver sRetryReceiver;
/*     */ 
/*     */   public static void checkDevice(Context context)
/*     */   {
/*  74 */     int version = Build.VERSION.SDK_INT;
/*  75 */     if (version < 8) {
/*  76 */       throw new UnsupportedOperationException(new StringBuilder().append("Device must be at least API Level 8 (instead of ").append(version).append(")").toString());
/*     */     }
/*     */ 
/*  79 */     PackageManager packageManager = context.getPackageManager();
/*     */     try {
/*  81 */       packageManager.getPackageInfo("com.google.android.gsf", 0);
/*     */     } catch (PackageManager.NameNotFoundException e) {
/*  83 */       throw new UnsupportedOperationException("Device does not have package com.google.android.gsf");
/*     */     }
/*     */   }
/*     */ 
/*     */   @SuppressWarnings("unchecked")
public static void checkManifest(Context context)
/*     */   {
/* 114 */     PackageManager packageManager = context.getPackageManager();
/* 115 */     String packageName = context.getPackageName();
/* 116 */     String permissionName = new StringBuilder().append(packageName).append(".permission.C2D_MESSAGE").toString();
/*     */     try
/*     */     {
/* 119 */       packageManager.getPermissionInfo(permissionName, 4096);
/*     */     }
/*     */     catch (PackageManager.NameNotFoundException e) {
/* 122 */       throw new IllegalStateException(new StringBuilder().append("Application does not define permission ").append(permissionName).toString());
/*     */     }
/*     */     PackageInfo receiversInfo;
/*     */     try
/*     */     {
/* 128 */       receiversInfo = packageManager.getPackageInfo(packageName, 2);
/*     */     }
/*     */     catch (PackageManager.NameNotFoundException e) {
/* 131 */       throw new IllegalStateException(new StringBuilder().append("Could not get receivers for package ").append(packageName).toString());
/*     */     }
/*     */ 
/* 134 */     ActivityInfo[] receivers = receiversInfo.receivers;
/* 135 */     if ((receivers == null) || (receivers.length == 0)) {
/* 136 */       throw new IllegalStateException(new StringBuilder().append("No receiver for package ").append(packageName).toString());
/*     */     }
/*     */ 
/* 139 */     if (Log.isLoggable("GCMRegistrar", 2)) {
/* 140 */       Log.v("GCMRegistrar", new StringBuilder().append("number of receivers for ").append(packageName).append(": ").append(receivers.length).toString());
/*     */     }
/*     */ 
/* 143 */     @SuppressWarnings("rawtypes")
Set allowedReceivers = new HashSet();
/* 144 */     for (ActivityInfo receiver : receivers) {
/* 145 */       if (!"com.google.android.c2dm.permission.SEND".equals(receiver.permission))
/*     */         continue;
/* 147 */       allowedReceivers.add(receiver.name);
/*     */     }
/*     */ 
/* 150 */     if (allowedReceivers.isEmpty()) {
/* 151 */       throw new IllegalStateException("No receiver allowed to receive com.google.android.c2dm.permission.SEND");
/*     */     }
/*     */ 
/* 154 */     checkReceiver(context, allowedReceivers, "com.google.android.c2dm.intent.REGISTRATION");
/*     */ 
/* 156 */     checkReceiver(context, allowedReceivers, "com.google.android.c2dm.intent.RECEIVE");
/*     */   }
/*     */ 
/*     */   private static void checkReceiver(Context context, Set<String> allowedReceivers, String action)
/*     */   {
/* 162 */     PackageManager pm = context.getPackageManager();
/* 163 */     String packageName = context.getPackageName();
/* 164 */     Intent intent = new Intent(action);
/* 165 */     intent.setPackage(packageName);
/* 166 */     List<ResolveInfo> receivers = pm.queryBroadcastReceivers(intent, 32);
/*     */ 
/* 168 */     if (receivers.isEmpty()) {
/* 169 */       throw new IllegalStateException(new StringBuilder().append("No receivers for action ").append(action).toString());
/*     */     }
/*     */ 
/* 172 */     if (Log.isLoggable("GCMRegistrar", 2)) {
/* 173 */       Log.v("GCMRegistrar", new StringBuilder().append("Found ").append(receivers.size()).append(" receivers for action ").append(action).toString());
/*     */     }
/*     */ 
/* 177 */     for (ResolveInfo receiver : receivers) {
/* 178 */       String name = receiver.activityInfo.name;
/* 179 */       if (!allowedReceivers.contains(name))
/* 180 */         throw new IllegalStateException(new StringBuilder().append("Receiver ").append(name).append(" is not set with permission ").append("com.google.android.c2dm.permission.SEND").toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void register(Context context, String[] senderIds)
/*     */   {
/* 202 */     setRetryBroadcastReceiver(context);
/* 203 */     resetBackoff(context);
/* 204 */     internalRegister(context, senderIds);
/*     */   }
/*     */ 
/*     */   static void internalRegister(Context context, String[] senderIds) {
/* 208 */     if ((senderIds == null) || (senderIds.length == 0)) {
/* 209 */       throw new IllegalArgumentException("No senderIds");
/*     */     }
/* 211 */     StringBuilder builder = new StringBuilder(senderIds[0]);
/* 212 */     for (int i = 1; i < senderIds.length; i++) {
/* 213 */       builder.append(',').append(senderIds[i]);
/*     */     }
/* 215 */     String senders = builder.toString();
/* 216 */     Log.v("GCMRegistrar", new StringBuilder().append("Registering app ").append(context.getPackageName()).append(" of senders ").append(senders).toString());
/*     */ 
/* 218 */     Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
/* 219 */     intent.setPackage("com.google.android.gsf");
/* 220 */     intent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
/*     */ 
/* 222 */     intent.putExtra("sender", senders);
/* 223 */     context.startService(intent);
/*     */   }
/*     */ 
/*     */   public static void unregister(Context context)
/*     */   {
/* 234 */     setRetryBroadcastReceiver(context);
/* 235 */     resetBackoff(context);
/* 236 */     internalUnregister(context);
/*     */   }
/*     */ 
/*     */   public static synchronized void onDestroy(Context context)
/*     */   {
/* 247 */     if (sRetryReceiver != null) {
/* 248 */       Log.v("GCMRegistrar", "Unregistering receiver");
/* 249 */       context.unregisterReceiver(sRetryReceiver);
/* 250 */       sRetryReceiver = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void internalUnregister(Context context) {
/* 255 */     Log.v("GCMRegistrar", new StringBuilder().append("Unregistering app ").append(context.getPackageName()).toString());
/* 256 */     Intent intent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
/* 257 */     intent.setPackage("com.google.android.gsf");
/* 258 */     intent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
/*     */ 
/* 260 */     context.startService(intent);
/*     */   }
/*     */ 
/*     */   static synchronized void setRetryBroadcastReceiver(Context context)
/*     */   {
/* 267 */     if (sRetryReceiver == null) {
/* 268 */       sRetryReceiver = new GCMBroadcastReceiver();
/* 269 */       String category = context.getPackageName();
/* 270 */       IntentFilter filter = new IntentFilter("com.google.android.gcm.intent.RETRY");
/*     */ 
/* 272 */       filter.addCategory(category);
/*     */ 
/* 274 */       String permission = new StringBuilder().append(category).append(".permission.C2D_MESSAGE").toString();
/* 275 */       Log.v("GCMRegistrar", "Registering receiver");
/* 276 */       context.registerReceiver(sRetryReceiver, filter, permission, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getRegistrationId(Context context)
/*     */   {
/* 289 */     SharedPreferences prefs = getGCMPreferences(context);
/* 290 */     String registrationId = prefs.getString("regId", "");
/*     */ 
/* 293 */     int oldVersion = prefs.getInt("appVersion", -2147483648);
/* 294 */     int newVersion = getAppVersion(context);
/* 295 */     if ((oldVersion != -2147483648) && (oldVersion != newVersion)) {
/* 296 */       Log.v("GCMRegistrar", new StringBuilder().append("App version changed from ").append(oldVersion).append(" to ").append(newVersion).append("; resetting registration id").toString());
/*     */ 
/* 298 */       clearRegistrationId(context);
/* 299 */       registrationId = "";
/*     */     }
/* 301 */     return registrationId;
/*     */   }
/*     */ 
/*     */   public static boolean isRegistered(Context context)
/*     */   {
/* 309 */     return getRegistrationId(context).length() > 0;
/*     */   }
/*     */ 
/*     */   static String clearRegistrationId(Context context)
/*     */   {
/* 319 */     return setRegistrationId(context, "");
/*     */   }
/*     */ 
/*     */   static String setRegistrationId(Context context, String regId)
/*     */   {
/* 329 */     SharedPreferences prefs = getGCMPreferences(context);
/* 330 */     String oldRegistrationId = prefs.getString("regId", "");
/* 331 */     int appVersion = getAppVersion(context);
/* 332 */     Log.v("GCMRegistrar", new StringBuilder().append("Saving regId on app version ").append(appVersion).toString());
/* 333 */     SharedPreferences.Editor editor = prefs.edit();
/* 334 */     editor.putString("regId", regId);
/* 335 */     editor.putInt("appVersion", appVersion);
/* 336 */     editor.commit();
/* 337 */     return oldRegistrationId;
/*     */   }
/*     */ 
/*     */   public static void setRegisteredOnServer(Context context, boolean flag)
/*     */   {
/* 344 */     SharedPreferences prefs = getGCMPreferences(context);
/* 345 */     Log.v("GCMRegistrar", new StringBuilder().append("Setting registered on server status as: ").append(flag).toString());
/* 346 */     SharedPreferences.Editor editor = prefs.edit();
/* 347 */     editor.putBoolean("onServer", flag);
/* 348 */     editor.commit();
/*     */   }
/*     */ 
/*     */   public static boolean isRegisteredOnServer(Context context)
/*     */   {
/* 355 */     SharedPreferences prefs = getGCMPreferences(context);
/* 356 */     boolean isRegistered = prefs.getBoolean("onServer", false);
/* 357 */     Log.v("GCMRegistrar", new StringBuilder().append("Is registered on server: ").append(isRegistered).toString());
/* 358 */     return isRegistered;
/*     */   }
/*     */ 
/*     */   private static int getAppVersion(Context context)
/*     */   {
/*     */     try
/*     */     {
/* 366 */       PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
/*     */ 
/* 368 */       return packageInfo.versionCode;
/*     */     } catch (PackageManager.NameNotFoundException e) {
/*     */     }
/* 371 */     throw new RuntimeException(new StringBuilder().append("Coult not get package name: ").append("").toString());
/*     */   }
/*     */ 
/*     */   static void resetBackoff(Context context)
/*     */   {
/* 383 */     Log.d("GCMRegistrar", new StringBuilder().append("resetting backoff for ").append(context.getPackageName()).toString());
/* 384 */     setBackoff(context, 3000);
/*     */   }
/*     */ 
/*     */   static int getBackoff(Context context)
/*     */   {
/* 394 */     SharedPreferences prefs = getGCMPreferences(context);
/* 395 */     return prefs.getInt("backoff_ms", 3000);
/*     */   }
/*     */ 
/*     */   static void setBackoff(Context context, int backoff)
/*     */   {
/* 408 */     SharedPreferences prefs = getGCMPreferences(context);
/* 409 */     SharedPreferences.Editor editor = prefs.edit();
/* 410 */     editor.putInt("backoff_ms", backoff);
/* 411 */     editor.commit();
/*     */   }
/*     */ 
/*     */   private static SharedPreferences getGCMPreferences(Context context) {
/* 415 */     return context.getSharedPreferences("com.google.android.gcm", 0);
/*     */   }
/*     */ 
/*     */   private GCMRegistrar() {
/* 419 */     throw new UnsupportedOperationException();
/*     */   }


	static String getFlatSenderIds(String... senderIds) {
	    if (senderIds == null || senderIds.length == 0) {
	        throw new IllegalArgumentException("No senderIds");
	    }
	    StringBuilder builder = new StringBuilder(senderIds[0]);
	    for (int i = 1; i < senderIds.length; i++) {
	        builder.append(',').append(senderIds[i]);
	    }
	    return builder.toString();
	}
	

/*     */ }

/* Location:           D:\gcm.jar
 * Qualified Name:     com.google.android.gcm.GCMRegistrar
 * JD-Core Version:    0.6.0
 */