package com.resync.app.util;

import android.graphics.Color;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: renamed from: wc.d */
/* JADX INFO: compiled from: ColorUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class ColorUtils {
    public static String toHexColor(int color) {
        return String.format("#%02X%02X%02X", Integer.valueOf(Color.red(color)), Integer.valueOf(Color.green(color)), Integer.valueOf(Color.blue(color)));
    }

    @Deprecated
    public static boolean isDark(int color) {
        return isDark(color, NightHelper.isNight());
    }

    @Deprecated
    public static boolean isDark(int color, boolean night) {
        return night ? ((((double) Color.red(color)) * 0.299d) + (((double) Color.green(color)) * 0.587d)) + (((double) Color.blue(color)) * 0.114d) < 100.0d : ((((double) Color.red(color)) * 0.299d) + (((double) Color.green(color)) * 0.587d)) + (((double) Color.blue(color)) * 0.114d) < 200.0d;
    }
}
