package puzzle.slider.vn;

import puzzle.slider.vn.animation.StartAnimationView;
import puzzle.slider.vn.animation.StartAnimationView.OnstartAnimationListener;
import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.CustomSharedPreferences;
import puzzle.slider.vn.util.ShowLog;
import puzzle.slider.vn.util.Utility;
import puzzle.slider.vn.util.ViewHelper;
import puzzle.slider.vn.view.CongratulationView;
import puzzle.slider.vn.view.CongratulationView.CongratulationClickListener;
import puzzle.slider.vn.view.PluzzleView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * Pluzzle Main
 * 
 */
@SuppressLint("HandlerLeak")
public class SliderMainActivity extends AbstractContentsActivity implements CongratulationClickListener {
	// AbstractContentsActivity {

	private String tag = SliderMainActivity.class.getSimpleName();

	private int widthTile;
	private int scnWidth, scnHeight;

	private ImageView imgShow;
	private Button btnReplay;
	private RelativeLayout rlRoots;
	private LinearLayout lnProgressBar;
	private RelativeLayout lnPluzzle;
	private PluzzleView pluzz;
	private RelativeLayout llBg1;
	private LinearLayout llBg2;
	private LinearLayout llBg3;
	private TextView tvTime;
	private StartAnimationView startAnimationView;
	private CongratulationView cgview;
	// public boolean finish = false;
	private boolean isClick = false;
	protected MyGestureListener myGestureListener;

	private int idGame;
	private int idGameS;

	// time
	private Handler customHandler = new Handler();
	private int mins = 0;
	private int hours = 0;
	private int secs = 0;

	private int row = 4;
	private int column = 4;

	private boolean isClickFinish = false;

	@Override
	protected int getViewLayoutId() {
		return R.layout.slider_main;
	}

	@Override
	protected void initView(final Bundle savedInstanceState) {
		super.initView(savedInstanceState);
		try {
			rlRoots = ViewHelper.findView(this, R.id.rlRoots);
			lnProgressBar = ViewHelper.findView(this, R.id.lnProgressBar);
			lnPluzzle = ViewHelper.findView(this, R.id.lnPluzzle);
			imgShow = ViewHelper.findView(this, R.id.imgShow);
			btnReplay = ViewHelper.findView(this, R.id.btnReplay);
			llBg1 = ViewHelper.findView(this, R.id.llBg1);
			llBg2 = ViewHelper.findView(this, R.id.llBg2);
			llBg3 = ViewHelper.findView(this, R.id.llBg3);

			tvTime = ViewHelper.findView(this, R.id.tvTime);
			initData();

			// createLayout();
			setListenerControl();

			new LoadData().execute();

			myGestureListener = new MyGestureListener(this);
		} catch (Exception e) {
			ShowLog.e(tag, "initData error" + e);
			if (Constant.IS_PrintStackTrace)
				e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void initData() {
		Intent intent;
		int imgW = 100;
		int wArea = 200;
		int hArea = 200;
		int gameW = 100;
		int wControl;
		try {
			System.gc();
			intent = this.getIntent();

			idGame = intent.getIntExtra(Constant.GAME_ID, -1);
			idGameS = intent.getIntExtra(Constant.GAME_ID_S, -1);

			cgview = new CongratulationView(this);
			cgview.setVisibility(View.GONE);
			rlRoots.addView(cgview);

			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			startAnimationView = new StartAnimationView(this);
			startAnimationView.setLayoutParams(param);
			rlRoots.addView(startAnimationView);

			// landscape
			scnWidth = CustomSharedPreferences.getPreferences(Constant.HEIGHT_SCREEN, 480);
			scnHeight = CustomSharedPreferences.getPreferences(Constant.WIDTH_SCREEN, 320);

			// widthTile = scnHeight / 4 * 87 / 100;

			ShowLog.i(tag, "initData() screen width, height=" + scnWidth + ", " + scnHeight + "; widthTile:" + widthTile);

			if (scnHeight < 720) {
				widthTile = scnHeight / row * 94 / 100;
				gameW = (int) (widthTile * 1.4);
				imgW = (int) (widthTile * 1.4) + 10;
				wArea = (int) (widthTile * (column + 1.3));
				hArea = widthTile * 4 + 42;
				wControl = (int)( widthTile * 2.4);
//				widthTile = widthTile + 5;

			} else {
				widthTile = scnHeight / row * 93 / 100;

				imgW = (int) (widthTile * 1.5) + 15;
				gameW = (int) (widthTile * 1.3);
//				widthTile = widthTile + 11;
				wArea = (int) (widthTile * (column + 1.2));
				hArea = widthTile * column + 30;
				wControl = (int)( widthTile * 2.3);
			}

			// Layout game
			RelativeLayout.LayoutParams layout_param = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			layout_param.setMargins(gameW, 0, 0, 0);
			lnPluzzle.setLayoutParams(layout_param);

			// System.gc();
			llBg1.getLayoutParams().width = wArea;
			llBg1.getLayoutParams().height = hArea;

			llBg2.getLayoutParams().width = widthTile + 8;
			RelativeLayout.LayoutParams param2 = (RelativeLayout.LayoutParams) llBg2.getLayoutParams();
			param2.setMargins(0, widthTile + 17, 0, 0);
			llBg2.setLayoutParams(param2);

			RelativeLayout.LayoutParams param3 = (RelativeLayout.LayoutParams) llBg3.getLayoutParams();
			param3.setMargins(0, widthTile + 19, 0, 0);
			llBg3.setLayoutParams(param3);
			llBg3.getLayoutParams().width = widthTile + 6;

			LinearLayout lnControl = ViewHelper.findView(this, R.id.lnControl);
			RelativeLayout.LayoutParams paramControl = (RelativeLayout.LayoutParams) lnControl.getLayoutParams();
			paramControl.width = wControl;
			lnControl.setLayoutParams(paramControl);

			btnReplay.getLayoutParams().width = widthTile;
			btnReplay.getLayoutParams().height = widthTile;

			// imgShow.setImageBitmap(bmpTmp2);
			imgShow.setImageResource(idGameS);

			imgShow.getLayoutParams().width = (int) (imgW * 1.3);
			imgShow.getLayoutParams().height = (int) (imgW * 1.3);
			// imgShow.setScaleType(ScaleType.FIT_XY);

			// ////////
			CustomSharedPreferences.setPreferences(Constant.WIDTH_TILE, widthTile);

		} catch (Exception e) {
			ShowLog.e(tag, "initData error" + e);
			// if (Constant.IS_PrintStackTrace)
			e.printStackTrace();
		}
	}

	private void setListenerControl() {

		btnReplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isClick)
					return;
				refreshGame();
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
		ShowLog.i(tag, "onStart");
	}

	@Override
	protected void onPause() {
		super.onPause();
		ShowLog.i(tag, "onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (cgview != null)
			cgview.Stop();
		ShowLog.i(tag, "onStop");
	}

	@Override
	protected void onDestroy() {

		try {

			ShowLog.i(tag, "onDestroy.... ");

			// SoundManager.stopSound(Constant.SOUND_E);
			unbindDrawables(findViewById(R.id.lnRoot));

			customHandler.removeCallbacks(updateTimerThread);
			System.gc();
			super.onDestroy();
		} catch (Exception e) {
			ShowLog.e(tag, "onDestroy error" + e);
			if (Constant.IS_PrintStackTrace)
				e.printStackTrace();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ShowLog.i(tag, "onTouchEvent........................................................");
		return myGestureListener.getDetector().onTouchEvent(event);
	}

	private void refreshGame() {
		showConfirmDialog(SliderMainActivity.this, "", SliderMainActivity.this.getString(R.string.configm_replay), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				pluzz.sortData();
				pluzz.isTime = false;

				// reset time
				mins = 0;
				hours = 0;
				secs = 0;
				customHandler.removeCallbacks(updateTimerThread);
				customHandler.postDelayed(updateTimerThread, 0);
				isClick = false;
			}

		}, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}

		});
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
		} catch (Exception e) {
			ShowLog.e(tag, "unbindDrawables error: " + e.getMessage());
		}
	}

	private class LoadData extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				if (!isFinishing())
					return;
				lnProgressBar.setVisibility(View.VISIBLE);

			} catch (Exception e) {
				ShowLog.e(tag, "Load Data error: " + e);
			}
		}

		@Override
		protected Object doInBackground(Object... params) {
			Bitmap bmp;
			if (!isFinishing()) {
				bmp = Utility.decodeBitmapFromResource(SliderMainActivity.this.getResources(), idGame, widthTile * 4, widthTile * 4);
				pluzz = new PluzzleView(SliderMainActivity.this);
				pluzz.init(4, 4, bmp);
				bmp = null;
				// //////test
				ShowLog.i(tag, "doInBackground set on Touch....");
				pluzz.setOnTouchListener(myGestureListener);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			ShowLog.i(tag, "postfinish........");

			if (!isFinishing()) {
				// pluzz.isTime = true;
				lnPluzzle.addView(pluzz);
				lnProgressBar.setVisibility(View.GONE);

				customHandler.postDelayed(updateTimerThread, 0);
			}
		}

	}

	private boolean checkTime(String newTime, String oldTime) {
		try {
			int newT = Integer.parseInt(newTime.replace(":", ""));
			int oldT = Integer.parseInt(oldTime.replace(":", ""));
			if (oldT == 0)
				return true;
			if (newT < oldT)
				return true;
			else
				return false;
		} catch (Exception e) {
		}
		return true;
	}

	public void showWin() {
		String time;
		try {
			isClick = true;
			isClickFinish = false;
			// //////////////
			CustomSharedPreferences.init(getApplicationContext());

			time = CustomSharedPreferences.getPreferences(idGame + "", "00:00:00") + "";
			if (checkTime(tvTime.getText().toString(), time))
				CustomSharedPreferences.setPreferences(idGame + "", tvTime.getText().toString());
			customHandler.removeCallbacks(updateTimerThread);

			// SoundManager.pauseSound(Constant.SOUND_A);
			// //////
			// System.gc();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					cgview.setVisibility(View.VISIBLE);

					cgview.Start();
					// savePlayed();
					// SoundManager.stopSound(Constant.SOUND_E);
				}
			}, 500);

			// cgview.setVisibility(View.VISIBLE);
			if (cgview.getPathImage() == null || cgview.getPathImage().equals("")) {
				// cgview.setGameType(GameType.Slider);
				cgview.setControlsClickListener(this);
			}
			System.gc();
		} catch (Exception e) {

		}
		// rlRoots.addView(cgview);

	}

	@Override
	public void OnClickButtonReplay() {
		ShowLog.i(tag, "button replay...");
		if (isClickFinish)
			return;
		isClickFinish = true;
		cgview.setVisibility(View.GONE);
		pluzz.sortData();
		pluzz.isTime = false;

		// reset time
		mins = 0;
		hours = 0;
		secs = 0;
		customHandler.postDelayed(updateTimerThread, 0);
		isClick = false;
	}

	@Override
	public void OnClickButtonChoice() {
		try {
			if (isClickFinish)
				return;
			isClickFinish = true;
			Intent intent = new Intent(this, SliderSelectActivity.class);
			intent.putExtra(Constant.KEY_LEVEL, Constant.KEY_DIFFICULT);
			startActivity(intent);
			finish();
			ShowLog.i(tag, "OnClickButtonChoice ");

		} catch (Exception e) {
			ShowLog.e(tag, "back error: " + e.getMessage());
		}
	}

	@Override
	public void onBackPressed() {
		if (cgview.getVisibility() == View.VISIBLE) {
			cgview.setVisibility(View.GONE);
			isClick = false;
		}
		Intent intent = new Intent(this, SliderSelectActivity.class);
		intent.putExtra(Constant.KEY_LEVEL, Constant.KEY_DIFFICULT);
		startActivity(intent);
		finish();
		ShowLog.i(tag, "onBackPressed ");

	}

	@Override
	public void onScreenOn(boolean isunlock) {
		ShowLog.i(tag, "onScreenOn is: " + isunlock);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isClick = false;
		isClickFinish = false;
	}

	// //////////////////////
	private Runnable updateTimerThread = new Runnable() {

		public void run() {
			secs = secs + 1;
			if (secs == 60) {
				mins = mins + 1;
				secs = 0;
			}
			if (mins == 60) {
				mins = 0;
				hours = hours + 1;
			}

			tvTime.setText(String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
			customHandler.postDelayed(this, 1000);
		}
	};

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
			ShowLog.i(tag, "fling: e1: " + e1.getX() + "," + e1.getY() + "; e2: " + e2.getX() + "," + e2.getY());
			if (pluzz.isTime) {
				ShowLog.i(tag, "onFling NOT MOVE FALSE");
				return false;
			}
			if (pluzz.moveTileXY((int) (e1.getX()), (int) (e1.getY()))) {
				ShowLog.i(tag, "fling: MOVE...");
				pluzz.moveEvent = true;
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e1) {
			ShowLog.i(tag, "onSingleTapConfirmed: e1: " + e1.getX() + "," + e1.getY());

			return super.onSingleTapConfirmed(e1);
		}

		public boolean onTouch(View v, MotionEvent event) {
			return gDetector.onTouchEvent(event);
		}

		public GestureDetector getDetector() {
			return gDetector;
		}
	}

}
