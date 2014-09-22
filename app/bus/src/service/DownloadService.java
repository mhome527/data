package app.infobus.service;

//import java.util.List;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
import android.app.Service;
//import android.content.Context;
import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
import android.os.IBinder;
//import android.widget.RemoteViews;
//import app.infobus.BuildConfig;
//import app.infobus.R;
//import app.infobus.dropbox.DownloadJson;
//import app.infobus.entity.AdEntity;
//import app.infobus.utils.Common;
//import app.infobus.utils.Constant;
//import app.infobus.utils.ULog;

public class DownloadService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// getCacheDir().getAbsoluteFile();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		ULog.i(this, "onStartCommand startId:" + startId);

		// download json
//		new DownloadJson(this) {
//			@Override
//			protected void onPostExecute(Boolean result) {
//				super.onPostExecute(result);
//
//				try {
//					if (result != null && result) {
//						sendNotification(DownloadService.this.getApplicationContext());
//					} else {
//						ULog.i(DownloadService.class, "Not change json");
//					}
//				} catch (Exception e) {
//					ULog.e(DownloadJson.class, "Read json error; " + e.getMessage());
//					if (BuildConfig.DEBUG)
//						e.printStackTrace();
//				}
//
//			}
//
//		}.execute();

		return super.onStartCommand(intent, flags, startId);
	}


	// private void publishResults(String filename) {
	// Intent intent = new Intent(Constant.BROADDCAST_RELOAD);
	// intent.putExtra(Constant.INTENT_FILE, filename);
	// sendBroadcast(intent);
	// }
	//
	// private void updateDB() {
	// CartoonEntity entity;
	// DaoMaster daoMaster;
	// CartoonDao dao;
	// entity = (CartoonEntity) Common.getObjectJsonPath(CartoonEntity.class, getCacheDir().getAbsoluteFile() + "/" + Constant.JSON_FILE_NAME);
	// if (entity != null && entity.list != null) {
	// ULog.i(DownloadJson.class, "list size:" + entity.list.size());
	//
	// ULog.i(this, "Service Insert DB");
	//
	// daoMaster = ((MyApplication) DownloadService.this.getApplicationContext()).daoMaster;
	// dao = daoMaster.newSession().getCartoonDao();
	//
	// // DaoSession mDaoSession = daoMaster.newSession();
	//
	// for (Cartoon cartoon : entity.list) {
	// // mDaoSession.insertOrReplace(cartoon);
	// dao.insertOrReplace(cartoon);
	// }
	// publishResults(Constant.JSON_FILE_NAME);
	//
	// } else
	// ULog.e(DownloadJson.class, "Read json error; can't download file json");
	// }
	//
//	@SuppressWarnings("deprecation")
//	private void sendNotification(Context context) {
//		AdEntity entity;
//		int icon;
//		ULog.i(DownloadService.class, "sendNotification...");
//
//		try {
//			entity = (AdEntity) Common.getObjectJsonPath(AdEntity.class, getCacheDir().getAbsoluteFile() + "/"
//					+ Constant.JSON_AD);
//			if (entity != null) {
//				ULog.i(DownloadService.class, "sendNotification: " + entity.packageName + "; title" + entity.title);
//
//				if (getAppINstalled(entity.packageName))
//					return;
//
//				if (entity.icon.equals("icon_slider"))
//					icon = R.drawable.icon_slider;
//				else
//					icon = R.drawable.icon_new;
//				ULog.i(DownloadService.class, "sendNotification create 2 ");
//
//				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + entity.packageName));
//				// Open NotificationView.java Activity
//				PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//						PendingIntent.FLAG_CANCEL_CURRENT);
//
//				NotificationManager notificationManager = (NotificationManager) context
//						.getSystemService(context.NOTIFICATION_SERVICE);
//				Notification notification = new Notification(icon, entity.title, System.currentTimeMillis());
//
//				notification.flags = notification.FLAG_AUTO_CANCEL;
//				if (Build.VERSION.SDK_INT > 8) {
//					ULog.i(DownloadService.class, "create push 222");
//					RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
//					//
//					contentView.setImageViewResource(R.id.imgNotification, icon);
//					contentView.setTextViewText(R.id.tvTitle, entity.title);
//					notification.contentView = contentView;
//					notification.contentIntent = pendingIntent;
//					// notification.defaults = Notification.DEFAULT_VIBRATE;
//				} else {
//					notification.setLatestEventInfo(context, context.getString(R.string.new_app), entity.title,
//							pendingIntent);
//				}
//				ULog.i(DownloadService.class, "Push notification....");
//				notificationManager.notify(R.string.app_name, notification);
//			} else {
//				ULog.e(DownloadService.class, "Can't ad.json !!!!!!!!!!!!!!!!!!!");
//			}
//
//		} catch (Exception ex) {
//			ULog.e(DownloadService.class, "showNotification Error:" + ex.getMessage());
//		}
//	}

	//
	// public void Notification() {
	// AdEntity entity;
	// int icon;
	// ULog.i(DownloadService.class, "sendNotification");
	// entity = (AdEntity) Common.getObjectJsonPath(AdEntity.class, getCacheDir().getAbsoluteFile() + "/" + Constant.JSON_AD);
	// if (entity != null) {
	// ULog.i(DownloadService.class, "sendNotification: " + entity.packageName + "; title" + entity.title);
	//
	// if (getAppINstalled(entity.packageName))
	// return;
	//
	// if (entity.icon.equals("icon_slider"))
	// icon = R.drawable.icon_slider;
	// else if (entity.icon.equals("icon_cartoon"))
	// icon = R.drawable.icon_cartoon;
	// else
	// icon = R.drawable.icon_new;
	// ULog.i(DownloadService.class, "sendNotification create 2 ");
	//
	// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + entity.packageName));
	// PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	//
	// //Create Notification using NotificationCompat.Builder
	// NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
	// // Set Icon
	// .setSmallIcon(icon)
	// // Set Ticker Message
	// .setTicker(entity.title)
	// // Set Title
	// .setContentTitle(entity.title)
	// // Set Text
	// .setContentText(entity.title)
	// // Add an Action Button below Notification
	// // .addAction(R.drawable.ic_launcher, "Action Button", pIntent)
	// // Set PendingIntent into Notification
	// .setContentIntent(pIntent)
	// // Dismiss Notification
	// .setAutoCancel(true);
	//
	// // Create Notification Manager
	// NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	// // Build Notification with Notification Manager
	// notificationmanager.notify(0, builder.build());
	// }
	// }
	//
	//
//	private boolean getAppINstalled(String packageName) {
//		try {
//			final PackageManager pm = getPackageManager();
//			// get a list of installed apps.
//			List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//
//			for (ApplicationInfo packageInfo : packages) {
//				if (packageName.equals(packageInfo.packageName))
//					return true;
//				// ULog.i(this, "Installed package :" + packageInfo.packageName);
//				// ULog.i(this, "Source dir : " + packageInfo.sourceDir);
//				// ULog.i(this, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
//			}
//		} catch (Exception e) {
//			ULog.e(this, "getAppINstalled error:" + e.getMessage());
//		}
//
//		return false;
//	}
}