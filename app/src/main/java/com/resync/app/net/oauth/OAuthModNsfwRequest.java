package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: z8.e */
/* JADX INFO: compiled from: OAuthModNsfwRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthModNsfwRequest extends RedditRequest<Void> {
    public OAuthModNsfwRequest(Context context, String str, String str2, boolean marked) {
        super(context, 1, buildUrl(context, marked), null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", str + "_" + str2);
    }

    public static String buildUrl(Context context, boolean marked) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(SettingsSingleton.getOauthUrl(context));
        if (marked) {
            sb2.append("api/marknsfw");
        } else {
            sb2.append("api/unmarknsfw");
        }
        return sb2.toString();
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.<Void>success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
