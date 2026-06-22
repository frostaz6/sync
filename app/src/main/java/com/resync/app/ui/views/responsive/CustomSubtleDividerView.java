package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: loaded from: classes3.dex */
public class CustomSubtleDividerView extends View {
    public CustomSubtleDividerView(Context context) {
        super(context);
        setBackgroundColor(ColorUtils.setAlphaComponent(MonetUtil.getNeutralTextColorAuto(), 63));
    }

    public void applyColors(boolean night) {
        setBackgroundColor(ColorUtils.setAlphaComponent(MonetUtil.getNeutralTextColor(night), 63));
    }

    public CustomSubtleDividerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBackgroundColor(ColorUtils.setAlphaComponent(MonetUtil.getNeutralTextColorAuto(), 63));
    }
}
