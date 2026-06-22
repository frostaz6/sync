package com.resync.app.ui.views.posts.swipe;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.resync.app.ui.activities.CasualGalleryActivity;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: loaded from: classes3.dex */
public class SwipeLinearLayout extends LinearLayout {

    private ColorDrawable backgroundDrawable;

    public SwipeLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupBackground();
    }

    private int minAlpha() {
        return ((getContext() instanceof CasualGalleryActivity) || NightHelper.isNight()) ? 143 : 175;
    }

    void setupBackground() {
        if ((getContext() instanceof CasualGalleryActivity) || NightHelper.isNight()) {
            this.backgroundDrawable = new ColorDrawable(-14606047);
        } else {
            this.backgroundDrawable = new ColorDrawable(-1);
        }
        this.backgroundDrawable.setAlpha(minAlpha());
        setBackgroundDrawable(this.backgroundDrawable);
    }

    public void setProgress(float progress) {
        this.backgroundDrawable.setAlpha(Math.max(minAlpha(), (int) (progress * 255.0f)));
    }
}
