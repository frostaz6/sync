package com.resync.app.media.images;

import android.text.TextUtils;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: c8.d */
/* JADX INFO: compiled from: PostValuehelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PostValuehelper {
    public static int boolToInt(boolean z) {
        return z ? 1 : 0;
    }

    public static int intToFlag(int value) {
        return value > 0 ? 1 : 0;
    }

    public static String getDomain(String str) {
        String domain = sanitize(str);
        if (TextUtils.isEmpty(domain)) {
            return "";
        }
        if (domain.startsWith("self.")) {
            domain = domain.replace("self.", "");
        }
        return StringUtils.removeEnd(StringUtils.removeEnd(StringUtils.removeEnd(StringUtils.removeEnd(domain.replace("i.imgur", "imgur"), ".com"), ".co.uk"), ".net"), ".me");
    }

    public static String getRawDomain(String str) {
        String domain = sanitize(str);
        return TextUtils.isEmpty(domain) ? "" : domain.startsWith("self.") ? domain.replace("self.", "") : domain;
    }

    public static String sanitize(String str) {
        if ("null".equals(str) || TextUtils.isEmpty(str)) {
            return null;
        }
        return str.trim();
    }

    public static int parseBoolean(String str) {
        if ("true".equalsIgnoreCase(str)) {
            return 1;
        }
        return "false".equalsIgnoreCase(str) ? -1 : 0;
    }
}
