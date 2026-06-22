package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import com.resync.app.model.Multireddits;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: a9.c */
/* JADX INFO: compiled from: OAuthMultiredditsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMultiredditsRequest extends RedditRequest<Multireddits> {
    public OAuthMultiredditsRequest(Context context, Response.Listener<Multireddits> listener, Response.ErrorListener errorListener) {
        super(context, 0, SettingsSingleton.getOauthUrl(context) + "api/multi/mine?limit=100", listener, errorListener);
    }

    @Override // com.android.volley.Request
    public Response<Multireddits> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONArray(new String(networkResponse.data));
            Multireddits multireddits = new Multireddits();
            multireddits.names = new String[jSONArray.length()];
            for (int index = 0; index < jSONArray.length(); index++) {
                multireddits.names[index] = jSONArray.getJSONObject(index).getJSONObject("data").getString("name");
            }
            return Response.success(multireddits, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
