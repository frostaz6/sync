package com.resync.app.net.thirdparty;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Gallery;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: s8.c */
/* JADX INFO: compiled from: ImgurIobLink.java */
/* JADX INFO: loaded from: classes.dex */
public class ImgurIobLink extends Request<Void> {

    private final Response.Listener<Gallery> responseListener;
    private final Response.ErrorListener errorListener;

    public ImgurIobLink(String str, Response.Listener<Gallery> listener, Response.ErrorListener errorListener) {
        super(0, str, null);
        this.responseListener = listener;
        this.errorListener = errorListener;
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    @Override // com.android.volley.Request
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Void result) {
    }

    @Override // com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        NetworkResponse networkResponse;
        if (volleyError == null || (networkResponse = volleyError.networkResponse) == null) {
            this.errorListener.onErrorResponse(volleyError);
            return;
        }
        int statusCode = networkResponse.statusCode;
        if (301 == statusCode || statusCode == 302 || statusCode == 303 || statusCode == 307) {
            OAuthRequestManager.enqueueAux(new ImgurIobLink(networkResponse.headers.get("Location"), this.responseListener, this.errorListener));
        } else {
            this.errorListener.onErrorResponse(volleyError);
        }
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        String str = new String(networkResponse.data);
        Logger.verbose("Returned: " + str);
        OAuthRequestManager.enqueueAux(new ImgurGalleryRequest("http://imgur.com/a/" + str.substring(str.indexOf("imgur://imgur.com/gallery/") + 26, str.indexOf("?_branch_match_id=")), this.responseListener, this.errorListener));
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
