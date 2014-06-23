package puzzle.slider.vn.view;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import java.util.Random;
import puzzle.slider.vn.SliderMainActivity;
import puzzle.slider.vn.SoundManager;
import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.CustomSharedPreferences;
import puzzle.slider.vn.util.ShowLog;

import android.view.ViewParent;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

public class PluzzleView extends RelativeLayout {

	private String tag = "HuynhTD-" + PluzzleView.class.getSimpleName();
	private TileImage[][] dataTile;
	private TileImage currTile;
	private ArrayList<TileImage> arrTile;
	// boolean isCheck = true;
	public boolean isTime = false;
	int widthTile;
	int row = 4;
	int column = 4;
	// private Bitmap scaleBmp;
	// private Bitmap blank;
	private SliderMainActivity pluzzleMain;
	public boolean moveEvent = false;
	private float currX = 0, currY = 0;

	// MediaPlayer mbackground;
	// MediaPlayer mSlider, mSlider2;

	public PluzzleView(Context context) {
		super(context);
		try {

		} catch (Exception e) {

		}
	}

	public PluzzleView(SliderMainActivity pluzzleMain) {
		super(pluzzleMain.getApplicationContext());
		this.pluzzleMain = pluzzleMain;
	}

	public void init(int row, int column, Bitmap bmp) {
		TileImage tileImg, tileLeft;
		Rect area;
		Bitmap scaleBmp;
		Bitmap tileBmp;
		int locLeft, locTop;
		int disX = 14;
		int disY = 11;
		try {
			this.row = row;
			this.column = column;

			arrTile = new ArrayList<TileImage>();
			dataTile = new TileImage[row + 1][column + 1];
			// widthTile = CustomSharedPreferences.getPreferences(Constant.HEIGHT_SCREEN, 0) / 4 * 80 / 100;
			widthTile = CustomSharedPreferences.getPreferences(Constant.WIDTH_TILE, 0);

			// widthTile = 100;
			// height = CustomSharedPreferences.getPreferences(Constant.HEIGHT_SCREEN, 0);
			ShowLog.i(tag, "init()  width tile: " + widthTile);

			scaleBmp = Bitmap.createScaledBitmap(bmp, widthTile * column, widthTile * row, true);


			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {

					tileBmp = createImage(scaleBmp, j, i);
					if (tileBmp == null) {
						ShowLog.e(tag, "Can't create bitmap");
						return;
					}

					locLeft = widthTile * j + j + widthTile + disX;
					locTop = widthTile * i + i + disY;
					area = new Rect(locLeft, locTop, locLeft + widthTile, locTop + widthTile);

					// tileImg = new TileImage(getContext(), locLeft, locTop, tileBmp);
					tileImg = new TileImage(getContext(), j + 1, i + 1, tileBmp);
					tileImg.setAreaNew(area);

					if (i == 0 && j == 0) {
						// currTile = tileImg;
						// tileImg.alpha = true;

						// ////Tile left
						area = new Rect(disX, disY, widthTile + disX - 1, widthTile + disY);
						tileLeft = new TileImage(getContext(), j, i + 1, null);
						tileLeft.setAreaNew(area);
						currTile = tileLeft;
						currTile.alpha = true;
						this.addView(tileLeft);
						dataTile[i + 1][j] = tileLeft;
						// ///////////////////
					}

					this.addView(tileImg);
					dataTile[i + 1][j + 1] = tileImg;
				}
			}
			// sort data
			sortData();

			scaleBmp = null;
			tileBmp = null;

		} catch (Exception e) {
			ShowLog.e(tag, "init error: " + e.getMessage());
			if (Constant.IS_PrintStackTrace)
				e.printStackTrace();
		}
	}

	public boolean moveTileXY(int x, int y) {
		MediaPlayer mPlayer;
		TileImage tile;
		tile = getTile(x, y);
		if (tile == null) {
			ShowLog.i(tag, "moveTileXY: tile NULL");
			return false;
		}
		if (moveDataTile(tile)) {
			// System.gc();
			if (arrTile.size() > 0) {
				mPlayer = SoundManager.getMediaPlayer(Constant.SOUND_L);
				if (mPlayer != null && mPlayer.isPlaying()) {
					SoundManager.playSound(Constant.SOUND_L2, false);
					ShowLog.i(tag, "moveTile 1");
				} else {
					SoundManager.playSound(Constant.SOUND_L, false);
					ShowLog.i(tag, "moveTile 2");
				}
			}

			moveTiles();
			if (checkWin()) {
				isTime = true;
				pluzzleMain.showWin();
			}

			// isCheck = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				ShowLog.i(tag, "onTouchEvent curr x,y " + event.getX() + ", " + event.getY());
				if (moveEvent)
					moveEvent = false;
				else {
					if (moveTileXY((int) event.getX(), (int) event.getY())) {
						return false;
					}
				}

				ViewParent pr = getParent();
				pr.requestDisallowInterceptTouchEvent(false);
				return super.onTouchEvent(event);

			default:
				break;
			}

		} catch (Exception e) {
			ShowLog.e(tag, "init error: " + e.getMessage());
			if (Constant.IS_PrintStackTrace)
				e.printStackTrace();
		}
		return true;

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		TileImage tile;
		super.dispatchTouchEvent(event);
		ShowLog.i(tag, "dispatchTouchEvent....isTime: " + isTime);
		// ShowLog.showLogInfo(tag, "dispatchTouchEvent dispatch: " + dispatch);
		// ShowLog.showLogInfo(tag, "dispatchTouchEvent  check: " + "; x,y= " + event.getX() + ", " + event.getY());
		if (isTime) {
			return false;
		}
		// if (!isCheck) {
		// return false;
		// }
		// isCheck = false;
		if (currX == event.getX() && currY == event.getY()) {
			return false;
		}
		currX = event.getX();
		currY = event.getY();

		tile = getTile((int) event.getX(), (int) event.getY());
		if (tile != null) {
			return true;
		} else {
			// isCheck = true;
			return false;
		}
		// return dispatch;
	}

	/*
	 * Move location tile
	 */

	private boolean moveDataTile(TileImage tileImage) {
		int currX, currY;
		TileImage tile;

		if (currTile != null && tileImage != null) {
			if (currTile.getLocX() == tileImage.getLocX()) {
				currX = currTile.getLocX();
				currY = currTile.getLocY();
				if (currY > tileImage.getLocY()) {
					// ShowLog.showLogInfo(tag, "moveDataTile top => bottom");
					for (int i = currY - 1; i >= tileImage.getLocY(); i--) {
						tile = getDataLoc(currX, i);
						arrTile.add(tile);
						changeItem(tile);
					}
					arrTile.add(currTile);
				} else if (currY < tileImage.getLocY()) {
					// ShowLog.showLogInfo(tag, "moveDataTile bottom => top");
					for (int i = currY + 1; i <= tileImage.getLocY(); i++) {
						tile = getDataLoc(currX, i);
						arrTile.add(tile);
						changeItem(tile);
					}
					arrTile.add(currTile);
				}

			} else if (currTile.getLocY() == tileImage.getLocY()) {
				currX = currTile.getLocX();
				currY = currTile.getLocY();
				if (currX > tileImage.getLocX()) {
					// ShowLog.showLogInfo(tag, "moveDataTile left => right");
					for (int i = currX - 1; i >= tileImage.getLocX(); i--) {
						tile = getDataLoc(i, currY);
						arrTile.add(tile);
						changeItem(tile);
					}
					arrTile.add(currTile);
				} else if (currX < tileImage.getLocX()) {
					// ShowLog.showLogInfo(tag, "moveDataTile right => left");
					for (int i = currX + 1; i <= tileImage.getLocX(); i++) {
						tile = getDataLoc(i, currY);
						arrTile.add(tile);
						changeItem(tile);
					}
					arrTile.add(currTile);
				}
			}

		}
		return true;
	}

	/**
	 * move tile image
	 */
	private void moveTiles() {

		// move tiles
		for (int i = 0; i < arrTile.size(); i++) {
			TileImage tm = arrTile.get(i);
			Rect oldrect = tm.getAreaOld();
			Rect areaRect = tm.getAreaNew();
			TranslateAnimation transAni = new TranslateAnimation(oldrect.left - areaRect.left, 0, oldrect.top - areaRect.top, 0.0f);
			transAni.setDuration(100);
			tm.startAnimation(transAni);
		}
		ShowLog.i(tag, "moveTiles row image size=" + arrTile.size());
		arrTile.clear();
	}

	private void changeItem(TileImage tile) {
		int x, y;
		x = tile.getLocX();
		y = tile.getLocY();
		Rect r = tile.getAreaNew();
		tile.setLocXY(currTile.getLocX(), currTile.getLocY());
		tile.setAreaNew(currTile.getAreaNew());
		tile.alpha = false;

		currTile.setAreaNew(r);
		currTile.setLocXY(x, y);
		currTile.alpha = true;
	}

	/**
	 * 
	 * get tile
	 */
	private TileImage getTile(int x, int y) {
		TileImage tile = null;
		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= column; j++) {
				tile = dataTile[i][j];
				if (tile != null && tile.getAreaNew().contains((int) x, (int) y)) {
					return tile;
				}
			}
		}
		ShowLog.e(tag, "getTile() can't fine tile x,y=" + x + ", " + y);

		return null;
	}

	/**
	 * Get location tile
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private TileImage getDataLoc(int x, int y) {
		TileImage tile = null;
		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= column; j++) {
				tile = dataTile[i][j];
				// if (tile != null && tile.getAreaNew().contains((int) x, (int) y)) {
				if (tile != null && tile.getLocX() == x && tile.getLocY() == y) {
					// ShowLog.showLogInfo(tag, "getDataLoc:" + tile.getLocX() + ", " + tile.getLocY());
					return tile;
				}
			}
		}
		ShowLog.e(tag, "getDataLoc() can't fine tile x,y=" + x + ", " + y);

		return null;
	}

	/**
	 * cut image
	 * 
	 * @param bmp
	 * @param x
	 * @param y
	 * @return
	 */
	private Bitmap createImage(Bitmap bmp, int x, int y) {
		return Bitmap.createBitmap(bmp, widthTile * x, widthTile * y, widthTile, widthTile);
	}

	/**
	 * sort tile
	 */
	public void sortData() {
		int rX, rY;
		Random rand;
		TileImage tile;
		ShowLog.i(tag, "sortData");
		tile = getTile(dataTile[1][1].getAreaNew().left + 10, dataTile[1][1].getAreaNew().top + 10);
		moveDataTile(tile);

		for (int i = 0; i < 300; i++) {
			// for (int i = 0; i < 300; i++) {
			rand = new Random();
			rX = rand.nextInt(row) + 1;
			rY = rand.nextInt(row) + 1;
			tile = dataTile[rY][rX];
			moveDataTile(tile);
		}
		setInvalidate();
		arrTile.clear();
	}

	private void setInvalidate() {
		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= column; j++) {
				dataTile[i][j].invalidate();
			}
		}
	}

	/**
	 * 
	 * @return boolean (true: win; false: wrong)
	 */
	private boolean checkWin() {
		TileImage tile;
		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= column; j++) {
				tile = dataTile[i][j];
				if (tile != null && (tile.getLocX() != j || tile.getLocY() != i))
					return false;
			}
		}
		return true;
	}

}
