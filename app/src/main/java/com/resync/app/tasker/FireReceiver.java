package com.resync.app.tasker;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;
import com.resync.app.service.SyncService;
import com.resync.app.helpers.CrashlyticsHelper;

/* JADX INFO: loaded from: classes.dex */
public class FireReceiver extends WakefulBroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("com.resync.app.subreddit");
        boolean booleanExtra = intent.getBooleanExtra("com.resync.app.comments", false);
        int intExtra = intent.getIntExtra("com.resync.app.pages", 5);
        Intent intent2 = new Intent(context, (Class<?>) SyncService.class);
        intent2.putExtra("com.resync.app.subreddit", stringExtra);
        intent2.putExtra("com.resync.app.comments", booleanExtra);
        intent2.putExtra("com.resync.app.pages", intExtra);
        try {
            WakefulBroadcastReceiver.startWakefulService(context, intent2);
        } catch (Exception e) {
            CrashlyticsHelper.logException(e);
        }
    }
}
