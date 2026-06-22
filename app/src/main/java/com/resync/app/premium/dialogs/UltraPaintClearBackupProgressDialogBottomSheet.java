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

/* JADX INFO: renamed from: ja.h */
/* JADX INFO: compiled from: UltraPaintClearBackupProgressDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraPaintClearBackupProgressDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    private void onSuccess() {
        BusSingleton.getBus().post(new UltraBackupStatusChanged());
        dismissAllowingStateLoss();
        StaticToast.show("User paints cleared!");
    }

    private void onError() {
        BusSingleton.getBus().post(new UltraBackupStatusChanged());
        dismissAllowingStateLoss();
        StaticToast.show("Error clearing user paints");
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
        // LIBERATED: "Clear all" originally only cleared the server-side paint catalog via Sync's
        // dead Ultra backend (Firebase callable "clearPaints"), leaving local paints untouched.
        // The server is gone, so instead clear the LOCAL paint map directly (com.resync.app.premium.UserPaintManager.l) — this makes
        // the action actually do what the user expects, with no network call.
        android.util.Log.i("UltraPaintClear", "Clearing paints locally (dead Ultra server)");
        com.resync.app.premium.UserPaintManager.getInstance().clearPaints(AccountSingleton.getInstance().getUsername());
        onSuccess();
    }
}
