package videoplayer.ngo.me.com.videoplayer;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import videoplayer.ngo.me.com.videoplayer.my_exoplayer.ExoStateChanged;
import videoplayer.ngo.me.com.videoplayer.my_exoplayer.ExoplayerUtility;

public class VideoActivity extends AppCompatActivity implements ExoStateChanged {

    @BindView(R.id.video_view)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.exo_fullscreen)
    ImageView fullScreenButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ExoplayerUtility exoplayerUtility;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private Dialog fullScreenDialog;
    private boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        exoplayerUtility = new ExoplayerUtility(this, exoPlayerView, this);
        initFullscreenDialog();
        initFullscreenButton();

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(Constants.STATE_RESUME_WINDOW);
            playbackPosition = savedInstanceState.getLong(Constants.STATE_RESUME_POSITION);
            playWhenReady = savedInstanceState.getBoolean(Constants.STATE_PLAYER_READY);
            isFullScreen = savedInstanceState.getBoolean(Constants.STATE_PLAYER_FULLSCREEN);
        }
        exoplayerUtility.setExoplayerVariables(currentWindow, playbackPosition, playWhenReady);
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT >= 23)) {
            exoplayerUtility.initializePlayer();
        }
        exoplayerUtility.hideSystemUi(exoPlayerView);
        progressBar.setVisibility(View.VISIBLE);
        if (isFullScreen) {
            fullScreenButton.setImageDrawable(getDrawable(R.drawable.ic_fullscreen_skrink));
            openFullscreenDialog();
        } else {
            fullScreenButton.setImageDrawable(getDrawable(R.drawable.ic_fullscreen_expand));
            closeFullscreenDialog();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.STATE_RESUME_WINDOW, exoplayerUtility.getCurrentWindow());
        outState.putLong(Constants.STATE_RESUME_POSITION, exoplayerUtility.getPlaybackPosition());
        outState.putBoolean(Constants.STATE_PLAYER_READY, exoplayerUtility.getPlayWhenReady());
        outState.putBoolean(Constants.STATE_PLAYER_FULLSCREEN, isFullScreen);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT >= 23) {
            exoplayerUtility.releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT >= 23) {
            exoplayerUtility.destroyPlayer();
        }
    }

    private void initFullscreenDialog() {
        fullScreenDialog = new Dialog(this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (isFullScreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {

        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        fullScreenDialog.addContentView(exoPlayerView, new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        fullScreenDialog.show();
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        ((FrameLayout) findViewById(R.id.player_container)).addView(exoPlayerView);
        fullScreenDialog.dismiss();
    }

    private void initFullscreenButton() {
        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFullScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    isFullScreen = false;
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    isFullScreen = true;
                }
            }
        });
    }


    @Override
    public void onExoPlayerStateChanged(int i) {
        switch (i) {
            case ExoPlayer.STATE_IDLE:
                progressBar.setVisibility(View.GONE);
                break;
            case ExoPlayer.STATE_BUFFERING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case ExoPlayer.STATE_READY:
                progressBar.setVisibility(View.GONE);
                break;
            case ExoPlayer.STATE_ENDED:
                break;
            default:
                break;
        }
    }
}
