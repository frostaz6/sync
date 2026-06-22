package com.resync.app.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/* JADX INFO: renamed from: t7.f0 */
/* JADX INFO: compiled from: SizeHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class SizeHelper {
    public static float dpToPxFloat(int dp) {
        return TypedValue.applyDimension(1, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static float dpToPxFloat(Context context, int dp) {
        return TypedValue.applyDimension(1, dp, context.getResources().getDisplayMetrics());
    }

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(1, dp, Resources.getSystem().getDisplayMetrics());
    }
}
