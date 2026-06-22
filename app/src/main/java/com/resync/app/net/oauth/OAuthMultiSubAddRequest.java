package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import org.json.JSONObject;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.d0 */
/* JADX INFO: compiled from: OAuthMultiSubAddRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMultiSubAddRequest extends RedditRequest<Void> {
    public OAuthMultiSubAddRequest(Context context, String str, String str2, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 2, buildUrl(context, str, str2), listener, errorListener);
        this.params = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", str2);
            this.params.put("model", jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String buildUrl(Context context, String str, String str2) {
        return SettingsSingleton.getOauthUrl(context) + "api/multi/user/" + AccountSingleton.getInstance().getUsername() + "/m/" + str + "/r/" + str2;
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public VolleyError parseNetworkError(VolleyError volleyError) {
        try {
            Logger.verbose("Error: " + new String(volleyError.networkResponse.data));
        } catch (Exception e) {
            Logger.logException(e);
        }
        return super.parseNetworkError(volleyError);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        Logger.verbose(new String(networkResponse.data));
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
