package puzzle.slider.vn;

//import java.text.DecimalFormat;

//import puzzle.slider.vn.util.Constant;
import puzzle.slider.vn.util.ShowLog;
import puzzle.slider.vn.util.Utility;
import puzzle.slider.vn.view.CongratulationView;
//import puzzle.slider.vn.view.CongratulationView.FinishType;
//import puzzle.slider.vn.view.CongratulationView.GameType;
//import android.annotation.SuppressLint;
//import android.content.Intent;
import android.os.Bundle;
//import android.os.Debug;

public class WinnerActivity extends AbstractContentsActivity {

	private String imagepath = "";
//	private String gameId = "";
//	private int FINISHTYPE = 0;
	CongratulationView cgview = null;
	Boolean firstStart;
//	private int leveldiff = 1;
//	private int leveltime = 0;
//	private boolean guide1 = false;
//	private boolean guide2 = false;
	
	@Override
	protected void initView(Bundle savedInstanceState) {

		super.initView(savedInstanceState);
		
//		logHeap("OnCreate");				
//		
//		Intent i = getIntent();
//		if (i != null) {
//			imagepath = i.getStringExtra(Constant.IMAGE_PATH);
//			gameId = i.getStringExtra(Constant.GAME_ID);
//			FINISHTYPE = i.getIntExtra(Constant.FINISH_TYPE, 0);
//			
//			leveldiff = i.getIntExtra(Constant.LEVEL_DIFF, 1);
//			leveltime = i.getIntExtra(Constant.LEVEL_TIME, 0);
//			guide1 = i.getBooleanExtra(Constant.LEVEL_GUIDE1, true);
//			guide2 = i.getBooleanExtra(Constant.LEVEL_GUIDE2, true);
//		}
		
		cgview = new CongratulationView(this);
		cgview.setPathImage(imagepath);
//		cgview.setGameType(GameType.Board);
//		if (FINISHTYPE == Constant.FINISH1) {
//			cgview.setFinishType(FinishType.finish1);
//		} else if (FINISHTYPE == Constant.FINISH2) {
//			cgview.setFinishType(FinishType.finish2);
//		} else {
//			cgview.setFinishType(FinishType.finish3);
//		}
		
		super.setContentView(cgview);
		
		firstStart = true;
		
		cgview.setControlsClickListener(new CongratulationView.CongratulationClickListener() {
			
			@Override
			public void OnClickButtonReplay() {
//				Intent intent = new Intent(WinnerActivity.this, PuzzleOptionActivity.class);
//				intent.putExtra(Constant.LEVEL_DIFF, leveldiff); // value: 1 -> 5
//				intent.putExtra(Constant.LEVEL_TIME, leveltime); // value: 0 -> 4
//				intent.putExtra(Constant.LEVEL_GUIDE1, guide1); // value: false, true
//				intent.putExtra(Constant.LEVEL_GUIDE2, guide2); // value: false, true
//				intent.putExtra(Constant.PATH_NAME, imagepath);
//				intent.putExtra(Constant.GAME_ID, gameId);
//				intent.putExtra(Constant.FLAG_ANIM_ISLEFT, false);
//				startActivity(intent);
//				SoundManager.playSound(Constant.SOUND_C, false);
//				finish();
			}
			
			@Override
			public void OnClickButtonChoice() {
//				SoundManager.playSound(Constant.SOUND_C, false);
////				ActivityHistoryManager.removeActivityByClassName(PuzzleOptionActivity.class.getSimpleName());
//				Intent i = new Intent(getApplicationContext(), PuzzleImageActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//				i.putExtra(Constant.FLAG_ANIM_ISLEFT, false);
//				startActivity(i);
//				finish();
			}
			
//			@Override
//			public void OnClickButtonBack() {
////				SoundManager.playSound(Constant.SOUND_C, false);
//////				ActivityHistoryManager.removeActivityByClassName(PuzzleOptionActivity.class.getSimpleName());
////				Intent i = new Intent(getApplicationContext(), StartupApp.class);
////				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
////				i.putExtra(Constant.FLAG_ANIM_ISLEFT, false);
////				SoundManager.isLEFT =3;
////				startActivity(i);
////				finish();
//			}
		});
				
	}
	@Override
	protected int getViewLayoutId() {
		return 0;
	}
	
	@Override
	protected void onStop() {
		
		if (cgview!=null) {
			cgview.Stop();
		}
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		Log("pause");
		
		if (cgview!=null) {
			cgview.Pause(false);
		}
		
		super.onPause();
	}
	
	@Override
	protected void onResume() {
//		Log("resume");
		if (firstStart) {
			firstStart = false;
			cgview.Start();	
		}
//		else{
//			if (cgview!=null) {
//				cgview.Resume();
//			}
//		}
		
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
//		logHeap("OnDestroy");
		
		Utility.unbindDrawables(cgview);
    	System.gc();
    	
		super.onDestroy();
	}
	
	@Override
	public void onScreenOn(boolean isunlock) {
//		super.onScreenOn(isunlock);
		
//		if (isunlock) {
//			cgview.Resume();
//		}
//		else{
//			//screen lock
			cgview.Pause(true);
//		}
		
	}
			
//	@SuppressLint("UseValueOf")
//	public void logHeap(String s) {
//        Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
//        Double available = new Double(Debug.getNativeHeapSize())/1048576.0;
//        Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0;
//        DecimalFormat df = new DecimalFormat();
//        df.setMaximumFractionDigits(2);
//        df.setMinimumFractionDigits(2);
//
//        ShowLog.showLogWarn("HEAP&MEM", "================ "+s+" =================" +getClass().getSimpleName());
//        ShowLog.showLogWarn("HEAP&MEM", "heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
//        ShowLog.showLogWarn("HEAP&MEM", "memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");
//        ShowLog.showLogWarn("HEAP&MEM", "====================================");
//    }
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		return;
	}
	
	private void Log(String msg){
		ShowLog.showLogDebug(WinnerActivity.class.getSimpleName(), msg);
	}
}
