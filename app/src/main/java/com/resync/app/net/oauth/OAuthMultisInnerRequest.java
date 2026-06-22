package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.f0 */
/* JADX INFO: compiled from: OAuthMultisInnerRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMultisInnerRequest extends RedditRequest<String[]> {
    public OAuthMultisInnerRequest(Context context, String str, String str2, Response.Listener<String[]> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, str, str2), listener, errorListener);
    }

    public static String buildUrl(Context context, String str, String str2) {
        return SettingsSingleton.getOauthUrl(context) + "api/multi/user/" + str + "/m/" + str2;
    }

    @Override // com.android.volley.Request
    public Request.Priority getPriority() {
        return Request.Priority.LOW;
    }

    @Override // com.android.volley.Request
    public Response<String[]> parseNetworkResponse(NetworkResponse networkResponse) {
        String str = new String(networkResponse.data);
        Logger.verbose(str);
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONObject("data").getJSONArray("subreddits");
            String[] strArr = new String[jSONArray.length()];
            for (int index = 0; index < jSONArray.length(); index++) {
                strArr[index] = jSONArray.getJSONObject(index).getString("name");
                Logger.verbose("Subreddit: " + strArr[index]);
            }
            return Response.success(strArr, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
