package com.resync.app.helpers;

import android.os.Parcelable;
import android.text.TextUtils;
import androidx.collection.LruCache;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: v7.a */
/* JADX INFO: compiled from: CommentsStateHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class CommentsStateHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static LruCache<String, Parcelable> stateCache = new LruCache<>(100);

    public static void saveState(String str, Parcelable parcelable) {
        if (!SettingsSingleton.getSettings().commentsRememberPos || TextUtils.isEmpty(str) || parcelable == null) {
            return;
        }
        if (stateCache.get(str) != null) {
            stateCache.remove(str);
        }
        Logger.verbose("Saving state: " + parcelable);
        stateCache.put(str, parcelable);
    }

    public static Parcelable popState(String str) {
        Parcelable parcelableE = stateCache.remove(str);
        Logger.verbose("Getting state: " + parcelableE);
        return parcelableE;
    }

    public static boolean hasState(String str) {
        return SettingsSingleton.getSettings().commentsRememberPos && stateCache.get(str) != null;
    }
}
