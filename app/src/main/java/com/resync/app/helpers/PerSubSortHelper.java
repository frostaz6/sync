package com.resync.app.helpers;

import android.util.Pair;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import java.util.Locale;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.PostsHelper;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: b8.b */
/* JADX INFO: compiled from: PerSubSortHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PerSubSortHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static HashMap<String, String> sortMap = new HashMap<>();

    private static Pair<String, String> getDefaultSort(String str) {
        return "friends".equalsIgnoreCase(str) ? new Pair<>("New", null) : MaterialPostsHelper.isSearch(str) ? new Pair<>("Relevance", null) : MaterialPostsHelper.isSubSearch(str) ? new Pair<>("Relevance", null) : MaterialPostsHelper.isUserProfile(str) ? new Pair<>("New", null) : SettingsSingleton.getSettings().postSort;
    }

    public static Pair<String, String> getSortPair(String str) {
        return TextUtils.isEmpty(str) ? getDefaultSort(str) : hasSort(str) ? PostsHelper.parseSortPair(getSortKey(str.toLowerCase(Locale.ENGLISH))) : getDefaultSort(str);
    }

    public static String getSortKey(String str) {
        if (!TextUtils.isEmpty(str) && hasSort(str)) {
            return sortMap.get(str.toLowerCase(Locale.ENGLISH));
        }
        return null;
    }

    public static boolean hasSort(String str) {
        return sortMap.containsKey(str.toLowerCase(Locale.ENGLISH));
    }

    public static void load() {
        loadInternal();
    }

    static void loadInternal() {
        GenericPerSubHelper.loadStringMap("per_sub_sorts", sortMap);
    }

    public static void removeSort(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        sortMap.remove(str.toLowerCase(Locale.ENGLISH));
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        save();
    }

    public static void save() {
        GenericPerSubHelper.saveStringMap("per_sub_sorts", sortMap);
    }

    public static void setSort(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        sortMap.put(str.toLowerCase(Locale.ENGLISH), str2);
        BusSingleton.getBus().post(new SubredditsChangedEvent());
        save();
    }
}
