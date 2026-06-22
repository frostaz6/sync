package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Kinds;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.x */
/* JADX INFO: compiled from: OAuthMarkMessageRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMarkMessageRequest extends RedditRequest<Void> {
    public OAuthMarkMessageRequest(Context context, int kind, String str) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/read_message", null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", Kinds.toPrefix(kind) + "_" + str);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        Context context = this.context;
        OAuthRequestManager.send(context, new OAuthServiceMessageCountRequest(context, null, null));
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
