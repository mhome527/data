package app.infobus;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import app.infobus.adapter.BusAdapter;
import app.infobus.entity.clsItem;
import app.infobus.entity.clsListData;
import app.infobus.entity.clsPathBus;
import app.infobus.utils.Common;
import app.infobus.utils.Constant;
import app.infobus.utils.ULog;
import app.infobus.utils.Utility;

public class InfoBusActivity extends AbstractActivity {

	String tag = InfoBusActivity.class.getSimpleName();
	private ListView lstBus;
	private BusAdapter adapterHCM = null;
	private BusAdapter adapterHN = null;

	public static ArrayList<clsPathBus> arrPathBus = null;

	@Override
	protected int getViewLayoutId() {
		return R.layout.info_bus;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		try {
			ULog.i(tag, "initView....");
			lstBus = (ListView) findViewById(R.id.lstBus);
			lstBus.setItemsCanFocus(false);
			lstBus.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterBus, View arg1, int position, long id) {
					Intent intent = new Intent(InfoBusActivity.this, InfoDetailActivity.class);
					clsPathBus pathBus = (clsPathBus) adapterBus.getItemAtPosition(position);
					intent.putExtra("num", pathBus.getNum());
					intent.putExtra("namePath", pathBus.getNamePath());
					intent.putExtra("start", Utility.ArrToString(pathBus.getPathStart()));
					intent.putExtra("back", Utility.ArrToString(pathBus.getPathBack()));
					intent.putExtra("info", pathBus.getInfo());
					startActivity(intent);
				}
			});

			rbtnHcm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						if (adapterHCM == null || adapterHCM.getCount() < 1)
							new LoadData(InfoBusActivity.this).execute();
						else {
							lstBus.setAdapter(adapterHCM);
							lstBus.post(new Runnable() {
						        @Override
						        public void run() {
						        	adapterHCM.notifyDataSetChanged();
						        }
						    });
						}
					}else{
						if (adapterHN == null || adapterHN.getCount() < 1)
							new LoadData(InfoBusActivity.this).execute();
						else {
							lstBus.setAdapter(adapterHN);
							lstBus.post(new Runnable() {
						        @Override
						        public void run() {
						        	adapterHN.notifyDataSetChanged();
						        }
						    });
						}
					}
				}

			});

			// rbtnHN.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//
			// @Override
			// public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// new LoadData(InfoBusActivity.this).execute();
			// }
			// });

			new LoadData(this).execute();

			// ///////ad
//			AdView adView = (AdView) this.findViewById(R.id.adView);
//			AdRequest adRequest = new AdRequest.Builder().build();
//			adView.loadAd(adRequest);
			// //////////////////
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class LoadData extends AsyncTask<Object, Object, Object> {
		clsListData clsList;
		private Activity activity;

		public LoadData(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Object doInBackground(Object... params) {

			try {
				// arrPathBus = ReadData.getPathData(activity);
				// clsList = Common.getDataHCM(activity);
				if (rbtnHcm.isChecked())
					clsList = Common.getDataBus(activity, Constant.HCM);
				else
					clsList = Common.getDataBus(activity, Constant.HANNOI);

				if (clsList == null)
					return false;
				else
					return true;
				// return getPathData(activity);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			clsPathBus clsPath;
			super.onPostExecute(result);
			boolean b = false;
			b = (Boolean) result;
			if (b) {
				arrPathBus = new ArrayList<clsPathBus>();
				// for (int i = 0; i < clsList.list.size(); i++) {
				for (clsItem item : clsList.list) {
					// clsItem item = (clsItem) clsList.list.get(i);
					// clsItem item = clsListData.class.cast(clsList.list.get(i));
					// clsItem item = clsList.getItem(i);
					clsPath = new clsPathBus();
					clsPath.setNum(item.getNum());
					clsPath.setNamePath(item.getName());
					clsPath.setPathStart(item.getStart().toString().split(" - "));
					clsPath.setPathBack(item.getBack().split(" - "));
					clsPath.setInfo(item.getInfo());
					arrPathBus.add(clsPath);

				}
				// lstBus.setAdapter(null);
				if (rbtnHcm.isChecked()){
					adapterHCM = new BusAdapter(activity, arrPathBus);
					lstBus.setAdapter(adapterHCM);
					lstBus.post(new Runnable() {
				        @Override
				        public void run() {
				        	adapterHCM.notifyDataSetChanged();
				        }
				    });
				}else{
					adapterHN = new BusAdapter(activity, arrPathBus);
					lstBus.setAdapter(adapterHN);
					lstBus.post(new Runnable() {
				        @Override
				        public void run() {
				        	adapterHN.notifyDataSetChanged();
				        }
				    });
				}

			} else
				ULog.i(tag, "Loading-PostExecute Load fail");
			ULog.i(tag, "Loading-PostExecute done");
		}
	}

}