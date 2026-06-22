package com.resync.app.premium.dialogs;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import java.util.HashMap;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.events.UltraBackupStatusChanged;

/* JADX INFO: renamed from: ha.c */
/* JADX INFO: compiled from: UltraCloudDeleteBackupProgressDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraCloudDeleteBackupProgressDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    private void onSuccess() {
        BusSingleton.getBus().post(new UltraBackupStatusChanged());
        dismissAllowingStateLoss();
        StaticToast.show("Backup deleted");
    }

    private void onError() {
        BusSingleton.getBus().post(new UltraBackupStatusChanged());
        dismissAllowingStateLoss();
        StaticToast.show("Error removing backup");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onCallableSuccess(HttpsCallableResult httpsCallableResult) {
        if (httpsCallableResult.getData().toString().contains("true")) {
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
        // LIBERATED: cloud delete asked Sync's dead Ultra backend to drop the stored cloud backup
        // (Firebase callable "deleteBackupCallable"). The server is gone, so this can only fail.
        // No-op: inform the user and dismiss instead of hanging on the dead host.
        android.util.Log.i("UltraCloudDelete", "Cloud backup delete unavailable (dead Ultra server)");
        StaticToast.show("Cloud backup is no longer available");
        dismissAllowingStateLoss();
    }
}
