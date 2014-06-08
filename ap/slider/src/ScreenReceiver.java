package puzzle.slider.vn;

import puzzle.slider.vn.util.ShowLog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @sine Apr 5, 2013
 */
public class ScreenReceiver extends BroadcastReceiver {
    
  public static boolean wasScreenOn = true;
  
  public static boolean wasScreenUnLoked = true;

  @Override
  public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
          // DO WHATEVER YOU NEED TO DO HERE
    	  ShowLog.showLogDebug("ScreenReceiver", intent.getAction());
    	  SoundManager.isScreenOFF = true;
          wasScreenOn = false;
          
          if (null!= onScreenListenner)
        	  onScreenListenner.onScreenOn(false);
          
      } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
          // AND DO WHATEVER YOU NEED TO DO HERE
    	  ShowLog.showLogDebug("ScreenReceiver", intent.getAction());
    	  SoundManager.isScreenOFF = false;
          wasScreenOn = true;
          KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
          boolean locked = km.inKeyguardRestrictedInputMode();
          ShowLog.v("ScreenReceiver", ": Phone lock state from KEYGUARD_SERVICE: Current state:" + (locked ? "LOCKED":"UNLOCKED"));
          
          if (!locked) {
        	  if (null!= onScreenListenner)
        	  onScreenListenner.onScreenOn(true);
        	  SoundManager.isScreenOFF = false;
          }else{
        	  onScreenListenner.onScreenOn(false);
        	  SoundManager.isScreenOFF = true;
          }
          
      } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
    	  ShowLog.showLogDebug("ScreenReceiver", intent.getAction());
    	  SoundManager.isScreenOFF = false;
    	  wasScreenUnLoked = true;
    	  
    	  if (null!= onScreenListenner)
    		  onScreenListenner.onScreenOn(true);
      }
  }
  
  
  /**
   * set screen listenner
 * 
 * @since Apr 8, 2013 - 10:25:20 AM
 * @param onScreenReceiverListenner
 */
public static void setOnScreenReceiverListenner(
		OnScreenReceiverListenner onScreenReceiverListenner) {
	onScreenListenner = onScreenReceiverListenner;
}
  static OnScreenReceiverListenner onScreenListenner;
  
  public interface OnScreenReceiverListenner{
	  public void onScreenOn(boolean isunlock);
  }

}