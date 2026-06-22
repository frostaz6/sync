package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.util.TextUtils;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.o0 */
/* JADX INFO: compiled from: OAuthSaveRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthSaveRequest extends RedditRequest<Void> {
    public OAuthSaveRequest(Context context, String str, String str2, boolean saving, String str3) {
        super(context, 1, buildUrl(context, saving), null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", str + "_" + str2);
        if (TextUtils.isEmpty(str3)) {
            return;
        }
        this.params.put("category", str3);
    }

    public static String buildUrl(Context context, boolean saving) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(SettingsSingleton.getOauthUrl(context));
        if (saving) {
            sb2.append("api/unsave");
        } else {
            sb2.append("api/save");
        }
        return sb2.toString();
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
