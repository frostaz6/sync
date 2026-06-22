package com.github.chrisbanes.photoview.gestures;

import android.content.Context;

/* JADX INFO: renamed from: re.f */
/* JADX INFO: compiled from: VersionedGestureDetector.java */
/* JADX INFO: loaded from: classes3.dex */
public final class VersionedGestureDetector {
    public static GestureDetector newInstance(Context context, OnGestureListener onGestureListener) {
        FroyoGestureDetector froyoGestureDetector = new FroyoGestureDetector(context);
        froyoGestureDetector.setOnGestureListener(onGestureListener);
        return froyoGestureDetector;
    }
}
