package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class ContentFrameLayout extends FrameLayout implements DarkModeAware {
    public ContentFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors();
    }

    private void applyColors() {
        setBackgroundColor(MonetUtil.getBackgroundColorAuto());
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }
}
