package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.RedditRequest;
import com.resync.app.net.RedditSubmitRetryPolicy;

/* JADX INFO: renamed from: y8.z */
/* JADX INFO: compiled from: OAuthMessageRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMessageRequest extends RedditRequest<Boolean> {
    public OAuthMessageRequest(Context context, String str, String str2, String str3, String str4, String str5, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/compose", listener, errorListener);
        HashMap map = new HashMap();
        this.params = map;
        map.put("to", str);
        this.params.put("subject", str2);
        this.params.put("text", str3);
        this.params.put("api_type", "json");
        if (str4 != null) {
            this.params.put("iden", str4);
            this.params.put("captcha", str5);
        }
        setRetryPolicy(new RedditSubmitRetryPolicy());
    }

    @Override // com.android.volley.Request
    public Response<Boolean> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONObject jSONObject = new JSONObject(new String(networkResponse.data)).getJSONObject("json");
            return jSONObject.getJSONArray("errors").length() > 0 ? Response.error(new VolleyError(jSONObject.getJSONArray("errors").getJSONArray(0).getString(1))) : Response.success(Boolean.TRUE, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException unused) {
            return Response.success(Boolean.FALSE, HttpHeaderParser.parseCacheHeaders(networkResponse));
        }
    }
}
