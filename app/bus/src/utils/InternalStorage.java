package app.infobus.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import app.infobus.MapBus;
public final class InternalStorage {

	private InternalStorage() {
	}

	public static void writeObject(Activity context, String key, Object object) {
		ULog.i(MapBus.class, "***** writeObject save cache key:" + key);
		try {
//			String pathFilename;
//			File externalStorage = Environment.getExternalStorageDirectory();
//			String sdcardPath = externalStorage.getAbsolutePath() + File.separator + Constant.INFO_BUS + File.separator;
//			pathFilename = sdcardPath + key;
//			Utility.createDirIfNotExists(sdcardPath);
			
			
//			FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//			oos.writeObject(object);
//			oos.close();
//			fos.close();
		} catch (Exception e) {
			ULog.e(InternalStorage.class, "writeObject error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public static Object readObject(Activity context, String key) {
		Object object = null;
		
		try {
//			FileInputStream fis = context.openFileInput(key);
//			ObjectInputStream ois = new ObjectInputStream(fis);
//			object = ois.readObject();
//			ULog.i(MapBus.class, "^^^^^ readObject read cache key:" + key);
		} catch (Exception e) {
			ULog.e(InternalStorage.class, "readObject error:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return object;
	}
}