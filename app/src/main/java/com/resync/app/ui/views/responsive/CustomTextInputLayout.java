package com.resync.app.ui.views.responsive;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CustomTextInputLayout extends TextInputLayout {
    public CustomTextInputLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors(NightHelper.isNight());
    }

    public void applyColors(boolean night) {
        setBoxStrokeColorStateList(new ColorStateList(new int[][]{new int[]{-16842908}, new int[]{R.attr.state_focused}}, new int[]{ColorUtils.setAlphaComponent(MonetUtil.getNeutralTextColor(night), 85), MonetUtil.getHighlightColorAuto()}));
        setDefaultHintTextColor(new ColorStateList(new int[][]{new int[]{-16842908}, new int[]{R.attr.state_focused}}, new int[]{MonetUtil.getNeutralTextColor(night), MonetUtil.getHighlightColorAuto()}));
        setHintTextColor(ColorStateList.valueOf(MonetUtil.getHighlightColorAuto()));
    }
}
