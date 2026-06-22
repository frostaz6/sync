package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.model.Kinds;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.m0 */
/* JADX INFO: compiled from: OAuthReportRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthReportRequest extends RedditRequest<Void> {
    public OAuthReportRequest(Context context, int kind, String str, String str2) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/report", null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", Kinds.toPrefix(kind) + "_" + str);
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        this.params.put("reason", str2.toString());
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            Logger.verbose("Report: " + new String(networkResponse.data));
        } catch (Exception unused) {
        }
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
