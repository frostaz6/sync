package com.resync.app.widgets;

import android.graphics.Canvas;

/* JADX INFO: renamed from: jb.c */
/* JADX INFO: compiled from: ShapeOverlay.java */
/* JADX INFO: loaded from: classes3.dex */
public interface ShapeOverlay {
    void update(int width, int height, int oldWidth, int oldHeight);

    void draw(Canvas canvas);
}
