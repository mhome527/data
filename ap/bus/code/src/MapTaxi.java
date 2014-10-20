package app.infobus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.infobus.entity.DataDistance;
import app.infobus.entity.PriceFace;
import app.infobus.model.GMapV2Direction;
import app.infobus.request.DistanceAPI;
import app.infobus.utils.Constant;
import app.infobus.utils.ULog;
import app.infobus.utils.Utility;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapTaxi extends BaseActivity implements OnClickListener {
	private GMapV2Direction md;
	private RelativeLayout llInfo;
	private TextView tvNext;
	private TextView tvPriceBegin;
	private TextView tvPrice1;
	private TextView tvPrice2;
	private TextView tvGuid1;
	private TextView tvGuid2;
	private TextView tvTiltePrice1;
	private TextView tvTiltePrice2;

	private TextView tvFrom;
	private TextView tvTo;
	private TextView tvKm;
	private TextView tvTime;
	private TextView tvMoney;

	private ImageButton imgEditPrice;
	private GoogleMap mMap;
	private LatLng locStart;
	private LatLng locEnd;
	private int choice = 0;
	private ProgressDialog progressDialog;
	private boolean isDraw = false;
	private ImageButton imgClose;
	private long price1 = 16500;
	private long price2 = 12500;
	private long priceBegin = 10500;
	private int kmLimit = 31;
	private Animation slideRight1;
	private Animation slideRight2;
	private Animation slideRight3;

	@Override
	protected int getViewLayoutId() {
		return R.layout.map_taxi;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		try {
			md = new GMapV2Direction();
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			tvNext = this.getViewChild(R.id.tvNext);
			llInfo = this.getViewChild(R.id.llInfo);
			imgClose = this.getViewChild(R.id.imgClose);
			llInfo.setVisibility(View.GONE);
			imgEditPrice = this.getViewChild(R.id.imgEditPrice);
			tvPriceBegin = MapTaxi.this.getViewChild(R.id.tvPriceBegin);
			tvPrice1 = MapTaxi.this.getViewChild(R.id.tvPrice1);
			tvPrice2 = MapTaxi.this.getViewChild(R.id.tvPrice2);

			tvFrom = MapTaxi.this.getViewChild(R.id.tvFrom);
			tvTo = MapTaxi.this.getViewChild(R.id.tvTo);
			tvKm = MapTaxi.this.getViewChild(R.id.tvKm);
			tvTime = MapTaxi.this.getViewChild(R.id.tvTime);
			tvMoney = MapTaxi.this.getViewChild(R.id.tvMoney);

			tvTiltePrice1 = MapTaxi.this.getViewChild(R.id.tvTiltePrice1);
			tvTiltePrice2 = MapTaxi.this.getViewChild(R.id.tvTiltePrice2);
			// tvMoney = MapTaxi.this.getViewChild(R.id.tvMoney);
			tvGuid1 = MapTaxi.this.getViewChild(R.id.tvGuid1);
			tvGuid2 = MapTaxi.this.getViewChild(R.id.tvGuid2);

			priceBegin = BaseActivity.pref.getLongValue(priceBegin, Constant.PRICE_BEGIN_TAXI);
			price1 = BaseActivity.pref.getLongValue(price1, Constant.PRICE1_TAXI);
			price2 = BaseActivity.pref.getLongValue(price2, Constant.PRICE2_TAXI);
			kmLimit = BaseActivity.pref.getIntValue(Constant.KM_DEFAULT, Constant.KM_TAXI);

			setListenerView();
			setCenterMapOnMyLocation();
			setAnimation();
			setInitData();
			// test();

			Utility.setScreenNameGA("MapTaxi");
			
			if (!Utility.checkGps(this))
				Utility.showDialogGPS(this);
			// ///////ad
			AdView adView = (AdView) this.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
		} catch (Exception e) {
			ULog.e(this, "onCreate error:" + e.getMessage());
		}
	}

	private void setInitData() {
		tvTiltePrice1.setText(String.format(getString(R.string.title_price1), kmLimit));
		tvTiltePrice2.setText(String.format(getString(R.string.title_price2), kmLimit));
	}

	private void setAnimation() {
		slideRight2 = AnimationUtils.loadAnimation(MapTaxi.this, R.anim.slide_out_right2);
		slideRight2.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				tvGuid2.setVisibility(View.VISIBLE);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// tvGuid2.setVisibility(View.GONE);
			}
		});

		slideRight1 = AnimationUtils.loadAnimation(MapTaxi.this, R.anim.slide_out_right);
		slideRight1.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tvGuid1.setVisibility(View.GONE);
				tvGuid2.startAnimation(slideRight2);
				// tvGuid2.setVisibility(View.VISIBLE);
			}
		});

		slideRight3 = AnimationUtils.loadAnimation(MapTaxi.this, R.anim.slide_out_right);
		slideRight3.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// tvGuid2.setVisibility(View.VISIBLE);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tvGuid2.setVisibility(View.GONE);
			}
		});
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
			if (choice == 0 && locStart != null) {
				choice = 1; // choice location 2
				// moveGuidToRight();
				tvGuid1.startAnimation(slideRight1);
			} else if (choice == 1 && locEnd != null) {
				isDraw = true;
				new DrawStreetAsk(this).execute();
				choice = 2;
				getDistance();
				// moveGuidToRight2() ;
				tvGuid2.startAnimation(slideRight3);

			} else if (choice == 2 && locStart != null & locEnd != null) {
				if (!isDraw) {
					llInfo.setVisibility(View.GONE);
					locStart = null;
					locEnd = null;
					choice = 0;
					mMap.clear();
					tvGuid1.setVisibility(View.VISIBLE);
					tvGuid2.setVisibility(View.GONE);

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
			Utility.dialogPriceTaxi(MapTaxi.this, price1, price2, priceBegin, kmLimit, priceFace);

			break;
		}
	}

	PriceFace priceFace = new PriceFace() {

		@Override
		public void setPrice(long priceBegin, long price1, long price2) {

			if (priceBegin > 0) {
				BaseActivity.pref.putLongValue(priceBegin, Constant.PRICE_BEGIN_TAXI);
				tvPriceBegin.setText(priceBegin + "");
			}

			if (price1 > 0) {
				BaseActivity.pref.putLongValue(price1, Constant.PRICE1_TAXI);
				tvPrice1.setText(price1 + "");
			}

			if (price2 > 0) {
				BaseActivity.pref.putLongValue(price2, Constant.PRICE2_TAXI);
				tvPrice2.setText(price2 + "");
			}

			MapTaxi.this.priceBegin = BaseActivity.pref.getLongValue(priceBegin, Constant.PRICE_BEGIN_TAXI);
			MapTaxi.this.price1 = BaseActivity.pref.getLongValue(price1, Constant.PRICE1_TAXI);
			MapTaxi.this.price2 = BaseActivity.pref.getLongValue(price2, Constant.PRICE2_TAXI);
			kmLimit = BaseActivity.pref.getIntValue(Constant.KM_DEFAULT, Constant.KM_TAXI);

			tvMoney.setText(getAmountMomey(tvKm.getText().toString()));
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
		String time;
		// TextView tvFrom = MapTaxi.this.getViewChild(R.id.tvFrom);

		tvFrom.setText(entity.origin_addresses[0]);
		tvTo.setText(entity.destination_addresses[0]);
		tvKm.setText(entity.rows.get(0).elements.get(0).distance.text);
		time = entity.rows.get(0).elements.get(0).duration.text;
		time = time.replaceAll("day", MapTaxi.this.getString(R.string.day)).replaceAll("hours", MapTaxi.this.getString(R.string.hour))
				.replaceAll("mins", MapTaxi.this.getString(R.string.mins));
		tvTime.setText(time);
		tvPriceBegin.setText(priceBegin + "");
		tvPrice1.setText(price1 + "");
		tvPrice2.setText(price2 + "");
		tvMoney.setText(getAmountMomey(entity.rows.get(0).elements.get(0).distance.text));
	}

	@SuppressLint("DefaultLocale")
	private String getAmountMomey(String strUnit) {
		double km = 0;
		long money = 0;
		try {
			if (strUnit.toUpperCase().contains("KM")) {
				km = Double.parseDouble(strUnit.toUpperCase().replaceAll("KM", "").replaceAll(",", "").trim());
			} else if (strUnit.toUpperCase().contains("M")) {
				km = Integer.parseInt(strUnit.toUpperCase().replaceAll("M", "").replaceAll(",", "").trim()) / 1000;
			}
			
			if (km <= 1)
				money = priceBegin;
			else if (km < kmLimit)
				money = (long) (priceBegin +  (double) ((km - 1) * price1));
			else
				money = (long) (priceBegin +  (double) ((kmLimit - 2) * price1) +  (double) ((km - (kmLimit - 1)) * price2));

			return money + "";
		} catch (Exception e) {
			ULog.e(MapTaxi.this, "getAmountMoney error:" + e.getMessage());
		}
		return "0";
	}

	private void setCenterMapOnMyLocation() {
		// LatLng myLocation;
		// mMap.setMyLocationEnabled(true);
		//
		// Location location = mMap.getMyLocation();
		//
		// if (location != null) {
		// myLocation = new LatLng(location.getLatitude(), location.getLongitude());
		// mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
		// }

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
		if (location != null) {
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(location.getLatitude(), location.getLongitude())) // Sets the center of the map to location user
					.zoom(14) // Sets the zoom
					// .bearing(90) // Sets the orientation of the camera to east
					// .tilt(40) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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
			else {
				ULog.e(MapTaxi.class, "Can't draw map");
				Toast.makeText(MapTaxi.this, MapTaxi.this.getString(R.string.not_found), Toast.LENGTH_LONG).show();
			}
		}
	}

}
