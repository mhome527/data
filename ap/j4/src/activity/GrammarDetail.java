package sjpn4.vn.activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import sjpn4.vn.Constant;
import sjpn4.vn.R;
import android.os.Bundle;
import android.webkit.WebView;

public class GrammarDetail extends BaseAct {

	private WebView web;

	@Override
	public int getContentViewID() {
		return R.layout.grammar_detail_layout;
	}

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		String data;
		web = (WebView) findViewById(R.id.webGrammar);
		if (getIntent().hasExtra(Constant.GRAMMAR_DATA)) {
			data = getIntent().getStringExtra(Constant.GRAMMAR_DATA);
			web.loadData(data, "text/html; charset=utf-8", "utf-8");
		}

		// ///////ad
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("sony-so_04d-CB5A1KBLPT").build();
		adView.loadAd(adRequest);
		// //////////////////
	}

}
