package sjpn3.vn.activity;

import sjpn3.vn.R;
import sjpn3.vn.Util.BitmapUtil;
import sjpn3.vn.Util.ULog;
import sjpn3.vn.model.ReadingModel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadingContentFragment extends Fragment {
	private static String KEY_DATA = "key_data";
	private static String KEY_POS = "key_pos";
	private static String TAG = ReadingContentFragment.class.getSimpleName();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ReadingModel model;
		int pos;
		View view = inflater.inflate(R.layout.reading_page, container, false);
		model = (ReadingModel) getArguments().getSerializable(KEY_DATA);
		pos =  getArguments().getInt(KEY_POS);
		setData(view, model, pos);
		
		return view;
	}
	
	private void setData(View layout, ReadingModel model, int pos) {
		
		ULog.i(TAG, "setData");
		TextView tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
		TextView tvTitle2 = (TextView) layout.findViewById(R.id.tvTitle2);
		TextView tvNote = (TextView) layout.findViewById(R.id.tvNote);
		ImageView imgReadingS = (ImageView) layout.findViewById(R.id.imgReadingS);
		
		imgReadingS.setTag("img" + pos);
//		model = lstDay.get(pos);
		if (model != null) {
			if (model.title == null || model.title.equals(""))
				tvTitle.setVisibility(View.GONE);
			else
				tvTitle.setText(model.title);

			if (model.title2 == null || model.title2.equals(""))
				tvTitle2.setVisibility(View.GONE);
			else
				tvTitle2.setText(model.title2);
			
			if (model.img != null && !model.img.equals("")) {
				int idReading = getActivity().getResources().getIdentifier(model.img, "drawable", getActivity().getPackageName());

				imgReadingS.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource2(getActivity().getResources(), idReading, ReadingFagment.widthScreen - 20, ReadingFagment.widthScreen - 20));
			}
			tvNote.setText(model.note);
		}
	}

	public static ReadingContentFragment newInstance(ReadingModel model, int pos) {

		ReadingContentFragment f = new ReadingContentFragment();
		Bundle b = new Bundle();
		b.putSerializable(KEY_DATA, model);
		b.putInt(KEY_POS, pos);
		f.setArguments(b);

		return f;
	}

}
