package com.resync.app.ui.views.video;

import com.github.chrisbanes.photoview.animation.OnErrorListener;
import com.github.chrisbanes.photoview.animation.OnStartListener;
import com.github.chrisbanes.photoview.animation.OnUpdateListener;

/* JADX INFO: renamed from: rc.b */
/* JADX INFO: compiled from: NewVideoInterface.java */
/* JADX INFO: loaded from: classes3.dex */
public interface NewVideoInterface {
    int getDuration();

    int getProgress();

    String getSource();

    boolean hasPathSet();

    boolean isVideoPlaying();

    void mute();

    void onDestroy();

    void onResume(boolean resume);

    long onStop();

    void pause();

    void restart();

    void resume();

    void seekTo(int position);

    void setMuted(boolean muted);

    void setOnErrorListener(OnErrorListener onErrorListener);

    void setOnStartListener(OnStartListener onStartListener);

    void setOnUpdateListener(OnUpdateListener onUpdateListener);

    void setSource(String source, boolean autoPlay, boolean muted);

    boolean shouldDisplayHdLogo();

    void start();

    void unmute();
}
