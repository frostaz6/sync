package com.resync.app.net.thirdparty;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.text.parsing.StreamableParserHelper;
import com.resync.app.net.LegacyCustomHttpHeaderParser;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: r8.b */
/* JADX INFO: compiled from: CheckStreamableRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class CheckStreamableRequest extends Request<String> {

    private final Context context;
    private final Response.Listener<String> responseListener;

    public CheckStreamableRequest(String str, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(0, buildUrl(str), errorListener);
        this.context = RedditApplication.getAppContext();
        this.responseListener = listener;
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    private static final String buildUrl(String str) {
        return "https://api.streamable.com/videos/" + LinkHandler.getLastSegmentId(str);
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        String videoUrl = StreamableParserHelper.parseVideoUrl(new String(networkResponse.data));
        return !TextUtils.isEmpty(videoUrl) ? Response.success(videoUrl, LegacyCustomHttpHeaderParser.parseCacheEntry(networkResponse)) : Response.error(new VolleyError("Parsing error"));
    }

    @Override // com.android.volley.Request
    public void deliverResponse(String str) {
        this.responseListener.onResponse(str);
    }
}
