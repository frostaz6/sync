package com.resync.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class SystemMonetThemeReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Logger.verbose("SYSTEM THEME CHANGED");
        MonetUtil.reload();
        MonetThemeReceiver.broadcast();
    }
}
