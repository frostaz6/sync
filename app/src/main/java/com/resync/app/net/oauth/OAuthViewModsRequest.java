package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.d1 */
/* JADX INFO: compiled from: OAuthViewModsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthViewModsRequest extends RedditRequest<String[]> {
    private final String subreddit;

    public OAuthViewModsRequest(Context context, String str, Response.Listener<String[]> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, str), listener, errorListener);
        this.subreddit = str.toLowerCase(Locale.ENGLISH);
    }

    public static String buildUrl(Context context, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(SettingsSingleton.getOauthUrl(context));
        stringBuffer.append("r/");
        stringBuffer.append(str);
        stringBuffer.append("/about/moderators");
        stringBuffer.append(".json");
        return stringBuffer.toString();
    }

    @Override // com.android.volley.Request
    public Response<String[]> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONObject(new String(networkResponse.data)).getJSONObject("data").getJSONArray("children");
            ArrayList arrayList = new ArrayList();
            for (int index = 0; index < jSONArray.length(); index++) {
                arrayList.add(jSONArray.getJSONObject(index).getString("name"));
            }
            String[] strArr = new String[arrayList.size()];
            arrayList.toArray(strArr);
            return Response.success(strArr, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
