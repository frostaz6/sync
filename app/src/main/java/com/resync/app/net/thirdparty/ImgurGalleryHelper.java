package com.resync.app.net.thirdparty;

import com.android.volley.Response;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Gallery;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: s8.a */
/* JADX INFO: compiled from: ImgurGalleryHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ImgurGalleryHelper {
    public static void load(String str, Response.Listener<Gallery> listener, Response.ErrorListener errorListener) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.contains("iob.imgur.com")) {
            OAuthRequestManager.enqueueAux(new ImgurIobLink(str, listener, errorListener));
        } else {
            OAuthRequestManager.enqueueAux(new ImgurGalleryRequest(str, listener, errorListener));
        }
    }
}
