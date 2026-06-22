package com.resync.app.helpers;

import android.content.Context;
import android.view.View;
import com.resync.app.R;
import com.resync.app.ui.activities.CasualGalleryActivity;

/* JADX INFO: renamed from: t7.m0 */
/* JADX INFO: compiled from: ThemeHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class ThemeHelper {
    public static void applyButtonBackground(Context context, View view) {
        if (NightHelper.isNight() || (context instanceof CasualGalleryActivity)) {
            view.setBackgroundResource(R.drawable.button_night);
        } else {
            view.setBackgroundResource(R.drawable.button_light);
        }
    }

    public static void applyEmptyButtonBackground(View view) {
        view.setBackgroundResource(R.drawable.button_empty);
    }

    public static void applyExtendedButtonBackground(Context context, View view) {
        if (NightHelper.isNight() || (context instanceof CasualGalleryActivity)) {
            view.setBackgroundResource(R.drawable.button_night_extended);
        } else {
            view.setBackgroundResource(R.drawable.button_light_extended);
        }
    }
}
