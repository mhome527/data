package puzzle.slider.vn;

import jp.sun_star_st.Puzzledaisuki.dao.GameHistoryDataHelper;
import jp.sun_star_st.Puzzledaisuki.lib.Constant;
import jp.sun_star_st.Puzzledaisuki.lib.CustomSharedPreferences;
import jp.sun_star_st.Puzzledaisuki.util.ShowLog;
import jp.sun_star_st.Puzzledaisuki.util.Utility;
import jp.sun_star_st.Puzzledaisuki.util.ViewHelper;
import jp.sun_star_st.Puzzledaisuki.view.PluzzleView;
import jp.sun_star_st.Puzzledaisuki.view.congratulation.CongratulationView;
import jp.sun_star_st.Puzzledaisuki.view.congratulation.CongratulationView.CongratulationClickListener;
import jp.sun_star_st.Puzzledaisuki.view.congratulation.CongratulationView.FinishType;
import jp.sun_star_st.Puzzledaisuki.view.congratulation.CongratulationView.GameType;
import jp.sun_star_st.Puzzledaisuki.view.congratulation.StartAnimationView;
import jp.sun_star_st.Puzzledaisuki.view.congratulation.StartAnimationView.OnstartAnimationListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

;

/**
 * Pluzzle Main
 * 
 * @author huynhtran
 * 
 */
@SuppressLint("HandlerLeak")
public class SliderMainActivity extends AbstractContentsActivity implements CongratulationClickListener {
	// AbstractContentsActivity {

	private String tag = "HuynhTD-" + SliderMainActivity.class.getSimpleName();
	private Bitmap bmpOrg = null;

	private int widthTile;
	private int scnWidth, scnHeight;

	private ImageView imgShow;
	private ImageView imgArea;
	private Button btnBack;
	private Button btnChoice;
	private Button btnReplay;
	private RelativeLayout rlRoots;
	private LinearLayout lnProgressBar;
	private RelativeLayout lnPluzzle;
	private LinearLayout lnControl;
	private PluzzleView pluzz;
	public String pathName = "";
	private String id = "";
	private ImageView imgBack;
	private StartAnimationView startAnimationView;
	private CongratulationView cgview;
	public boolean finish = false;
	private boolean isClick = false;
	protected MyGestureListener myGestureListener;

	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.slider_main;
	}

	@Override
	protected void initView(final Bundle savedInstanceState) {
		super.initView(savedInstanceState);
		try {
			rlRoots = ViewHelper.findView(this, R.id.rlRoots);
			lnProgressBar = ViewHelper.findView(this, R.id.lnProgressBar);
			lnPluzzle = ViewHelper.findView(this, R.id.lnPluzzle);
			lnControl = ViewHelper.findView(this, R.id.lnControl);
			imgShow = ViewHelper.findView(this, R.id.imgShow);
			btnBack = ViewHelper.findView(this, R.id.btnBack);
			btnChoice = ViewHelper.findView(this, R.id.btnChoice);
			btnReplay = ViewHelper.findView(this, R.id.btnReplay);
			imgArea = ViewHelper.findView(this, R.id.imgArea);
			imgBack = ViewHelper.findView(this, R.id.imgBack);

			initData();

			// createLayout();
			setListenerControl();

			// Intent intent = new Intent(PluzzleMainActivity.this, SliderFloat.class);
			// startActivity(intent);
			new LoadData().execute();

			myGestureListener = new MyGestureListener(this);
		}
		catch (Exception e) {
			ShowLog.showLogError(tag, "initData error" + e);
			if (Constant.IS_PrintStackTrace)
				e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void initData() {
		Drawable dbmp;
		Bitmap bmpTmp1, bmpTmp2;
		Intent intent;
		int imgW = 100;
		int wArea = 200;
		int hArea = 200;
		int gameW = 100, controlW = 100, controlH = 100;
		try {
			System.gc();
			intent = this.getIntent();
			pathName = intent.getStringExtra(Constant.PATH_NAME);
			id = intent.getStringExtra(Constant.GAME_ID);
			finish = intent.getBooleanExtra(Constant.FINISH_GAME, false);

			cgview = new CongratulationView(this);
			if (!finish)
				cgview.setFinishType(FinishType.finish1);
			else
				cgview.setFinishType(FinishType.finish2);
			cgview.setVisibility(View.GONE);
			rlRoots.addView(cgview);

			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			startAnimationView = new StartAnimationView(this);
			startAnimationView.setLayoutParams(param);
			rlRoots.addView(startAnimationView);

			scnWidth = CustomSharedPreferences.getPreferences(Constant.WIDTH_SCREEN, 480);
			scnHeight = CustomSharedPreferences.getPreferences(Constant.HEIGHT_SCREEN, 320);

			widthTile = scnHeight / 4 * 87 / 100;

			ShowLog.showLogInfo(tag, "initData() screen width, height=" + scnWidth + ", " + scnHeight);

			wArea = widthTile * 5 + 28;
			hArea = widthTile * 4 + 25;

			switch (scnHeight) {
			case 320:
				imgW = (int) (widthTile * 1.3);
				gameW = widthTile;
				controlW = widthTile / 2;
				break;
			case 480:
				imgW = (int) (widthTile * 1.4) + 10;
				gameW = (int) (widthTile * 1.2) - 5;
				controlW = (int) (widthTile / 1.8) + 10;
				controlH = (int) (widthTile * 1.3) + 10;
				wArea = (int) (widthTile * 5.5);
				hArea = widthTile * 4 + 42;
				widthTile = widthTile + 5;
				break;
			case 540:
				imgW = (int) (widthTile * 1.5) + 15;
				gameW = (int) (widthTile * 1.5);
				controlW = widthTile - 10;
				controlH = (int) (widthTile * 1.3) + 10;
				wArea = (int) (widthTile * 5.5) + 8;
				hArea = widthTile * 4 + 55;
				widthTile = widthTile + 8;

				break;
			case 720:
			default:
				imgW = (int) (widthTile * 1.5) + 15;
				gameW = (int) (widthTile * 1.5);
				controlW = widthTile - 15;
				controlH = (int) (widthTile * 1.3) + 10;
				widthTile = widthTile + 11;
				hArea = widthTile * 4 + 30;
				wArea = (int) (widthTile * 5) + 28;
				ShowLog.showLogInfo(tag, "initData widthW: " + gameW + "; controlW=" + controlW);
				break;
			}

			// get default bitmap
			// dbmp = getResources().getDrawable(R.drawable.miss);
			// bmpOrg = ((BitmapDrawable) dbmp).getBitmap();

			// pathName = Utility.getPathImg(this, "miss.jpg");
			ShowLog.showLogInfo(tag, "initData Path name: " + pathName);
			if (pathName.equals("")) {
				ShowLog.showLogError(tag, "initData can't get file name");
				return;
			}
			bmpOrg = Utility.decodeBitmapFromFile(pathName, widthTile * 4, widthTile * 4);

			// //////////// test only !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// // ////////////////////////////////////////////
			// if (bmpOrg == null) {
			// Log.d(tag, "initData ==== get image temp");
			//
			// // get default bitmap
			// dbmp = getResources().getDrawable(R.drawable.miss);
			// bmpOrg = ((BitmapDrawable) dbmp).getBitmap();
			// }
			// ///////////////

			// dbmp = getResources().getDrawable(R.drawable.blank);
			// bmpBlank = ((BitmapDrawable) dbmp).getBitmap();

			// Layout game
			RelativeLayout.LayoutParams layout_param = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layout_param.setMargins(gameW, 0, 0, 0);
			lnPluzzle.setLayoutParams(layout_param);

			// //////
			layout_param = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
			layout_param.setMargins(controlW, controlH, 5, 5);
			lnControl.setLayoutParams(layout_param);

			// ////image area game
			// layout_param = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
			// layout_param.setMargins(gameW, 0, 0, 0);
			// imgArea.setLayoutParams(layout_param);
			System.gc();
			dbmp = getResources().getDrawable(R.drawable.slider_area);
			bmpTmp1 = ((BitmapDrawable) dbmp).getBitmap();
			// bmpTmp2 = Utility.getResizedBitmap(bmpTmp1, wArea, wArea, true);
			bmpTmp2 = Utility.getResizedBitmap(bmpTmp1, 100, 100, true);
			// bmpTmp2 = bmpTmp1;
			imgArea.setImageBitmap(bmpTmp2);
			imgArea.getLayoutParams().width = wArea;
			imgArea.getLayoutParams().height = hArea;

			imgArea.setScaleType(ScaleType.FIT_XY);
			// //////
			// ShowLog.showLogInfo(tag, "initData size: gc imgShow1=" + bmpTmp2.getByteCount() + "; bmpOrg=" + bmpOrg.getByteCount());
			// //////Image
			bmpTmp2 = Utility.getResizedBitmap(bmpOrg, imgW, imgW, false);
			// ShowLog.showLogInfo(tag, "initData size: gc imgShow2=" + bmpTmp2.getByteCount() + "; bmpOrg=" + bmpOrg.getByteCount());
			// bmpTmp2 = bmpTmp1;
			// imgShow.setImageBitmap(bmpOrg);
			imgShow.setImageBitmap(bmpTmp2);

			imgShow.getLayoutParams().width = imgW;
			imgShow.getLayoutParams().height = imgW;
			// imgShow.setScaleType(ScaleType.FIT_XY);

			// ////////
			CustomSharedPreferences.setPreferences(Constant.WIDTH_TILE, widthTile);

			// ////////music
			SoundManager.initSounds(this);
			SoundManager.playSound(Constant.SOUND_E, true);
			// ////////////

			dbmp = null;
			bmpTmp1 = null;
			bmpTmp2 = null;
			System.gc();
			// bmpOrg = null;
		}
		catch (Exception e) {
			ShowLog.showLogError(tag, "initData error" + e);
			if (Constant.IS_PrintStackTrace)
				e.printStackTrace();
		}
	}

	private void setListenerControl() {

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (isClick)
						return;
					isClick = true;
					// showWin();
					SoundManager.stopSound(Constant.SOUND_E);
					SoundManager.playSound(Constant.SOUND_D, false);
					SoundManager.isLEFT =3;
					Intent intent = new Intent(SliderMainActivity.this, StartupApp.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					finish();
				}
				catch (Exception e) {
					ShowLog.showLogError(tag, "Back error: " + e.getMessage());
				}
			}
		});

		btnChoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowLog.showLogInfo(tag, "move SliderImageActivity");
				if (isClick)
					return;
				isClick = true;
				SoundManager.stopSound(Constant.SOUND_E);
				SoundManager.playSound(Constant.SOUND_D, false);
				Intent intent = new Intent(SliderMainActivity.this, SliderImageActivity.class);
				intent.putExtra(Constant.FLAG_ANIM_ISLEFT, false);
				SliderMainActivity.this.startActivity(intent);
				finish();

			}
		});

		btnReplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				pluzz.setClickable(false);
				pluzz.sortData();
				pluzz.isTime = true;
				// pluzz.playMusicBackground();
				SoundManager.playSound(Constant.SOUND_E, true);
				SoundManager.playSound(Constant.SOUND_C, false);
				startAnimationView.Start();
			}
		});

		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (isClick)
						return;
					isClick = true;
					// showWin();
					SoundManager.stopSound(Constant.SOUND_E);
					SoundManager.playSound(Constant.SOUND_D, false);
					Intent intent = new Intent(SliderMainActivity.this, SliderImageActivity.class);
					intent.putExtra(Constant.FLAG_ANIM_ISLEFT, false);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					SliderMainActivity.this.startActivity(intent);
					finish();
				}
				catch (Exception e) {
					ShowLog.showLogError(tag, "Back error: " + e.getMessage());
				}
			}
		});

		startAnimationView.SetAnimationListener(new OnstartAnimationListener() {

			@Override
			public void animaitonFinish() {
				pluzz.isTime = false;

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		SoundManager.playSound(Constant.SOUND_E, true);
		ShowLog.showLogInfo(tag, "onStart");
	}

	@Override
	protected void onPause() {
		super.onPause();
		SoundManager.stopSound(Constant.SOUND_E);
		ShowLog.showLogInfo(tag, "onPause");
	}

	
	 @Override
	 protected void onStop() {
		 super.onStop();
		 cgview.Stop();
	 	ShowLog.showLogInfo(tag, "onStop");
	 }
	//
	// @Override
	// protected void onResume() {
	// super.onResume();
	// ShowLog.showLogInfo(tag, "onResume");
	// }
	//
	// @Override
	// protected void onRestart() {
	// super.onRestart();
	// ShowLog.showLogInfo(tag, "onRestart");
	// }

	@Override
	protected void onDestroy() {

		try {

			ShowLog.showLogInfo(tag, "onDestroy.... ");
			if (bmpOrg != null) {
				bmpOrg.recycle();
				bmpOrg = null;
				ShowLog.showLogInfo(tag, "onDestroy recycle bmpOrg ");
			}
			SoundManager.stopSound(Constant.SOUND_E);
			if (pluzz != null)
				pluzz.onDestroy();
			unbindDrawables(findViewById(R.id.lnRoot));

			System.gc();
			super.onDestroy();
		}
		catch (Exception e) {
			ShowLog.showLogError(tag, "onDestroy error" + e);
			if (Constant.IS_PrintStackTrace)
				e.printStackTrace();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// return super.onTouchEvent(event);
		return myGestureListener.getDetector().onTouchEvent(event);
	}

	private void unbindDrawables(View view) {
		try {
			if (view == null)
				return;
			if (view.getBackground() != null) {
				view.getBackground().setCallback(null);
			}
			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));
				}
				((ViewGroup) view).removeAllViews();
			}
		}
		catch (Exception e) {
			ShowLog.showLogError(tag, "unbindDrawables error: " + e.getMessage());
		}
	}

	private class LoadData extends AsyncTask<Object, Object, Object> {
		// ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				if (isFinishing())
					return;
				lnProgressBar.setVisibility(View.VISIBLE);
				// pd = ProgressDialog.show(PluzzleMainActivity.this,null, null,false);
				ShowLog.showLogInfo(tag, "start animation........");
				startAnimationView.Start();
				// timer = new Timer();
				// count = 0;
			}
			catch (Exception e) {
				ShowLog.showLogError(tag, "Load Data error: " + e);
			}
		}

		@Override
		protected Object doInBackground(Object... params) {
			if (!isFinishing()) {
				pluzz = new PluzzleView(SliderMainActivity.this);
				pluzz.init(4, 4, bmpOrg);
				bmpOrg = null;
				// //////test
				pluzz.setOnTouchListener(myGestureListener);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (!isFinishing()) {
				pluzz.isTime = true;
				lnPluzzle.addView(pluzz);
				lnProgressBar.setVisibility(View.GONE);
			}
		}

	}

	private void savePlayed() {
		try {
			if (id.equals(""))
				return;
			GameHistoryDataHelper dataHelper = new GameHistoryDataHelper(getApplicationContext());
			dataHelper.update(id, 1);
			ShowLog.showLogInfo(tag, "savePlayed saved");
		}
		catch (Exception e) {
			if (Constant.IS_PrintStackTrace)
				e.printStackTrace();
		}
	}

	public void showWin() {
		// cgview = new CongratulationView(this);
		try {
			// System.gc();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					cgview.setVisibility(View.VISIBLE);

					cgview.Start();
					savePlayed();
					SoundManager.stopSound(Constant.SOUND_E);
				}
			}, 1000);

			// cgview.setVisibility(View.VISIBLE);
			if (cgview.getPathImage() == null || cgview.getPathImage().equals("")) {
				cgview.setPathImage(pathName);
				cgview.setGameType(GameType.Slider);
				cgview.setFinishType(FinishType.finish2);
				cgview.setControlsClickListener(this);
			}
			//
			// // pluzz.stopMusicBackground();
			// // SoundManager.stopSound(Constant.SOUND_E);
			// // pluzzleMain.setContentView(cgview);
			// cgview.Start();
			// savePlayed();
			System.gc();
		}
		catch (Exception e) {

		}
		// rlRoots.addView(cgview);

	}

	@Override
	public void OnClickButtonReplay() {
		ShowLog.showLogInfo(tag, "button replay...");
		// unbindDrawables(cgview);
		// rlRoots.removeView(cgview);
		cgview.setVisibility(View.GONE);
		pluzz.sortData();
		SoundManager.playSound(Constant.SOUND_E, true);
		SoundManager.playSound(Constant.SOUND_C, false);
		pluzz.isTime = true;

		// /start animation
		startAnimationView.Start();
		// waitTimer.start();
		// rlRoots.invalidate();
		// initData();
		// new LoadData().execute();

	}

	@Override
	public void OnClickButtonChoice() {
		try {
			if (isClick)
				return;
			isClick = true;
			SoundManager.stopSound(Constant.SOUND_E);
			SoundManager.playSound(Constant.SOUND_D, false);
			Intent intent = new Intent(this, SliderImageActivity.class);
			intent.putExtra(Constant.FLAG_ANIM_ISLEFT, false);
			startActivity(intent);
			finish();
		}
		catch (Exception e) {
			ShowLog.showLogError(tag, "back error: " + e.getMessage());
		}
	}

	@Override
	public void OnClickButtonBack() {
		ShowLog.showLogInfo(tag, "button back...");
		if (isClick)
			return;
		isClick = true;
		SoundManager.stopSound(Constant.SOUND_E);
		SoundManager.playSound(Constant.SOUND_D, false);
		SoundManager.isLEFT =3;
		Intent intent = new Intent(SliderMainActivity.this, StartupApp.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		try {
			if (pluzz.isTime)
				return;
			Intent intent = new Intent(this, SliderImageActivity.class);
			intent.putExtra(Constant.FLAG_ANIM_ISLEFT, false);
			startActivity(intent);
			SoundManager.playSound(Constant.SOUND_D, false);
			finish();
		}
		catch (Exception e) {
			ShowLog.showLogError(tag, "back error: " + e.getMessage());
		}
	}

	@Override
	public void onScreenOn(boolean isunlock) {

		// super.onScreenOn(isunlock);
		if (isunlock && !Utility.isApplicationSentToBackground(getApplicationContext())) {
			SoundManager.playSound(Constant.SOUND_E, true);
		}
		else {
			SoundManager.pauseSound(Constant.SOUND_E);
		}

		ShowLog.showLogInfo(tag, "onScreenOn is: " + isunlock);

	}

	@Override
	protected void onResume() {
		super.onResume();
		isClick = false;
	}

	// /////////////////////////////////////////////////////////////////////////
	class MyGestureListener extends SimpleOnGestureListener implements OnTouchListener {
		Context context;
		GestureDetector gDetector;

		public MyGestureListener() {
			super();
		}

		public MyGestureListener(Context context) {
			this(context, null);
		}

		public MyGestureListener(Context context, GestureDetector gDetector) {

			if (gDetector == null)
				gDetector = new GestureDetector(context, this);

			this.context = context;
			this.gDetector = gDetector;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			ShowLog.showLogInfo(tag, "fling: e1: " + e1.getX() + "," + e1.getY() + "; e2: " + e2.getX() + "," + e2.getY());
			if (pluzz.isTime)
				return false;
			if (pluzz.moveTileXY((int) (e1.getX()), (int) (e1.getY()))) {
				pluzz.moveEvent = true;
				// return false;
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e1) {
			ShowLog.showLogInfo(tag, "onSingleTapConfirmed: e1: " + e1.getX() + "," + e1.getY());
			// pluzz.moveTileXY((int) (e1.getX()), (int) (e1.getY()));

			return super.onSingleTapConfirmed(e1);
		}

		public boolean onTouch(View v, MotionEvent event) {

			// ShowLog.showLogInfo(tag, "onTouch: event: " + event.getX() + "," + event.getY());
			return gDetector.onTouchEvent(event);
		}

		public GestureDetector getDetector() {
			return gDetector;
		}
	}

}
