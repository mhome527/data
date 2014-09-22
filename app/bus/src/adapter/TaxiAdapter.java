package app.infobus.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.infobus.R;
import app.infobus.entity.clsTaxiEntity;
import app.infobus.utils.ULog;

public class TaxiAdapter extends BaseAdapter {
	private List<clsTaxiEntity.clsTaxiItem> adapter;
	private Context context;
	LayoutInflater layoutInflater;

	public TaxiAdapter(Context context, List<clsTaxiEntity.clsTaxiItem> adapter) {
		layoutInflater = LayoutInflater.from(context);
		this.adapter = adapter;
		this.context= context; 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return adapter.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return adapter.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.taxi_item, null);
		}
		try {
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			TextView tvTel = (TextView) convertView.findViewById(R.id.tvTel);
			ImageView imgLogo = (ImageView) convertView.findViewById(R.id.imgLogo);

			tvName.setText(adapter.get(pos).name);
			tvTel.setText(adapter.get(pos).tel);
			if (adapter.get(pos).img != null && !adapter.get(pos).img.equals("")){
				imgLogo.setBackgroundResource(context.getResources().getIdentifier(adapter.get(pos).img, "drawable",
						context.getPackageName()));
			}else{
				imgLogo.setBackgroundResource(R.drawable.call);
			}
		} catch (Exception e) {
			ULog.e(this, "getView error:" + e.getMessage());
		}
		
		return convertView;
	}

	public void setAdapterTX(List<clsTaxiEntity.clsTaxiItem> adapter) {
		this.adapter = adapter;
		notifyDataSetChanged();
	}
}
