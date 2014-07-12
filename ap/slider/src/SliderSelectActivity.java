package puzzle.slider.vn;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.CustomSharedPreferences;
import puzzle.slider.vn.util.ShowLog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * select image
 * 
 * @author huynhtran
 * 
 */
public class SliderSelectActivity extends AbstractContentsActivity implements OnClickListener {

	private String tag = SliderSelectActivity.class.getSimpleName();
	private RadioButton radioEasy;
	private RadioButton radioDifficult;
	private boolean isClick = false;
	private GridView gridView;

	private int[] arrImg = { R.drawable.jigsaw_image_01, R.drawable.jigsaw_image_02, R.drawable.jigsaw_image_03, R.drawable.jigsaw_image_04, R.drawable.jigsaw_image_05,
			R.drawable.jigsaw_image_06 };
	private int[] arrImgSmall = { R.drawable.jigsaw_image_s_01, R.drawable.jigsaw_image_s_02, R.drawable.jigsaw_image_s_03, R.drawable.jigsaw_image_s_04,
			R.drawable.jigsaw_image_s_05, R.drawable.jigsaw_image_s_06 };

	@Override
	protected int getViewLayoutId() {
		return R.layout.slider_select_image;
	}

	@Override
	protected void initView(final Bundle savedInstanceState) {
		String difficult;
		ImageAdapter adapter;
		super.initView(savedInstanceState);
		try {
//			ShowLog.i(tag, "initView......");

			gridView = (GridView) findViewById(R.id.gridView);

			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);

			radioEasy = (RadioButton) this.findViewById(R.id.radioEasy);
			radioDifficult = (RadioButton) this.findViewById(R.id.radioDifficult);

			CustomSharedPreferences.init(getApplicationContext());
//			CustomSharedPreferences.setPreferences(Constant.WIDTH_SCREEN, dm.widthPixels);
			CustomSharedPreferences.setPreferences(Constant.HEIGHT_SCREEN, dm.heightPixels);

//			width = dm.widthPixels / 5;
			TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
			//if table
	        if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE){
	        	CustomSharedPreferences.setPreferences(Constant.WIDTH_SCREEN, dm.widthPixels - 50);
	        	adapter = new ImageAdapter(this, arrImg);
	        }
	        else
	        	adapter = new ImageAdapter(this, arrImgSmall);

	        //			adapter = new ImageAdapter(this, arrImg);
	        
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent;

					if (isClick)
						return;
					isClick = true;

					if (radioEasy.isChecked())
						intent = new Intent(SliderSelectActivity.this, SliderMainEasyActivity.class);
					else
						intent = new Intent(SliderSelectActivity.this, SliderMainActivity.class);

					intent.putExtra(Constant.GAME_ID, arrImg[position]);
					intent.putExtra(Constant.GAME_ID_S, arrImgSmall[position]);
					startActivity(intent);
					finish();

				}
			});

			//
			// ////////music
			SoundManager.initSounds(this);
			// SoundManager.playSound(Constant.SOUND_A, true);
			// /////
			AdView adView = (AdView) this.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
			// /////

			difficult = getIntent().getStringExtra(Constant.KEY_LEVEL);

			if (difficult != null && difficult.equals(Constant.KEY_DIFFICULT))
				radioDifficult.setChecked(true);
			else
				radioEasy.setChecked(true);
//			ShowLog.i(tag, "initView width screen: " + width);
		} catch (Exception e) {
			ShowLog.e(tag, "initView error" + e);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isClick = false;
		ShowLog.i(tag, "onResume");

		// SoundManager.playSound(Constant.SOUND_A, true);
	}
	

	private class ImageAdapter extends BaseAdapter {
		private int[] arrImage;
		private Context mContext;

		public ImageAdapter(Context mContext, int[] arrImage) {
			this.arrImage = arrImage;
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return arrImage.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View grid;
			String time;
			ImageView imgShow;

			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				grid = new View(mContext);
				grid = inflater.inflate(R.layout.slider_item, null);
				imgShow = (ImageView) grid.findViewById(R.id.imgShow);
				TextView tvTime = (TextView) grid.findViewById(R.id.tvTime);
				time = CustomSharedPreferences.getPreferences(arrImage[position] + "", "00:00:00") + "";
				if (!time.equals("") && !time.equals("00:00:00"))
					tvTime.setText(time + "");
				imgShow.setImageResource(arrImage[position]);
//				imgShow.setImageResource(arrImgSmall[position]);

			} else {
				grid = (View) convertView;
			}
			return grid;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShowLog.i(tag, "onDestroy");
	}

	@Override
	public void onScreenOn(boolean isunlock) {

	}

}