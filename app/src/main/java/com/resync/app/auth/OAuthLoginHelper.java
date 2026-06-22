package com.resync.app.auth;

import android.content.Context;
import android.text.TextUtils;
import java.util.UUID;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: w8.b */
/* JADX INFO: compiled from: OAuthLoginHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class OAuthLoginHelper {
    public static boolean isStateValid(Context context, String str) {
        return TextUtils.equals(str, getStoredState(context));
    }

    private static String generateAndStoreState(Context context) {
        String string = UUID.randomUUID().toString();
        PreferencesHelper.getDefaultPrefs("OAuthLoginHelper").edit().putString("state", string).apply();
        return string;
    }

    public static String basicAuthHeader() {
        return "Basic UTQzZlNwVGU4TGNrRWc6dC1JQ" + "o6ZsbBJQ5R5JiP8YiQqJ9Ca2G448JOtKUTQzZlNwVGU4TGNrRWc6dC1JQe4MVHBQb2QaTQsxqc5Pf7jhCU83L9f58bj45eKgQK374i2lDQFgGEPFyEh20PnrPXF6ZHd1XzdwWk1rWU5EcjFDYWNvMVFr".substring(121, 152);
    }

    public static String buildAuthorizeUrl(Context context) {
        return "https://ssl.reddit.com/api/v1/authorize.compact?client_id=YOUR_REDDIT_CLIENT_ID&response_type=code&state=" + generateAndStoreState(context) + "&redirect_uri=" + redirectUri() + "&duration=permanent&scope=identity,edit,flair,history,modcontributors,modconfig,modflair,modlog,modposts,modwiki,mysubreddits,privatemessages,read,report,save,submit,subscribe,vote,wikiedit,wikiread,account";
    }

    private static String getStoredState(Context context) {
        return PreferencesHelper.getDefaultPrefs("OAuthLoginHelper").getString("state", null);
    }

    public static String redirectUri() {
        return "http://redditsync/auth";
    }

    public static boolean isRedirectUri(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.startsWith(redirectUri());
    }
}
