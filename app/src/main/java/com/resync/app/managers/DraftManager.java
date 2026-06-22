package com.resync.app.managers;

import android.content.Context;
import android.content.SharedPreferences;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import java.util.HashSet;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: f9.a */
/* JADX INFO: compiled from: DraftManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class DraftManager {
    public static boolean hasDrafts() {
        return getDrafts().size() > 0;
    }

    public static void clearDrafts() {
        PreferencesHelper.getAccountPrefs("DraftManager").edit().remove("editor_drafts").apply();
    }

    public static ArrayList<String> getDrafts() {
        ArrayList<String> arrayList = new ArrayList<>();
        SharedPreferences sharedPreferencesG = PreferencesHelper.getAccountPrefs("DraftManager");
        if (sharedPreferencesG.contains("editor_drafts")) {
            arrayList.addAll(sharedPreferencesG.getStringSet("editor_drafts", new HashSet()));
        }
        return arrayList;
    }

    public static void deleteDraft(String str) {
        ArrayList<String> arrayListC = getDrafts();
        arrayListC.remove(str);
        if (arrayListC.size() > 0) {
            PreferencesHelper.getAccountPrefs("DraftManager").edit().putStringSet("editor_drafts", new HashSet(arrayListC)).apply();
        } else {
            clearDrafts();
        }
    }

    public static void saveDraft(Context context, String str) {
        StaticToast.showWithContext(context, "Draft saved");
        ArrayList<String> arrayListC = getDrafts();
        arrayListC.add(str);
        if (arrayListC.size() > 0) {
            PreferencesHelper.getAccountPrefs("DraftManager").edit().putStringSet("editor_drafts", new HashSet(arrayListC)).apply();
        } else {
            clearDrafts();
        }
    }
}
