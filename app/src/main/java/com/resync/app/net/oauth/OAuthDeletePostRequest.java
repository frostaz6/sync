package com.resync.app.net.oauth;

import android.content.ContentValues;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.j */
/* JADX INFO: compiled from: OAuthDeletePostRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthDeletePostRequest extends RedditRequest<Void> {
    public OAuthDeletePostRequest(String str) {
        super(RedditApplication.getAppContext(), 1, SettingsSingleton.getOauthUrl(RedditApplication.getAppContext()) + "api/del", null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", "t3_" + str);
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", str);
        contentValues.put("hidden", (Integer) 1);
        RedditApplication.getAppContext().getContentResolver().update(RedditProvider.URI_UPDATE_HIDDEN, contentValues, null, null);
        RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
