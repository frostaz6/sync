package com.resync.app.ui.views.posts.swipe;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: loaded from: classes3.dex */
public class SwipeBottomSheetWrapper extends FrameLayout {
    public SwipeBottomSheetWrapper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyBackground();
    }

    void applyBackground() {
        setBackgroundDrawable(new ColorDrawable(MonetUtil.getWindowBackgroundColorAuto()));
    }
}
