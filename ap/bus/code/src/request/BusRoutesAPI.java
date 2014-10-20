package app.infobus.request;

import java.util.Map;
import app.infobus.entity.DataDistance;
import app.infobus.utils.Constant;
import com.android.volley.Request;

public abstract class BusRoutesAPI extends BaseApi<DataDistance>{
	
	public BusRoutesAPI(Class<DataDistance> cls) {
		super(cls);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getUrl() {
		return Constant.URL_BUS_ROUTE;
	}

	@Override
	public int getMethod() {
		// TODO Auto-generated method stub
		return Request.Method.GET;
	}

	@Override
	public abstract Map<String, String> getParamsAPI();

}
