package puzzle.slider.vn.view;

import puzzle.slider.vn.util.ShowLog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Tile
 * 
 * @author huynhtran
 * 
 */
public class TileImage extends View {

	private String tag = TileImage.class.getSimpleName();
	private int locX;
	private int locY;
	private int orgLocX;
	private int orgLocY;
	private int lengthTile;
	public boolean alpha = false;

	private Rect areaNew = new Rect(0, 0, 0, 0);
	private Rect areaOld = new Rect(0, 0, 0, 0);

	private Bitmap bmp = null;

	public TileImage(Context context) {
		super(context);
	}

	public TileImage(Context context, int locX, int locY, Bitmap bmp) {
		super(context);
		this.locX = locX;
		this.locY = locY;

		this.orgLocX = locX;
		this.orgLocY = locY;
		if (bmp != null) {
			this.lengthTile = bmp.getWidth();
			// this.setBackgroundResource(R.drawable.ic_launcher);
			this.bmp = bmp;
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint;
		try {
			super.onDraw(canvas);
			paint = new Paint();
			// if (alpha == true) {
			// paint.setAlpha(90);
			// ShowLog.showLogInfo(tag, "onDraw alpha = true");
			// }
			if (alpha == false)
				canvas.drawBitmap(bmp, null, getAreaNew(), paint);
			// ShowLog.showLogInfo(tag, "onDraw location x,y= " + getAreaNew().left + ", " + getAreaNew().top + " || x,y=" + locX + "," + locY + " || org x,y=" + orgLocX +
			// ","
			// + orgLocY);
		}
		catch (Exception ex) {
			ShowLog.e(tag, "onDraw error: " + ex.getMessage());
		}
	}

	public int getLengthTile() {
		return lengthTile;
	}

	public void setLengthTile(int lengthTile) {
		this.lengthTile = lengthTile;
	}

	public Bitmap getBmp() {
		return bmp;
	}

	public void setBmp(Bitmap bmp) {
		this.bmp = bmp;
	}

	public Rect getAreaNew() {
		return areaNew;
	}

	public void setAreaNew(Rect area) {
		this.areaOld = this.areaNew;
		this.areaNew = area;
	}

	public int getLocX() {
		return locX;
	}

	public void setLocX(int locationX) {
		this.locX = locationX;
	}

	public int getLocY() {
		return locY;
	}

	public void setLocY(int locationY) {
		this.locY = locationY;
	}

	public void setLocXY(int locX, int locY) {
		this.locX = locX;
		this.locY = locY;
	}

	public int getOrgLocX() {
		return orgLocX;
	}

	public int getOrgLocY() {
		return orgLocY;
	}

	public void setOrgLocXY(int orgLocX, int orgLocY) {
		this.orgLocX = orgLocX;
		this.orgLocY = orgLocY;
	}

	public Rect getAreaOld() {
		return areaOld;
	}

	public void setAreaOld(Rect areaOld) {
		this.areaOld = areaOld;
	}

}
