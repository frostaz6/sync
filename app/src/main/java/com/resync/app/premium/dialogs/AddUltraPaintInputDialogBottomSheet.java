package com.resync.app.premium.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.premium.UserPaintManager;
import com.resync.app.managers.UserIconManager;

/* JADX INFO: renamed from: ja.a */
/* JADX INFO: compiled from: AddUltraPaintInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class AddUltraPaintInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        UserPaintManager.getInstance().showAddPaintDialog(getContext(), str);
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Next";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "Username";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Add user paint";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public String[] getAutocompleteOptions() {
        return UserIconManager.getInstance().getUsernames();
    }
}
