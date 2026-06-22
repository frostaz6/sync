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
            ConnectivityManager cm = getConnectivityManager();
            android.net.Network active = cm.getActiveNetwork();
            if (active != null) {
                android.net.NetworkCapabilities caps = cm.getNetworkCapabilities(active);
                if (caps != null) {
                    return caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR);
                }
            }
        } catch (Exception unused) {
        }
        try {
            return getConnectivityManager().getNetworkInfo(0).isConnected();
        } catch (Exception unused2) {
            return false;
        }
    }

    public static boolean isWifiConnected() {
        // Prefer the modern NetworkCapabilities API (the legacy getNetworkInfo(TYPE_WIFI) is
        // deprecated and returns null/false on newer Android, which broke "autoplay on WiFi only").
        // Fall back to the legacy check only if capabilities are unavailable.
        try {
            ConnectivityManager cm = getConnectivityManager();
            android.net.Network active = cm.getActiveNetwork();
            if (active != null) {
                android.net.NetworkCapabilities caps = cm.getNetworkCapabilities(active);
                if (caps != null) {
                    return caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI);
                }
            }
        } catch (Exception unused) {
        }
        try {
            return getConnectivityManager().getNetworkInfo(1).isConnected();
        } catch (Exception unused2) {
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
