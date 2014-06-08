/*
 *  LogUtil.java
 *  Created on Nov 12, 2013
 */

package app.infobus.utils;

import android.util.Log;

public class LogUtil {
	public static boolean debug = false;

	public static void i(String tag, String msg) {
		if (debug)
			Log.i(tag + "-busInfo", msg);
	}

	public static void e(String tag, String msg) {
		if (debug)
			Log.e(tag + "-busInfo", msg);
	}
}
