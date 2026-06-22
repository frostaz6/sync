package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: loaded from: classes3.dex */
public class CustomButtonsWrapper extends LinearLayout {

    public Paint paint;

    public CustomButtonsWrapper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = new Paint();
        setBackgroundColor(MonetUtil.getBackgroundColorAuto());
        setBaselineAligned(false);
        setWillNotDraw(false);
    }
}
