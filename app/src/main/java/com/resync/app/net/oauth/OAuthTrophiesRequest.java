package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.resync.app.singleton.SettingsSingleton;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.model.Trophy;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.z0 */
/* JADX INFO: compiled from: OAuthTrophiesRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthTrophiesRequest extends RedditRequest<Trophy[]> {
    public OAuthTrophiesRequest(Context context, Object obj, String str, Response.Listener<Trophy[]> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, str), listener, errorListener);
        if (obj != null) {
            setTag(obj);
        }
        setShouldCache(true);
    }

    public static String buildUrl(Context context, String str) {
        return SettingsSingleton.getOauthUrl(context) + "api/v1/user/" + str + "/trophies";
    }

    @Override // com.android.volley.Request
    public Response<Trophy[]> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            JSONArray jSONArray = new JSONObject(str).getJSONObject("data").getJSONArray("trophies");
            int length = jSONArray.length();
            Trophy[] trophyArr = new Trophy[length];
            for (int index = 0; index < length; index++) {
                trophyArr[index] = new Trophy();
                trophyArr[index].name = jSONArray.getJSONObject(index).getJSONObject("data").getString("name");
                trophyArr[index].iconUrl = jSONArray.getJSONObject(index).getJSONObject("data").getString("icon_70");
            }
            return Response.success(trophyArr, CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
