package com.resync.app.net.oauth;

import android.content.ContentValues;
import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.h */
/* JADX INFO: compiled from: OAuthDeleteCommentRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthDeleteCommentRequest extends RedditRequest<Void> {
    public OAuthDeleteCommentRequest(Context context, String str) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/del", null, null);
        this.params = new HashMap();
        ContentValues contentValues = new ContentValues();
        contentValues.put("author", "[deleted]");
        contentValues.put("body_processed", "[deleted]");
        contentValues.put("body_raw", "[deleted]");
        contentValues.put("author_flair_text", "");
        contentValues.put("link_flair_text", "");
        context.getContentResolver().update(RedditProvider.URI_BROAD_COMMENT_UPDATE, contentValues, str, null);
        context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        this.params.put("id", "t1_" + str);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
