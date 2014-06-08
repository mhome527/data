package sjpn4.vn.Util;

import java.io.InputStreamReader;
import java.io.Reader;

import sjpn4.vn.Constant;
import sjpn4.vn.R;
import sjpn4.vn.model.VocabularyModel;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.google.gson.Gson;

public class Common {
	public static Object getObjectJson(Context context) {
		Object obj = null;
		try {
			Reader reader = new InputStreamReader(context.getAssets().open(Constant.LEVEL));
			Gson gson = new Gson();
			obj = gson.fromJson(reader, VocabularyModel.class);
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

	public static void showDialogWifi(final Context context) {
//		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//
//		// Setting Dialog Title
////		alertDialog.setTitle("Confirm...");
//		
//		// Setting Dialog Message
//		alertDialog.setMessage("Do you want to go to wifi settings?");
//
//		// Setting Icon to Dialog
//		// alertDialog.setIcon(R.drawable.ic_launcher);
//
//		// Setting Positive "Yes" Button
//		alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//
//				// Activity transfer to wifi settings
//				context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
//			}
//		});
//
//		// Setting Negative "NO" Button
//		alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				// Write your code here to invoke NO event
//
//				dialog.cancel();
//			}
//		});
//
//		// Showing Alert Message
//		alertDialog.show();
		
		
		 // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);      
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
//        dialog.setTitle("connect to internet");
        
        dialog.show();
        
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        // if decline button is clicked, close the custom dialog
        btnYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
				dialog.dismiss();
			}
		});
        
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        // if decline button is clicked, close the custom dialog
        btnNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
}
