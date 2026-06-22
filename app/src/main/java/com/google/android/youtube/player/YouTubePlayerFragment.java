package com.google.android.youtube.player;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/* JADX INFO: loaded from: classes2.dex */
public class YouTubePlayerFragment extends Fragment implements YouTubePlayer.Provider {

    private final ViewLifecycleListener viewLifecycleListener = new ViewLifecycleListener();
    private Bundle savedState;
    private YouTubePlayerView playerView;
    private String developerKey;
    private YouTubePlayer.OnInitializedListener pendingListener;
    private boolean manageAudioFocus;

    private final class ViewLifecycleListener implements YouTubePlayerView.OnViewLifecycleListener {
        private ViewLifecycleListener() {
        }

        @Override // com.google.android.youtube.player.YouTubePlayerView.OnViewLifecycleListener
        public final void onViewCreated(YouTubePlayerView youTubePlayerView) {
        }

        @Override // com.google.android.youtube.player.YouTubePlayerView.OnViewLifecycleListener
        public final void onViewInitialize(YouTubePlayerView youTubePlayerView, String str, YouTubePlayer.OnInitializedListener onInitializedListener) {
            YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.this;
            youTubePlayerFragment.initialize(str, youTubePlayerFragment.pendingListener);
        }
    }

    private void tryInitialize() {
        YouTubePlayerView youTubePlayerView = this.playerView;
        if (youTubePlayerView == null || this.pendingListener == null) {
            return;
        }
        youTubePlayerView.setManageAudioFocus(this.manageAudioFocus);
        this.playerView.initializeInternal(getActivity(), this, this.developerKey, this.pendingListener, this.savedState);
        this.savedState = null;
        this.pendingListener = null;
    }

    public void initialize(String str, YouTubePlayer.OnInitializedListener onInitializedListener) {
        this.developerKey = str;
        this.pendingListener = onInitializedListener;
        tryInitialize();
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.savedState = bundle != null ? bundle.getBundle("YouTubePlayerFragment.KEY_PLAYER_VIEW_STATE") : null;
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.playerView = new YouTubePlayerView(getActivity(), null, 0, this.viewLifecycleListener);
        tryInitialize();
        return this.playerView;
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        if (this.playerView != null) {
            Activity activity = getActivity();
            this.playerView.onActivityDestroyed(activity == null || activity.isFinishing());
        }
        super.onDestroy();
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        this.playerView.release(getActivity().isFinishing());
        this.playerView = null;
        super.onDestroyView();
    }

    @Override // android.app.Fragment
    public void onPause() {
        this.playerView.onPause();
        super.onPause();
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        this.playerView.onResume();
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        YouTubePlayerView youTubePlayerView = this.playerView;
        bundle.putBundle("YouTubePlayerFragment.KEY_PLAYER_VIEW_STATE", youTubePlayerView != null ? youTubePlayerView.getSavedState() : this.savedState);
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        this.playerView.onStart();
    }

    @Override // android.app.Fragment
    public void onStop() {
        this.playerView.onStop();
        super.onStop();
    }
}
