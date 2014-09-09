package app.infobus;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
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
import app.infobus.entity.clsPolylineEntity;
import app.infobus.model.GMapV2Direction;
import app.infobus.model.PolylineCacheStore;
import app.infobus.utils.InternalStorage;
import app.infobus.utils.ULog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapBus_bk extends FragmentActivity {
	private GoogleMap mMap;
	private GMapV2Direction md;
	// private ArrayList<clsPathBus> arrPathBus;
	private int number;
	private ProgressBar progressMap;
	private ProgressBar progressMap2;

	private PolylineOptions rectLineAllStart;
	private PolylineOptions rectLineAllBack;

	// private Handler loadMap;
	// LatLng pos1 = new LatLng(10.739956, 106.703260);
	// LatLng pos2 = new LatLng(10.759561, 106.698604);
	// LatLng pos3 = new LatLng(10.760994, 106.700385);

	private int width;
	private int height;
	private boolean reloadStart = false;
	private boolean reloadBack = false;
	private LatLngBounds boundXY;
	private LatLng startL, endL;
	private PolylineCacheStore cacheStore;

	@SuppressWarnings({ "deprecation" })
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
		progressMap2 = (ProgressBar) findViewById(R.id.progressMap2);
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
		cacheStore = PolylineCacheStore.load(this, "sb");
		
		number = getIntent().getIntExtra("num", 0);

		TextView tvNum = (TextView) findViewById(R.id.tvNum);
		tvNum.setText(InfoBusActivity.arrPathBus.get(number).getNum());

		// Bundle extra = getIntent().getBundleExtra("extra");
		// arrPathBus = (ArrayList<clsPathBus>) extra.getSerializable("list_bus");
		// List<LocXY> listXY = new ArrayList<LocXY>();

		rectLineAllStart = new PolylineOptions().width(8).color(getResources().getColor(R.color.green));
		rectLineAllBack = new PolylineOptions().width(3).color(getResources().getColor(R.color.orange));

		DrawStreet();
		// testDrawStreet();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void testDrawStreet() {
		List<LocXY> listXY = new ArrayList<LocXY>();
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.770839, 106.705722), 14));
		listXY.add(new LocXY(10.777030, 106.705789));
		// listXY.add(new LocXY(10.773741, 106.706486));
		listXY.add(new LocXY(10.770839, 106.705722));
		listXY.add(new LocXY(10.769705, 106.696914));
		listXY.add(new LocXY(10.754545, 106.678246));
		listXY.add(new LocXY(10.752373, 106.666144));
		listXY.add(new LocXY(10.750873, 106.658414));
		listXY.add(new LocXY(10.751439, 106.651902));
		setLocationXY(listXY, true);
		// GroundOverlayOptions newarkMap = new GroundOverlayOptions()
		// .image(BitmapDescriptorFactory.fromResource(R.drawable.bus_green))
		// .anchor(1, 1)
		// .position(new LatLng(10.770839, 106.705722), 8600f, 6500f);
		//
		//
		// mMap.addGroundOverlay(newarkMap);

	}

	private void DrawStreet() {
		Handler loadMap = new Handler();
		loadMap.postDelayed(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {

				// ULog.e(this, "loading....");
				startL = new LatLng(InfoBusActivity.arrPathBus.get(number).locS.get(0).lat, InfoBusActivity.arrPathBus.get(number).locS
						.get(0).lng);
				endL = new LatLng(InfoBusActivity.arrPathBus.get(number).locB.get(0).lat, InfoBusActivity.arrPathBus.get(number).locB
						.get(0).lng);
				// if (InfoBusActivity.arrPathBus.get(number).locS.size() > 3)
				// xy = new LatLng(InfoBusActivity.arrPathBus.get(number).locS.get(3).lat, InfoBusActivity.arrPathBus.get(number).locS
				// .get(3).lng);
				// else
				// xy = new LatLng(InfoBusActivity.arrPathBus.get(number).locS.get(0).lat, InfoBusActivity.arrPathBus.get(number).locS
				// .get(0).lng);

				boundXY = createLatLngBoundsObject(startL, endL);
				mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundXY, width, height, 150));
				// mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(boundXY, 12));

				// mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(xy, 12));

				// mMap.addMarker(new MarkerOptions().position(startL).title("Xuat phat")
				// .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_green)));
				//
				// mMap.addMarker(new MarkerOptions().position(endL).title("Luoc ve")
				// .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_orange)));

				addMarkerStreet();
				// new DrawStreetAsk().execute(InfoBusActivity.arrPathBus.get(number).locS, InfoBusActivity.arrPathBus.get(number).locB);
				new DrawStreetAsk(true).execute(InfoBusActivity.arrPathBus.get(number).locS);
				new DrawStreetAsk(false).execute(InfoBusActivity.arrPathBus.get(number).locB);
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

	private void setLocationXY(List<LocXY> locXY, boolean start) {
		LocXY loc;
		LocXY locTmp = null;
		LatLng latX, latY;
		ULog.i(MapBus_bk.class, "setLocationXY....start:" + start);

		for (int i = 0; i < locXY.size(); i++) {
			loc = locXY.get(i);
			if (i == 0) {
				locTmp = loc;
			} else {
				// ULog.i(this, "location loc1:" + locTmp.lat + ", " + locTmp.lng + "; loc2:" + loc.lat + ", " + loc.lng);
				latX = new LatLng(locTmp.lat, locTmp.lng);
				latY = new LatLng(loc.lat, loc.lng);
				locTmp = loc;
				// add
				drawMap2(latX, latY, start);
				// drawMap(latX, latY, start);
				// drawStreet(latX, latY, start);

			}
		}
		// setLocationLat(new)
	}

	// private void drawMap(LatLng lX, LatLng lY, boolean start) {
	// PolylineOptions rectLine;
	// Document doc = md.getDocument(lX, lY, GMapV2Direction.MODE_DRIVING);
	//
	// ArrayList<LatLng> directionPoint = md.getDirection(doc);
	// if (start)
	// rectLine = new PolylineOptions().width(8).color(Color.BLUE);
	// else
	// rectLine = new PolylineOptions().width(3).color(Color.RED);
	// //
	// for (int i = 0; i < directionPoint.size(); i++) {
	// rectLine.add(directionPoint.get(i));
	// }
	// mMap.addPolyline(rectLine);
	// }

	// ////////////////////// new

	private void drawMap2(LatLng lX, LatLng lY, boolean start) {
		try {
			if (isFinishing())
				return;
			Document doc = md.getDocument(lX, lY, GMapV2Direction.MODE_DRIVING);

			if (doc == null) {
				ULog.e(MapBus_bk.class, "drawMap2 get document NULL!!!!!");
				return;
			}
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			if (directionPoint.size() == 0) {
				if (start)
					reloadStart = true;
				else
					reloadBack = true;
			}
//			ULog.i(MapBus.class, "drawMap2 directionPoin size:" + directionPoint.size() + "; start: " + start);
			if (start) {
				for (int i = 0; i < directionPoint.size(); i++) {
					rectLineAllStart.add(directionPoint.get(i));
				}
			} else {
				for (int i = 0; i < directionPoint.size(); i++)
					rectLineAllBack.add(directionPoint.get(i));
			}
		} catch (Exception e) {
			ULog.e(MapBus_bk.class, "drawMap2 error:" + e.getMessage());
			e.printStackTrace();
		}

	}

	//
	private class DrawStreetAsk extends AsyncTask<List<LocXY>, Void, Boolean> {
		private boolean start;

		public DrawStreetAsk(boolean start) {
			this.start = start;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (isFinishing())
				return;
			progressMap.setVisibility(View.GONE);
			if (rectLineAllBack != null && rectLineAllBack.getPoints().size() == 0)
				progressMap2.setVisibility(View.VISIBLE);

			mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundXY, width, height, 150));

			if (start) {
				mMap.addPolyline(rectLineAllStart);

				if (reloadStart) {
					ULog.i(MapBus_bk.class, "reload street start");
					reloadStart = false;
					rectLineAllStart = new PolylineOptions().width(8).color(getResources().getColor(R.color.green));
					new ReloadStreet(true).execute(InfoBusActivity.arrPathBus.get(number).locS);
				} else {
//					if (result) {
//						clsPolylineEntity entity = new clsPolylineEntity();
//						entity.rectLineAll = rectLineAllStart;
//						// save cache start
////						InternalStorage.writeObject(MapBus.this, number + "s", entity);
//						cacheStore.putData(number + "s", entity);
//					}
				}
			} else {
				mMap.addPolyline(rectLineAllBack);
				if (reloadBack) {
					ULog.i(MapBus_bk.class, "reload street back");
					reloadBack = false;
					rectLineAllBack = new PolylineOptions().width(3).color(getResources().getColor(R.color.orange));
					new ReloadStreet(false).execute(InfoBusActivity.arrPathBus.get(number).locB);
				} else {
					progressMap2.setVisibility(View.GONE);
//					if (result) {
//
//						clsPolylineEntity entity = new clsPolylineEntity();
//						entity.rectLineAll = rectLineAllStart;
//						// save cache back
////						InternalStorage.writeObject(MapBus.this, number + "b", entity);
//						cacheStore.putData(number + "b", entity);
//					}
				}
			}

		}

		@Override
		protected Boolean doInBackground(List<LocXY>... lstLocXY) {
			clsPolylineEntity entity;
			try {
//				if (start) {
//					//read cache
//					entity = (clsPolylineEntity) InternalStorage.readObject(MapBus.this, number + "s");
//					if (entity != null && entity.rectLineAll != null) {
//						ULog.i(MapBus.class, "Read cache successful (start)");
//						rectLineAllStart = entity.rectLineAll;
//						return false;
//					}
//				} else {
//					entity = (clsPolylineEntity) InternalStorage.readObject(MapBus.this, number + "b");
//					if (entity != null && entity.rectLineAll != null) {
//						ULog.i(MapBus.class, "Read cache successful (back)");
//						rectLineAllBack = entity.rectLineAll;
//						
//						return false;
//					}
//				}
				setLocationXY(lstLocXY[0], start);
				// setLocationXY(lstLocXY[1], false);
			} catch (Exception e) {
				ULog.e(MapBus_bk.class, "Draw Error:" + e.getMessage());
				return false;
			}
			return true;
		}
	}

	private class ReloadStreet extends AsyncTask<List<LocXY>, Void, Boolean> {
		private boolean start;

		public ReloadStreet(boolean start) {
			this.start = start;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMap2.setVisibility(View.VISIBLE);
			ULog.i(MapBus_bk.class, "ReloadStreet.... start:" + start);
		}

		@Override
		protected Boolean doInBackground(List<LocXY>... lstLocXY) {
			try {
				setLocationXY(lstLocXY[0], start);
			} catch (Exception e) {
				ULog.e(MapBus_bk.class, "Draw Error:" + e.getMessage());
				return false;
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (isFinishing())
				return;
			if (rectLineAllStart.getPoints().size() > 0 && rectLineAllBack.getPoints().size() > 0) {
				mMap.clear();
				addMarkerStreet();
				mMap.addPolyline(rectLineAllStart);
				mMap.addPolyline(rectLineAllBack);
			}

			if (start) {
				if (reloadStart) {
					ULog.i(MapBus_bk.class, "reload 2 street start");
					reloadStart = false;
					rectLineAllStart = new PolylineOptions().width(8).color(getResources().getColor(R.color.green));
					new ReloadStreet(true).execute(InfoBusActivity.arrPathBus.get(number).locS);
				} else {
					progressMap2.setVisibility(View.GONE);
				}
			} else {
				mMap.addPolyline(rectLineAllBack);
				if (reloadBack) {
					ULog.i(MapBus_bk.class, "reload 2 street back");
					reloadBack = false;
					rectLineAllBack = new PolylineOptions().width(3).color(getResources().getColor(R.color.orange));
					new ReloadStreet(false).execute(InfoBusActivity.arrPathBus.get(number).locB);
				} else {
					progressMap2.setVisibility(View.GONE);
				}
			}

			ULog.i(MapBus_bk.class, "ReloadStreet finish start:" + start);
		}
	}

	private void addMarkerStreet() {
		mMap.addMarker(new MarkerOptions().position(startL).title(MapBus_bk.this.getString(R.string.startPath2))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_green)));

		mMap.addMarker(new MarkerOptions().position(endL).title(MapBus_bk.this.getString(R.string.backPath2))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_orange)));
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