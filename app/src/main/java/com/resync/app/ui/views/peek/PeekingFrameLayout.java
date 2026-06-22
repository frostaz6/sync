package com.resync.app.ui.views.peek;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes3.dex */
public class PeekingFrameLayout extends FrameLayout {
    public PeekingFrameLayout(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Override // android.view.View
    public boolean postDelayed(Runnable runnable, long delayMillis) {
        return runnable.getClass().getSimpleName().equals("CheckForLongPress") ? super.postDelayed(runnable, delayMillis / 2) : super.postDelayed(runnable, delayMillis);
    }
}
