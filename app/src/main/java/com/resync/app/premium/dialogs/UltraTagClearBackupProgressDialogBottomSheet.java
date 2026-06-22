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
import com.resync.app.premium.UltraHelper;
import com.resync.app.events.UltraBackupStatusChanged;

/* JADX INFO: renamed from: ka.h */
/* JADX INFO: compiled from: UltraTagClearBackupProgressDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraTagClearBackupProgressDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    private void onSuccess() {
        BusSingleton.getBus().post(new UltraBackupStatusChanged());
        dismissAllowingStateLoss();
        StaticToast.show("User tags cleared!");
    }

    private void onError() {
        BusSingleton.getBus().post(new UltraBackupStatusChanged());
        dismissAllowingStateLoss();
        StaticToast.show("Error clearing user tags");
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
        // LIBERATED: "Clear all" originally only cleared the server-side tag catalog via Sync's
        // dead Ultra backend (Firebase callable "clearTags"), leaving local tags untouched. The
        // server is gone, so instead clear the LOCAL tag map directly (NewUserTagManager.n) — this
        // makes the action actually do what the user expects, with no network call.
        android.util.Log.i("UltraTagClear", "Clearing tags locally (dead Ultra server)");
        com.resync.app.managers.NewUserTagManager.getInstance().clearTags(AccountSingleton.getInstance().getUsername());
        onSuccess();
    }
}
