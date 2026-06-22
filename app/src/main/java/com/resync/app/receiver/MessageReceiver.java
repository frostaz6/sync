package com.resync.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.helpers.messaging.MessageSchedulingHelper;

/* JADX INFO: loaded from: classes.dex */
public class MessageReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        MessageSchedulingHelper.scheduleIfEnabled(context);
    }
}
