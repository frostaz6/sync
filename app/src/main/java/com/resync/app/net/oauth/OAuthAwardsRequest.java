package com.resync.app.net.oauth;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.model.Award;
import com.resync.app.util.Logger;
import com.resync.app.util.WebsafeUtils;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.c */
/* JADX INFO: compiled from: OAuthAwardsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthAwardsRequest extends RedditRequest<Award[]> {
    public OAuthAwardsRequest(String str, String str2, Response.Listener<Award[]> listener, Response.ErrorListener errorListener) {
        super(RedditApplication.getAppContext(), 0, buildUrl(str, str2), listener, errorListener);
        setShouldCache(true);
    }

    public static String buildUrl(String str, String str2) {
        return "https://oauth.reddit.com/api/info/?id=" + str + str2;
    }

    @Override // com.android.volley.Request
    public Response<Award[]> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONObject(new String(networkResponse.data)).getJSONObject("data").getJSONArray("children").getJSONObject(0).getJSONObject("data").getJSONArray("all_awardings");
            int length = jSONArray.length();
            Award[] awardArr = new Award[length];
            for (int index = 0; index < length; index++) {
                JSONObject jSONObject = jSONArray.getJSONObject(index);
                awardArr[index] = new Award();
                awardArr[index].count = jSONObject.getInt("count");
                awardArr[index].name = jSONObject.getString("name");
                awardArr[index].description = jSONObject.getString("description");
                awardArr[index].iconUrl = jSONObject.getString("icon_url");
                JSONArray resizedIcons = jSONObject.getJSONArray("resized_icons");
                awardArr[index].resizedIconUrl = WebsafeUtils.unescapeHtml(resizedIcons.getJSONObject(resizedIcons.length() - 1).getString("url"));
            }
            return Response.success(awardArr, CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new VolleyError("Could not load gallery"));
        }
    }
}
