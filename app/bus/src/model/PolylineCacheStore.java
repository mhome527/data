package app.infobus.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import android.content.Context;
import app.infobus.BuildConfig;
import app.infobus.MapBus;
import app.infobus.entity.clsPolylineEntity;
import app.infobus.utils.ULog;

import com.github.ignition.support.cache.AbstractCache;

//import com.google.android.gms.maps.model.PolylineOptions;

public class PolylineCacheStore extends AbstractCache<String, clsPolylineEntity> {

	// private static final String CACHE_KEY = "bus";
	public String key = "sb";

	public PolylineCacheStore(String key) {
		super(key, 1, 60 * 24 * 365, 2);
		this.key = key;
	}

	public static PolylineCacheStore load(Context context, String key) {
		PolylineCacheStore store = new PolylineCacheStore(key);
		store.enableDiskCache(context, DISK_CACHE_INTERNAL);
		return store;
	}

	public clsPolylineEntity get(String key1) {
		return super.get(key);
	}

	public void putData(String key1, clsPolylineEntity polyline) {
		put(key, polyline);
	}

	@Override
	public String getFileNameForKey(String name) {
		ULog.i(PolylineCacheStore.class, "getFileNameForKey name:" + name);
		return key;
	}

	@Override
	protected clsPolylineEntity readValueFromDisk(File file) throws IOException {
		clsPolylineEntity polyline = null;
		BufferedInputStream istream = new BufferedInputStream(new FileInputStream(file));
		ObjectInput input = new ObjectInputStream(istream);

		try {
			ULog.e(PolylineCacheStore.class, "readValueFromDisk name:" + file.getName());
			polyline = (clsPolylineEntity) input.readObject();
			input.close();
			istream.close();
		} catch (ClassNotFoundException e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
			ULog.e(PolylineCacheStore.class, "readValueFromDisk Error:" + e.getMessage());
		}

		return polyline;
	}

	@Override
	protected void writeValueToDisk(File file, clsPolylineEntity polyline) throws IOException {
		try {
			ULog.e(PolylineCacheStore.class, "writeValueToDisk name:" + file.getName());
			BufferedOutputStream ostream = new BufferedOutputStream(new FileOutputStream(file));
			ObjectOutput output = new ObjectOutputStream(ostream);
			output.writeObject(polyline);
			output.close();
		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
			ULog.e(PolylineCacheStore.class, "writeValueToDisk Error:" + e.getMessage());
		}
	}

	// @Override
	// public String getFileNameForKey(Object arg0) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// protected Object readValueFromDisk(File arg0) throws IOException {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// protected void writeValueToDisk(File arg0, Object arg1) throws IOException {
	// // TODO Auto-generated method stub
	//
	// }

}
