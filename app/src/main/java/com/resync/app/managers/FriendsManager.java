package com.resync.app.managers;

import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.HashSet;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: d8.b */
/* JADX INFO: compiled from: FriendsManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class FriendsManager {
    private static final Object LOCK = new Object();
    private static FriendsManager instance;

    private SubredditArrayList friends;

    private FriendsManager() {
        load();
    }

    public static FriendsManager getInstance() {
        FriendsManager friendsManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new FriendsManager();
            }
            friendsManager = instance;
        }
        return friendsManager;
    }

    private static String getPrefsName() {
        return "FriendsManager";
    }

    public void addFriend(String str) {
        this.friends.addIfAbsent(str);
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_friends", this.friends.toSet()).apply();
    }

    public void setFriends(SubredditArrayList subredditArrayList) {
        this.friends.clear();
        this.friends.addAll(subredditArrayList);
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_friends", this.friends.toSet()).apply();
    }

    public String[] getFriends() {
        return this.friends.toStringArray();
    }

    public boolean isFriend(String str) {
        return this.friends.contains(str);
    }

    public void load() {
        this.friends = new SubredditArrayList(PreferencesHelper.getAccountPrefs(getPrefsName()).getStringSet("user_friends", new HashSet()));
    }

    public void removeFriend(String str) {
        this.friends.removeIgnoreCase(str);
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_friends", this.friends.toSet()).apply();
    }
}
