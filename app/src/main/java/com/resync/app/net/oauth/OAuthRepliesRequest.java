package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.model.Kinds;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.l0 */
/* JADX INFO: compiled from: OAuthRepliesRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthRepliesRequest extends RedditRequest<Void> {
    public OAuthRepliesRequest(Context context, int kind, String str, boolean enabled, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/sendreplies", listener, errorListener);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", Kinds.toPrefix(kind) + "_" + str);
        this.params.put("state", Boolean.toString(enabled));
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
