package com.resync.app.ui.views.responsive.chips;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.chip.Chip;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class ContentChip extends Chip implements DarkModeAware {
    boolean compact;

    public ContentChip(Context context) {
        super(context);
        this.compact = false;
        applyStyle();
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyStyle();
    }

    public void setCompact() {
        this.compact = true;
        applyStyle();
    }

    public void applyUnselectedColors() {
        applyUnselectedColors(NightHelper.isNight());
    }

    public void applyUnselectedColors(boolean night) {
        if (this.compact) {
            int backgroundColor = MonetUtil.getCardColorAuto();
            int textColor = MonetUtil.getTertiaryTextColorAuto();
            setChipBackgroundColor(ColorStateList.valueOf(backgroundColor));
            setCloseIconTint(ColorStateList.valueOf(textColor));
            setTextColor(textColor);
            setChipStrokeWidth(SizeHelper.dpToPx(0));
            return;
        }
        int backgroundColor = MonetUtil.getBackgroundColor(night);
        int textColor = MonetUtil.getPrimaryTextColor(night);
        setChipBackgroundColor(ColorStateList.valueOf(backgroundColor));
        if (shouldTintCloseIcon()) {
            setCloseIconTint(ColorStateList.valueOf(textColor));
        }
        setRippleColor(ColorStateList.valueOf(textColor));
        setTextColor(textColor);
        if (!SettingsSingleton.getSettings().monetForceAmoled || !NightHelper.isNight()) {
            setChipStrokeWidth(SizeHelper.dpToPx(0));
        } else {
            setChipStrokeWidth(SizeHelper.dpToPx(1));
            setChipStrokeColor(ColorStateList.valueOf(ColorUtils.setAlphaComponent(MonetUtil.getPrimaryTextColor(true), 50)));
        }
    }

    public void applySelectedColors() {
        applySelectedColors(NightHelper.isNight());
    }

    public void applySelectedColors(boolean night) {
        int backgroundColor = MonetUtil.getAccent2BackgroundColor(night);
        int textColor = MonetUtil.getAccent2TextColor(night);
        setChipBackgroundColor(ColorStateList.valueOf(backgroundColor));
        if (shouldTintCloseIcon()) {
            setCloseIconTint(ColorStateList.valueOf(textColor));
        }
        setRippleColor(ColorStateList.valueOf(textColor));
        setTextColor(textColor);
        setChipStrokeWidth(SizeHelper.dpToPx(0));
    }

    protected boolean shouldTintCloseIcon() {
        return true;
    }

    public void applyStyle() {
        setEnsureMinTouchTargetSize(false);
        if (isSelected()) {
            applySelectedColors();
        } else {
            applyUnselectedColors();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (isSelected()) {
            applySelectedColors();
        } else {
            applyUnselectedColors();
        }
    }

    public ContentChip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.compact = false;
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, SyncStyleable.CONTENT_CHIP);
        this.compact = typedArray.getBoolean(0, false);
        typedArray.recycle();
        applyStyle();
    }
}
