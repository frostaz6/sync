package com.resync.app.util;

/* JADX INFO: renamed from: wc.p */
/* JADX INFO: compiled from: WebsafeUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public final class WebsafeUtils {
    public static String unescapeHtml(String str) {
        return TextUtils.isEmpty(str) ? str : str.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("\u202e", "").replaceAll("\u200f", "");
    }
}
