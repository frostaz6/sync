package com.resync.app.fragments.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.Preference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.squareup.otto.Subscribe;
import com.resync.app.premium.dialogs.UltraConfirmCloudBackupAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.net.OAuthRateHelper;
import com.resync.app.media.BackupSafHelper;
import com.resync.app.premium.dialogs.UltraConfirmCloudDeleteAlertDialogBottomSheet;
import com.resync.app.premium.dialogs.UltraConfirmCloudRestoreAlertDialogBottomSheet;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.dialogs.BackupAlertDialogBottomSheet;
import com.resync.app.dialogs.BackupRestoredAlertDialogBottomSheet;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.dialogs.AbstractLocationSelectionBottomSheetFragment;
import com.resync.app.dialogs.BackupLocationSelectionBottomSheetFragment;
import com.resync.app.dialogs.MediaLocationSelectionBottomSheetFragment;
import com.resync.app.util.Logger;
import com.resync.app.helpers.messaging.MessageCountHelper;
import com.resync.app.events.UltraBackupStatusChanged;

/* JADX INFO: compiled from: PreferencesBackupFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesBackupFragment extends BasePreferencesFragment {

    /* JADX INFO: compiled from: PreferencesBackupFragment.java */
    class BackupDirClickListener implements Preference.OnPreferenceClickListener {
        BackupDirClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(BackupLocationSelectionBottomSheetFragment.class, PreferencesBackupFragment.this.getChildFragmentManager());
            return false;
        }
    }

    /* JADX INFO: compiled from: PreferencesBackupFragment.java */
    class BackupAllClickListener implements Preference.OnPreferenceClickListener {
        BackupAllClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (BackupSafHelper.isLocationValid()) {
                PreferencesBackupFragment.this.createBackup();
                return false;
            }
            BackupLocationSelectionBottomSheetFragment backupLocationSelectionBottomSheetFragment = new BackupLocationSelectionBottomSheetFragment();
            final PreferencesBackupFragment fragment = PreferencesBackupFragment.this;
            backupLocationSelectionBottomSheetFragment.setCompleteListener(new AbstractLocationSelectionBottomSheetFragment.OnCompleteListener() { // from class: pa.r
                @Override // com.resync.app.dialogs.AbstractLocationSelectionBottomSheetFragment.OnCompleteListener
                public final void onComplete() {
                    PreferencesBackupFragment.startCreateBackup(fragment);
                }
            });
            backupLocationSelectionBottomSheetFragment.show(PreferencesBackupFragment.this.getChildFragmentManager(), null);
            return false;
        }
    }

    /* JADX INFO: compiled from: PreferencesBackupFragment.java */
    class RestoreClickListener implements Preference.OnPreferenceClickListener {
        RestoreClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (BackupSafHelper.isLocationValid()) {
                PreferencesBackupFragment.this.launchRestorePicker();
                return false;
            }
            BackupLocationSelectionBottomSheetFragment backupLocationSelectionBottomSheetFragment = new BackupLocationSelectionBottomSheetFragment();
            final PreferencesBackupFragment fragment = PreferencesBackupFragment.this;
            backupLocationSelectionBottomSheetFragment.setCompleteListener(new AbstractLocationSelectionBottomSheetFragment.OnCompleteListener() { // from class: pa.s
                @Override // com.resync.app.dialogs.AbstractLocationSelectionBottomSheetFragment.OnCompleteListener
                public final void onComplete() {
                    fragment.launchRestorePicker();
                }
            });
            backupLocationSelectionBottomSheetFragment.show(PreferencesBackupFragment.this.getChildFragmentManager(), null);
            return false;
        }
    }

    /* JADX INFO: compiled from: PreferencesBackupFragment.java */
    class CloudBackupClickListener implements Preference.OnPreferenceClickListener {
        CloudBackupClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(UltraConfirmCloudBackupAlertDialogBottomSheet.class, PreferencesBackupFragment.this.getChildFragmentManager());
            return false;
        }
    }

    /* JADX INFO: compiled from: PreferencesBackupFragment.java */
    class CloudRestoreClickListener implements Preference.OnPreferenceClickListener {
        CloudRestoreClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(UltraConfirmCloudRestoreAlertDialogBottomSheet.class, PreferencesBackupFragment.this.getChildFragmentManager());
            return false;
        }
    }

    /* JADX INFO: compiled from: PreferencesBackupFragment.java */
    class CloudDeleteClickListener implements Preference.OnPreferenceClickListener {
        CloudDeleteClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(UltraConfirmCloudDeleteAlertDialogBottomSheet.class, PreferencesBackupFragment.this.getChildFragmentManager());
            return false;
        }
    }

    /* JADX INFO: compiled from: PreferencesBackupFragment.java */
    class RestoreThread extends Thread {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Uri uri;

        RestoreThread(Uri uri) {
            this.uri = uri;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("PreferencesBackupFragment").edit();
                JSONObject jSONObject = new JSONObject(IOUtils.toString(RedditApplication.getAppContext().getContentResolver().openInputStream(this.uri)));
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    Object obj = jSONObject.get(next);
                    Logger.verbose("Key: " + next);
                    if (next != null && obj != null && !PreferencesBackupFragment.isExcludedKey(next)) {
                        if (obj instanceof Integer) {
                            editorEdit.putInt(next, ((Integer) obj).intValue());
                        } else if (obj instanceof String) {
                            editorEdit.putString(next, (String) obj);
                        } else if (obj instanceof Boolean) {
                            editorEdit.putBoolean(next, ((Boolean) obj).booleanValue());
                        } else if (obj instanceof Float) {
                            editorEdit.putFloat(next, ((Float) obj).floatValue());
                        } else if (obj instanceof Long) {
                            editorEdit.putLong(next, ((Long) obj).longValue());
                        } else if (obj instanceof JSONArray) {
                            JSONArray jSONArray = (JSONArray) obj;
                            HashSet hashSet = new HashSet();
                            for (int index = 0; index < jSONArray.length(); index++) {
                                hashSet.add(jSONArray.getString(index));
                            }
                            editorEdit.putStringSet(next, hashSet);
                        } else {
                            Logger.verbose("Unsupported: " + next);
                            Logger.verbose("Unsupported type: " + obj.getClass());
                        }
                    }
                }
                editorEdit.apply();
                FragmentUtils.show(BackupRestoredAlertDialogBottomSheet.class, PreferencesBackupFragment.this.getChildFragmentManager());
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void startCreateBackup(PreferencesBackupFragment fragment) {
        fragment.createBackup();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createBackup() {
        try {
            JSONObject jSONObject = new JSONObject();
            Map<String, ?> all = PreferencesHelper.getAccountPrefs("PreferencesBackupFragment").getAll();
            for (String str : all.keySet()) {
                Object obj = all.get(str);
                if (!isExcludedKey(str)) {
                    if ((obj instanceof Float) || (obj instanceof String) || (obj instanceof Boolean) || (obj instanceof Long) || (obj instanceof Integer)) {
                        jSONObject.put(str, obj);
                    } else if (obj instanceof HashSet) {
                        JSONArray jSONArray = new JSONArray();
                        Iterator it = ((HashSet) obj).iterator();
                        while (it.hasNext()) {
                            jSONArray.put(it.next());
                        }
                        jSONObject.put(str, jSONArray);
                    } else {
                        Logger.verbose("Unsupported: " + obj.getClass());
                        Logger.verbose(str + StringUtils.SPACE + obj);
                    }
                }
            }
            Logger.verbose(jSONObject.toString());
            Calendar calendar = Calendar.getInstance();
            Uri uri = BackupSafHelper.createBackupFile("sync_backup_" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(calendar.getTime())).getUri();
            IOUtils.write(jSONObject.toString(4), RedditApplication.getAppContext().getContentResolver().openOutputStream(uri));
            FragmentUtils.showWithUrl(BackupAlertDialogBottomSheet.class, getChildFragmentManager(), uri.toString());
        } catch (Exception e) {
            Logger.logException(e);
            StaticToast.showWithContext(getActivity(), "Error backing up settings");
        }
    }

    public static boolean isExcludedKey(String str) {
        Logger.verbose("Key: " + str);
        return StringUtils.isEmpty(str) || StringUtils.startsWith(str, MediaLocationSelectionBottomSheetFragment.getSafPrefix()) || str.equals("icon") || str.equals("subredddit_color") || str.equals("subredddit_icon") || str.equals("backers") || str.equals("ultra_testers") || "freshness".equals(str) || "lat".equals(str) || "lon".equals(str) || "acc".equals(str) || str.equals(KeyManager.adsEnabledKey()) || str.equals("failedToValidate") || str.equals(KeyManager.purchaseTokenKey()) || str.equals("top_subreddits") || str.equals("top_subreddits_new") || "username".equals(str) || "refresh_token".equals(str) || OAuthRateHelper.KEY_LAST_UPDATED.equals(str) || OAuthRateHelper.KEY_REMAINING_CALLS.equals(str) || OAuthRateHelper.KEY_RESET_TIME.equals(str) || str.equals("App Restrictions") || str.equals("state") || str.equals("recently_viewed_final") || str.equalsIgnoreCase("previous_version") || str.equalsIgnoreCase(MessageCountHelper.COUNT_KEY) || str.equalsIgnoreCase("directory_media") || str.equalsIgnoreCase("directory_backup") || str.equalsIgnoreCase("_visited_") || str.equalsIgnoreCase("user_friends") || str.equalsIgnoreCase("user_subreddits") || str.equalsIgnoreCase("user_moderated") || str.equalsIgnoreCase("user_favorites") || str.equalsIgnoreCase("user_multireddits");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onCloudBackupResult(HttpsCallableResult httpsCallableResult) {
        try {
            Logger.verbose("iap_helper", "BACKUP RESULT: " + httpsCallableResult.getData());
            boolean zContains = httpsCallableResult.getData().toString().contains("true");
            findPreference("ultra_backup_restore").setVisible(zContains);
            findPreference("ultra_backup_delete").setVisible(zContains);
            findPreference("ultra_backup_delete").setSummary("Backup exists for " + AccountSingleton.getInstance().getUsername());
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onCloudBackupFailed(Exception exc) {
        Logger.verbose("iap_helper", "BACKUP FAILED: " + exc);
    }

    public static PreferencesBackupFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesBackupFragment fragment = new PreferencesBackupFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void restoreFromUri(Uri uri) {
        new RestoreThread(uri).start();
    }

    private void refreshCloudBackupVisibility() {
        // LIBERATED: this asked Sync's dead Ultra backend whether a cloud backup exists (Firebase
        // callable "backupExistsCallable") and showed the cloud restore/delete rows accordingly.
        // The server is gone, so a cloud backup can never exist — keep both rows hidden and skip
        // the network call. (The whole cloud-backup section is also hidden in onCreatePreferences.)
        android.util.Log.i("PreferencesBackupFragment", "Skipped cloud backup-exists check (dead Ultra server)");
        findPreference("ultra_backup_restore").setVisible(false);
        findPreference("ultra_backup_delete").setVisible(false);
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_backup);
        super.onCreatePreferences(bundle, str);
        findPreference("backup_dir").setOnPreferenceClickListener(new BackupDirClickListener());
        findPreference("backup_all").setOnPreferenceClickListener(new BackupAllClickListener());
        findPreference("backup_restore").setOnPreferenceClickListener(new RestoreClickListener());
        findPreference("ultra_backup_all").setSummary("Create a backup for " + AccountSingleton.getInstance().getUsername());
        findPreference("ultra_backup_all").setOnPreferenceClickListener(new CloudBackupClickListener());
        findPreference("ultra_backup_restore").setSummary("Restore a backup for " + AccountSingleton.getInstance().getUsername());
        findPreference("ultra_backup_restore").setOnPreferenceClickListener(new CloudRestoreClickListener());
        refreshCloudBackupVisibility();
        findPreference("ultra_backup_delete").setOnPreferenceClickListener(new CloudDeleteClickListener());
        // LIBERATED: the cloud-backup section (create/restore/delete cloud backup) talks only to
        // Sync's dead Ultra backend, so hide it outright. The local file backup/restore rows
        // above ("backup_all"/"backup_restore") are unaffected and remain visible.
        boolean z = false;
        findPreference("ultra_backup").setVisible(z);
        findPreference("ultra_backup_divider").setVisible(z);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 101 && resultCode == -1) {
            restoreFromUri(intent.getData());
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Subscribe
    public void onBackupStatusChangedEvent(UltraBackupStatusChanged ultraBackupStatusChanged) {
        refreshCloudBackupVisibility();
    }

    public void launchRestorePicker() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT", (Uri) null);
        intent.setType("text/plain");
        intent.putExtra("android.provider.extra.INITIAL_URI", BackupSafHelper.getRootUri());
        startActivityForResult(intent, 101);
    }
}
