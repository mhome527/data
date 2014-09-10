package app.infobus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import app.infobus.utils.Constant;
import app.infobus.utils.Prefs;
import app.infobus.utils.ULog;

public abstract class AbstractActivity extends Activity {
	private String tag = AbstractActivity.class.getSimpleName();
	private LinearLayout lnSearch;
	private ImageView imgHome;
	private boolean isClick = false;
	private Activity mActivity;
	public RadioButton rbtnHcm;
	public RadioButton rbtnHN;
    public static Prefs pref;


	@Override
	protected final void onCreate(final Bundle savedInstanceState) {
		// ShowLog.showLogDebug(""+tag, "<< onCreate "
		// + savedInstanceState + " "+ getClass().getSimpleName());
		View mainView;

		super.onCreate(savedInstanceState);
		try {
			ULog.i(tag, "======class: " + this.getClass().getSimpleName());
			mActivity = this;
			if (pref == null)
	            pref = new Prefs(this);
			// //////////
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.layout_base);
			LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			FrameLayout frmMain = (FrameLayout) findViewById(R.id.framelayout_main);

			int rid = getViewLayoutId();
			if (rid != -1) {
				mainView = inflator.inflate(rid, null, false);
				frmMain.removeAllViews();
				frmMain.addView(mainView);
			}
			
			initView();
			initView(savedInstanceState);

			setAction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ////////////////////////

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isClick = false;
	}

	private void initView() {
		rbtnHN = (RadioButton) findViewById(R.id.rbtnHN);
		rbtnHcm = (RadioButton) findViewById(R.id.rbtnHcm);
		imgHome = (ImageView) findViewById(R.id.imgHome);
	}

	private void setAction() {

		if(mActivity.getClass().isAssignableFrom(SearchActivity.class)
				|| mActivity.getClass().isAssignableFrom(InfoDetailActivity.class)){
			RadioGroup groupRadio = (RadioGroup) findViewById(R.id.groupRadio);
			groupRadio.setVisibility(View.GONE);
		}
		imgHome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mActivity.getClass().isAssignableFrom(InfoBusActivity.class))
					return;

				if (isClick)
					return;
				isClick = true;
				Intent i = new Intent(AbstractActivity.this, InfoBusActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

		lnSearch = (LinearLayout) findViewById(R.id.search);

		lnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mActivity.getClass().isAssignableFrom(SearchActivity.class))
					return;

				if (isClick)
					return;
				isClick = true;
				Intent i = new Intent(AbstractActivity.this, SearchActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra(Constant.KEY_CITY, rbtnHcm.isChecked());
				startActivity(i);
			}
		});
	}

	protected abstract int getViewLayoutId();

	protected abstract void initView(final Bundle savedInstanceState);

	public void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener onOkie, DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, onOkie);
        builder.setNegativeButton(R.string.cancel, onCancel);
        builder.setTitle(title);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
