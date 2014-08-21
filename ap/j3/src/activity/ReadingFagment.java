package sjpn3.vn.activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import sjpn3.vn.Constant;
import sjpn3.vn.R;
import sjpn3.vn.Util.BitmapUtil;
import sjpn3.vn.Util.Common;
import sjpn3.vn.Util.Prefs;
import sjpn3.vn.Util.ULog;
import sjpn3.vn.adapter.ReadingFragmentAdapter;
import sjpn3.vn.model.ReadingModel;
import sjpn3.vn.model.subModel.ReadingDay;
import sjpn3.vn.slidingmenu.adapter.SlideMenuListAdapter;
import sjpn3.vn.view.CustomizePagerView;
import sjpn3.vn.view.ScaleImageView;
import sjpn3.vn.activity.ReadingFagment;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReadingFagment extends FragmentActivity implements OnPageChangeListener, OnClickListener {

	public static int widthScreen;
	public static int heightScreen;
//	private LinearLayout lnProgressDialog;
	private RelativeLayout rlReading;
	private TextView tvDay;

	private ImageView imgLeft;
	private ImageView imgRight;
	// private ScaleImageView imgReading;
	private ImageView imgClose;
	private boolean isClick = false;
	private CustomizePagerView myPager;
	private Button btnExercises;
	private Button btnShow;

	private int day = 1;

	// //slide menu
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ReadingDay allDay;

	private boolean isShow = false;

	// slide menu items
	private TypedArray navMenuIcons;

	private SlideMenuListAdapter adapterMenu;
	public Prefs pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading_layout);
//		lnProgressDialog = (LinearLayout) findViewById(R.id.content_progress_dialog);

		rlReading = (RelativeLayout) findViewById(R.id.rlReading);
		tvDay = (TextView) findViewById(R.id.tvDay);
		imgLeft = (ImageView) findViewById(R.id.imgLeft);
		imgRight = (ImageView) findViewById(R.id.imgRight);
		// imgReading = (ScaleImageView) findViewById(R.id.imgReading);
		imgClose = (ImageView) findViewById(R.id.imgClose);
		myPager = (CustomizePagerView) findViewById(R.id.pagerReading);
		btnShow = (Button) findViewById(R.id.btnShow);
		btnExercises = (Button) findViewById(R.id.btnExercises);

		btnShow.setOnClickListener(this);
		btnExercises.setOnClickListener(this);
		imgClose.setOnClickListener(this);
		imgLeft.setOnClickListener(this);
		imgRight.setOnClickListener(this);
		myPager.setOnPageChangeListener(this);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthScreen = dm.widthPixels;
		heightScreen = dm.heightPixels;

		if (pref == null)
			pref = new Prefs(this);

		day = pref.getIntValue(1, Constant.DAY_READING_LEARN);
		if (day <= 1) {
			imgLeft.setVisibility(View.INVISIBLE);
			day = 1;
		} else if (day >= Constant.READING_DAY_MAX) {
			imgRight.setVisibility(View.INVISIBLE);
			day = Constant.READING_DAY_MAX;
		}
		tvDay.setText(this.getString(R.string.level) + " " + day);
	        
		createLayoutSlideMenu();

		new LoadData().execute();

		// ///////ad
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
		adView.loadAd(adRequest);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		ULog.i(ReadingPager.class, "**** onPageSelected = " + position);
		isClick = false;
		day = position + 1;
		tvDay.setText(ReadingFagment.this.getString(R.string.level) + " " + day);
		// /
		// slide menu
		adapterMenu = new SlideMenuListAdapter(ReadingFagment.this, allDay.day.get(position).readingList);
		// setting the nav drawer list adapter
		mDrawerList.setAdapter(adapterMenu);

		// //
		if (day == Constant.READING_DAY_MAX) {
			imgRight.setVisibility(View.GONE);

		} else if (day == 1) {
			imgLeft.setVisibility(View.GONE);

		} else {
			imgRight.setVisibility(View.VISIBLE);
			imgLeft.setVisibility(View.VISIBLE);
		}

		pref.putIntValue(day, Constant.DAY_READING_LEARN);

	}

	// //////////

	@Override
	public void onBackPressed() {
		if (isShow) {
			showImageZoom(false);
		} else
			super.onBackPressed();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isClick = false;
		isShow = false;
		isShow = false;
		showImageZoom(false);

	}

	private void showImageZoom(boolean show) {
		ReadingModel model;
		isShow = show;

		// test
		// ImageView tv = (ImageView)myPager.findViewWithTag("img" + (day -1));
		// ULog.i(ReadingPager.class, "showImageZoom tv:" + tv.getText());
		// //
		ScaleImageView imgReading = (ScaleImageView) findViewById(R.id.imgReading);
		if (allDay == null || allDay.day == null)
			return;
		model = allDay.day.get(day - 1);

		if (show) {
			myPager.setPagingEnabled(false);
			rlReading.setVisibility(View.VISIBLE);
			int idReading = getResources().getIdentifier(model.img, "drawable", getPackageName());
			imgReading.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(getResources(), idReading,
					widthScreen - 10, widthScreen - 10));
			ImageView img = (ImageView) myPager.findViewWithTag("img" + (day - 1));
			img.setImageBitmap(null);

		} else {
			rlReading.setVisibility(View.GONE);
			imgReading.setImageBitmap(null);
			// myPager.setEnabled(false);
			myPager.setPagingEnabled(true);
			int idReading = getResources().getIdentifier(model.img, "drawable", getPackageName());
			ImageView img = (ImageView) myPager.findViewWithTag("img" + (day - 1));
			img.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(getResources(), idReading, widthScreen - 10,
					widthScreen - 10));
		}
		System.gc();
	}

	private class LoadData extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			lnProgressDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			ULog.i(this, "load day:" + day);
			allDay = (ReadingDay) Common.getObjectJson(ReadingFagment.this, ReadingDay.class, "reading");
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
//			lnProgressDialog.setVisibility(View.GONE);
			if (allDay != null) {

				// adapter = new ReadingPagerAdapter(ReadingFagment.this, allDay.day);
				// myPager.setAdapter(adapter);
				myPager.setAdapter(new ReadingFragmentAdapter(ReadingFagment.this.getSupportFragmentManager(),
						allDay.day));
				myPager.setCurrentItem(day - 1);

				// slide menu
				adapterMenu = new SlideMenuListAdapter(ReadingFagment.this, allDay.day.get(0).readingList);
				// setting the nav drawer list adapter
				mDrawerList.setAdapter(adapterMenu);
			} else {
				ULog.e(ReadingPager.class, "Loaddata Json error:");
			}
		}
	}

	// //////////////////////////////
	// /////////////////////////////
	@SuppressLint("InflateParams")
	private void createLayoutSlideMenu() {

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.test // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// header ad
		View header = getLayoutInflater().inflate(R.layout.ad, null);
		mDrawerList.addHeaderView(header);
		AdView adView = (AdView) header.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// display view for selected nav drawer item
			// displayView(position);
			ULog.i(this, "onItemClick() " + position + "; id:" + id);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ULog.i(this, "onOptionsItemSelected");
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during onPostCreate() and onConfigurationChanged()...
	 */
	//
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (mDrawerToggle == null)
			return;
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// /////
	private void nextReading() {
		if (isClick || day == Constant.READING_DAY_MAX || isShow)
			return;

		isClick = true;
		day = day + 1;
		tvDay.setText(ReadingFagment.this.getString(R.string.level) + " " + day);
		if (day == Constant.READING_DAY_MAX)
			imgRight.setVisibility(View.GONE);
		else
			imgRight.setVisibility(View.VISIBLE);
		pref.putIntValue(day, Constant.DAY_READING_LEARN);
		imgLeft.setVisibility(View.VISIBLE);
		myPager.setCurrentItem(day - 1);
		// myPager.animate().translationX(0f).setDuration(1700);

		// slide menu
		adapterMenu = new SlideMenuListAdapter(ReadingFagment.this, allDay.day.get(day - 1).readingList);
		// setting the nav drawer list adapter
		mDrawerList.setAdapter(adapterMenu);
	}

	private void preReading() {
		if (isClick || day == 1 || isShow)
			return;
		isClick = true;
		day = day - 1;
		tvDay.setText(ReadingFagment.this.getString(R.string.level) + " " + day);
		if (day == 1)
			imgLeft.setVisibility(View.GONE);
		else
			imgLeft.setVisibility(View.VISIBLE);
		pref.putIntValue(day, Constant.DAY_READING_LEARN);
		imgRight.setVisibility(View.VISIBLE);
		myPager.setCurrentItem(day - 1);
		// myPager.animate().translationX(0f).setDuration(1700);

		// slide menu
		adapterMenu = new SlideMenuListAdapter(ReadingFagment.this, allDay.day.get(day - 1).readingList);
		// setting the nav drawer list adapter
		mDrawerList.setAdapter(adapterMenu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgLeft:
			preReading();
			break;
		case R.id.imgRight:
			nextReading();
			break;
		case R.id.btnShow:
			showImageZoom(true);
			break;
		case R.id.btnExercises:
			mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.imgClose:
			showImageZoom(false);
			break;
		}
	}

}
