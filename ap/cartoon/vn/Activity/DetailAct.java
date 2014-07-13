package cartoon.youtube.vn.Activity;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import cartoon.youtube.vn.Constant;
import cartoon.youtube.vn.R;

/**
 * Created by trandaohuynh on 7/12/14.
 */
public class DetailAct extends YouTubeFailureRecoveryActivity {
    private YouTubePlayerView playerView;
    private YouTubePlayer player;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.movie_activity);
        playerView = (YouTubePlayerView) findViewById(R.id.player);
        playerView.initialize(Constant.APP_YOUTUBE, this);
        player.setFullscreen(true);

    }

//    public int getContentViewID() {
//        return R.layout.movie_activity;
//    }
//
////    @Override
//    public void onAfterCreate(Bundle savedInstanceState) {
//        playerView = (YouTubePlayerView) findViewById(R.id.player);
//        playerView.initialize(Constant.APP_YOUTUBE, this);
//
//    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return null;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }


}
