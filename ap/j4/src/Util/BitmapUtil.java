package  sjpn4.vn.Util;

import java.io.File;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class BitmapUtil {
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
//	    options.inJustDecodeBounds = true;
//	    BitmapFactory.decodeResource(res, resId, options);
//
//	    // Calculate inSampleSize
//	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//	    // Decode bitmap with inSampleSize set
//	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap decodeSampledBitmapFromPath(String path,
	        int reqWidth, int reqHeight) {

		if(path == null){
			return null;
		}
		File file = new File(path);
		if(file == null || !file.exists()){
			return null;
		}
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(path, options);
	}
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        YgoLog.i("BitmapUtil", "calculateInSampleSize");
    // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource2(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
    	System.gc();
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    
	    ULog.i("BitmapUtil", "====inSampleSize:" + options.inSampleSize + "; width:" + options.outWidth );
//        if(Constant.WITH_SCREEN > 480)
//            options.inSampleSize = 1;
//        else
//            options.inSampleSize = 2;
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
