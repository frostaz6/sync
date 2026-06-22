package com.resync.app.net.oauth;

import android.content.Context;
import android.util.Pair;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.AccountSingleton;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.auth.OAuthLoginHelper;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: b9.a */
/* JADX INFO: compiled from: OAuthAccessTokenRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthAccessTokenRequest extends RedditRequest<Pair<String, String>> {
    private final String authCode;

    public OAuthAccessTokenRequest(Context context, String str, Response.Listener<Pair<String, String>> listener, Response.ErrorListener errorListener) {
        super(context, 1, "https://ssl.reddit.com/api/v1/access_token", listener, errorListener);
        this.authCode = str;
    }

    @Override // com.android.volley.Request
    public byte[] getBody() throws AuthFailureError {
        return ("grant_type=authorization_code&code=" + this.authCode + "&redirect_uri=" + URLEncoder.encode(OAuthLoginHelper.redirectUri())).getBytes();
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = AccountSingleton.getDefaultHeaders();
        headers.put("Authorization", OAuthLoginHelper.basicAuthHeader());
        return headers;
    }

    @Override // com.android.volley.Request
    public Response<Pair<String, String>> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            return Response.success(new Pair(new JSONObject(str).getString("access_token"), new JSONObject(str).getString("refresh_token")), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
