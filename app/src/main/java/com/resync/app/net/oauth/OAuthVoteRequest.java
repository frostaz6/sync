package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.e1 */
/* JADX INFO: compiled from: OAuthVoteRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthVoteRequest extends RedditRequest<Void> {
    public OAuthVoteRequest(Context context, int type, String str, int currentDir, int newDir) {
        super(context, 1, buildUrl(context), null, null);
        HashMap map = new HashMap();
        this.params = map;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(type == 0 ? "t3_" : "t1_");
        sb2.append(str);
        map.put("id", sb2.toString());
        this.params.put("dir", String.valueOf(currentDir == newDir ? 0 : newDir));
    }

    public static String buildUrl(Context context) {
        return SettingsSingleton.getOauthUrl(context) + "api/vote";
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
