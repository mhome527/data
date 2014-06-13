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
package puzzle.slider.vn;


import puzzle.slider.vn.gcm.GCMBaseIntentService;
import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.CustomSharedPreferences;
import puzzle.slider.vn.util.ShowLog;
import puzzle.slider.vn.util.Utility;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;


/**
 * {@link IntentService} responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(Constant.SENDER_ID);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {
//		long when = System.currentTimeMillis();
//		NotificationManager notificationManager = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(R.drawable.icon,
//				message, when);
//		String title = context.getString(R.string.app_name);
//		Intent notificationIntent = new Intent(context,
//				StartupApp.class);
////		notificationIntent.putExtra(Constant.IS_START_NOTICE, true);
//		// set intent so it does not start a new activity
//		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		PendingIntent intent = PendingIntent.getActivity(context, 0,
//				notificationIntent, 0);
//		notification.setLatestEventInfo(context, title, message, intent);
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		notificationManager.notify((int)System.currentTimeMillis(), notification);
	}

	

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		ShowLog.e(TAG, "onError: "+arg1);
	}

	@Override
	protected void onMessage(Context context, Intent data) {

		ShowLog.showLogDebug(TAG, "RECEIVED A MESSAGE: "+data.getStringExtra("message"));
		// Get the data from intent and send to notificaion bar
		if (null == data.getStringExtra("message")) {
			return;
		}
		
		try {
			if (Utility.isApplicationSentToBackground(getApplicationContext())) {
				generateNotification(context,data.getStringExtra("message"));
			}else{
				if (null != onMessageCome) {
					onMessageCome.onMessage(data.getStringExtra("message"));
				}
			}
			
		} catch (Exception e) {
			if(CustomSharedPreferences.getPreferences("isShowPrintstacktrace", false)){
				ShowLog.e(TAG, "receiver error :"+e.getMessage());
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		ShowLog.showLogDebug(TAG, "onRegistered = "+arg1);
//		RegisterDeviceDao dao = new RegisterDeviceDao(getApplicationContext());
//		dao.regist(arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		ShowLog.showLogDebug(TAG, "onUnRegistered = "+arg1);
	}
	
	
	private static OnMessageComming onMessageCome;
	public static void setOnMessageComming(OnMessageComming onMessageComming) {
		onMessageCome = onMessageComming;
	}
	public interface OnMessageComming{
		public void onMessage(String mess);
	}
	
}
