package com.resync.app.net;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.android.volley.NetworkResponse;
import com.resync.app.singleton.AccountSingleton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import com.resync.app.helpers.GoogleAnalyticsHelper;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: d9.d */
/* JADX INFO: compiled from: OAuthRateHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class OAuthRateHelper {

    public static String KEY_RESET_TIME = "RATE_RESET_TIME";
    public static String KEY_REMAINING_CALLS = "RATE_REMAINING_CALLS";
    public static String KEY_LAST_UPDATED = "RATE_LAST_UPDATED";

    private static Date getLastUpdated() {
        return new Date(PreferencesHelper.getDefaultPrefs("OAuthRateHelper").getLong(KEY_LAST_UPDATED, 0L));
    }

    public static int getRemainingCalls() {
        return PreferencesHelper.getDefaultPrefs("OAuthRateHelper").getInt(KEY_REMAINING_CALLS, 600);
    }

    public static long getResetTime() {
        return PreferencesHelper.getDefaultPrefs("OAuthRateHelper").getLong(KEY_RESET_TIME, System.currentTimeMillis());
    }

    public static boolean isWithinRateLimit(String str) {
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            return true;
        }
        if (System.currentTimeMillis() >= getResetTime()) {
            return true;
        }
        boolean hasRemainingCalls = getRemainingCalls() > 0;
        if (!hasRemainingCalls) {
            GoogleAnalyticsHelper.trackEvent("Errors", "Rate limiting", "No remaining calls");
        }
        return hasRemainingCalls;
    }

    public static void updateFromResponse(NetworkResponse networkResponse, String str) {
        Map<String, String> map;
        if (networkResponse == null || (map = networkResponse.headers) == null || map.size() == 0) {
            return;
        }
        String str2 = map.get("date");
        String str3 = map.get("x-ratelimit-reset");
        String str4 = map.get("x-ratelimit-remaining");
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str4) || TextUtils.isEmpty(str3)) {
            return;
        }
        try {
            int resetSeconds = Integer.parseInt(str3);
            store(str, new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(str2), System.currentTimeMillis() + ((long) (resetSeconds * 1000)), (int) Float.parseFloat(str4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reset() {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("OAuthRateHelper").edit();
        editorEdit.remove(KEY_RESET_TIME);
        editorEdit.remove(KEY_REMAINING_CALLS);
        editorEdit.remove(KEY_LAST_UPDATED);
        editorEdit.apply();
    }

    private static void store(String str, Date date, long resetTime, int remainingCalls) {
        Date dateA = getLastUpdated();
        if (date.after(dateA) || date.equals(dateA)) {
            SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("OAuthRateHelper").edit();
            editorEdit.putLong(KEY_RESET_TIME, resetTime);
            editorEdit.putInt(KEY_REMAINING_CALLS, remainingCalls);
            editorEdit.putLong(KEY_LAST_UPDATED, date.getTime());
            editorEdit.apply();
        }
    }
}
