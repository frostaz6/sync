package com.resync.app.receiver.download;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.UUID;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class CancelledReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("id", -1);
        UUID uuid = (UUID) intent.getSerializableExtra("job_uid");
        Logger.verbose("JOB ID THERE: " + uuid);
        WorkManagerHelper.cancelWorkById(uuid);
        ((NotificationManager) context.getSystemService("notification")).cancel("final", intExtra);
        intent.removeExtra("id");
        intent.removeExtra("job_uid");
    }
}
