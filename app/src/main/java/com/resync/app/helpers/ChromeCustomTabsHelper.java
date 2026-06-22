package com.resync.app.helpers;

import com.resync.app.RedditApplication;
import com.resync.app.helpers.CustomTabsHelper;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: t7.g */
/* JADX INFO: compiled from: ChromeCustomTabsHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ChromeCustomTabsHelper {
    public static boolean isAvailable() {
        return !TextUtils.isEmpty(CustomTabsHelper.getPackageNameToUse(RedditApplication.getAppContext()));
    }
}
