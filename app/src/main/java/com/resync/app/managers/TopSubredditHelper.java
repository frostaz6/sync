package com.resync.app.managers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import com.bumptech.glide.MaterialPostsHelper;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: d8.q */
/* JADX INFO: compiled from: TopSubredditHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class TopSubredditHelper {

    static final String TAG = "q";

    /* JADX INFO: compiled from: TopSubredditHelper.java */
    static class EntryValueComparator implements Comparator<Map.Entry<String, Integer>> {
        EntryValueComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Map.Entry<String, Integer> entry, Map.Entry<String, Integer> entry2) {
            int iCompareTo = entry.getValue().compareTo(entry2.getValue());
            return iCompareTo != 0 ? iCompareTo : entry2.getKey().compareToIgnoreCase(entry.getKey());
        }
    }

    /* JADX INFO: compiled from: TopSubredditHelper.java */
    static class ShortcutIconTarget extends SimpleTarget<Bitmap> {

        final /* synthetic */ Bitmap[] icons;

        final /* synthetic */ int index;

        final /* synthetic */ String[] subreddits;

        ShortcutIconTarget(Bitmap[] bitmapArr, int index, String[] strArr) {
            this.icons = bitmapArr;
            this.index = index;
            this.subreddits = strArr;
        }

        @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
        public void onLoadFailed(Drawable drawable) {
            Logger.verbose("Updating shortcuts: failed");
            int index = this.index;
            String[] strArr = this.subreddits;
            if (index < strArr.length - 1) {
                TopSubredditHelper.loadShortcutIcons(strArr, this.icons, index + 1);
            } else {
                TopSubredditHelper.applyShortcuts(strArr, this.icons);
            }
        }

        @Override // com.bumptech.glide.request.target.Target
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            Logger.verbose("Updating shortcuts:" + bitmap);
            Bitmap[] bitmapArr = this.icons;
            int index = this.index;
            bitmapArr[index] = bitmap;
            String[] strArr = this.subreddits;
            if (index < strArr.length - 1) {
                TopSubredditHelper.loadShortcutIcons(strArr, bitmapArr, index + 1);
            } else {
                TopSubredditHelper.applyShortcuts(strArr, bitmapArr);
            }
        }
    }

    public static void clearTopSubreddits() {
        if (TestUtils.isRobolectric()) {
            return;
        }
        PreferencesHelper.getAccountPrefs("TopSubredditHelper").edit().remove("top_subreddits_new").apply();
        removeAllShortcuts();
    }

    private static void removeAllShortcuts() {
        try {
            if (Build.VERSION.SDK_INT >= 25) {
                ((ShortcutManager) RedditApplication.getAppContext().getSystemService(ShortcutManager.class)).removeAllDynamicShortcuts();
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    public static void removeSubVisit(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        Logger.verbose("Saving sub visit: " + str);
        if (MaterialPostsHelper.isModAbout(str)) {
            Logger.verbose("Mod sub visit skipped: " + str);
            return;
        }
        if (MaterialPostsHelper.isUserProfile(str)) {
            Logger.verbose("Saving sub visit skipped: " + str);
            return;
        }
        if (MaterialPostsHelper.isDomain(str)) {
            Logger.verbose("Saving sub visit skipped: " + str);
            return;
        }
        if (MaterialPostsHelper.isSearch(str)) {
            Logger.verbose("Saving sub visit skipped: " + str);
            return;
        }
        Map<String, Integer> topSubreddits = loadTopSubreddits();
        if (topSubreddits.get(str) != null) {
            topSubreddits.remove(str);
        }
        saveTopSubreddits(context, topSubreddits);
    }

    private static Map<String, Integer> loadTopSubreddits() {
        if (!PreferencesHelper.getAccountPrefs("TopSubredditHelper").contains("top_subreddits_new") || !SettingsSingleton.getSettings().recordTopSubreddits) {
            return new HashMap();
        }
        Set<String> stringSet = PreferencesHelper.getAccountPrefs("TopSubredditHelper").getStringSet("top_subreddits_new", new HashSet());
        HashMap map = new HashMap();
        Iterator<String> it = stringSet.iterator();
        while (it.hasNext()) {
            String[] strArrSplit = it.next().split("::");
            Logger.verbose(TAG, "Loading sub: " + strArrSplit[0] + " : " + strArrSplit[1]);
            map.put(strArrSplit[0], Integer.valueOf(Integer.parseInt(strArrSplit[1])));
        }
        return map;
    }

    public static Integer getVisitCount(String str) {
        return loadTopSubreddits().get(str);
    }

    public static String[] getTopSubreddits() {
        Map<String, Integer> sortedSubreddits = sortByValue(loadTopSubreddits());
        Logger.verbose(TAG, "Top values: " + sortedSubreddits);
        ArrayList arrayList = new ArrayList(sortedSubreddits.keySet());
        Collections.reverse(arrayList);
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    static String[] getTopFour(Map<String, Integer> map) {
        Map<String, Integer> sortedSubreddits = sortByValue(map);
        Logger.verbose(TAG, "Top values: " + sortedSubreddits);
        ArrayList arrayList = new ArrayList(sortedSubreddits.keySet());
        Collections.reverse(arrayList);
        ArrayList arrayList2 = new ArrayList(arrayList.subList(0, Math.min(4, arrayList.size())));
        return (String[]) arrayList2.toArray(new String[arrayList2.size()]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loadShortcutIcons(String[] strArr, Bitmap[] bitmapArr, int index) {
        String iconUrl = SubredditIconManager.getInstance().getIcon(strArr[index]);
        Logger.verbose("Updating shortcuts:" + iconUrl);
        if (StringUtils.isNotEmpty(iconUrl)) {
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asBitmap().load(iconUrl).transform(new CircleCrop()).into(new ShortcutIconTarget(bitmapArr, index, strArr));
        } else if (index < strArr.length - 1) {
            loadShortcutIcons(strArr, bitmapArr, index + 1);
        } else {
            applyShortcuts(strArr, bitmapArr);
        }
    }

    private static void saveTopSubreddits(Context context, Map<String, Integer> map) {
        if (!SettingsSingleton.getSettings().recordTopSubreddits) {
            removeAllShortcuts();
            return;
        }
        HashSet hashSet = new HashSet();
        for (String str : map.keySet()) {
            Logger.verbose(TAG, "Saving sub: " + str + " : " + map.get(str));
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("::");
            sb2.append(Integer.toString(map.get(str).intValue()));
            hashSet.add(sb2.toString());
        }
        PreferencesHelper.getAccountPrefs("TopSubredditHelper").edit().putStringSet("top_subreddits_new", hashSet).apply();
        updateShortcuts(context, map);
        BusSingleton.getBus().post(new SubredditsChangedEvent());
    }

    public static void saveSubVisit(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        Logger.verbose("Saving sub visit: " + str);
        if (MaterialPostsHelper.isModAbout(str)) {
            Logger.verbose("Mod sub visit skipped: " + str);
            return;
        }
        if (MaterialPostsHelper.isUserProfile(str)) {
            Logger.verbose("Saving sub visit skipped: " + str);
            return;
        }
        if (MaterialPostsHelper.isDomain(str)) {
            Logger.verbose("Saving sub visit skipped: " + str);
            return;
        }
        if (MaterialPostsHelper.isSearch(str)) {
            Logger.verbose("Saving sub visit skipped: " + str);
            return;
        }
        if (MaterialPostsHelper.isSubSearch(str)) {
            Logger.verbose("Saving full sub visit skipped: " + str);
            return;
        }
        Map<String, Integer> topSubreddits = loadTopSubreddits();
        if (topSubreddits.get(str) != null) {
            topSubreddits.put(str, Integer.valueOf(topSubreddits.get(str).intValue() + 1));
        } else {
            topSubreddits.put(str, 1);
        }
        saveTopSubreddits(context, topSubreddits);
    }

    static Map<String, Integer> sortByValue(Map<String, Integer> map) {
        LinkedList<Map.Entry<String, Integer>> linkedList = new LinkedList<>(map.entrySet());
        Collections.sort(linkedList, new EntryValueComparator());
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry<String, Integer> entry : linkedList) {
            linkedHashMap.put((String) entry.getKey(), (Integer) entry.getValue());
        }
        return linkedHashMap;
    }

    private static void updateShortcuts(Context context, Map<String, Integer> map) {
        Logger.verbose(TAG, "Updating shortcuts!");
        try {
            if (Build.VERSION.SDK_INT >= 25) {
                String[] strArrI = getTopFour(map);
                loadShortcutIcons(strArrI, new Bitmap[strArrI.length], 0);
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyShortcuts(String[] strArr, Bitmap[] bitmapArr) {
        ArrayList arrayList = new ArrayList();
        Logger.verbose("Updating shortcuts:" + strArr.length);
        int length = bitmapArr.length;
        for (int i = 0; i < length; i++) {
            Logger.verbose("Updating shortcuts:" + bitmapArr[i]);
        }
        for (int i = 0; i < strArr.length; i++) {
            ShortcutInfo.Builder intent = new ShortcutInfo.Builder(RedditApplication.getAppContext(), strArr[i]).setShortLabel(strArr[i]).setLongLabel(strArr[i]).setIntent(new Intent("android.intent.action.VIEW", Uri.parse("sync://reddit.com/r/" + strArr[i])));
            arrayList.add((bitmapArr[i] != null ? intent.setIcon(Icon.createWithBitmap(bitmapArr[i])) : intent.setIcon(Icon.createWithResource(RedditApplication.getAppContext(), R.mipmap.ic_shortcut_subreddit))).build());
        }
        ShortcutManager shortcutManager = (ShortcutManager) RedditApplication.getAppContext().getSystemService(ShortcutManager.class);
        if (arrayList.size() <= 0 || !SettingsSingleton.getSettings().iconShortcuts) {
            shortcutManager.removeAllDynamicShortcuts();
            return;
        }
        boolean dynamicShortcuts = shortcutManager.setDynamicShortcuts(arrayList);
        Logger.verbose(TAG, "Set dynamic shortcuts: " + dynamicShortcuts);
    }
}
