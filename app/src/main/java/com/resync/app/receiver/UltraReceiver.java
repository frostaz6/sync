package com.resync.app.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.RedditApplication;

/* JADX INFO: loaded from: classes.dex */
public class UltraReceiver extends BroadcastReceiver {
    public static String getAction() {
        return "com.resync.app.ultra";
    }

    public static void broadcast() {
        RedditApplication.getAppContext().sendBroadcast(new Intent(getAction()));
    }

    public static void unregister(Activity activity, UltraReceiver ultraReceiver) {
        if (ultraReceiver != null) {
            activity.unregisterReceiver(ultraReceiver);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
    }
}
