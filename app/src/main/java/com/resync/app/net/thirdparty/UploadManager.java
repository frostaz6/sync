package com.resync.app.net.thirdparty;

import android.content.Context;
import android.net.Uri;
import com.android.volley.Response;
import com.resync.app.RedditApplication;

/* JADX INFO: renamed from: u8.d */
/* JADX INFO: compiled from: UploadManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class UploadManager {
    public static void upload(Context context, Uri uri, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        // ReSync: the original code gated imgur uploads behind a remote kill-switch
        // (CheckUploadStatusRequest -> the dev's GitHub api/upload.json). That repo dependency is gone;
        // enqueue the imgur upload directly.
        RedditApplication.thirdPartyRequestQueue.add(new UploadImgurRequest(context, uri, listener, errorListener));
    }
}
