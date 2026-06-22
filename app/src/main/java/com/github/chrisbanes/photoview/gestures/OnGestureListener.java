package com.github.chrisbanes.photoview.gestures;

/* JADX INFO: renamed from: re.e */
/* JADX INFO: compiled from: OnGestureListener.java */
/* JADX INFO: loaded from: classes3.dex */
public interface OnGestureListener {
    void onScale(float scaleFactor, float focusX, float focusY);

    void onFling(float startX, float startY, float velocityX, float velocityY);

    void onDrag(float dx, float dy);
}
