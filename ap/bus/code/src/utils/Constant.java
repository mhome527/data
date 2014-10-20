package app.infobus.utils;

import app.infobus.BuildConfig;

public class Constant {

	final static public String APP_KEY = "784aqs9zpcajd47";
	final static public String APP_SECRET = "8i4fgakaa12k8q6";
	static public String APP_TOCKEN = "ukTwK3tqg4YAAAAAAAAUc5W0O_qi3r7hEKLszLaTmdo_md7TWJSLNF5bJrY2Zj5G";
	// final static public String APP_KEY = "784aqs9zpcajd47";
	// final static public String APP_SECRET = "8i4fgakaa12k8q6";
	// final static public String APP_TOCKEN = "ukTwK3tqg4YAAAAAAAAQTK0gVjiMjB5P_vrpMab3zHhmqNGEu_OVPdkYBKXvsavp";
	//
	//http://maps.googleapis.com/maps/api/distancematrix/json?origins=10.861657,%20106.678478&destinations=10.741041,106.618371&mode=driving
	public static String URL_DISTANCE ="http://maps.googleapis.com/maps/api/distancematrix/json?mode=driving";
	
	//https://maps.googleapis.com/maps/api/directions/json?origin=Adelaide,SA&destination=Adelaide,SA&waypoints=optimize:true|Barossa+Valley,SA|Clare,SA|Connawarra,SA|McLaren+Vale,SA&key=AIzaSyBrkpsBfa8Q22h2Y9qUMN8UBUa0s1rWn_U
	public static String URL_BUS_ROUTE ="https://maps.googleapis.com/maps/api/directions/json?origin=Adelaide,SA&destination=Adelaide,SA&waypoints=optimize:true|Barossa+Valley,SA|Clare,SA|Connawarra,SA|McLaren+Vale,SA&key=AIzaSyBrkpsBfa8Q22h2Y9qUMN8UBUa0s1rWn_U";
	public static final String KEY_ORG = "origins";
	public static final String KEY_DES = "destinations";
	public static String HCM = "HCM";
	public static String HANNOI = "HANOI2";
	public static String TAXI = "TAXI.txt";
	public static boolean bLog = BuildConfig.DEBUG;
	public static String INFO_BUS = "InfoBus";

	public static String LIST_STREET_HCM = "listStreetHCM";
	public static String LIST_STREET_HN = "listStreetHN";

	public static String KEY_CITY = "city";
	public static final String PREF_MODIFY_AD = "date_modify_ad";
	//
	///////// dropbox
	static public String FOLDER_NAME;
	static{
		if(BuildConfig.DEBUG)
			FOLDER_NAME = "/Bus_debug/";
		else
			FOLDER_NAME = "/Bus/";
	}
	final static public String JSON_AD = "ad.json";
	final static public String JSON_BUS_HCM = "HCM";
	final static public String JSON_BUS_HN = "HANOI2";
	final static public String CLEAR_CACHE_HCM = "clear_cache_HCM";
	final static public String CLEAR_CACHE_HN = "clear_cache_HN";

	final static public String PRICE1_TAXI = "price1_taxi";
	final static public String PRICE2_TAXI = "price2_taxi";
	final static public String PRICE_BEGIN_TAXI = "price_begin_taxi";
	final static public String KM_TAXI = "km_taxi";
	static public int KM_DEFAULT = 31;

	// GA
	public static String KEY_ANALYSIS = "UA-54709178-1"; // daohuynh7
	public static String GA_HCM = "HCM";
	public static String GA_HN = "HANOI";
	public static String GA_BUS = "BUS_NUMBER";

}
