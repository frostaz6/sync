package com.resync.app.util;

/* JADX INFO: renamed from: wc.j */
/* JADX INFO: compiled from: MathUtil.java */
/* JADX INFO: loaded from: classes3.dex */
public final class MathUtil {
    public static boolean isInteger(String str) {
        if (str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-') {
                if (str.length() == 1) {
                    return false;
                }
            } else if (Character.digit(str.charAt(i), 10) < 0) {
                return false;
            }
        }
        return true;
    }
}
