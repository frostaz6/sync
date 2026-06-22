package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.m */
/* JADX INFO: compiled from: OAuthFavRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthFavRequest extends RedditRequest<Void> {
    public OAuthFavRequest(Context context, int type, String str, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/favorite", listener, errorListener);
        HashMap map = new HashMap();
        this.params = map;
        if (type == 0) {
            map.put("make_favorite", "true");
        } else if (type == 1) {
            map.put("make_favorite", "false");
        }
        this.params.put("sr_name", str);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
