package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: z8.b */
/* JADX INFO: compiled from: OAuthModBanRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthModBanRequest extends RedditRequest<Void> {
    public OAuthModBanRequest(Context context, String str, String str2, String str3, String str4, String str5, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 1, buildUrl(context, str2), listener, errorListener);
        HashMap map = new HashMap();
        this.params = map;
        map.put("ban_message", str5);
        this.params.put("note", str3);
        this.params.put("duration", str4);
        this.params.put("name", str);
        this.params.put("type", "banned");
        this.params.put("api_type", "json");
    }

    public static String buildUrl(Context context, String str) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(SettingsSingleton.getOauthUrl(context));
        sb2.append("r/" + str + "/api/friend");
        return sb2.toString();
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.<Void>success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
