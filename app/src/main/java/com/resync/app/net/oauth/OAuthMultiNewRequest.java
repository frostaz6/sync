package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.util.TextUtils;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.c0 */
/* JADX INFO: compiled from: OAuthMultiNewRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMultiNewRequest extends RedditRequest<Void> {
    public OAuthMultiNewRequest(Context context, String str, String str2, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 1, buildUrl(context, str), listener, errorListener);
        this.params = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("visibility", "private");
            if (!TextUtils.isEmpty(str2)) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("name", str2);
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(jSONObject2);
                jSONObject.put("subreddits", jSONArray);
            }
            jSONObject.put("display_name", str);
            jSONObject.put("weighting_scheme", "classic");
            jSONObject.put("description_md", "");
            this.params.put("model", jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String buildUrl(Context context, String str) {
        return SettingsSingleton.getOauthUrl(context) + "api/multi/user/" + AccountSingleton.getInstance().getUsername() + "/m/" + str;
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
