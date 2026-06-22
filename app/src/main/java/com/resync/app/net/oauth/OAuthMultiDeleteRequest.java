package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.b0 */
/* JADX INFO: compiled from: OAuthMultiDeleteRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMultiDeleteRequest extends RedditRequest<Void> {
    public OAuthMultiDeleteRequest(Context context, String str, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 3, buildUrl(context, str), listener, errorListener);
    }

    public static String buildUrl(Context context, String str) {
        return SettingsSingleton.getOauthUrl(context) + "api/multi/user/" + AccountSingleton.getInstance().getUsername() + "/m/" + str;
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
