package com.resync.app.ui.views.monet.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.button.MaterialButton;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class MonetContentHighlightButton extends MaterialButton implements DarkModeAware {
    public MonetContentHighlightButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, SyncStyleable.MONET_HIGHLIGHT_BUTTON);
        setSelected(typedArray.getBoolean(0, false));
        typedArray.recycle();
        applyStyle();
    }

    public void styleUnselected() {
        applyUnselectedColors(NightHelper.isNight());
    }

    public void applyUnselectedColors(boolean night) {
        setBackgroundColor(MonetUtil.getCardColor(night));
        setTextColor(MonetUtil.getTertiaryTextColor(night));
    }

    public void styleSelected() {
        applySelectedColors(NightHelper.isNight());
    }

    public void applySelectedColors(boolean night) {
        setBackgroundColor(MonetUtil.getAccent2BackgroundColor(night));
        setTextColor(MonetUtil.getAccent2TextColor(night));
    }

    public void applyStyle() {
        PaddingHelper.setPaddingTopBottom(this, 12, 12);
        setCornerRadius(SizeHelper.dpToPx(12));
        if (isSelected()) {
            styleSelected();
        } else {
            styleUnselected();
        }
    }

    public void applyColors(boolean night) {
        setCornerRadius(SizeHelper.dpToPx(12));
        if (isSelected()) {
            applySelectedColors(night);
        } else {
            applyUnselectedColors(night);
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
