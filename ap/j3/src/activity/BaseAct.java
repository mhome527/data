package sjpn3.vn.activity;

import sjpn3.vn.R;
import sjpn3.vn.Util.Prefs;
import sjpn3.vn.Util.ULog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public abstract class BaseAct extends Activity {
	public int widthScreen;
	public int heightScreen;
	public Prefs pref;

	abstract public int getContentViewID();

	abstract public void onAfterCreate(Bundle savedInstanceState);

	private View mainView;
	protected LinearLayout lnProgress;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ULog.i(this, "oncreate");		
		onCreateContent(savedInstanceState);

		onAfterCreate(savedInstanceState);
		
	}

	public void onCreateContent(Bundle savedInstanceState) {
		if (pref == null)
			pref = new Prefs(this);
		
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        widthScreen = dm.widthPixels;
        heightScreen = dm.heightPixels;
        
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.layout_base);
		LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		FrameLayout frmMain = (FrameLayout) findViewById(R.id.framelayout_main);
		lnProgress = (LinearLayout) findViewById(R.id.content_progress_dialog);
		lnProgress.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		int rid = getContentViewID();
		if (rid != -1) {
			mainView = inflator.inflate(rid, null, false);
			frmMain.removeAllViews();
			frmMain.addView(mainView);
		}
	}

	public void startActivityAct(Class<?> cls) {
		Intent i = new Intent(this, cls);
		startActivity(i);
	}

	
}
