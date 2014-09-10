/*
 *  LogUtil.java
 *  Created on Nov 12, 2013
 */

package app.infobus.utils;

import android.util.Log;

public class ULog {
	public static boolean debug = false;

	public static void i(Object obj, String msg) {
		if (Constant.bLog)
			if (obj instanceof String)
				Log.i(obj + "-htd", msg);
			else
				Log.i(obj.getClass().getSimpleName() + "-htd", msg);

	}

	public static void i(Class<?> obj, String msg) {
		if (Constant.bLog)
			Log.i(obj.getSimpleName() + "-htd", msg);
	}

	public static void e(Object obj, String msg) {
		if (Constant.bLog)
			if (obj instanceof String)
				Log.e(obj + "-htd", msg);
			else
				Log.e(obj.getClass().getSimpleName() + "-htd", msg);
	}

	public static void e(Class<?> obj, String msg) {
		if (Constant.bLog)
			Log.e(obj.getSimpleName() + "-htd", msg);
	}
}
