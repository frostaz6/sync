package com.resync.app.premium.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.dialogs.FragmentUtils;

/* JADX INFO: renamed from: ka.b */
/* JADX INFO: compiled from: UltraConfirmTagClearAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraConfirmTagClearAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_clear_24;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Clear";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        FragmentUtils.show(UltraTagClearBackupProgressDialogBottomSheet.class, getChildFragmentManager());
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Clear all user tags?";
    }
}
