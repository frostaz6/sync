package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import org.json.JSONObject;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.u0 */
/* JADX INFO: compiled from: OAuthSetPrefRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthSetPrefRequest extends RedditRequest<Void> {
    public OAuthSetPrefRequest(Context context, String str, Object obj) {
        super(context, 7, SettingsSingleton.getOauthUrl(context) + "api/v1/me/prefs", null, null);
        this.params = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(str, obj);
            this.params.put("json", jSONObject.toString());
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
