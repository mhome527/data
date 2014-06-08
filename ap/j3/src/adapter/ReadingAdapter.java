package sjpn3.vn.adapter;

import java.util.ArrayList;
import sjpn3.vn.R;
import sjpn3.vn.entry.ReadingEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ReadingAdapter extends BaseAdapter {

	private ArrayList<ReadingEntry> arrData;
	private Context context;

	private class ViewHolder {
		TextView tvTitle;
		TextView tvQA;
		TextView tvAns;
		Button btnAns;
	}

	public ReadingAdapter(Context context, ArrayList<ReadingEntry> arrData) {
		this.arrData = arrData;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return arrData.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final TextView tvAnswer;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.reading_item, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvQA = (TextView) convertView.findViewById(R.id.tvQA);
			holder.tvAns = (TextView) convertView.findViewById(R.id.tvAns);
			tvAnswer= (TextView) convertView.findViewById(R.id.tvAns);
			holder.btnAns =(Button)convertView.findViewById(R.id.btnAnswer);
			holder.btnAns.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					TextView tv  = (TextView) convertView.findViewById(R.id.tvAns);
					tvAnswer.setVisibility(View.VISIBLE);
					v.setVisibility(View.GONE);
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ReadingEntry entry = arrData.get(position);
		if (entry != null) {
			if (entry.bTitle) {
				holder.tvTitle.setText(entry.title);
				holder.tvTitle.setVisibility(View.VISIBLE);
				holder.tvQA.setVisibility(View.GONE);
				holder.tvAns.setVisibility(View.GONE);
				holder.btnAns.setVisibility(View.GONE);

			} else if (entry.answer != null && !entry.answer.equals("")) {
				holder.tvAns.setText(entry.answer);
				holder.btnAns.setVisibility(View.VISIBLE);
				holder.tvAns.setVisibility(View.GONE);
				holder.tvTitle.setVisibility(View.GONE);
				holder.tvQA.setVisibility(View.GONE);
			} else {
				holder.tvQA.setText(entry.title);
				holder.tvQA.setVisibility(View.VISIBLE);
				holder.tvAns.setVisibility(View.GONE);
				holder.btnAns.setVisibility(View.GONE);
				holder.tvTitle.setVisibility(View.GONE);
			}
		}else{
			holder.tvQA.setVisibility(View.GONE);
			holder.tvAns.setVisibility(View.GONE);
			holder.btnAns.setVisibility(View.GONE);
			holder.tvTitle.setVisibility(View.GONE);
		}
		return convertView;
	}
}
