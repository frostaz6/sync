package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.helpers.GoogleAnalyticsHelper;
import com.resync.app.net.RedditRequest;
import com.resync.app.net.oauth.OAuthSidebarRequest;

/* JADX INFO: renamed from: a9.e */
/* JADX INFO: compiled from: OAuthSubscribeRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthSubscribeRequest extends RedditRequest<Void> {
    private final String subreddit;

    public OAuthSubscribeRequest(Context context, int subscribeAction, String str, String str2, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/subscribe", listener, errorListener);
        this.subreddit = str;
        HashMap map = new HashMap();
        this.params = map;
        if (subscribeAction == 0) {
            map.put("action", "sub");
            GoogleAnalyticsHelper.trackEvent("RedditStats", "Subscribe", str);
        } else if (subscribeAction == 1) {
            map.put("action", "unsub");
            GoogleAnalyticsHelper.trackEvent("RedditStats", "Unsubscribe", str);
        }
        this.params.put("sr", str2);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        RedditApplication.oauthRequestQueue.getCache().remove(OAuthSidebarRequest.buildUrl(this.context, this.subreddit));
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
