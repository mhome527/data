package sjpn4.vn.activity;

import sjpn4.vn.R;
import sjpn4.vn.model.ReadingModel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReadingContentFragment extends Fragment {
	private static String KEY_DATA = "key_data";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ReadingModel model;
		View view = inflater.inflate(R.layout.reading_page, container, false);
		model = (ReadingModel) getArguments().getSerializable(KEY_DATA);
		setData(view, model);
		
		return view;
	}
	
	private void setData(View view, ReadingModel model) {
		
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		TextView tvTitle2 = (TextView) view.findViewById(R.id.tvTitle2);
		TextView tvNote = (TextView) view.findViewById(R.id.tvNote);
		TextView tvReading = (TextView) view.findViewById(R.id.tvReading);

		
		if (model != null) {
			if (model.title == null || model.title.equals(""))
				tvTitle.setVisibility(View.GONE);
			else
				tvTitle.setText(model.title);

			if (model.title2 == null || model.title2.equals(""))
				tvTitle2.setVisibility(View.GONE);
			else
				tvTitle2.setText(model.title2);

//			webReading.loadData(model.reading, "text/html; charset=utf-8", "utf-8");
//			webReading.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			tvReading.setText(Html.fromHtml(model.reading));
			tvNote.setText(model.note);

		}
	}

	public static ReadingContentFragment newInstance(ReadingModel model) {

		ReadingContentFragment f = new ReadingContentFragment();
		Bundle b = new Bundle();
		b.putSerializable(KEY_DATA, model);

		f.setArguments(b);

		return f;
	}

}
