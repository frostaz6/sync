package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.y */
/* JADX INFO: compiled from: OAuthMarkThreadRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMarkThreadRequest extends RedditRequest<Void> {
    public OAuthMarkThreadRequest(Context context, String str, Response.ErrorListener errorListener) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/store_visits", null, errorListener);
        HashMap map = new HashMap();
        this.params = map;
        map.put("links", "t3_" + str);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
