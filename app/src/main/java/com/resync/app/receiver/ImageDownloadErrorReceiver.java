package com.resync.app.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: loaded from: classes.dex */
public class ImageDownloadErrorReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        LinkHelper.open(context, intent.getStringExtra("url"));
        ((NotificationManager) context.getSystemService("notification")).cancel(intent.getIntExtra("id", -1));
    }
}
