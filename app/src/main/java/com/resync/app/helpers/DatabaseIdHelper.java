package com.resync.app.helpers;

import android.text.TextUtils;

/* JADX INFO: renamed from: t7.l */
/* JADX INFO: compiled from: DatabaseIdHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class DatabaseIdHelper {
    public static String buildId(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return str;
        }
        return str + "_" + str2;
    }
}
