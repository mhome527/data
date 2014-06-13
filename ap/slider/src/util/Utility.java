package puzzle.slider.vn.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.cookie.DateParseException;

import puzzle.slider.vn.Entity.Size;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 * @author huynhtran
 * 
 */

public class Utility {
	private static String TAG = "Utility";
//	public static String getPathImg(Context contect, String name) {
//
//		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
//		String imgDir = "";
//		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			File dir = new File(extStorageDirectory + "/" + Constant.FOLDER_IMAGE);
//			if (dir.exists() && dir.isDirectory()) {
//
//			}
//			else {
//				if (!dir.mkdir()) {
//					return "";
//				}
//			}
//			imgDir = extStorageDirectory + "/" + Constant.FOLDER_IMAGE + "/" + name;
//		}
//		else {
//			File basedirectory = contect.getFileStreamPath("");
//			File subdirectory = new File(basedirectory, "/" + Constant.FOLDER_IMAGE);
//			if (!subdirectory.exists() || !subdirectory.isDirectory()) {
//				subdirectory.mkdirs();
//			}
//
//			imgDir = basedirectory.toString() + "/" + Constant.FOLDER_IMAGE + "/" + name;
//		}
//
//		return imgDir;
//	}

	public static Bitmap decodeBitmapFromFile(String path, int reqWidth, int reqHeight) {
		// Bitmap bmp = BitmapFactory.decodeFile(path);
		int MAX_SIZE = 960;
		Bitmap bmp = null;
		Bitmap bmp1 = null;
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, o);

			// Find the correct scale value. It should be the power
			// of 2.
			int maxSize = o.outWidth > o.outHeight ? o.outWidth : o.outHeight;
			int scale = 1;
			int newMaxsize = MAX_SIZE;
			if (newMaxsize != -1 && newMaxsize < maxSize) {
				scale = (int) android.util.FloatMath.floor(maxSize / (float) newMaxsize);
			}

			o.inJustDecodeBounds = false;
			o.inSampleSize = scale;
			bmp = BitmapFactory.decodeFile(path, o);
			// Log.i("HuynhTD-Utility", "decodeBitmapFromFile before: " + bmp.getByteCount());
			bmp1 = getResizedBitmap(bmp, reqWidth, reqHeight, true);
			// bmp1 = getResizedBitmap(bmp.copy(bmp.getConfig(), true), reqWidth, reqHeight, true);
			// bmp.recycle();
			bmp = null;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// Log.i("HuynhTD-Utility", "decodeBitmapFromFile after : " + bmp1.getByteCount());
		return bmp1;
	}

	
	public static Bitmap decodeBitmapFromResource(Resources res, int id, int reqWidth, int reqHeight) {
		int MAX_SIZE = 960;
		Bitmap bmp = null;
		Bitmap bmp1 = null;
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(path, o);
			BitmapFactory.decodeResource(res, id, o);

			// Find the correct scale value. It should be the power
			// of 2.
			int maxSize = o.outWidth > o.outHeight ? o.outWidth : o.outHeight;
			int scale = 1;
			int newMaxsize = MAX_SIZE;
			if (newMaxsize != -1 && newMaxsize < maxSize) {
				scale = (int) android.util.FloatMath.floor(maxSize / (float) newMaxsize);
			}

			o.inJustDecodeBounds = false;
			o.inSampleSize = scale;
//			bmp = BitmapFactory.decodeFile(path, o);
			bmp = BitmapFactory.decodeResource(res, id, o);

			// Log.i("HuynhTD-Utility", "decodeBitmapFromFile before: " + bmp.getByteCount());
			bmp1 = getResizedBitmap(bmp, reqWidth, reqHeight, true);
			// bmp1 = getResizedBitmap(bmp.copy(bmp.getConfig(), true), reqWidth, reqHeight, true);
			// bmp.recycle();
			bmp = null;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// Log.i("HuynhTD-Utility", "decodeBitmapFromFile after : " + bmp1.getByteCount());
		return bmp1;
	}

	
	public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight, boolean isRecycle) {

		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = newWidth;
		float scaleHeight = newHeight;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();

		// resize the bitmap
		matrix.postScale(scaleWidth / width, scaleHeight / height);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

		if (isRecycle) {
//			ShowLog.showLogInfo("HuynhTD-Utility", "getResizedBitmap recycle bmp");
//			bm.recycle();
			bm=null;
		}
		return resizedBitmap;
	}

	public static Bitmap scaleImageBitmap(Bitmap bmp, int reqwidth, int regheight) {
		if (bmp != null && reqwidth > 0) {
			/*
			 * scale bitmap
			 */
			Matrix mat = new Matrix();
			float scaleWidth = 1;
			float scaleHeight = 1;
			float w = bmp.getWidth();
//			float h = bmp.getHeight();

			scaleHeight = scaleWidth = 1.0f * reqwidth / w;
			mat.postScale(scaleWidth, scaleHeight);

			Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, reqwidth, regheight, mat, false);
			return correctBmp;
		}

		return bmp;
	}
	
	
	/**
	 * get data path in app
	 * @author thangtb
	 * @since Mar 28, 2013 - 11:40:53 AM
	 * @param context
	 * @return
	 */
	static public String getDataPath(Context context){
		PackageManager m = context.getPackageManager();
		String s = context.getPackageName();
		try {
		    PackageInfo p = m.getPackageInfo(s, 0);
		    s = p.applicationInfo.dataDir;
		    ShowLog.showLogDebug(TAG, "DataPath= "+s);
		    return s;
		} catch (NameNotFoundException e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * get size was scaled
	 * @author thangtb
	 * @since Mar 25, 2013 - 1:26:31 PM
	 * @param displaymetrics
	 * @param ratio
	 * @return
	 */
	public static Size getSize(DisplayMetrics displaymetrics, double ratio){
		int height = displaymetrics.heightPixels;
    	int width = displaymetrics.widthPixels;
		Size size = new Size();
		
		size.setHeight((int) (height*ratio));
		size.setWidth((int) (width*ratio));
		
		return size;
	}
	
	/**
	 * convert String to Date
	 * @param dateString
	 * @return
	 */
	public static Date ConvertToDate(final String dateString) throws DateParseException {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		Date convertedDate;
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (final ParseException e) {
			// BAD PROGRAMMING PRACTICE, don't ever catch and silently consume the exception.
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
			return null;
		}
		return convertedDate;
	}

	  /** get distance between Point a and b 
	  * @param a
	  * @param b
	  * @return
	  */
	 public static int distance(Point a, Point b){
	  return (int) Math.sqrt(Math.pow((b.x - a.x),2) + Math.pow((b.y - a.y),2));
	 }
	 
	 /**
	 * use to unbind drawable
	 * @author thangtb
	 * @since Mar 29, 2013 - 9:03:07 AM
	 * @param view
	 */
	public static void unbindDrawables(View view)
	    {
	            if (view.getBackground() != null)
	            {
	                    view.getBackground().setCallback(null);
	            }
	            if (view instanceof ViewGroup && !(view instanceof AdapterView))
	            {
	                    for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
	                    {
//	                    	ShowLog.showLogDebug(TAG, "unbindDrawables view"+i);
	                            unbindDrawables(((ViewGroup) view).getChildAt(i));
	                    }
	                    ((ViewGroup) view).removeAllViews();
	            }
	    }
	
	/*
	 * unbind imageView
	 */
	public static void unbindImageView(ImageView imageview) {
		if (imageview != null) {
			Drawable drawable = imageview.getDrawable();
			if (drawable instanceof BitmapDrawable) {
				Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
				bmp.recycle();
			}
		}
	}
	
	/**
	 * @author thangtb
	 * @since Apr 1, 2013 - 5:23:35 PM
	 * @param filePath
	 * @return
	 */
	public static Boolean checkDataFolder(String filePath){
		filePath =filePath.trim().toString();
		String[] paths = filePath.split("/");
		String s="";
		for (int i = 0; i < paths.length; i++) {
			s = s+"/"+paths[i];
			File dir = new File(s); 
			if (dir.exists() && dir.isDirectory()) {
				continue;
			}else{
				if (dir.mkdir()) {
					continue;
				}else{
					return false;
				}
			}
		}
		
		return true;
	}

	 /**
	  * @author thangtb
	   * Checks if the application is being sent in the background (i.e behind
	   * another application's Activity).
	   * 
	   * @param context the context
	   * @return <code>true</code> if another application will be above this one.
	   */
	  public static boolean isApplicationSentToBackground(final Context context) {
	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	    if (!tasks.isEmpty()) {
	      ComponentName topActivity = tasks.get(0).topActivity;
	      if (!topActivity.getPackageName().equals(context.getPackageName())) {
	    	  ShowLog.showLogDebug(TAG, "App not running");
	        return true;
	      }
	    }

	    ShowLog.showLogDebug(TAG, "App is running");
	    return false;
	  }
	  
	  
	  /**
		 * Return the path to the removable storage or SDcard
		 * 
		 * @param context
		 * @return path 
		 */
		public static String getPath(final Context context, final String pathName) {
			String outputName = "";
			String extStorageDirectory;
			try {
				extStorageDirectory = Environment.getExternalStorageDirectory().toString();

				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					outputName = extStorageDirectory + "/" + pathName;
				} else {
					final File basedirectory = context.getFileStreamPath("");
					outputName = basedirectory.toString() + "/" + pathName;

				}
				return outputName;
			} catch (final Exception ex) {
				ShowLog.e("Utility", "getPath() Error:" + ex.toString());
				return "";
			}
		}
		
		/*
	     * get center point of View
	     */
	    public static Point getCenterPoint(ImageView view, int leftmarginMain) {
	    	Point p = new Point();
	    	RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) view.getLayoutParams();
	    	int x = params.leftMargin + view.getWidth()/2 + leftmarginMain;
	    	int y = params.topMargin + view.getHeight()/2;
	    	p.set(x, y);
	    	return p;
	    }
}
