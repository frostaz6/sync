package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.model.Kinds;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.d */
/* JADX INFO: compiled from: OAuthBlockRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthBlockRequest extends RedditRequest<Void> {
    public OAuthBlockRequest(Context context, String str, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 1, buildUrl(context), listener, errorListener);
        setShouldCache(false);
        String str2 = Kinds.toPrefix(2) + "_" + str;
        Logger.verbose("Account ID: " + str2);
        HashMap map = new HashMap();
        this.params = map;
        map.put("account_id", str2);
    }

    public static String buildUrl(Context context) {
        return SettingsSingleton.getOauthUrl(context) + "api/block_user";
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
