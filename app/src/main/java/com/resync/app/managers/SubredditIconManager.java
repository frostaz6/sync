package com.resync.app.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: d8.o */
/* JADX INFO: compiled from: SubredditIconManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class SubredditIconManager {
    private static final Object LOCK = new Object();
    private static SubredditIconManager instance;

    private HashMap<String, String> icons = new HashMap<>();

    private SubredditIconManager() {
        for (String str : new HashSet<String>(PreferencesHelper.getDefaultPrefs("SubredditIconManager").getStringSet("subredddit_icon", new HashSet()))) {
            String str2 = str.split("###SUBREDDIT_icon###")[0];
            String str3 = str.split("###SUBREDDIT_icon###")[1];
            if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
                this.icons.put(str2, str3);
            }
        }
    }

    public static SubredditIconManager getInstance() {
        SubredditIconManager subredditIconManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new SubredditIconManager();
            }
            subredditIconManager = instance;
        }
        return subredditIconManager;
    }

    public void setIcon(String str, String str2) {
        if (StringUtils.equalsIgnoreCase(str, "random")) {
            return;
        }
        Logger.verbose("Adding icon: " + str + "  - " + str2);
        String strTrim = str.toLowerCase(Locale.ENGLISH).trim();
        this.icons.remove(strTrim);
        this.icons.put(strTrim, str2);
        HashSet hashSet = new HashSet();
        for (String str3 : this.icons.keySet()) {
            hashSet.add(str3 + "###SUBREDDIT_icon###" + this.icons.get(str3));
        }
        PreferencesHelper.getDefaultPrefs("SubredditIconManager").edit().putStringSet("subredddit_icon", hashSet).apply();
    }

    public String getIcon(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return this.icons.get(str.toLowerCase(Locale.ENGLISH));
    }
}
