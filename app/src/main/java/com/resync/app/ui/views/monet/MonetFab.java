package com.resync.app.ui.views.monet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class MonetFab extends FloatingActionButton implements DarkModeAware {
    public MonetFab(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors();
    }

    private void applyColors() {
        setBackgroundTintList(ColorStateList.valueOf(MonetUtil.getProgressColorAuto()));
        setColorFilter(MonetUtil.getAccentSurfaceColorAuto());
        setRippleColor(MonetUtil.getRippleColorAuto());
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    @Override // com.google.android.material.floatingactionbutton.FloatingActionButton, android.widget.ImageView
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        applyColors();
    }
}
