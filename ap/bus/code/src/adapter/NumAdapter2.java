package app.infobus.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.infobus.entity.Node;

import app.infobus.R;

public class NumAdapter2 extends ArrayAdapter<Node> {
	Activity activity;
	ArrayList<Node> arrNum;

	public NumAdapter2(Activity activity, ArrayList<Node> arrNum) {
		super(activity, R.layout.search_list, arrNum);
		this.activity = activity;
		this.arrNum = arrNum;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Node num;
		TextView tvNum, tvStreet;
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.search_list, null, true);
		}
		num = arrNum.get(position);
		tvNum = (TextView) convertView.findViewById(R.id.tvNum);		
		tvNum.setText(num.getNum());
		
		tvStreet= (TextView) convertView.findViewById(R.id.tvPath);
		tvStreet.setText(num.getNum());
		return convertView;
	}

}
