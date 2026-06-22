package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.model.Message;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.i */
/* JADX INFO: compiled from: OAuthDeleteMessageRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthDeleteMessageRequest extends RedditRequest<Void> {
    public OAuthDeleteMessageRequest(Context context, Message message) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/del_msg", null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", "t4_" + message.id);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
