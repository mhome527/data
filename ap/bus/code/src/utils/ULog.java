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
			Log.i(obj.getClass().getSimpleName() + "-HuynhTD", msg);
	}

	public static void i(Class<?> obj, String msg) {
		if (Constant.bLog)
			Log.i(obj.getSimpleName() + "-HuynhTD", msg);
	}

	public static void e(Object obj, String msg) {
		if (Constant.bLog)
			Log.e(obj.getClass().getSimpleName() + "-HuynhTD", msg);
	}

	public static void e(Class<?> obj, String msg) {
		if (Constant.bLog)
			Log.e(obj.getSimpleName() + "-HuynhTD", msg);
	}
}
