package com.resync.app.helpers;

import android.view.WindowManager;
import com.resync.app.RedditApplication;

/* JADX INFO: renamed from: t7.y0 */
/* JADX INFO: compiled from: WindowUtils.java */
/* JADX INFO: loaded from: classes.dex */
public final class WindowUtils {
    public static int getDisplayHeight() {
        return ((WindowManager) RedditApplication.getAppContext().getSystemService("window")).getDefaultDisplay().getHeight();
    }

    public static int getDisplayWidth() {
        return ((WindowManager) RedditApplication.getAppContext().getSystemService("window")).getDefaultDisplay().getWidth();
    }
}
