package app.infobus.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.infobus.R;
import app.infobus.entity.clsPathBus;
import app.infobus.utils.ULog;
import app.infobus.utils.Utility;

public class BusAdapter extends ArrayAdapter<clsPathBus> {

	private String tag = BusAdapter.class.getSimpleName();
	ArrayList<clsPathBus> arrPathBus;
	Activity activity;

	// public BusAdapter(Context context, int textViewResourceId, List<clsPathBus> objects) {
	// super(context, textViewResourceId, objects);
	// }

	public BusAdapter(Activity activity, ArrayList<clsPathBus> arrPathBus) {
		super(activity, R.layout.list_num, arrPathBus);
		this.arrPathBus = arrPathBus;
		this.activity = activity;
		ULog.i(tag, "BusAdapter....");

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		clsPathBus clsBus;
		String str;
		TextView tv;
		// Log.i("BusAdapter-HuynhTD", "getview.... position: " + position);
		if(convertView ==null){
		LayoutInflater inflater = activity.getLayoutInflater();
		convertView = inflater.inflate(R.layout.list_num, null, true);
		}
		clsBus = arrPathBus.get(position);
		tv = (TextView) convertView.findViewById(R.id.tvNum);
		tv.setText(clsBus.getNum());
		tv = (TextView) convertView.findViewById(R.id.tvNamePath);
		tv.setText(clsBus.getNamePath());
		tv = (TextView) convertView.findViewById(R.id.tvStart);
		str = Utility.ArrToString(clsBus.getPathStart());
		tv.setText(str);

		return convertView;
	}

	
}
