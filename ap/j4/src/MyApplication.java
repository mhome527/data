package sjpn4.vn;

import sjpn4.vn.Util.ULog;
import sjpn4.vn.db.DataVocabulary;
import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ULog.i(this, "onCreate() ");
		initData();
	}
	
	private void initData(){
		new DataVocabulary(this);
//		data.initData(this);
	}
}
