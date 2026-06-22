package com.reddit.indicatorfastscroll;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerView;

/* Stub: cleaned from decompiled Kotlin. Preserves the public API the app
 * uses (setupWithFastScroller) and the ItemIndicatorSelectedCallback contract.
 * Styling/animation is not wired up. */
public final class FastScrollerThumbView extends ConstraintLayout implements FastScrollerView.ItemIndicatorSelectedCallback {

    private FastScrollerView fastScrollerView;
    private ColorStateList thumbColor;
    private ColorStateList iconColor;
    private int iconSize;
    private int textAppearanceRes;
    private int textColor;

    public FastScrollerThumbView(Context context) {
        this(context, null, 0);
    }

    public FastScrollerThumbView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FastScrollerThumbView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    private boolean isSetup() {
        return this.fastScrollerView != null;
    }

    public final ColorStateList getThumbColor() {
        return this.thumbColor;
    }

    public final void setThumbColor(ColorStateList colorStateList) {
        this.thumbColor = colorStateList;
    }

    public final ColorStateList getIconColor() {
        return this.iconColor;
    }

    public final void setIconColor(ColorStateList colorStateList) {
        this.iconColor = colorStateList;
    }

    public final int getIconSize() {
        return this.iconSize;
    }

    public final void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public final int getTextAppearanceRes() {
        return this.textAppearanceRes;
    }

    public final void setTextAppearanceRes(int textAppearanceRes) {
        this.textAppearanceRes = textAppearanceRes;
    }

    public final int getTextColor() {
        return this.textColor;
    }

    public final void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override // com.reddit.indicatorfastscroll.FastScrollerView.ItemIndicatorSelectedCallback
    public void onItemIndicatorSelected(FastScrollItemIndicator indicator, int indicatorCenterY, int itemPosition) {
        // No-op: thumb positioning/animation not wired up.
    }

    public final void setupWithFastScroller(FastScrollerView fastScrollerView) {
        if (isSetup()) {
            throw new IllegalStateException("Only set this view's FastScrollerView once!");
        }
        this.fastScrollerView = fastScrollerView;
        fastScrollerView.getItemIndicatorSelectedCallbacks().add(this);
        fastScrollerView.setOnItemIndicatorTouched$indicator_fast_scroll_release(
                new kotlin.jvm.functions.Function1<Boolean, kotlin.Unit>() {
                    @Override
                    public kotlin.Unit invoke(Boolean activated) {
                        FastScrollerThumbView.this.setActivated(activated);
                        return kotlin.Unit.INSTANCE;
                    }
                });
    }
}
