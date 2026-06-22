package com.resync.app.ui.views.responsive;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CustomMaterialRadioButton extends MaterialRadioButton {
    public CustomMaterialRadioButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors(NightHelper.isNight());
    }

    public void applyColors(boolean night) {
        setUseMaterialThemeColors(false);
        setTextColor(StyleSingleton.getTextColor(getContext(), false, night));
        setButtonTintList(new ColorStateList(new int[][]{new int[]{-16842910}, new int[]{R.attr.state_checked}, new int[0]}, new int[]{MonetUtil.getNeutralTextColor(night), MonetUtil.getLinkColor(night), MonetUtil.getNeutralTextColor(night)}));
    }
}
