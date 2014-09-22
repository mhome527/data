package app.infobus.entity;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.android.gms.maps.model.LatLng;

public class clsPolylineEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5377864582079604570L;
	/**
	 * 
	 */
//	private transient ArrayList<LatLng> listPoint;
	public ArrayList<PointStreet> listPoint2;
	public String test = "data test";

	// public PolylineOptions rectLineAll;
	// public String name;
	public clsPolylineEntity() {

		listPoint2 = new ArrayList<PointStreet>();
		listPoint2.add(new PointStreet(112D, 335D));

	}

	public void setListPoint(ArrayList<LatLng> listPointTmp) {
		listPoint2 = new ArrayList<PointStreet>();
		for (LatLng point : listPointTmp) {
			listPoint2.add(new PointStreet(point.latitude, point.longitude));
		}

	}

	public ArrayList<LatLng> getListPoint() {
		ArrayList<LatLng> listPointTmp = new ArrayList<LatLng>();
		if (listPoint2 == null)
			return null;
		
		for (PointStreet point : listPoint2) {
			listPointTmp.add(new LatLng(point.lat, point.lng));
		}
		return listPointTmp;
	}

	public class PointStreet implements Serializable {
		private static final long serialVersionUID = -6893907984404797179L;
		public double lat;
		public double lng;

		public PointStreet(double lat, double lng) {
			this.lat = lat;
			this.lng = lng;
		}

	}
	// // private static final long serialVersionUID = 1L;
	// private transient PolylineOptions rectLineAll;
	// public PolylineOptions getRectLineAll() {
	// return rectLineAll;
	// }
	// public void setRectLineAll(PolylineOptions rectLineAll) {
	// this.rectLineAll = rectLineAll;
	// }

}
