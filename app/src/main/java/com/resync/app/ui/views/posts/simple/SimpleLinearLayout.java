package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.RippleUtils;

/* JADX INFO: loaded from: classes3.dex */
public class SimpleLinearLayout extends LinearLayoutCompat {
    int position;
    boolean borderless;
    GradientDrawable background;

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(8));
        }
    }

    public SimpleLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(1);
        setElevation(0.0f);
        GradientDrawable gradientDrawable = new GradientDrawable();
        this.background = gradientDrawable;
        setBackground(gradientDrawable);
        setForeground(RippleUtils.getSelectableBackground());
        applyBackgroundColor();
    }

    private void applyBackgroundColor() {
        this.background.setColor(MonetUtil.getBackgroundColorAuto());
    }

    public void removeForeground() {
        this.borderless = true;
        setForeground(null);
    }

    public void enableRoundedCorners() {
        setOutlineProvider(new RoundedOutlineProvider());
        setClipToOutline(true);
    }

    public void disableRoundedCorners() {
        setOutlineProvider(null);
        setClipToOutline(false);
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
