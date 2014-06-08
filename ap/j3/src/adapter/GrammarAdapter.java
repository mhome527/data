package sjpn3.vn.adapter;

import java.util.List;

import sjpn3.vn.Constant;
import sjpn3.vn.R;
import sjpn3.vn.model.subModel.GrammarList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GrammarAdapter extends BaseAdapter {
	private List<GrammarList> grammarList;
	private Context context;

	private class ViewHolder {
		TextView tvTitle;
		TextView tvMean;

	}

	public GrammarAdapter(Context context, List<GrammarList> grammarList) {
		this.grammarList = grammarList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return grammarList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return grammarList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return grammarList.indexOf(getItem(position));

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grammar_item, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvMean = (TextView) convertView.findViewById(R.id.tvMean);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GrammarList entry = grammarList.get(position);
		holder.tvTitle.setText(entry.grammar);
		if (Constant.english)
			holder.tvMean.setText(entry.meanEN + "");
		else
			holder.tvMean.setText(entry.meanVN + "");
		return convertView;
	}

}
