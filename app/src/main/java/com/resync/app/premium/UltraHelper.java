package com.resync.app.premium;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import com.resync.app.receiver.UltraReceiver;
import com.resync.app.ultra.jobs.UltraGrabPaintsJob;
import com.resync.app.ultra.jobs.UltraGrabTagsJob;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: uc.b */
/* JADX INFO: compiled from: UltraHelper.java */
/* JADX INFO: loaded from: classes3.dex */
public class UltraHelper {
    // Synthetic purchase token used to liberate the "second gate" Ultra features.
    // Many Ultra features gate not only on isUltra() (ownership) but ALSO on the stored Play
    // purchase token containing the substring ".AO-" (a real Play "AO-..." token marker).
    // Sync's IAP backend is dead so a genuine token can never be obtained; this constant
    // makes every ".AO-" check pass. It is intentionally non-null (safer than the old null
    // return for KeyManager.ultraSkuPassword()/decrypt()/encrypt(), which use it as an MD5/AES seed) and contains
    // ".AO-". The value is never sent anywhere useful: every server call that used to POST
    // it has been neutralised (see the Ultra jobs / cloud sheets / com.resync.app.premium.UserPaintManager / NewUserTagManager).
    private static final String SYNTHETIC_ULTRA_TOKEN = "liberated.AO-ultra";

    // Guards the one-time write-through of SYNTHETIC_ULTRA_TOKEN into the real prefs key,
    // so the direct-pref-read ".AO-" checks (which read KeyManager.purchaseTokenKey() instead of calling
    // getPurchaseToken()) also pass. Idempotent; the SharedPreferences write happens at most once/process.
    private static boolean sTokenSeeded = false;

    private static void seedSyntheticToken() {
        if (sTokenSeeded) {
            return;
        }
        try {
            SharedPreferences sharedPreferences = PreferencesHelper.getDefaultPrefs("UltraHelper");
            if (!SYNTHETIC_ULTRA_TOKEN.equals(sharedPreferences.getString(KeyManager.purchaseTokenKey(), null))) {
                sharedPreferences.edit().putString(KeyManager.purchaseTokenKey(), SYNTHETIC_ULTRA_TOKEN).apply();
            }
            sTokenSeeded = true;
        } catch (Throwable th) {
            android.util.Log.i("UltraHelper", "Ultra token seed deferred: " + th);
        }
    }

    public static void clearPurchaseToken() {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("UltraHelper").edit();
        editorEdit.remove(KeyManager.purchaseTokenKey());
        editorEdit.apply();
        UltraReceiver.broadcast();
    }

    public static String getBackerSku() {
        return "ultra_backer";
    }

    public static String getLifetimeSku() {
        return "ultra_lifetime";
    }

    public static Intent getManageSubscriptionIntent() {
        String str = "https://play.google.com/store/account/subscriptions?sku=" + getSku() + "&package=com.resync.app";
        Logger.verbose("iap_helper", str);
        return new Intent("android.intent.action.VIEW", Uri.parse(str));
    }

    public static String getMonthlySku() {
        return "ultra_monthly";
    }

    public static String getPurchaseToken() {
        // Liberated: always return a non-null token containing ".AO-" so the compound
        // Ultra gates (isUltra() && contains(getPurchaseToken(), ".AO-")) pass. Also write it through to the
        // real prefs key so the direct-pref-read variants of the check pass too.
        seedSyntheticToken();
        return SYNTHETIC_ULTRA_TOKEN;
    }

    public static String getSku() {
        return PreferencesHelper.getDefaultPrefs("UltraHelper").getString(KeyManager.ultraSkuKey(), null);
    }

    public static String getSubscriptionDisplayName() {
        return "ultra_monthly".equalsIgnoreCase(getSku()) ? "Sync Ultra Monthly" : "ultra_yearly".equalsIgnoreCase(getSku()) ? "Sync Ultra Yearly" : "ultra_lifetime".equalsIgnoreCase(getSku()) ? "Sync Ultra lifetime" : "ultra_backer".equalsIgnoreCase(getSku()) ? "Sync Ultra backer edition" : "";
    }

    public static Intent getSubscriptionsIntent() {
        Logger.verbose("iap_helper", "https://play.google.com/store/account/subscriptions");
        return new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/account/subscriptions"));
    }

    public static boolean isUltra() {
        return isUltraOwned();
    }

    public static String getYearlySku() {
        return "ultra_yearly";
    }

    public static boolean isUltraOwned() {
        // Sync Ultra (premium) unlock. The original gates Ultra on a Play-billing purchase token
        // stored in prefs, but Sync's IAP backend is dead, so Ultra could never be (re)purchased.
        // Treat this personal/local build as Ultra-owned (same fix ReVanced's unlock patch applies).
        // Seed the synthetic ".AO-" token here too: isUltraOwned() runs constantly (via isUltra()) once the app is
        // up, so the direct-pref-read ".AO-" checks pass even if FirebaseRemoteConfig.d() ("c4266fa")
        // ever returns true (Google-hosted remote config may still be live).
        seedSyntheticToken();
        return true;
    }

    public static boolean isBacker() {
        return StringUtils.equals(getSku(), "ultra_backer");
    }

    public static boolean isLifetime() {
        return StringUtils.equals(getSku(), "ultra_lifetime");
    }

    public static void savePurchase(String str, String str2) {
        Logger.verbose("iap_helper", "Purchase token: " + str2);
        SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("UltraHelper").edit();
        editorEdit.putString(KeyManager.ultraSkuKey(), str);
        editorEdit.putString(KeyManager.purchaseTokenKey(), str2);
        editorEdit.apply();
        UltraGrabPaintsJob.schedule();
        UltraGrabTagsJob.schedule();
        UltraReceiver.broadcast();
    }
}
