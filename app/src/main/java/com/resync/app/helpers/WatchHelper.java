package com.resync.app.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Pair;
import com.resync.app.jobs.WatchSubredditJob;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.WatchEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.WatchAlertDialogBottomSheet;

/* JADX INFO: renamed from: t7.w0 */
/* JADX INFO: compiled from: WatchHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class WatchHelper {
    protected static boolean clearWatchPrefs() {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("WatchHelper").edit();
        editorEdit.remove("WATCH_KEY");
        return editorEdit.commit();
    }

    protected static Pair<String, String> parseWatchEntry(String str) {
        String[] strArrSplit = str.split(":");
        return new Pair<String, String>(strArrSplit[0], strArrSplit[1]);
    }

    public static String getWatchedColor(String str) {
        String strLowerCase = StringUtils.lowerCase(str);
        if (isWatched(strLowerCase)) {
            return getWatchMap().get(strLowerCase);
        }
        return null;
    }

    public static Set<String> getWatchedKeys() {
        return getWatchMap().keySet();
    }

    public static boolean hasWatches() {
        return PreferencesHelper.getAccountPrefs("WatchHelper").contains("WATCH_KEY");
    }

    public static boolean isWatched(String str) {
        return getWatchMap().containsKey(StringUtils.lowerCase(str));
    }

    protected static HashMap<String, String> getWatchMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        String string = PreferencesHelper.getAccountPrefs("WatchHelper").getString("WATCH_KEY", null);
        String[] strArrSplit = TextUtils.isEmpty(string) ? null : string.split(";");
        if (ArrayUtils.isNotEmpty(strArrSplit)) {
            for (String str : strArrSplit) {
                Pair<String, String> pairB = parseWatchEntry(str);
                map.put((String) pairB.first, (String) pairB.second);
            }
        }
        return map;
    }

    protected static boolean removeWatch(String str) {
        String strLowerCase = StringUtils.lowerCase(str);
        HashMap<String, String> watches = getWatchMap();
        ArrayList arrayList = new ArrayList();
        watches.remove(strLowerCase);
        if (watches.size() <= 0) {
            return clearWatchPrefs();
        }
        for (String str2 : watches.keySet()) {
            arrayList.add(serializeWatchEntry(str2, watches.get(str2)));
        }
        String strJoin = StringUtils.join(arrayList, ";");
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("WatchHelper").edit();
        editorEdit.putString("WATCH_KEY", strJoin);
        return editorEdit.commit();
    }

    public static boolean addOrUpdateWatch(String str, String str2) {
        String strLowerCase = StringUtils.lowerCase(str);
        HashMap<String, String> watches = getWatchMap();
        ArrayList arrayList = new ArrayList();
        watches.remove(strLowerCase);
        watches.put(strLowerCase, str2);
        for (String str3 : watches.keySet()) {
            arrayList.add(serializeWatchEntry(str3, watches.get(str3)));
        }
        String strJoin = StringUtils.join(arrayList, ";");
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("WatchHelper").edit();
        editorEdit.putString("WATCH_KEY", strJoin);
        return editorEdit.commit();
    }

    protected static String serializeWatchEntry(String str, String str2) {
        return StringUtils.join(StringUtils.lowerCase(str), ":", str2);
    }

    public static void toggleWatch(Context context, String str) {
        if (isWatched(str)) {
            unwatch(context, str);
        } else {
            showWatchDialog(context, str);
        }
    }

    public static void unwatch(Context context, String str) {
        StaticToast.showWithContext(context, "Unwatching: " + str);
        removeWatch(str);
        BusSingleton.getBus().post(new WatchEvent());
        WatchSubredditJob.schedule(context);
    }

    public static void showWatchDialog(Context context, String str) {
        FragmentUtils.showWithSubreddit(WatchAlertDialogBottomSheet.class, ContextHelper.getFragmentManager(context), str);
    }
}
