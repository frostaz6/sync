package com.resync.app.premium.dialogs;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.fragments.preferences.PreferencesBackupFragment;
import com.resync.app.events.UltraBackupStatusChanged;

/* JADX INFO: renamed from: ga.c */
/* JADX INFO: compiled from: UltraCloudBackupProgressDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraCloudBackupProgressDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    private void onSuccess() {
        BusSingleton.getBus().post(new UltraBackupStatusChanged());
        dismissAllowingStateLoss();
        StaticToast.show("Backup complete");
    }

    private void onError() {
        dismissAllowingStateLoss();
        StaticToast.show("Error making backup");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onCallableSuccess(HttpsCallableResult httpsCallableResult) {
        if (httpsCallableResult.getData().toString().contains("200")) {
            onSuccess();
        } else {
            onError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onCallableFailure(Exception exc) {
        onError();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public void onProgressReady() {
        // LIBERATED: cloud backup uploaded an AES-encrypted settings blob to Sync's dead Ultra
        // backend (Firebase callable "addBackupCallable"). The server is gone, so this can only
        // fail. No-op: inform the user and dismiss instead of hanging on the dead host. The
        // local file backup (PreferencesBackupFragment "Backup all") is unaffected.
        android.util.Log.i("UltraCloudBackup", "Cloud backup unavailable (dead Ultra server)");
        StaticToast.show("Cloud backup is no longer available");
        dismissAllowingStateLoss();
    }
}
