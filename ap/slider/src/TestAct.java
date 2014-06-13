package puzzle.slider.vn;

import puzzle.slider.vn.SliderMainActivity.MyGestureListener;
import puzzle.slider.vn.animation.StartAnimationView;
import puzzle.slider.vn.animation.StartAnimationView.OnstartAnimationListener;
import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.CustomSharedPreferences;
import puzzle.slider.vn.util.ShowLog;
import puzzle.slider.vn.util.Utility;
import puzzle.slider.vn.util.ViewHelper;
import puzzle.slider.vn.view.CongratulationView;
import puzzle.slider.vn.view.PluzzleView;
import puzzle.slider.vn.view.CongratulationView.CongratulationClickListener;
import puzzle.slider.vn.view.CongratulationView.FinishType;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class TestAct extends AbstractContentsActivity implements CongratulationClickListener {

	private String tag = SliderMainActivity.class.getSimpleName();
	protected MyGestureListener myGestureListener;

	private Bitmap bmpOrg = null;

	private int widthTile;
	private int scnWidth, scnHeight;

	private ImageView imgShow;
	private ImageView imgArea;
	private Button btnChoice;
	private Button btnReplay;
	private RelativeLayout rlRoots;
	private LinearLayout lnProgressBar;
	private RelativeLayout lnPluzzle;
	private LinearLayout lnControl;
	private PluzzleView pluzz;
	private LinearLayout llBg1;
	private LinearLayout llBg2;
	// public String pathName = "";
	// private String id = "";
	// private ImageView imgBack;
	private StartAnimationView startAnimationView;
	private CongratulationView cgview;
	public boolean finish = false;
	private boolean isClick = false;
	private int idGame;

	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.slider_main;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.initView(savedInstanceState);

		try {
			rlRoots = ViewHelper.findView(this, R.id.rlRoots);
			lnProgressBar = ViewHelper.findView(this, R.id.lnProgressBar);
			lnPluzzle = ViewHelper.findView(this, R.id.lnPluzzle);
			lnControl = ViewHelper.findView(this, R.id.lnControl);
			imgShow = ViewHelper.findView(this, R.id.imgShow);
//			btnChoice = ViewHelper.findView(this, R.id.btnChoice);
			btnReplay = ViewHelper.findView(this, R.id.btnReplay);
//			imgArea = ViewHelper.findView(this, R.id.imgArea);
			// imgBack = ViewHelper.findView(this, R.id.imgBack);
			llBg1 = ViewHelper.findView(this, R.id.llBg1);
			llBg2 = ViewHelper.findView(this, R.id.llBg2);

			initData();

			 setListenerControl();

			// Intent intent = new Intent(PluzzleMainActivity.this, SliderFloat.class);
			// startActivity(intent);

			myGestureListener = new MyGestureListener(this);
			 new LoadData().execute();

		} catch (Exception e) {
			ShowLog.e(tag, "initData error" + e);
			if (Constant.IS_PrintStackTrace)
				e.printStackTrace();
		}

		myGestureListener = new MyGestureListener(this);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		ShowLog.i("Test", "touch....");
		// return super.onTouchEvent(event);
		return myGestureListener.getDetector().onTouchEvent(event);
	}

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
			// pathName = intent.getStringExtra(Constant.PATH_NAME);
			// id = intent.getStringExtra(Constant.GAME_ID);
			finish = intent.getBooleanExtra(Constant.FINISH_GAME, false);

			idGame = intent.getIntExtra(Constant.GAME_ID, -1);

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

			// landscape
			scnWidth = CustomSharedPreferences.getPreferences(Constant.HEIGHT_SCREEN, 480);
			scnHeight = CustomSharedPreferences.getPreferences(Constant.WIDTH_SCREEN, 320);

			widthTile = scnHeight / 4 * 87 / 100;

			ShowLog.i(tag, "initData() screen width, height=" + scnWidth + ", " + scnHeight + "; widthTile:" + widthTile);

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
				ShowLog.i(tag, "initData widthW: " + gameW + "; controlW=" + controlW);
				break;
			}

			// get default bitmap
			// dbmp = getResources().getDrawable(R.drawable.miss);
			// bmpOrg = ((BitmapDrawable) dbmp).getBitmap();

			// pathName = Utility.getPathImg(this, "miss.jpg");
			// ShowLog.showLogInfo(tag, "initData Path name: " + pathName);
			// if (pathName.equals("")) {
			// ShowLog.showLogError(tag, "initData can't get file name");
			// return;
			// }
			// bmpOrg = Utility.decodeBitmapFromFile(pathName, widthTile * 4, widthTile * 4);
			bmpOrg = Utility.decodeBitmapFromResource(this.getResources(), idGame, widthTile * 4, widthTile * 4);

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
			RelativeLayout.LayoutParams layout_param = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
			// bmpTmp2 = Utility.getResizedBitmap(bmpTmp1, 100, 100, true);
			// // bmpTmp2 = bmpTmp1;
			// imgArea.setImageBitmap(bmpTmp2);
			// imgArea.getLayoutParams().width = wArea;
			// imgArea.getLayoutParams().height = hArea;

			// imgArea.setScaleType(ScaleType.FIT_XY);

			llBg1.getLayoutParams().width = wArea;
			llBg1.getLayoutParams().height = hArea;

			llBg2.getLayoutParams().width = widthTile;
			llBg2.getLayoutParams().height = hArea;

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
		} catch (Exception e) {
			ShowLog.e(tag, "initData error" + e);
			// if (Constant.IS_PrintStackTrace)
			e.printStackTrace();
		}
	}

	private void setListenerControl() {

		btnChoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowLog.i(tag, "move SliderImageActivity");
				if (isClick)
					return;
				isClick = true;
				SoundManager.stopSound(Constant.SOUND_E);
				SoundManager.playSound(Constant.SOUND_D, false);
				Intent intent = new Intent(TestAct.this, SliderImageActivity.class);
				intent.putExtra(Constant.FLAG_ANIM_ISLEFT, false);
				TestAct.this.startActivity(intent);
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

		startAnimationView.SetAnimationListener(new OnstartAnimationListener() {

			@Override
			public void animaitonFinish() {
				pluzz.isTime = false;

			}
		});
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
			ShowLog.i(tag, "fling: e1: " + e1.getX() + "," + e1.getY() + "; e2: " + e2.getX() + "," + e2.getY());
			// if (pluzz.isTime)
			// return false;
			// if (pluzz.moveTileXY((int) (e1.getX()), (int) (e1.getY()))) {
			// pluzz.moveEvent = true;
			// // return false;
			// }
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e1) {
			ShowLog.i(tag, "onSingleTapConfirmed: e1: " + e1.getX() + "," + e1.getY());
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

	@Override
	public void OnClickButtonReplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnClickButtonChoice() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnClickButtonBack() {
		// TODO Auto-generated method stub

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
				ShowLog.i(tag, "start animation........");
				startAnimationView.Start();
				// timer = new Timer();
				// count = 0;
			} catch (Exception e) {
				ShowLog.e(tag, "Load Data error: " + e);
			}
		}

		@Override
		protected Object doInBackground(Object... params) {
			if (!isFinishing()) {
				pluzz = new PluzzleView(TestAct.this);
				pluzz.init(4, 4, bmpOrg);
				bmpOrg = null;
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
//				pluzz.isTime = true;
				lnPluzzle.addView(pluzz);
				lnProgressBar.setVisibility(View.GONE);
			}
		}

	}
}
