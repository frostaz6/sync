package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.n0 */
/* JADX INFO: compiled from: OAuthRulesRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthRulesRequest extends RedditRequest<String> {
    public OAuthRulesRequest(Context context, String str, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, str), listener, errorListener);
        setShouldCache(false);
    }

    public static String buildUrl(Context context, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(SettingsSingleton.getOauthUrl(context));
        stringBuffer.append("r/");
        stringBuffer.append(str);
        stringBuffer.append("/about/rules.json");
        return stringBuffer.toString();
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONObject(new String(networkResponse.data)).getJSONArray("rules");
            StringBuilder sb2 = new StringBuilder();
            int index = 0;
            while (index < jSONArray.length()) {
                JSONObject jSONObject = jSONArray.getJSONObject(index);
                sb2.append("# ");
                sb2.append(jSONObject.getString("short_name"));
                sb2.append("\n");
                sb2.append("\n");
                sb2.append(jSONObject.getString("description"));
                index++;
                if (index < jSONArray.length()) {
                    sb2.append("\n");
                    sb2.append("\n");
                }
            }
            return Response.success(sb2.toString(), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
