package com.resync.app.managers;

import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.HashSet;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: d8.c */
/* JADX INFO: compiled from: ModeratedSubredditManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class ModeratedSubredditManager {
    private static final Object LOCK = new Object();
    private static ModeratedSubredditManager instance;

    private SubredditArrayList moderated;

    private ModeratedSubredditManager() {
        load();
    }

    public static ModeratedSubredditManager getInstance() {
        ModeratedSubredditManager moderatedSubredditManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ModeratedSubredditManager();
            }
            moderatedSubredditManager = instance;
        }
        return moderatedSubredditManager;
    }

    private static String getPrefsName() {
        return "ModeratedSubredditManager";
    }

    public void setModerated(SubredditArrayList subredditArrayList) {
        this.moderated.clear();
        this.moderated.addAll(subredditArrayList);
        this.moderated.sort(SubredditArrayList.SORT_DEFAULT);
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_moderated", this.moderated.toSet()).apply();
    }

    public SubredditArrayList getModerated() {
        return this.moderated;
    }

    public boolean hasModerated() {
        return getModerated() != null && getModerated().size() > 0;
    }

    public boolean isModerated(String str) {
        return getModerated().contains(str);
    }

    public void load() {
        SubredditArrayList subredditArrayList = new SubredditArrayList(PreferencesHelper.getAccountPrefs(getPrefsName()).getStringSet("user_moderated", new HashSet()));
        this.moderated = subredditArrayList;
        subredditArrayList.sort(SubredditArrayList.SORT_DEFAULT);
    }
}
