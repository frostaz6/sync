package com.resync.app.helpers;

import android.content.Context;

/* JADX INFO: renamed from: t7.f */
/* JADX INFO: compiled from: ChangelogHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class ChangelogHelper {
    public static boolean isFirstLaunch(Context context) {
        return !PreferencesHelper.getAccountPrefs("Changelog").contains("previous_version");
    }
}
