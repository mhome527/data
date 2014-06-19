package puzzle.slider.vn;

import java.text.DecimalFormat;

import puzzle.slider.vn.GCMIntentService.OnMessageComming;
import puzzle.slider.vn.ScreenReceiver.OnScreenReceiverListenner;
import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.ShowLog;
import puzzle.slider.vn.util.Utility;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
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
public abstract class AbstractContentsActivity extends AbstractActivity implements OnClickListener, OnScreenReceiverListenner, OnMessageComming {

	private final String tag = AbstractContentsActivity.class.getSimpleName();

	@Override
	public void onMessage(String mess) {
		// TODO Auto-generated method stub
		// Intent intent = new Intent(getApplicationContext(), MessageCameActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// intent.putExtra(Constant.PUSH_MESS, mess);
		// try {
		// startActivity(intent);
		// } catch (Exception e) {
		// if (Constant.IS_PrintStackTrace) {
		// e.printStackTrace();
		// }
		// }

	}

	@Override
	public void onScreenOn(boolean isunlock) {
		// TODO Auto-generated method stub
		ShowLog.showLogDebug("ScreenReceive", "onScreenOn is " + isunlock);
		if (!isunlock || Utility.isApplicationSentToBackground(getApplicationContext())) {
			SoundManager.isPause = true;
			SoundManager.pauseSound(Constant.SOUND_A);
		} else {
			PlaysoundA();
		}
	}

	@Override
	protected void initView(final Bundle savedInstanceState) {
		// logHeap("CREATE");
		super.initView(savedInstanceState);
//		PlaysoundA();
//		ScreenReceiver.setOnScreenReceiverListenner(this);
//		GCMIntentService.setOnMessageComming(this);
//		if (
//		// !(this instanceof JigsawMainActivityV2)
//		// &&
//		!(this instanceof WinnerActivity)
//		// && !(this instanceof StartupApp)
//		) {
//			startActivityAnim(savedInstanceState);
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */

	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		ShowLog.i(tag, "onResume - activity history size =" + ActivityHistoryManager.ActivityHistory.size());
		PlaysoundA();
		if (
		// this instanceof StartupApp
		// || this instanceof PuzzleOptionActivity
		// || this instanceof PuzzleImageActivity ||
		this instanceof SliderImageActivity)
		// || this instanceof PuzzlesStoreActivity)
		{
			if (SoundManager.isLEFT == 2) {
				ShowLog.showLogWarn("ANIMATION", "left in ------------- left out");
				SoundManager.isLEFT = 1;
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			} else if (SoundManager.isLEFT == 3) {
				ShowLog.showLogWarn("ANIMATION", "right in ------------- right out");
				SoundManager.isLEFT = 1;
				overridePendingTransition(R.anim.slide_in_right2, R.anim.slide_out_right);
			}
		}
		super.onResume();
	}

	/**
	 * @author thangtb
	 * @since Apr 8, 2013 - 11:56:11 AM
	 */
	private void PlaysoundA() {
		if (this instanceof SliderMainActivity ) { // this instanceof JigsawMainActivityV2 ||
			SoundManager.isPause = true;
			SoundManager.pauseSound(Constant.SOUND_A);
		} else {
			SoundManager.isPause = false;
			SoundManager.playSound(Constant.SOUND_A, true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		ShowLog.showLogDebug("AbstractContents", "onActivityResult");
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
		ShowLog.showLogDebug("Abstract", "onkeydown -- keyCode:" + keyCode);
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
	protected void onStop() {
		ShowLog.i(tag, "onStop");
		if (Utility.isApplicationSentToBackground(getApplicationContext())) {
			SoundManager.isPause = true;
			SoundManager.pauseSound(Constant.SOUND_A);
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		ShowLog.i(tag, "onDestroy - activity history size =" + ActivityHistoryManager.ActivityHistory.size());
		// logHeap("DESTROY");
		if (ActivityHistoryManager.ActivityHistory.size() == 1) {
			SoundManager.stopSound(Constant.SOUND_A);
			SoundManager.clearAllSound();
		}
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		ShowLog.showLogWarn(tag, "gc" + getClass().getSimpleName() + " onLowMemory");
		super.onLowMemory();
	}

	@SuppressLint("UseValueOf")
	public void logHeap(String s) {
		Double allocated = new Double(Debug.getNativeHeapAllocatedSize()) / new Double((1048576));
		Double available = new Double(Debug.getNativeHeapSize()) / 1048576.0;
		Double free = new Double(Debug.getNativeHeapFreeSize()) / 1048576.0;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		ShowLog.showLogWarn("HEAP&MEM", "================ " + s + " =================" + getClass().getSimpleName());
		ShowLog.showLogWarn("HEAP&MEM", "heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
		ShowLog.showLogWarn(
				"HEAP&MEM",
				"memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory() / 1048576)) + "MB of "
						+ df.format(new Double(Runtime.getRuntime().maxMemory() / 1048576)) + "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory() / 1048576))
						+ "MB free)");
		ShowLog.showLogWarn("HEAP&MEM", "====================================");
	}

	/**
	 * @author thangtb
	 * @since Apr 12, 2013 - 9:27:05 AM
	 * @param isLeft
	 */
	public void startActivityAnim(Bundle bundle) {
		boolean isLeft = true;
		bundle = getIntent().getExtras();
		if (null != bundle && bundle.containsKey(Constant.FLAG_ANIM_ISLEFT)) {
			isLeft = bundle.getBoolean(Constant.FLAG_ANIM_ISLEFT, true);
		}

		if (isLeft) {
			ShowLog.showLogWarn("ANIMATION", "left in ------------- left out");
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		} else {
			ShowLog.showLogWarn("ANIMATION", "right in ------------- right out");
			overridePendingTransition(R.anim.slide_in_right2, R.anim.slide_out_right);
		}
	}
}
