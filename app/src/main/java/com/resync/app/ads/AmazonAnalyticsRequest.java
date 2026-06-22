package com.resync.app.ads;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: renamed from: e7.e */
/* JADX INFO: compiled from: AmazonAnalyticsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class AmazonAnalyticsRequest extends Request<Void> {

    private String targetUrl;

    public AmazonAnalyticsRequest(String str) {
        super(1, "https://us-central1-ad-server-f1010.cloudfunctions.net/add", null);
        this.targetUrl = str;
        setShouldCache(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.volley.Request
    public void deliverResponse(Void response) {
    }

    @Override // com.android.volley.Request
    protected Map<String, String> getParams() {
        HashMap map = new HashMap();
        map.put("url", this.targetUrl);
        return map;
    }

    @Override // com.android.volley.Request
    protected Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
