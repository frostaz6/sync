package com.github.chrisbanes.photoview.gestures;

import android.view.MotionEvent;

/* JADX INFO: renamed from: re.d */
/* JADX INFO: compiled from: GestureDetector.java */
/* JADX INFO: loaded from: classes3.dex */
public interface GestureDetector {
    boolean onTouchEvent(MotionEvent motionEvent);

    void setOnGestureListener(OnGestureListener onGestureListener);

    boolean isDragging();

    boolean isScaling();
}
