package com.resync.app.net.thirdparty;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.LegacyCustomHttpHeaderParser;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: r8.a */
/* JADX INFO: compiled from: CheckDeviantArtRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class CheckDeviantArtRequest extends Request<String> {

    private final Response.Listener<String> responseListener;

    public CheckDeviantArtRequest(String str, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(0, "http://backend.deviantart.com/oembed?url=" + str, errorListener);
        this.responseListener = listener;
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            JSONObject jSONObject = new JSONObject(str);
            return jSONObject.has("fullsize_url") ? Response.success(jSONObject.getString("fullsize_url"), LegacyCustomHttpHeaderParser.parseCacheEntry(networkResponse)) : Response.success(jSONObject.getString("url"), LegacyCustomHttpHeaderParser.parseCacheEntry(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override // com.android.volley.Request
    public void deliverResponse(String str) {
        this.responseListener.onResponse(str);
    }
}
