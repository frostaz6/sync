package com.resync.app.ui.views.monet;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class MonetSwitch extends MaterialSwitch implements DarkModeAware {
    public MonetSwitch(Context context) {
        super(context);
        applyColors();
    }

    private void applyColors() {
        int linkColor = MonetUtil.getLinkColorAuto();
        int blended80 = ColorUtils.blendARGB(MonetUtil.getNeutralTextColorAuto(), MonetUtil.getBackgroundColorAuto(), 0.8f);
        int blended50 = ColorUtils.blendARGB(MonetUtil.getNeutralTextColorAuto(), MonetUtil.getBackgroundColorAuto(), 0.5f);
        int blended70 = ColorUtils.blendARGB(MonetUtil.getNeutralTextColorAuto(), MonetUtil.getBackgroundColorAuto(), 0.7f);
        setThumbTintList(new ColorStateList(new int[][]{new int[]{-16842910}, new int[]{R.attr.state_checked}, new int[0]}, new int[]{blended70, MonetUtil.getWindowBackgroundColorAuto(), blended80}));
        setThumbIconResource(com.resync.app.R.drawable.outline_check_24);
        setThumbIconTintList(new ColorStateList(new int[][]{new int[]{-16842912}, new int[]{R.attr.state_checked}}, new int[]{blended80, linkColor}));
        setTrackTintList(new ColorStateList(new int[][]{new int[]{-16842910}, new int[]{R.attr.state_checked}, new int[0]}, new int[]{blended70, linkColor, blended50}));
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    public MonetSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors();
    }
}
