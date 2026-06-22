package com.resync.app.net.oauth;

import android.content.Context;
import android.graphics.Color;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.resync.app.singleton.SettingsSingleton;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.managers.SubredditColorManager;
import com.resync.app.managers.SubredditIconManager;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.State;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.util.WebsafeUtils;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: a9.a */
/* JADX INFO: compiled from: OAuthDefaultSubredditsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthDefaultSubredditsRequest extends RedditRequest<State> {
    private final Response.ErrorListener errorListener;

    public OAuthDefaultSubredditsRequest(Context context, String str, Response.Listener<State> listener, Response.ErrorListener errorListener) {
        super(context, 0, SettingsSingleton.getOauthUrl() + "subreddits/default.json" + (str != null ? "?after=" + str : ""), listener, errorListener);
        this.errorListener = errorListener;
        setShouldCache(true);
    }

    @Override // com.android.volley.Request
    public Request.Priority getPriority() {
        return Request.Priority.LOW;
    }

    @Override // com.android.volley.Request
    public Response<State> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            JSONObject jSONObject = new JSONObject(str).getJSONObject("data");
            State state = new State();
            String string = jSONObject.getString("after");
            if (string != null && string.equalsIgnoreCase("null")) {
                string = null;
            }
            JSONArray jSONArray = jSONObject.getJSONArray("children");
            for (int index = 0; index < jSONArray.length(); index++) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(index).getJSONObject("data");
                String string2 = jSONObject2.getString("display_name");
                state.subscribed.addIfAbsent(string2);
                String strOptString = jSONObject2.optString("community_icon");
                if (TextUtils.isEmpty(strOptString)) {
                    String strOptString2 = jSONObject2.optString("icon_img");
                    if (!TextUtils.isEmpty(strOptString2)) {
                        SubredditIconManager.getInstance().setIcon(string2, WebsafeUtils.unescapeHtml(strOptString2));
                    }
                } else {
                    SubredditIconManager.getInstance().setIcon(string2, WebsafeUtils.unescapeHtml(strOptString));
                }
                String strOptString3 = jSONObject2.optString("banner_background_color");
                if (!TextUtils.isEmpty(strOptString3)) {
                    SubredditColorManager.getInstance().setColor(string2, Integer.valueOf(Color.parseColor(strOptString3)));
                }
            }
            if (string == null) {
                state.status = 0;
                return Response.success(state, CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
            }
            state.status = 1;
            Context context = this.context;
            OAuthRequestManager.sendAuthenticated(context, this.token, new OAuthDefaultSubredditsRequest(context, string, this.responseListener, this.errorListener));
            return Response.success(state, CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
