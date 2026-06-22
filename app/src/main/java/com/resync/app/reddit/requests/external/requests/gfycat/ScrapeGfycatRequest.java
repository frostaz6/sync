package com.resync.app.reddit.requests.external.requests.gfycat;

import android.util.Patterns;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.regex.Matcher;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.LegacyCustomHttpHeaderParser;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class ScrapeGfycatRequest extends Request<String> {

    protected Response.Listener<String> responseListener;

    public ScrapeGfycatRequest(String str, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(0, str, errorListener);
        this.responseListener = listener;
        setShouldCache(true);
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            Matcher matcher = Patterns.WEB_URL.matcher(new String(networkResponse.data));
            String str = null;
            String str2 = null;
            while (matcher.find()) {
                String strGroup = matcher.group();
                if (StringUtils.endsWith(strGroup, ".mp4")) {
                    if (strGroup.endsWith("-mobile.mp4")) {
                        str = strGroup;
                    } else {
                        str2 = strGroup;
                    }
                }
            }
            if (ConnectivityHelper.isWifiConnected() || !StringUtils.isNotEmpty(str)) {
                str = str2;
            }
            Logger.verbose("FOUND: " + str);
            return Response.success(str, LegacyCustomHttpHeaderParser.parseCacheEntry(networkResponse));
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
