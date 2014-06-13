package puzzle.slider.vn.view;

import java.io.File;
import java.util.ArrayList;

import puzzle.slider.vn.R;
import puzzle.slider.vn.SoundManager;
import puzzle.slider.vn.animation.ParticleAnimationView;
import puzzle.slider.vn.animation.ParticleAnimationView.ParticleListener;
import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.CustomSharedPreferences;
import puzzle.slider.vn.util.ScaleImage;
import puzzle.slider.vn.util.Utility;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @sine Mar 14, 2013
 */

public class CongratulationView extends RelativeLayout implements ParticleListener {

	public enum GameType {
		/*
		 * slider: la dang di chuyen o^ tro^ng'. board: la dang keo' va tha.
		 */
		Slider, Board
	}

	public enum FinishType {
		finish1, finish2, finish3, slider
	}

	public interface CongratulationClickListener {
		public void OnClickButtonReplay();

		public void OnClickButtonChoice();

		public void OnClickButtonBack();
	}

	/*
	 * 
	 */

	TextView txtTitle;
	//
	ImageView btnleft, btnmid, btnright;
	//image 
	ImageView imgItem;
	//image circle zoomin
	ImageView circleAni;
	LinearLayout layoutBottom;
	Bitmap bmpath;
	String path;
	/*
	 * 
	 * */
	MediaPlayer player_background = null;
	MediaPlayer player1 = null;
	MediaPlayer player2 = null;
	MediaPlayer player3 = null;
	GameType gameType;
	FinishType finishType;
	/*
	 * config for Circle animation
	 */
	int max_circle_size = 0;
	int bm_size;
	/*
	 * config for Text Animation
	 */
	int indexNumberText;
	int NUMBER_TEXT_CONGRATULATION = 7;
	int TEXT_WIDTH_NORMAL = 80;
	int TEXT_HEIGHT_NORMAL = 80;
	int SPACE_BETWEEN_TEXT = 0;// PIXEL
	// kich thuoc text tang len 2 lan khi moi hien thi.
	int CGL_TEXT_RATE = 2;
	int SCALE_DELTA_X, SCALE_DELTA_Y;
	int Y_TEXT_START, Y_TEXT_END;
	int X_TEXT_START_POSITION;
	ArrayList<ImageView> buttonsText;
	RelativeLayout rltexts;
	
	/*
	 * 
	 * */
	//cho trang thai screen off
	Boolean screenOff=false;
	//luu thong tin animation dang thuc hien toi buoc nao khi screen off
	int step;
	//
	Boolean particleFinish;
	ParticleAnimationView particle;
	CongratulationClickListener controlsdelegate;

	public CongratulationView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.congratulation, this, true);

		/*
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		 */	
		int width = CustomSharedPreferences.getPreferences(Constant.WIDTH_SCREEN, 480);
		int height = CustomSharedPreferences.getPreferences(Constant.HEIGHT_SCREEN, 320);

		txtTitle = (TextView) findViewById(R.id.txtMessage);
		btnleft = (ImageView) findViewById(R.id.btnleft);
		btnmid = (ImageView) findViewById(R.id.btnmid);
		btnright = (ImageView) findViewById(R.id.btnright);
		circleAni = (ImageView) findViewById(R.id.imgCircle);
		imgItem = (ImageView) findViewById(R.id.imgItem);
		layoutBottom = (LinearLayout) findViewById(R.id.layout_bottom_control);
		rltexts = ViewHelper.findView(this, R.id.rlTextanimation);
		
		txtTitle.setVisibility(INVISIBLE);
		layoutBottom.setVisibility(INVISIBLE);
		rltexts.setVisibility(GONE);
		circleAni.setVisibility(GONE);
		circleAni.setImageBitmap(null);
		/*
		 * prepare for loadtext animation
		 */
		indexNumberText = 0;
		int allTextWidth = NUMBER_TEXT_CONGRATULATION * TEXT_WIDTH_NORMAL + (NUMBER_TEXT_CONGRATULATION - 1) * SPACE_BETWEEN_TEXT;
		X_TEXT_START_POSITION = (width - allTextWidth) / 2;
		Y_TEXT_END = (height - TEXT_HEIGHT_NORMAL) / 2;
		Y_TEXT_START = height + 30;
		
		/*
		 * prepare for loadtext animation
		 */
		
		particle = (ParticleAnimationView) findViewById(R.id.particleAnimationView1);		
		particle.setParticleListener(this);
		
		/*
		 * media player
		 */

		//LoadAllSound();
		
		btnleft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (controlsdelegate != null) {
					controlsdelegate.OnClickButtonReplay();
					StopAllSound();
				}
			}
		});

		btnmid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (controlsdelegate != null) {
					controlsdelegate.OnClickButtonChoice();
					StopAllSound();
				}
			}
		});

		btnright.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (controlsdelegate != null) {
					controlsdelegate.OnClickButtonBack();
					StopAllSound();
				}
			}
		});

	}

	@Override
	public void destroyDrawingCache() {
		super.destroyDrawingCache();
		
		/*
		 * note: khong duoc dung` unbindDrawable o day, vi destroyDrawingCache se~ dc goi khi set visibility cho view la GONE.
		 *  
		 * */
		this.ShowLog("Congratulation destroyDrawingCache");		
		StopAllSound();
		if ( null!=bmpath && !bmpath.isRecycled()) {
			bmpath.recycle();
		}
		bmpath = null;
		path=null;
		System.gc();

	}
	
	@Override
	protected void onDetachedFromWindow() {
		
		this.ShowLog("onDetachedFromWindow");		
		StopAllSound();
		if ( null!=bmpath && !bmpath.isRecycled()) {
			bmpath.recycle();
		}
		bmpath = null;
		path=null;
		System.gc();
		
		super.onDetachedFromWindow();
	}

	private void StopAllSound() {
		
		ShowLog("Stop all sound");
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
		}
		catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}
	}
	
	private void LoadAllSound(){
		
		loadSound1();
		loadSound2();
		loadSound3();
		loadSound4();
		
	}
	
	public void setPathImage(String path) {
		
		this.ShowLog("setpath");
		
		this.path = path;
		File f=new File(path);
		if (f.exists()) {
			
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			DisplayMetrics displaymetrics = new DisplayMetrics();
			display.getMetrics(displaymetrics);

			ScaleImage scale = new ScaleImage(displaymetrics);

			bmpath = scale.getResizedBitmap(path, 0.84f);
			imgItem.setImageBitmap(bmpath);

			int margin_top_bottom = ( (display.getHeight() - bmpath.getHeight())/2 )/2;
			
			FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) layoutBottom.getLayoutParams();
			//lp.width = bmpath.getWidth();
			//lp.height = scale.getSizeScale(0.08).getHeight();// get layout bottom height
			lp.bottomMargin = margin_top_bottom;

			layoutBottom.setLayoutParams(lp);
			
			lp = null;

			/*
			 * 
			 * */
			lp = (android.widget.FrameLayout.LayoutParams) txtTitle.getLayoutParams();
			lp.height = scale.getSizeScale(0.08).getHeight();
			//lp.width = bm.getWidth();
			//lp.topMargin = scale.getSizeScale(0.015).getHeight();
			lp.topMargin = margin_top_bottom;

			lp = null;
			
		}
		else{
			
			puzzle.slider.vn.util.ShowLog.e(CongratulationView.class.getSimpleName(), "Error: path file not found!!! please check again.");
						
		}

	}

	public void setGameType(GameType gt) {
		this.gameType = gt;
	}

	public void setFinishType(FinishType ft) {
		this.finishType = ft;

		if (gameType == GameType.Board) {
			if (finishType == FinishType.finish1) {
				txtTitle.setText(getResources().getText(R.string.congratulation1));
			}
			else if (finishType == FinishType.finish2) {
				txtTitle.setText(getResources().getText(R.string.congratulation2));
			}
			else {
				// (finishType == FinishType.finish3)
				txtTitle.setText(getResources().getText(R.string.congratulation3));
			}
		}
		else {
			/*
			 * ben iOS dang slider chi thay co 2 trang thai finish
			 */
			if (finishType == FinishType.finish1) {
				txtTitle.setText(getResources().getText(R.string.congratulation1_slider));
			}
			else if (finishType == FinishType.finish2) {
				txtTitle.setText(getResources().getText(R.string.congratulation2_slider));
			}

		}

	}

	public void setControlsClickListener(CongratulationClickListener listener) {
		this.controlsdelegate = listener;
	}

	public void Start() {
		
		this.ShowLog("start animaiton");
		
		init();
		//prepare for animation
		
		startCircleAnimation();

	}

	public void Pause(Boolean isScreenOff){
		
		ShowLog("pause animaiton");
		this.screenOff = isScreenOff;
		StopAllSound();
		
	}
	
	public void Resume(){
		
		ShowLog("resume animaiton");
		if (!SoundManager.isScreenOFF && !Utility.isApplicationSentToBackground(getContext())) {
			
			LoadAllSound();
			
			if (screenOff) {
				
				screenOff = false;
				if ( step == 0 ) {
					startCircleAnimation();
				}
				else if ( step == 1 ) {
					StartAnimationLoadtext();
				}
				else{
					/*
					 * checking sound background.
					 * step == 2
					 * */
					 
					CheckingSoundBackground();
					if (!particleFinish) {
						particle.setStart(true);
					}
					
				}
				
			}
			else{
				if (particleFinish) {
					CheckingSoundBackground();
				}
			}
						
		}		
		
	}
	
	public void Stop(){
		
		ShowLog("stop animaiton");
		StopAllSound();
		
	}
	
	private void init(){
		
		this.ShowLog("init animation");
		//
		layoutBottom.setVisibility(INVISIBLE);
		txtTitle.setVisibility(INVISIBLE);
		//
		step = 0;
		/*
		 * prepare for animation list.
		 */		
		LoadAllSound();	
		/*
		 * prepare for circle animation
		 */		
		int height = CustomSharedPreferences.getPreferences(Constant.HEIGHT_SCREEN, 320);
		int width = CustomSharedPreferences.getPreferences(Constant.WIDTH_SCREEN, 480);
		
		BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.glow_circle, o);		
		// new size
		height /= 3;		
		float scaleDelta = (float) ((height * 1.0) / o.outHeight);
				
		bm_size = (int) (o.outWidth*scaleDelta);
		max_circle_size = width / bm_size + 3;		
		o=null;
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bm_size, bm_size);
		params.addRule(CENTER_IN_PARENT);
		
		circleAni.setLayoutParams(params);		
		//circleAni.setBackgroundResource(R.drawable.glow_circle);
		Bitmap bmp = null;
		bmp = getResizedBitmap(R.drawable.glow_circle, scaleDelta, scaleDelta);
		circleAni.setBackgroundDrawable(new BitmapDrawable(bmp));
		bmp=null;
		
		/*
		 * prepare data for text animation
		 * */
		if (null!=buttonsText) {
			buttonsText.clear();
			buttonsText=null;
		}
		buttonsText = new ArrayList<ImageView>();
		rltexts.setVisibility(VISIBLE);
		//
		int preId = 8000;
		for (int i = 0; i < NUMBER_TEXT_CONGRATULATION; i++) {
			// scale bitmap character
			Bitmap bm = getImage("text" + i);

			ImageView imgChar = new ImageView(getContext());
			imgChar.setVisibility(INVISIBLE);
			imgChar.setBackgroundDrawable(new BitmapDrawable(bm));
			imgChar.setId(preId);

			RelativeLayout.LayoutParams layoutParams;
			layoutParams = new RelativeLayout.LayoutParams(TEXT_WIDTH_NORMAL, TEXT_HEIGHT_NORMAL);
			if (i == 0) {
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
				layoutParams.setMargins(X_TEXT_START_POSITION, 0, 0, 0);
			}
			else {
				layoutParams.addRule(RelativeLayout.RIGHT_OF, preId - 1);
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			}

			imgChar.setLayoutParams(layoutParams);
			bm = null;

			this.rltexts.addView(imgChar);
			buttonsText.add(imgChar);
			preId++;
		}

		/*
		 * particle animation
		 */
		particleFinish = false;		
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tspark);		
		particle.setDrawbleIcon(bmp);		
		bmp.recycle();
		bmp = null;
		
	}
	
	private void Clear(){
		
		this.ShowLog("clear");
		
		/*
		 * clear data of circle animation
		 * */
		if (circleAni!=null) {
			
			try {
				circleAni.setVisibility(GONE);
				
				BitmapDrawable drb = (BitmapDrawable) circleAni.getBackground();
				if (drb!=null) {
					drb.getBitmap().recycle();
					drb = null;			
				}
				
				circleAni.setBackgroundDrawable(null);
				
			} catch (Exception e) {
				
			}
			
		}
				
		/*
		 * clear data of texts animation
		 * */		
		rltexts.setVisibility(GONE);
		if (buttonsText!=null) {
			
			try {
				for (int i = 0; i < NUMBER_TEXT_CONGRATULATION; i++) {
					ImageView img = buttonsText.get(i);
					this.rltexts.removeView(img);
					BitmapDrawable drb = (BitmapDrawable) img.getBackground();
					if (drb!=null) {
						drb.getBitmap().recycle();
						drb = null;			
					}
					img.setBackgroundDrawable(null);
					img.setVisibility(GONE);
					img=null;
				}
				buttonsText.clear();
				buttonsText = null;
			} catch (Exception e) {
				
			}
			
		}
		
	}
	
	public void startCircleAnimation() {

		this.ShowLog("start circle animation");
		
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

				//dang thuc hien animation 1 da thuc hien xong. qua animation 2
				step = 1;
				circleAni.setVisibility(ImageView.GONE);
				StartAnimationLoadtext();

			}
		});

		ScaleAnimation ani = new ScaleAnimation(1f, max_circle_size, 1f, max_circle_size, bm_size / 2, bm_size / 2);
		ani.setDuration(600);
		aniSet.addAnimation(ani);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setStartOffset(800);
		alphaAnimation.setDuration(600);
		//aniSet.addAnimation(alphaAnimation);
						
		try {
			if (player1 == null) {
				loadSound1();
			}
			player1.start();
		}
		catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}
		
		circleAni.setVisibility(ImageView.VISIBLE);
		
		if (screenOff) {
			return;
		}
		
		circleAni.startAnimation(aniSet);
				
	}

	/*
	 * ------------------------------------------ Loadtext animation
	 */
	private void StartAnimationLoadtext() {

		this.ShowLog("start animation text");
		
		/*
		 * neu truoc do dang thuc hien ma event screenOff dc goi --> tam dung; khi resume dc goi --> thuc hien tiep.
		 * neu screenoff == false ---> truoc do phai la truong hop tren --> khoi tao lai gia tri.
		 * */
		if (!screenOff) {
			indexNumberText = 0;
		}		
				
		try {
			if (player2 == null) {
				loadSound2();
			}
			player2.start();
		}
		catch (Exception e) {
			ShowLog("load sound 2 err");
		}

		try {
			if (player_background == null) {
				loadSound4();
			}
			player_background.start();
		}
		catch (Exception e) {
			ShowLog("load sound backgrond err");
		}

		// go go..
		CharacterAnimation();
	}

	private void CharacterAnimation() {

		if (indexNumberText == NUMBER_TEXT_CONGRATULATION) {

			/*
			 * animation load text is finish.
			 */
			
			if (!screenOff) {
				//dang thuc hien animation 2, qua animation 3.
				step = 2;
				
				try {
					if (player3 == null) {
						loadSound3();
					}
					player3.start();
				}
				catch (Exception e) {
					if (Constant.IS_PrintStackTrace) {
						e.printStackTrace();
					}
				}
				
				/*
				 * checking sound background.
				 * */
				CheckingSoundBackground();				
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
		// aniSet.setFillAfter(true);
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

		/*
		 * --------------
		 */

		ScaleAnimation scale = new ScaleAnimation(CGL_TEXT_RATE, 1, CGL_TEXT_RATE, 1, TEXT_WIDTH_NORMAL, TEXT_HEIGHT_NORMAL);
		scale.setDuration(180);

		aniSet.addAnimation(scale);

		/*
		 * --------------
		 */

		TranslateAnimation trans = new TranslateAnimation(0, 0, Y_TEXT_START, 0);
		trans.setDuration(160);
		aniSet.addAnimation(trans);

		/*
		 * --------------
		 */
		img.setVisibility(VISIBLE);
		img.startAnimation(aniSet);
	}

	/*
	 * ------------------------------------------ animation alpha
	 */

	private void startAlphaAnimationText() {

		ShowLog("start animation hidden text");
		
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
				//finish all animation --> clear memory
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

		txtTitle.startAnimation(alpha);
		txtTitle.setVisibility(VISIBLE);

		layoutBottom.startAnimation(alpha);
		layoutBottom.setVisibility(VISIBLE);
		
	}

	/*
	 * ------------------------------------------ general method
	 */

	private Bitmap getImage(String name) {
		int resID = getResources().getIdentifier(name, "drawable", getContext().getPackageName());
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// opt.inMutable = true;
		// opt.inSampleSize=2;
		// opt.inScaled = true;
		return BitmapFactory.decodeResource(getResources(), resID, opt);
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
		bm=null;
		
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

	// private void Log(String msg)
	// {
	// android.util.Log.d(CongratulationView.class.getSimpleName(), msg);
	// }

	private void loadSound1() {

		try {

			if (player1 != null) {
				try {
					if (player1.isPlaying()) {
						player1.stop();
					}
					player1.release();
					player1 = null;
				}
				catch (Exception e) {

				}
			}

			player1 = new MediaPlayer();

			AssetFileDescriptor descriptor = getResources().getAssets().openFd("puzzule_j_kansei_fanfare.mp3");
			player1.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			player1.prepare();

		}
		catch (Exception e) {
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
				}
				catch (Exception e) {

				}
			}

			player2 = new MediaPlayer();

			AssetFileDescriptor descriptor = getResources().getAssets().openFd("puzzule_i_kansei_applause.mp3");
			player2.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			player2.prepare();

		}
		catch (Exception e) {
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
				}
				catch (Exception e) {

				}
			}

			player3 = new MediaPlayer();
			AssetFileDescriptor descriptor = getResources().getAssets().openFd("puzzule_k_kansei_kira.mp3");
			player3.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			player3.prepare();

		}
		catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}
	}

	private void loadSound4() {
		// sound background
		try {

			if (player_background != null) {
				try {
					if (player_background.isPlaying()) {
						player_background.stop();
					}
					player_background.release();
					player_background = null;
				}
				catch (Exception e) {

				}
			}
			player_background = new MediaPlayer();
			AssetFileDescriptor descriptor = getResources().getAssets().openFd("puzzule_e_bgm2.mp3");
			player_background.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			player_background.setLooping(true);
			player_background.prepare();

		}
		catch (Exception e) {

			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}

		}
	}

	/*
	 * 
	 * */
	@Override
	public void ParticleCompleted() {

		this.ShowLog("finish particle animation");
		
		//finish particle
		step = 3;
		particleFinish = true;
		startAlphaAnimationText();
		ShowButtonControls();
		if (!screenOff) {				
			CheckingSoundBackground();			
		}		
		
	}

	private void CheckingSoundBackground(){
		
		/*
		 * checking sound background.
		 * */
		try {
			if (player_background==null) {
				loadSound4();
			}
			if (!player_background.isPlaying()) {
				player_background.start();
			}	
		} catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void ShowLog(String mess) {
		puzzle.slider.vn.util.ShowLog.i(CongratulationView.class.getSimpleName(), "--+ " + mess + " +--");
	}

	public String getPathImage() {
		return path;
	}
}
