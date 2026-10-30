package com.resync.app.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.helpers.ui.SubredditArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.provider.RedditProvider;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthFavRequest;

/* JADX INFO: renamed from: d8.a */
/* JADX INFO: compiled from: FavouriteSubredditManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class FavouriteSubredditManager {
    private static final Object LOCK = new Object();
    private static FavouriteSubredditManager instance;

    private SubredditArrayList favourites;

    /*
     * Previously getAccountPrefs() fell back to DEFAULT preferences unless
     * the account had a "set" marker. Web-session login omitted that marker,
     * silently sharing user_favorites between accounts.
     *
     * Favorites always use a per-account file now, regardless of the marker.
     * The old global list is retained as a backup and copied only to the
     * oldest saved real account on its first load.
     */
    private static SharedPreferences getFavouritesPrefs() {
        Context context = RedditApplication.getAppContext();
        String accountName = PreferencesHelper.getAccountName();
        if (TextUtils.isEmpty(accountName) || "logged out".equalsIgnoreCase(accountName)) {
            return PreferencesHelper.getDefaultPrefs();
        }
        SharedPreferences ownPrefs = context.getSharedPreferences(accountName, Context.MODE_PRIVATE);
        if (!ownPrefs.contains("user_favorites") && isOldestSavedAccount(context, accountName)) {
            String legacy = PreferencesHelper.getDefaultPrefs().getString("user_favorites", null);
            if (legacy != null) {
                // Commit before returning to avoid any first-load/switch race.
                ownPrefs.edit().putString("user_favorites", legacy).commit();
                android.util.Log.i("ReSyncFavourites",
                        "Migrated legacy global favorites to oldest saved account");
            }
        }
        return ownPrefs;
    }

    private static boolean isOldestSavedAccount(Context context, String username) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(RedditProvider.URI_ACCOUNTS,
                    new String[]{"account_name"}, null, null, "account_id ASC");
            if (cursor == null) return false;
            int index = cursor.getColumnIndex("account_name");
            if (index < 0) return false;
            while (cursor.moveToNext()) {
                String name = cursor.getString(index);
                if (!TextUtils.isEmpty(name) && !"Logged out".equalsIgnoreCase(name)) {
                    return username.equalsIgnoreCase(name);
                }
            }
        } catch (Exception e) {
            // Never guess the previous owner or copy legacy favorites to all users.
            android.util.Log.w("ReSyncFavourites", "Couldn't resolve legacy favorites owner", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
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
        this.favourites = new SubredditArrayList(StringUtils.split(getFavouritesPrefs().getString("user_favorites", null), ","));
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
        // Persist against the account active at call time, not a worker thread
        // that might run after the user has switched to another account.
        getFavouritesPrefs().edit().putString("user_favorites", sb2.toString()).apply();
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
