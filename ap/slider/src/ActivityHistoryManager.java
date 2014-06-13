package puzzle.slider.vn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import puzzle.slider.vn.util.ShowLog;
import android.app.Activity;


/**
 * @author 
 * @version 1.0
 * 
 *          Activity management history
 * 
 */
public class ActivityHistoryManager {

	@SuppressWarnings("unused")
	private final static String TAG = ActivityHistoryManager.class.getSimpleName();
	private final static int ACTIVITY_QUEUE_SIZE = 20;
	private final static long ACTIVITY_TIMEOUT = TimeUnit.SECONDS.toMillis(60 * 60 * 6);

	public static List<Activity> ActivityHistory = new ArrayList<Activity>() ;
	private static long lastModificationTime = 0;

	/**
	 * @author HuynhTD
	 * @see contractor
	 * 
	 */
	private ActivityHistoryManager() {
	}

	/**
	 * @author HuynhTD
	 * @param newActivity
	 * @see Add new Activity to ActivityHistory
	 * 
	 */
	public static synchronized void addNewActivity(Activity newActivity) {

		synchronized (ActivityHistory) {
			if (ACTIVITY_QUEUE_SIZE < ActivityHistory.size()) {
				Activity oldestActivity = ActivityHistory.remove(0);
				oldestActivity.finish();
			}
		}
		ActivityHistory.add(newActivity);
		lastModificationTime = System.currentTimeMillis();
	}

	/**
	 * @author HuynhTD
	 * @param removeActivity
	 * @see remove Activity from ActivityHistory
	 * 
	 */
	public static synchronized void removeFromActivityHistory(Activity removeActivity) {
		synchronized (ActivityHistory) {
			if (ActivityHistory.contains(removeActivity)) {
				ActivityHistory.remove(removeActivity);
			}

		}
		
	}

	/**
	 * @author HuynhTD
	 * 
	 * @see clear all Activity from ActivityHistory
	 * 
	 */
	public static void shutdownAllActivity() {
		
		ActivityHistory.clear();
//		ActivityHistory = null;

		lastModificationTime = System.currentTimeMillis();
	}
	/**
	 * @author HuynhTD
	 * 
	 * @see clear all Activity from ActivityHistory
	 * 
	 */
	public static void shutdownAllActivityBefore() {

		synchronized (ActivityHistory) {
			while(ActivityHistory.size()>1){
				Activity acrm =ActivityHistory.remove(0);
				acrm.finish();
			}
		}

		lastModificationTime = System.currentTimeMillis();
	}
	public static Activity peekLast (){
		synchronized (ActivityHistory) {
			int size  = ActivityHistory.size();
			if(size>0){
				return ActivityHistory.get(size-1);
			}
		}
		return null;
	}


	public static AbstractContentsActivity getLastAbstractContentActivityInHistory(){
		
		synchronized (ActivityHistory) {
			int size  = ActivityHistory.size();
			for(int i = size -1 ;size >=0;i++){
				Activity ac = ActivityHistory.get(i);
				if(ac instanceof AbstractContentsActivity)
					return (AbstractContentsActivity) ac;
			}
		}
		return null;
	}
	public static boolean confirmNoLongerOperation() {

		long duration = System.currentTimeMillis() - lastModificationTime;
		if (duration > ACTIVITY_TIMEOUT) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author HuynhTD
	 * @return boolean : true if ActivityHistory empty
	 * @see check activity
	 * 
	 */
	public static boolean isEmptyActivityHistory() {
		return null == ActivityHistory || ActivityHistory.isEmpty();
	}
	/**
	 * @author DucLH
	 * 
	 * @see Show all Activity History
	 * 
	 */
	public static void showAllActivityHistory() {
		int i = 0 ;
		for (Activity activity : ActivityHistory) {
			i += 1;
			ShowLog.i("ActivityHistoryManager", "backActivity name " + i + " : " + activity.getClass().getSimpleName());
		}
		
	}

	/**
	 * @author NhanNATC
	 * @remove activity by class
	 */
	
	public static void removeActivityByClassName(String activitySimpleClassName) {
		Activity removeActivity = null;
		for (Activity activity : ActivityHistory) {
			if (activity.getClass().getSimpleName().equals(activitySimpleClassName)) {
				removeActivity = activity;
				break;
			}						
		}
		if (null != removeActivity) {
			ActivityHistory.remove(removeActivity);
			removeActivity.finish();
			ShowLog.i("ActivityHistoryManager", "removeActivity name " + removeActivity);
		}
	}
}
