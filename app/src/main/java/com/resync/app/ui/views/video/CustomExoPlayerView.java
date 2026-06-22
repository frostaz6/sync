package com.resync.app.ui.views.video;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.github.chrisbanes.photoview.animation.OnErrorListener;
import com.github.chrisbanes.photoview.animation.OnStartListener;
import com.github.chrisbanes.photoview.animation.OnUpdateListener;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DeviceInfo;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.text.CueGroup;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoSize;
import com.resync.app.singleton.ExoplayerFactorySingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.internal.SyncStyleable;
import java.io.EOFException;
import java.util.List;
import com.resync.app.net.OkHttpHelper;
import com.resync.app.singletons.SimpleCacheSingleton;
import com.resync.app.ui.views.video.CustomTextureView;
import com.resync.app.ui.views.video.NewVideoInterface;
import com.resync.app.ui.views.video.SimpleTextureView;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.util.BestVideoTrackSelection;
import com.resync.app.util.InlineVideoTrackSelection;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher;

/* JADX INFO: loaded from: classes3.dex */
public class CustomExoPlayerView extends FrameLayout implements NewVideoInterface {
    static final String TAG = CustomExoPlayerView.class.getSimpleName();
    private OnErrorListener mErrorListener;
    protected boolean mHasAudio;
    protected boolean mHd;
    protected boolean mMute;
    protected ExoPlayer mPlayer;
    protected SimpleTextureView mSimpleTextureView;
    protected String mSource;
    private OnStartListener mStartListener;
    protected CustomTextureView mTextureView;
    private OnUpdateListener mUpdateListener;
    protected boolean mZoomable;

    class PlayerListener implements Player.Listener {
        PlayerListener() {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onAudioAttributesChanged(AudioAttributes audioAttributes) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onAudioSessionIdChanged(int audioSessionId) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onAvailableCommandsChanged(Player.Commands commands) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onCues(CueGroup cueGroup) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onCues(List list) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onDeviceInfoChanged(DeviceInfo deviceInfo) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onDeviceVolumeChanged(int volume, boolean muted) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onEvents(Player interfaceC0145Player, Player.Events events) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onIsLoadingChanged(boolean isLoading) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onIsPlayingChanged(boolean isPlaying) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onLoadingChanged(boolean isLoading) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onMaxSeekToPreviousPositionChanged(long maxSeekToPreviousPositionMs) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onMediaItemTransition(MediaItem mediaItem, int reason) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onMetadata(Metadata metadata) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPlaybackStateChanged(int playbackState) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public void onPlayerError(PlaybackException playbackException) {
            CrashlyticsHelper.logEvent(6, "Video source", CustomExoPlayerView.this.getSource());
            try {
                if (TextUtils.isEmpty(playbackException.getMessage())) {
                    CrashlyticsHelper.logEvent(6, "Error decoding", "No message supplied");
                } else {
                    CrashlyticsHelper.logEvent(6, "Error decoding", playbackException.getMessage());
                }
            } catch (Exception decodeLogException) {
                CrashlyticsHelper.logException(decodeLogException);
            }
            int errorCode = playbackException.errorCode;
            if (errorCode == 1) {
                try {
                    CrashlyticsHelper.logException(playbackException);
                } catch (Exception logException) {
                    CrashlyticsHelper.logException(logException);
                }
            } else if (errorCode == 0) {
                try {
                    CrashlyticsHelper.logException(playbackException);
                } catch (Exception logException) {
                    CrashlyticsHelper.logException(logException);
                }
            } else if (errorCode == 2) {
                try {
                    CrashlyticsHelper.logException(playbackException);
                } catch (Exception logException) {
                    CrashlyticsHelper.logException(logException);
                }
            }
            if (CustomExoPlayerView.this.mErrorListener != null) {
                CustomExoPlayerView.this.mErrorListener.onException(playbackException);
                if (playbackException.getCause() instanceof EOFException) {
                    CustomExoPlayerView.this.mErrorListener.onErrorCode(20);
                } else if (playbackException.getCause() instanceof FileDataSource.FileDataSourceException) {
                    try {
                        if (playbackException.getCause().getCause() instanceof EOFException) {
                            CustomExoPlayerView.this.mErrorListener.onErrorCode(20);
                        } else {
                            CustomExoPlayerView.this.mErrorListener.onErrorCode(19);
                        }
                    } catch (Exception unused) {
                        CustomExoPlayerView.this.mErrorListener.onErrorCode(19);
                    }
                } else if (playbackException.getCause() instanceof MediaCodecRenderer.DecoderInitializationException) {
                    CrashlyticsHelper.logEvent(6, "Video source", CustomExoPlayerView.this.getSource());
                    try {
                        if (TextUtils.isEmpty(playbackException.getMessage())) {
                            CrashlyticsHelper.logEvent(6, "Error decoding", "No message supplied");
                        } else {
                            CrashlyticsHelper.logEvent(6, "Error decoding", playbackException.getMessage());
                        }
                    } catch (Exception decodeLogException) {
                        CrashlyticsHelper.logException(decodeLogException);
                    }
                    int decoderErrorCode = playbackException.errorCode;
                    if (decoderErrorCode == 1) {
                        try {
                            CrashlyticsHelper.logException(playbackException);
                        } catch (Exception logException) {
                            CrashlyticsHelper.logException(logException);
                        }
                        CustomExoPlayerView.this.mErrorListener.onErrorCode(33);
                    } else if (decoderErrorCode == 0) {
                        try {
                            CrashlyticsHelper.logException(playbackException);
                        } catch (Exception logException) {
                            CrashlyticsHelper.logException(logException);
                        }
                        CustomExoPlayerView.this.mErrorListener.onErrorCode(34);
                    } else if (decoderErrorCode == 2) {
                        try {
                            CrashlyticsHelper.logException(playbackException);
                        } catch (Exception logException) {
                            CrashlyticsHelper.logException(logException);
                        }
                        CustomExoPlayerView.this.mErrorListener.onErrorCode(35);
                    } else {
                        CustomExoPlayerView.this.mErrorListener.onErrorCode(32);
                    }
                } else {
                    if (playbackException.getCause() != null) {
                        CustomExoPlayerView.this.mErrorListener.onMessage(playbackException.getCause().getClass().getSimpleName());
                    }
                    CustomExoPlayerView.this.mErrorListener.onErrorCode(14);
                }
                CustomExoPlayerView.this.setOnErrorListener(null);
            }
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPlayerErrorChanged(PlaybackException playbackException) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPlaylistMetadataChanged(MediaMetadata mediaMetadata) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPositionDiscontinuity(int reason) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPositionDiscontinuity(Player.PositionInfo positionInfo, Player.PositionInfo positionInfo2, int reason) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public void onRenderedFirstFrame() {
            CustomExoPlayerView.this.deliverMessageUpdate("Rendered first frame");
            if (CustomExoPlayerView.this.mStartListener != null) {
                CustomExoPlayerView.this.mStartListener.onStart();
                CustomExoPlayerView.this.setOnStartListener(null);
            }
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onRepeatModeChanged(int repeatMode) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onSeekBackIncrementChanged(long seekBackIncrementMs) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onSeekForwardIncrementChanged(long seekForwardIncrementMs) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onSeekProcessed() {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onSkipSilenceEnabledChanged(boolean skipSilenceEnabled) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onSurfaceSizeChanged(int width, int height) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onTimelineChanged(Timeline timeline, int reason) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onTrackSelectionParametersChanged(TrackSelectionParameters trackSelectionParameters) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public void onTracksChanged(Tracks tracks) {
            if (tracks != null) {
                try {
                    if (tracks.isEmpty()) {
                        return;
                    }
                    for (int groupIndex = 0; groupIndex < tracks.getGroups().size(); groupIndex++) {
                        for (int trackIndex = 0; trackIndex < tracks.getGroups().get(groupIndex).length; trackIndex++) {
                            String sampleMimeType = tracks.getGroups().get(groupIndex).getTrackFormat(trackIndex).sampleMimeType;
                            if (sampleMimeType != null && sampleMimeType.contains(MimeTypes.BASE_TYPE_AUDIO)) {
                                CustomExoPlayerView.this.mHasAudio = true;
                            }
                        }
                    }
                } catch (Exception trackException) {
                    Logger.logException(trackException);
                }
            }
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public void onVideoSizeChanged(VideoSize videoSize) {
            Logger.verbose(CustomExoPlayerView.TAG, "Setting video size: [" + videoSize.width + "x" + videoSize.height + "]");
            CustomExoPlayerView customExoPlayerView = CustomExoPlayerView.this;
            CustomTextureView customTextureView = customExoPlayerView.mTextureView;
            if (customTextureView != null) {
                customTextureView.setVideoSize(videoSize.width, videoSize.height);
            } else {
                customExoPlayerView.mSimpleTextureView.setVideoSize(videoSize.width, videoSize.height);
            }
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onVolumeChanged(float volume) {
        }
    }

    class PhotoTapClickListener implements View.OnClickListener {

        final /* synthetic */ com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher.OnPhotoTapListener photoTapListener;

        PhotoTapClickListener(com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher.OnPhotoTapListener photoTapListener) {
            this.photoTapListener = photoTapListener;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            this.photoTapListener.onPhotoTap(view, 0.0f, 0.0f);
        }
    }

    class LongClickWrapper implements View.OnLongClickListener {

        final /* synthetic */ View.OnLongClickListener longClickListener;

        LongClickWrapper(View.OnLongClickListener onLongClickListener) {
            this.longClickListener = onLongClickListener;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            this.longClickListener.onLongClick(view);
            return true;
        }
    }

    public CustomExoPlayerView(Context context, boolean zoomable) {
        super(context);
        this.mZoomable = zoomable;
        init();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliverMessageUpdate(String str) {
        Logger.verbose(str);
        OnUpdateListener onUpdateListener = this.mUpdateListener;
        if (onUpdateListener != null) {
            onUpdateListener.onUpdate("\tExoPlayer: " + str);
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public int getDuration() {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            return (int) interfaceC0144ExoPlayer.getDuration();
        }
        return 0;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public int getProgress() {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            return (int) interfaceC0144ExoPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public String getSource() {
        return this.mSource;
    }

    public boolean hasAudio() {
        if (this.mPlayer != null) {
            return this.mHasAudio;
        }
        return false;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public boolean hasPathSet() {
        return !com.resync.app.util.TextUtils.isEmpty(this.mSource);
    }

    protected void init() {
        if (SettingsSingleton.getSettings().zoomVideo) {
            CustomTextureView customTextureView = new CustomTextureView(getContext());
            this.mTextureView = customTextureView;
            addView(customTextureView, new FrameLayout.LayoutParams(-1, -1));
            this.mTextureView.enableZoom(this.mZoomable);
            return;
        }
        this.mSimpleTextureView = new SimpleTextureView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        addView(this.mSimpleTextureView, layoutParams);
    }

    protected void initPlayer(boolean playWhenReady) {
        DefaultTrackSelector defaultTrackSelector;
        MediaSource mediaSourceCreateMediaSource;
        if (com.resync.app.util.TextUtils.isEmpty(this.mSource)) {
            return;
        }
        deliverMessageUpdate("Source: " + this.mSource);
        if (this.mSource.contains("v.redd.it")) {
            ExoTrackSelection.Factory trackSelectionFactory = (this.mHd || SettingsSingleton.getSettings().redditVideoQuality == 0) ? new BestVideoTrackSelection.Factory() : (SettingsSingleton.getSettings().redditVideoQuality == 1 && ConnectivityHelper.isWifiConnected()) ? new BestVideoTrackSelection.Factory() : new InlineVideoTrackSelection.Factory();
            defaultTrackSelector = new DefaultTrackSelector(getContext(), trackSelectionFactory);
        } else {
            defaultTrackSelector = new DefaultTrackSelector();
        }
        DefaultLoadControl defaultLoadControlCreateDefaultLoadControl = new DefaultLoadControl.Builder().createDefaultLoadControl();
        ExoPlayer.Builder builder = new ExoPlayer.Builder(getContext());
        builder.setTrackSelector(defaultTrackSelector);
        builder.setLoadControl(defaultLoadControlCreateDefaultLoadControl);
        ExoPlayer interfaceC0144ExoPlayerBuild = builder.build();
        this.mPlayer = interfaceC0144ExoPlayerBuild;
        this.mHasAudio = false;
        CustomTextureView customTextureView = this.mTextureView;
        if (customTextureView != null) {
            interfaceC0144ExoPlayerBuild.setVideoTextureView(customTextureView);
        } else {
            interfaceC0144ExoPlayerBuild.setVideoTextureView(this.mSimpleTextureView);
        }
        if (LinkTypeHelper.isRedditVideoHls(this.mSource)) {
            mediaSourceCreateMediaSource = new HlsMediaSource.Factory(ExoplayerFactorySingleton.getInstance().getDataSourceFactory()).createMediaSource(MediaItem.fromUri(this.mSource));
        } else if (LinkTypeHelper.isRedditVideoDash(this.mSource)) {
            deliverMessageUpdate("Starting streaming reddit video playback");
            OkHttpDataSource.Factory factory = new OkHttpDataSource.Factory(OkHttpHelper.getManifestCacheClient());
            OkHttpDataSource.Factory factory2 = new OkHttpDataSource.Factory(OkHttpHelper.getUserAgentClient());
            CacheDataSource.Factory factory3 = new CacheDataSource.Factory();
            factory3.setCache(SimpleCacheSingleton.getInstance().getCache());
            factory3.setUpstreamDataSourceFactory(factory2);
            mediaSourceCreateMediaSource = new DashMediaSource.Factory(new DefaultDashChunkSource.Factory(factory3, 1), factory).createMediaSource(MediaItem.fromUri(this.mSource));
        } else if (this.mSource.startsWith("http")) {
            deliverMessageUpdate("Starting generic streaming video playback");
            OkHttpDataSource.Factory factory4 = new OkHttpDataSource.Factory(OkHttpHelper.getUserAgentClient());
            CacheDataSource.Factory factory5 = new CacheDataSource.Factory();
            factory5.setCache(SimpleCacheSingleton.getInstance().getCache());
            factory5.setUpstreamDataSourceFactory(factory4);
            mediaSourceCreateMediaSource = new ProgressiveMediaSource.Factory(factory5).createMediaSource(MediaItem.fromUri(this.mSource));
        } else {
            deliverMessageUpdate("Starting cached video playback");
            mediaSourceCreateMediaSource = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "Sync for reddit"), (TransferListener) null)).createMediaSource(MediaItem.fromUri(this.mSource));
        }
        this.mPlayer.addListener(new PlayerListener());
        this.mPlayer.setRepeatMode(2);
        OkHttpHelper.getUserAgentClient().dispatcher().cancelAll();
        this.mPlayer.prepare(mediaSourceCreateMediaSource);
        if (this.mMute) {
            mute();
        }
        if (playWhenReady) {
            this.mPlayer.setPlayWhenReady(true);
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public boolean isVideoPlaying() {
        return this.mPlayer != null;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void mute() {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            interfaceC0144ExoPlayer.setVolume(0.0f);
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void onDestroy() {
        this.mStartListener = null;
        this.mErrorListener = null;
        this.mUpdateListener = null;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void onResume(boolean paused) {
        Logger.verbose(TAG, "Resuming");
        initPlayer(!paused);
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public long onStop() {
        String str = TAG;
        Logger.verbose(str, "Stopping");
        if (this.mPlayer == null) {
            Logger.verbose(str, "Failed to stop");
            return 0L;
        }
        Logger.verbose(str, "Now stopping");
        long currentPosition = this.mPlayer.getCurrentPosition();
        this.mPlayer.stop();
        this.mPlayer.clearVideoSurface();
        Logger.verbose(str, "Releasing resources");
        this.mPlayer.release();
        CustomTextureView customTextureView = this.mTextureView;
        if (customTextureView != null) {
            customTextureView.setSurfaceTextureListener(null);
        } else {
            this.mSimpleTextureView.setSurfaceTextureListener(null);
        }
        this.mPlayer = null;
        return currentPosition;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void pause() {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            interfaceC0144ExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void restart() {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            interfaceC0144ExoPlayer.seekTo(0L);
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void resume() {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            interfaceC0144ExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void seekTo(int positionMs) {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            interfaceC0144ExoPlayer.seekTo(positionMs);
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void setMuted(boolean muted) {
        this.mMute = muted;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mErrorListener = onErrorListener;
    }

    public void setOnPhotoLongPressListener(View.OnLongClickListener onLongClickListener) {
        CustomTextureView customTextureView = this.mTextureView;
        if (customTextureView != null) {
            customTextureView.setVideoLongClickListener(onLongClickListener);
        } else {
            setOnLongClickListener(new LongClickWrapper(onLongClickListener));
        }
    }

    public void setOnPhotoTapListener(com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher.OnPhotoTapListener photoTapListener) {
        CustomTextureView customTextureView = this.mTextureView;
        if (customTextureView != null) {
            customTextureView.setPhotoTapListener(photoTapListener);
        } else {
            setOnClickListener(new PhotoTapClickListener(photoTapListener));
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void setOnStartListener(OnStartListener onStartListener) {
        this.mStartListener = onStartListener;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.mUpdateListener = onUpdateListener;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void setSource(String source, boolean hd, boolean muted) {
        this.mSource = source;
        this.mHd = hd;
        setMuted(muted);
        Logger.verbose("Source: " + source);
        initPlayer(false);
    }

    public void setSpeed(float speed) {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            interfaceC0144ExoPlayer.setPlaybackParameters(new PlaybackParameters(speed, 1.0f));
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public boolean shouldDisplayHdLogo() {
        if (this.mHd || TextUtils.isEmpty(getSource()) || !getSource().contains("v.redd.it") || SettingsSingleton.getSettings().redditVideoQuality == 0) {
            return false;
        }
        return (SettingsSingleton.getSettings().redditVideoQuality == 1 && ConnectivityHelper.isWifiConnected()) ? false : true;
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void start() {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            interfaceC0144ExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override // com.resync.app.ui.views.video.NewVideoInterface
    public void unmute() {
        ExoPlayer interfaceC0144ExoPlayer = this.mPlayer;
        if (interfaceC0144ExoPlayer != null) {
            interfaceC0144ExoPlayer.setVolume(1.0f);
        }
    }

    public CustomExoPlayerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.ZOOMABLE_VIDEO);
        this.mZoomable = typedArrayObtainStyledAttributes.getBoolean(0, false);
        typedArrayObtainStyledAttributes.recycle();
        init();
    }
}
