package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: z8.c */
/* JADX INFO: compiled from: OAuthModDistinguishRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthModDistinguishRequest extends RedditRequest<Void> {
    public OAuthModDistinguishRequest(Context context, String str, String str2, boolean distinguish, boolean sticky) {
        super(context, 1, buildUrl(context), null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", str + "_" + str2);
        if (distinguish) {
            this.params.put("how", "yes");
        } else {
            this.params.put("how", "no");
        }
        if (sticky) {
            this.params.put("sticky", "true");
        }
    }

    public static String buildUrl(Context context) {
        return SettingsSingleton.getOauthUrl(context) + "api/distinguish";
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.<Void>success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
