package com.resync.app.ui.views.peek;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/* JADX INFO: loaded from: classes3.dex */
public class PeekingLinearLayout extends LinearLayout {
    public PeekingLinearLayout(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Override // android.view.View
    public boolean postDelayed(Runnable runnable, long delayMillis) {
        if (runnable.getClass().getSimpleName().equals("CheckForLongPress")) {
            delayMillis /= 2;
        }
        return super.postDelayed(runnable, delayMillis);
    }
}
