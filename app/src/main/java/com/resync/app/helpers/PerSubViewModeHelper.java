package com.resync.app.helpers;

import com.resync.app.singleton.UiSingleton;
import java.util.HashMap;
import java.util.Locale;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: b8.c */
/* JADX INFO: compiled from: PerSubViewModeHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PerSubViewModeHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static HashMap<String, Integer> viewModeMap = new HashMap<>();

    private static int getDefaultViewMode() {
        return UiSingleton.getInstance().getUiMode();
    }

    public static int getViewMode(String subreddit) {
        return TextUtils.isEmpty(subreddit) ? getDefaultViewMode() : hasViewMode(subreddit) ? viewModeMap.get(subreddit.toLowerCase(Locale.ENGLISH)).intValue() : getDefaultViewMode();
    }

    public static boolean hasViewMode(String subreddit) {
        if (android.text.TextUtils.isEmpty(subreddit)) {
            return false;
        }
        return viewModeMap.containsKey(subreddit.toLowerCase(Locale.ENGLISH));
    }

    public static void load() {
        loadInternal();
    }

    static void loadInternal() {
        GenericPerSubHelper.loadIntMap("per_sub_views_", viewModeMap);
    }

    public static void removeViewMode(String subreddit) {
        if (TextUtils.isEmpty(subreddit)) {
            return;
        }
        viewModeMap.remove(subreddit.toLowerCase(Locale.ENGLISH));
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        save();
    }

    public static void save() {
        GenericPerSubHelper.saveIntMap("per_sub_views_", viewModeMap);
    }

    public static void setViewMode(String subreddit, int viewMode) {
        if (TextUtils.isEmpty(subreddit)) {
            return;
        }
        viewModeMap.put(subreddit.toLowerCase(Locale.ENGLISH), Integer.valueOf(viewMode));
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        save();
    }
}
