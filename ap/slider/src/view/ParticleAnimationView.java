/*
 * Copyright (c) 2009 Bakhtiyor Khodjayev (http://www.bakhtiyor.com/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package puzzle.slider.vn.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import puzzle.slider.vn.animation.AnimateDrawable;
import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.Utility;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * @author ngant
 * @sine Mar 14, 2013
 */
public class ParticleAnimationView extends View implements AnimationListener {
	public interface ParticleListener {
		public void ParticleCompleted();
	}

	// the count of snow
	private int snow_flake_count = 70;
	// list snow drawble
	private List<AnimateDrawable> drawables = null;
	//
	private int width, height;
	// position of snow
	private int x_center, y_center;
	//
	private int item_size;
	// snow center.
	private Bitmap snow_flake;
	//
	private Boolean start = false;
	//
	private Boolean startTime = false, endTime = false;
	//
	private long milisecondStart;
	//
	private int duration = 1000;// 3s

	// Context context;
	//
	ParticleListener particleDelegate;

	public ParticleAnimationView(Context context) {
		super(context);
		// this.context = context;
		setFocusable(true);
		setFocusableInTouchMode(true);

	}

	public ParticleAnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//
		setFocusable(true);
		setFocusableInTouchMode(true);
		// this.context = context;

	}

	public ParticleAnimationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//
		setFocusable(true);
		setFocusableInTouchMode(true);
		// this.context = context;

	}

	@Override
	public void destroyDrawingCache() {
		try {
			ClearBitmapDrawbleList();
			if (snow_flake!=null) {
				snow_flake.recycle();
				snow_flake = null;
			}
			if (drawables!=null) {
				drawables.clear();
				drawables = null;
			}
			Utility.unbindDrawables(this);
		}
		catch (Exception e) {

		}
		super.destroyDrawingCache();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		try {
			ClearBitmapDrawbleList();
			if (snow_flake!=null) {
				snow_flake.recycle();
				snow_flake = null;
			}
			if (drawables!=null) {
				drawables.clear();
				drawables = null;
			}
			Utility.unbindDrawables(this);
		}
		catch (Exception e) {

		}
		super.onDetachedFromWindow();
	}
	
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {

		super.onSizeChanged(width, height, oldw, oldh);
		this.width = width;
		this.height = height;
		//

	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!start) {
			return;
		}

		if (!startTime) {
			startTime = true;
			endTime = false;
			milisecondStart = System.currentTimeMillis();
		}
		else {
			if (!endTime) {
				if (System.currentTimeMillis() - milisecondStart >= duration) {
					endTime = true;
					// reset animation
					for (int i = 0; i < snow_flake_count; i++) {
						AnimateDrawable drawable = drawables.get(i);
						Animation ani = drawable.getAnimation();
						ani.setRepeatCount(0);
					}
				}
			}
		}

		if (endTime) {
			int i = 0, count = 0;
			for (; i < snow_flake_count; i++) {

				AnimateDrawable drawable = drawables.get(i);

				if (!drawable.hasEnded()) {
					drawable.draw(canvas);
					count++;
				}

			}

			if (count == 0) {
				start = false;
				this.setVisibility(GONE);
				if (particleDelegate != null) {
					particleDelegate.ParticleCompleted();
				}
			}
		}
		else {
			for (int i = 0; i < snow_flake_count; i++) {
				AnimateDrawable drawable = drawables.get(i);
				// canvas.save();

				drawable.draw(canvas);
				// canvas.restore();
			}
		}

		invalidate();
	}

	public void initAllSnow() {

		try {

			this.ShowLog("init particle animation");
			
			ClearBitmapDrawbleList();

			Random random = new Random(System.currentTimeMillis());
			Interpolator interpolator = new LinearInterpolator();

			x_center = (width - item_size) / 2;
			y_center = (height - item_size) / 2;

			drawables = new ArrayList<AnimateDrawable>();
			int size = (x_center > y_center ? x_center : y_center) + 10;
			int flag=1;
			for (int i = 0; i < snow_flake_count; i++) {
				size = random.nextInt(x_center/2)+ x_center/2;
				Animation animation = null;
				int endX, endY;
				endX = x_center + (int) (size * Math.cos((2 * Math.PI / snow_flake_count) * i));
				endY = y_center - (int) (size * Math.sin((2 * Math.PI / snow_flake_count) * i));

				animation = new TranslateAnimation(x_center, endX, y_center, endY);

				// speech 400
				animation.setDuration(500);
				animation.setRepeatCount(-1);
				animation.initialize(10, 10, 10, 10);
				animation.setInterpolator(interpolator);

				// my code, this is the time to wait next animation
				animation.setStartOffset(random.nextInt(snow_flake_count * 25));
				int R =255;
				int G =255;
				int B =255;
				if (flag==1) {
					R= 255;
					G = random.nextInt(255);
					B = random.nextInt(255);
					flag=2;
				}else if (flag==2) {
					R= random.nextInt(255);
					G = 255;
					B = random.nextInt(255);
					flag=3;
				}else if (flag==3) {
					R= random.nextInt(255);
					G = random.nextInt(255);
					B = 200;
					flag=1;
				}
				
				
				
				Drawable drb = ChangeColorDrawble(R, G, B);
				AnimateDrawable aniDr = new AnimateDrawable(drb, animation);
				// animation.startNow();

				drawables.add(aniDr);

			}
			
		}
		catch (Exception e) {
			if (Constant.IS_PrintStackTrace) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * public method
	 */

	private void ClearBitmapDrawbleList() {

		this.ShowLog("clear particle animation");
		if (drawables != null && drawables.size() > 0) {
			for (int i = 0; i < snow_flake_count; i++) {
				AnimateDrawable drawable = drawables.get(i);
				BitmapDrawable bmdrawable = (BitmapDrawable) drawable.getProxy();

				bmdrawable.getBitmap().recycle();
				bmdrawable = null;
				drawable = null;
			}
			drawables.clear();
			drawables=null;
		}

	}

	private BitmapDrawable ChangeColorDrawble(int red, int green, int blue) {

		Bitmap myBitmap = snow_flake;
		Bitmap bitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.ARGB_8888);

		int[] allpixels = new int[myBitmap.getHeight() * myBitmap.getWidth()];
		myBitmap.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());

		for (int i = 0; i < myBitmap.getHeight() * myBitmap.getWidth(); i++) {
			if (allpixels[i] != Color.TRANSPARENT) {
				int alpha = Color.alpha(allpixels[i]);
				
				int cl = Color.argb(alpha, red, green, blue);
				allpixels[i] = cl;// Color.RED;
			}
		}
		bitmap.setPixels(allpixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

		/*
		 * for (int i = 0; i < myBitmap.getWidth(); i++) { for (int j = 0; j < myBitmap.getHeight(); j++) { int pixel = myBitmap.getPixel(i, j); if (pixel !=
		 * Color.TRANSPARENT) { bitmap.setPixel(i, j, color); } else { bitmap.setPixel(i, j, pixel); } } }
		 */
		BitmapDrawable drb = new BitmapDrawable(bitmap);
		drb.setBounds(0, 0, drb.getIntrinsicWidth(), drb.getIntrinsicHeight());

		/*
		 * 
		 * 
		 * //colorFillter int iColor = Color.RED;
		 * 
		 * int red = (iColor & 0xFF0000) / 0xFFFF; int green = (iColor & 0xFF00) / 0xFF; int blue = iColor & 0xFF;
		 * 
		 * float[] matrix = { 0, 0, 0, 0, red , 0, 0, 0, 0, green , 0, 0, 0, 0, blue , 0, 0, 0, 1, 0 };
		 * 
		 * ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
		 * 
		 * drb.setColorFilter(colorFilter);
		 */

		return drb;
	}

	public void setStart(Boolean val) {
		
		this.ShowLog("start particle animation");
		initAllSnow();
		this.setVisibility(VISIBLE);
		startTime = false;
		start = val;
		this.invalidate();
		
	}

	public void setDrawbleIcon(Bitmap bmp) {

		this.ShowLog("set icon particle animation");
		
		if (snow_flake != null) {
			if (!snow_flake.isRecycled()) {
				snow_flake.recycle();
			}			
			snow_flake = null;
		}

		snow_flake = bmp.copy(Bitmap.Config.ARGB_8888, true);
		int pixels[] = new int[bmp.getHeight() * bmp.getWidth()];
		bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
		snow_flake.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

		BitmapDrawable drb = new BitmapDrawable(snow_flake);

		item_size = drb.getIntrinsicWidth();// snow_flake.getWidth();
	}

	public void setCenter(int x, int y) {
		x_center = x;
		y_center = y;
	}

	public void setParticleListener(ParticleListener onlistener) {
		particleDelegate = onlistener;
	}

	@Override
	public void onAnimationEnd(Animation animation) {

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		puzzle.slider.vn.util.ShowLog.i(ParticleAnimationView.class.getSimpleName(), "--+ repeat animation +--");
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	private void ShowLog(String mess) {
		puzzle.slider.vn.util.ShowLog.i(ParticleAnimationView.class.getSimpleName(), "--+ " + mess + " +--");
	}
	
}
