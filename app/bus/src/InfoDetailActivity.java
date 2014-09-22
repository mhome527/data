package app.infobus;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.infobus.utils.Constant;
import app.infobus.utils.Utility;

public class InfoDetailActivity extends BaseActivity {

	private ImageView imgHome;
	private LinearLayout lnSearch;

	private boolean isHCM = true;
	private TextView tvStart, tvBack, tvInfo, tvNum, tvNamePath, tvCity;
	private String num, namePath, startPath, backPath, info;
	private boolean isClick = false;
	@Override
	protected int getViewLayoutId() {
		return R.layout.info_detail_layout;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		try {

			tvStart = (TextView) findViewById(R.id.tvStart);
			tvBack = (TextView) findViewById(R.id.tvBack);
			tvInfo = (TextView) findViewById(R.id.tvInfo);
			tvNamePath = (TextView) findViewById(R.id.tvNamePath);
			tvNum = (TextView) findViewById(R.id.tvNum);
			tvCity = (TextView) findViewById(R.id.tvCity);
			imgHome = (ImageView) findViewById(R.id.imgHome);
			lnSearch = (LinearLayout) findViewById(R.id.search);


			getIntentData();
			setData();

			imgHome.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (isClick)
						return;
					isClick = true;
//					Intent i = new Intent(InfoDetailActivity.this, MainFragment.class);
//					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(i);
					finish();
				}
			});
			
			// search
			lnSearch.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isClick)
						return;
					isClick = true;
					Intent i = new Intent(InfoDetailActivity.this, SearchActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra(Constant.KEY_CITY, isHCM);
					startActivity(i);
					finish();
				}
			});

			
			// GA
			if (isHCM) {
				tvCity.setText(getString(R.string.HCM));
				Utility.setScreenNameGA("InfoDetailActivity Ho Chi Minh");
				Utility.setEventGA( Constant.GA_HCM + "-Detail", num);
			} else {
				tvCity.setText(getString(R.string.HN));
				Utility.setScreenNameGA("InfoDetailActivity Ha Noi");
				Utility.setEventGA(Constant.GA_HN + "-Detail", num);
			}

			// ///////ad
			AdView adView = (AdView) this.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
			// //////////////////
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getIntentData() {

		Intent intent = getIntent();
		num = intent.getStringExtra("num");
		namePath = intent.getStringExtra("namePath");
		startPath = intent.getStringExtra("start");
		backPath = intent.getStringExtra("back");
		info = intent.getStringExtra("info");
		isHCM = intent.getBooleanExtra(Constant.KEY_CITY, true);
	}

	public void setData() {
		tvNum.setText(num);
		tvNamePath.setText(namePath);
		tvStart.setText(startPath);
		tvBack.setText(backPath);
		tvInfo.setText(Html.fromHtml(info));
	}
}
