package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.LiveRedditPreferences;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.r */
/* JADX INFO: compiled from: OAuthGetUserPrefsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthGetUserPrefsRequest extends RedditRequest<LiveRedditPreferences> {
    public OAuthGetUserPrefsRequest(Context context, Response.Listener<LiveRedditPreferences> listener, Response.ErrorListener errorListener) {
        super(context, 0, SettingsSingleton.getOauthUrl(context) + "api/v1/me/prefs", listener, errorListener);
        setShouldCache(false);
    }

    @Override // com.android.volley.Request
    public Response<LiveRedditPreferences> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            JSONObject jSONObject = new JSONObject(str);
            Logger.verbose(str);
            LiveRedditPreferences liveRedditPreferences = new LiveRedditPreferences();
            liveRedditPreferences.mediaPreviewOn = StringUtils.equalsIgnoreCase(jSONObject.optString("media"), "on");
            Logger.verbose("media preview on: " + liveRedditPreferences.mediaPreviewOn);
            liveRedditPreferences.mediaPreviewOff = StringUtils.equalsIgnoreCase(jSONObject.optString("media"), "off");
            Logger.verbose("media preview off: " + liveRedditPreferences.mediaPreviewOff);
            liveRedditPreferences.mediaPreviewSubreddit = StringUtils.equalsIgnoreCase(jSONObject.optString("media"), "subreddit");
            Logger.verbose("media preview subreddit: " + liveRedditPreferences.mediaPreviewSubreddit);
            liveRedditPreferences.compress = jSONObject.optBoolean("compress");
            liveRedditPreferences.searchIncludeOver18 = jSONObject.optBoolean("search_include_over_18");
            liveRedditPreferences.showPresence = jSONObject.optBoolean("show_presence");
            liveRedditPreferences.allowClickTracking = jSONObject.optBoolean("allow_clicktracking");
            return Response.success(liveRedditPreferences, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
