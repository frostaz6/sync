package com.resync.app.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: loaded from: classes.dex */
public class SettingChangeReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        ((NotificationManager) context.getSystemService("notification")).cancel(intent.getIntExtra("id", -1));
        String stringExtra = intent.getStringExtra("key");
        boolean booleanExtra = intent.getBooleanExtra("value", false);
        if (StringUtils.isNotEmpty(stringExtra)) {
            SettingsSingleton.getInstance().putBoolean(stringExtra, booleanExtra);
            if (booleanExtra) {
                StaticToast.show("Enabled");
            } else {
                StaticToast.show("Disabled");
            }
        }
    }
}
