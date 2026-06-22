package com.resync.app.reddit.requests.external.requests.gfycat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONObject;
import com.resync.app.net.LegacyCustomHttpHeaderParser;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: loaded from: classes.dex */
public class GrabGfycatRequest extends Request<String> {

    protected Response.Listener<String> responseListener;
    protected boolean forceFullQuality;

    public GrabGfycatRequest(String str, boolean z, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(0, buildUrl(str), errorListener);
        this.responseListener = listener;
        this.forceFullQuality = z;
        setShouldCache(true);
    }

    static final String buildUrl(String str) {
        return "https://api.gfycat.com/v1/gfycats/" + LinkHandler.getGfycatId(str);
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONObject jSONObject = new JSONObject(new String(networkResponse.data)).getJSONObject("gfyItem");
            String string = (ConnectivityHelper.isWifiConnected() || !jSONObject.has("mobileUrl")) ? jSONObject.getString("mp4Url") : jSONObject.getString("mobileUrl");
            if (this.forceFullQuality) {
                string = jSONObject.getString("mp4Url");
            }
            return Response.success(string, LegacyCustomHttpHeaderParser.parseCacheEntry(networkResponse));
        } catch (Exception unused) {
            return Response.error(new VolleyError("Error parsing"));
        }
    }

    @Override // com.android.volley.Request
    public void deliverResponse(String str) {
        Response.Listener<String> listener = this.responseListener;
        if (listener != null) {
            listener.onResponse(str);
        }
    }
}
