package sjpn3.vn.Util;

import android.util.Log;
import sjpn3.vn.Constant;

public class ULog {
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
