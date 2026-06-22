package com.resync.app.dialogs;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.installations.FirebaseInstallations;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ActivityHelper;

/* JADX INFO: renamed from: t9.p0 */
/* JADX INFO: compiled from: FirebaseAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class FirebaseAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    private void deleteIdAndRestart() {
        StaticToast.showWithContext(getContext(), "ID deleted");
        ActivityHelper.restartToMain(getActivity());
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onDeleteSuccess(Void response) {
        deleteIdAndRestart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onDeleteFailure(Exception exc) {
        deleteIdAndRestart();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Delete ID and restart";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        FirebaseInstallations.getInstance().delete().addOnSuccessListener(new OnSuccessListener() { // from class: t9.o0
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                FirebaseAlertDialogBottomSheet.this.onDeleteSuccess((Void) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: t9.n0
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                FirebaseAlertDialogBottomSheet.this.onDeleteFailure(exc);
            }
        });
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Delete Firebase installation ID?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Your current anonymous ID is: " + getUrl());
        return sb2.toString();
    }
}
