package com.resync.app.helpers;

import android.content.SharedPreferences;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.Collections;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.VisitedChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: t7.v0 */
/* JADX INFO: compiled from: VisitedManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class VisitedManager {
    public static void clearVisited() {
        if (TestUtils.isRobolectric()) {
            return;
        }
        PreferencesHelper.getAccountPrefs("VisitedManager").edit().remove("_visited_").apply();
        BusSingleton.getBus().post(new VisitedChangedEvent());
    }

    public static SubredditArrayList getVisitedReversed() {
        if (getVisitedArray() == null) {
            return new SubredditArrayList();
        }
        SubredditArrayList subredditArrayList = new SubredditArrayList(getVisitedArray());
        Collections.reverse(subredditArrayList);
        return subredditArrayList;
    }

    public static String[] getVisitedArray() {
        if (!SettingsSingleton.getSettings().recordHistory) {
            return null;
        }
        SharedPreferences sharedPreferencesG = PreferencesHelper.getAccountPrefs("VisitedManager");
        if (!sharedPreferencesG.contains("_visited_")) {
            return null;
        }
        String string = sharedPreferencesG.getString("_visited_", null);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return string.split("###VISITED###");
    }

    public static boolean hasVisited() {
        if (TestUtils.isRobolectric()) {
            return false;
        }
        return ArrayUtils.isNotEmpty(getVisitedArray());
    }

    public static void removeVisited(String str) {
        if (TestUtils.isRobolectric()) {
            return;
        }
        String[] strArrC = getVisitedArray();
        if (strArrC != null) {
            SubredditArrayList subredditArrayList = new SubredditArrayList(strArrC);
            subredditArrayList.removeIgnoreCase(str);
            if (subredditArrayList.size() == 0) {
                PreferencesHelper.getAccountPrefs("VisitedManager").edit().remove("_visited_").apply();
            } else {
                PreferencesHelper.getAccountPrefs("VisitedManager").edit().putString("_visited_", StringUtils.join(subredditArrayList.toStringArray(), "###VISITED###")).apply();
            }
        }
        BusSingleton.getBus().post(new VisitedChangedEvent());
    }

    public static void saveVisited(String str) {
        Logger.verbose("VisitedManager", "Saving visited: " + str);
        if (TestUtils.isRobolectric() || TextUtils.isEmpty(str) || !SettingsSingleton.getSettings().recordHistory) {
            return;
        }
        SubredditArrayList subredditArrayList = new SubredditArrayList();
        if (getVisitedArray() != null) {
            subredditArrayList.addAll(getVisitedArray());
        }
        subredditArrayList.remove(str);
        subredditArrayList.add(str);
        while (subredditArrayList.size() > 20) {
            subredditArrayList.remove(0);
        }
        if (subredditArrayList.size() == 0) {
            PreferencesHelper.getAccountPrefs("VisitedManager").edit().remove("_visited_").apply();
        } else {
            PreferencesHelper.getAccountPrefs("VisitedManager").edit().putString("_visited_", StringUtils.join(subredditArrayList.toStringArray(), "###VISITED###")).apply();
        }
        BusSingleton.getBus().post(new VisitedChangedEvent());
    }
}
