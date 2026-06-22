package com.resync.app.dialogs;

import androidx.fragment.app.FragmentManager;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;

/* JADX INFO: renamed from: x9.a */
/* JADX INFO: compiled from: ExampleAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExampleAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    public static void show(FragmentManager fragmentManager) {
        new ExampleAlertDialogBottomSheet().show(fragmentManager, ExampleAlertDialogBottomSheet.class.getSimpleName());
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Submit";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        StaticToast.showWithContext(getContext(), "Positive");
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Example alert dialog";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "Example text would go here describing the action. I think a normal message would probably be a little longer.";
    }
}
