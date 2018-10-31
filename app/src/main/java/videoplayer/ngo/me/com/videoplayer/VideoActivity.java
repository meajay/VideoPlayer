package videoplayer.ngo.me.com.videoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import videoplayer.ngo.me.com.videoplayer.util.ExoplayerUtility;

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    SimpleExoPlayerView exoPlayerView;

    private ExoplayerUtility exoplayerUtility;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        exoplayerUtility = new ExoplayerUtility(this, exoPlayerView);

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(Constants.STATE_RESUME_WINDOW);
            playbackPosition = savedInstanceState.getLong(Constants.STATE_RESUME_POSITION);
            playWhenReady = savedInstanceState.getBoolean(Constants.STATE_PLAYER_FULLSCREEN);
        }
        exoplayerUtility.setExoplayerVariables(currentWindow, playbackPosition, playWhenReady);
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT > 23)) {
            exoplayerUtility.initializePlayer();
        }
        exoplayerUtility.hideSystemUi(exoPlayerView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.STATE_RESUME_WINDOW, currentWindow);
        outState.putLong(Constants.STATE_RESUME_POSITION, playbackPosition);
        outState.putBoolean(Constants.STATE_PLAYER_FULLSCREEN, playWhenReady);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT > 23) {
            exoplayerUtility.releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT > 23) {
            exoplayerUtility.destroyPlayer();
        }
    }


}
