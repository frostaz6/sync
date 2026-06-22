package com.resync.app.util;

import com.resync.app.helpers.CrashlyticsHelper;

/* JADX INFO: renamed from: wc.i */
/* JADX INFO: compiled from: Logger.java */
/* JADX INFO: loaded from: classes3.dex */
public final class Logger {
    public static void debug(String str) {
        debug("reddit_sync", str);
    }

    public static void debug(String str, String str2) {
    }

    public static void logException(Exception exc) {
        logException(exc, true);
    }

    public static void logException(Exception exc, boolean z) {
        if (z) {
            CrashlyticsHelper.logException(exc);
        }
    }

    public static void verbose(String str) {
        verbose("reddit_sync", str);
    }

    public static void verbose(String str, String str2) {
    }
}
