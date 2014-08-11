package sjpn4.vn.slidingmenu.adapter;

import java.util.List;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import sjpn4.vn.R;
import sjpn4.vn.Util.Common;
import sjpn4.vn.model.subModel.ReadingList;
import sjpn4.vn.model.subModel.SubReadingList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SlideMenuListAdapter extends BaseAdapter {

	// /////
	private Context context;
	private List<ReadingList> arrData;

	public SlideMenuListAdapter(Context context, List<ReadingList> arrData) {
		this.context = context;
		this.arrData = arrData;
	}

	@Override
	public int getCount() {
		return arrData.size();
	}

	@Override
	public Object getItem(int position) {
		return arrData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final TextView tvAns;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.reading_menu_item, null);
			holder.tvReading = (TextView) convertView.findViewById(R.id.tvReading);
			tvAns = (TextView) convertView.findViewById(R.id.tvAns);
			holder.tvAns = tvAns;
			holder.btnAns = (Button) convertView.findViewById(R.id.btnAns);
			holder.btnAns.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Common.isNetAvailable(context)) {
						tvAns.setVisibility(View.VISIBLE);
						v.setVisibility(View.GONE);
					} else {
						// Toast.makeText(context, "connect to netword",
						// Toast.LENGTH_LONG).show();
						Common.showDialogWifi(context);
					}
				}
			});
			// ///////ad
			// AdView adView = (AdView) convertView.findViewById(R.id.adView);
			// AdRequest adRequest = new AdRequest.Builder().build();
			// adView.loadAd(adRequest);
			// // //////////////////
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// holder.web.loadData(createHtml(arrData.get(position), position),
		// "text/html; charset=utf-8", "utf-8");
		holder.tvReading.setText(Html.fromHtml(createHtml(arrData.get(position), position)));
		holder.tvAns.setText(arrData.get(position).ans);
		return convertView;
	}

	private class ViewHolder {
		TextView tvAns;
		TextView tvReading;
		Button btnAns;
	}

	private String createHtml(ReadingList listQA, int position) {
		String htmlQA = "";
		int j = 0;
		// ////////slide menu

		htmlQA = createTagTitleHtml(position + 1, listQA.question);
		for (SubReadingList sub : listQA.listQuestion) {
			j++;
			htmlQA += createTagSubHtml(j, sub.qa);
		}
		htmlQA += " </p></body></html>";

		// ULog.i(this, "createHtml() html:" + htmlQA);
		return htmlQA;
	}

	private String createTagTitleHtml(int count, String data) {
		String html;
		html = "<!DOCTYPE html><html><body>";
		html += "<p style='font-size:16px; line-height:25px'><b>";
		html += "<span style='border: 1px solid green ;padding: 3px 0px 0px 0px;'>" + count + " </span>" + data
				+ "</b></p><p>";
		return html;
	}

	private String createTagSubHtml(int count, String data) {
		String html;
		html = "<span style='padding: 0px 0px 0px 20px;'>" + count + ". " + data + "</span><br/>";
		return html;
	}
}
