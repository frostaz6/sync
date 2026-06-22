package com.resync.app.managers;

import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.helpers.ui.SubredditArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthFavRequest;

/* JADX INFO: renamed from: d8.a */
/* JADX INFO: compiled from: FavouriteSubredditManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class FavouriteSubredditManager {
    private static final Object LOCK = new Object();
    private static FavouriteSubredditManager instance;

    private SubredditArrayList favourites;

    /* JADX INFO: compiled from: FavouriteSubredditManager.java */
    class SaveTask extends Thread {

        final /* synthetic */ StringBuilder data;

        SaveTask(StringBuilder sb2) {
            this.data = sb2;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            PreferencesHelper.getAccountPrefs(FavouriteSubredditManager.getPrefsName()).edit().putString("user_favorites", this.data.toString()).apply();
        }
    }

    private FavouriteSubredditManager() {
        load();
    }

    public static FavouriteSubredditManager getInstance() {
        FavouriteSubredditManager favouriteSubredditManager;
        Logger.verbose("GETTING FAV");
        synchronized (LOCK) {
            if (instance == null) {
                instance = new FavouriteSubredditManager();
            }
            favouriteSubredditManager = instance;
        }
        return favouriteSubredditManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getPrefsName() {
        return "FavouriteSubredditManager";
    }

    public void addFavourite(String str) {
        SubredditArrayList subredditArrayList = new SubredditArrayList(this.favourites.toStringArray());
        subredditArrayList.sort(SubredditArrayList.SORT_DEFAULT);
        boolean zEquals = subredditArrayList.equals(this.favourites);
        this.favourites.addIfAbsent(str);
        if (zEquals) {
            this.favourites.sort(SubredditArrayList.SORT_DEFAULT);
        }
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        if (AccountSingleton.getInstance().isLoggedIn() && !MaterialPostsHelper.isMultireddit(str)) {
            OAuthRequestManager.send(RedditApplication.getAppContext(), new OAuthFavRequest(RedditApplication.getAppContext(), 0, str, null, null));
        }
        save();
    }

    public void addAllFavourites(SubredditArrayList subredditArrayList) {
        boolean zIsEmpty = this.favourites.isEmpty();
        this.favourites.addAll(subredditArrayList);
        if (zIsEmpty) {
            this.favourites.sort(SubredditArrayList.SORT_DEFAULT);
        }
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        save();
    }

    public void clearFavourites() {
        this.favourites.clear();
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        save();
    }

    public SubredditArrayList getFavourites() {
        return this.favourites;
    }

    public boolean isFavourite(String str) {
        return this.favourites.contains(str);
    }

    public void load() {
        this.favourites = new SubredditArrayList(StringUtils.split(PreferencesHelper.getAccountPrefs(getPrefsName()).getString("user_favorites", null), ","));
    }

    public void removeFavourite(String str) {
        if (this.favourites.removeIgnoreCase(str)) {
            BusSingleton.getBus().post(new SubredditsChangedEvent());
            if (AccountSingleton.getInstance().isLoggedIn() && !MaterialPostsHelper.isMultireddit(str)) {
                OAuthRequestManager.send(RedditApplication.getAppContext(), new OAuthFavRequest(RedditApplication.getAppContext(), 1, str, null, null));
            }
            save();
        }
    }

    public void save() {
        StringBuilder sb2 = new StringBuilder();
        int i = 0;
        while (i < this.favourites.size()) {
            sb2.append(this.favourites.get(i));
            i++;
            if (i < this.favourites.size()) {
                sb2.append(",");
            }
        }
        new SaveTask(sb2).start();
    }

    public void setFavourites(SubredditArrayList subredditArrayList) {
        this.favourites.clear();
        this.favourites.addAll(subredditArrayList);
        save();
    }

    public void toggleFavourite(String str) {
        if (isFavourite(str)) {
            removeFavourite(str);
        } else {
            addFavourite(str);
        }
    }
}
