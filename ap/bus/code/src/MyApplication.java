package app.infobus;

//import java.util.Calendar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

//import android.app.AlarmManager;
import android.app.Application;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import app.infobus.service.DownloadService;
import app.infobus.utils.Constant;

//import app.infobus.utils.ULog;

public class MyApplication extends Application {
	public static MyApplication mInstance;
	private Tracker tracker;
	private RequestQueue mRequestQueue;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;
		// startService();
	}

	public static synchronized MyApplication getInstance() {
		return mInstance;
	}

	public Tracker getTrackerApp() {
		if (tracker == null)
			tracker = GoogleAnalytics.getInstance(this).getTracker(Constant.KEY_ANALYSIS);
		return tracker;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

//	public <T> void addToRequestQueue(Request<T> req, String tag) {
//		// set the default tag if tag is empty
//		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//		getRequestQueue().add(req);
//	}

	/**
	 * Start service using AlarmManager
	 */
	// private void startService() {
	// ULog.i(this.getClass(), "startService...");
	// Calendar cal = Calendar.getInstance();
	// cal.add(Calendar.SECOND, 10);
	// Intent intent = new Intent(this, DownloadService.class);
	//
	// PendingIntent pIntent = PendingIntent.getService(this, 0, intent, 0);
	// AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	//
	// alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15 * 1000, pIntent); // 5 minute
	// startService(new Intent(getBaseContext(), DownloadService.class));
	//
	// }
}
