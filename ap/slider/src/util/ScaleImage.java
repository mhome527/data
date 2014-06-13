package puzzle.slider.vn.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import puzzle.slider.vn.Entity.Size;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

/**
 * this class for get bitmap was scaled from bitmap or from file
 * 
 * 
 * @sine Mar 15, 2013
 */
public class ScaleImage {
	private DisplayMetrics displaymetrics;
	private static int FH = 600;
	private static int FW = 900;
	
	private double scale;
	/**
	 * constructor
	 * 
	 * @since Mar 15, 2013
	 * @param displaymetrics 
	 * How to create the displaymetrics ?
	 * <br \>
	 * copy code here: 
	 * <br \>
	 * DisplayMetrics displaymetrics = new DisplayMetrics();
     * getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	 */
	public ScaleImage(DisplayMetrics displaymetrics) {
		this.displaymetrics = displaymetrics;
	}
	
	/**
	 * get scale ratio
	 * 
	 * @since Mar 15, 2013 - 6:23:36 PM
	 * @param ratio This is ratio compared to the screen
	 * @param h The current height of image 
	 * @param w The current width of image
	 * @param isHeight
	 * @return
	 */
	public double scale(double ratio, int h, int w, boolean isHeight){
    	int height = this.displaymetrics.heightPixels;
    	int width = this.displaymetrics.widthPixels;
    	ShowLog.showLogDebug("ScaleImage", "screen width "+ width);
		ShowLog.showLogDebug("ScaleImage", "screen height "+ height);
		double scale =0.0;
		if (isHeight) {
			scale = (double) (ratio*height)/h;
			ShowLog.showLogDebug("ScaleImage", "screen scale height "+ scale);
		}else{
			scale = (double) (ratio*width)/w;
		}
		this.scale = scale;
		return scale;
	}	
	
	/**
	 * 
	 * @since Mar 18, 2013 - 11:59:22 AM
	 * @return
	 */
	public double getScale() {
		return scale;
	}
	
	/**
	 * get bitmap was scaled from file
	 * 
	 * @since Mar 15, 2013 - 6:24:02 PM
	 * @param filePath
	 * this is filePath ex: /sdcard/image/image.png
	 * @param ratio This is ratio compared to the screen. Example: 0.6
	 *  this is the ratio want to scalse with screen ( 60% of screen )
	 *  <br \>
	 * @return
	 */
	public Bitmap getResizedBitmap(String filePath,double ratio) {
		try{
			Bitmap bm =decodeFile(filePath, ratio);
		    int width = bm.getWidth();
		    int height = bm.getHeight();
		    
		    double scale = scale(ratio, FH, FW, true);
		    
		    if (scale >= 1) {
				return bm;
			}
		    
		    int newHeight = (int) (height*scale);
		    int newWidth = (int) (width*scale);
		    
		    float scaleWidth = ((float) newWidth) / width;
		    float scaleHeight = ((float) newHeight) / height;
		    // CREATE A MATRIX FOR THE MANIPULATION
		    Matrix matrix = new Matrix();
		    // RESIZE THE BIT MAP
		    matrix.postScale(scaleWidth, scaleHeight);
	
		    // "RECREATE" THE NEW BITMAP
		    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		    if (null != bm && !bm.isRecycled() && bm!= resizedBitmap) {
		    	ShowLog.showLogDebug("ScaleImage - getResizedBitmap", "recycle bitmap");
				bm.recycle();
				bm = null;
			}
		    return resizedBitmap;
		}
	    catch (Exception e) {
			// TODO: handle exception
	    	e.fillInStackTrace();
		}
	    return null;
	}

	/**
	 * get bitmap was scaled from bitmap
	 * 
	 * @since Mar 22, 2013 - 2:42:55 PM
	 * @param bitmap
	 * @return
	 */
	public Bitmap getResizedBitmap(Bitmap bitmap) {
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
//	    int heightScreen = this.displaymetrics.heightPixels;
	    double scale = scale(1, 600, 900, true);//(double)height/heightScreen;
	    
	    int newHeight = (int) (height*scale);
	    int newWidth = (int) (width*scale);
	    
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
	    if (null != bitmap && !bitmap.isRecycled() && bitmap!= resizedBitmap) {
	    	ShowLog.showLogDebug("ScaleImage - getResizedBitmap", "recycle bitmap");
	    	bitmap.recycle();
	    	bitmap = null;
		}
	    
	    return resizedBitmap;
	}
	
	/**
	 * get bitmap was scaled from bitmap
	 * 
	 * @since Mar 15, 2013 - 6:25:32 PM
	 * @param bitmap
	 * @param ratio This is ratio compared to the screen. Example: 0.6
	 * this is the ratio want to scalse with screen ( 60% of screen )
	 * <br \>
	 * @return
	 */
	public Bitmap getResizedBitmap(Bitmap bitmap,double ratio) {
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    
	    double scale = scale(ratio, height, width, true);
	    int newHeight = (int) (height*scale);
	    int newWidth = (int) (width*scale);
	    
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
	    if (null != bitmap && !bitmap.isRecycled() && bitmap!= resizedBitmap) {
	    	ShowLog.showLogDebug("ScaleImage - getResizedBitmap", "recycle bitmap");
	    	bitmap.recycle();
	    	bitmap = null;
		}
	    
	    return resizedBitmap;
	}
	
	
	//decodes image and scales it to reduce memory consumption
		/**
		 * get bitmap from file, this bitmap return has scale
		 * 
		 * 
		 * @since Mar 15, 2013 - 6:26:19 PM
		 * @param filePath
		 * @param ratio This is ratio compared to the screen. Example: 0.6
		 * @return
		 */
		private Bitmap decodeFile(String filePath,double ratio){
		    try {
		        //Decode image size
		        BitmapFactory.Options o = new BitmapFactory.Options();
		        
		        o.inJustDecodeBounds = true;
		        FileInputStream fStream = new FileInputStream(filePath);
		        BitmapFactory.decodeStream(fStream,null,o);
		        //The new size we want to scale to
		        //Find the correct scale value. It should be the power of 2.
		        int scale=1;
		        double scaleMain = scale(ratio, o.outHeight, o.outWidth, true);
		        
		        while (o.outHeight/(o.outHeight*scaleMain) >=(2*scale)) {
					scale*=2;
				}
		        
		        //Decode with inSampleSize
		        BitmapFactory.Options o2 = new BitmapFactory.Options();
		        
		        o2.inSampleSize=scale;
		        return BitmapFactory.decodeStream(new FileInputStream(filePath), null, o2);
		    } catch (FileNotFoundException e) {
		    	e.printStackTrace();
		    }
		    return null;
		}
		
		/**
		 * get size was scaled
		 * 
		 * @since Mar 25, 2013 - 1:25:51 PM
		 * @param ratio
		 * @return size
		 */
		public Size getSizeScale(double ratio){
			return Utility.getSize(displaymetrics, ratio);
		}

}
