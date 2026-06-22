package com.resync.app.presenters;

import com.resync.app.util.TestUtils;

/* JADX INFO: renamed from: ya.i */
/* JADX INFO: compiled from: PresenterUtils.java */
/* JADX INFO: loaded from: classes.dex */
public final class PresenterUtils {
    public static boolean shouldAttachPresenter() {
        return !TestUtils.isRobolectric();
    }
}
