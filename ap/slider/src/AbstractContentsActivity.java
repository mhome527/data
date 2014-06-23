package puzzle.slider.vn;

import java.text.DecimalFormat;

import puzzle.slider.vn.ScreenReceiver.OnScreenReceiverListenner;
import puzzle.slider.vn.util.ShowLog;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author
 * @version 1.0
 * 
 *          used for other classes inherit
 * 
 */
public abstract class AbstractContentsActivity extends AbstractActivity implements OnClickListener, OnScreenReceiverListenner
{

	private final String tag = AbstractContentsActivity.class.getSimpleName();

	@Override
	protected void initView(final Bundle savedInstanceState) {
		super.initView(savedInstanceState);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */

	@Override
	protected int getViewLayoutId() {
		return 0;
	}

	@Override
	protected void onResume() {		
//		SoundManager.playSound(Constant.SOUND_A, true);
		super.onResume();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		ShowLog.showLogDebug(tag, "onActivityResult");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onWindowFocusChanged(final boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

	}

	@Override
	public void onClick(final View v) {

	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		ShowLog.showLogDebug(tag, "onkeydown -- keyCode:" + keyCode);
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {

		return super.onMenuItemSelected(featureId, item);

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		ShowLog.showLogWarn(tag, "gc" + getClass().getSimpleName() + " onLowMemory");
		super.onLowMemory();
	}

	@SuppressLint("UseValueOf")
	public void logHeap(String s) {
//		Double allocated = new Double(Debug.getNativeHeapAllocatedSize()) / new Double((1048576));
//		Double available = new Double(Debug.getNativeHeapSize()) / 1048576.0;
//		Double free = new Double(Debug.getNativeHeapFreeSize()) / 1048576.0;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		ShowLog.i(tag, "================ " + s + " =================" + getClass().getSimpleName());
//		ShowLog.showLogWarn("HEAP&MEM", "heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
//		ShowLog.showLogWarn(
//				"HEAP&MEM",
//				"memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory() / 1048576)) + "MB of "
//						+ df.format(new Double(Runtime.getRuntime().maxMemory() / 1048576)) + "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory() / 1048576))
//						+ "MB free)");
//		ShowLog.showLogWarn("HEAP&MEM", "====================================");
	}

	/**
	 * @since Apr 12, 2013 - 9:27:05 AM
	 * @param isLeft
	 */
	public void startActivityAnim(Bundle bundle) {
		boolean isLeft = true;
		bundle = getIntent().getExtras();

		if (isLeft) {
			ShowLog.showLogWarn(tag, "left in ------------- left out");
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		} else {
			ShowLog.showLogWarn(tag, "right in ------------- right out");
			overridePendingTransition(R.anim.slide_in_right2, R.anim.slide_out_right);
		}
	}

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
