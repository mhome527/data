package app.infobus.utils;

import java.io.File;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.infobus.R;

public class Utility {
	public static int parseInt(final String num) {
		try {
			return Integer.parseInt(num);
		} catch (final Exception e) {
			return -1;
		}
	}

	public static String ArrToString(String[] arr) {
		String str = "";
		if (arr == null || arr.length == 0)
			return "";
		if (arr.length == 1) {
			str = arr[0];
		} else {
			for (int i = 0; i < arr.length - 1; i++) {
				if (!arr[i].contains("ttt"))
					str += arr[i] + " - ";
			}
			str += arr[arr.length - 1];
		}
		return str;
	}

	public static AlertDialog dialogWifi(final Activity activity) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		// create layout for dialog
		LinearLayout layout = new LinearLayout(activity);
		LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(parms);
		layout.setGravity(Gravity.CLIP_VERTICAL);
		layout.setPadding(2, 2, 2, 2);

		TextView tv = new TextView(activity);
		tv.setText(activity.getString(R.string.connect_wifi));
		tv.setPadding(10, 30, 10, 30);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(16);

		// create layout for textview
		LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(tv, tvParams);

		builder.setView(layout);
		builder.setPositiveButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(activity.getString(R.string.turn_on_wifi), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.dismiss();
				activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		});
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}


	// Check if Internet Network is active
	public static boolean checkNetwork(Activity activity) {
		boolean wifiDataAvailable = false;
		boolean mobileDataAvailable = false;
		ConnectivityManager conManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
		for (NetworkInfo netInfo : networkInfo) {
			if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
				if (netInfo.isConnected())
					wifiDataAvailable = true;
			if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
				if (netInfo.isConnected())
					mobileDataAvailable = true;
		}
		return wifiDataAvailable || mobileDataAvailable;
	}
	
	public static boolean createDirIfNotExists(String path) {
		boolean ret = true;

		File file = new File(path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				ULog.e("Common", "Problem creating Image folder");
				ret = false;
			}
		}
		return ret;
	}
	
	public static void logPoint(ArrayList<LatLng> directionPoint, String number, boolean start ){
		
	}
}
