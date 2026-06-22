package com.google.android.youtube.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/* Stub: public API surface preserved (initialize + lifecycle); player is a no-op. */
public class YouTubePlayerSupportFragment extends Fragment implements YouTubePlayer.Provider {
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
            YouTubePlayerSupportFragment self = YouTubePlayerSupportFragment.this;
            self.initialize(str, self.pendingListener);
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

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.savedState = bundle != null ? bundle.getBundle("YouTubePlayerSupportFragment.KEY_PLAYER_VIEW_STATE") : null;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.playerView = new YouTubePlayerView(getActivity(), null, 0, this.viewLifecycleListener);
        tryInitialize();
        return this.playerView;
    }

    @Override
    public void onDestroyView() {
        if (this.playerView != null) {
            this.playerView.release(getActivity() == null || getActivity().isFinishing());
            this.playerView = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (this.playerView != null) {
            FragmentActivity activity = getActivity();
            this.playerView.onActivityDestroyed(activity == null || activity.isFinishing());
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (this.playerView != null) {
            this.playerView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.playerView != null) {
            this.playerView.onResume();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        YouTubePlayerView youTubePlayerView = this.playerView;
        bundle.putBundle("YouTubePlayerSupportFragment.KEY_PLAYER_VIEW_STATE", youTubePlayerView != null ? youTubePlayerView.getSavedState() : this.savedState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.playerView != null) {
            this.playerView.onStart();
        }
    }

    @Override
    public void onStop() {
        if (this.playerView != null) {
            this.playerView.onStop();
        }
        super.onStop();
    }

    public void initialize(String str, YouTubePlayer.OnInitializedListener onInitializedListener) {
        this.developerKey = str;
        this.pendingListener = onInitializedListener;
        tryInitialize();
    }
}
