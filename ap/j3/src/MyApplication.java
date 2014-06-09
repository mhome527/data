package sjpn3.vn;

import sjpn3.vn.Util.ULog;
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
//		new DataVocabulary(this);
//		data.initData(this);
	}
}
