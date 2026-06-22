package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import org.json.JSONObject;
import com.resync.app.net.RedditRequest;
import com.resync.app.helpers.messaging.MessageCountHelper;

/* JADX INFO: renamed from: y8.t0 */
/* JADX INFO: compiled from: OAuthServiceMessageCountRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthServiceMessageCountRequest extends RedditRequest<Integer> {
    public OAuthServiceMessageCountRequest(Context context, Response.Listener<Integer> listener, Response.ErrorListener errorListener) {
        super(context, 0, SettingsSingleton.getOauthUrl(context) + "api/v1/me", listener, errorListener);
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Integer num) {
        MessageCountHelper.setCount(this.context, num.intValue());
        super.deliverResponse(num);
    }

    @Override // com.android.volley.Request
    public Response<Integer> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(Integer.valueOf(new JSONObject(new String(networkResponse.data)).getInt("inbox_count")), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
