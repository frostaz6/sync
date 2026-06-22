package com.resync.app.media;

import android.webkit.MimeTypeMap;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: h8.d */
/* JADX INFO: compiled from: ImageUtils.java */
/* JADX INFO: loaded from: classes.dex */
public final class ImageUtils {

    static String TAG = "d";

    public static String getImgurExtension(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(ImgurImageHelper.normalizeUrl(str));
        return (TextUtils.isEmpty(fileExtensionFromUrl) || !ImageExtensionHelper.isSupportedExtension(fileExtensionFromUrl)) ? "jpg" : fileExtensionFromUrl;
    }

    public static String getExtension(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        return (TextUtils.isEmpty(fileExtensionFromUrl) || !ImageExtensionHelper.isSupportedExtension(fileExtensionFromUrl)) ? "jpg" : fileExtensionFromUrl;
    }

    public static boolean isTallImage(int width, int height) {
        if (width == 0 || height == 0) {
            return false;
        }
        // restored (float) cast dropped by JADX — see Vineflower h8/d.java; int/int truncates the ratio (e.g. 1080/1920 -> 0) otherwise
        float ratio = (float) height / width;
        Logger.verbose(TAG, "Input: " + width + "x" + height);
        String str = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Ratio: ");
        sb2.append(ratio);
        Logger.verbose(str, sb2.toString());
        return ratio >= 2.25f;
    }
}
