package com.resync.app.managers;

import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.HashSet;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: d8.d */
/* JADX INFO: compiled from: MultiredditManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class MultiredditManager {
    private static final Object LOCK = new Object();
    private static MultiredditManager instance;

    private SubredditArrayList multireddits;

    private MultiredditManager() {
        load();
    }

    public static MultiredditManager getInstance() {
        MultiredditManager multiredditManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new MultiredditManager();
            }
            multiredditManager = instance;
        }
        return multiredditManager;
    }

    private static String getPrefsName() {
        return "MultiredditManager";
    }

    public void addMultireddit(String str) {
        this.multireddits.addIfAbsent(str);
        this.multireddits.sort(SubredditArrayList.SORT_DEFAULT);
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_multireddits", this.multireddits.toSet()).apply();
    }

    public void setMultireddits(SubredditArrayList subredditArrayList) {
        this.multireddits.clear();
        for (String str : subredditArrayList) {
            this.multireddits.addIfAbsent("multi_" + str);
        }
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_multireddits", this.multireddits.toSet()).apply();
    }

    public SubredditArrayList getMultireddits() {
        return this.multireddits;
    }

    public void load() {
        SubredditArrayList subredditArrayList = new SubredditArrayList(PreferencesHelper.getAccountPrefs(getPrefsName()).getStringSet("user_multireddits", new HashSet()));
        this.multireddits = subredditArrayList;
        subredditArrayList.sort(SubredditArrayList.SORT_DEFAULT);
    }

    public void removeMultireddit(String str) {
        this.multireddits.removeIgnoreCase(str);
        this.multireddits.sort(SubredditArrayList.SORT_DEFAULT);
        FavouriteSubredditManager.getInstance().removeFavourite(str);
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_multireddits", this.multireddits.toSet()).apply();
    }
}
