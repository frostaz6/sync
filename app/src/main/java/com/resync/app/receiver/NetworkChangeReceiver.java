package com.resync.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Logger.verbose("Network changed!");
        Logger.verbose("Network online: " + ConnectivityHelper.isConnected());
        Logger.verbose("Network wifi connected: " + ConnectivityHelper.isWifiConnected());
        Logger.verbose("Network wifi connecting: " + ConnectivityHelper.isWifiConnectedOrConnecting());
        Logger.verbose("Network cell data available: " + ConnectivityHelper.isMobileConnected());
    }
}
