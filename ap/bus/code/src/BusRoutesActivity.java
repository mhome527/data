package app.infobus;

import java.util.Map;

import com.android.volley.VolleyError;

import android.os.Bundle;
import app.infobus.entity.DataDistance;
import app.infobus.request.BusRoutesAPI;
import app.infobus.utils.ULog;

public class BusRoutesActivity extends BaseActivity {

	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.map_taxi;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		test();

	}
	
	private void test(){
		new BusRoutesAPI(DataDistance.class){

			@Override
			public void onResponse(DataDistance arg0) {
				ULog.i(BusRoutesActivity.class, "onResponse");
			}

			@Override
			public Map<String, String> getParamsAPI() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				ULog.i(BusRoutesActivity.class, "onErrorResponse error:" + error.getMessage());
				
			}
			
		};
	}

}
