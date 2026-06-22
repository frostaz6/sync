package com.resync.app.helpers;

import java.text.DecimalFormat;
import java.util.Locale;

/* JADX INFO: renamed from: t7.u0 */
/* JADX INFO: compiled from: ViewsHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ViewsHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static DecimalFormat FORMAT_K = new DecimalFormat("#k views");
    private static DecimalFormat FORMAT_K_DECIMAL = new DecimalFormat("#.#k views");

    public static String formatViews(int count) {
        return count >= 100000 ? FORMAT_K.format(((double) count) / 1000.0d) : count >= 10000 ? FORMAT_K_DECIMAL.format(((double) count) / 1000.0d) : count == 1 ? String.format(Locale.getDefault(), "%d view", Integer.valueOf(count)) : String.format(Locale.getDefault(), "%d views", Integer.valueOf(count));
    }
}
