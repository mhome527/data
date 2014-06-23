package puzzle.slider.vn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import puzzle.slider.vn.util.ShowLog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
/**
 * 
 * @sine Apr 4, 2013
 */
public class SoundManager {
	
	static private SoundManager _instance;
	private static Context mContext;
	
	private static HashMap<String, MediaPlayer> map;
	public static boolean isPause = false;
	public static boolean isRetain = false;
	
	public static boolean isScreenOFF = false;
	
	// 1 is normal, 2 is left, 3 is right
	public static int isLEFT = 1;
	
	private SoundManager(){
		
	}
	// Returns the single instance of the SoundManager
	static synchronized public SoundManager getInstance() {
	    if (_instance == null) 
	      _instance = new SoundManager();
	    return _instance;
	 }

	// Initializes the storage for the sounds
	public static  void initSounds(Context context){ 
		 mContext = context;
	     map = new HashMap<String, MediaPlayer>();
	}
	
	/**
	 * 
	 * @since Apr 4, 2013 - 1:46:57 PM
	 * @param name The name of sound
	 * @param isloop
	 */
	public static void playSound(final String name, boolean isloop){
		if (null!= map && null != map.get(name)) {
			MediaPlayer mp = map.get(name);
			if (!mp.isPlaying() && !isScreenOFF) {
				mp.setLooping(isloop);
				mp.start();
			}
			
		}else{
			MediaPlayer mp = new MediaPlayer();
			try{
				final AssetFileDescriptor descriptor = mContext.getAssets().openFd(name);
				mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
				mp.prepareAsync();
				mp.setLooping(isloop);
				mp.setOnPreparedListener(new OnPreparedListener() {
					
					public void onPrepared(MediaPlayer mp) {
						// start call here
						map.put(name, mp);
						if (!isScreenOFF) {
							mp.start();
							try {
								descriptor.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
				
			}catch(Exception e){
				
			}
		}
	}
	
	/**
	 * 
	 * @since Apr 4, 2013 - 1:46:54 PM
	 * @param name The name of sound
	 */
	public static void resumeSound(String name){
		if (null!= map && null!= map.get(name)) {
			MediaPlayer mp = map.get(name);
			if (mp!=null && !mp.isPlaying()) {
				mp.start();
			}
		}
	}
	
	/**
	 * 
	 * @since Apr 4, 2013 - 1:46:51 PM
	 * @param name The name of sound
	 */
	public static void pauseSound(String name){
		if (null!= map && null!= map.get(name)) {
			MediaPlayer mp = map.get(name);
			if (mp!=null && mp.isPlaying()) {
//				if (name.equals(Constant.SOUND_A) && isPause) {
//					mp.pause();
//				}else if(!name.equals(Constant.SOUND_A)){
					mp.pause();
//				}
			}
		}
	}
	
	/**
	 * 
	 * @since Apr 4, 2013 - 1:46:48 PM
	 * @param name The name of sound
	 */
	public static void stopSound(String name){
		if (null!= map && null!= map.get(name)) {
			MediaPlayer mp = map.get(name);
			if (mp!=null) {
				mp.stop();
				mp.release();
				mp = null;
				map.remove(name);
				ShowLog.i("SoundManager", "stopSound!!!!!! name: " + name);
			}
		}
	}
	
	/**
	 * 
	 * @since Apr 4, 2013 - 2:47:16 PM
	 */
	public static void clearAllSound(){
		if(null != map){
			for (Map.Entry<String,MediaPlayer> entry : map.entrySet()) {
				MediaPlayer mp = entry.getValue();
				mp.stop();
				mp.release();
				mp = null;
				}
			map.clear();
		}		
	}
	
	public static MediaPlayer getMediaPlayer(String name) {
		MediaPlayer mPlayer = null;
		if (null != map)
			mPlayer = map.get(name);
		return mPlayer;

	}
	
}