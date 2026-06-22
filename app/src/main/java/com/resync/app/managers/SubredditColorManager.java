package com.resync.app.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: d8.n */
/* JADX INFO: compiled from: SubredditColorManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class SubredditColorManager {
    private static final Object LOCK = new Object();
    private static SubredditColorManager instance;

    private HashMap<String, Integer> colors = new HashMap<>();

    private SubredditColorManager() {
        for (String str : new HashSet<String>(PreferencesHelper.getDefaultPrefs("SubredditColorManager").getStringSet("subredddit_color", new HashSet()))) {
            String str2 = str.split("###subreddit_color_###")[0];
            Integer numValueOf = Integer.valueOf(Integer.parseInt(str.split("###subreddit_color_###")[1]));
            if (!TextUtils.isEmpty(str2)) {
                this.colors.put(str2, numValueOf);
            }
        }
        if (!this.colors.containsKey("frontpage")) {
            setColor("frontpage", -48383);
        }
        if (!this.colors.containsKey("random")) {
            setColor("random", -16121);
        }
        if (!this.colors.containsKey("popular")) {
            setColor("popular", -11684180);
        }
        if (this.colors.containsKey("friends")) {
            return;
        }
        setColor("friends", -278483);
    }

    public static SubredditColorManager getInstance() {
        SubredditColorManager subredditColorManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new SubredditColorManager();
            }
            subredditColorManager = instance;
        }
        return subredditColorManager;
    }

    public void setColor(String str, Integer num) {
        String strTrim = str.toLowerCase(Locale.ENGLISH).trim();
        this.colors.remove(strTrim);
        this.colors.put(strTrim, num);
        HashSet hashSet = new HashSet();
        for (String str2 : this.colors.keySet()) {
            hashSet.add(str2 + "###subreddit_color_###" + this.colors.get(str2));
        }
        PreferencesHelper.getDefaultPrefs("SubredditColorManager").edit().putStringSet("subredddit_color", hashSet).apply();
    }

    public Integer getColor(String str) {
        if (StringUtils.isEmpty(str)) {
            return -11624729;
        }
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (this.colors.containsKey(lowerCase)) {
            return this.colors.get(lowerCase);
        }
        return -11624729;
    }
}
