package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.SubredditArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.p0 */
/* JADX INFO: compiled from: OAuthSavedCategoriesRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthSavedCategoriesRequest extends RedditRequest<String[]> {
    public OAuthSavedCategoriesRequest(Context context, Response.Listener<String[]> listener, Response.ErrorListener errorListener) {
        super(context, 0, SettingsSingleton.getOauthUrl(context) + "api/saved_categories", listener, errorListener);
    }

    @Override // com.android.volley.Request
    public Response<String[]> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONObject(new String(networkResponse.data)).getJSONArray("categories");
            SubredditArrayList subredditArrayList = new SubredditArrayList();
            for (int index = 0; index < jSONArray.length(); index++) {
                subredditArrayList.addIfAbsent(jSONArray.getJSONObject(index).getString("category"));
            }
            return Response.success(subredditArrayList.toStringArray(), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
