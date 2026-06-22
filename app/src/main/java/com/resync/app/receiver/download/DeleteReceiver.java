package com.resync.app.receiver.download;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.documentfile.provider.DocumentFile;
import com.resync.app.RedditApplication;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class DeleteReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            DocumentFile.fromSingleUri(RedditApplication.getAppContext(), Uri.parse(intent.getStringExtra("uri"))).delete();
        } catch (Exception e) {
            Logger.logException(e);
            StaticToast.show("Error deleting file");
        }
        ((NotificationManager) context.getSystemService("notification")).cancel("final", intent.getIntExtra("id", -1));
        StaticToast.show("Deleted!");
    }
}
