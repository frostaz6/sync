package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: renamed from: t9.v2 */
/* JADX INFO: compiled from: UnderscoreDetectedAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UnderscoreDetectedAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Fix";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        LinkHelper.open(getContext(), getUrl().replaceAll("%5C_", "_"));
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Escaped underscore detected";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "It looks like someone has shared a link that might not work as expected. Would you like Sync to try and fix this for you?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onNegativeAction() {
        LinkHelper.open(getContext(), getUrl());
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNegativeButtonText() {
        return "Ignore";
    }
}
