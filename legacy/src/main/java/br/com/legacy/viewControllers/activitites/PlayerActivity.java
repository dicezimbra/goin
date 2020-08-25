package br.com.legacy.viewControllers.activitites;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import br.com.goin.component.analytics.analytics.Analytics;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.managers.PlayerManager;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;

public class PlayerActivity extends AppCompatActivity implements PlayerManager.PlayerHandler, PlayerManager.PlayerGesturesDetection{

    private SimpleExoPlayerView playerView;
    private PlayerManager player;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Coordinator.setStatusBarColorDark(this, getString(R.color.black));

        url = getIntent().getStringExtra(Constants.URL);

        playerView = findViewById(R.id.player_view);
        player = new PlayerManager(this);
        player.setContentUrl(url);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (url == null) {
            url = getIntent().getStringExtra(Constants.URL);
            player.setContentUrl(url);
        }

        player.init(this, playerView);

        Analytics.Companion.getInstance().screenView(this, getString(R.string.post_video_screen_view));
    }

    @Override
    public void onPause() {
        super.onPause();
        player.reset();
    }

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }

    @Override
    public void onStateEnd() {
        finish();
    }

    @Override
    public void onPlayerError(String error) {
        DialogUtils.INSTANCE.show(this, getString(R.string.error_playing_video), new DialogUtils.DialogCallback() {
            @Override
            public void onClickOk() {
                finish();
            }

            @Override
            public void onClickCancel() {

            }
        });
    }

    @Override
    public void onFling() {
        finish();
    }
}
