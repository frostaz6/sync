package com.resync.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: loaded from: classes.dex */
public class CustomYouTubeVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView playerView;

    private String url;
    private String videoId;

    private int startSeconds;

    class CloseClickListener implements View.OnClickListener {
        CloseClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CustomYouTubeVideoActivity.this.finish();
        }
    }

    private void initPlayer() {
        this.playerView.initialize("YOUR_YOUTUBE_DATA_API_KEY", this);
        try {
            ((View) this.playerView.getParent()).setOnClickListener(new CloseClickListener());
        } catch (Exception unused) {
        }
    }

    @Override // com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (wasRestored) {
            return;
        }
        youTubePlayer.setFullscreen(true);
        try {
            youTubePlayer.loadVideo(this.videoId, this.startSeconds);
        } catch (Exception unused) {
        }
    }

    @Override // com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult != null && youTubeInitializationResult.toString().equalsIgnoreCase("SERVICE_DISABLED")) {
            StaticToast.showWithContext(this, "Custom YouTube player detected.\n\nInternal YouTube link handling disabled.");
            SettingsSingleton.getInstance().setEmbedYouTube(false);
            LinkHelper.open(this, this.url);
            finish();
            return;
        }
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
            return;
        }
        if (!youTubeInitializationResult.toString().equalsIgnoreCase("SERVICE_INVALID")) {
            StaticToast.showWithContext(this, String.format("There was an error initializing the YouTubePlayer (%1$s)", youTubeInitializationResult.toString()));
            return;
        }
        StaticToast.showWithContext(this, "Custom YouTube player detected.\n\nInternal YouTube link handling disabled.");
        SettingsSingleton.getInstance().setEmbedYouTube(false);
        LinkHelper.open(this, this.url);
        finish();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            this.playerView.initialize("YOUR_YOUTUBE_DATA_API_KEY", this);
        }
    }

    @Override // com.google.android.youtube.player.YouTubeBaseActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setNavigationBarColor(-16777216);
        setContentView(R.layout.activity_youtube);
        this.playerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        String stringExtra = getIntent().getStringExtra("url");
        this.url = stringExtra;
        this.videoId = com.resync.app.util.links.LinkHandler.getYouTubeId(stringExtra);
        this.startSeconds = com.resync.app.util.links.LinkHandler.getYouTubeTimestamp(this.url) * 1000;
        if (TextUtils.isEmpty(this.videoId)) {
            StaticToast.showWithContext(this, "Invalid ID");
            finish();
        }
        initPlayer();
    }
}
