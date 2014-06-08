package app.infobus.utils;

public class Utility {
	public static int parseInt(final String num) {
		try {
			return Integer.parseInt(num);
		} catch (final Exception e) {
			return -1;
		}
	}

	public static String ArrToString(String[] arr) {
		String str = "";
		if (arr == null || arr.length == 0)
			return "";
		if (arr.length == 1) {
			str = arr[0];
		} else {
			for (int i = 0; i < arr.length - 1; i++) {
				if (!arr[i].contains("ttt"))
					str += arr[i] + " - ";
			}
			str += arr[arr.length - 1];
		}
		return str;
	}
	
}
