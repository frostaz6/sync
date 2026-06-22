package com.resync.app.ui.views.monet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.slider.RangeSlider;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class MonetSlider extends RangeSlider implements DarkModeAware {
    public MonetSlider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors();
    }

    public void applyColors() {
        int linkColor = MonetUtil.getLinkColorAuto();
        ColorUtils.blendARGB(MonetUtil.getNeutralTextColorAuto(), MonetUtil.getBackgroundColorAuto(), 0.8f);
        ColorUtils.blendARGB(MonetUtil.getNeutralTextColorAuto(), MonetUtil.getBackgroundColorAuto(), 0.5f);
        int inactiveTrackColor = ColorUtils.blendARGB(MonetUtil.getNeutralTextColorAuto(), MonetUtil.getBackgroundColorAuto(), 0.7f);
        setTrackActiveTintList(ColorStateList.valueOf(linkColor));
        setThumbTintList(ColorStateList.valueOf(linkColor));
        setHaloTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(linkColor, 127)));
        setTrackInactiveTintList(ColorStateList.valueOf(inactiveTrackColor));
        setLabelBehavior(2);
        setTickVisible(false);
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }
}
