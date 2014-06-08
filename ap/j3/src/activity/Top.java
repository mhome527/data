package sjpn3.vn.activity;

import java.util.Locale;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import sjpn3.vn.Constant;
import sjpn3.vn.R;
import sjpn3.vn.Util.ULog;
import sjpn3.vn.db.DataVocabulary;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Top extends BaseAct implements OnClickListener {

	private LinearLayout lnProgressDialog;

	private TextView tvTitle;
	private TextView tvPart;
	private ImageView imgLanguage;
	private Button btnVocabulary;
	private Button btnReading;
	private Button btnGammer;
	private Configuration config;

	@Override
	public int getContentViewID() {
		// TODO Auto-generated method stub
		return R.layout.top_layout;
	}

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		lnProgressDialog = (LinearLayout) findViewById(R.id.content_progress_dialog);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvPart = (TextView) findViewById(R.id.tvPart);
		imgLanguage = (ImageView) findViewById(R.id.imgLanguage);
		btnVocabulary = (Button) findViewById(R.id.btnVocabulary);
		btnReading = (Button) findViewById(R.id.btnReading);
		btnGammer = (Button) findViewById(R.id.btnGammer);

		imgLanguage.setOnClickListener(this);
		btnVocabulary.setOnClickListener(this);
		btnReading.setOnClickListener(this);
		btnGammer.setOnClickListener(this);

		new LoadData().execute();
		// ///////ad
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("sony-so_04d-CB5A1KBLPT").build();
		adView.loadAd(adRequest);
		// //////////////////

		if (pref.getBooleanValue(false, Constant.LANGUAGE)){
			imgLanguage.setBackgroundResource(R.drawable.en);
			Constant.english = false;
		}
		else{
			imgLanguage.setBackgroundResource(R.drawable.vn);
			Constant.english = true;
		}
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu; this adds items to the action bar if it is present. getMenuInflater().inflate(R.menu.top,
	 * menu); return true; }
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgLanguage:
			setLocle();
			if (pref.getBooleanValue(false, Constant.LANGUAGE)){
				imgLanguage.setBackgroundResource(R.drawable.en);
				Constant.english = false;
			}
			else{
				imgLanguage.setBackgroundResource(R.drawable.vn);
				Constant.english = true;
			}
			refreshLayout();
			break;
		case R.id.btnVocabulary:
			startActivityAct(Vocabulary.class);
			// startActivityAct(Reading.class);
			break;
		case R.id.btnReading:
			// startActivityAct(Reading.class);
			startActivityAct(ReadingPager.class);
			break;
		case R.id.btnGammer:
			// startActivityAct(Vocabulary.class);
			startActivityAct(Grammar.class);
			break;

		default:
			break;
		}
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater().inflate(R.menu.main, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle item selection switch (item.getItemId()) { case R.id.action_settings: setLocle();
	 * item.setTitle(this.getResources().getString(R.string.language)); refreshLayout(); ULog.i(this, "language:" + getString(R.string.test)); return true; default: return
	 * super.onOptionsItemSelected(item); } }
	 */
	private void refreshLayout() {
		tvTitle.setText(this.getResources().getString(R.string.top_title));
		tvPart.setText(this.getResources().getString(R.string.top_part));
		btnVocabulary.setText(this.getResources().getString(R.string.vocabulary));
		btnReading.setText(this.getResources().getString(R.string.reading));
		btnGammer.setText(this.getResources().getString(R.string.grammar));
	}

	private void setLocle() {
		config = new Configuration(getResources().getConfiguration());
		ULog.i(this, "Language: " + pref.getBooleanValue(false, Constant.LANGUAGE));
		if (pref.getBooleanValue(false, Constant.LANGUAGE)) {
			config.locale = Locale.ENGLISH;
			pref.putBooleanValue(false, Constant.LANGUAGE);
			// item.setTitle("English");
		} else {
			config.locale = Locale.ITALY;
			pref.putBooleanValue(true, Constant.LANGUAGE);
			// item.setTitle("Tieng Viet");
		}

		this.getResources().updateConfiguration(config, getResources().getDisplayMetrics());
		getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
	}
	
	private class LoadData extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			lnProgressDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			DataVocabulary.getInstance(Top.this);
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			lnProgressDialog.setVisibility(View.GONE);
		}
	}
}
