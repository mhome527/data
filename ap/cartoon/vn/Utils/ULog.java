package cartoon.youtube.vn.Utils;

import android.util.Log;

import cartoon.youtube.vn.BuildConfig;
import cartoon.youtube.vn.Constant;

public class ULog {
	public static void i(Object obj, String msg) {
		if (BuildConfig.DEBUG)
			Log.i(obj.getClass().getSimpleName() + "-YYY", msg);
	}

	public static void i(Class<?> obj, String msg) {
		if (BuildConfig.DEBUG)
			Log.i(obj.getSimpleName() + "-YYY", msg);
	}

	public static void e(Object obj, String msg) {
		if (BuildConfig.DEBUG)
			Log.e(obj.getClass().getSimpleName() + "-YYY", msg);
	}

	public static void e(Class<?> obj, String msg) {
		if (BuildConfig.DEBUG)
			Log.e(obj.getSimpleName() + "-YYY", msg);
	}
}
