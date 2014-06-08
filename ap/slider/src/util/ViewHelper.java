package puzzle.slider.vn.util;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Get view
 * 
 * @author ThangTB
 * @version 1.0
 */
public final class ViewHelper {

	/**
	 * Hide constructor
	 */
	private ViewHelper() {
	}

	/**
	 * get view
	 * 
	 * @param <V>
	 * @param view
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <V extends View> V findView(View view, int viewId) {
		return (V) view.findViewById(viewId);
	}

	/**
	 * get view
	 * 
	 * @param <V>
	 * @param activity
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <V extends View> V findView(Activity activity, int viewId) {
		return (V) activity.findViewById(viewId);
	}

	/**
	 * Set text
	 * 
	 * @param v
	 * @param id
	 * @param stringId
	 */
	public static void setText(View v, int id, int stringId) {
		((TextView) v.findViewById(id)).setText(stringId);
	}

	/**
	 * Set text
	 * 
	 * @param v
	 * @param id
	 * @param text
	 */
	public static void setText(View v, int id, CharSequence text) {
		((TextView) v.findViewById(id)).setText(text);
	}

	/**
	 * Set image
	 * 
	 * @param v
	 * @param id
	 * @param drawableId
	 */
	public static void setImage(View v, int id, int drawableId) {
		((ImageView) v.findViewById(id)).setImageResource(drawableId);
	}

	/**
	 * Set image
	 * 
	 * @param v
	 * @param id
	 * @param drawable
	 */
	public static void setImage(View v, int id, Drawable drawable) {
		((ImageView) v.findViewById(id)).setImageDrawable(drawable);
	}

	/**
	 * Set text color for all state
	 * 
	 * @param v
	 * @param id
	 * @param color
	 */
	public static void setColor(View v, int id, int color) {
		((TextView) v.findViewById(id)).setTextColor(color);
	}
}
