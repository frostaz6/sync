package com.google.android.youtube.player;

/* JADX INFO: loaded from: classes2.dex */
public interface YouTubeThumbnailLoader {

    public enum ErrorReason {
        NETWORK_ERROR,
        INTERNAL_ERROR,
        UNKNOWN
    }

    public interface OnThumbnailLoadedListener {
        void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String str);

        void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, ErrorReason errorReason);
    }
}
