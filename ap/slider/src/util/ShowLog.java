package puzzle.slider.vn.util;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

/**
 * Show Log for debug
 * 
 * @author 
 * @version
 * @since
 */
public class ShowLog {

	// ShowLog Flag (Using turn on or turn off debug)
	private static final boolean IS_FLAG_DEBUG = false;
	private static final boolean IS_FLAG_ERROR = true;
	private static final boolean IS_FLAG_INFO = true;
	private static final boolean IS_FLAG_VERBOSE = false;
	private static final boolean IS_FLAG_WARN = false;
	private static final boolean IS_FLAG_OS = false;
	private static final boolean IS_FLAG_TOAST = false;
	
//	private static final boolean IS_FLAG_DEBUG = true;
//	private static final boolean IS_FLAG_ERROR = true;
//	private static final boolean IS_FLAG_INFO = true;
//	private static final boolean IS_FLAG_VERBOSE = true;
//	private static final boolean IS_FLAG_WARN = true;
//	private static final boolean IS_FLAG_OS = true;
//	private static final boolean IS_FLAG_TOAST = true;

	/**
	 * Contact value is 3 Show Log Debug. Data note flow page.
	 * 
	 * @param tag
	 *            Prefix
	 * @param message
	 *            Debug message
	 */
	public static void showLogDebug(final String tag, final String message) {
		if (IS_FLAG_DEBUG) {
			Log.d(tag + "YGO", message);
		}
	}

	/**
	 * Contact value is 6 Show Log Error. Data very important, use to start to end block or end point to redirect page
	 * 
	 * @param tag
	 *            Prefix
	 * @param message
	 *            Debug message
	 */
	public static void e(final String tag, final String message) {
		if (IS_FLAG_ERROR) {
			Log.e(tag + "YGO", message);
		}
	}

	/**
	 * Contact value is 4 Show Log Info. Data show result
	 * 
	 * @param tag
	 *            Prefix
	 * @param message
	 *            Debug message
	 */
	public static void i(final String tag, final String message) {
		if (IS_FLAG_INFO) {
			Log.i(tag + "YGO", message);
		}
	}

	/**
	 * Contact value is 2 Show Log Verbose. Data show result
	 * 
	 * @param tag
	 *            Prefix
	 * @param message
	 *            Debug message
	 */
	public static void showLogVerbose(final String tag, final String message) {
		if (IS_FLAG_VERBOSE) {
			Log.i(tag, message);
		}
	}

	/**
	 * Contact value is 2 Show Log Verbose. Data show result
	 * 
	 * @param tag
	 *            Prefix
	 * @param message
	 *            Debug message
	 */
	public static void v(final String tag, final String message) {
		if (IS_FLAG_VERBOSE) {
			Log.v(tag, message);
		}
	}

	/**
	 * Contact value is 5 Show Log Warn. Data show result
	 * 
	 * @param tag
	 *            Prefix
	 * @param message
	 *            Debug message
	 */
	public static void showLogWarn(final String tag, final String message) {
		if (IS_FLAG_WARN) {
			Log.i(tag, message);
		}
	}

	// /**
	// * Show Log Current Time
	// * @param tag Prefix
	// * @param message Message add more
	// */
	// public static void showLogTime(final String tag, final String message){
	// //System.currentTimeMillis
	// if(IS_FLAG_OS){
	// final Date date = new Date();
	// final String current = date.getMinutes() + ":" + date.getSeconds();
	// Log.e(tag, message + " :: Current time now: " + current + " (mm:ss)");
	// }
	// }

	/**
	 * Show Toast
	 * 
	 * @param context
	 *            Context of activity
	 * @param message
	 *            Message show toast
	 */
	public static void showToast(final Context context, final String message) {
		if (IS_FLAG_TOAST) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Show memory message
	 * 
	 * @param tag
	 * @param message
	 *            Message show
	 */
	public static void showLogMemory(final String tag, final String message) {
		// Note: android.os.Debug.getNativeHeapAllocatedSize()
		// Link Preferent: http://blog.javia.org/how-to-work-around-androids-24-mb-memory-limit/
		// http://organicandroid.blogspot.com/2010/12/totalfree-memory-on-android-apps-and.html
		// long maxMem = Runtime.getRuntime().maxMemory();

		// String className = ShowLog.getClass().getName();

		if (IS_FLAG_OS) {
			final long allSize = Debug.getNativeHeapAllocatedSize() / 1048576L; // Origin is BYTES, print out is KB //1024
			final long freeSize = Debug.getNativeHeapFreeSize() / 1048576L;
			final long size = Debug.getNativeHeapSize() / 1048576L;
			final long maxMem = Runtime.getRuntime().maxMemory() / 1048576L; // Max memory
			// Usage
			Log.e(tag, message + ":: Max Memory(" + maxMem + " ) Allocation(" + allSize + " )" + " Free(" + freeSize + " )" + " Usage(" + size + " )");
		}
	}
}
