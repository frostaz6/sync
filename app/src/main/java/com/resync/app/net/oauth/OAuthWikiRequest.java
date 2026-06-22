package com.resync.app.net.oauth;

import android.content.Context;
import android.text.TextUtils;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.bumptech.glide.RedditHelper;
import com.resync.app.singleton.SettingsSingleton;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.f1 */
/* JADX INFO: compiled from: OAuthWikiRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthWikiRequest extends RedditRequest<String> {
    private final String subreddit;

    public OAuthWikiRequest(Context context, String str, String str2, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, str, str2), listener, errorListener);
        this.subreddit = str;
        setShouldCache(true);
    }

    private static String buildUrl(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return SettingsSingleton.getOauthUrl(context) + "wiki/" + normalizePage(str2) + ".json";
        }
        return SettingsSingleton.getOauthUrl(context) + "r/" + str + "/wiki/" + normalizePage(str2) + ".json";
    }

    private static String normalizePage(String str) {
        return com.resync.app.util.TextUtils.isEmpty(str) ? "index" : str;
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(RedditHelper.processMarkdownToHtml(this.subreddit, new JSONObject(new String(networkResponse.data)).getJSONObject("data").getString("content_md")), CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
