package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.s */
/* JADX INFO: compiled from: OAuthHasGoldRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthHasGoldRequest extends RedditRequest<Boolean> {
    public OAuthHasGoldRequest(Context context, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        super(context, 0, SettingsSingleton.getOauthUrl(context) + "user/" + AccountSingleton.getInstance().getUsername() + "/about.json", listener, errorListener);
        setShouldCache(true);
    }

    @Override // com.android.volley.Request
    public Response<Boolean> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(Boolean.valueOf(new JSONObject(new String(networkResponse.data)).getJSONObject("data").getBoolean("is_gold")), CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
