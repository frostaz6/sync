package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.auth.OAuthLoginHelper;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: b9.b */
/* JADX INFO: compiled from: OAuthLoggedOutTokenExchangeRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthLoggedOutTokenExchangeRequest extends RedditRequest<String> {
    private final RedditRequest wrappedRequest;

    public OAuthLoggedOutTokenExchangeRequest(Context context, RedditRequest redditRequest) {
        super(context, 1, "https://ssl.reddit.com/api/v1/access_token", null, null);
        this.wrappedRequest = redditRequest;
        if (redditRequest.getTag() != null) {
            setTag(redditRequest.getTag());
        }
        setShouldCache(true);
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
        return ("grant_type=https://oauth.reddit.com/grants/installed_client&device_id=" + UUID.randomUUID().toString()).getBytes();
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
            String str = new String(networkResponse.data);
            Logger.verbose("RESPONSE: " + str);
            return Response.success(new JSONObject(str).getString("access_token"), CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public void deliverResponse(String str) {
        RedditRequest redditRequest = this.wrappedRequest;
        if (redditRequest != null) {
            redditRequest.setToken(str);
            RedditApplication.oauthRequestQueue.add(this.wrappedRequest);
        }
    }
}
