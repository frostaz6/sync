package com.resync.app.helpers;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.resync.app.R;
import okhttp3.HttpUrl;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: t7.d0 */
/* JADX INFO: compiled from: RemoteConfigHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class RemoteConfigHelper {
    public static String getBackers() {
        return PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").getString("backers", HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
    }

    public static boolean isUltraBackup() {
        return PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").getBoolean("ultra_backup", false);
    }

    public static boolean isC4266fa() {
        return PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").getBoolean("c4266fa", false);
    }

    public static boolean isUltraRestore() {
        return PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").getBoolean("ultra_restore", false);
    }

    public static boolean isUltraTranslate() {
        return PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").getBoolean("ultra_translate", false);
    }

    public static String getUltraTesters() {
        return PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").getString("ultra_testers", HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onConfigFetched(FirebaseRemoteConfig firebaseRemoteConfig, Task task) {
        if (task.isSuccessful()) {
            boolean zBooleanValue = ((Boolean) task.getResult()).booleanValue();
            firebaseRemoteConfig.activate();
            Logger.verbose("RemoteConfigHelper", "Config params updated: " + zBooleanValue);
            setBackers(firebaseRemoteConfig.getString("patreon_backers"));
            Logger.verbose("RemoteConfigHelper", "Backers value updated: " + getBackers());
            setUltraTesters(firebaseRemoteConfig.getString("ultra_testers"));
            Logger.verbose("RemoteConfigHelper", "Ultra testers value updated: " + getUltraTesters());
            setUltraRestore(firebaseRemoteConfig.getBoolean("ultra_restore"));
            Logger.verbose("RemoteConfigHelper", "Restore value updated: " + isUltraRestore());
            setUltraTranslate(firebaseRemoteConfig.getBoolean("ultra_translate"));
            Logger.verbose("RemoteConfigHelper", "Translate value updated: " + isUltraTranslate());
            setC4266fa(firebaseRemoteConfig.getBoolean("c4266fa"));
            Logger.verbose("RemoteConfigHelper", "c4266fa value updated: " + isC4266fa());
            setUltraBackup(firebaseRemoteConfig.getBoolean("ultra_backup"));
            Logger.verbose("RemoteConfigHelper", "Backup value updated: " + isUltraBackup());
        }
    }

    public static void setBackers(String str) {
        PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").edit().putString("backers", str).apply();
    }

    public static void setUltraBackup(boolean z) {
        PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").edit().putBoolean("ultra_backup", z).apply();
    }

    public static void setC4266fa(boolean z) {
        PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").edit().putBoolean("c4266fa", z).apply();
    }

    public static void setUltraRestore(boolean z) {
        PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").edit().putBoolean("ultra_restore", z).apply();
    }

    public static void setUltraTranslate(boolean z) {
        PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").edit().putBoolean("ultra_translate", z).apply();
    }

    public static void setUltraTesters(String str) {
        PreferencesHelper.getDefaultPrefs("RemoteConfigHelper").edit().putString("ultra_testers", str).apply();
    }

    public static void fetch() {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600L).build());
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener() { // from class: t7.c0
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public final void onComplete(Task task) {
                RemoteConfigHelper.onConfigFetched(firebaseRemoteConfig, task);
            }
        });
    }
}
