package com.resync.app.net.thirdparty;

import androidx.core.util.Pair;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import de.jetwick.snacktory.ArticleTextExtractor;
import de.jetwick.snacktory.JResult;
import org.apache.commons.lang3.time.DateUtils;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: r8.e */
/* JADX INFO: compiled from: ReadableWebsiteRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class ReadableWebsiteRequest extends Request<Pair<String, String>> {

    final Response.Listener<Pair<String, String>> responseListener;

    public ReadableWebsiteRequest(String str, Response.Listener<Pair<String, String>> listener, Response.ErrorListener errorListener) {
        super(0, str, errorListener);
        this.responseListener = listener;
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    @Override // com.android.volley.Request
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Pair<String, String> pair) {
        Response.Listener<Pair<String, String>> listener = this.responseListener;
        if (listener != null) {
            listener.onResponse(pair);
        }
    }

    @Override // com.android.volley.Request
    public Response<Pair<String, String>> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JResult jResult = new JResult();
            new ArticleTextExtractor().extractContent(jResult, new String(networkResponse.data));
            return Response.success(new Pair(jResult.getTitle(), jResult.getText()), CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
