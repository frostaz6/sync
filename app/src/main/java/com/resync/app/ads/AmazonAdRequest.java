package com.resync.app.ads;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: e7.b */
/* JADX INFO: compiled from: AmazonAdRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class AmazonAdRequest extends Request<AmazonAd[]> {

    private Response.Listener<AmazonAd[]> responseListener;

    public AmazonAdRequest(Response.Listener<AmazonAd[]> listener) {
        super(0, "https://us-central1-ad-server-f1010.cloudfunctions.net/getAmazonAd", null);
        this.responseListener = listener;
        setShouldCache(false);
    }

    @Override // com.android.volley.Request
    public void deliverResponse(AmazonAd[] amazonAdArr) {
        this.responseListener.onResponse(amazonAdArr);
    }

    @Override // com.android.volley.Request
    public Response<AmazonAd[]> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONArray(new String(networkResponse.data));
            int length = jSONArray.length();
            AmazonAd[] amazonAdArr = new AmazonAd[length];
            for (int i = 0; i < length; i++) {
                amazonAdArr[i] = new AmazonAd(jSONArray.getJSONObject(i).getString("title"), jSONArray.getJSONObject(i).getString("url"), jSONArray.getJSONObject(i).getString("thumbnail"), jSONArray.getJSONObject(i).getInt("thumbnail_width"), jSONArray.getJSONObject(i).getInt("thumbnail_height"), jSONArray.getJSONObject(i).getString("price"));
            }
            return Response.success(amazonAdArr, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
