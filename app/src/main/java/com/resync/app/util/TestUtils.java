package com.resync.app.util;

import android.os.Build;

/* JADX INFO: renamed from: wc.l */
/* JADX INFO: compiled from: TestUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public final class TestUtils {
    public static boolean isRobolectric() {
        return "robolectric".equals(Build.FINGERPRINT);
    }
}
