package app.infobus.request;

import java.util.Map;
import android.net.Uri;
import app.infobus.MyApplication;
import app.infobus.utils.ULog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public abstract class BaseApi<T> implements Response.Listener<T> {
	public abstract String getUrl();

	public abstract int getMethod();

	public abstract Map<String, String> getParamsAPI();

	public GsonRequest<T> gsonRequest;

	private int count = 0;
	private Class<T> cls;

	public abstract void onErrorResponse(VolleyError error);

	public BaseApi(Class<T> cls) {
		this.cls = cls;
		callAPI();
	}

	public void callAPI() {
		MyApplication mInstance = MyApplication.getInstance();
		RequestQueue mRequestQueue = mInstance.getRequestQueue();
		gsonRequest = new GsonRequest<T>(getMethod(), getUrlBase(), cls, this, new VolleyErrorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return getParamsAPI();
			}
		};
		mRequestQueue.add(gsonRequest);
	}

	private class VolleyErrorListener implements Response.ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			if (count == 0) {
				count++;
				ULog.i("BaseApi-htd", "Call API again....");
				callAPI();
			} else {
				// Utils.LogE("BaseApi-htd", "error !!! count:" + count);
				BaseApi.this.onErrorResponse(error);
			}
		}
	}

	private String getUrlBase() {
		Map<String, String> map = getParamsAPI();
		if (getMethod() == Request.Method.GET) {
			Uri.Builder b = Uri.parse(getUrl()).buildUpon();
			if (map != null) {
				for (String key : map.keySet()) {
					b.appendQueryParameter(key, map.get(key));
				}
			}
			ULog.i("BaseApi-htd", "get url:" + b.build().toString());

			return b.build().toString();
		} else {
			return getUrl();
		}
	}

}
