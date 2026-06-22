package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: z8.f */
/* JADX INFO: compiled from: OAuthModRemoveRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthModRemoveRequest extends RedditRequest<Void> {
    public OAuthModRemoveRequest(Context context, String str, String str2, boolean removeAsSpam) {
        super(context, 1, buildUrl(context), null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", str + "_" + str2);
        this.params.put("spam", Boolean.toString(removeAsSpam));
    }

    public static String buildUrl(Context context) {
        return SettingsSingleton.getOauthUrl(context) + "api/remove";
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.<Void>success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
