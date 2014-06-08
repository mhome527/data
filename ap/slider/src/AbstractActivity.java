package puzzle.slider.vn;

import java.util.HashSet;
import java.util.Set;

import puzzle.slider.vn.util.ShowLog;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

/**
 * @author
 * @version 1.0
 * 
 *          used for other classes inherit
 * 
 */
public abstract class AbstractActivity extends Activity {

	private final String tag = AbstractActivity.class.getSimpleName();

	public static interface ActivityStateChangedListener {
		public void onChanged(AbstractActivity activity);
	}

	private final Set<ActivityStateChangedListener> onResumeListeners = new HashSet<AbstractActivity.ActivityStateChangedListener>();

	private final Set<ActivityStateChangedListener> onPauseListeners = new HashSet<AbstractActivity.ActivityStateChangedListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected final void onCreate(final Bundle savedInstanceState) {
		ShowLog.showLogInfo(tag,"onCreate....");
		ShowLog.showLogDebug(tag, "<< onCreate " + savedInstanceState + " " + getClass().getSimpleName());

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		if (getViewLayoutId() != 0) {
			setContentView(getViewLayoutId());
		}
		
		initView(savedInstanceState);

		ActivityHistoryManager.addNewActivity(this);

	}

	protected abstract int getViewLayoutId();

	protected void initView(final Bundle savedInstanceState) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		ShowLog.showLogDebug(tag, "onPause ===== AbstractActivity ======= " + this.getClass().getSimpleName());

		for (final ActivityStateChangedListener listener : onPauseListeners) {
			listener.onChanged(this);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		ShowLog.showLogDebug(tag, "onResume ===== AbstractActivity ======= " + this.getClass().getSimpleName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShowLog.showLogDebug(tag, " onDestroy ============= AbstractActivity =============" + this.getClass().getSimpleName());
		ActivityHistoryManager.removeFromActivityHistory(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		ShowLog.showLogDebug(tag, " onStop ============= AbstractActivity =============" + this.getClass().getSimpleName());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress()) {
//			ShowLog.showLogDebug("DucLH", "onKeyDown  ============= keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress()1111 ============="
//					+ this.getClass().getSimpleName());
			return true;
		}
		else {
//			ShowLog.showLogDebug("DucLH", "onKeyDown  ============= keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress()22222 ============="
//					+ this.getClass().getSimpleName());
			return super.onKeyDown(keyCode, event);
		}

	}

	/**
	 * @author HuynhTD
	 * @param addListener
	 * @see add listener when resume
	 * 
	 */
	public void addOnResumeEventListener(final ActivityStateChangedListener addListener) {
		this.onResumeListeners.add(addListener);
	}

	/**
	 * @author HuynhTD
	 * @param addListener
	 * @see add listener when resume
	 * 
	 */
	public void removeOnResumeEventListener(final ActivityStateChangedListener removeListener) {
		this.onResumeListeners.remove(removeListener);
	}

	/**
	 * @author HuynhTD
	 * @param addListener
	 * @see add listener when pause
	 * 
	 */
	public void addOnPauseEventListener(final ActivityStateChangedListener addListener) {
		this.onPauseListeners.add(addListener);
	}

	/**
	 * @author HuynhTD
	 * @param removeListener
	 * @see remove listener when pause
	 * 
	 */
	public void removeOnPauseEventListener(final ActivityStateChangedListener removeListener) {
		this.onPauseListeners.remove(removeListener);
	}

	/**
	 * @author DucLH
	 * @param
	 * @see
	 * 
	 */
	@Override
	public void startActivity(final Intent intent) {
		super.startActivity(intent);
		ShowLog.showLogInfo(tag,"startActivity....");

	}

	/**
	 * @author DucLH
	 * @param
	 * @see
	 * 
	 */
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		ShowLog.showLogInfo(tag,"startActivityForResult....");

	}

	/**
	 * Don't check network when start activity
	 * 
	 * @param intent
	 * @param requestCode
	 */
	public void startActivityForResult2(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		ShowLog.showLogWarn(tag, "gc" + getClass().getSimpleName() + " onConfigurationChanged " + newConfig);
		super.onConfigurationChanged(newConfig);
	}

}
