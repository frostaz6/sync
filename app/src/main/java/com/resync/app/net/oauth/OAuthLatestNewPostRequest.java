package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.v */
/* JADX INFO: compiled from: OAuthLatestNewPostRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthLatestNewPostRequest extends RedditRequest<String> {
    public OAuthLatestNewPostRequest(Context context, String str, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, str), listener, errorListener);
        setShouldCache(false);
    }

    public static String buildUrl(Context context, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(SettingsSingleton.getOauthUrl(context));
        stringBuffer.append("r/");
        stringBuffer.append(str);
        stringBuffer.append("/new.json");
        return stringBuffer.toString();
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONObject(new String(networkResponse.data)).getJSONObject("data").getJSONArray("children");
            return jSONArray.length() > 0 ? Response.success(jSONArray.getJSONObject(0).getJSONObject("data").getString("id"), HttpHeaderParser.parseCacheHeaders(networkResponse)) : Response.error(new VolleyError("No posts available"));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
