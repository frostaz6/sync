package com.github.chrisbanes.photoview.scrollers;

import android.annotation.TargetApi;
import android.content.Context;
import android.widget.OverScroller;

/* JADX INFO: renamed from: se.a */
/* JADX INFO: compiled from: GingerScroller.java */
/* JADX INFO: loaded from: classes3.dex */
@TargetApi(9)
public class GingerScroller extends ScrollerProxy {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected final OverScroller mScroller;
    private boolean mFirstScroll = false;

    public GingerScroller(Context context) {
        this.mScroller = new OverScroller(context);
    }

    @Override // com.github.chrisbanes.photoview.scrollers.ScrollerProxy
    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        this.mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
    }

    @Override // com.github.chrisbanes.photoview.scrollers.ScrollerProxy
    public void forceFinished(boolean finished) {
        this.mScroller.forceFinished(finished);
    }

    @Override // com.github.chrisbanes.photoview.scrollers.ScrollerProxy
    public boolean computeScrollOffset() {
        return this.mScroller.computeScrollOffset();
    }

    @Override // com.github.chrisbanes.photoview.scrollers.ScrollerProxy
    public int getCurrX() {
        return this.mScroller.getCurrX();
    }

    @Override // com.github.chrisbanes.photoview.scrollers.ScrollerProxy
    public int getCurrY() {
        return this.mScroller.getCurrY();
    }

    @Override // com.github.chrisbanes.photoview.scrollers.ScrollerProxy
    public boolean isFinished() {
        return this.mScroller.isFinished();
    }
}
