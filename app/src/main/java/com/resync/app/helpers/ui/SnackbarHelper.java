package com.resync.app.helpers.ui;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: gb.o */
/* JADX INFO: compiled from: SnackbarHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public final class SnackbarHelper {
    public static Snackbar make(View view, int textRes, int duration) {
        return applyStyle(Snackbar.make(view, textRes, duration));
    }

    public static Snackbar make(View view, String text, int duration) {
        return applyStyle(Snackbar.make(view, text, duration));
    }

    private static Snackbar applyStyle(Snackbar snackbar) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(SizeHelper.dpToPxFloat(25));
        if (NightHelper.isNight()) {
            int backgroundColor = MonetUtil.getToolbarColorAuto();
            int textColor = MonetUtil.getPrimaryTextColorAuto();
            snackbar.setBackgroundTint(backgroundColor);
            gradientDrawable.setColor(backgroundColor);
            snackbar.setTextColor(textColor);
            snackbar.setActionTextColor(textColor);
        } else {
            snackbar.setBackgroundTint(-14671580);
            gradientDrawable.setColor(-14671580);
            snackbar.setTextColor(-1);
            snackbar.setActionTextColor(-1);
        }
        snackbar.getView().setBackground(gradientDrawable);
        snackbar.getView().setClipToOutline(true);
        return snackbar;
    }
}
