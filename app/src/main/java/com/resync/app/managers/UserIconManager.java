package com.resync.app.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.TextUtils;
import com.resync.app.net.oauth.OAuthAuthorThumbnailsRequest;

/* JADX INFO: renamed from: d8.r */
/* JADX INFO: compiled from: UserIconManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class UserIconManager {
    private static final Object LOCK = new Object();
    private static UserIconManager instance;

    private HashMap<String, String> icons = new HashMap<>();

    private UserIconManager() {
        for (String str : new HashSet<String>(PreferencesHelper.getDefaultPrefs("UserIconManager").getStringSet("user_icon", new HashSet()))) {
            String str2 = str.split("###user_icon###")[0];
            String str3 = str.split("###user_icon###")[1];
            if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
                this.icons.put(str2, str3);
            }
        }
    }

    public static UserIconManager getInstance() {
        UserIconManager userIconManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new UserIconManager();
            }
            userIconManager = instance;
        }
        return userIconManager;
    }

    public static void requestIcons(ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        OAuthRequestManager.sendWithAppContext(new OAuthAuthorThumbnailsRequest(arrayList));
    }

    public void setIcon(String str, String str2) {
        String strTrim = str.toLowerCase(Locale.ENGLISH).trim();
        this.icons.remove(strTrim);
        this.icons.put(strTrim, str2);
        HashSet hashSet = new HashSet();
        for (String str3 : this.icons.keySet()) {
            hashSet.add(str3 + "###user_icon###" + this.icons.get(str3));
        }
        PreferencesHelper.getDefaultPrefs("UserIconManager").edit().putStringSet("user_icon", hashSet).apply();
    }

    public String[] getUsernames() {
        if (this.icons.size() == 0) {
            return null;
        }
        String[] strArr = new String[this.icons.size()];
        this.icons.keySet().toArray(strArr);
        return strArr;
    }

    public synchronized String getIcon(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        return "logged out".equalsIgnoreCase(lowerCase) ? "https://www.redditstatic.com/avatars/avatar_default_12_FF8717.png" : this.icons.get(lowerCase);
    }
}
