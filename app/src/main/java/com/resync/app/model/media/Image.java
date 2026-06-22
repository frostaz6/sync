package com.resync.app.model.media;

import android.text.TextUtils;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.media.ImageUtils;

/* JADX INFO: renamed from: g8.c */
/* JADX INFO: compiled from: Image.java */
/* JADX INFO: loaded from: classes.dex */
public class Image {

    public String id;          // a: imgur id
    public String extension;   // b: file extension
    public String subdomain;   // c: imgur subdomain

    public Image(String str) {
        this(str, ImageUtils.getImgurExtension(str));
    }

    private String resolveExtension(boolean z) {
        return z ? this.extension : (this.extension.equals("gif") || this.extension.equals("gifv")) ? "mp4" : this.extension;
    }

    private String subdomainPrefix() {
        if (TextUtils.isEmpty(this.subdomain) || this.subdomain.equalsIgnoreCase("i")) {
            return "i";
        }
        return "i." + this.subdomain;
    }

    public String getHugeThumbnailUrl() {
        return "https://" + subdomainPrefix() + ".imgur.com/" + this.id + "h." + resolveExtension(true);
    }

    public String getBigThumbnailUrl() {
        return "https://" + subdomainPrefix() + ".imgur.com/" + this.id + "b." + resolveExtension(true);
    }

    public String getUrl() {
        return "https://" + subdomainPrefix() + ".imgur.com/" + this.id + "." + resolveExtension(false);
    }

    public boolean isVideo() {
        return "gifv".equalsIgnoreCase(this.extension) || "mp4".equalsIgnoreCase(this.extension) || "gif".equalsIgnoreCase(this.extension);
    }

    public String toString() {
        return getUrl();
    }

    public Image(String str, String str2) {
        String normalizedUrl = ImgurImageHelper.normalizeUrl(str);
        this.subdomain = ImgurImageHelper.getSubdomain(normalizedUrl);
        this.id = ImgurImageHelper.getId(normalizedUrl);
        this.extension = str2;
    }
}
