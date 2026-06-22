package com.resync.app.ui.views.drawer;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.widgets.SidebarFrameLayout;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: loaded from: classes3.dex */
public class PanelContentFrameLayout extends SidebarFrameLayout {
    public PanelContentFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors();
    }

    private void applyColors() {
        setBackgroundColor(MonetUtil.getBackgroundColorAuto());
    }

    @Override // com.resync.app.widgets.SidebarFrameLayout, com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        super.onDarkModeChanged();
        applyColors();
    }
}
