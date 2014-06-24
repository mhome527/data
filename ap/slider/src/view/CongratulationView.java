package puzzle.slider.vn.view;

import java.io.File;
import java.util.ArrayList;

import puzzle.slider.vn.R;
import puzzle.slider.vn.animation.ParticleAnimationView;
import puzzle.slider.vn.animation.ParticleAnimationView.ParticleListener;
import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.CustomSharedPreferences;
import puzzle.slider.vn.util.ScaleImage;
import puzzle.slider.vn.util.ShowLog;
import puzzle.slider.vn.util.ViewHelper;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CongratulationView extends RelativeLayout implements ParticleListener {

	private static String TAG = CongratulationView.class.getSimpleName();
	private Button btnReplay;
	private Button btnChoice;
	private ImageView imgItem;
	private LinearLayout layoutBottom;
	private Bitmap bmpath;
	private String path;
	/*
	 * 
	 * */
	private MediaPlayer player_background = null;
	private MediaPlayer player1 = null;
	private MediaPlayer player2 = null;
	private MediaPlayer player3 = null;

	private int max_circle_size = 50;
	private int bm_size = 50;
	/*
	 * config for Text Animation
	 */
	private int indexNumberText;
	private int NUMBER_TEXT_CONGRATULATION = 1;
	private int TEXT_WIDTH_NORMAL = 80;
	private int TEXT_HEIGHT_NORMAL = 80;

	private int CGL_TEXT_RATE = 2;
	private int Y_TEXT_START;
	private ArrayList<ImageView> buttonsText;
	private RelativeLayout rltexts;

	/*
	 * 
	 * */
	// cho trang thai screen off
	Boolean screenOff = false;
	// luu thong tin animation dang thuc hien toi buoc nao khi screen off
	int step;
	//
	Boolean particleFinish;
	ParticleAnimationView particle;
	CongratulationClickListener controlsdelegate;

	public interface CongratulationClickListener {
		public void OnClickButtonReplay();

		public void OnClickButtonChoice();
	}

	public CongratulationView(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.congratulation, this, true);

		int height = CustomSharedPreferences.getPreferences(Constant.HEIGHT_SCREEN, 320);

		// txtTitle = (TextView) findViewById(R.id.txtMessage);
		btnReplay = (Button) findViewById(R.id.btnReplay);
		btnChoice = (Button) findViewById(R.id.btnChoice);
		// circleAni = (ImageView) findViewById(R.id.imgCircle);
		imgItem = (ImageView) findViewById(R.id.imgItem);
		layoutBottom = (LinearLayout) findViewById(R.id.layout_bottom_control);
		rltexts = ViewHelper.findView(this, R.id.rlTextanimation);

		// txtTitle.setVisibility(INVISIBLE);
		layoutBottom.setVisibility(INVISIBLE);
		rltexts.setVisibility(GONE);
		
		indexNumberText = 0;
		Y_TEXT_START = height + 30;

		particle = (ParticleAnimationView) findViewById(R.id.particleAnimationView1);
		particle.setParticleListener(this);


		btnReplay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (controlsdelegate != null) {
					controlsdelegate.OnClickButtonReplay();
					StopAllSound();
				}
			}
		});

		btnChoice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (controlsdelegate != null) {
					controlsdelegate.OnClickButtonChoice();
					StopAllSound();
				}
			}
		});

	}

	@Override
	public void destroyDrawingCache() {
		super.destroyDrawingCache();
		ShowLog.i(TAG, "Congratulation destroyDrawingCache");
		StopAllSound();
		if (null != bmpath && !bmpath.isRecycled()) {
			bmpath.recycle();
		}
		bmpath = null;
		path = null;
		System.gc();

	}

	@Override
	protected void onDetachedFromWindow() {

		ShowLog.i(TAG, "onDetachedFromWindow");
		StopAllSound();
		if (null != bmpath && !bmpath.isRecycled()) {
			bmpath.recycle();
		}
		bmpath = null;
		path = null;
		System.gc();

		super.onDetachedFromWindow();
	}

	private void StopAllSound() {

		ShowLog.i(TAG, "Stop all sound");
		try {
			if (player1 != null) {
				player1.stop();
				player1.release();
				player1 = null;
			}
			if (player2 != null) {
				player2.stop();
				player2.release();
				player2 = null;
			}
			if (player3 != null) {
				player3.stop();
				player3.release();
				player3 = null;
			}
			if (player_background != null) {
				player_background.stop();
				player_background.release();
				player_background = null;
			}
		} catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}
	}

	private void LoadAllSound() {
		loadSound1();
		loadSound2();
		loadSound3();
	}

	public void setPathImage(String path) {

		ShowLog.i(TAG, "setpath");

		this.path = path;
		File f = new File(path);
		if (f.exists()) {

			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			DisplayMetrics displaymetrics = new DisplayMetrics();
			display.getMetrics(displaymetrics);

			ScaleImage scale = new ScaleImage(displaymetrics);

			bmpath = scale.getResizedBitmap(path, 0.84f);
			imgItem.setImageBitmap(bmpath);

		} else {
			ShowLog.e(TAG, "Error: path file not found!");
		}

	}


	public void setControlsClickListener(CongratulationClickListener listener) {
		this.controlsdelegate = listener;
	}

	public void Start() {

		ShowLog.i(TAG, "start animaiton");

		init();
		// prepare for animation
		startCircleAnimation();

	}

	public void Pause(Boolean isScreenOff) {

		ShowLog.i(TAG, "pause animaiton");
		this.screenOff = isScreenOff;
		StopAllSound();

	}


	public void Stop() {

		ShowLog.i(TAG, "stop animaiton");
		StopAllSound();

	}

	private void init() {
		Bitmap bmp = null;
		ShowLog.i(TAG, "init animation");
		//
		layoutBottom.setVisibility(INVISIBLE);
		//
		step = 0;

		LoadAllSound();
		
		if (null != buttonsText) {
			buttonsText.clear();
			buttonsText = null;
		}
		buttonsText = new ArrayList<ImageView>();
		rltexts.setVisibility(VISIBLE);
		//

		ImageView imgChar = new ImageView(getContext());
		this.rltexts.addView(imgChar);
		buttonsText.add(imgChar);
		
		particleFinish = false;
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.money);
		particle.setDrawbleIcon(bmp);
		bmp.recycle();
		bmp = null;

	}

	private void Clear() {

		ShowLog.i(TAG, "clear");

		/*
		 * clear data of texts animation
		 */
		rltexts.setVisibility(GONE);
		if (buttonsText != null) {

			try {
				for (int i = 0; i < NUMBER_TEXT_CONGRATULATION; i++) {
					ImageView img = buttonsText.get(i);
					this.rltexts.removeView(img);
					BitmapDrawable drb = (BitmapDrawable) img.getBackground();
					if (drb != null) {
						drb.getBitmap().recycle();
						drb = null;
					}
					img.setBackgroundDrawable(null);
					img.setVisibility(GONE);
					img = null;
				}
				buttonsText.clear();
				buttonsText = null;
			} catch (Exception e) {
				ShowLog.e(TAG,  "Clear error:" + e.getMessage());
			}

		}

	}

	public void startCircleAnimation() {

		ShowLog.i(TAG, "start circle animation");

		AnimationSet aniSet = new AnimationSet(false);
		aniSet.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				// dang thuc hien animation 1 da thuc hien xong. qua animation 2
				step = 1;
				// circleAni.setVisibility(ImageView.GONE);
				ShowLog.i(TAG, "startCircleAnimation onAnimationEnd");
				StartAnimationLoadtext();

			}
		});

		ScaleAnimation ani = new ScaleAnimation(1f, max_circle_size, 1f, max_circle_size, bm_size / 2, bm_size / 2);
		ani.setDuration(600);
		aniSet.addAnimation(ani);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setStartOffset(800);
		alphaAnimation.setDuration(600);
		// aniSet.addAnimation(alphaAnimation);

		try {
			if (player1 == null) {
				loadSound1();
			}
			player1.start();
		} catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}

		// circleAni.setVisibility(ImageView.VISIBLE);

		if (screenOff) {
			return;
		}

		CharacterAnimation();
		// circleAni.startAnimation(aniSet);

	}

	/*
	 * ------------------------------------------ Loadtext animation
	 */
	private void StartAnimationLoadtext() {

		ShowLog.i(TAG, "start animation text");

		/*
		 * neu truoc do dang thuc hien ma event screenOff dc goi --> tam dung; khi resume dc goi --> thuc hien tiep. neu screenoff == false ---> truoc do phai la truong hop tren
		 * --> khoi tao lai gia tri.
		 */
		if (!screenOff) {
			indexNumberText = 0;
		}

		CharacterAnimation();
	}

	private void CharacterAnimation() {

		if (indexNumberText == NUMBER_TEXT_CONGRATULATION) {

			if (!screenOff) {
				//dang thuc hien animation 2, qua animation 3.
				step = 2;

				try {
					if (player3 == null) {
						loadSound3();
					}
					player3.start();
				} catch (Exception e) {
					if (Constant.IS_PrintStackTrace) {
						e.printStackTrace();
					}
				}

//				SoundManager.playSound(Constant.SOUND_A, true);
				particle.setStart(true);
			}

			return;
		}

		ImageView imgv = buttonsText.get(indexNumberText);
		TransitionAnimationCharacter(imgv);

		//
		indexNumberText++;
	}

	private void TransitionAnimationCharacter(ImageView img) {

		AnimationSet aniSet = new AnimationSet(true);
		aniSet.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				/*
				 * a text is finish translate animation.
				 */
				if (!screenOff) {
					CharacterAnimation();
				}

			}
		});

		ScaleAnimation scale = new ScaleAnimation(CGL_TEXT_RATE, 1, CGL_TEXT_RATE, 1, TEXT_WIDTH_NORMAL, TEXT_HEIGHT_NORMAL);
		scale.setDuration(180);

		aniSet.addAnimation(scale);


		TranslateAnimation trans = new TranslateAnimation(0, 0, Y_TEXT_START, 0);
		trans.setDuration(160);
		aniSet.addAnimation(trans);

	
		img.setVisibility(VISIBLE);
		img.startAnimation(aniSet);
	}

	/*
	 * animation alpha
	 */

	private void startAlphaAnimationText() {

		ShowLog.i(TAG, "start animation hidden text");

		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
		alpha.setStartOffset(200);
		alpha.setDuration(700);
		alpha.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// finish all animation --> clear memory
				rltexts.setVisibility(GONE);
				Clear();
			}
		});

		rltexts.startAnimation(alpha);
	}

	private void ShowButtonControls() {

		AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
		alpha.setStartOffset(200);
		alpha.setDuration(700);
		// alpha.setFillAfter(true);

		layoutBottom.startAnimation(alpha);
		layoutBottom.setVisibility(VISIBLE);
	}
	public Bitmap getResizedBitmap(int res, float scale_width, float scale_height) {

		Bitmap bm = BitmapFactory.decodeResource(getResources(), res);
		int width = bm.getWidth();
		int height = bm.getHeight();

		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scale_width, scale_height);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		bm.recycle();
		bm = null;

		return resizedBitmap;

	}

	public Bitmap getResizedBitmap(Bitmap bm, float scale_width, float scale_height) {

		int width = bm.getWidth();
		int height = bm.getHeight();

		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scale_width, scale_height);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;

	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		return getResizedBitmap(bm, scaleWidth, scaleHeight);

	}

	private void loadSound1() {

		try {

			if (player1 != null) {
				try {
					if (player1.isPlaying()) {
						player1.stop();
					}
					player1.release();
					player1 = null;
				} catch (Exception e) {

				}
			}

			player1 = new MediaPlayer();

			AssetFileDescriptor descriptor = getResources().getAssets().openFd(Constant.SOUND_J);
			player1.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			player1.prepare();

		} catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}

	}

	private void loadSound2() {
		// sound loadtext
		try {

			if (player2 != null) {
				try {
					if (player2.isPlaying()) {
						player2.stop();
					}
					player2.release();
					player2 = null;
				} catch (Exception e) {

				}
			}

			player2 = new MediaPlayer();

			AssetFileDescriptor descriptor = getResources().getAssets().openFd(Constant.SOUND_I);
			player2.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			player2.prepare();

		} catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}
	}

	private void loadSound3() {
		try {
			if (player3 != null) {
				try {
					if (player3.isPlaying()) {
						player3.stop();
					}
					player3.release();
					player3 = null;
				} catch (Exception e) {

				}
			}

			player3 = new MediaPlayer();
			AssetFileDescriptor descriptor = getResources().getAssets().openFd(Constant.SOUND_K);
			player3.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			player3.prepare();

		} catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void ParticleCompleted() {

		ShowLog.i(TAG, "finish particle animation");

		// finish particle
		step = 3;
		particleFinish = true;
		startAlphaAnimationText();
		ShowButtonControls();
//		if (!screenOff) {
//			SoundManager.playSound(Constant.SOUND_A, true);
//		}
	}

	public String getPathImage() {
		return path;
	}
}
