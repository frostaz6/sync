package com.resync.app.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.resync.app.RedditApplication;
import java.util.HashMap;
import org.apache.commons.lang3.ObjectUtils;
import com.resync.app.net.OAuthRateHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: com.resync.app.singleton.a */
/* JADX INFO: compiled from: AccountSingleton.java */
/* JADX INFO: loaded from: classes.dex */
public final class AccountSingleton {
    static final String TAG = "a";
    private static final Object LOCK = new Object();

    private static AccountSingleton instance;

    private String username;
    private String refreshToken;

    /* JADX INFO: renamed from: com.resync.app.singleton.a$a */
    /* JADX INFO: compiled from: AccountSingleton.java */
    class MatchAllRequestFilter1 implements RequestQueue.RequestFilter {
        MatchAllRequestFilter1() {
        }

        @Override // com.android.volley.RequestQueue.RequestFilter
        public boolean apply(Request<?> request) {
            return true;
        }
    }

    /* JADX INFO: renamed from: com.resync.app.singleton.a$b */
    /* JADX INFO: compiled from: AccountSingleton.java */
    class MatchAllRequestFilter2 implements RequestQueue.RequestFilter {
        MatchAllRequestFilter2() {
        }

        @Override // com.android.volley.RequestQueue.RequestFilter
        public boolean apply(Request<?> request) {
            return true;
        }
    }

    /* JADX INFO: renamed from: com.resync.app.singleton.a$c */
    /* JADX INFO: compiled from: AccountSingleton.java */
    class MatchAllRequestFilter3 implements RequestQueue.RequestFilter {
        MatchAllRequestFilter3() {
        }

        @Override // com.android.volley.RequestQueue.RequestFilter
        public boolean apply(Request<?> request) {
            return true;
        }
    }

    private AccountSingleton() {
        SharedPreferences sharedPreferencesE = PreferencesHelper.getDefaultPrefs("AccountSingleton");
        this.refreshToken = sharedPreferencesE.getString("refresh_token", null);
        this.username = sharedPreferencesE.getString("username", "Logged out");
    }

    public static HashMap<String, String> getDefaultHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put("User-Agent", getUserAgent());
        return map;
    }

    public static String obfuscateUsername(String str) {
        if ("Logged out".equalsIgnoreCase(str)) {
            return "Anonymous";
        }
        if (!SettingsSingleton.getSettings().hide_username) {
            return str;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str.charAt(0));
        for (int i = 0; i < str.substring(1).length(); i++) {
            sb2.append("*");
        }
        return sb2.toString();
    }

    public static AccountSingleton getInstance() {
        AccountSingleton accountSingleton;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new AccountSingleton();
            }
            accountSingleton = instance;
        }
        return accountSingleton;
    }

    public static HashMap<String, String> getAuthHeaders(String str) {
        HashMap<String, String> headers = getDefaultHeaders();
        if (!TextUtils.isEmpty(str)) {
            headers.put("Authorization", "bearer " + str);
        }
        return headers;
    }

    public static String getUserAgent() {
        return "android:com.resync.app:v1.0";
    }

    public static boolean isLoggedIn(Context context) {
        return !TextUtils.isEmpty(PreferencesHelper.getDefaultPrefs(context, "AccountSingleton").getString("refresh_token", null));
    }

    public String getDisplayUsername() {
        if ("Logged out".equalsIgnoreCase(getUsername())) {
            return "Anonymous";
        }
        String currentUsername = getUsername();
        if (!SettingsSingleton.getSettings().hide_username) {
            return currentUsername;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(currentUsername.charAt(0));
        for (int i = 0; i < currentUsername.substring(1).length(); i++) {
            sb2.append("*");
        }
        return sb2.toString();
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(getRefreshToken());
    }

    public void logout() {
        updateSession("Logged out", null, true);
        // Logged out -> tear down the reddit.com web session (cookies + cached bearer) so the next
        // request mints a fresh ANONYMOUS token instead of silently reusing the previous user's
        // session (which would still read /best etc. as that user). With this cleared, signed-out
        // browsing is genuinely anonymous.
        try {
            com.resync.app.auth.WebSessionManager.get(RedditApplication.getAppContext()).logout();
        } catch (Throwable t) {
            Logger.verbose("WebSession logout failed: " + t);
        }
    }

    public void updateSession(String str, String str2, boolean z) {
        if (ObjectUtils.equals(str, getUsername()) && ObjectUtils.equals(str2, getRefreshToken()) && !z) {
            Logger.verbose(TAG, "Updating session skipped!");
            return;
        }
        String str3 = TAG;
        Logger.verbose(str3, "Updating session!");
        SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("AccountSingleton: update session").edit();
        editorEdit.putString("username", str);
        editorEdit.putString("refresh_token", str2);
        editorEdit.apply();
        this.username = str;
        this.refreshToken = str2;
        Logger.verbose(str3, "Loaded account: " + this.username);
        Logger.verbose(str3, "Loaded refresh token: " + this.refreshToken);
        OAuthRateHelper.reset();
        RedditApplication.mainRequestQueue.cancelAll((RequestQueue.RequestFilter) new MatchAllRequestFilter1());
        RedditApplication.oauthRequestQueue.cancelAll((RequestQueue.RequestFilter) new MatchAllRequestFilter2());
        RedditApplication.thirdPartyRequestQueue.cancelAll((RequestQueue.RequestFilter) new MatchAllRequestFilter3());
        if (z) {
            Logger.verbose("Clearing all caches!");
            RedditApplication.mainRequestQueue.start();
            RedditApplication.oauthRequestQueue.start();
            RedditApplication.thirdPartyRequestQueue.start();
        }
    }
}
