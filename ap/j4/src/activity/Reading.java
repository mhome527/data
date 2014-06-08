package sjpn4.vn.activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import sjpn4.vn.Constant;
import sjpn4.vn.R;
import sjpn4.vn.Util.BitmapUtil;
import sjpn4.vn.Util.Common;
import sjpn4.vn.Util.ULog;
import sjpn4.vn.slidingmenu.adapter.SlideMenuListAdapter;
import sjpn4.vn.view.ScaleImageView;
import sjpn4.vn.model.ReadingModel;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Reading extends BaseAct implements OnClickListener {

	private LinearLayout lnProgressDialog;
	private TextView tvDay;
	private TextView tvReading;
	private TextView tvNote;
	private TextView tvTitle;
	private TextView tvTitle2;

	private ScaleImageView imgReading;
	private ImageView imgReadingS;
	private ImageView imgLeft;
	private ImageView imgRight;
	private ImageView imgClose;
	private RelativeLayout rlReading;
	private Button btnShow;
	private Button btnExercises;
	private int idReading = -1;
	private boolean isClick = false;

//	private Bitmap bmp = null;
	// private WebView webReading;
	private int day = 1;

	private ReadingModel model;

	// //slide menu
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// slide menu items
	// private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	// private NavDrawerListAdapter adapter;
	private SlideMenuListAdapter adapterMenu;

	// /////

	private boolean isShow = false;

	@Override
	public int getContentViewID() {
		// TODO Auto-generated method stub
		return R.layout.reading_layout;
	}

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		imgLeft = (ImageView) findViewById(R.id.imgLeft);
		imgRight = (ImageView) findViewById(R.id.imgRight);
		tvDay = (TextView) findViewById(R.id.tvDay);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle2 = (TextView) findViewById(R.id.tvTitle2);
		tvReading = (TextView) findViewById(R.id.tvReading);
		tvNote = (TextView) findViewById(R.id.tvNote);
		imgReading = (ScaleImageView) findViewById(R.id.imgReading);
		imgReadingS = (ImageView) findViewById(R.id.imgReadingS);
		rlReading = (RelativeLayout) findViewById(R.id.rlReading);
		imgClose = (ImageView) findViewById(R.id.imgClose);
		btnShow = (Button) findViewById(R.id.btnShow);
		lnProgressDialog = (LinearLayout) findViewById(R.id.content_progress_dialog);

		/*
		 * webReading = (WebView) findViewById(R.id.webReading);
		 * 
		 * webReading.getSettings().setJavaScriptEnabled(true); webReading.getSettings().setSupportZoom(true); webReading.getSettings().setBuiltInZoomControls(true);
		 * webReading.getSettings().setLoadWithOverviewMode(true);
		 * 
		 * webReading.getSettings().setPluginsEnabled(true);
		 */

		btnExercises = (Button) findViewById(R.id.btnExercises);
		btnExercises.setOnClickListener(this);
		imgLeft.setOnClickListener(Reading.this);
		imgRight.setOnClickListener(Reading.this);
		imgReadingS.setOnClickListener(Reading.this);
		imgClose.setOnClickListener(Reading.this);
		btnShow.setOnClickListener(Reading.this);

		ULog.i(this, "=========== WidthScreen" + widthScreen +"; heigth:" + heightScreen);
//		day = pref.getIntValue(1, Constant.DAY_READING_LEARN);
		if (day == 1)
			imgLeft.setVisibility(View.INVISIBLE);
		tvDay.setText(Reading.this.getString(R.string.level) + " " + day);
		new LoadData().execute();
		// initData();
		createLayoutSlideMenu();

		// ///////ad
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("sony-so_04d-CB5A1KBLPT").build();
		adView.loadAd(adRequest);
		// //////////////////
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
		case R.id.imgReadingS:
			if (isClick)
				return;
			isClick = true;
			isShow = true;
			rlReading.setVisibility(View.VISIBLE);
			// imgReading.setImageResource(idReading);
			imgReading.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(getResources(), idReading, widthScreen - 10, widthScreen - 10));
			imgReadingS.setImageBitmap(null);
			break;
		case R.id.imgClose:
			isClick = false;
			isShow = false;
			rlReading.setVisibility(View.GONE);
			// imgReading.setImageResource(0);
			// imgReadingS.setImageResource(idReading);
			imgReading.setImageBitmap(null);
			imgReadingS.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(getResources(), idReading, widthScreen - 10, widthScreen - 10));

			break;
		case R.id.btnShow:
			isShow = true;
			// imgReading.setImageResource(idReading);
			// imgReadingS.setImageResource(0);
			imgReading.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(getResources(), idReading, widthScreen - 10, widthScreen - 10));
			imgReadingS.setImageBitmap(null);

			rlReading.setVisibility(View.VISIBLE);
			break;
		case R.id.btnExercises:
			mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		isClick = false;
		if (isShow) {
			isShow = false;
			rlReading.setVisibility(View.GONE);
			// imgReading.setImageResource(0);
			// imgReadingS.setImageResource(idReading);
			imgReading.setImageBitmap(null);
			imgReadingS.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(getResources(), idReading, widthScreen - 10, widthScreen - 10));
			return;
		}
		/*
		 * if (isMenu) { isMenu = false; mDrawerLayout.closeDrawers(); return; }
		 */
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isClick = false;
	}

	private class LoadData extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			lnProgressDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			ULog.i(this, "load day:" + day);
			model = (ReadingModel) Common.getObjectJson(Reading.this, ReadingModel.class, "day" + day);
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
//			Bitmap bmp;
			super.onPostExecute(result);
			isClick = false;
			lnProgressDialog.setVisibility(View.GONE);

			if (model.img != null && !model.img.equals("")) {

				idReading = getResources().getIdentifier(model.img, "drawable", getPackageName());
//				System.gc();
//				bmp = BitmapUtil.decodeSampledBitmapFromResource2(getResources(), idReading, widthScreen - 10, widthScreen - 10);
//				ULog.i(this, "bitmap ===========size:" + bmp.getByteCount()/1024);
				// imgReading.setVisibility(View.VISIBLE);
				// imgReading.setImageResource(idReading);
				System.gc();
				imgReadingS.setVisibility(View.VISIBLE);
				System.gc();
				// imgReadingS.setImageResource(idReading);
				imgReadingS.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(getResources(), idReading, widthScreen - 10, widthScreen - 10));

				tvReading.setVisibility(View.GONE);

				/*
				 * } else { tvReading.setText(model.reading); imgReading.setVisibility(View.GONE); tvReading.setVisibility(View.VISIBLE);
				 */

			}
			tvTitle.setText(model.title);
			tvTitle2.setText(model.title2);
			tvNote.setText(model.note);

			// slide menu
			adapterMenu = new SlideMenuListAdapter(Reading.this, model.readingList);
			// setting the nav drawer list adapter
			// adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
			// mDrawerList.setAdapter(adapter);
			mDrawerList.setAdapter(adapterMenu);
		}
	}

	// //////////////////////////////
	// /////////////////////////////
	private void createLayoutSlideMenu() {
		// ArrayList<String> arrText = new ArrayList<String>();
		// mTitle = mDrawerTitle = getTitle();
		// mTitle = misClickDrawerTitle = "DAY " + day;

		// load slide menu items
		// navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		/*
		 * adapterMenu = new SlideMenuListAdapter(this, model.readingList); // setting the nav drawer list adapter // adapter = new NavDrawerListAdapter(getApplicationContext(),
		 * navDrawerItems); // mDrawerList.setAdapter(adapter); mDrawerList.setAdapter(adapterMenu);
		 */

		// enabling action bar app icon and behaving it as toggle button
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);

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
		if (isClick || day == Constant.WORD_DAY_MAX)
			return;

		isClick = true;
		day = day + 1;
		tvDay.setText(Reading.this.getString(R.string.level) + " " + day);
		if (day == Constant.WORD_DAY_MAX)
			imgRight.setVisibility(View.GONE);
		else
			imgRight.setVisibility(View.VISIBLE);
		pref.putIntValue(day, Constant.DAY_READING_LEARN);
		imgLeft.setVisibility(View.VISIBLE);

		// initData();
		// adapterMenu = new SlideMenuListAdapter(this, model.readingList);
		// mDrawerList.setAdapter(adapterMenu);
		new LoadData().execute();
	}

	private void preReading() {
		if (isClick || day == 1)
			return;
		isClick = true;
		day = day - 1;
		tvDay.setText(Reading.this.getString(R.string.level) + " " + day);
		if (day == 1)
			imgLeft.setVisibility(View.GONE);
		else
			imgLeft.setVisibility(View.VISIBLE);
		pref.putIntValue(day, Constant.DAY_READING_LEARN);
		imgRight.setVisibility(View.VISIBLE);
		// initData();
		// adapterMenu = new SlideMenuListAdapter(this, model.readingList);
		// mDrawerList.setAdapter(adapterMenu);
		new LoadData().execute();
	}

}
