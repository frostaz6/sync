package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: z8.g */
/* JADX INFO: compiled from: OAuthModSpoilerRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthModSpoilerRequest extends RedditRequest<Void> {
    public OAuthModSpoilerRequest(Context context, String str, String str2, boolean markSpoiler) {
        super(context, 1, buildUrl(context, markSpoiler), null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", str + "_" + str2);
    }

    public static String buildUrl(Context context, boolean markSpoiler) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(SettingsSingleton.getOauthUrl(context));
        if (markSpoiler) {
            sb2.append("api/spoiler");
        } else {
            sb2.append("api/unspoiler");
        }
        return sb2.toString();
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.<Void>success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
