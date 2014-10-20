package app.infobus;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import app.infobus.utils.Prefs;
import app.infobus.utils.ULog;

public abstract class BaseActivity extends FragmentActivity {
	private String tag = BaseActivity.class.getSimpleName();
	public static Prefs pref;
	private View viewMain;

	@Override
	protected final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
//			ActionBar mActionBar = this.getActionBar();
//			if (mActionBar != null)
//				mActionBar.hide();
//				mActionBar.setDisplayShowHomeEnabled(false);

			ULog.i(tag, "======class: " + this.getClass().getSimpleName());
			if (pref == null)
				pref = new Prefs(this);
			// //////////
//			 requestWindowFeature(Window.FEATURE_NO_TITLE);
			if (getViewLayoutId() > 0) {
				viewMain = this.getLayoutInflater().inflate(getViewLayoutId(), null);
				setContentView(viewMain);
			}
			// setContentView(getViewLayoutId());
			initView();
			initView(savedInstanceState);

			// setAction();

			// GA
			// Utility.setScreenNameGA(this.getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ////////////////////////

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initView() {

		// imgHome = (ImageView) findViewById(R.id.imgHome);
	}

	@SuppressWarnings("unchecked")
	public <V extends View> V getViewChild(int id) {
		return (V) viewMain.findViewById(id);
	}

	@SuppressWarnings("unchecked")
	public <V extends View> V getViewLayout() {
		return (V) viewMain;
	}

	// private void setAction() {
	//
	// if(mActivity.getClass().isAssignableFrom(SearchActivity.class)
	// || mActivity.getClass().isAssignableFrom(InfoDetailActivity.class)){
	// RadioGroup groupRadio = (RadioGroup) findViewById(R.id.groupRadio);
	// groupRadio.setVisibility(View.GONE);
	// }
	// imgHome.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// if (mActivity.getClass().isAssignableFrom(InfoBusActivity.class))
	// return;
	//
	// if (isClick)
	// return;
	// isClick = true;
	// Intent i = new Intent(BaseActivity.this, InfoBusActivity.class);
	// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// startActivity(i);
	// }
	// });
	//
	//
	// }

	protected abstract int getViewLayoutId();

	protected abstract void initView(final Bundle savedInstanceState);

	public void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener onOkie,
			DialogInterface.OnClickListener onCancel) {
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
