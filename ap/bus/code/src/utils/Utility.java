package app.infobus.utils;

//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.maps.model.LatLng;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.infobus.BuildConfig;
import app.infobus.MyApplication;
import app.infobus.R;
import app.infobus.entity.PriceFace;

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

	public static AlertDialog dialogCallConfirm(Activity activity, String title, DialogInterface.OnClickListener lisOk,
			DialogInterface.OnClickListener lisCancel) {
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
		tv.setText(String.format(activity.getString(R.string.confirm_call_tx), title));
		tv.setPadding(10, 30, 10, 30);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(16);

		// create layout for textview
		LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(tv, tvParams);

		builder.setView(layout);
		builder.setPositiveButton(activity.getString(R.string.cancel), lisCancel);

		builder.setNegativeButton(activity.getString(R.string.call), lisOk);

		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	// /////
	public static void dialogPriceTaxi(final Context context, long price, final PriceFace priceFace) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_price_taxi);
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);

		final EditText edtPrice = (EditText) dialog.findViewById(R.id.edtPrice);
		Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
		Button btnSave = (Button) dialog.findViewById(R.id.btnSave);

		edtPrice.setHint(price + "");
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String strPrice;
				long price;
				strPrice = edtPrice.getText().toString().trim(); 
				if(!strPrice.equals("")){
					try{
						price = Long.parseLong(strPrice);
						priceFace.setPrice(price);
					}catch(Exception e){
						
					}
				}
				
				
			}
		});
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

	public static void logPoint(ArrayList<LatLng> directionPoint, String number, boolean start) {

	}

	/**
	 * set event tracking
	 * 
	 * @param category
	 *            : The event category
	 * @param label
	 *            : The event label
	 */
	public static void setEventGA(String action, String label) {
		try {
			if (!BuildConfig.DEBUG) {
				MyApplication mInstance = MyApplication.getInstance();
				Tracker tracker = mInstance.getTrackerApp();
				tracker.send(new HitBuilders.EventBuilder().setCategory("BUS").setAction(action).setLabel(label).build());
			}

		} catch (Exception e) {
			ULog.e("Common", "setEventGA Error:" + e.getMessage());
		}
	}

	public static void setScreenNameGA(String name) {
		// int count;
		try {
			if (!BuildConfig.DEBUG) {
				MyApplication mInstance = MyApplication.getInstance();
				Tracker tracker = mInstance.getTrackerApp();
				tracker.set(Fields.SCREEN_NAME, name);
				tracker.send(MapBuilder.createAppView().build());
			}
		} catch (Exception e) {
			ULog.e("Common", "setScreenNameGA Error:" + e.getMessage());
		}
	}

	public static boolean checkGps(Context context) {
		try {
			ULog.i("Common", "checkGPS");
			LocationManager myLocManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			if (myLocManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
				return true;
		} catch (Exception e) {
			ULog.e("Common", "checkGps error:" + e.getMessage());
		}
		return false;
	}

	public static int getResId(String variableName, Context context, Class<?> c) {

		try {
			Field idField = c.getDeclaredField(variableName);
			return idField.getInt(idField);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	// public static void showDialogGPS(final Activity context) {
	//
	// final Dialog dialog = new Dialog(context);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.dialog_gps);
	//
	// dialog.show();
	// dialog.setCanceledOnTouchOutside(false);
	// // LinearLayout lnNotice = (LinearLayout)dialog.findViewById(R.id.lnNotice);
	// TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
	// Button btnSettings = (Button) dialog.findViewById(R.id.btnSettings);
	// Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
	//
	// //
	// // lnNotice.getLayoutParams().width = width;
	// // lnNotice.getLayoutParams().height = height;
	// tvMsg.setText(String.format(context.getString(R.string.msg_alert_gps),context.getString(R.string.app_name)));
	// btnSettings.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View arg0) {
	// dialog.dismiss();
	// Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	// context.startActivity(gpsIntent);
	// }
	// });
	//
	// btnCancel.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View arg0) {
	// dialog.dismiss();
	// }
	// });
	//
	// }

	// public static void writeGAToSDFile(String filename, String data) {
	// String pathfile;
	// String aDataRow = "";
	// String aBuffer = "";
	//
	// try {
	//
	// pathfile = android.os.Environment.getExternalStorageDirectory() +"";
	// ULog.i("Common", "writeGAToSDFile Path file: " + pathfile);
	// File dir = new File(pathfile, Constant.INFO_BUS);
	// if (!dir.exists()) {
	// dir.mkdirs();
	// }
	//
	// File f = new File(dir + File.separator + filename);
	// //read file
	// if(f.exists()){
	// FileInputStream fIn = new FileInputStream(f);
	// BufferedReader myReader = new BufferedReader(
	// new InputStreamReader(fIn));
	//
	// while ((aDataRow = myReader.readLine()) != null) {
	// aBuffer += aDataRow + "\n";
	// }
	// myReader.close();
	// }
	// ////
	//
	// FileOutputStream fOut = new FileOutputStream(f);
	// OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	// // myOutWriter.append(data);
	// myOutWriter.write(aBuffer + data);
	// myOutWriter.close();
	// fOut.close();
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// ULog.e("Common", "FileNotFoundException Error:" + e.getMessage());
	//
	// } catch (Exception e) {
	// if (BuildConfig.DEBUG)
	// e.printStackTrace();
	// ULog.e("Common", "Exception Error:" + e.getMessage());
	// }
	//
	// }
	//
	// public static void deleteLogGAToSDFile(String filename) {
	// String pathfile;
	//
	// try {
	//
	// pathfile = android.os.Environment.getExternalStorageDirectory() +"";
	// ULog.i("Common", "deleteLogGAToSDFile Path file: " + pathfile);
	// File dir = new File(pathfile, ConstanstKey.LOG_FOLDER_NAME);
	// if (!dir.exists()) {
	// dir.mkdirs();
	// }
	//
	// File f = new File(dir + File.separator + filename);
	//
	// if(f.exists()){
	// f.delete();
	// }
	// } catch (Exception e) {
	// if (BuildConfig.DEBUG)
	// e.printStackTrace();
	// ULog.e("Common", "Exception Error:" + e.getMessage());
	// }
	// }
}
