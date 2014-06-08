package app.infobus.utils;

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
			LogUtil.e("Common", "getObjectJson Error:" + e.getMessage());
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
			LogUtil.e("Common", "getObjectJson 2 Error:" + e.getMessage());
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
			LogUtil.e("Common", "getObjectJson 2 Error:" + e.getMessage());
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
			LogUtil.e("Common", "getObjectJson 2 Error:" + e.getMessage());
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

	
}
