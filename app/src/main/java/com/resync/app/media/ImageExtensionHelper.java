package com.resync.app.media;

import android.text.TextUtils;

/* JADX INFO: renamed from: h8.c */
/* JADX INFO: compiled from: ImageExtensionHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ImageExtensionHelper {
    public static boolean isSupportedExtension(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.equals("jpeg") || str.equals("jpg") || str.equals("png") || str.equals("gif") || str.equals("gifv");
    }
}
