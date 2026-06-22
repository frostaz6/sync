package com.google.android.youtube.player;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/* Stub: public API surface preserved; thumbnail loading is a no-op
 * (internal SDK classes were removed). */
public final class YouTubeThumbnailView extends ImageView {

    public interface OnInitializedListener {
        void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader);

        void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult);
    }

    public YouTubeThumbnailView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }
}
