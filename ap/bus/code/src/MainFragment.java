package app.infobus;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import app.infobus.adapter.ListBusAdapter;
import app.infobus.adapter.TaxiAdapter;
import app.infobus.dropbox.DownloadJson;
import app.infobus.entity.AdEntity;
import app.infobus.entity.clsItem;
import app.infobus.entity.clsListData;
import app.infobus.entity.clsPathBus;
import app.infobus.entity.clsTaxiEntity;
import app.infobus.utils.Common;
import app.infobus.utils.Constant;
import app.infobus.utils.InternalStorage;
import app.infobus.utils.ULog;
import app.infobus.utils.Utility;
import app.infobus.view.MainLayout;

public class MainFragment extends BaseActivity implements View.OnClickListener {

	private String tag = MainFragment.class.getSimpleName();
	private ListView lstBus;
	private ListBusAdapter adapterHCM = null;
	private ListBusAdapter adapterHN = null;
	private TextView tvTilteMenu;
	private RadioButton rbtnHcm;
	private boolean isClick = false;
	public boolean isHCM = true;

	public static ArrayList<clsPathBus> arrPathBusHCM = null;
	public static ArrayList<clsPathBus> arrPathBusHN = null;
	// ListView menu
	private ListView lvMenu;
	private TaxiAdapter txAdapter;
	private clsTaxiEntity txEntity;
	private MainLayout mainLayout;
	private ProgressDialog progressDialog;

	@Override
	protected int getViewLayoutId() {
		return R.layout.main_layout;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		try {
			ULog.i(tag, "initView....");

			// //////
			mainLayout = this.getViewLayout();
			lstBus = (ListView) findViewById(R.id.lstBus);
			rbtnHcm = (RadioButton) findViewById(R.id.rbtnHcm);
			ImageButton imgTaxi = getViewChild(R.id.imgTaxi);
			TextView tvDistanceTaxi = getViewChild(R.id.tvDistanceTaxi);

			ImageButton imgNextDistance = getViewChild(R.id.imgNextDistance);
			RelativeLayout rlDistanceTaxi = getViewChild(R.id.rlDistanceTaxi);

			rlDistanceTaxi.setOnClickListener(this);
			tvDistanceTaxi.setOnClickListener(this);
			imgTaxi.setOnClickListener(this);
			imgNextDistance.setOnClickListener(this);

			setListenerView();

			new LoadData(this).execute();
			new LoadTaxi(this).execute();

			setHeaderMenu();
			setTitleMenu(false);

			// ///////ad
			AdView adView = (AdView) this.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
			// //////////////////

			// get data from dropbox
			GetDataDropbox();

			// GA
			Utility.setScreenNameGA("Main Bus");

			//

		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isClick = false;
		if (adapterHCM != null)
			adapterHCM.isClick = false;
		if (adapterHN != null)
			adapterHN.isClick = false;
	}

	@Override
	public void onBackPressed() {
		if (mainLayout != null && mainLayout.isMenuShown()) {
			mainLayout.toggleMenu();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.imgMenu:
			setTitleMenu(!mainLayout.isMenuShown());
			mainLayout.toggleMenu();

			break;
		case R.id.rlDistanceTaxi:
		case R.id.tvDistanceTaxi:
		case R.id.imgTaxi:
		case R.id.imgNextDistance:
			i = new Intent(MainFragment.this, MapTaxi.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// i.putExtra(Constant.KEY_CITY, isHCM);
			startActivity(i);
			break;
		case R.id.imgSearch:
			if (isClick)
				return;
			isClick = true;
			i = new Intent(MainFragment.this, SearchActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra(Constant.KEY_CITY, isHCM);
			startActivity(i);
			break;

		}

	}

	private void setTitleMenu(boolean isMenu) {
		if (!isMenu) {
			if (isHCM)
				tvTilteMenu.setText(getString(R.string.bus) + " - " + getString(R.string.HCM));
			else
				tvTilteMenu.setText(getString(R.string.bus) + " - " + getString(R.string.HN));
		} else {
			if (isHCM)
				tvTilteMenu.setText(getString(R.string.taxi) + " - " + getString(R.string.HCM));
			else
				tvTilteMenu.setText(getString(R.string.taxi) + " - " + getString(R.string.HN));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// mainLayout.toggleMenu();
	// return true;
	// }

	private void setListenerView() {
		lstBus.setItemsCanFocus(false);

		rbtnHcm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ULog.i(MainFragment.class, "radio checked:" + isChecked);
				isHCM = isChecked;
				updateListBus();
			}

		});

	}

	@SuppressLint("InflateParams")
	private void setHeaderMenu() {
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.header_menu);

		ActionBar mActionBar = MainFragment.this.getActionBar();
		mActionBar.show();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.header_menu, null);

		tvTilteMenu = (TextView) mCustomView.findViewById(R.id.tvTilteMenu);
		tvTilteMenu.setText(getString(R.string.info));
		ImageButton imgMenu = (ImageButton) mCustomView.findViewById(R.id.imgMenu);
		imgMenu.setOnClickListener(this);

		ImageButton imgTaxi = (ImageButton) mCustomView.findViewById(R.id.imgTaxi);
		imgTaxi.setOnClickListener(this);

		ImageButton imgSearch = (ImageButton) mCustomView.findViewById(R.id.imgSearch);
		imgSearch.setOnClickListener(this);

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	/**
	 * update list
	 * 
	 * @param isChecked
	 *            (true: HCM; false: HaNoi)
	 */
	private void updateListBus() {
		ULog.i(MainFragment.class, "updateListBus Load bus value:" + isHCM);
		setTitleMenu(false);
		if (isHCM) {
			if (adapterHCM == null || adapterHCM.getCount() < 1)
				new LoadData(MainFragment.this).execute();
			else {
				lstBus.setAdapter(adapterHCM);
				lstBus.post(new Runnable() {
					@Override
					public void run() {
						adapterHCM.notifyDataSetChanged();
					}
				});
			}

			if (txEntity != null)
				txAdapter.setAdapterTX(txEntity.hcm);

		} else {
			if (adapterHN == null || adapterHN.getCount() < 1)
				new LoadData(MainFragment.this).execute();
			else {
				lstBus.setAdapter(adapterHN);
				lstBus.post(new Runnable() {
					@Override
					public void run() {
						adapterHN.notifyDataSetChanged();
					}
				});
			}
			if (txEntity != null)
				txAdapter.setAdapterTX(txEntity.hanoi);
		}
	}

	// private void test() {
	// clsPolylineEntity entity = new clsPolylineEntity();
	// InternalStorage.writeObject(this, "s", entity);
	// InternalStorage.readObject(this, "s");
	// }
	private class LoadTaxi extends AsyncTask<Object, Object, Boolean> {
		private Context context;

		public LoadTaxi(Context context) {
			this.context = context;
		}

		@Override
		protected Boolean doInBackground(Object... arg0) {
			if (txEntity != null)
				return true;
			txEntity = (clsTaxiEntity) Common.getObjectJson(context, clsTaxiEntity.class, Constant.TAXI);
			if (txEntity == null)
				return false;
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result == false) {
				ULog.e(MainFragment.class, "load data taxi error");
				return;
			}
			if (BaseActivity.pref.getLongValue(0, Constant.PRICE1_TAXI) == 0) {
				BaseActivity.pref.putLongValue(txEntity.price1, Constant.PRICE1_TAXI);
				BaseActivity.pref.putLongValue(txEntity.price2, Constant.PRICE2_TAXI);
				BaseActivity.pref.putLongValue(txEntity.price_begin, Constant.PRICE_BEGIN_TAXI);
				BaseActivity.pref.putIntValue(txEntity.default_km, Constant.KM_TAXI);
			}
			ULog.i(MainFragment.class, "load data taxi");
			if (isHCM)
				txAdapter = new TaxiAdapter(context, txEntity.hcm);
			else
				txAdapter = new TaxiAdapter(context, txEntity.hanoi);

			lvMenu = (ListView) findViewById(R.id.activity_main_menu_listview);
			lvMenu.setAdapter(txAdapter);
			lvMenu.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (mainLayout.isMenuShown())
						onMenuItemClick(parent, view, position, id);
					else
						ULog.i(this, "menu dont show");
				}

			});
		}
	}

	private class LoadData extends AsyncTask<Object, Object, Boolean> {
		clsListData clsList;
		private MainFragment activity;

		public LoadData(MainFragment activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(activity);
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
			}

			if (!isFinishing()) {
				progressDialog.show();
			}
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
				ULog.e(MainFragment.class, "LoadData doing Error:" + e.getMessage());
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			clsPathBus clsPath;
			String date_cache;

			super.onPostExecute(result);
			try {
				ULog.i(MainFragment.class, "LoadData doing.... result:" + result);
				if (!isFinishing()) {
					progressDialog.dismiss();
				}
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
						adapterHCM = new ListBusAdapter(activity, arrPathBusHCM);
						lstBus.setAdapter(adapterHCM);
						lstBus.post(new Runnable() {
							@Override
							public void run() {
								adapterHCM.notifyDataSetChanged();
							}
						});

						date_cache = BaseActivity.pref.getStringValue("", Constant.CLEAR_CACHE_HCM);
						ULog.i(MainFragment.class, "date cache hcm: " + date_cache + "; json:" + clsList.update_date);

						if (clsList.update_date != null && !clsList.update_date.equals("") && !clsList.update_date.equals(date_cache)) {
							for (String str : clsList.update_bus) {
								if (str != null && !str.equals("")) {
									ULog.i(MainFragment.class, "************** clear cache bus:" + str + ", date:" + clsList.update_date);

									InternalStorage.writeObject(MainFragment.this, str + "s", "");
									InternalStorage.writeObject(MainFragment.this, str + "b", "");
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

						adapterHN = new ListBusAdapter(activity, arrPathBusHN);
						lstBus.setAdapter(adapterHN);
						lstBus.post(new Runnable() {
							@Override
							public void run() {
								adapterHN.notifyDataSetChanged();
							}
						});

						date_cache = BaseActivity.pref.getStringValue("", Constant.CLEAR_CACHE_HN);
						ULog.i(MainFragment.class, "date cache hn: " + date_cache + "; json:" + clsList.update_date);
						if (clsList.update_date != null && !clsList.update_date.equals("") && !clsList.update_date.equals(date_cache)) {
							for (String str : clsList.update_bus) {
								if (str != null && !str.equals("")) {
									ULog.i(MainFragment.class, "************** clear cache bus:" + str + ", date:" + clsList.update_date);

									InternalStorage.writeObject(MainFragment.this, str + "shn", "");
									InternalStorage.writeObject(MainFragment.this, str + "bhn", "");
								}
							}
						}
						BaseActivity.pref.putStringValue(clsList.update_date, Constant.CLEAR_CACHE_HN);
					}

				} else
					ULog.i(tag, "Loading-PostExecute Load fail");
				ULog.i(tag, "Loading-PostExecute done");

			} catch (Exception e) {
				ULog.e(MainFragment.class, "LoadData Exception " + e.getMessage());
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
						sendNotification(MainFragment.this);
						ULog.i(MainFragment.class, "************* AD **************");
					} else if (result[1] || result[2]) {
						new LoadData(MainFragment.this).execute();
					} else {
						ULog.i(MainFragment.class, "Not update");
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
		ULog.i(MainFragment.class, "sendNotification...");

		try {
			entity = (AdEntity) Common.getObjectJsonPath(AdEntity.class, getCacheDir().getAbsoluteFile() + "/" + Constant.JSON_AD);
			if (entity != null) {
				ULog.i(MainFragment.class, "sendNotification: " + entity.packageName + "; title" + entity.title);

				if (getAppINstalled(entity.packageName))
					return;

				if (entity.icon.equals("icon_slider"))
					icon = R.drawable.icon_slider;
				else
					icon = R.drawable.icon_new;
				ULog.i(MainFragment.class, "sendNotification create 2 ");

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + entity.packageName));
				// Open NotificationView.java Activity
				PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

				NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(icon, entity.title, System.currentTimeMillis());

				notification.flags = notification.FLAG_AUTO_CANCEL;
				if (Build.VERSION.SDK_INT > 8) {
					ULog.i(MainFragment.class, "create push 222");
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
				ULog.i(MainFragment.class, "Push notification....");
				notificationManager.notify(R.string.app_name, notification);
			} else {
				ULog.e(MainFragment.class, "Can't ad.json !!!!!!!!!!!!!!!!!!!");
			}

		} catch (Exception ex) {
			ULog.e(MainFragment.class, "showNotification Error:" + ex.getMessage());
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

	private void onMenuItemClick(AdapterView<?> parent, View view, final int position, long id) {
		String name;
		final String tel;
		if (isHCM) {
			tel = txEntity.hcm.get(position).tel;
			name = txEntity.hcm.get(position).name;
		} else {
			tel = txEntity.hanoi.get(position).tel;
			name = txEntity.hanoi.get(position).name;
		}

		// mainLayout.toggleMenu();
		DialogInterface.OnClickListener lisOk = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ULog.i(MainFragment.this, "tel:" + tel.replaceAll(".", "").replaceAll(" ", ""));
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + tel.replaceAll("[.]", "").replaceAll(" ", "")));
				MainFragment.this.startActivity(intent);
			}

		};

		DialogInterface.OnClickListener lisCancel = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}

		};

		Utility.dialogCallConfirm(MainFragment.this, name, lisOk, lisCancel);

		// Hide menu anyway
		// mainLayout.toggleMenu();
	}

}
