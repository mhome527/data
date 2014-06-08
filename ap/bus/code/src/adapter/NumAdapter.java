package app.infobus.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.infobus.R;

public class NumAdapter extends ArrayAdapter<String> {
	Activity activity;
	ArrayList<String> arrNum;

	public NumAdapter(Activity activity, ArrayList<String> arrNum) {
		super(activity, R.layout.search_list, arrNum);
		this.activity = activity;
		this.arrNum = arrNum;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String num;
		TextView tv;
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.search_list, null, true);
		}
		num = arrNum.get(position);
		tv = (TextView) convertView.findViewById(R.id.tvNum);
		tv.setText(num);
		return convertView;
	}

}
