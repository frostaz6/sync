package com.resync.app.premium.dialogs;

import android.content.SharedPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.dialogs.BackupRestoredAlertDialogBottomSheet;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.fragments.preferences.PreferencesBackupFragment;

/* JADX INFO: renamed from: ia.c */
/* JADX INFO: compiled from: UltraCloudRestoreBackupProgressDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraCloudRestoreBackupProgressDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    private void applyBackup(String str) {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("PreferencesBackupFragment").edit();
        try {
            int hash = SettingsSingleton.getInstance().buildFreshSettings().getHash(false);
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                Object obj = jSONObject.get(next);
                Logger.verbose("iap_helper", "Key: " + next);
                if (!PreferencesBackupFragment.isExcludedKey(next)) {
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
                        for (int i = 0; i < jSONArray.length(); i++) {
                            hashSet.add(jSONArray.getString(i));
                        }
                        editorEdit.putStringSet(next, hashSet);
                    } else {
                        Logger.verbose("iap_helper", "Unsupported: " + next);
                        Logger.verbose("iap_helper", "Unsupported type: " + obj.getClass());
                    }
                }
            }
            editorEdit.apply();
            if (SettingsSingleton.getInstance().hasSettingsChanged(hash, false)) {
                FragmentUtils.show(BackupRestoredAlertDialogBottomSheet.class, getChildFragmentManager());
            } else {
                StaticToast.show("No changes were made");
                onFinished();
            }
        } catch (Exception unused) {
            onError();
        }
    }

    private void onFinished() {
        dismissAllowingStateLoss();
    }

    private void onError() {
        dismissAllowingStateLoss();
        StaticToast.show("Error restoring backup");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onCallableSuccess(HttpsCallableResult httpsCallableResult) {
        try {
            String str = (String) ((HashMap) httpsCallableResult.getData()).get("backup");
            String decryptedBackup = KeyManager.decrypt(str);
            Logger.verbose("iap_helper", "RAW: " + str);
            Logger.verbose("iap_helper", "RAW: " + decryptedBackup);
            if (StringUtils.isEmpty(decryptedBackup)) {
                onError();
            } else {
                applyBackup(decryptedBackup);
            }
        } catch (Exception unused) {
            onError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onCallableFailure(Exception exc) {
        onError();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public void onProgressReady() {
        // LIBERATED: cloud restore downloaded an AES-encrypted settings blob from Sync's dead
        // Ultra backend (Firebase callable "loadBackupCallable"). The server is gone, so this can
        // only fail. No-op: inform the user and dismiss instead of hanging on the dead host. The
        // local file restore (PreferencesBackupFragment "Restore") is unaffected.
        android.util.Log.i("UltraCloudRestore", "Cloud restore unavailable (dead Ultra server)");
        StaticToast.show("Cloud restore is no longer available");
        dismissAllowingStateLoss();
    }
}
