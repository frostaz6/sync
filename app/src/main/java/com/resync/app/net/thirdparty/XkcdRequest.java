package com.resync.app.net.thirdparty;

import android.text.Html;
import androidx.core.util.Pair;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import org.json.JSONObject;
import com.resync.app.net.LegacyCustomHttpHeaderParser;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: r8.k */
/* JADX INFO: compiled from: XkcdRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class XkcdRequest extends Request<Pair<String, String>> {

    private final Response.Listener<Pair<String, String>> responseListener;

    public XkcdRequest(String str, Response.Listener<Pair<String, String>> listener, Response.ErrorListener errorListener) {
        super(0, buildUrl(str), errorListener);
        this.responseListener = listener;
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    public static String buildUrl(String str) {
        return "https://xkcd.com/" + LinkHandler.getXkcdId(str) + "/info.0.json";
    }

    @Override // com.android.volley.Request
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Pair<String, String> pair) {
        this.responseListener.onResponse(pair);
    }

    @Override // com.android.volley.Request
    public Response<Pair<String, String>> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("title");
            String string2 = Html.fromHtml(jSONObject.getString("alt")).toString();
            return Response.success(Pair.create(string + ": " + string2, jSONObject.getString("img")), LegacyCustomHttpHeaderParser.parseCacheEntry(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
