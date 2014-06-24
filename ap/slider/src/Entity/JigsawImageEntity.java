package puzzle.slider.vn.Entity;

import android.graphics.Bitmap;

/**
 * 
 * @author huynhtran
 * 
 */
public class JigsawImageEntity {
	// private Bitmap bmp;
	private String gameId;
	private int start;
	private int numTimer;
	private String pathName;
	public int res;
	int width;
	int height;
	boolean finish;

	// public JigsawImageEntity(Bitmap bmp, String pathName) {
	// this.bmp = bmp;
	// this.pathName = pathName;
	// }


	public JigsawImageEntity(int res) {
		this.res = res;
	}

	
	public JigsawImageEntity(String gameId, String pathName, boolean finish) {
		this.pathName = pathName;
		this.finish = finish;
		this.gameId = gameId;
	}

	public JigsawImageEntity(String gameId, int start, int numTimer, String pathName, boolean finish) {
		this.gameId = gameId;
		this.start = start;
		this.numTimer = numTimer;
		this.pathName = pathName;
		this.finish = finish;
	}

	// public JigsawImageEntity(Bitmap bmp, int start, int numTimer, String pathName) {
	// this.bmp = bmp;
	// this.start = start;
	// this.numTimer = numTimer;
	// this.pathName = pathName;
	// }

	// public Bitmap getBmp() {
	// return bmp;
	// }
	//
	// public void setBmp(Bitmap bmp) {
	// this.bmp = bmp;
	// }

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getNumTimer() {
		return numTimer;
	}

	public void setNumTimer(int numTimer) {
		this.numTimer = numTimer;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidthBmp() {
		return width;
	}

	public int getHeightBmp() {
		return height;
	}
}
