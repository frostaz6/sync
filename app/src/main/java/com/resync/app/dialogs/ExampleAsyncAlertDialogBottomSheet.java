package com.resync.app.dialogs;

import androidx.fragment.app.FragmentManager;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;

/* JADX INFO: renamed from: x9.c */
/* JADX INFO: compiled from: ExampleAsyncAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExampleAsyncAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void showTestResult() {
        if (isDialogShowing()) {
            showResult("Testing");
        }
    }

    public static void show(FragmentManager fragmentManager) {
        new ExampleAsyncAlertDialogBottomSheet().show(fragmentManager, ExampleAsyncAlertDialogBottomSheet.class.getSimpleName());
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onContentSetup() {
        getView().postDelayed(new Runnable() { // from class: x9.b
            @Override // java.lang.Runnable
            public final void run() {
                ExampleAsyncAlertDialogBottomSheet.this.showTestResult();
            }
        }, 1000L);
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Submit";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        StaticToast.showWithContext(getContext(), "TODO");
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Example alert dialog";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public boolean isAsync() {
        return true;
    }
}
