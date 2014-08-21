package sjpn3.vn.adapter;

import java.util.List;

import sjpn3.vn.R;
import sjpn3.vn.Util.BitmapUtil;
import sjpn3.vn.Util.ULog;
import sjpn3.vn.activity.ReadingPager;
import sjpn3.vn.model.ReadingModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadingPagerAdapter extends PagerAdapter implements OnClickListener {

	private static String TAG = ReadingPagerAdapter.class.getSimpleName();
//	private View layout;
	private ReadingPager act;
//	private TextView tvNote;
//	private TextView tvTitle;
//	private TextView tvTitle2;
	private int idReading = -1;
	
//	private ScaleImageView imgReading;
//	private ImageView imgReadingS;
//	private ImageView imgClose;
//	private RelativeLayout rlReading;
	

//	private boolean isShow = false;
	private boolean isClick = false;



	public List<ReadingModel> lstDay;

	public ReadingPagerAdapter(ReadingPager act, List<ReadingModel> lstDay) {
		this.act = act;
		this.lstDay = lstDay;
	}

	@Override
	public int getCount() {
		return lstDay.size();
	}

	@SuppressLint("InflateParams")
	@Override
	public Object instantiateItem(View container, int position) {
		LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.reading_page, null);
		initView(layout, position);
//		setData(lstDay.get(position));

		((ViewPager) container).addView(layout, 0);
		return layout;
	}

	
	private void initView(View layout, int pos) {
		ReadingModel model;
		ULog.i(TAG, "initView pager:" + pos);
		TextView tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
		TextView tvTitle2 = (TextView) layout.findViewById(R.id.tvTitle2);
		TextView tvNote = (TextView) layout.findViewById(R.id.tvNote);
//		imgReading = (ScaleImageView) layout.findViewById(R.id.imgReading);
		ImageView imgReadingS = (ImageView) layout.findViewById(R.id.imgReadingS);
		
		imgReadingS.setTag("img" + pos);
		model = lstDay.get(pos);
		if (model != null) {
			if (model.title == null || model.title.equals(""))
				tvTitle.setVisibility(View.GONE);
			else
				tvTitle.setText(model.title);

			if (model.title2 == null || model.title2.equals(""))
				tvTitle2.setVisibility(View.GONE);
			else
				tvTitle2.setText(model.title2);
			
			if (model.img != null && !model.img.equals("")) {
				idReading = act.getResources().getIdentifier(model.img, "drawable", act.getPackageName());

				imgReadingS.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(act.getResources(), idReading, act.widthScreen - 20, act.widthScreen - 20));
//				imgReadingS.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//				imgReadingS.setImageResource(idReading);
//				System.gc();

			}
			tvNote.setText(model.note);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgReadingS:
			if (isClick)
				return;
			isClick = true;
//			isShow = true;
//			rlReading.setVisibility(View.VISIBLE);
			// imgReading.setImageResource(idReading);
//			imgReading.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(act.getResources(), idReading, act.widthScreen - 10, act.widthScreen - 10));
//			imgReadingS.setImageBitmap(null);
			break;

		}
	}
	
	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
		arg0 = null;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}
