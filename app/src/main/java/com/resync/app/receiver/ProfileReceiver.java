package com.resync.app.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.ui.activities.CasualActivity;

/* JADX INFO: loaded from: classes.dex */
public class ProfileReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        CasualActivity.openUser(context, intent.getStringExtra("user"));
        ((NotificationManager) context.getSystemService("notification")).cancel(intent.getIntExtra("id", -1));
    }
}
