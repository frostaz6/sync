package com.resync.app.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: t7.n0 */
/* JADX INFO: compiled from: TimestampHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class TimestampHelper {
    public static String getRelativeTime(long epochSeconds) {
        if (epochSeconds == 0) {
            return null;
        }
        long elapsedSeconds = (System.currentTimeMillis() / 1000) - epochSeconds;
        if (elapsedSeconds >= 31536000) {
            return ((((elapsedSeconds / 60) / 60) / 24) / 365) + "y";
        }
        if (elapsedSeconds >= 86400) {
            long days = ((elapsedSeconds / 60) / 60) / 24;
            return (elapsedSeconds / 86400) + "d";
        }
        if (elapsedSeconds >= 3600) {
            return ((elapsedSeconds / 60) / 60) + "h";
        }
        if (elapsedSeconds < 60) {
            return "just now";
        }
        return (elapsedSeconds / 60) + "m";
    }

    public static String getAccountAgeString(long epochSeconds) {
        if (epochSeconds == 0) {
            return null;
        }
        long elapsedSeconds = (System.currentTimeMillis() / 1000) - epochSeconds;
        Logger.verbose("Diff: " + elapsedSeconds);
        if (elapsedSeconds >= 31536000) {
            long years = (((elapsedSeconds / 60) / 60) / 24) / 365;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("redditor for ");
            sb2.append(years);
            sb2.append(" year");
            sb2.append(years <= 1 ? "" : "s");
            return sb2.toString();
        }
        if (elapsedSeconds >= 86400) {
            long days = ((elapsedSeconds / 60) / 60) / 24;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("redditor for ");
            sb3.append(days);
            sb3.append(" day");
            sb3.append(days <= 1 ? "" : "s");
            return sb3.toString();
        }
        if (elapsedSeconds >= 3600) {
            long hours = (elapsedSeconds / 60) / 60;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("redditor for ");
            sb4.append(hours);
            sb4.append(" hour");
            sb4.append(hours <= 1 ? "" : "s");
            return sb4.toString();
        }
        if (elapsedSeconds < 60) {
            return "redditor for just now";
        }
        long minutes = elapsedSeconds / 60;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("redditor for ");
        sb5.append(minutes);
        sb5.append(" minute");
        sb5.append(minutes <= 1 ? "" : "s");
        return sb5.toString();
    }

    public static String getFormattedDate(long epochSeconds) {
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(epochSeconds * 1000));
    }
}
