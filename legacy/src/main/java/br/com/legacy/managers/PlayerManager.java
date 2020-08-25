package br.com.legacy.managers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by appsimples on 1/23/18.
 */

public class PlayerManager {

    private static final String TAG = PlayerManager.class.getSimpleName();

    private static final int VELOCITY_FLING_X = 50;
    private static final int VELOCITY_FLING_Y = 200;

    private SimpleExoPlayer player;
    private long contentPosition;
    private String contentUrl;
    private PlayerHandler playerHandler;
    private PlayerGesturesDetection playerGesturesDetection;

    public interface PlayerHandler {
        void onStateEnd();
        void onPlayerError(String error);
    }

    public PlayerManager(Context context) {
        playerHandler = (PlayerHandler) context;
        playerGesturesDetection = (PlayerGesturesDetection) context;
    }

    public void init(Context context, SimpleExoPlayerView simpleExoPlayerView) {
        // Create a default track selector.
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create a player instance.
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getApplicationInfo().name));

        // This is the MediaSource representing the content media (i.e. not the ad).
        if (contentUrl != null) {
            Uri uri = Uri.parse(contentUrl);

            // This is the MediaSource representing the media to be played.
            MediaSource contentMediaSource;
            String scheme = uri.getScheme();
            if (scheme != null && (scheme.equals("http") || scheme.equals("https"))) {
                contentMediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            } else {
                contentMediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            }

            // Prepare the player with the source.
            player.seekTo(contentPosition);
            player.prepare(contentMediaSource);
            player.setPlayWhenReady(true);

            // Add listeners
            addPlayerListener();
            addGestureListener(context, simpleExoPlayerView);
        } else {
            playerHandler.onPlayerError("video URL found is null");
        }
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public void reset() {
        if (player != null) {
            contentPosition = player.getContentPosition();
            player.release();
            player = null;
        }
    }

    public void release() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void addPlayerListener() {
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);
                switch (playbackState) {
                    case Player.STATE_ENDED:
                        playerHandler.onStateEnd();
                        break;
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                playerHandler.onPlayerError(error.getCause().getMessage());
            }
        });
    }

    public void setControllersVisibility(SimpleExoPlayerView simpleExoPlayerView, Boolean isVisible) {
        simpleExoPlayerView.setUseController(isVisible);
    }

    public void setPlayAndPauseOnTouch(SimpleExoPlayerView simpleExoPlayerView) {
        simpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (player.getPlayWhenReady())
                                    player.setPlayWhenReady(false);
                                else
                                    player.setPlayWhenReady(true);
                                return true;

                        }
                        return false;
            }
        });
    }

    public interface PlayerGesturesDetection {
        void onFling();
    }

    public void addGestureListener(final Context context, final SimpleExoPlayerView simpleExoPlayerView) {

        final Boolean[] showingControllers = new Boolean[1];
        simpleExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                switch (visibility) {
                    case PlaybackControlView.VISIBLE:
                        showingControllers[0] = true;
                        break;
                    case PlaybackControlView.GONE:
                        showingControllers[0] = false;
                        break;
                    case PlaybackControlView.INVISIBLE:
                        showingControllers[0] = false;
                        break;
                }
            }
        });

        simpleExoPlayerView.setControllerShowTimeoutMs(2000);

        final GestureDetector detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent event) {
                Log.d(TAG,"onDown: ");
                // don't return false here or else none of the other
                // gestures will work
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.i("TAG", "onSingleTapConfirmed: ");
                if (showingControllers[0])
                    simpleExoPlayerView.hideController();
                else
                    simpleExoPlayerView.showController();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.i(TAG, "onLongPress: ");
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.i(TAG, "onDoubleTap: ");
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                Log.i(TAG, "onScroll: ");
                return true;
            }

            @Override
            public boolean onFling(MotionEvent event1, MotionEvent event2,
                                   float velocityX, float velocityY) {
                Log.d(TAG, "onFling: ");
                playerGesturesDetection.onFling();
                return true;
            }
        });

        simpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });

    }

}
