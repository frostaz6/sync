package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.w */
/* JADX INFO: compiled from: OAuthMarkAllMessagesViewedRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMarkAllMessagesViewedRequest extends RedditRequest<Void> {
    public OAuthMarkAllMessagesViewedRequest(Context context, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/read_all_messages", listener, errorListener);
    }

    @Override // com.android.volley.Request
    public byte[] getBody() throws AuthFailureError {
        return "".getBytes();
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        Context context = this.context;
        OAuthRequestManager.send(context, new OAuthServiceMessageCountRequest(context, null, null));
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
