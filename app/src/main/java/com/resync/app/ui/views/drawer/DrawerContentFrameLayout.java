package com.resync.app.ui.views.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.drawerlayout.widget.DrawerLayout;
import com.resync.app.widgets.SidebarFrameLayout;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.TabletHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class DrawerContentFrameLayout extends SidebarFrameLayout {
    public DrawerContentFrameLayout(Context context, AttributeSet attributeSet) {
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

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        Logger.verbose("PARAMS: " + layoutParams);
        StringBuilder builder = new StringBuilder();
        builder.append("PARAMS EQUALS: ");
        boolean isDrawerParams = layoutParams instanceof DrawerLayout.LayoutParams;
        builder.append(isDrawerParams);
        Logger.verbose(builder.toString());
        if (isDrawerParams) {
            int gravity = ((DrawerLayout.LayoutParams) layoutParams).gravity;
            if (gravity == 8388611) {
                if (TabletHelper.isLandscape()) {
                    layoutParams.width = (int) (WindowUtils.getDisplayWidth() * 0.4f);
                    return;
                } else {
                    layoutParams.width = (int) (WindowUtils.getDisplayWidth() * 0.8f);
                    return;
                }
            }
            if (gravity == 8388613) {
                if (TabletHelper.isLandscape()) {
                    layoutParams.width = (int) (WindowUtils.getDisplayWidth() * 0.5f);
                } else {
                    layoutParams.width = (int) (WindowUtils.getDisplayWidth() * 0.85f);
                }
            }
        }
    }
}
