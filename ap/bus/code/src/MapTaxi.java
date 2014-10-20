package app.infobus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.infobus.R.id;
import app.infobus.entity.DataDistance;
import app.infobus.entity.PriceFace;
import app.infobus.model.GMapV2Direction;
import app.infobus.request.DistanceAPI;
import app.infobus.utils.Constant;
import app.infobus.utils.ULog;
import app.infobus.utils.Utility;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapTaxi extends BaseActivity implements OnClickListener{
	private GMapV2Direction md;
	private RelativeLayout llInfo;
	private TextView tvNext;
	private TextView tvMoneyKm;

	private ImageButton imgEditPrice;
	private GoogleMap mMap;
	private LatLng locStart;
	private LatLng locEnd;
	private int choice = 0;
	private ProgressDialog progressDialog;
	private boolean isDraw = false;
	private ImageButton imgClose;
	private long price = 16500;

	
	@Override
	protected int getViewLayoutId() {
		return R.layout.map_taxi;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		md = new GMapV2Direction();
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		tvNext = this.getViewChild(R.id.tvNext);
		llInfo = this.getViewChild(R.id.llInfo);
		imgClose = this.getViewChild(R.id.imgClose);
		llInfo.setVisibility(View.GONE);
		imgEditPrice = this.getViewChild(R.id.imgEditPrice);
		tvMoneyKm = MapTaxi.this.getViewChild(R.id.tvMoneyKm);

		price = BaseActivity.pref.getLongValue(price, Constant.PRICE_TAXI);

		setListenerView();
		setCenterMapOnMyLocation();
		// test();

	}

	// @Override
	// public void onWindowFocusChanged(boolean hasFocus) {
	// super.onWindowFocusChanged(hasFocus);
	// if (hasFocus) {
	// decorView.setSystemUiVisibility(
	// View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	// | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	// | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	// | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	// | View.SYSTEM_UI_FLAG_FULLSCREEN
	// | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
	// }

	// @Override
	// public void onWindowFocusChanged(boolean hasFocus) {
	// super.onWindowFocusChanged(hasFocus);
	// RelativeLayout mBaseLayout = this.getViewChild(id.rootView);
	// if (hasFocus) {
	// mBaseLayout.setSystemUiVisibility(
	// View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	// | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	// | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	// | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	// | View.SYSTEM_UI_FLAG_FULLSCREEN
	// | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
	// }

	private void test() {
		// View decorView = getWindow().getDecorView();
		// // Hide the status bar.
		// int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		// decorView.setSystemUiVisibility(uiOptions);
		// // Remember that you should never show the action bar if the
		// // status bar is hidden, so hide that too if necessary.
		// ActionBar actionBar = getActionBar();
		// actionBar.hide();
		RelativeLayout mBaseLayout = this.getViewChild(id.rootView);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			mBaseLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			mBaseLayout.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
	}

	private void setListenerView() {
		mMap.setMyLocationEnabled(true);
		mMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng loc) {
				if (choice == 0) {
					locStart = loc;
					addMarkerStreet();
				} else if (choice == 1) {
					locEnd = loc;
					addMarkerStreet();
				}

			}
		});

		tvNext.setOnClickListener(this);
		imgClose.setOnClickListener(this);
		imgEditPrice.setOnClickListener(this);
	}

	// float distance = (float) Math.sqrt((newX - oldX) * (newX - oldX) + (newY - oldY) * (newY - oldY));
	private void addMarkerStreet() {
		mMap.clear();
		if (locStart != null)
			mMap.addMarker(new MarkerOptions().position(locStart).title(MapTaxi.this.getString(R.string.startPath2))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_green)));
		if (locEnd != null)
			mMap.addMarker(new MarkerOptions().position(locEnd).title(MapTaxi.this.getString(R.string.backPath2))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_orange)));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvNext:
			if (choice == 0 && locStart != null)
				choice = 1; // choice location 2
			else if (choice == 1 && locEnd != null) {
				isDraw = true;
				new DrawStreetAsk(this).execute();
				choice = 2;
				getDistance();
			} else if (choice == 2 && locStart != null & locEnd != null) {
				if (!isDraw) {
					locStart = null;
					locEnd = null;
					choice = 0;
					mMap.clear();
				}
			} else {
				// //show msg
			}
			ULog.i(MapTaxi.class, "choice:" + choice);
			break;
		case R.id.imgClose:
			llInfo.setVisibility(View.GONE);
			break;
		case R.id.imgEditPrice:
			// edit price
			Utility.dialogPriceTaxi(MapTaxi.this, price, priceFace);
			
			
			break;
		}
	}

	PriceFace priceFace = new PriceFace() {
		
		@Override
		public void setPrice(long price) {
			tvMoneyKm.setText(price +"");
			BaseActivity.pref.putLongValue(price, Constant.PRICE_TAXI);
		}
	};
	
	private void getDistance() {
		new DistanceAPI(DataDistance.class) {

			@Override
			public void onResponse(DataDistance entity) {
				String status = "";
				ULog.i(MapTaxi.class, "Get onResponse successful " + entity.status);
				try {
					status = entity.rows.get(0).elements.get(0).status;
				} catch (Exception e) {
					ULog.e(MapTaxi.class, "Can't found street, Error:" + e.getMessage());
				}

				if (status.equals("OK")) {
					llInfo.setVisibility(View.VISIBLE);
					setData(entity);
				} else {
					// //show dialog
				}
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				ULog.e(MapTaxi.class, "onErrorResponse error:" + error.getMessage());
			}

			@Override
			public Map<String, String> getParamsAPI() {
				Map<String, String> map = new HashMap<String, String>();
				map.put(Constant.KEY_ORG, locStart.latitude + "," + locStart.longitude);
				map.put(Constant.KEY_DES, locEnd.latitude + "," + locEnd.longitude);
				return map;
			}
		};
	}

	private void setData(DataDistance entity) {
		TextView tvFrom = MapTaxi.this.getViewChild(R.id.tvFrom);
		TextView tvTo = MapTaxi.this.getViewChild(R.id.tvTo);
		TextView tvKm = MapTaxi.this.getViewChild(R.id.tvKm);
		TextView tvTime = MapTaxi.this.getViewChild(R.id.tvTime);
		TextView tvMoney = MapTaxi.this.getViewChild(R.id.tvMoney);
		// TextView tvFrom = MapTaxi.this.getViewChild(R.id.tvFrom);

		tvFrom.setText(entity.origin_addresses[0]);
		tvTo.setText(entity.destination_addresses[0]);
		tvKm.setText(entity.rows.get(0).elements.get(0).distance.text);
		tvTime.setText(entity.rows.get(0).elements.get(0).duration.text);
		tvMoneyKm.setText(price + "");
		tvMoney.setText(getAmountMomey(entity.rows.get(0).elements.get(0).distance.text, 10000));
	}

	@SuppressLint("DefaultLocale")
	private String getAmountMomey(String strUnit, double money) {
		double km = 0;
		try {
			if (strUnit.toUpperCase().contains("KM")) {
				km = Double.parseDouble(strUnit.toUpperCase().replaceAll("KM", "").trim());
			} else if (strUnit.toUpperCase().contains("M")) {
				km = Integer.parseInt(strUnit.toUpperCase().replaceAll("M", "").trim()) / 1000;
			}
			return (long) (km * money) + "";
		} catch (Exception e) {
			return "0";
		}
	}

	private void setCenterMapOnMyLocation() {
		LatLng myLocation;
		mMap.setMyLocationEnabled(true);

		Location location = mMap.getMyLocation();

		if (location != null) {
			myLocation = new LatLng(location.getLatitude(), location.getLongitude());
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
		}
	}

	private class DrawStreetAsk extends AsyncTask<Void, Void, Boolean> {
		PolylineOptions rectLine;
		private Activity activity;

		public DrawStreetAsk(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(activity);
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
			}

			if (!isFinishing()) {
				progressDialog.show();
			}

			rectLine = new PolylineOptions().width(5).color(getResources().getColor(R.color.green));
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			ArrayList<LatLng> directionPoint = null;
			Document doc;
			try {
				isDraw = false;
				if (isFinishing())
					return false;

				for (int i = 0; i < 20; i++) {
					doc = md.getDocument(locStart, locEnd, GMapV2Direction.MODE_DRIVING);
					directionPoint = md.getDirection(doc);
					if (directionPoint != null && directionPoint.size() > 0)
						break;
					else
						ULog.i(MapTaxi.class, "drawMap reload get location..." + i);

				}
				if (directionPoint == null || directionPoint.size() == 0) {
					return false;
				}

				rectLine.addAll(directionPoint);
				return true;

			} catch (Exception e) {
				ULog.e(MapTaxi.class, "drawMap error:" + e.getMessage());
				e.printStackTrace();
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!isFinishing()) {
				progressDialog.dismiss();
			}

			if (result)
				mMap.addPolyline(rectLine);
			else
				ULog.e(MapTaxi.class, "Can't draw map");

		}
	}

	// float distance = (float) Math.sqrt((newX - oldX) * (newX - oldX) + (newY - oldY) * (newY - oldY));

	private float getKm() {
		// return (float) Math.sqrt((locStart.latitude - locEnd.latitude) * (locStart.latitude - locEnd.latitude)
		// + (locStart.longitude - locEnd.longitude) * (locStart.longitude - locEnd.longitude));

		return 0;
	}
}
