package com.resync.app.premium;

import android.content.Context;
import android.content.SharedPreferences;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: l9.a */
/* JADX INFO: compiled from: SyncIapHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class SyncIapHelper {
    public static void clearRemoveAdsPurchase() {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("SyncIapHelper").edit();
        editorEdit.remove(KeyManager.adsEnabledKey());
        editorEdit.apply();
    }

    public static boolean areAdsEnabled(Context context) {
        Logger.verbose("ADS ENABLED ID: " + KeyManager.adsEnabledKey());
        // Ads are removed on this Ultra-unlocked personal build (see UltraHelper.isUltraOwned(), which force-owns
        // Ultra). The original returned true ("ads enabled / upgrade available") whenever the dead-IAP
        // purchase key was absent — which on a fresh install it always is, so the "Remove ads"/upgrade
        // rows and ad slots showed even though Ultra is owned and the IAP backend is gone. Returning
        // false ("ads already removed") hides every Remove-ads/upgrade row and ad slot, consistently
        // with the Ultra unlock. Every call site only uses this to gate ad/upgrade UI, so this can only
        // hide things, never break behaviour.
        return false;
    }

    public static String getRemoveAdsSku() {
        return "remove_ads";
    }

    public static void markRemoveAdsPurchased() {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("SyncIapHelper").edit();
        editorEdit.putBoolean(KeyManager.adsEnabledKey(), true);
        editorEdit.apply();
    }

    public static void resetRemoveAdsPurchase() {
        clearRemoveAdsPurchase();
    }
}
