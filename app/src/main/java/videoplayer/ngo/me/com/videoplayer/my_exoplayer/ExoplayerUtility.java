package videoplayer.ngo.me.com.videoplayer.my_exoplayer;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import videoplayer.ngo.me.com.videoplayer.Constants;
import videoplayer.ngo.me.com.videoplayer.R;
import videoplayer.ngo.me.com.videoplayer.util.SystemUtils;
import videoplayer.ngo.me.com.videoplayer.util.UIUtils;

/**
 * Created by Ajay on 30-10-2018.
 */
public class ExoplayerUtility {

    private SimpleExoPlayer exoPlayer;
    private Context context;
    private SimpleExoPlayerView exoPlayerView;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private MediaSource videoSource;
    private ExoStateChanged exoStateChanged;


    public ExoplayerUtility(Context context, SimpleExoPlayerView exoPlayerView, ExoStateChanged exoStateChanged) {
        this.context = context;
        this.exoPlayerView = exoPlayerView;
        this.exoStateChanged = exoStateChanged;
    }

    public void setExoplayerVariables(int currentWindow, long playbackPosition, boolean playWhenReady) {
        this.currentWindow = currentWindow;
        this.playbackPosition = playbackPosition;
        this.playWhenReady = playWhenReady;
    }

    public int getCurrentWindow() {
        return currentWindow;
    }

    public long getPlaybackPosition() {
        return playbackPosition;
    }

    public boolean getPlayWhenReady() {
        return playWhenReady;
    }

    public void initializePlayer() {
        if (exoPlayer == null && videoSource == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(), new DefaultLoadControl());

             videoSource = new ExtractorMediaSource(Uri.parse(Constants.VIDEO_URL),
                    new MyExoPlayerCacheFactory(context, 200 * 1024 * 1024, 100 * 1024 * 1024), new DefaultExtractorsFactory(), null, null);

            exoPlayer.prepare(videoSource, true, false);
            exoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    exoStateChanged.onExoPlayerStateChanged(playbackState);
                    switch (playbackState) {
                        case ExoPlayer.STATE_IDLE:
                            break;
                        case ExoPlayer.STATE_BUFFERING:
                            if (!SystemUtils.isNetworkAvailable(context)) {
                                UIUtils.showToast(context, context.getString(R.string.internet_disconnected_play_offline));
                            }
                            break;
                        case ExoPlayer.STATE_READY:
                            break;
                        case ExoPlayer.STATE_ENDED:
                            exoPlayer.seekTo(0);
                            exoPlayer.setPlayWhenReady(false);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    if (!SystemUtils.isNetworkAvailable(context)) {
                        UIUtils.showToast(context, context.getString(R.string.internet_not_available));
                        exoPlayer.setPlayWhenReady(false);
                    }
                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });
        } else {
            exoPlayer.prepare(videoSource, false, true);
        }

        exoPlayerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.seekTo(currentWindow, playbackPosition);
    }

    public void releasePlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            playWhenReady = exoPlayer.getPlayWhenReady();
            exoPlayer.stop();
        }
    }

    public void destroyPlayer() {
        if (exoPlayer != null) {
            releasePlayer();
            exoPlayer = null;
        }
    }

    public void hideSystemUi(SimpleExoPlayerView simpleExoPlayerView) {
        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("video-player")).
                createMediaSource(uri);
    }

}
