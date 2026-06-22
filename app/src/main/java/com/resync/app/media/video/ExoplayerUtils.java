package com.resync.app.media.video;

import android.content.Context;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.resync.app.singleton.ExoplayerFactorySingleton;
import com.resync.app.util.InlineVideoTrackSelection;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: xc.a */
/* JADX INFO: compiled from: ExoplayerUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public final class ExoplayerUtils {
    public static MediaSource createMediaSource(String str) {
        Logger.verbose("EXOPLAYER SOURCE: " + str);
        return LinkTypeHelper.isRedditVideoDash(str) ? new DashMediaSource.Factory(new DefaultDashChunkSource.Factory(ExoplayerFactorySingleton.getInstance().getHttpDataSourceFactory(), 1), ExoplayerFactorySingleton.getInstance().getDataSourceFactory()).createMediaSource(MediaItem.fromUri(str)) : LinkTypeHelper.isRedditVideoHls(str) ? new HlsMediaSource.Factory(ExoplayerFactorySingleton.getInstance().getDataSourceFactory()).createMediaSource(MediaItem.fromUri(str)) : new ProgressiveMediaSource.Factory(ExoplayerFactorySingleton.getInstance().getDataSourceFactory()).createMediaSource(MediaItem.fromUri(str));
    }

    public static ExoPlayer createPlayer(Context context) {
        DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector(context, new InlineVideoTrackSelection.Factory());
        DefaultLoadControl defaultLoadControlCreateDefaultLoadControl = new DefaultLoadControl.Builder().createDefaultLoadControl();
        DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(context);
        ExoPlayer.Builder builder = new ExoPlayer.Builder(context);
        builder.setTrackSelector(defaultTrackSelector);
        builder.setLoadControl(defaultLoadControlCreateDefaultLoadControl);
        builder.setRenderersFactory(defaultRenderersFactory);
        return builder.build();
    }
}
