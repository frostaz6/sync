package com.resync.app.widgets;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.exoplayer2.DeviceInfo;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.text.CueGroup;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.video.VideoSize;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.CustomSurfaceView;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.OkHttpHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.media.video.ExoplayerUtils;

/* JADX INFO: renamed from: lc.f */
/* JADX INFO: compiled from: SimpleExoPlayerTextureView.java */
/* JADX INFO: loaded from: classes3.dex */
public class SimpleExoPlayerTextureView extends FrameLayout {

    private CustomSurfaceView surfaceView;
    private ExoPlayer player;
    private AppCompatImageView backButton;

    private AppCompatImageView controlsButton;

    private AppCompatImageView volumeButton;

    private TextView timeText;
    private boolean controlsVisible;

    private String currentUrl;
    private Player.Listener playerListener;
    private StringBuilder stringBuilder;
    private Formatter formatter;

    private TimeUpdateRunnable timeUpdater;

    /* JADX INFO: renamed from: lc.f$a */
    /* JADX INFO: compiled from: SimpleExoPlayerTextureView.java */
    class PlayerEventListener implements Player.Listener {
        PlayerEventListener() {
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
        public /* synthetic */ void onMaxSeekToPreviousPositionChanged(long j) {
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
        public /* synthetic */ void onPlaybackSuppressionReasonChanged(int reason) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPlayerError(PlaybackException playbackException) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onPlayerErrorChanged(PlaybackException playbackException) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (SimpleExoPlayerTextureView.this.timeUpdater != null) {
                SimpleExoPlayerTextureView.this.timeUpdater.stop();
                SimpleExoPlayerTextureView simpleExoPlayerTextureView = SimpleExoPlayerTextureView.this;
                simpleExoPlayerTextureView.removeCallbacks(simpleExoPlayerTextureView.timeUpdater);
            }
            if (playWhenReady) {
                SimpleExoPlayerTextureView simpleExoPlayerTextureView2 = SimpleExoPlayerTextureView.this;
                simpleExoPlayerTextureView2.timeUpdater = simpleExoPlayerTextureView2.new TimeUpdateRunnable();
                SimpleExoPlayerTextureView simpleExoPlayerTextureView3 = SimpleExoPlayerTextureView.this;
                simpleExoPlayerTextureView3.postDelayed(simpleExoPlayerTextureView3.timeUpdater, 1000L);
            }
            if (playbackState == 1 || playbackState == 4 || !playWhenReady) {
                SimpleExoPlayerTextureView.this.setKeepScreenOn(false);
            } else {
                SimpleExoPlayerTextureView.this.setKeepScreenOn(true);
            }
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
            SimpleExoPlayerTextureView.this.animate().setDuration(220L).alpha(1.0f);
            SimpleExoPlayerTextureView.this.updateTimeRemaining();
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onRepeatModeChanged(int repeatMode) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onSeekBackIncrementChanged(long j) {
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onSeekForwardIncrementChanged(long j) {
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
        public void onTracksChanged(com.google.android.exoplayer2.Tracks tracks) {
            boolean hasAudio = false;
            if (tracks != null) {
                try {
                    if (!tracks.isEmpty()) {
                        for (int group = 0; group < tracks.getGroups().size(); group++) {
                            for (int track = 0; track < tracks.getGroups().get(group).length; track++) {
                                String mimeType = tracks.getGroups().get(group).getTrackFormat(track).sampleMimeType;
                                if (mimeType != null && mimeType.contains("audio")) {
                                    hasAudio = true;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.logException(e);
                }
            }
            if (hasAudio) {
                SimpleExoPlayerTextureView.this.volumeButton.setVisibility(0);
                ((FrameLayout.LayoutParams) SimpleExoPlayerTextureView.this.timeText.getLayoutParams()).leftMargin = com.resync.app.helpers.SizeHelper.dpToPx(118);
            } else {
                SimpleExoPlayerTextureView.this.volumeButton.setVisibility(8);
                ((FrameLayout.LayoutParams) SimpleExoPlayerTextureView.this.timeText.getLayoutParams()).leftMargin = com.resync.app.helpers.SizeHelper.dpToPx(82);
            }
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public void onVideoSizeChanged(VideoSize videoSize) {
            SimpleExoPlayerTextureView.this.surfaceView.getLayoutParams().width = (int) (videoSize.width * ((float) SimpleExoPlayerTextureView.this.getHeight() / videoSize.height));
        }

        @Override // com.google.android.exoplayer2.Player.Listener
        public /* synthetic */ void onVolumeChanged(float volume) {
        }
    }

    /* JADX INFO: renamed from: lc.f$b */
    /* JADX INFO: compiled from: SimpleExoPlayerTextureView.java */
    class TimeUpdateRunnable implements Runnable {

        boolean running = true;

        public TimeUpdateRunnable() {
        }

        public void stop() {
            this.running = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SimpleExoPlayerTextureView.this.player == null || !this.running) {
                return;
            }
            SimpleExoPlayerTextureView.this.updateTimeRemaining();
            stop();
            SimpleExoPlayerTextureView simpleExoPlayerTextureView = SimpleExoPlayerTextureView.this;
            simpleExoPlayerTextureView.timeUpdater = simpleExoPlayerTextureView.new TimeUpdateRunnable();
            SimpleExoPlayerTextureView simpleExoPlayerTextureView2 = SimpleExoPlayerTextureView.this;
            simpleExoPlayerTextureView2.postDelayed(simpleExoPlayerTextureView2.timeUpdater, 1000L);
        }
    }

    public SimpleExoPlayerTextureView(Context context) {
        super(context);
        this.stringBuilder = new StringBuilder();
        this.formatter = new Formatter(this.stringBuilder, Locale.getDefault());
        init();
    }

    private void initPlayer() {
        this.player = ExoplayerUtils.createPlayer(getContext());
        this.playerListener = new PlayerEventListener();
        this.player.setForegroundMode(true);
        this.player.setVideoSurfaceView(this.surfaceView);
        this.player.setRepeatMode(2);
    }

    private String formatTime(int millis) {
        int totalSeconds = millis / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        this.stringBuilder.setLength(0);
        return hours > 0 ? this.formatter.format("%d:%02d:%02d", Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)).toString() : this.formatter.format("%02d:%02d", Integer.valueOf(minutes), Integer.valueOf(seconds)).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTimeRemaining() {
        this.timeText.setText(formatTime((int) (this.player.getDuration() - this.player.getCurrentPosition())));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onBackClick(View view) {
        restart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onVolumeClick(View view) {
        toggleMute();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onControlsClick(View view) {
        togglePlayPause();
    }

    private void restart() {
        this.player.seekTo(0L);
        TimeUpdateRunnable runnable = this.timeUpdater;
        if (runnable != null) {
            runnable.stop();
            removeCallbacks(this.timeUpdater);
        }
    }

    private void togglePlayPause() {
        if (this.controlsButton.isSelected()) {
            this.controlsButton.setSelected(false);
            this.controlsButton.setImageResource(R.drawable.ic_pause_white_24dp);
            this.player.setPlayWhenReady(true);
        } else {
            this.controlsButton.setSelected(true);
            this.controlsButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            this.player.setPlayWhenReady(false);
        }
    }

    private void toggleMute() {
        if (this.player.getVolume() == 0.0f) {
            this.volumeButton.setImageResource(R.drawable.ic_volume_up_white_24dp);
            this.player.setVolume(1.0f);
        } else {
            this.volumeButton.setImageResource(R.drawable.ic_volume_mute_white_24dp);
            this.player.setVolume(0.0f);
        }
    }

    private void resetControls() {
        this.volumeButton.setImageResource(R.drawable.ic_volume_mute_white_24dp);
        this.controlsButton.setSelected(false);
        this.controlsButton.setImageResource(R.drawable.ic_pause_white_24dp);
    }

    public int getCurrentPosition() {
        ExoPlayer interfaceC0144ExoPlayer = this.player;
        if (interfaceC0144ExoPlayer != null) {
            return (int) interfaceC0144ExoPlayer.getCurrentPosition();
        }
        return 0;
    }

    protected void init() {
        View.inflate(getContext(), R.layout.view_inline_exoplayer, this);
        findViewById(R.id.control_wrapper).setBackground(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{-1442840576, 1996488704, 0}));
        this.surfaceView = (CustomSurfaceView) findViewById(R.id.surface_view);
        this.backButton = (AppCompatImageView) findViewById(R.id.back);
        this.timeText = (TextView) findViewById(R.id.time);
        this.controlsButton = (AppCompatImageView) findViewById(R.id.controls);
        this.volumeButton = (AppCompatImageView) findViewById(R.id.volume);
        this.backButton.setOnClickListener(new View.OnClickListener() { // from class: lc.e
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SimpleExoPlayerTextureView.this.onBackClick(view);
            }
        });
        this.volumeButton.setOnClickListener(new View.OnClickListener() { // from class: lc.d
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SimpleExoPlayerTextureView.this.onVolumeClick(view);
            }
        });
        this.controlsButton.setOnClickListener(new View.OnClickListener() { // from class: lc.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SimpleExoPlayerTextureView.this.onControlsClick(view);
            }
        });
        initPlayer();
    }

    public boolean isAttachedTo(View view) {
        return view.equals(getParent());
    }

    public void release() {
        stopAndDetach();
        this.player.setForegroundMode(false);
        this.player.release();
        Logger.verbose("SimpleExoPlayerTextureView", "Player released");
    }

    public void stop() {
        TimeUpdateRunnable runnable = this.timeUpdater;
        if (runnable != null) {
            runnable.stop();
            this.timeUpdater = null;
        }
        if (this.player.isPlaying()) {
            this.player.stop();
            this.player.clearMediaItems();
            this.player.removeListener(this.playerListener);
            Logger.verbose("SimpleExoPlayerTextureView", "Player stopped");
        }
        this.currentUrl = null;
    }

    public void stopAndDetach() {
        stop();
        this.surfaceView.getLayoutParams().width = 1;
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    public void play(String str, boolean muted, boolean showControls) {
        this.controlsVisible = showControls;
        findViewById(R.id.control_wrapper).setVisibility(this.controlsVisible ? 0 : 4);
        if (StringUtils.equals(this.currentUrl, str)) {
            if (this.player.isPlaying()) {
                return;
            }
            this.player.setPlayWhenReady(true);
            return;
        }
        Logger.verbose("SimpleExoPlayerTextureView", "Player starting");
        animate().cancel();
        if (this.player.isPlaying()) {
            stop();
        }
        this.surfaceView.getLayoutParams().width = 1;
        this.currentUrl = str;
        resetControls();
        setAlpha(0.0f);
        OkHttpHelper.getUserAgentClient().dispatcher().cancelAll();
        this.player.setMediaSource(ExoplayerUtils.createMediaSource(str));
        this.player.prepare(ExoplayerUtils.createMediaSource(str), true, false);
        this.player.addListener(this.playerListener);
        this.player.setPlayWhenReady(true);
        if (muted) {
            this.player.setVolume(0.0f);
        }
    }

    public void play(Post post) {
        play(post, true, true);
    }

    public void play(Post post, boolean muted, boolean showControls) {
        String videoUrl = post.getVideoPreview();
        if (SettingsSingleton.getSettings().enhancedAutoPlay) {
            if (StringUtils.contains(post.getUrl(), "redgifs.com")) {
                videoUrl = "https://ap.syncforreddit.com/image?url=redgifs-" + com.resync.app.util.links.LinkHandler.getGfycatId(post.getUrl());
            } else if (StringUtils.contains(post.getUrl(), "gfycat.com")) {
                videoUrl = "https://ap.syncforreddit.com/image?url=gfycat-" + com.resync.app.util.links.LinkHandler.getGfycatId(post.getUrl());
            }
        }
        play(videoUrl, muted, showControls);
    }
}
