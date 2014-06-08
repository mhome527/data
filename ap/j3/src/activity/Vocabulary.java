package sjpn3.vn.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import sjpn3.vn.Constant;
import sjpn3.vn.R;
import sjpn3.vn.Util.ULog;
import sjpn3.vn.action.SwipeDismissListViewTouchListener;
import sjpn3.vn.adapter.WordAdapter;
import sjpn3.vn.db.DataVocabulary;
import sjpn3.vn.model.subModel.VocabularyList;
import sjpn3.vn.viewmodel.UpdateData;
import android.content.Context;
//import sjpn3.vn.viewmodel.UpdateData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Vocabulary extends BaseAct implements OnClickListener {

	private LinearLayout lnProgressDialog;
	private TextView tvDay;
	private ImageView imgRight;
	private ImageView imgLeft;

	private ListView lvWordNew;
	private ListView lvWordOld;
	private Button btnNew;
	private Button btnOld;
	private ArrayList<VocabularyList> arrWordNew;
	private ArrayList<VocabularyList> arrWordOld;
	private DataVocabulary dbData;
	private WordAdapter adapterNew;
	private WordAdapter adapterOld;
	private boolean isClickNew = true;
	private boolean isClickOld = false;
	private int day = 1;

	// private boolean dataChange = false;

	@Override
	public int getContentViewID() {
		return R.layout.vocabulary_layout;
	}

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		tvDay = (TextView) findViewById(R.id.tvDay);
		imgRight = (ImageView) findViewById(R.id.imgRight);
		imgLeft = (ImageView) findViewById(R.id.imgLeft);
		lvWordNew = (ListView) findViewById(R.id.lvWordNew);
		lvWordOld = (ListView) findViewById(R.id.lvWordOld);
		btnNew = (Button) findViewById(R.id.btnNew);
		btnOld = (Button) findViewById(R.id.btnOld);
		lnProgressDialog = (LinearLayout) findViewById(R.id.content_progress_dialog);
		// setInitData();
		imgRight.setOnClickListener(this);
		imgLeft.setOnClickListener(this);
		btnNew.setOnClickListener(this);
		btnOld.setOnClickListener(this);
		day = pref.getIntValue(1, Constant.DAY_WORD_LEARN);

		if (day == 1)
			imgLeft.setVisibility(View.INVISIBLE);
		tvDay.setText(this.getString(R.string.level) + " " + day);

		// ///////ad
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("sony-so_04d-CB5A1KBLPT").build();
		adView.loadAd(adRequest);
		// //////////////////

		new LoadinitData().execute();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNew:
			if (isClickNew)
				return;
			isClickNew = true;
			isClickOld = false;
			lvWordNew.setVisibility(View.VISIBLE);
			lvWordOld.setVisibility(View.GONE);
			btnNew.setBackgroundResource(R.drawable.tab_b_selected);
			btnOld.setBackgroundResource(R.drawable.tab_button);
			// adapterNew.notifyDataSetChanged();
			// ULog.i(this, "onClick() datachange1:" + dataChange);
			// if (dataChange) {
			new LoadData("0").execute();
			// dataChange = false;
			// }
			break;
		case R.id.btnOld:
			if (isClickOld)
				return;
			isClickNew = false;
			isClickOld = true;

			lvWordNew.setVisibility(View.GONE);
			lvWordOld.setVisibility(View.VISIBLE);

			btnNew.setBackgroundResource(R.drawable.tab_button);
			btnOld.setBackgroundResource(R.drawable.tab_b_selected);
			// adapterOld.notifyDataSetChanged();
			lvWordOld.invalidateViews();
			// lvWordOld.refreshDrawableState();
			// ULog.i(this, "onClick() datachange2:" + dataChange);
			// if (dataChange) {
			new LoadData("1").execute();
			// dataChange = false;
			// }
			break;
		case R.id.imgRight:
			day++;
			if (day >= Constant.WORD_DAY_MAX) {
				imgRight.setVisibility(View.INVISIBLE);
			}
			pref.putIntValue(day, Constant.DAY_WORD_LEARN);
			imgLeft.setVisibility(View.VISIBLE);
			tvDay.setText(this.getString(R.string.level) + " " + day);
			new LoadData("0").execute();

			break;

		case R.id.imgLeft:
			day--;
			if (day == 1) {
				imgLeft.setVisibility(View.INVISIBLE);
			}
			pref.putIntValue(day, Constant.DAY_WORD_LEARN);
			tvDay.setText(this.getString(R.string.level) + " " + day);
			imgRight.setVisibility(View.VISIBLE);
			new LoadData("0").execute();
			break;
		default:
			break;
		}
	}

	private void setInitData() {
		btnNew.setBackgroundResource(R.drawable.tab_b_selected);
		try {
			/*
			 * dbData = new DataVocabulary(this); arrWordNew = dbData.getWordByLean("0");
			 */
			if (arrWordNew == null || arrWordNew.size() == 0) {
				ULog.e(this, "setInitData() can't get data!!!!!!!!!!!!!");
				return;
			}
			adapterNew = new WordAdapter(this, R.layout.word_item, arrWordNew, false);

			// //////list new
			// Create a ListView-specific touch listener. ListViews are given special treatment because
			// by default they handle touches for their list items... i.e. they're in charge of drawing
			// the pressed state (the list selector), handling list item clicks, etc.
			SwipeDismissListViewTouchListener touchListenerNew = new SwipeDismissListViewTouchListener(lvWordNew, new SwipeDismissListViewTouchListener.DismissCallbacks() {
				@Override
				public boolean canDismiss(int pos) {
					ULog.i(Vocabulary.this, "canDismiss() new");
					return true;
				}

				@Override
				public void onDismiss(ListView listView, int[] reverseSortedPositions) {
					// Log.i(TAG, "onDismiss() ");
					try {
						for (int position : reverseSortedPositions) {
							if (position >= adapterNew.getCount())
								return;
							VocabularyList entry = adapterNew.getItem(position);
							arrWordNew.remove(entry);
							adapterNew.remove(entry);
							// dataChange = true;
							// dbData.updateWordLeand(entry.ID, "1");
							new UpdateData(Vocabulary.this, entry.ID, "1").execute();
							// new UpdateData(Vocabulary.this, entry.ID, "1", adapterNew).execute();
							// ULog.i(this, "onDismiss() position:" + position + "; jp:" + entry.wordJP);
						}
						adapterNew.notifyDataSetChanged();
					} catch (Exception e) {
						ULog.e(this, "Dismiss error:" + e.getMessage());
					}
				}

				@Override
				public void onClickItem(View v, int position) {
					View change;
					String tag = "";
					change = v.findViewById(R.id.llWord);
					ULog.i(Vocabulary.this, "onClickItem() new llWord:" + R.id.llWord + "; tag:" + change.getTag());

					if (change.getTag() != null)
						tag = change.getTag().toString();

					if (change.getId() == R.id.llWord && tag != null && tag.equals("1")) {
						change.setTag("");
						change.setVisibility(View.GONE);
						v.findViewById(R.id.rlMean).setVisibility(View.VISIBLE);
						arrWordNew.get(position).wordView = "1";
						new UpdateView(Vocabulary.this, arrWordNew.get(position).ID, "1").execute();
						return;
					}

					change = v.findViewById(R.id.tvJP2);
					tag = "";
					if (change.getTag() != null)
						tag = change.getTag().toString();
					ULog.i(Vocabulary.this, "onClickItem() new tvJP2:" + R.id.tvJP2 + "; tag:" + change.getTag());
					if (change.getId() == R.id.tvJP2 && tag != null && tag.equals("1")) {
						change.setTag("");
						v.findViewById(R.id.rlMean).setVisibility(View.GONE);
						v.findViewById(R.id.llWord).setVisibility(View.VISIBLE);
						arrWordNew.get(position).wordView = "0";
						new UpdateView(Vocabulary.this, arrWordNew.get(position).ID, "0").execute();
						return;
					}

					change = v.findViewById(R.id.imgbRedo);
					tag = "";
					if (change.getTag() != null)
						tag = change.getTag().toString();
					ULog.i(Vocabulary.this, "onClickItem() new imgbRedo:" + R.id.imgbRedo + "; tag:" + change.getTag());
					if (change.getId() == R.id.imgbRedo && tag != null && tag.equals("1")) {
						change.setTag("");
						v.findViewById(R.id.rlMean).setVisibility(View.GONE);
						v.findViewById(R.id.llWord).setVisibility(View.VISIBLE);
						arrWordNew.get(position).wordView = "0";
						new UpdateView(Vocabulary.this, arrWordNew.get(position).ID, "0").execute();
						return;
					}
				}
			});

			lvWordNew.setAdapter(adapterNew);
			lvWordNew.setOnTouchListener(touchListenerNew);

			// /////List OLd

			if (arrWordNew == null || arrWordNew.size() == 0) {
				ULog.e(Vocabulary.this, "setInitData() can't get data!!!!!!!!!!!!!");
				return;
			}
			adapterOld = new WordAdapter(this, R.layout.word_item, arrWordOld, true);
			SwipeDismissListViewTouchListener touchListenerOld = new SwipeDismissListViewTouchListener(lvWordOld, new SwipeDismissListViewTouchListener.DismissCallbacks() {
				@Override
				public boolean canDismiss(int pos) {
					ULog.i(Vocabulary.this, "canDismiss() old");

					return true;
				}

				@Override
				public void onDismiss(ListView listView, int[] reverseSortedPositions) {
					try {
						for (int position : reverseSortedPositions) {
							if (position >= adapterOld.getCount())
								return;
							VocabularyList entry = adapterOld.getItem(position);
							arrWordOld.remove(entry);
							adapterOld.remove(entry);
							new UpdateData(Vocabulary.this, entry.ID, "0").execute();
							// dataChange = true;
							// new UpdateData(Vocabulary.this, entry.ID, "0", adapterOld).execute();
							// ULog.i(this, "onDismiss() old position:" + position + "; jp:" + entry.wordJP);
						}

						adapterOld.notifyDataSetChanged();
					} catch (Exception e) {
						ULog.e(Vocabulary.this, "Dismiss 2 error:" + e.getMessage());
					}
				}

				@Override
				public void onClickItem(View v, int position) {
					View change;
					String tag = "";
					ULog.i(this, "onClickItem() old");
					change = v.findViewById(R.id.llWord);
					if (change.getTag() != null)
						tag = change.getTag().toString();

					if (tag != null && tag.equals("1")) {
						change.setTag("");
						change.setVisibility(View.GONE);
						v.findViewById(R.id.rlMean).setVisibility(View.VISIBLE);
						arrWordOld.get(position).wordView = "1";
						new UpdateView(Vocabulary.this, arrWordOld.get(position).ID, "1").execute();
						return;
					}

					change = v.findViewById(R.id.tvJP2);
					tag = "";
					if (change.getTag() != null)
						tag = change.getTag().toString();

					if (tag != null && tag.equals("1")) {
						change.setTag("");
						v.findViewById(R.id.rlMean).setVisibility(View.GONE);
						v.findViewById(R.id.llWord).setVisibility(View.VISIBLE);
						arrWordOld.get(position).wordView = "0";
						new UpdateView(Vocabulary.this, arrWordOld.get(position).ID, "0").execute();
						return;
					}

					change = v.findViewById(R.id.imgbRedo);
					tag = "";
					if (change.getTag() != null)
						tag = change.getTag().toString();

					if (change.getId() == R.id.imgbRedo && tag != null && tag.equals("1")) {
						change.setTag("");
						v.findViewById(R.id.rlMean).setVisibility(View.GONE);
						v.findViewById(R.id.llWord).setVisibility(View.VISIBLE);
						arrWordNew.get(position).wordView = "0";
						new UpdateView(Vocabulary.this, arrWordOld.get(position).ID, "0").execute();
						return;
					}
				}
			});

			lvWordOld.setAdapter(adapterOld);
			lvWordOld.setOnTouchListener(touchListenerOld);

		} catch (Exception e) {
			ULog.e(this, "setInitData() Error:" + e.getMessage());
		}
	}

	public class LoadinitData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			lnProgressDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
//			dbData = new DataVocabulary(Vocabulary.this);
			dbData =  DataVocabulary.getInstance(Vocabulary.this);

			arrWordNew = dbData.getWordByLeanDay("0", day + "");
			arrWordOld = dbData.getWordByLeanDay("1", day + "");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			lnProgressDialog.setVisibility(View.GONE);
			setInitData();
		}

	}

	// /////////////
	public class LoadData extends AsyncTask<Void, Void, ArrayList<VocabularyList>> {

		private String lean;

		public LoadData(String lean) {
			this.lean = lean;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			lnProgressDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected ArrayList<VocabularyList> doInBackground(Void... params) {
			return dbData.getWordByLeanDay(lean, day + "");
		}

		@Override
		protected void onPostExecute(ArrayList<VocabularyList> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ULog.i(this, "onPostExecute() lean:" + lean);
			lnProgressDialog.setVisibility(View.GONE);
			if (result != null && result.size() > 0) {
				if (lean.equals("1")) {
					arrWordOld = result;
					adapterOld.clear();
					adapterOld.addAll(arrWordOld);
					adapterOld.notifyDataSetChanged();
				} else {
					arrWordNew = result;
					adapterNew.clear();
					adapterNew.addAll(arrWordNew);
					adapterNew.notifyDataSetChanged();

				}
			}
		}
	}

	private class UpdateView extends AsyncTask<Void, Void, Integer> {

		private DataVocabulary dbData;
		private String lean;
		private String id;

		public UpdateView(Context context, String id, String lean) {
			this.lean = lean;
			this.id = id;
//			dbData = new DataVocabulary(context);
			dbData =  DataVocabulary.getInstance(Vocabulary.this);

		}

		@Override
		protected Integer doInBackground(Void... params) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(dbData.WORD_VIEW, lean);
			return dbData.updateWordById(map, id);
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ULog.i(Vocabulary.this, "result update:" + result);

		}

	}
}
