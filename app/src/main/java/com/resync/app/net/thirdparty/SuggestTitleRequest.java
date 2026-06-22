package com.resync.app.net.thirdparty;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import de.jetwick.snacktory.ArticleTextExtractor;
import de.jetwick.snacktory.JResult;
import org.apache.commons.lang3.time.DateUtils;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: r8.i */
/* JADX INFO: compiled from: SuggestTitleRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class SuggestTitleRequest extends Request<String> {

    final Response.Listener<String> responseListener;

    public SuggestTitleRequest(String str, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(0, str, errorListener);
        this.responseListener = listener;
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JResult jResult = new JResult();
            new ArticleTextExtractor().extractContent(jResult, new String(networkResponse.data));
            return Response.success(jResult.getTitle(), CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
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
