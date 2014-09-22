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
	public static String HCM = "HCM";
	public static String HANNOI = "HANOI";
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
	final static public String JSON_BUS_HN = "HANOI";
	final static public String CLEAR_CACHE_HCM = "clear_cache_HCM";
	final static public String CLEAR_CACHE_HN = "clear_cache_HN";

	// GA
	public static String KEY_ANALYSIS = "UA-54709178-1"; // daohuynh7
	public static String GA_HCM = "HCM";
	public static String GA_HN = "HANOI";
	public static String GA_BUS = "BUS_NUMBER";

}
