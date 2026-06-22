package com.resync.app.helpers;

import java.text.DecimalFormat;
import java.util.Locale;

/* JADX INFO: renamed from: t7.e0 */
/* JADX INFO: compiled from: ScoreHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ScoreHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static DecimalFormat FORMAT_K = new DecimalFormat("#k points");
    private static DecimalFormat FORMAT_K_DECIMAL = new DecimalFormat("#.#k points");

    public static String formatPoints(int count) {
        return count >= 100000 ? FORMAT_K.format(((double) count) / 1000.0d) : count >= 10000 ? FORMAT_K_DECIMAL.format(((double) count) / 1000.0d) : count == 1 ? String.format(Locale.getDefault(), "%d point", Integer.valueOf(count)) : String.format(Locale.getDefault(), "%d points", Integer.valueOf(count));
    }

    public static String formatCountCompact(int count) {
        if (count < 0) {
            count = 0;
        }
        return count >= 100000 ? new DecimalFormat("#k").format(((double) count) / 1000.0d) : count >= 10000 ? new DecimalFormat("#.#k").format(((double) count) / 1000.0d) : count == 1 ? String.format(Locale.getDefault(), "%d", Integer.valueOf(count)) : String.format(Locale.getDefault(), "%d", Integer.valueOf(count));
    }

    public static String formatPointsLabel(int count) {
        return count == 1 ? String.format(Locale.getDefault(), "%d point", Integer.valueOf(count)) : String.format(Locale.getDefault(), "%d points", Integer.valueOf(count));
    }

    public static String formatCountCompactMillions(int count) {
        return count >= 1000000 ? new DecimalFormat("#.#M").format(((double) count) / 1000000.0d) : count >= 100000 ? new DecimalFormat("#k").format(((double) count) / 1000.0d) : count >= 10000 ? new DecimalFormat("#.#k").format(((double) count) / 1000.0d) : count == 1 ? String.format(Locale.getDefault(), "%d", Integer.valueOf(count)) : String.format(Locale.getDefault(), "%d", Integer.valueOf(count));
    }
}
