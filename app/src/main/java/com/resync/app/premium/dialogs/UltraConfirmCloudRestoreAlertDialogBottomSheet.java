package com.resync.app.premium.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.dialogs.FragmentUtils;

/* JADX INFO: renamed from: ia.d */
/* JADX INFO: compiled from: UltraConfirmCloudRestoreAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraConfirmCloudRestoreAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_cloud_download_24;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Restore";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        FragmentUtils.show(UltraCloudRestoreBackupProgressDialogBottomSheet.class, getChildFragmentManager());
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Restore from your cloud backup?";
    }
}
