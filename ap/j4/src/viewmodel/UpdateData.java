package sjpn4.vn.viewmodel;

import sjpn4.vn.db.DataVocabulary;
import android.content.Context;
import android.os.AsyncTask;

public class UpdateData extends AsyncTask<Void, Void, Integer> {

	private DataVocabulary dbData;
	private String lean;
	private String id;

	public UpdateData(Context context, String id, String lean) {
		this.lean = lean;
		this.id = id;
//		dbData = new DataVocabulary(context);
		dbData =  DataVocabulary.getInstance(context);
	}

	@Override
	protected Integer doInBackground(Void... params) {
		return dbData.updateWordLeand(id, lean);
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		/*ULog.i(this, "onPostExecute() result:" + result + "; lean:" + lean);
		arrWord = dbData.getWordByLean(lean);
		adapter.clear();
		adapter.addAll(arrWord);
		adapter.notifyDataSetChanged();*/
	}

}
