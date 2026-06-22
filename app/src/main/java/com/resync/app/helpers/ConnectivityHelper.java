package com.resync.app.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import com.resync.app.RedditApplication;

/* JADX INFO: renamed from: t7.i */
/* JADX INFO: compiled from: ConnectivityHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ConnectivityHelper {

    static ConnectivityManager sCachedManager;

    static final ConnectivityManager getConnectivityManager() {
        ConnectivityManager connectivityManager = sCachedManager;
        return connectivityManager != null ? connectivityManager : (ConnectivityManager) RedditApplication.getAppContext().getSystemService("connectivity");
    }

    public static final boolean isWifiConnectedOrConnecting() {
        try {
            return getConnectivityManager().getNetworkInfo(1).isConnectedOrConnecting();
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isMobileConnected() {
        try {
            return getConnectivityManager().getNetworkInfo(0).isConnected();
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isWifiConnected() {
        try {
            return getConnectivityManager().getNetworkInfo(1).isConnected();
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isConnected() {
        try {
            ConnectivityManager connectivityManagerA = getConnectivityManager();
            if (connectivityManagerA.getActiveNetworkInfo() == null) {
                return false;
            }
            return connectivityManagerA.getActiveNetworkInfo().isConnected();
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isConnected(Context context) {
        if (context == null) {
            return false;
        }
        try {
            ConnectivityManager connectivityManagerA = getConnectivityManager();
            if (connectivityManagerA.getActiveNetworkInfo() == null) {
                return false;
            }
            return connectivityManagerA.getActiveNetworkInfo().isConnected();
        } catch (Exception unused) {
            return false;
        }
    }
}
