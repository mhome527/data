package sjpn4.vn.adapter;

import java.util.List;
import sjpn4.vn.Constant;
import sjpn4.vn.R;
import sjpn4.vn.Util.ULog;
import sjpn4.vn.model.subModel.VocabularyList;
import sjpn4.vn.viewmodel.UpdateData;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WordAdapter extends ArrayAdapter<VocabularyList> implements OnTouchListener {
	private List<VocabularyList> arrWord;
	private Context context;
	private int layoutResourceId;
	private boolean history = false;

	/*
	 * public WordAdapter(Context context, int layoutResourceId, List<VocabularyList> arrWord) { super(context, layoutResourceId, arrWord); this.arrWord = arrWord; this.context =
	 * context; this.layoutResourceId = layoutResourceId; }
	 */

	public WordAdapter(Context context, int layoutResourceId, List<VocabularyList> arrWord, boolean history) {
		super(context, layoutResourceId, arrWord);
		this.arrWord = arrWord;
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.history = history;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		WordHolder wordHolder;
		VocabularyList entry;
		final View row;
		// ULog.i(this, "getView() ");
		entry = arrWord.get(position);
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			wordHolder = new WordHolder();
			wordHolder.tvJP1 = (TextView) row.findViewById(R.id.tvJP1);
			wordHolder.tvJP2 = (TextView) row.findViewById(R.id.tvJP2);
			wordHolder.tvHiragana = (TextView) row.findViewById(R.id.tvHiragana);
			wordHolder.tvMean = (TextView) row.findViewById(R.id.tvMean);
			wordHolder.tvEx = (TextView) row.findViewById(R.id.tvEx);
			wordHolder.tvViet = (TextView) row.findViewById(R.id.tvViet);
			wordHolder.llWord = (LinearLayout) row.findViewById(R.id.llWord);
		
			wordHolder.llWord.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
						ULog.i(WordAdapter.this, "onTouch()1 item action:" + event.getAction() + ";llWord id:" + v.getId());

						v.setTag("1");
//						if(event.getAction()==1)
//							return false;
//						new UpdateView(context, arrWord.get(position).ID, "1").execute();
						return false;
					
				}
			});
//
			wordHolder.tvJP2.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
						ULog.i(WordAdapter.this, "onTouch()2 ==>UpdateView; item action:" + event.getAction() + "; id:" + arrWord.get(position).ID);
						v.setTag("1");
//						new UpdateView(context, arrWord.get(position).ID, "0").execute();
						
						return false;
				}
			});

		
			
			///////////////////
			ImageButton imgbRedo = (ImageButton) row.findViewById(R.id.imgbRedo);
			imgbRedo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setTag("1");
//					row.findViewById(R.id.rlMean).setVisibility(View.GONE);
//					row.findViewById(R.id.llWord).setVisibility(View.VISIBLE);
//					// arrWord.get(position).wordView = "0";
//					new UpdateView(context, arrWord.get(position).ID, "0");
				}
			});

			ImageView imgRemove = (ImageView) row.findViewById(R.id.imgRemove);
			imgRemove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Animation anim = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
					anim.setDuration(500);
					row.startAnimation(anim);

					new Handler().postDelayed(new Runnable() {

						public void run() {
							ULog.i(WordAdapter.this, "run() pos:" + position + "; arr size:" + arrWord.size() + ", history:" + history);
							VocabularyList entry = arrWord.get(position);
							arrWord.remove(position);

							if (history)
								new UpdateData(context, entry.ID, "0").execute();
							else
								new UpdateData(context, entry.ID, "1").execute();

							WordAdapter.this.notifyDataSetChanged();
						}

					}, anim.getDuration());

				}
			});

			row.setTag(wordHolder);
		} else {
			row = convertView;
			wordHolder = (WordHolder) row.getTag();
		}

		if (entry.wordView != null && entry.wordView.equals("1")) {
			row.findViewById(R.id.llWord).setVisibility(View.GONE);
			row.findViewById(R.id.rlMean).setVisibility(View.VISIBLE);
		} else {
			row.findViewById(R.id.llWord).setVisibility(View.VISIBLE);
			row.findViewById(R.id.rlMean).setVisibility(View.GONE);
		}
		// //////////// test
		if (position == 5) {
			ULog.i(this, "getView() " + entry.wordJP + ", hira:" + entry.wordHiragana);
		}
		// /////////

		if (entry.wordJP != null && !entry.wordJP.equals("")) {
			wordHolder.tvJP1.setText(entry.wordJP);
			wordHolder.tvJP2.setText(entry.wordJP);
		} else {
			wordHolder.tvJP1.setText(entry.wordHiragana);
			wordHolder.tvJP2.setText(entry.wordHiragana);
		}
		wordHolder.tvHiragana.setText(entry.wordHiragana);
		wordHolder.tvEx.setText(entry.wordEX);
		if (Constant.english) {
			wordHolder.tvMean.setText(entry.wordEN);
			wordHolder.tvViet.setVisibility(View.GONE);
		} else {
			wordHolder.tvMean.setText(entry.wordVN);
			wordHolder.tvViet.setVisibility(View.VISIBLE);
			wordHolder.tvViet.setText(entry.wordViet);
		}

		return row;
	}

	static class WordHolder {
		TextView tvJP1;
		TextView tvJP2;
		TextView tvHiragana;
		TextView tvEx;
		TextView tvMean;
		TextView tvViet;
		ImageView imgRemove;
		LinearLayout llWord;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		ULog.i(WordAdapter.this, "onTouch() ");
		return true;
	}

}
