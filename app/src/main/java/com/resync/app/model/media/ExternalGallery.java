package com.resync.app.model.media;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kotlin.UByte;
import com.resync.app.media.imgur.ImgurImageHelper;

/* JADX INFO: renamed from: g8.b */
/* JADX INFO: compiled from: ExternalGallery.java */
/* JADX INFO: loaded from: classes.dex */
public class ExternalGallery {

    public String id;   // a: imgur album id

    public ExternalGallery(String url) {
        this.id = ImgurImageHelper.getId(ImgurImageHelper.normalizeUrl(url));
    }

    public static String hash(String input) {
        try {
            byte[] bArrDigest = MessageDigest.getInstance("MD5").digest(input.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte digestByte : bArrDigest) {
                stringBuffer.append(Integer.toHexString((digestByte & UByte.MAX_VALUE) | 256).substring(1, 3));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException unused) {
            return null;
        }
    }

    public String getUrl() {
        return "https://images.syncforreddit.com/a/" + hash(this.id + "yUR2jePPMcO6PVJxat8o73jDfQTpBhbq") + "/" + this.id;
    }

    public String toString() {
        return getUrl();
    }
}
