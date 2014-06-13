package puzzle.slider.vn;

import java.util.ArrayList;

import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.CustomSharedPreferences;
import puzzle.slider.vn.util.ShowLog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * select image
 * 
 * @author huynhtran
 * 
 */
public class SliderImageActivity extends AbstractContentsActivity implements OnClickListener {
	String tag = "HuynhTD-" + SliderImageActivity.class.getSimpleName();
	public int width = 100, height = 100;
	boolean isClick = false;
	private GridView gridView;
	int[] arrImg = { R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank };
	@Override
	protected int getViewLayoutId() {
		return R.layout.slider_select_image;
	}

	@Override
	protected void initView(final Bundle savedInstanceState) {
		int widthScr;

		super.initView(savedInstanceState);
		try {
			gridView = (GridView) findViewById(R.id.gridView);

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);

			CustomSharedPreferences.init(getApplicationContext());
			CustomSharedPreferences.setPreferences(Constant.WIDTH_SCREEN, dm.widthPixels);
			CustomSharedPreferences.setPreferences(Constant.HEIGHT_SCREEN, dm.heightPixels);
			
			width = dm.widthPixels / 5;
			// switch (widthScr) {
			// case 1280:
			// width = widthScr / 5 + 25;
			// break;
			// default:
			// width = widthScr / 5;
			// break;
			// }
			ImageAdapter adapter = new ImageAdapter(this, arrImg);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(SliderImageActivity.this, SliderMainActivity.class);
//					Intent intent = new Intent(SliderImageActivity.this, TestAct.class);
					intent.putExtra(Constant.GAME_ID, arrImg[position]);
					startActivity(intent);
					
				}
			});

			ShowLog.i(tag, "initView width screen: " + width);
		} catch (Exception e) {
			ShowLog.e(tag, "initView error" + e);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		super.onClick(v);
//		switch (v.getId()) {
//		case R.id.img1:
//			intent = new Intent(SliderImageActivity.this, SliderMainActivity.class);
//			intent.putExtra(Constant.GAME_ID, R.id.img1);
//			startActivity(intent);
//			break;
//		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		isClick = false;
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
			// TODO Auto-generated method stub
			return arrImage.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View grid;
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				grid = new View(mContext);
				grid = inflater.inflate(R.layout.slider_item, null);
				ImageView imageView = (ImageView) grid.findViewById(R.id.imgShow);
				imageView.setImageResource(arrImage[position]);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//				imageView.setAdjustViewBounds(true);


			} else {
				grid = (View) convertView;
			}
			return grid;
		}

	}

	// @Override
	// public void onBackPressed() {
	// try {
	// SoundManager.playSound(Constant.SOUND_D, false);
	// Intent intent = new Intent(SliderImageActivity.this, StartupApp.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	// SoundManager.isLEFT =3;
	// startActivity(intent);
	// finish();
	// }
	// catch (Exception e) {
	// ShowLog.showLogError(tag, "back error: " + e.getMessage());
	// }
	// }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}