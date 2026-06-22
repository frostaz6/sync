package com.github.chrisbanes.photoview.scrollers;

import android.annotation.TargetApi;
import android.content.Context;

/* JADX INFO: renamed from: se.b */
/* JADX INFO: compiled from: IcsScroller.java */
/* JADX INFO: loaded from: classes3.dex */
@TargetApi(14)
public class IcsScroller extends GingerScroller {
    public IcsScroller(Context context) {
        super(context);
    }

    @Override // com.github.chrisbanes.photoview.scrollers.ScrollerProxy
    public boolean computeScrollOffset() {
        return this.mScroller.computeScrollOffset();
    }
}
