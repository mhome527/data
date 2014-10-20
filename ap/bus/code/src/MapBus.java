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
import app.infobus.entity.clsPathBus;
import app.infobus.entity.clsPolylineEntity;
import app.infobus.model.GMapV2Direction;
import app.infobus.utils.Constant;
import app.infobus.utils.InternalStorage;
import app.infobus.utils.ULog;
import app.infobus.utils.Utility;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
	private ProgressBar progressMap2;

	public ArrayList<LatLng> listPointStart;
	public ArrayList<LatLng> listPointBack;

	private int width;
	private int height;
	private boolean reloadStart = false;
	private boolean reloadBack = false;
	private LatLngBounds boundXY;
	private LatLng startL, endL;
	private boolean isHcm = true;
	private ArrayList<clsPathBus> arrPathBus = null;

	// private PolylineCacheStore cacheStore;

	@SuppressWarnings({ "deprecation" })
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_bus);
		try {
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

			// cacheStore = PolylineCacheStore.load(this, "sb");

			number = getIntent().getIntExtra("num", 0);
			isHcm = getIntent().getBooleanExtra(Constant.KEY_CITY, true);

			if (isHcm)
				arrPathBus = MainFragment.arrPathBusHCM;
			else
				arrPathBus = MainFragment.arrPathBusHN;

			TextView tvNum = (TextView) findViewById(R.id.tvNum);
			tvNum.setText(arrPathBus.get(number).getNum());

			ULog.i(MapBus.class, "oncreate number:" + number + "; city:" + isHcm);
			DrawStreet();

			// ///////ad
			AdView adView = (AdView) this.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
			// GA
			if (isHcm) {
				Utility.setScreenNameGA("MapBus Ho Chi Minh");
				Utility.setEventGA(Constant.GA_HCM + "-Map", arrPathBus.get(number).getNum() + "");
			} else {
				Utility.setScreenNameGA("MapBus Ha Noi");
				Utility.setEventGA(Constant.GA_HN + "-Map", arrPathBus.get(number).getNum() + "");
			}

		} catch (Exception e) {
			ULog.e(MapBus.class, "onCreate error:" + e.getMessage());
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void DrawStreet() {
		Handler loadMap = new Handler();
		loadMap.postDelayed(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {

				// ULog.e(this, "loading....");
				startL = new LatLng(arrPathBus.get(number).locS.get(0).lat, arrPathBus.get(number).locS.get(0).lng);
				endL = new LatLng(arrPathBus.get(number).locB.get(0).lat, arrPathBus.get(number).locB.get(0).lng);

				boundXY = createLatLngBoundsObject(startL, endL);
				mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundXY, width, height, 150));

				addMarkerStreet();

				// new DrawStreetAsk().execute(MainFragment.arrPathBus.get(number).locS, MainFragment.arrPathBus.get(number).locB);

				new DrawStreetAsk(true).execute(arrPathBus.get(number).locS);
				new DrawStreetAsk(false).execute(arrPathBus.get(number).locB);

				drawMapTmp(arrPathBus.get(number).locS, true);
				drawMapTmp(arrPathBus.get(number).locB, false);

			}
		}, 500);

	}

	private void setLocationXY(List<LocXY> locXY, boolean start) {
		LocXY loc;
		LocXY locTmp = null;
		LatLng latX, latY;
		boolean bChange = false;
		if (start)
			listPointStart = new ArrayList<LatLng>();
		else
			listPointBack = new ArrayList<LatLng>();

		// ULog.i(MapBus.class, "setLocationXY....start:" + start +"; size:" + listPointStart.size() + ", size2:" + listPointBack.size());

		for (int i = 0; i < locXY.size(); i++) {
			loc = locXY.get(i);
			// ULog.i(this, "location loc1====, {'lat':" + loc.lat + ", 'lng':" + loc.lng +"},");

			if (i == 0) {
				locTmp = loc;
			} else {
				// ULog.i(this, "location loc1====, {'lat':" + locTmp.lat + ", 'lng':" + locTmp.lng + "; loc2:" + loc.lat + ", " + loc.lng);

				if (loc.lat < 0) {
					bChange = true;
					loc.lat = Math.abs(loc.lat);
				}
				ULog.i(this, "location loc1====, {'lat':" + locTmp.lat + ", 'lng':" + locTmp.lng + "; loc2:" + loc.lat + ", " + loc.lng
						+ "; bChange:" + bChange);

				latX = new LatLng(locTmp.lat, locTmp.lng);
				latY = new LatLng(loc.lat, loc.lng);

				locTmp = loc;
				// add
				if (bChange)
					drawMap2(latY, latX, start); // xu ly truong hop nguoc chieu, ko ve duoc
				else
					drawMap2(latX, latY, start);

			}
			bChange = false;
		}
		// setLocationLat(new)
	}

	// ////////////////////// new

	private void drawMap2(LatLng lX, LatLng lY, boolean start) {
		ArrayList<LatLng> directionPoint = null;
		Document doc;
		try {
			if (isFinishing())
				return;

			for (int i = 0; i < 20; i++) {
				doc = md.getDocument(lX, lY, GMapV2Direction.MODE_DRIVING);
				directionPoint = md.getDirection(doc);
				if (directionPoint != null && directionPoint.size() > 0)
					break;
				else
					ULog.i(MapBus.class, "drawMap2 reload get location..." + i);

			}
			if (directionPoint == null || directionPoint.size() == 0) {
				if (start)
					reloadStart = true;
				else
					reloadBack = true;
			}
			// ULog.i(MapBus.class, "drawMap2 directionPoin size:" + directionPoint.size() + "; start: " + start);
			if (start) {
				listPointStart.addAll(directionPoint);
			} else {
				listPointBack.addAll(directionPoint);
			}
		} catch (Exception e) {
			ULog.e(MapBus.class, "drawMap2 error:" + e.getMessage());
			e.printStackTrace();
		}

	}

	//
	private class DrawStreetAsk extends AsyncTask<List<LocXY>, Void, Boolean> {
		private boolean start;

		public DrawStreetAsk(boolean start) {
			this.start = start;
		}

		@Override
		protected Boolean doInBackground(List<LocXY>... lstLocXY) {
			clsPolylineEntity entity;
			ArrayList<LatLng> listPointTmp;
			try {
				if (start) {
					// read cache
					if (isHcm)
						entity = (clsPolylineEntity) InternalStorage.readObject(MapBus.this, arrPathBus.get(number).getNum() + "s");
					else
						entity = (clsPolylineEntity) InternalStorage.readObject(MapBus.this, arrPathBus.get(number).getNum() + "shn");

					if (entity != null) {
						listPointTmp = entity.getListPoint();
						if (listPointTmp != null && listPointTmp.size() > 0) {
							listPointStart = listPointTmp;
							ULog.i(MapBus.class, "Read cache successful (start)");
							return false;
						}
					}
				} else {
					if (isHcm)
						entity = (clsPolylineEntity) InternalStorage.readObject(MapBus.this, arrPathBus.get(number).getNum() + "b");
					else
						entity = (clsPolylineEntity) InternalStorage.readObject(MapBus.this, arrPathBus.get(number).getNum() + "bhn");
					if (entity != null) {
						listPointTmp = entity.getListPoint();

						if (listPointTmp != null && listPointTmp.size() > 0) {
							listPointBack = listPointTmp;
							ULog.i(MapBus.class, "Read cache successful (back)");
							return false;
						}
					}
				}
				setLocationXY(lstLocXY[0], start);
				// setLocationXY(lstLocXY[1], false);
			} catch (Exception e) {
				ULog.e(MapBus.class, "DrawStreetAsk Draw Error:" + e.getMessage());
				if (BuildConfig.DEBUG)
					e.printStackTrace();
				return false;
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Boolean result) {
			PolylineOptions rectLineAll;
			super.onPostExecute(result);

			if (isFinishing())
				return;
			try {
				progressMap.setVisibility(View.GONE);
				// if (listPointBack != null && listPointBack.size() == 0)
				progressMap2.setVisibility(View.VISIBLE);

				mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundXY, width, height, 150));

				if (start) {
					// ULog.i(MapBus.class, "Draw street start size: " + listPointStart.size());
					mMap.clear();
					addMarkerStreet();
					rectLineAll = new PolylineOptions().width(8).color(getResources().getColor(R.color.green));
					rectLineAll.addAll(listPointStart);
					mMap.addPolyline(rectLineAll);

					// ve duong ve (neu co)
					if (listPointBack != null && listPointBack.size() > 0) {
						rectLineAll = new PolylineOptions().width(3).color(getResources().getColor(R.color.orange));
						rectLineAll.addAll(listPointBack);
						mMap.addPolyline(rectLineAll);
					}

					if (reloadStart) {
						ULog.i(MapBus.class, "reload street start");
						reloadStart = false;
						// rectLineAllStart = new PolylineOptions().width(8).color(getResources().getColor(R.color.green));
						new ReloadStreet(true).execute(arrPathBus.get(number).locS);
					} else {
						if (result) {
							clsPolylineEntity entity = new clsPolylineEntity();
							// entity.listPoint = listPointStart;
							entity.setListPoint(listPointStart);
							// save cache start
							if (isHcm)
								InternalStorage.writeObject(MapBus.this, arrPathBus.get(number).getNum() + "s", entity);
							else
								InternalStorage.writeObject(MapBus.this, arrPathBus.get(number).getNum() + "shn", entity);
							// cacheStore.putData(number + "s", entity);
						}
						// logTemp(listPointStart, start);

					}
				} else {
					mMap.clear();
					addMarkerStreet();
					// ve duong di neu co
					if (listPointStart != null && listPointStart.size() > 0) {
						rectLineAll = new PolylineOptions().width(8).color(getResources().getColor(R.color.green));
						rectLineAll.addAll(listPointStart);
						mMap.addPolyline(rectLineAll);
					}
					// ULog.i(MapBus.class, "Draw street back size: " + listPointBack.size());
					rectLineAll = new PolylineOptions().width(3).color(getResources().getColor(R.color.orange));
					rectLineAll.addAll(listPointBack);
					mMap.addPolyline(rectLineAll);

					if (reloadBack) {
						ULog.i(MapBus.class, "reload street back");
						reloadBack = false;
						// chua lay het toa do thi lay lai toa do
						// rectLineAllBack = new PolylineOptions().width(3).color(getResources().getColor(R.color.orange));
						new ReloadStreet(false).execute(arrPathBus.get(number).locB);
					} else {
						progressMap2.setVisibility(View.GONE);
						if (result) {
							clsPolylineEntity entity = new clsPolylineEntity();
							// entity.listPoint = listPointBack;
							entity.setListPoint(listPointBack);
							// save cache back
							if (isHcm)
								InternalStorage.writeObject(MapBus.this, arrPathBus.get(number).getNum() + "b", entity);
							else
								InternalStorage.writeObject(MapBus.this, arrPathBus.get(number).getNum() + "bhn", entity);
							// cacheStore.putData(number + "b", entity);
						}
						// logTemp(listPointStart, start);
					}
				}
			} catch (Exception e) {
				if (BuildConfig.DEBUG)
					e.printStackTrace();
			}

		}

	}

	private void drawMapTmp(List<LocXY> locTmp, boolean b) {
		ArrayList<LatLng> listPoint = new ArrayList<LatLng>();
		LatLng xy;
		PolylineOptions rectLineAll;
		if (b) {
			rectLineAll = new PolylineOptions().width(8).color(getResources().getColor(R.color.green));
		} else
			rectLineAll = new PolylineOptions().width(3).color(getResources().getColor(R.color.orange));

		for (LocXY loc : locTmp) {
			xy = new LatLng(Math.abs(loc.lat), loc.lng);
			listPoint.add(xy);
		}

		rectLineAll.addAll(listPoint);
		mMap.addPolyline(rectLineAll);
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
			ULog.i(MapBus.class, "ReloadStreet.... start:" + start);
		}

		@Override
		protected Boolean doInBackground(List<LocXY>... lstLocXY) {
			try {
				setLocationXY(lstLocXY[0], start);
			} catch (Exception e) {
				ULog.e(MapBus.class, "ReloadStreet Draw Error:" + e.getMessage());
				if (BuildConfig.DEBUG)
					e.printStackTrace();
				return false;
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Boolean result) {
			PolylineOptions rectLineAllStart, rectLineAllBack;
			super.onPostExecute(result);

			if (isFinishing())
				return;

			rectLineAllStart = new PolylineOptions().width(8).color(getResources().getColor(R.color.green));
			rectLineAllBack = new PolylineOptions().width(3).color(getResources().getColor(R.color.orange));
			rectLineAllBack.zIndex(10);

			mMap.clear();
			addMarkerStreet();
			if (listPointStart.size() > 0) {
				rectLineAllStart.addAll(listPointStart);
				mMap.addPolyline(rectLineAllStart);
			}

			if (listPointBack.size() > 0) {
				rectLineAllBack.addAll(listPointBack);
				mMap.addPolyline(rectLineAllBack);
			}

			if (start) {

				if (reloadStart) {
					ULog.i(MapBus.class, "reload 2 street start");
					reloadStart = false;
					// rectLineAllStart = new PolylineOptions().width(8).color(getResources().getColor(R.color.green));
					new ReloadStreet(true).execute(arrPathBus.get(number).locS);
				} else {
					progressMap2.setVisibility(View.GONE);

					clsPolylineEntity entity = new clsPolylineEntity();
					// entity.listPoint = listPointStart;
					entity.setListPoint(listPointStart);
					// save cache back
					if (isHcm)
						InternalStorage.writeObject(MapBus.this, arrPathBus.get(number).getNum() + "s", entity);
					else
						InternalStorage.writeObject(MapBus.this, arrPathBus.get(number).getNum() + "shn", entity);
					// logTemp(listPointStart, start);

				}

			} else {

				if (reloadBack) {
					ULog.i(MapBus.class, "reload 2 street back");
					reloadBack = false;
					// rectLineAllBack = new PolylineOptions().width(3).color(getResources().getColor(R.color.orange));
					new ReloadStreet(false).execute(arrPathBus.get(number).locB);
				} else {
					progressMap2.setVisibility(View.GONE);

					clsPolylineEntity entity = new clsPolylineEntity();
					entity.setListPoint(listPointBack);
					// save cache back
					if (isHcm)
						InternalStorage.writeObject(MapBus.this, arrPathBus.get(number).getNum() + "b", entity);
					else
						InternalStorage.writeObject(MapBus.this, arrPathBus.get(number).getNum() + "bhn", entity);

					// logTemp(listPointStart, start);

				}
			}

			ULog.i(MapBus.class, "ReloadStreet finish start:" + start);
		}
	}

	private void addMarkerStreet() {
		mMap.addMarker(new MarkerOptions().position(startL).title(MapBus.this.getString(R.string.startPath2))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_green)));

		mMap.addMarker(new MarkerOptions().position(endL).title(MapBus.this.getString(R.string.backPath2))
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