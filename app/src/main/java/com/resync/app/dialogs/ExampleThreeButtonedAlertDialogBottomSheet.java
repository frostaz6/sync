package com.resync.app.dialogs;

import androidx.fragment.app.FragmentManager;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;

/* JADX INFO: renamed from: x9.j */
/* JADX INFO: compiled from: ExampleThreeButtonedAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExampleThreeButtonedAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    public static void show(FragmentManager fragmentManager) {
        new ExampleThreeButtonedAlertDialogBottomSheet().show(fragmentManager, ExampleThreeButtonedAlertDialogBottomSheet.class.getSimpleName());
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
        return "Example text would go here describing the action";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNeutralButtonText() {
        return "Neutral";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onNeutralAction() {
        StaticToast.showWithContext(getContext(), "Neutral");
        dismiss();
    }
}
