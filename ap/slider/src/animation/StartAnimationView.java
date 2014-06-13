package puzzle.slider.vn.animation;

import java.util.ArrayList;

import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.Utility;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author ngant
 * @sine Mar 14, 2013
 */
public class StartAnimationView extends RelativeLayout
{
	
	public interface OnstartAnimationListener{
		public void animaitonFinish();
	}
	
	Boolean destroy = false;
	int TEXT_WIDTH_NORMAL = 80;
	int TEXT_HEIGHT_NORMAL = 80;
	int NUMBER_ITEMS = 4;
	int indexNumberItem=0;
	ArrayList<ImageView> imgViews;
	OnstartAnimationListener animationdelegate;
	
	public StartAnimationView(Context context) {
		super(context);
		this.setVisibility(GONE);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setClickable(true);
	}

	public StartAnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setVisibility(GONE);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setClickable(true);
	}

	public StartAnimationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setVisibility(GONE);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setClickable(true);
	}

	@Override
	public void destroyDrawingCache() {	
		super.destroyDrawingCache();
				
		Clear();
		Utility.unbindDrawables(this);
	}
	
	private void inItAnimation(){
		
		imgViews = new ArrayList<ImageView>();
		indexNumberItem = 0;
		try{
		int indexs[]={3, 2, 1, 0};
		for (int i = 0; i < NUMBER_ITEMS; i++) {
			// scale bitmap character
			Bitmap bm = getImage("count_" + indexs[i]);

			ImageView imgChar = new ImageView(getContext());
			imgChar.setVisibility(INVISIBLE);
			imgChar.setBackgroundDrawable(new BitmapDrawable(bm));
			
			RelativeLayout.LayoutParams layoutParams;
			layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			layoutParams = new RelativeLayout.LayoutParams(TEXT_WIDTH_NORMAL, TEXT_HEIGHT_NORMAL);
			layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);				
			
			imgChar.setLayoutParams(layoutParams);			
			bm = null;

			this.addView(imgChar);
			imgViews.add(imgChar);
			
		}
		
		this.setVisibility(VISIBLE);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/*
	 * public method
	 * */
	
	public void Start(){
		inItAnimation();
		CharacterAnimation();
	}
	
	public void SetAnimationListener(OnstartAnimationListener listener){
		this.animationdelegate = listener;
	}
	
	/*
	 * private method
	 * */
	
	private void CharacterAnimation() {

		if (indexNumberItem == NUMBER_ITEMS) {

			/*
			 * animation load text is finish.
			 */
			this.setVisibility(GONE);
			if (animationdelegate!=null) {
				animationdelegate.animaitonFinish();
			}
			return;
		}

		ImageView imgv = imgViews.get(indexNumberItem);
		TransitionAnimationCharacter(imgv);

		//
		indexNumberItem++;
	}

	private void TransitionAnimationCharacter(ImageView img) {

		AnimationSet aniSet = new AnimationSet(true);
		aniSet.setFillAfter(true);
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
				try {
					CharacterAnimation();
				} catch (Exception e) {
					
				}
				
			}
		});

		/*
		 * --------------
		 */

		AlphaAnimation alpshow = new AlphaAnimation(0, 1);
		alpshow.setDuration(100);		
		aniSet.addAnimation(alpshow);

		/*
		 * --------------
		 */

		AlphaAnimation alphide = new AlphaAnimation(1, 0);
		alphide.setStartOffset(800);
		alphide.setDuration(300);
		aniSet.addAnimation(alphide);
		
		/*
		 * --------------
		 */
		img.setVisibility(VISIBLE);
		img.startAnimation(aniSet);
				
	}
	
	private Bitmap getImage(String name) {
		int resID = getResources().getIdentifier(name, "drawable", getContext().getPackageName());
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// opt.inMutable = true;
		// opt.inSampleSize=2;
		// opt.inScaled = true;
		return BitmapFactory.decodeResource(getResources(), resID, opt);
	}
	
	private void Clear(){
		
		try {
			if(imgViews!=null && imgViews.size()>0){
				for (int i = 0; i < NUMBER_ITEMS; i++){				
					ImageView imgview = imgViews.get(i);
					this.removeView(imgview);

					imgview.setVisibility(GONE);
					imgview.setImageDrawable(null);					
					imgview = null;
				}
				imgViews.clear();
				imgViews = null;
			}
		} catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}
		
	}
	
}
