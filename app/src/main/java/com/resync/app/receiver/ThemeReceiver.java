package com.resync.app.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.RedditApplication;

/* JADX INFO: loaded from: classes.dex */
public class ThemeReceiver extends BroadcastReceiver {
    public static String getAction() {
        return "com.resync.app.theme";
    }

    public static void broadcast() {
        RedditApplication.getAppContext().sendBroadcast(new Intent(getAction()));
    }

    public static void unregister(Activity activity, ThemeReceiver themeReceiver) {
        if (themeReceiver != null) {
            activity.unregisterReceiver(themeReceiver);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
    }
}
