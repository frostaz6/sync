package com.google.android.youtube.player;

/* Stub: public API surface preserved; bodies are no-ops. */
public interface YouTubePlayer {

    public enum ErrorReason {
        EMBEDDING_DISABLED,
        BLOCKED_FOR_APP,
        NOT_PLAYABLE,
        NETWORK_ERROR,
        UNAUTHORIZED_OVERLAY,
        PLAYER_VIEW_TOO_SMALL,
        PLAYER_VIEW_NOT_VISIBLE,
        EMPTY_PLAYLIST,
        AUTOPLAY_DISABLED,
        USER_DECLINED_RESTRICTED_CONTENT,
        USER_DECLINED_HIGH_BANDWIDTH,
        UNEXPECTED_SERVICE_DISCONNECTION,
        INTERNAL_ERROR,
        UNKNOWN
    }

    public interface OnFullscreenListener {
        void onFullscreen(boolean fullscreen);
    }

    public interface OnInitializedListener {
        void onInitializationSuccess(Provider provider, YouTubePlayer youTubePlayer, boolean restored);

        void onInitializationFailure(Provider provider, YouTubeInitializationResult youTubeInitializationResult);
    }

    public interface PlaybackEventListener {
        void onBuffering(boolean buffering);

        void onPlaying();

        void onPaused();

        void onSeekTo(int positionMillis);

        void onStopped();
    }

    public interface PlayerStateChangeListener {
        void onLoading();

        void onLoaded(String str);

        void onAdStarted();

        void onVideoStarted();

        void onVideoEnded();

        void onError(ErrorReason errorReason);
    }

    public enum PlayerStyle {
        DEFAULT,
        MINIMAL,
        CHROMELESS
    }

    public interface PlaylistEventListener {
        void onPrevious();

        void onNext();

        void onPlaylistEnded();
    }

    public interface Provider {
    }

    void setFullscreen(boolean fullscreen);

    void loadVideo(String videoId, int startSeconds);
}
