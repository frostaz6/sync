package com.resync.app.net.oauth;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.model.PostFlair;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.j0 */
/* JADX INFO: compiled from: OAuthPostFlairsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthPostFlairsRequest extends RedditRequest<PostFlair[]> {
    public OAuthPostFlairsRequest(String str, Response.Listener<PostFlair[]> listener, Response.ErrorListener errorListener) {
        super(RedditApplication.getAppContext(), 0, buildUrl(str), listener, errorListener);
        setShouldCache(true);
    }

    public static String buildUrl(String str) {
        return "https://oauth.reddit.com/r/" + str + "/api/link_flair.json";
    }

    @Override // com.android.volley.Request
    public Response<PostFlair[]> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONArray(new String(networkResponse.data));
            PostFlair[] postFlairArr = new PostFlair[jSONArray.length()];
            for (int index = 0; index < jSONArray.length(); index++) {
                postFlairArr[index] = new PostFlair();
                postFlairArr[index].text = jSONArray.getJSONObject(index).getString("text");
            }
            return Response.success(postFlairArr, CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new VolleyError("Could not load flairs"));
        }
    }
}
