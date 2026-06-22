package com.resync.app.premium.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.dialogs.FragmentUtils;

/* JADX INFO: renamed from: ga.d */
/* JADX INFO: compiled from: UltraConfirmCloudBackupAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraConfirmCloudBackupAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_backup_24;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Backup";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        FragmentUtils.show(UltraCloudBackupProgressDialogBottomSheet.class, getChildFragmentManager());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Cloud backup your settings?";
    }
}
