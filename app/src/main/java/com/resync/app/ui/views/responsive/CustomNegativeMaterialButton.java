package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.button.MaterialButton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CustomNegativeMaterialButton extends MaterialButton {
    public CustomNegativeMaterialButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyStyle(NightHelper.isNight());
    }

    public void applyStyle(boolean night) {
        setStrokeColor(ColorStateList.valueOf(ColorUtils.setAlphaComponent(MonetUtil.getNeutralTextColor(night), 85)));
        setStrokeWidth(SizeHelper.dpToPx(1));
        setBackgroundColor(MonetUtil.getBackgroundColor(night));
        setTextColor(MonetUtil.getLinkColor(night));
        setAllCaps(false);
        setElevation(0.0f);
        setStateListAnimator(null);
        setRippleColor(ColorStateList.valueOf(1433892727));
        setClickable(true);
    }

    public CustomNegativeMaterialButton(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        applyStyle(NightHelper.isNight());
    }
}
