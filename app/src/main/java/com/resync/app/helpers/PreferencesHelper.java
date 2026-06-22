package com.resync.app.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import java.util.Locale;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: t7.z */
/* JADX INFO: compiled from: PreferencesHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PreferencesHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String LOG_TAG = "z";
    public static String ACCOUNT_MARKER_KEY = "set";

    private PreferencesHelper() {
    }

    public static String getAccountName() {
        String username = AccountSingleton.getInstance().getUsername();
        if (TextUtils.isEmpty(username)) {
            return null;
        }
        return username.toLowerCase(Locale.ENGLISH);
    }

    public static String toLowerCase(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.toLowerCase(Locale.ENGLISH);
    }

    public static SharedPreferences getDefaultPrefs() {
        return getDefaultPrefs(RedditApplication.getAppContext(), null);
    }

    public static SharedPreferences getDefaultPrefs(Context context, String str) {
        if (!TestUtils.isRobolectric()) {
            Logger.verbose(LOG_TAG, "Returning default setting: " + str);
        }
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences getDefaultPrefs(String str) {
        return getDefaultPrefs(RedditApplication.getAppContext(), str);
    }

    public static SharedPreferences getAccountPrefs(Context context, String str) {
        String accountName = getAccountName();
        if (!TextUtils.isEmpty(accountName)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(accountName, 0);
            if (sharedPreferences.contains(ACCOUNT_MARKER_KEY)) {
                if (!TestUtils.isRobolectric()) {
                    Logger.verbose(LOG_TAG, "Returning account setting: " + str);
                }
                return sharedPreferences;
            }
        }
        if (!TestUtils.isRobolectric()) {
            Logger.verbose(LOG_TAG, "Returning generic setting: " + str);
        }
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences getAccountPrefs(String str) {
        return getAccountPrefs(RedditApplication.getAppContext(), str);
    }

    public static SharedPreferences getAccountPrefsForUser(Context context, String str, String str2) {
        String lowerUsername = toLowerCase(str);
        if (!TextUtils.isEmpty(lowerUsername)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(lowerUsername, 0);
            if (sharedPreferences.contains(ACCOUNT_MARKER_KEY)) {
                if (!TestUtils.isRobolectric()) {
                    Logger.verbose(LOG_TAG, "Returning account setting: " + str2);
                }
                return sharedPreferences;
            }
        }
        if (!TestUtils.isRobolectric()) {
            Logger.verbose(LOG_TAG, "Returning generic setting: " + str2);
        }
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean hasAccountPrefs() {
        String accountName = getAccountName();
        return !TextUtils.isEmpty(accountName) && RedditApplication.getAppContext().getSharedPreferences(accountName, 0).contains(ACCOUNT_MARKER_KEY);
    }

    public static synchronized boolean hasPrefsForUser(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (RedditApplication.getAppContext().getSharedPreferences(str, 0).contains(ACCOUNT_MARKER_KEY)) {
                return true;
            }
        }
        return false;
    }

    public static void markPrefsAsSet(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        RedditApplication.getAppContext().getSharedPreferences(str.toLowerCase(Locale.ENGLISH), 0).edit().putBoolean(ACCOUNT_MARKER_KEY, true).apply();
    }
}
