package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.e0 */
/* JADX INFO: compiled from: OAuthMultiSubDeleteRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMultiSubDeleteRequest extends RedditRequest<Void> {
    public OAuthMultiSubDeleteRequest(Context context, String str, String str2, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 3, buildUrl(context, str, str2), listener, errorListener);
    }

    public static String buildUrl(Context context, String str, String str2) {
        return SettingsSingleton.getOauthUrl(context) + "api/multi/user/" + AccountSingleton.getInstance().getUsername() + "/m/" + str + "/r/" + str2;
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
