package app.infobus;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import app.infobus.utils.Constant;

public class InfoDetailActivity extends AbstractActivity {

	TextView tvStart, tvBack, tvInfon, tvNum, tvNamePath;
	String num, namePath, startPath, backPath, info;

	@Override
	protected int getViewLayoutId() {
		return R.layout.info_detail;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		try {

			tvStart = (TextView) findViewById(R.id.tvStart);
			tvBack = (TextView) findViewById(R.id.tvBack);
			tvInfon = (TextView) findViewById(R.id.tvInfo);
			tvNamePath = (TextView) findViewById(R.id.tvNamePath);
			tvNum = (TextView) findViewById(R.id.tvNum);

			boolean isCheck = getIntent().getBooleanExtra(Constant.KEY_CITY, true);
			if(isCheck)
				rbtnHcm.setChecked(true);
			else
				rbtnHN.setChecked(true);
			
			// ///////ad
//			AdView adView = (AdView) this.findViewById(R.id.adView);
//			AdRequest adRequest = new AdRequest.Builder().build();
//			adView.loadAd(adRequest);
			// //////////////////
			getIntentData();
			setData();
		}
		catch (Exception e) {
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
	}

	public void setData() {
		tvNum.setText(num);
		tvNamePath.setText(namePath);
		tvStart.setText(startPath);
		tvBack.setText(backPath);
		tvInfon.setText(Html.fromHtml(info));
	}
}
