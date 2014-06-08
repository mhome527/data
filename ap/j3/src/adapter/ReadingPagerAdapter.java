package sjpn3.vn.adapter;

import java.util.List;

import sjpn3.vn.R;
import sjpn3.vn.Util.BitmapUtil;
import sjpn3.vn.Util.ULog;
import sjpn3.vn.activity.ReadingPager;
import sjpn3.vn.model.ReadingModel;
import sjpn3.vn.view.ScaleImageView;
import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReadingPagerAdapter extends PagerAdapter implements OnClickListener {

	private View layout;
	private ReadingPager act;
//	private WebView webReading;
	private TextView tvNote;
	private TextView tvTitle;
	private TextView tvTitle2;
	private int idReading = -1;
	
//	private ScaleImageView imgReading;
	private ImageView imgReadingS;
//	private ImageView imgClose;
//	private RelativeLayout rlReading;
	

	private boolean isShow = false;
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

	@Override
	public Object instantiateItem(View container, int position) {
		LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.reading_page, null);

		initView(position);
		setData(lstDay.get(position));

		((ViewPager) container).addView(layout, 0);
		return layout;
	}

	
	private void initView(int pos) {
		tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
		tvTitle2 = (TextView) layout.findViewById(R.id.tvTitle2);
		tvNote = (TextView) layout.findViewById(R.id.tvNote);
//		imgReading = (ScaleImageView) layout.findViewById(R.id.imgReading);
		imgReadingS = (ImageView) layout.findViewById(R.id.imgReadingS);
//		rlReading = (RelativeLayout) layout.findViewById(R.id.rlReading);
//		imgClose = (ImageView) layout.findViewById(R.id.imgClose);
//		btnShow = (Button) layout.findViewById(R.id.btnShow);
		
		imgReadingS.setTag("img" + pos);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgReadingS:
			if (isClick)
				return;
			isClick = true;
			isShow = true;
//			rlReading.setVisibility(View.VISIBLE);
			// imgReading.setImageResource(idReading);
//			imgReading.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(act.getResources(), idReading, act.widthScreen - 10, act.widthScreen - 10));
			imgReadingS.setImageBitmap(null);
			break;
//		case R.id.imgClose:
//			isClick = false;
//			isShow = false;
//			rlReading.setVisibility(View.GONE);
//			// imgReading.setImageResource(0);
//			// imgReadingS.setImageResource(idReading);
//			imgReading.setImageBitmap(null);
//			imgReadingS.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(act.getResources(), idReading, act.widthScreen - 10, act.widthScreen - 10));
//
//			break;
//		case R.id.btnShow:
//			isShow = true;
//			// imgReading.setImageResource(idReading);
//			// imgReadingS.setImageResource(0);
//			imgReading.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(act.getResources(), idReading, act.widthScreen - 10, act.widthScreen - 10));
//			imgReadingS.setImageBitmap(null);
//
//			rlReading.setVisibility(View.VISIBLE);
//			break;
		}
	}
	private void setData(ReadingModel model) {
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
				System.gc();
//				imgReadingS.setVisibility(View.VISIBLE);
//				System.gc();
				imgReadingS.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(act.getResources(), idReading, act.widthScreen - 10, act.widthScreen - 10));
			}
			tvNote.setText(model.note);

		}
	}
	
//	public void showImage(){
//		ULog.i("ReadingPagerAdapter", "showImage text:" + tvTitle.getText());
//		isShow = true;
////		imgReading.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(act.getResources(), idReading, act.widthScreen - 10, act.widthScreen - 10));
//		imgReadingS.setImageBitmap(null);
//		rlReading.setVisibility(View.VISIBLE);
//		
//	}

	
	// return true; //back
	// return false; // no back
//	public boolean closeImage() {
//		isClick = false;
//		if (isShow) {
//			isShow = false;
//			rlReading.setVisibility(View.GONE);
//			imgReading.setImageBitmap(null);
//			imgReadingS.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(act.getResources(), idReading, act.widthScreen - 10, act.widthScreen - 10));
//			return false;
//		}
//		return true;
//		
//	}
	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
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
