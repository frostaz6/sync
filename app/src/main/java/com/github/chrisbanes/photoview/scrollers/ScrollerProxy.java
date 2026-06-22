package com.github.chrisbanes.photoview.scrollers;

import android.content.Context;

/* JADX INFO: renamed from: se.c */
/* JADX INFO: compiled from: ScrollerProxy.java */
/* JADX INFO: loaded from: classes3.dex */
public abstract class ScrollerProxy {
    public static ScrollerProxy getScroller(Context context) {
        return new IcsScroller(context);
    }

    public abstract boolean computeScrollOffset();

    public abstract void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY);

    public abstract void forceFinished(boolean finished);

    public abstract int getCurrX();

    public abstract int getCurrY();

    public abstract boolean isFinished();
}
