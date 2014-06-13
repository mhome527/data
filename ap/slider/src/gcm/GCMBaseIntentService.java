package puzzle.slider.vn.gcm;
/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Random;
import java.util.concurrent.TimeUnit;

import puzzle.slider.vn.util.ShowLog;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;

/**
 * Skeleton for application-specific {@link IntentService}s responsible for
 * handling communication from Google Cloud Messaging service.
 * <p>
 * The abstract methods in this class are called from its worker thread, and
 * hence should run in a limited amount of time. If they execute long
 * operations, they should spawn new threads, otherwise the worker thread will
 * be blocked.
 * <p>
 * Subclasses must provide a public no-arg constructor.
 */
public abstract class GCMBaseIntentService extends IntentService {

    public static final String TAG = "GCMBaseIntentService";

    // wakelock
    private static final String WAKELOCK_KEY = "GCM_LIB";
    private static PowerManager.WakeLock sWakeLock;

    // Java lock used to synchronize access to sWakelock
    private static final Object LOCK = GCMBaseIntentService.class;

    private final String[] mSenderIds;

    // instance counter
    private static int sCounter = 0;

    private static final Random sRandom = new Random();

    private static final int MAX_BACKOFF_MS =
        (int) TimeUnit.SECONDS.toMillis(3600); // 1 hour

    // token used to check intent origin
    private static final String TOKEN =
            Long.toBinaryString(sRandom.nextLong());
    private static final String EXTRA_TOKEN = "token";

    /**
     * Constructor that does not set a sender id, useful when the sender id
     * is context-specific.
     * <p>
     * When using this constructor, the subclass <strong>must</strong>
     * override {@link #getSenderIds(Context)}, otherwise methods such as
     * {@link #onHandleIntent(Intent)} will throw an
     * {@link IllegalStateException} on runtime.
     */
    protected GCMBaseIntentService() {
        this(getName("DynamicSenderIds"), null);
    }

    /**
     * Constructor used when the sender id(s) is fixed.
     */
    protected GCMBaseIntentService(String... senderIds) {
        this(getName(senderIds), senderIds);
    }

    private GCMBaseIntentService(String name, String[] senderIds) {
        super(name);  // name is used as base name for threads, etc.
        mSenderIds = senderIds;
    }

    private static String getName(String senderId) {
        String name = "GCMIntentService-" + senderId + "-" + (++sCounter);
        ShowLog.v(TAG, "Intent service name: " + name);
        return name;
    }

    private static String getName(String[] senderIds) {
        String flatSenderIds = GCMRegistrar.getFlatSenderIds(senderIds);
        return getName(flatSenderIds);
    }

    /**
     * Gets the sender ids.
     *
     * <p>By default, it returns the sender ids passed in the constructor, but
     * it could be overridden to provide a dynamic sender id.
     *
     * @throws IllegalStateException if sender id was not set on constructor.
     */
    protected String[] getSenderIds(Context context) {
        if (mSenderIds == null) {
            throw new IllegalStateException("sender id not set on constructor");
        }
        return mSenderIds;
    }

    /**
     * Called when a cloud message has been received.
     *
     * @param context application's context.
     * @param intent intent containing the message payload as extras.
     */
    protected abstract void onMessage(Context context, Intent intent);

    /**
     * Called when the GCM server tells pending messages have been deleted
     * because the device was idle.
     *
     * @param context application's context.
     * @param total total number of collapsed messages
     */
    protected void onDeletedMessages(Context context, int total) {
    }

    /**
     * Called on a registration error that could be retried.
     *
     * <p>By default, it does nothing and returns {@literal true}, but could be
     * overridden to change that behavior and/or display the error.
     *
     * @param context application's context.
     * @param errorId error id returned by the GCM service.
     *
     * @return if {@literal true}, failed operation will be retried (using
     *         exponential backoff).
     */
    protected boolean onRecoverableError(Context context, String errorId) {
        return true;
    }

    /**
     * Called on registration or unregistration error.
     *
     * @param context application's context.
     * @param errorId error id returned by the GCM service.
     */
    protected abstract void onError(Context context, String errorId);

    /**
     * Called after a device has been registered.
     *
     * @param context application's context.
     * @param registrationId the registration id returned by the GCM service.
     */
    protected abstract void onRegistered(Context context,
            String registrationId);

    /**
     * Called after a device has been unregistered.
     *
     * @param registrationId the registration id that was previously registered.
     * @param context application's context.
     */
    protected abstract void onUnregistered(Context context,
            String registrationId);

    @Override
    public final void onHandleIntent(Intent intent) {
        try {
            Context context = getApplicationContext();
            String action = intent.getAction();
            if (action.equals(GCMConstants.INTENT_FROM_GCM_REGISTRATION_CALLBACK)) {
                GCMRegistrar.setRetryBroadcastReceiver(context);
                handleRegistration(context, intent);
            } else if (action.equals(GCMConstants.INTENT_FROM_GCM_MESSAGE)) {
                // checks for special messages
                String messageType =
                        intent.getStringExtra(GCMConstants.EXTRA_SPECIAL_MESSAGE);
                if (messageType != null) {
                    if (messageType.equals(GCMConstants.VALUE_DELETED_MESSAGES)) {
                        String sTotal =
                                intent.getStringExtra(GCMConstants.EXTRA_TOTAL_DELETED);
                        if (sTotal != null) {
                            try {
                                int total = Integer.parseInt(sTotal);
                                ShowLog.v(TAG, "Received deleted messages " +
                                        "notification: " + total);
                                onDeletedMessages(context, total);
                            } catch (NumberFormatException e) {
                            	ShowLog.e(TAG, "GCM returned invalid number of " +
                                        "deleted messages: " + sTotal);
                            }
                        }
                    } else {
                        // application is not using the latest GCM library
                    	ShowLog.e(TAG, "Received unknown special message: " +
                                messageType);
                    }
                } else {
                    onMessage(context, intent);
                }
            } else if (action.equals(GCMConstants.INTENT_FROM_GCM_LIBRARY_RETRY)) {
                String token = intent.getStringExtra(EXTRA_TOKEN);
                if (!TOKEN.equals(token)) {
                    // make sure intent was generated by this class, not by a
                    // malicious app.
                	ShowLog.e(TAG, "Received invalid token: " + token);
                    return;
                }
                // retry last call
                if (GCMRegistrar.isRegistered(context)) {
                    GCMRegistrar.internalUnregister(context);
                } else {
                    String[] senderIds = getSenderIds(context);
                    GCMRegistrar.internalRegister(context, senderIds);
                }
            }
        } finally {
            // Release the power lock, so phone can get back to sleep.
            // The lock is reference-counted by default, so multiple
            // messages are ok.

            // If onMessage() needs to spawn a thread or do something else,
            // it should use its own lock.
            synchronized (LOCK) {
                // sanity check for null as this is a public method
                if (sWakeLock != null) {
                    ShowLog.v(TAG, "Releasing wakelock");
                    if (sWakeLock.isHeld()) {
                    	sWakeLock.release();
                    	ShowLog.v(TAG, "Released wakelock");
					}
                    
                } else {
                    // should never happen during normal workflow
                	ShowLog.e(TAG, "Wakelock reference is null");
                }
            }
        }
    }

    /**
     * Called from the broadcast receiver.
     * <p>
     * Will process the received intent, call handleMessage(), registered(),
     * etc. in background threads, with a wake lock, while keeping the service
     * alive.
     */
    static void runIntentInService(Context context, Intent intent,
            String className) {
        synchronized (LOCK) {
            if (sWakeLock == null) {
                // This is called from BroadcastReceiver, there is no init.
                PowerManager pm = (PowerManager)
                        context.getSystemService(Context.POWER_SERVICE);
                sWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        WAKELOCK_KEY);
            }
        }
        ShowLog.v(TAG, "Acquiring wakelock");
        sWakeLock.acquire();
        intent.setClassName(context, className);
        context.startService(intent);
    }

    private void handleRegistration(final Context context, Intent intent) {
        String registrationId = intent.getStringExtra(GCMConstants.EXTRA_REGISTRATION_ID);
        String error = intent.getStringExtra(GCMConstants.EXTRA_ERROR);
        String unregistered = intent.getStringExtra(GCMConstants.EXTRA_UNREGISTERED);
        ShowLog.showLogDebug(TAG, "handleRegistration: registrationId = " + registrationId +
                ", error = " + error + ", unregistered = " + unregistered);

        // registration succeeded
        if (registrationId != null) {
            GCMRegistrar.resetBackoff(context);
            GCMRegistrar.setRegistrationId(context, registrationId);
            onRegistered(context, registrationId);
            return;
        }

        // unregistration succeeded
        if (unregistered != null) {
            // Remember we are unregistered
            GCMRegistrar.resetBackoff(context);
            String oldRegistrationId =
                    GCMRegistrar.clearRegistrationId(context);
            onUnregistered(context, oldRegistrationId);
            return;
        }

        // last operation (registration or unregistration) returned an error;
        ShowLog.showLogDebug(TAG, "Registration error: " + error);
        // Registration failed
        if (GCMConstants.ERROR_SERVICE_NOT_AVAILABLE.equals(error)) {
            boolean retry = onRecoverableError(context, error);
            if (retry) {
                int backoffTimeMs = GCMRegistrar.getBackoff(context);
                int nextAttempt = backoffTimeMs / 2 +
                        sRandom.nextInt(backoffTimeMs);
                ShowLog.showLogDebug(TAG, "Scheduling registration retry, backoff = " +
                        nextAttempt + " (" + backoffTimeMs + ")");
                Intent retryIntent =
                        new Intent(GCMConstants.INTENT_FROM_GCM_LIBRARY_RETRY);
                retryIntent.putExtra(EXTRA_TOKEN, TOKEN);
                PendingIntent retryPendingIntent = PendingIntent
                        .getBroadcast(context, 0, retryIntent, 0);
                AlarmManager am = (AlarmManager)
                        context.getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + nextAttempt,
                        retryPendingIntent);
                // Next retry should wait longer.
                if (backoffTimeMs < MAX_BACKOFF_MS) {
                  GCMRegistrar.setBackoff(context, backoffTimeMs * 2);
                }
            } else {
            	 ShowLog.showLogDebug(TAG, "Not retrying failed operation");
            }
        } else {
            // Unrecoverable error, notify app
            onError(context, error);
        }
    }

}





/*
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import app.jp.cropnet.utils.ShowLog;

     
     public abstract class GCMBaseIntentService extends IntentService
     {
	     public static final String TAG = "GCMBaseIntentService";
	     private static final String WAKELOCK_KEY = "GCM_LIB";
	     private static PowerManager.WakeLock sWakeLock;
	 60 private static final Object LOCK = GCMBaseIntentService.class;
	     private final String mSenderId;
	 65 private static int sCounter = 0;
	     
	 67 private static final Random sRandom = new Random();
	     
	 69 private static final int MAX_BACKOFF_MS = (int) TimeUnit.SECONDS
			.toMillis(3600L);
	     
	 73 private static final String TOKEN = Long.toBinaryString(sRandom
			.nextLong());
	     private static final String EXTRA_TOKEN = "token";

	     
	     protected GCMBaseIntentService(String senderId)
	     {
		 83 super("GCMIntentService-" + senderId + "-" + ++sCounter);
		 84 this.mSenderId = senderId;
		     }

	     
	     protected abstract void onMessage(Context paramContext,
			Intent paramIntent);

	     
	     protected void onDeletedMessages(Context context, int total)
	     {
		     }

	     
	     protected boolean onRecoverableError(Context context, String errorId)
	     {
		 118 return true;
		     }

	     
	     protected abstract void onError(Context paramContext,
			String paramString);

	     
	     protected abstract void onRegistered(Context paramContext,
			String paramString);

	     
	     protected abstract void onUnregistered(Context paramContext,
			String paramString);

	     
	     public final void onHandleIntent(Intent intent)
	     {
		     try
		     {
			 150 Context context = getApplicationContext();
			 151 String action = intent.getAction();
			 152 if (action
					.equals("com.google.android.c2dm.intent.REGISTRATION")) {
				 153 handleRegistration(context, intent);
				 154 } else if (action
					.equals("com.google.android.c2dm.intent.RECEIVE"))
			     {
				 156 String messageType = intent
						.getStringExtra("message_type");
				     
				 158 if (messageType != null) {
					 159 if (messageType.equals("deleted_messages")) {
						 160 String sTotal = intent
								.getStringExtra("total_deleted");
						     
						 162 if (sTotal != null) {
							     try {
								 164 int total = Integer.parseInt(sTotal);
								 165 ShowLog.showLogVerbose("GCMBaseIntentService",
										"Received deleted messages notification: "
												+ total);
								     
								 167 onDeletedMessages(context, total);
								     } catch (NumberFormatException e) {
								 169 ShowLog.showLogError("GCMBaseIntentService",
										"GCM returned invalid number of deleted messages: "
												+ sTotal);
								     }
							     }
						     }
					     else
					     {
						 175 ShowLog.showLogError("GCMBaseIntentService",
								"Received unknown special message: "
										+ messageType);
						     }
					     }
				     else
					 179 onMessage(context, intent);
				     }
			 181 else if (action
					.equals("com.google.android.gcm.intent.RETRY")) {
				 182 String token = intent.getStringExtra("token");
				 183 if (!TOKEN.equals(token))
				     {
					 186 ShowLog.showLogError("GCMBaseIntentService",
							"Received invalid token: " + token);
					     return;
					     }
				 190 if (GCMRegistrar.isRegistered(context))
					 191 GCMRegistrar.internalUnregister(context);
				     else {
					 193 GCMRegistrar.internalRegister(context,
							new String[] { this.mSenderId });
					     }
				     
				     }
			     
			     }
		     finally
		     {
			 203 synchronized (LOCK)
			     {
				 205 if (sWakeLock != null) {
							ShowLog.showLogVerbose("GCMBaseIntentService", "Releasing wakelock");
							if (sWakeLock.isHeld()) {
								sWakeLock.release();
								ShowLog.showLogVerbose("GCMBaseIntentService", "----Released wakelock");
							}
					     }
				     else {
					 210 ShowLog.showLogError("GCMBaseIntentService",
							"Wakelock reference is null");
					     }
				     }
			     }
		     }

	     
	     static void runIntentInService(Context context, Intent intent,
			String className)
	     {
		 225 synchronized (LOCK) {
			 226 if (sWakeLock == null)
			     {
				 228 PowerManager pm = (PowerManager) context
						.getSystemService("power");
				     
				 230 sWakeLock = pm.newWakeLock(1, "GCM_LIB");
				     }
			     }
		     
		 234 ShowLog.showLogVerbose("GCMBaseIntentService", "Acquiring wakelock");
		 235 sWakeLock.acquire();
		 236 intent.setClassName(context, className);
		 237 context.startService(intent);
		     }

	     
	     private void handleRegistration(Context context, Intent intent) {
		 241 String registrationId = intent.getStringExtra("registration_id");
		 242 String error = intent.getStringExtra("error");
		 243 String unregistered = intent.getStringExtra("unregistered");
		 244 ShowLog.showLogDebug("GCMBaseIntentService",
				"handleRegistration: registrationId = " + registrationId
						+ ", error = " + error + ", unregistered = "
						+ unregistered);
		     
		 248 if (registrationId != null) {
			 249 GCMRegistrar.resetBackoff(context);
			 250 GCMRegistrar.setRegistrationId(context, registrationId);
			 251 onRegistered(context, registrationId);
			 252 return;
			     }
		     
		 256 if (unregistered != null)
		     {
			 258 GCMRegistrar.resetBackoff(context);
			 259 String oldRegistrationId = GCMRegistrar.clearRegistrationId(context);
			     
			 261 onUnregistered(context, oldRegistrationId);
			 262 return;
			     }
		     
		 266 ShowLog.showLogDebug("GCMBaseIntentService", "Registration error: " + error);
		     
		 268 if ("SERVICE_NOT_AVAILABLE".equals(error)) {
			 269 boolean retry = onRecoverableError(context, error);
			 270 if (retry) {
				 271 int backoffTimeMs = GCMRegistrar.getBackoff(context);
				 272 int nextAttempt = backoffTimeMs / 2 + sRandom.nextInt(backoffTimeMs);
				     
				 274 ShowLog.showLogDebug("GCMBaseIntentService",
						"Scheduling registration retry, backoff = "+ nextAttempt + " (" + backoffTimeMs + ")");
				
				 276 Intent retryIntent = new Intent("com.google.android.gcm.intent.RETRY");
				     
				 278 retryIntent.putExtra("token", TOKEN);
				 279 PendingIntent retryPendingIntent = PendingIntent.getBroadcast(context, 0, retryIntent, 0);
				     
				 281 AlarmManager am = (AlarmManager) context.getSystemService("alarm");
				     
				 283 am.set(3, SystemClock.elapsedRealtime() + nextAttempt,retryPendingIntent);
				     
				 287 if (backoffTimeMs < MAX_BACKOFF_MS)
					 288 GCMRegistrar.setBackoff(context, backoffTimeMs * 2);
				     }
			     else {
				 291 ShowLog.showLogDebug("GCMBaseIntentService","Not retrying failed operation");
				     }
			     }
		     else {
			 295 onError(context, error);
			     }
		     }
	     
}


 * Location: D:\gcm.jar Qualified Name:
 * com.google.android.gcm.GCMBaseIntentService JD-Core Version: 0.6.0
 */