package com.resync.app.net;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.errors.OAuthError;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import java.util.Map;
import com.resync.app.net.OAuthRateHelper;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.helpers.GoogleAnalyticsHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: x8.a */
/* JADX INFO: compiled from: RedditRequest.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class RedditRequest<T> extends Request<T> {

    public final String URL_KEY;
    public final String tag;
    protected final Context context;

    protected final Response.Listener<T> responseListener;

    protected String token;

    protected Map<String, String> params;

    public RedditRequest(Context context, int method, String str, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, str, errorListener);
        this.URL_KEY = "URL";
        this.tag = getClass().getSimpleName();
        this.context = context;
        this.responseListener = listener;
        setShouldCache(false);
        Logger.verbose("URL", getUrl());
    }

    public void setToken(String str) {
        this.token = str;
    }

    @Override // com.android.volley.Request
    public void deliverResponse(T t) {
        if (this.responseListener == null || isCanceled()) {
            return;
        }
        this.responseListener.onResponse(t);
    }

    @Override // com.android.volley.Request
    public Map<String, String> getHeaders() throws AuthFailureError {
        return AccountSingleton.getAuthHeaders(this.token);
    }

    @Override // com.android.volley.Request
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }

    public boolean isRateLimited() {
        return !OAuthRateHelper.isWithinRateLimit(getUrl());
    }

    @Override // com.android.volley.Request
    public VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError == null) {
            CrashlyticsHelper.logEvent(6, "Volley", "No error returned!");
            return super.parseNetworkError(volleyError);
        }
        try {
            Logger.verbose("RedditRequest", new String(volleyError.networkResponse.data));
        } catch (Exception unused) {
            Logger.verbose("RedditRequest", "No data returned");
        }
        NetworkResponse networkResponse = volleyError.networkResponse;
        if (networkResponse != null) {
            Map<String, String> map = networkResponse.headers;
            if (map.get("www-authenticate") != null && map.get("www-authenticate").equals("Bearer realm=\"reddit\", error=\"invalid_token\"")) {
                GoogleAnalyticsHelper.trackEvent("Errors", "OAuth", "Invalid token");
                RequestQueue requestQueue = RedditApplication.oauthRequestQueue;
                if (requestQueue != null && requestQueue.getCache() != null) {
                    RedditApplication.mainRequestQueue.getCache().clear();
                }
                return new OAuthError(volleyError);
            }
        }
        return super.parseNetworkError(volleyError);
    }
}
