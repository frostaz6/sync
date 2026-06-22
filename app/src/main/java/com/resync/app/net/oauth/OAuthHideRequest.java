package com.resync.app.net.oauth;

import android.content.ContentValues;
import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.model.Kinds;
import com.resync.app.net.RedditRequest;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: y8.u */
/* JADX INFO: compiled from: OAuthHideRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthHideRequest extends RedditRequest<Void> {
    public OAuthHideRequest(Context context, Post post, int type) {
        super(context, 1, buildUrl(context, type), null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", Kinds.toPrefix(3) + "_" + post.getId());
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", post.getId());
        if (type == 0) {
            contentValues.put("hidden", (Integer) 1);
        } else {
            contentValues.put("hidden", (Integer) 0);
        }
        context.getContentResolver().update(RedditProvider.URI_UPDATE_HIDDEN, contentValues, null, null);
        context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    public static String buildUrl(Context context, int type) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(SettingsSingleton.getOauthUrl(context));
        if (type == 0) {
            sb2.append("api/hide");
        } else {
            sb2.append("api/unhide");
        }
        return sb2.toString();
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
