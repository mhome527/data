package app.infobus.adapter;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import app.infobus.InfoDetailActivity;
import app.infobus.MainFragment;
import app.infobus.MapBus;
import app.infobus.R;
import app.infobus.entity.clsPathBus;
import app.infobus.utils.Constant;
import app.infobus.utils.ULog;
import app.infobus.utils.Utility;

@SuppressLint("InflateParams")
public class ListBusAdapter extends ArrayAdapter<clsPathBus> implements SectionIndexer {

	private String tag = ListBusAdapter.class.getSimpleName();
	private ArrayList<clsPathBus> arrPathBus;
	private MainFragment activity;
	private String[] alpha;
	public boolean isClick = false;

	// public BusAdapter(Context context, int textViewResourceId, List<clsPathBus> objects) {
	// super(context, textViewResourceId, objects);
	// }

	public ListBusAdapter(MainFragment activity, ArrayList<clsPathBus> arrPathBus) {
		super(activity, R.layout.list_num, arrPathBus);
		this.arrPathBus = arrPathBus;
		this.activity = activity;
		int i = 0;
		ULog.i(tag, "BusAdapter....size:" + arrPathBus.size());
		// if(arrPathBus.size() < 90)
		// return;
		alpha = null;
		alpha = new String[arrPathBus.size()];
		for (clsPathBus bus : arrPathBus) {
			alpha[i++] = bus.getNum();
			// if(arrPathBus.size() < 90 && i >20)
			// return;
		}
		isClick = false;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		clsPathBus clsBus;
		String str;
		TextView tv;
		LinearLayout llDetail;
		ImageButton imgMap;
		LinearLayout llMap;

		if (convertView == null) {
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

		llMap = (LinearLayout) convertView.findViewById(R.id.llMap);

		if (clsBus.locS != null && clsBus.locS.size() > 0) {
			llMap.setVisibility(View.VISIBLE);
		} else {
			llMap.setVisibility(View.GONE);
		}
		//
		isClick = false;
		imgMap = (ImageButton) convertView.findViewById(R.id.imgMap);
		llMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// LinearLayout vwParentRow = (LinearLayout)v.getParent().getParent();
				// vwParentRow.setBackgroundColor(Color.YELLOW);
				// vwParentRow.refreshDrawableState();
				if (isClick)
					return;
				isClick = true;
				if (!Utility.checkNetwork(activity)) {
					Utility.dialogWifi(activity);
					isClick = false;
				} else {
					Intent intent = new Intent(activity, MapBus.class);
					// Intent intent = new Intent(activity, MapBus_bk.class);
					intent.putExtra("num", position);
					intent.putExtra(Constant.KEY_CITY, activity.isHCM);
					activity.startActivity(intent);
				}
				ULog.i(ListBusAdapter.class, "map click value: " + activity.isHCM);
			}
		});

		imgMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// LinearLayout vwParentRow = (LinearLayout)v.getParent().getParent();
				// vwParentRow.setBackgroundColor(Color.YELLOW);
				// vwParentRow.refreshDrawableState();
				if (isClick)
					return;
				isClick = true;
				if (!Utility.checkNetwork(activity)) {
					Utility.dialogWifi(activity);
					isClick = false;
				} else {
					Intent intent = new Intent(activity, MapBus.class);
					intent.putExtra("num", position);
					intent.putExtra(Constant.KEY_CITY, activity.isHCM);
					activity.startActivity(intent);
				}
				ULog.i(ListBusAdapter.class, "map2 click value: " + activity.isHCM);

			}
		});

		llDetail = (LinearLayout) convertView.findViewById(R.id.llDetail);
		llDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// LinearLayout vwParentRow = (LinearLayout)v.getParent().getParent();
				// vwParentRow.setBackgroundColor(Color.YELLOW);
				// vwParentRow.refreshDrawableState();

				Intent intent = new Intent(activity, InfoDetailActivity.class);
				clsPathBus pathBus = arrPathBus.get(position);
				intent.putExtra("num", pathBus.getNum());
				intent.putExtra("namePath", pathBus.getNamePath());
				intent.putExtra("start", Utility.ArrToString(pathBus.getPathStart()));
				intent.putExtra("back", Utility.ArrToString(pathBus.getPathBack()));
				intent.putExtra("info", pathBus.getInfo());
				intent.putExtra(Constant.KEY_CITY, activity.isHCM);
				activity.startActivity(intent);
			}
		});

		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(activity, InfoDetailActivity.class);
				clsPathBus pathBus = arrPathBus.get(position);
				intent.putExtra("num", pathBus.getNum());
				intent.putExtra("namePath", pathBus.getNamePath());
				intent.putExtra("start", Utility.ArrToString(pathBus.getPathStart()));
				intent.putExtra("back", Utility.ArrToString(pathBus.getPathBack()));
				intent.putExtra("info", pathBus.getInfo());
				intent.putExtra(Constant.KEY_CITY, activity.isHCM);
				activity.startActivity(intent);
			}
		});

		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		return section;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		ULog.i(tag, "getSections....size:" + arrPathBus.size());

		return alpha;
	}

}
