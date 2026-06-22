package com.resync.app.helpers;

import java.text.DecimalFormat;
import java.util.Locale;

/* JADX INFO: renamed from: t7.j0 */
/* JADX INFO: compiled from: SubscribedHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class SubscribedHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final DecimalFormat FORMAT_K = new DecimalFormat("#k subs");
    private static final DecimalFormat FORMAT_K_DECIMAL = new DecimalFormat("#.#k subs");

    public static String formatSubscribers(int count) {
        return count >= 1000000 ? String.format("%.2fm subs", Double.valueOf(((double) count) / 1000000.0d)) : count >= 100000 ? FORMAT_K.format(((double) count) / 1000.0d) : count >= 10000 ? FORMAT_K_DECIMAL.format(((double) count) / 1000.0d) : count == 1 ? String.format(Locale.getDefault(), "%d subs", Integer.valueOf(count)) : String.format(Locale.getDefault(), "%d subs", Integer.valueOf(count));
    }
}
