package com.resync.app.receiver.download;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.jobs.media.DownloadAlbumJob;
import com.resync.app.jobs.media.DownloadMediaJob;
import com.resync.app.jobs.media.DownloadRedditVideoJob;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes.dex */
public class RetryReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("title");
        String stringExtra2 = intent.getStringExtra("subreddit");
        String stringExtra3 = intent.getStringExtra("url");
        int intExtra = intent.getIntExtra("mode", 1);
        if (stringExtra3.contains(",")) {
            DownloadAlbumJob.enqueue(stringExtra, stringExtra2, stringExtra3, intExtra);
        } else if (LinkTypeHelper.isRedditVideo(stringExtra3)) {
            DownloadRedditVideoJob.enqueue(stringExtra, stringExtra2, stringExtra3, intExtra);
        } else {
            DownloadMediaJob.enqueue(stringExtra, stringExtra2, stringExtra3, intExtra);
        }
        ((NotificationManager) context.getSystemService("notification")).cancel("error", intent.getIntExtra("id", -1));
    }
}
