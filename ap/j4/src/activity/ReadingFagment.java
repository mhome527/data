package sjpn4.vn.activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import sjpn4.vn.Constant;
import sjpn4.vn.R;
import sjpn4.vn.Util.Common;
import sjpn4.vn.Util.Prefs;
import sjpn4.vn.Util.ULog;
import sjpn4.vn.adapter.ReadingFragmentAdapter;
import sjpn4.vn.model.subModel.ReadingDay;
import sjpn4.vn.slidingmenu.adapter.SlideMenuListAdapter;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ReadingFagment extends FragmentActivity implements OnPageChangeListener, OnClickListener {

	// private LinearLayout lnProgressDialog;
	private TextView tvDay;

	private ImageView imgLeft;
	private ImageView imgRight;
	private boolean isClick = false;
	private ViewPager myPager;
	private Button btnExercises;

	private int day = 1;

	// //slide menu
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
//	private ReadingPagerAdapter adapter;
	private ReadingDay allDay;

	// slide menu items
	// private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private SlideMenuListAdapter adapterMenu;
	public Prefs pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading_layout);
		// PreferenceUtil.setBoolean(this, PreferenceUtil.START_APP_NOT_FIRST,true);
		// myPager = (ViewPager) findViewById(R.id.tutorial_view_pager);
		// myPager.setOnPageChangeListener(this);
		// myPager.setAdapter(new ReadingFragmentAdapter(getSupportFragmentManager()));

		if (pref == null)
			pref = new Prefs(this);
		// lnProgressDialog = (LinearLayout) findViewById(R.id.content_progress_dialog);

		tvDay = (TextView) findViewById(R.id.tvDay);
		imgLeft = (ImageView) findViewById(R.id.imgLeft);
		imgRight = (ImageView) findViewById(R.id.imgRight);
		myPager = (ViewPager) findViewById(R.id.pagerReading);
		btnExercises = (Button) findViewById(R.id.btnExercises);

		btnExercises.setOnClickListener(this);
		imgLeft.setOnClickListener(this);
		imgRight.setOnClickListener(this);
		myPager.setOnPageChangeListener(this);

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

		 ///////ad
		 AdView adView = (AdView) this.findViewById(R.id.adView);
		 AdRequest adRequest = new AdRequest.Builder().build();
		 adView.loadAd(adRequest);

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isClick = false;
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
	private class LoadData extends AsyncTask<Void, Void, Boolean> {

		// private ReadingFagment activity;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// lnProgressDialog.setVisibility(View.VISIBLE);
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
			// lnProgressDialog.setVisibility(View.GONE);
			if (allDay != null && allDay.day != null) {
				// adapter = new ReadingPagerAdapter(ReadingFagment.this, allDay.day);
				// myPager.setAdapter(adapter);
				myPager.setAdapter(new ReadingFragmentAdapter(ReadingFagment.this.getSupportFragmentManager(),
						allDay.day));

				myPager.setCurrentItem(day - 1);

				// slide menu
				adapterMenu = new SlideMenuListAdapter(ReadingFagment.this, allDay.day.get(0).readingList);
				// setting the nav drawer list adapter
				// adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
				// mDrawerList.setAdapter(adapter);
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
				// getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		/*
		 * if (savedInstanceState == null) { // on first time display view for first nav item displayView(0); }
		 */
		
		//footer ad
		View header = getLayoutInflater().inflate(R.layout.ad, null);
		mDrawerList.addHeaderView(header);
		AdView adView = (AdView)header.findViewById(R.id.adView);
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
		if (isClick || day == Constant.READING_DAY_MAX)
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
		if (isClick || day == 1)
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
		case R.id.btnExercises:
			mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		}
	}

}
