package app.infobus;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import app.infobus.entity.LocXY;
import app.infobus.model.GMapV2Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapBus extends FragmentActivity {
	GoogleMap mMap;
	GMapV2Direction md;
	LatLng pos1 = new LatLng(10.739956, 106.703260);
	LatLng pos2 = new LatLng(10.759561, 106.698604);
	LatLng pos3 = new LatLng(10.760994, 106.700385);

	LatLng pos4 = new LatLng(10.771239, 106.693411);
	LatLng pos5 = new LatLng(10.775539, 106.698883);
	LatLng pos6 = new LatLng(10.782368, 106.705041);

	// LatLng pos8 = new LatLng(10.8086801,106.6705759);
	// LatLng pos9 = new LatLng(10.8170503,106.680957);
	// LatLng pos10 = new LatLng(10.8236609,106.6816507);
	// LatLng pos11 = new LatLng(10.8618387,106.6743374);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_bus);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		md = new GMapV2Direction();
		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));

		// mMap.addMarker(new MarkerOptions().position(pos1).title("Start"));
		// mMap.addMarker(new MarkerOptions().position(pos2));
		// mMap.addMarker(new MarkerOptions().position(pos3));
		// //
		// mMap.addMarker(new MarkerOptions().position(pos6).visible(false));

		// mMap.addPolyline(rectLine);
		List<LocXY> listXY = new ArrayList<LocXY>();

		listXY.add(new LocXY(10.789956, 106.703260));
		listXY.add(new LocXY(10.759561, 106.698604));
		listXY.add(new LocXY(10.760994, 106.700385));

		setLocationXY(listXY, true);
		
		listXY = new ArrayList<LocXY>();

		listXY.add(new LocXY(10.739956, 106.703260));
		listXY.add(new LocXY(10.759561, 106.698604));
		listXY.add(new LocXY(10.860994, 106.900385));

		setLocationXY(listXY, false);
	}

	private void setLocationXY(List<LocXY> locXY, boolean start) {
		LocXY loc;
		LocXY locTmp = null;
		LatLng latX, latY;
		for (int i = 0; i < locXY.size(); i++) {
			loc = locXY.get(i);
			if (i == 0) {
				locTmp = loc;
				mMap.addMarker(new MarkerOptions().position(
						new LatLng(loc.x, loc.y)).title("start bus").icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
			} else {
				latX = new LatLng(locTmp.x, locTmp.y);
				latY = new LatLng(loc.x, loc.y);

				drawMap(latX, latY, start);
				if (i == locXY.size() - 1) {
					mMap.addMarker(new MarkerOptions().position(
							new LatLng(loc.x, loc.y)).title("End buyt"));
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
}