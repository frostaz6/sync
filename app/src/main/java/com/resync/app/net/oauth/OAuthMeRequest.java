package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.resync.app.singleton.SettingsSingleton;
import org.json.JSONObject;
import com.resync.app.net.LegacyCustomHttpHeaderParser;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: a9.b */
/* JADX INFO: compiled from: OAuthMeRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMeRequest extends RedditRequest<String> {
    public OAuthMeRequest(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, 0, SettingsSingleton.getOauthUrl(context) + "api/v1/me", listener, errorListener);
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(new JSONObject(new String(networkResponse.data)).getString("name"), LegacyCustomHttpHeaderParser.parseCacheEntry(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
