package com.google.android.youtube.player;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ViewGroup;

/* Stub: public API surface preserved; player internals are no-ops.
 * YouTube playback is not wired up (internal SDK classes were removed). */
public final class YouTubePlayerView extends ViewGroup implements YouTubePlayer.Provider {

    private final OnViewLifecycleListener viewLifecycleListener;
    private Bundle savedState;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private YouTubePlayer.Provider provider;

    interface OnViewLifecycleListener {
        void onViewCreated(YouTubePlayerView youTubePlayerView);

        void onViewInitialize(YouTubePlayerView youTubePlayerView, String str, YouTubePlayer.OnInitializedListener onInitializedListener);
    }

    public YouTubePlayerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public YouTubePlayerView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        this(context, attributeSet, defStyleAttr, context instanceof YouTubeBaseActivity ? ((YouTubeBaseActivity) context).getViewLifecycleListener() : null);
        if (!(context instanceof YouTubeBaseActivity)) {
            throw new IllegalStateException("A YouTubePlayerView can only be created with an Activity  which extends YouTubeBaseActivity as its context.");
        }
    }

    YouTubePlayerView(Context context, AttributeSet attributeSet, int defStyleAttr, OnViewLifecycleListener listener) {
        super(context, attributeSet, defStyleAttr);
        this.viewLifecycleListener = listener;
        if (getBackground() == null) {
            setBackgroundColor(-16777216);
        }
    }

    final void onStart() {
    }

    final void initializeInternal(Activity activity, YouTubePlayer.Provider provider, String str, YouTubePlayer.OnInitializedListener onInitializedListener, Bundle bundle) {
        this.provider = provider;
        this.onInitializedListener = onInitializedListener;
        this.savedState = bundle;
        // No internal SDK available: report initialization failure so callers
        // can fall back gracefully instead of hanging.
        if (onInitializedListener != null) {
            onInitializedListener.onInitializationFailure(provider, YouTubeInitializationResult.SERVICE_MISSING);
        }
    }

    final void setManageAudioFocus(boolean z) {
    }

    final void onResume() {
    }

    final void onActivityDestroyed(boolean z) {
    }

    final void onPause() {
    }

    final void release(boolean z) {
    }

    final void onStop() {
    }

    final Bundle getSavedState() {
        return this.savedState;
    }

    @Override // android.view.ViewGroup
    protected final void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() > 0) {
            getChildAt(0).layout(0, 0, right - left, bottom - top);
        }
    }

    public final void initialize(String str, YouTubePlayer.OnInitializedListener onInitializedListener) {
        if (this.viewLifecycleListener != null) {
            this.viewLifecycleListener.onViewInitialize(this, str, onInitializedListener);
        } else if (onInitializedListener != null) {
            onInitializedListener.onInitializationFailure(this, YouTubeInitializationResult.SERVICE_MISSING);
        }
    }
}
