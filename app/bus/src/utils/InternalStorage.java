package app.infobus.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;
import app.infobus.MapBus;
import app.infobus.MyApplication;

public final class InternalStorage {

	private InternalStorage() {
	}

	public static void writeObject(Context context, String key, Object object) {
		ULog.i(MapBus.class, "***** writeObject save cache key:" + key);
		try {
			// String pathFilename;
			// File externalStorage = Environment.getExternalStorageDirectory();
			// String sdcardPath = externalStorage.getAbsolutePath() + File.separator + Constant.INFO_BUS + File.separator;
			// pathFilename = sdcardPath + key;
			// Utility.createDirIfNotExists(sdcardPath);

			// FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
			FileOutputStream fos = MyApplication.getInstance().getApplicationContext().openFileOutput(key, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			fos.close();
		} catch (Exception e) {
			ULog.e(InternalStorage.class, "writeObject error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public static Object readObject(Context context, String key) {
		Object object = null;

		try {
			// FileInputStream fis = context.openFileInput(key);
			FileInputStream fis = MyApplication.getInstance().getApplicationContext().openFileInput(key);
			ObjectInputStream ois = new ObjectInputStream(fis);
			object = ois.readObject();
			if (object.toString().length() == 0)
				return null;
			ULog.i(MapBus.class, "^^^^^ readObject read cache key:" + key);
		} catch (Exception e) {
			ULog.e(InternalStorage.class, "readObject error:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return object;
	}
}