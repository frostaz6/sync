package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class CustomProgressBar extends ProgressBar implements DarkModeAware {
    public CustomProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors();
    }

    public void applyColors() {
        applyColors(NightHelper.isNight());
    }

    public void applyColors(boolean night) {
        getIndeterminateDrawable().setColorFilter(MonetUtil.getProgressColor(night), PorterDuff.Mode.SRC_IN);
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }
}
