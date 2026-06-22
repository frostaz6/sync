package com.resync.app.ui.views.monet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class MonetLinearProgressIndicator extends FrameLayout implements DarkModeAware {

    LinearProgressIndicator progressIndicator;

    public MonetLinearProgressIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.progressIndicator = new LinearProgressIndicator(getContext());
        applyColors();
        this.progressIndicator.setIndeterminate(true);
        this.progressIndicator.setIndicatorDirection(0);
        addView(this.progressIndicator);
    }

    private void applyColors() {
        int linkColor = MonetUtil.getLinkColorAuto();
        int windowBackgroundColor = MonetUtil.getWindowBackgroundColorAuto();
        LinearProgressIndicator linearProgressIndicator = this.progressIndicator;
        int[] iArr = new int[3];
        iArr[0] = NightHelper.isNight() ? ColorUtils.blendARGB(linkColor, windowBackgroundColor, 0.8f) : linkColor;
        iArr[1] = ColorUtils.blendARGB(linkColor, windowBackgroundColor, 0.6f);
        iArr[2] = ColorUtils.blendARGB(linkColor, windowBackgroundColor, 0.4f);
        linearProgressIndicator.setIndicatorColor(iArr);
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
    }
}
