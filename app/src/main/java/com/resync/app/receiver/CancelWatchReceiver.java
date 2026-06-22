package com.resync.app.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.helpers.WatchHelper;

/* JADX INFO: loaded from: classes.dex */
public class CancelWatchReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("notification_id", -1);
        WatchHelper.unwatch(context, intent.getStringExtra("subreddit"));
        ((NotificationManager) context.getSystemService("notification")).cancel(intExtra);
    }
}
