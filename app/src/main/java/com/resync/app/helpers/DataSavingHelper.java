package com.resync.app.helpers;

import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ConnectivityHelper;

/* JADX INFO: renamed from: w7.a */
/* JADX INFO: compiled from: DataSavingHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class DataSavingHelper {
    public static boolean isFullDataSaving() {
        return SettingsSingleton.getSettings().dataSavingMode == 4;
    }

    public static boolean shouldSaveData() {
        int dataSavingMode = SettingsSingleton.getSettings().dataSavingMode;
        boolean wifiConnected = ConnectivityHelper.isWifiConnected();
        boolean mobileConnected = ConnectivityHelper.isMobileConnected();
        if (dataSavingMode == 0) {
            return false;
        }
        if (1 == dataSavingMode) {
            return true;
        }
        if (!wifiConnected && mobileConnected && 2 == dataSavingMode) {
            return true;
        }
        return (wifiConnected && 3 == dataSavingMode) || 4 == dataSavingMode;
    }
}
