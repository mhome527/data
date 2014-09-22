package app.infobus;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import app.infobus.adapter.BusAdapter;
import app.infobus.dropbox.DownloadJson;
import app.infobus.entity.AdEntity;
import app.infobus.entity.clsItem;
import app.infobus.entity.clsListData;
import app.infobus.entity.clsPathBus;
import app.infobus.utils.Common;
import app.infobus.utils.Constant;
import app.infobus.utils.InternalStorage;
import app.infobus.utils.ULog;
import app.infobus.utils.Utility;

public class InfoBusActivity extends BaseActivity {

	String tag = InfoBusActivity.class.getSimpleName();
	private ListView lstBus;
	private BusAdapter adapterHCM = null;
	private BusAdapter adapterHN = null;
	private LinearLayout lnSearch;
	private RadioButton rbtnHcm;
	private boolean isClick = false;
	public boolean isHCM = true;

	public static ArrayList<clsPathBus> arrPathBusHCM = null;
	public static ArrayList<clsPathBus> arrPathBusHN = null;

	@Override
	protected int getViewLayoutId() {
		return R.layout.info_bus_layout;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		try {
			ULog.i(tag, "initView....");
			lstBus = (ListView) findViewById(R.id.lstBus);
			lnSearch = (LinearLayout) findViewById(R.id.search);
			rbtnHcm = (RadioButton) findViewById(R.id.rbtnHcm);

			setListenerView();

			new LoadData(this).execute();

			// ///////ad
			AdView adView = (AdView) this.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
			// //////////////////

			// get data from dropbox
			 GetDataDropbox();

			// GA
			Utility.setScreenNameGA("InfoBusActivity");
			
			//
			
		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isClick = false;
		if (adapterHCM != null)
			adapterHCM.isClick = false;
		if (adapterHN != null)
			adapterHN.isClick = true;
	}

	private void setListenerView() {
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
				intent.putExtra(Constant.HCM, isHCM);

				startActivity(intent);
				if (isHCM)
					Utility.setEventGA(Constant.GA_HCM +"-list", pathBus.getNum());
				else
					Utility.setEventGA(Constant.GA_HN +"-list", pathBus.getNum());
			}
		});

		rbtnHcm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ULog.i(InfoBusActivity.class, "radio checked:" + isChecked);
				isHCM = isChecked;
				updateListBus(isChecked);
			}

		});

		// search
		lnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isClick)
					return;
				isClick = true;
				Intent i = new Intent(InfoBusActivity.this, SearchActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra(Constant.KEY_CITY, isHCM);
				startActivity(i);
			}
		});
	}

	/**
	 * update list
	 * 
	 * @param isChecked
	 *            (true: HCM; false: HaNoi)
	 */
	private void updateListBus(boolean isChecked) {
		ULog.i(InfoBusActivity.class, "updateListBus Load bus value:" + isChecked);
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
		} else {
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

	// private void test() {
	// clsPolylineEntity entity = new clsPolylineEntity();
	// InternalStorage.writeObject(this, "s", entity);
	// InternalStorage.readObject(this, "s");
	// }

	private class LoadData extends AsyncTask<Object, Object, Boolean> {
		clsListData clsList;
		private InfoBusActivity activity;

		public LoadData(InfoBusActivity activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Object... params) {

			try {
				// arrPathBus = ReadData.getPathData(activity);
				// clsList = Common.getDataHCM(activity);
				if (isHCM)
					clsList = Common.getDataBus(activity, Constant.HCM);
				else
					clsList = Common.getDataBus(activity, Constant.HANNOI);

				if (clsList == null)
					return false;
				else
					return true;
				// return getPathData(activity);
			} catch (Exception e) {
				ULog.e(InfoBusActivity.class, "LoadData doing Error:" + e.getMessage());
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			clsPathBus clsPath;
			String date_cache;

			super.onPostExecute(result);
			try {
				ULog.i(InfoBusActivity.class, "LoadData doing.... result:" + result);
				if (result) {
					if (isHCM) {
						arrPathBusHCM = new ArrayList<clsPathBus>();
						for (clsItem item : clsList.list) {
							clsPath = new clsPathBus();
							clsPath.setNum(item.getNum());
							clsPath.setNamePath(item.getName());
							clsPath.setPathStart(item.getStart().toString().split(" - "));
							clsPath.setPathBack(item.getBack().split(" - "));
							clsPath.setInfo(item.getInfo());
							clsPath.locS = item.locS;
							clsPath.locB = item.locB;
							arrPathBusHCM.add(clsPath);

						}
						adapterHCM = new BusAdapter(activity, arrPathBusHCM);
						lstBus.setAdapter(adapterHCM);
						lstBus.post(new Runnable() {
							@Override
							public void run() {
								adapterHCM.notifyDataSetChanged();
							}
						});

						date_cache = BaseActivity.pref.getStringValue("", Constant.CLEAR_CACHE_HCM);
						ULog.i(InfoBusActivity.class, "date cache hcm: " + date_cache +"; json:" + clsList.update_date);

						if (clsList.update_date != null && !clsList.update_date.equals("") && !clsList.update_date.equals(date_cache)) {
							for (String str : clsList.update_bus) {
								if (str != null && !str.equals("")) {
									ULog.i(InfoBusActivity.class, "************** clear cache bus:" + str + ", date:" + clsList.update_date);

									InternalStorage.writeObject(InfoBusActivity.this, str + "s", "");
									InternalStorage.writeObject(InfoBusActivity.this, str + "b", "");
								}
							}
						}
						BaseActivity.pref.putStringValue(clsList.update_date, Constant.CLEAR_CACHE_HCM);

					} else {
						arrPathBusHN = new ArrayList<clsPathBus>();
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
							clsPath.locS = item.locS;
							clsPath.locB = item.locB;
							arrPathBusHN.add(clsPath);

						}

						adapterHN = new BusAdapter(activity, arrPathBusHN);
						lstBus.setAdapter(adapterHN);
						lstBus.post(new Runnable() {
							@Override
							public void run() {
								adapterHN.notifyDataSetChanged();
							}
						});

						date_cache = BaseActivity.pref.getStringValue("", Constant.CLEAR_CACHE_HN);
						ULog.i(InfoBusActivity.class, "date cache hn: " + date_cache + "; json:" + clsList.update_date);
						if (clsList.update_date != null && !clsList.update_date.equals("") && !clsList.update_date.equals(date_cache)) {
							for (String str : clsList.update_bus) {
								if (str != null && !str.equals("")) {
									ULog.i(InfoBusActivity.class, "************** clear cache bus:" + str + ", date:" + clsList.update_date);

									InternalStorage.writeObject(InfoBusActivity.this, str + "shn", "");
									InternalStorage.writeObject(InfoBusActivity.this, str + "bhn", "");
								}
							}
						}
						BaseActivity.pref.putStringValue(clsList.update_date, Constant.CLEAR_CACHE_HN);
					}

				} else
					ULog.i(tag, "Loading-PostExecute Load fail");
				ULog.i(tag, "Loading-PostExecute done");

			} catch (Exception e) {
				ULog.e(InfoBusActivity.class, "LoadData Exception " + e.getMessage());
			}
		}
	}

	// dropbox
	private void GetDataDropbox() {
		// download json
		new DownloadJson(this) {
			@Override
			protected void onPostExecute(Boolean[] result) {
				super.onPostExecute(result);

				try {
					if (result[0]) {
						sendNotification(InfoBusActivity.this);
						ULog.i(InfoBusActivity.class, "************* AD **************");
					} else if (result[1] || result[2]) {
						new LoadData(InfoBusActivity.this).execute();
					} else {
						ULog.i(InfoBusActivity.class, "Not update");
					}
				} catch (Exception e) {
					ULog.e(DownloadJson.class, "Read json error; " + e.getMessage());
					if (BuildConfig.DEBUG)
						e.printStackTrace();
				}

			}

		}.execute();
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	private void sendNotification(Context context) {
		AdEntity entity;
		int icon;
		ULog.i(InfoBusActivity.class, "sendNotification...");

		try {
			entity = (AdEntity) Common.getObjectJsonPath(AdEntity.class, getCacheDir().getAbsoluteFile() + "/" + Constant.JSON_AD);
			if (entity != null) {
				ULog.i(InfoBusActivity.class, "sendNotification: " + entity.packageName + "; title" + entity.title);

				if (getAppINstalled(entity.packageName))
					return;

				if (entity.icon.equals("icon_slider"))
					icon = R.drawable.icon_slider;
				else
					icon = R.drawable.icon_new;
				ULog.i(InfoBusActivity.class, "sendNotification create 2 ");

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + entity.packageName));
				// Open NotificationView.java Activity
				PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

				NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(icon, entity.title, System.currentTimeMillis());

				notification.flags = notification.FLAG_AUTO_CANCEL;
				if (Build.VERSION.SDK_INT > 8) {
					ULog.i(InfoBusActivity.class, "create push 222");
					RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
					//
					contentView.setImageViewResource(R.id.imgNotification, icon);
					contentView.setTextViewText(R.id.tvTitle, entity.title);
					notification.contentView = contentView;
					notification.contentIntent = pendingIntent;
					// notification.defaults = Notification.DEFAULT_VIBRATE;
				} else {
					notification.setLatestEventInfo(context, context.getString(R.string.new_app), entity.title, pendingIntent);
				}
				ULog.i(InfoBusActivity.class, "Push notification....");
				notificationManager.notify(R.string.app_name, notification);
			} else {
				ULog.e(InfoBusActivity.class, "Can't ad.json !!!!!!!!!!!!!!!!!!!");
			}

		} catch (Exception ex) {
			ULog.e(InfoBusActivity.class, "showNotification Error:" + ex.getMessage());
		}
	}

	private boolean getAppINstalled(String packageName) {
		try {
			final PackageManager pm = getPackageManager();
			// get a list of installed apps.
			List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

			for (ApplicationInfo packageInfo : packages) {
				if (packageName.equals(packageInfo.packageName))
					return true;
				// ULog.i(this, "Installed package :" + packageInfo.packageName);
				// ULog.i(this, "Source dir : " + packageInfo.sourceDir);
				// ULog.i(this, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
			}
		} catch (Exception e) {
			ULog.e(this, "getAppINstalled error:" + e.getMessage());
		}

		return false;
	}
}