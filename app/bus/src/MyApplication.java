package app.infobus;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import app.infobus.service.DownloadService;
import app.infobus.utils.ULog;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
//		startService();
	}
	
	/**
     *  Start service using AlarmManager
     */
    private void startService() {
    	ULog.i(this.getClass(), "startService...");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(this, DownloadService.class);

        PendingIntent pIntent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15 * 1000, pIntent); // 5 minute
        startService(new Intent(getBaseContext(), DownloadService.class));

    }
}
