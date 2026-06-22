package com.resync.app.ui.views.monet.buttons;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.material.button.MaterialButton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class MonetContentBackgroundButton extends MaterialButton implements DarkModeAware {
    public MonetContentBackgroundButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyStyle();
    }

    public void styleUnselected() {
        applyUnselectedColors(NightHelper.isNight());
    }

    public void applyUnselectedColors(boolean night) {
        setBackgroundColor(MonetUtil.isAmoled() ? MonetUtil.getCardColor(night) : MonetUtil.getBackgroundColor(night));
        setTextColor(MonetUtil.getPrimaryTextColor(night));
    }

    public void styleSelected() {
        applySelectedColors(NightHelper.isNight());
    }

    public void applySelectedColors(boolean night) {
        setBackgroundColor(MonetUtil.getAccent2BackgroundColor(night));
        setTextColor(MonetUtil.getAccent2TextColor(night));
    }

    public void applyStyle() {
        PaddingHelper.setPaddingTopBottom(this, 14, 14);
        setCornerRadius(SizeHelper.dpToPx(12));
        if (isSelected()) {
            styleSelected();
        } else {
            styleUnselected();
        }
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyStyle();
    }

    @Override // android.widget.TextView, android.view.View
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (isSelected()) {
            styleSelected();
        } else {
            styleUnselected();
        }
    }
}
