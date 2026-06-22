package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.EditText;
import androidx.core.graphics.ColorUtils;
import com.bumptech.glide.RedditHelper;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class CustomEditText extends EditText implements DarkModeAware {
    public CustomEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors();
    }

    public void applyColors() {
        setHintTextColor(MonetUtil.getSecondaryTextColorAuto());
        setBackgroundDrawable(new ColorDrawable(0));
        setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        setTextColor(StyleSingleton.getTextColorAuto(getContext(), false));
        setHighlightColor(ColorUtils.setAlphaComponent(MonetUtil.getLinkColorAuto(), 85));
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }
}
