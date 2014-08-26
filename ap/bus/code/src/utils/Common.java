package app.infobus.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import app.infobus.entity.clsListData;
import app.infobus.entity.clsListName;

import com.google.gson.Gson;

public class Common {
	public static Object getObjectJson(Context context) {
		Object obj = null;
		try {
			Reader reader = new InputStreamReader(context.getAssets().open("n3"));
			Gson gson = new Gson();
			obj = gson.fromJson(reader, clsListData.class);
		} catch (Exception e) {
			ULog.e("Common", "getObjectJson Error:" + e.getMessage());
		}
		return obj;
	}

	public static Object getObjectJson(Context context, Class<?> cls, String name) {
		Object obj = null;
		try {
			Reader reader = new InputStreamReader(context.getAssets().open(name));
			Gson gson = new Gson();
			obj = gson.fromJson(reader, cls);
		} catch (Exception e) {
			ULog.e("Common", "getObjectJson 2 Error:" + e.getMessage());
		}
		return obj;
	}

	public static clsListData getDataHCM(Context context) {
		clsListData obj = null;
		try {
			Reader reader = new InputStreamReader(context.getAssets().open("HCM"));
			Gson gson = new Gson();
			obj = gson.fromJson(reader, clsListData.class);
		} catch (Exception e) {
			ULog.e("Common", "getObjectJson 2 Error:" + e.getMessage());
		}
		return obj;
	}
	
	public static clsListData getDataBus(Context context, String name) {
		clsListData obj = null;
		try {
			ULog.i("Common", "getDataBus:" + name);

			Reader reader = new InputStreamReader(context.getAssets().open(name));
			Gson gson = new Gson();
			obj = gson.fromJson(reader, clsListData.class);
		} catch (Exception e) {
			ULog.e("Common", "getObjectJson 2 Error:" + e.getMessage());
		}
		return obj;
	}

	public static clsListName getNameHCM(Context context) {
		clsListName obj = null;
		try {
			Reader reader = new InputStreamReader(context.getAssets().open("listStreet"));
			Gson gson = new Gson();
			obj = gson.fromJson(reader, clsListName.class);
		} catch (Exception e) {
			ULog.e("Common", "getObjectJson 2 Error:" + e.getMessage());
		}
		return obj;
	}
	
	public static clsListName getListName(Context context, String listStreet) {
		clsListName obj = null;
		try {
			Reader reader = new InputStreamReader(context.getAssets().open(listStreet));
			Gson gson = new Gson();
			obj = gson.fromJson(reader, clsListName.class);
		} catch (Exception e) {
			ULog.e("Common", "getObjectJson 2 Error:" + e.getMessage());
		}
		return obj;
	}
	
	public static Boolean isNetAvailable(Context con) {

		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	 public static Object getObjectJsonPath(Class<?> cls, String pathFile) {
	        Object obj = null;
	        try {
	            ULog.i(Common.class, "Read file json from local");
	            File yourFile = new File(pathFile);
	            FileInputStream stream = new FileInputStream(yourFile);
	            Reader reader = new InputStreamReader(stream);
	            Gson gson = new Gson();
	            obj = gson.fromJson(reader, cls);
	        } catch (Exception e) {
	            ULog.e("Common", "getObjectJsonPath 3 Error:" + e.getMessage());
	            e.printStackTrace();
	        }
	        return obj;
	    }
	
}
