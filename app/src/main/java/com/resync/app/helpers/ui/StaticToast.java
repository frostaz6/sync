package com.resync.app.helpers.ui;

import android.content.Context;
import android.widget.Toast;
import com.resync.app.RedditApplication;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: gb.p */
/* JADX INFO: compiled from: StaticToast.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class StaticToast {
    public static void debugToast(String str) {
    }

    public static void show(Context context, int messageRes) {
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), messageRes, 0).show();
        }
    }

    @Deprecated
    public static void showWithContext(Context context, String str) {
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), str, 0).show();
        }
    }

    public static void show(String str) {
        Logger.verbose(str);
        Toast.makeText(RedditApplication.getAppContext(), str, 0).show();
    }

    @Deprecated
    public static void showWithContext(String str, Context context) {
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), str, 0).show();
        }
    }
}
