package com.resync.app.net.thirdparty;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: r8.f */
/* JADX INFO: compiled from: RedditStatusRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class RedditStatusRequest extends Request<Boolean> {

    private Response.Listener<Boolean> responseListener;

    public RedditStatusRequest(Response.Listener<Boolean> listener) {
        super(0, "https://reddit.statuspage.io/api/v2/incidents.json", null);
        this.responseListener = listener;
        setShouldCache(false);
    }

    @Override // com.android.volley.Request
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Boolean bool) {
        this.responseListener.onResponse(bool);
    }

    @Override // com.android.volley.Request
    public Response<Boolean> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONObject(new String(networkResponse.data)).getJSONArray("incidents");
            if (jSONArray.length() <= 0) {
                return Response.success(Boolean.TRUE, HttpHeaderParser.parseCacheHeaders(networkResponse));
            }
            JSONObject jSONObject = jSONArray.getJSONObject(0);
            if (jSONObject.optString("resolved_at") != null && !jSONObject.optString("resolved_at").equalsIgnoreCase("null")) {
                return Response.success(Boolean.TRUE, HttpHeaderParser.parseCacheHeaders(networkResponse));
            }
            return Response.success(Boolean.FALSE, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
