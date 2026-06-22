package com.resync.app.util;

import android.annotation.TargetApi;
import android.view.View;

/* JADX INFO: renamed from: qe.a */
/* JADX INFO: compiled from: Compat.java */
/* JADX INFO: loaded from: classes3.dex */
public class Compat {
    public static int getGreenComponent(int color) {
        return getGreenComponentInternal(color);
    }

    @TargetApi(11)
    private static int getGreenComponentInternal(int color) {
        return (color & 65280) >> 8;
    }

    public static void postOnAnimation(View view, Runnable runnable) {
        postOnAnimationInternal(view, runnable);
    }

    @TargetApi(16)
    private static void postOnAnimationInternal(View view, Runnable runnable) {
        view.postOnAnimation(runnable);
    }
}
