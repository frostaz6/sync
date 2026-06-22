package com.resync.app.helpers;

import android.content.SharedPreferences;
import com.resync.app.R;

/* JADX INFO: renamed from: t7.q */
/* JADX INFO: compiled from: IconHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class IconHelper {
    public static int getCurrentIconRes() {
        int iconId = PreferencesHelper.getDefaultPrefs("IconHelper").getInt("icon", 0);
        if (iconId == 0) {
            return R.mipmap.launcher_outline_light_22;
        }
        if (iconId == 5) {
            return R.mipmap.launcher_outline_dark_22;
        }
        if (iconId == 1) {
            return R.mipmap.ic_launcher_legacy;
        }
        if (iconId == 2) {
            return R.mipmap.ic_launcher_new_fancy;
        }
        if (iconId == 3) {
            return R.mipmap.ic_pride_launcher;
        }
        if (iconId == 4) {
            return R.mipmap.ic_blm_launcher;
        }
        throw new RuntimeException("Unsupported icon");
    }

    public static int getSystemIconRes() {
        return PreferencesHelper.getDefaultPrefs("IconHelper").getInt("icon", 0) == 1 ? R.drawable.sync_system_old : R.drawable.sync_system_new;
    }

    public static void setIcon(int iconId) {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("IconHelper").edit();
        editorEdit.putInt("icon", iconId);
        editorEdit.apply();
    }
}
