package com.google.android.youtube.player;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/* JADX INFO: loaded from: classes2.dex */
public class YouTubeBaseActivity extends Activity {

    private ViewLifecycleListener viewLifecycleListener;
    private YouTubePlayerView playerView;
    private int state;
    private Bundle savedState;

    private final class ViewLifecycleListener implements YouTubePlayerView.OnViewLifecycleListener {
        private ViewLifecycleListener() {
        }

        /* synthetic */ ViewLifecycleListener(YouTubeBaseActivity youTubeBaseActivity, byte b) {
            this();
        }

        @Override // com.google.android.youtube.player.YouTubePlayerView.OnViewLifecycleListener
        public final void onViewCreated(YouTubePlayerView youTubePlayerView) {
            if (YouTubeBaseActivity.this.playerView != null && YouTubeBaseActivity.this.playerView != youTubePlayerView) {
                YouTubeBaseActivity.this.playerView.release(true);
            }
            YouTubeBaseActivity.this.playerView = youTubePlayerView;
            if (YouTubeBaseActivity.this.state > 0) {
                youTubePlayerView.onStart();
            }
            if (YouTubeBaseActivity.this.state >= 2) {
                youTubePlayerView.onResume();
            }
        }

        @Override // com.google.android.youtube.player.YouTubePlayerView.OnViewLifecycleListener
        public final void onViewInitialize(YouTubePlayerView youTubePlayerView, String str, YouTubePlayer.OnInitializedListener onInitializedListener) {
            YouTubeBaseActivity youTubeBaseActivity = YouTubeBaseActivity.this;
            youTubePlayerView.initializeInternal(youTubeBaseActivity, youTubePlayerView, str, onInitializedListener, youTubeBaseActivity.savedState);
            YouTubeBaseActivity.clearSavedState(YouTubeBaseActivity.this);
        }
    }

    static /* synthetic */ Bundle clearSavedState(YouTubeBaseActivity youTubeBaseActivity) {
        youTubeBaseActivity.savedState = null;
        return null;
    }

    final YouTubePlayerView.OnViewLifecycleListener getViewLifecycleListener() {
        return this.viewLifecycleListener;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.viewLifecycleListener = new ViewLifecycleListener(this, (byte) 0);
        this.savedState = bundle != null ? bundle.getBundle("YouTubeBaseActivity.KEY_PLAYER_VIEW_STATE") : null;
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        YouTubePlayerView youTubePlayerView = this.playerView;
        if (youTubePlayerView != null) {
            youTubePlayerView.onActivityDestroyed(isFinishing());
        }
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onPause() {
        this.state = 1;
        YouTubePlayerView youTubePlayerView = this.playerView;
        if (youTubePlayerView != null) {
            youTubePlayerView.onPause();
        }
        super.onPause();
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.state = 2;
        YouTubePlayerView youTubePlayerView = this.playerView;
        if (youTubePlayerView != null) {
            youTubePlayerView.onResume();
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        YouTubePlayerView youTubePlayerView = this.playerView;
        bundle.putBundle("YouTubeBaseActivity.KEY_PLAYER_VIEW_STATE", youTubePlayerView != null ? youTubePlayerView.getSavedState() : this.savedState);
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        this.state = 1;
        YouTubePlayerView youTubePlayerView = this.playerView;
        if (youTubePlayerView != null) {
            youTubePlayerView.onStart();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        this.state = 0;
        YouTubePlayerView youTubePlayerView = this.playerView;
        if (youTubePlayerView != null) {
            youTubePlayerView.onStop();
        }
        super.onStop();
    }
}
