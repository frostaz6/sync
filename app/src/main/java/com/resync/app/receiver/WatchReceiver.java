package com.resync.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.jobs.WatchSubredditJob;

/* JADX INFO: loaded from: classes.dex */
public class WatchReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        WatchSubredditJob.schedule(context);
    }
}
