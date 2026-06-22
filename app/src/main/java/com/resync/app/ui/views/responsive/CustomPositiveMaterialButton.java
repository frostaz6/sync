package com.resync.app.ui.views.responsive;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import com.google.android.material.button.MaterialButton;
import com.resync.app.helpers.NightHelper;
import com.resync.app.util.ColorUtils;

/* JADX INFO: loaded from: classes3.dex */
public class CustomPositiveMaterialButton extends MaterialButton {
    public CustomPositiveMaterialButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyStyle(NightHelper.isNight());
    }

    public void applyStyle(boolean night) {
        int color = getContext().getColor(R.color.system_accent1_100);
        setBackgroundColor(color);
        setTextColor(ColorUtils.isDark(color, night) ^ true ? -16777216 : -1);
        setAllCaps(false);
    }

    public CustomPositiveMaterialButton(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        applyStyle(NightHelper.isNight());
    }
}
