package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: loaded from: classes3.dex */
public class EditFrameLayout extends FrameLayout {
    public EditFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBackgroundDrawable(new ColorDrawable(MonetUtil.getBackgroundColorAuto()));
    }
}
