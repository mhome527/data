package app.infobus.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LocXY implements Parcelable  {

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	@SerializedName("lat")	
	public double lat;
	@SerializedName("lng")	
	public double lng;
	
	//don't delete contractor because gJson error when parse
	public LocXY(){
		
	}
	public LocXY(double lat, double lng){
		this.lat = lat;
		this.lng = lng;		
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
