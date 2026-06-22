package com.resync.app.dialogs;

import androidx.fragment.app.FragmentManager;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;

/* JADX INFO: renamed from: x9.f */
/* JADX INFO: compiled from: ExampleInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExampleInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    public static void show(FragmentManager fragmentManager) {
        new ExampleInputDialogBottomSheet().show(fragmentManager, ExampleInputDialogBottomSheet.class.getSimpleName());
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        StaticToast.showWithContext(getContext(), "Positive: " + str);
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Example";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "Example hint";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Example input dialog";
    }
}
