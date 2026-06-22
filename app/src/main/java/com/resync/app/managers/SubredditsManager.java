package com.resync.app.managers;

import com.android.volley.Response;
import com.resync.app.RedditApplication;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.HashSet;
import com.resync.app.net.oauth.OAuthSubscribeRequest;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.net.oauth.OAuthSubIdRequest;

/* JADX INFO: renamed from: d8.p */
/* JADX INFO: compiled from: SubredditsManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class SubredditsManager {
    private static final Object LOCK = new Object();
    private static SubredditsManager instance;

    private SubredditArrayList subreddits;

    /* JADX INFO: compiled from: SubredditsManager.java */
    class SubscribeListener implements Response.Listener<String> {

        final /* synthetic */ String subreddit;

        SubscribeListener(String str) {
            this.subreddit = str;
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(String str) {
            OAuthRequestManager.sendWithAppContext(new OAuthSubscribeRequest(RedditApplication.getAppContext(), 1, this.subreddit, str, null, null));
        }
    }

    /* JADX INFO: compiled from: SubredditsManager.java */
    class UnsubscribeListener implements Response.Listener<String> {

        final /* synthetic */ String subreddit;

        UnsubscribeListener(String str) {
            this.subreddit = str;
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(String str) {
            OAuthRequestManager.sendWithAppContext(new OAuthSubscribeRequest(RedditApplication.getAppContext(), 0, this.subreddit, str, null, null));
        }
    }

    private SubredditsManager() {
        load();
    }

    public static SubredditsManager getInstance() {
        SubredditsManager subredditsManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new SubredditsManager();
            }
            subredditsManager = instance;
        }
        return subredditsManager;
    }

    private static String getPrefsName() {
        return "SubredditsManager";
    }

    public void addSubreddit(String str) {
        this.subreddits.addIfAbsent(str);
        this.subreddits.sort(SubredditArrayList.SORT_DEFAULT);
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_subreddits", this.subreddits.toSet()).apply();
    }

    public void setSubreddits(SubredditArrayList subredditArrayList) {
        this.subreddits.clear();
        this.subreddits.addAll(subredditArrayList);
        this.subreddits.sort(SubredditArrayList.SORT_DEFAULT);
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_subreddits", this.subreddits.toSet()).apply();
    }

    public SubredditArrayList getSubreddits() {
        return this.subreddits;
    }

    public boolean isSubscribed(String str) {
        return this.subreddits.contains(str);
    }

    public void load() {
        SubredditArrayList subredditArrayList = new SubredditArrayList(PreferencesHelper.getAccountPrefs(getPrefsName()).getStringSet("user_subreddits", new HashSet()));
        this.subreddits = subredditArrayList;
        subredditArrayList.sort(SubredditArrayList.SORT_DEFAULT);
    }

    public void removeSubreddit(String str) {
        this.subreddits.removeIgnoreCase(str);
        this.subreddits.sort(SubredditArrayList.SORT_DEFAULT);
        FavouriteSubredditManager.getInstance().removeFavourite(str);
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        PreferencesHelper.getAccountPrefs(getPrefsName()).edit().putStringSet("user_subreddits", this.subreddits.toSet()).apply();
    }

    public void toggleSubscription(String str) {
        if (isSubscribed(str)) {
            removeSubreddit(str);
            OAuthRequestManager.sendWithAppContext(new OAuthSubIdRequest(RedditApplication.getAppContext(), str, new SubscribeListener(str), null));
        } else {
            addSubreddit(str);
            OAuthRequestManager.sendWithAppContext(new OAuthSubIdRequest(RedditApplication.getAppContext(), str, new UnsubscribeListener(str), null));
        }
    }
}
