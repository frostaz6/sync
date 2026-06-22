package com.resync.app.net.thirdparty;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/* JADX INFO: renamed from: t8.d */
/* JADX INFO: compiled from: RedgifRequestWrapper.java */
/* JADX INFO: loaded from: classes.dex */
public class RedgifRequestWrapper {

    public String gifUrl;
    public boolean forceHd;
    public String token;
    public String userAddr;

    public Response.Listener<String> responseListener;

    public Response.ErrorListener errorListener;

    public RedgifRequestWrapper(String str, boolean forceHd, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this.gifUrl = str;
        this.forceHd = forceHd;
        this.responseListener = listener;
        this.errorListener = errorListener;
    }

    public void deliverError(VolleyError volleyError) {
        Response.ErrorListener errorListener = this.errorListener;
        if (errorListener != null) {
            errorListener.onErrorResponse(volleyError);
        }
    }

    public void deliverResponse(String str) {
        Response.Listener<String> listener = this.responseListener;
        if (listener != null) {
            listener.onResponse(str);
        }
    }

    public String getToken() {
        return this.token;
    }

    public Response.ErrorListener getErrorListener() {
        return this.errorListener;
    }

    public String getUserAddr() {
        return this.userAddr;
    }

    public boolean isForceHd() {
        return this.forceHd;
    }

    public String getGifUrl() {
        return this.gifUrl;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public void setUserAddr(String str) {
        this.userAddr = str;
    }
}
