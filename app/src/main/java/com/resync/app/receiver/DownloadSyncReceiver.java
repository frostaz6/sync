package com.resync.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class DownloadSyncReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Logger.verbose("STARTED HERE");
        Intent intent2 = new Intent("android.intent.action.VIEW");
        intent2.setFlags(268435456);
        intent2.setData(Uri.parse("market://details?id=com.resync.app"));
        context.startActivity(intent2);
    }
}
