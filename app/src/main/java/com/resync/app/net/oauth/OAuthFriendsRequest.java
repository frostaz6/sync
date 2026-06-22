package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.managers.UserIconManager;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.p */
/* JADX INFO: compiled from: OAuthFriendsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthFriendsRequest extends RedditRequest<String[]> {
    public OAuthFriendsRequest(Context context, Response.Listener<String[]> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context), listener, errorListener);
        setShouldCache(true);
        HashMap map = new HashMap();
        this.params = map;
        map.put("limit", "100");
    }

    public static String buildUrl(Context context) {
        return SettingsSingleton.getOauthUrl(context) + "api/v1/me/friends";
    }

    @Override // com.android.volley.Request
    public Request.Priority getPriority() {
        return Request.Priority.LOW;
    }

    @Override // com.android.volley.Request
    public Response<String[]> parseNetworkResponse(NetworkResponse networkResponse) {
        String str = new String(networkResponse.data);
        Logger.verbose("FRIENDS: " + str);
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONObject("data").getJSONArray("children");
            String[] strArr = new String[jSONArray.length()];
            ArrayList arrayList = new ArrayList();
            for (int index = 0; index < jSONArray.length(); index++) {
                strArr[index] = jSONArray.getJSONObject(index).getString("name");
                arrayList.add(jSONArray.getJSONObject(index).getString("id"));
            }
            UserIconManager.requestIcons(arrayList);
            return Response.success(strArr, CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
