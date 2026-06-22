package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.auth.OAuthLoginHelper;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: b9.c */
/* JADX INFO: compiled from: OAuthTempTokenExchangeRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthTempTokenExchangeRequest extends RedditRequest<String> {
    private final RedditRequest wrappedRequest;

    private final String refreshToken;

    public OAuthTempTokenExchangeRequest(Context context, RedditRequest redditRequest, String str) {
        super(context, 1, "https://ssl.reddit.com/api/v1/access_token", null, null);
        this.wrappedRequest = redditRequest;
        this.refreshToken = str;
        if (redditRequest.getTag() != null) {
            setTag(redditRequest.getTag());
        }
        setShouldCache(false);
    }

    @Override // com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        RedditRequest redditRequest = this.wrappedRequest;
        if (redditRequest != null) {
            redditRequest.deliverError(volleyError);
        }
    }

    @Override // com.android.volley.Request
    public byte[] getBody() throws AuthFailureError {
        return ("grant_type=refresh_token&refresh_token=" + this.refreshToken).getBytes();
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = AccountSingleton.getDefaultHeaders();
        headers.put("Authorization", OAuthLoginHelper.basicAuthHeader());
        return headers;
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(new JSONObject(new String(networkResponse.data)).getString("access_token"), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public void deliverResponse(String str) {
        RedditRequest redditRequest = this.wrappedRequest;
        if (redditRequest != null) {
            redditRequest.setToken(str);
            RedditApplication.thirdPartyRequestQueue.add(this.wrappedRequest);
        }
    }
}
