package com.resync.app.net.thirdparty;

import com.android.volley.VolleyError;

/* JADX INFO: renamed from: u8.b */
/* JADX INFO: compiled from: ImgurUploadError.java */
/* JADX INFO: loaded from: classes.dex */
public class ImgurUploadError extends VolleyError {
    public ImgurUploadError() {
        super("Upload to Imgur is not currently available");
    }
}
