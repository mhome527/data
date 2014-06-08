package sjpn4.vn.activity;

import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import sjpn4.vn.Constant;
import sjpn4.vn.R;
import sjpn4.vn.Util.Common;
import sjpn4.vn.Util.ULog;
import sjpn4.vn.adapter.GrammarAdapter;
import sjpn4.vn.model.GrammarModel;
import sjpn4.vn.model.subModel.GrammarList;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Grammar extends BaseAct {

	private ListView lvGrammar;
	private List<GrammarList> arrGrammer;

	@Override
	public int getContentViewID() {
		// TODO Auto-generated method stub
		return R.layout.grammar_layout;
	}

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {

		try {
			init();
			GrammarAdapter adapter = new GrammarAdapter(this, arrGrammer);
			lvGrammar.setAdapter(adapter);

			lvGrammar.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
					ULog.i(this, "onItemClick() position");
					Intent i = new Intent(Grammar.this, GrammarDetail.class);
					i.putExtra(Constant.GRAMMAR_DATA, arrGrammer.get(position).detail);
					startActivity(i);
				}
			});

		} catch (Exception e) {
			ULog.e(this, "onAfterCreate() Error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void init() {
		GrammarModel model;
		lvGrammar = (ListView) findViewById(R.id.lvGrammar);
		model = (GrammarModel) Common.getObjectJson(this, GrammarModel.class, "grammar");
		arrGrammer = model.listGrammar;

		// ///////ad
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("sony-so_04d-CB5A1KBLPT").build();
		adView.loadAd(adRequest);
		// //////////////////
	}
}
