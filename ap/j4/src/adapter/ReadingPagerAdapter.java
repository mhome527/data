package sjpn4.vn.adapter;

import java.util.List;

import sjpn4.vn.R;
import sjpn4.vn.Util.ULog;
import sjpn4.vn.model.ReadingModel;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ReadingPagerAdapter extends PagerAdapter {

	private View layout;
	private Activity act;
	private TextView tvNote;
	private TextView tvTitle;
	private TextView tvTitle2;
	private TextView tvReading;

	public List<ReadingModel> lstDay;

	public ReadingPagerAdapter(Activity act, List<ReadingModel> lstDay) {
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
		layout = inflater.inflate(R.layout.reading_page, null);

		initView();
		setData(lstDay.get(position));

		((ViewPager) container).addView(layout, 0);
		return layout;
	}

	private void initView() {
		tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
		tvTitle2 = (TextView) layout.findViewById(R.id.tvTitle2);
//		webReading = (WebView) layout.findViewById(R.id.webReading);
		tvNote = (TextView) layout.findViewById(R.id.tvNote);
		tvReading = (TextView) layout.findViewById(R.id.tvReading);
//		webReading.getSettings().setRenderPriority(RenderPriority.HIGH);
//		webReading.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
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

//			webReading.loadData(model.reading, "text/html; charset=utf-8", "utf-8");
//		
			tvReading.setText(model.reading);
			ULog.i("readingadapter", "===text:" + model.reading);
			tvNote.setText(model.note);

		}else{
			ULog.e("readingadapter", "===text: NULL");
		}
	}

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
