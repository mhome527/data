package app.infobus;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import app.infobus.entity.LocXY;
import app.infobus.model.GMapV2Direction;
import app.infobus.utils.ULog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapBus extends FragmentActivity {
	private GoogleMap mMap;
	private GMapV2Direction md;
	// private ArrayList<clsPathBus> arrPathBus;
	private int number;
	private ProgressBar progressMap;

	private PolylineOptions rectLineAllStart;
	private PolylineOptions rectLineAllBack;

	// private Handler loadMap;
	// LatLng pos1 = new LatLng(10.739956, 106.703260);
	// LatLng pos2 = new LatLng(10.759561, 106.698604);
	// LatLng pos3 = new LatLng(10.760994, 106.700385);
	//
	// LatLng pos4 = new LatLng(10.771239, 106.693411);
	// LatLng pos5 = new LatLng(10.775539, 106.698883);
	// LatLng pos6 = new LatLng(10.782368, 106.705041);

	// LatLng pos8 = new LatLng(10.8086801,106.6705759);
	// LatLng pos9 = new LatLng(10.8170503,106.680957);
	// LatLng pos10 = new LatLng(10.8236609,106.6816507);
	// LatLng pos11 = new LatLng(10.8618387,106.6743374);
	private int width;
	private int height;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_bus);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();

		progressMap = (ProgressBar) findViewById(R.id.progressMap);
		md = new GMapV2Direction();
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		// mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));

		// mMap.addMarker(new MarkerOptions().position(pos1).title("Start"));
		// mMap.addMarker(new MarkerOptions().position(pos2));
		// mMap.addMarker(new MarkerOptions().position(pos3));
		// //
		// mMap.addMarker(new MarkerOptions().position(pos6).visible(false));

		// mMap.addPolyline(rectLine);
		number = getIntent().getIntExtra("num", 0);
		TextView tvNum = (TextView) findViewById(R.id.tvNum);
		tvNum.setText(number + "");
		// Bundle extra = getIntent().getBundleExtra("extra");
		// arrPathBus = (ArrayList<clsPathBus>) extra.getSerializable("list_bus");
		// List<LocXY> listXY = new ArrayList<LocXY>();

		rectLineAllStart = new PolylineOptions().width(8).color(Color.GREEN);
		rectLineAllBack = new PolylineOptions().width(3).color(Color.RED);

		DrawStreet();
		// testDrawStreet();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// new DrawDirectoryAsk().execute(InfoBusActivity.arrPathBus.get(number).locS, InfoBusActivity.arrPathBus.get(number).locB);
		// setLocationXY(InfoBusActivity.arrPathBus.get(number).locS, true);
		// setLocationXY(InfoBusActivity.arrPathBus.get(number).locB, true);
	}

	// "lat":10.777100, "lng":106.705768}, {"lat":10.771697, "lng":106.706377}, {"lat":10.775091, "lng":106.701785},
	// {"lat":10.769705, "lng":106.696914}, {"lat":10.754545, "lng":106.678246}, {"lat":10.752373, "lng":106.666144},
	// {"lat":10.750873, "lng":106.658414}, {"lat":10.751439, "lng":106.651902}],

	private void testDrawStreet() {
		List<LocXY> listXY = new ArrayList<LocXY>();
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.777030, 106.705789), 14));
		listXY.add(new LocXY(10.777030, 106.705789));
		// listXY.add(new LocXY(10.773741, 106.706486));
		listXY.add(new LocXY(10.770839, 106.705722));
		listXY.add(new LocXY(10.769705, 106.696914));
		listXY.add(new LocXY(10.754545, 106.678246));
		listXY.add(new LocXY(10.752373, 106.666144));
		listXY.add(new LocXY(10.750873, 106.658414));
		listXY.add(new LocXY(10.751439, 106.651902));
		setLocationXY(listXY, true);
	}

	private void DrawStreet() {
		Handler loadMap = new Handler();
		loadMap.postDelayed(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				LatLng startL, endL, xy;
				LatLngBounds boundXY;
				// ULog.e(this, "loading....");
				startL = new LatLng(InfoBusActivity.arrPathBus.get(number).locS.get(0).lat, InfoBusActivity.arrPathBus.get(number).locS
						.get(0).lng);
				endL = new LatLng(InfoBusActivity.arrPathBus.get(number).locB.get(0).lat, InfoBusActivity.arrPathBus.get(number).locB
						.get(0).lng);
				if (InfoBusActivity.arrPathBus.get(number).locS.size() > 3)
					xy = new LatLng(InfoBusActivity.arrPathBus.get(number).locS.get(3).lat, InfoBusActivity.arrPathBus.get(number).locS
							.get(3).lng);
				else
					xy = new LatLng(InfoBusActivity.arrPathBus.get(number).locS.get(0).lat, InfoBusActivity.arrPathBus.get(number).locS
							.get(0).lng);

				// boundXY = createLatLngBoundsObject(startL, endL);
				// mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundXY, width, height, 150));

				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(xy, 12));

				mMap.addMarker(new MarkerOptions().position(startL).title("Xuat phat")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_green)));

				mMap.addMarker(new MarkerOptions().position(endL).title("Luoc ve")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_orange)));

				// new DrawStreetAsk().execute(InfoBusActivity.arrPathBus.get(number).locS, InfoBusActivity.arrPathBus.get(number).locB);
				new DrawStreetAsk(true).execute(InfoBusActivity.arrPathBus.get(number).locS);
				new DrawStreetAsk(false).execute(InfoBusActivity.arrPathBus.get(number).locB);
				// setLocationXY(InfoBusActivity.arrPathBus.get(number).locS, true);
				// setLocationXY(InfoBusActivity.arrPathBus.get(number).locS, true);
				// setLocationXY(InfoBusActivity.arrPathBus.get(number).locB, false);
			}
		}, 500);
		// if (InfoBusActivity.arrPathBus != null) {
		// // new DrawDirectoryAsk().execute(InfoBusActivity.arrPathBus.get(number).locS, InfoBusActivity.arrPathBus.get(number).locB);
		// // setLocationXY(InfoBusActivity.arrPathBus.get(number).locS, true);
		// // setLocationXY(InfoBusActivity.arrPathBus.get(number).locB, true);
		// } else {
		// ULog.e(this, "list data bus not found");
		// }
	}

	//
	private class DrawStreetAsk extends AsyncTask<List<LocXY>, Void, Boolean> {

		List<LocXY> lstLocXY[];

		private boolean start;

		public DrawStreetAsk(boolean start) {
			this.start = start;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected void onPostExecute(Boolean result) {
			// setLocationXY(lstLocXY[0], true);
			// setLocationXY(lstLocXY[1], false);
			super.onPostExecute(result);
			progressMap.setVisibility(View.GONE);

			ULog.i(MapBus.class, "draw map.. start:" + start);

			// for (PolylineOptions polyline : rectLineAllStart)
			if (start)
				mMap.addPolyline(rectLineAllStart);
			else {
				mMap.addPolyline(rectLineAllBack);
			}
			// mMap.addPolyline(rectLineAllBack);

			Handler loadMap = new Handler();
			loadMap.postDelayed(new Runnable() {

				@Override
				public void run() {
					// for (PolylineOptions polyline : rectLineAllBack)
					// mMap.addPolyline(rectLineAllBack);
					ULog.i(MapBus.class, "reDraw start:" + start);
					if (start)
						mMap.addPolyline(rectLineAllStart);
					else {
						mMap.addPolyline(rectLineAllBack);
					}
				}
			}, 2000);

			// for (PolylineOptions polyline : rectLineAllBack)
			// mMap.addPolyline(polyline);
		}

		@Override
		protected Boolean doInBackground(List<LocXY>... lstLocXY) {
			try {
				// md = new GMapV2Direction();
				// this.lstLocXY = lstLocXY;
				setLocationXY(lstLocXY[0], start);
				// setLocationXY(lstLocXY[1], false);
				// setLocationXY(lstLocXY[1], false);
			} catch (Exception e) {
				ULog.e(MapBus.class, "Draw Error:" + e.getMessage());
				return false;
			}
			return true;
		}
	}

	private void setLocationXY(List<LocXY> locXY, boolean start) {
		LocXY loc;
		LocXY locTmp = null;
		LatLng latX, latY;
		ULog.i(MapBus.class, "setLocationXY....start:" + start);

		for (int i = 0; i < locXY.size(); i++) {
			loc = locXY.get(i);
			if (i == 0) {
				locTmp = loc;
				// mMap.addMarker(new MarkerOptions().position(new LatLng(loc.lat, loc.lng)).title("Xuat phat")
				// .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_orange)));
				// mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.lat, loc.lng), 14));
			} else {
				ULog.i(this, "location loc1:" + locTmp.lat + ", " + locTmp.lng + "; loc2:" + loc.lat + ", " + loc.lng);
				latX = new LatLng(locTmp.lat, locTmp.lng);
				latY = new LatLng(loc.lat, loc.lng);
				locTmp = loc;
				// add
				drawMap2(latX, latY, start);
				// drawMap(latX, latY, start);
				// drawStreet(latX, latY, start);

				if (i == locXY.size() - 1) {
					// mMap.addMarker(new MarkerOptions().position(new LatLng(loc.lat, loc.lng)).title("End buyt"));
					// mMap.addMarker(new MarkerOptions().position(new LatLng(loc.lat, loc.lng)).title("tro ve")
					// .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_orange)));
				}
			}
		}
		// setLocationLat(new)
	}

	private void drawMap(LatLng lX, LatLng lY, boolean start) {
		PolylineOptions rectLine;
		Document doc = md.getDocument(lX, lY, GMapV2Direction.MODE_DRIVING);

		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		if (start)
			rectLine = new PolylineOptions().width(8).color(Color.BLUE);
		else
			rectLine = new PolylineOptions().width(3).color(Color.RED);
		//
		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}
		mMap.addPolyline(rectLine);
	}

	// ////////////////////// new

	private void drawMap2(LatLng lX, LatLng lY, boolean start) {
		PolylineOptions rectLine;
		Document doc = md.getDocument(lX, lY, GMapV2Direction.MODE_DRIVING);

		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		// if (start)
		// rectLine = new PolylineOptions().width(8).color(Color.BLUE);
		// else
		// rectLine = new PolylineOptions().width(3).color(Color.RED);
		//
		if (start) {
			for (int i = 0; i < directionPoint.size(); i++) {
				rectLineAllStart.add(directionPoint.get(i));
			}
		} else {
			for (int i = 0; i < directionPoint.size(); i++)
				rectLineAllBack.add(directionPoint.get(i));
		}

		// if (start)
		// rectLineAllStart.add(rectLine);
		// else
		// rectLineAllBack.add(rectLine);
		// mMap.addPolyline(rectLine);
	}

	// private void drawStreet(LatLng lX, LatLng lY, boolean start){
	// new DrawShootStreet(start).execute(lX, lY);
	// }

	private class DrawShootStreet extends AsyncTask<LatLng, Boolean, PolylineOptions> {
		private boolean start;

		public DrawShootStreet(boolean start) {
			this.start = start;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected PolylineOptions doInBackground(LatLng... params) {
			PolylineOptions rectLine;
			if (start)
				rectLine = new PolylineOptions().width(8).color(Color.BLUE);
			else
				rectLine = new PolylineOptions().width(3).color(Color.RED);

			Document doc = md.getDocument(params[0], params[0], GMapV2Direction.MODE_DRIVING);

			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			for (int i = 0; i < directionPoint.size(); i++) {
				rectLine.add(directionPoint.get(i));
			}
			return rectLine;
		}

		@Override
		protected void onPostExecute(PolylineOptions rectLine) {
			// TODO Auto-generated method stub
			super.onPostExecute(rectLine);
			ULog.i(MapBus.class, "finish, draw...");
			mMap.addPolyline(rectLine);

		}

	}

	private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation) {
		if (firstLocation != null && secondLocation != null) {
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			builder.include(firstLocation).include(secondLocation);

			return builder.build();
		}
		return null;
	}

}