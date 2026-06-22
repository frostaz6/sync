package com.resync.app.premium.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.dialogs.FragmentUtils;

/* JADX INFO: renamed from: ha.d */
/* JADX INFO: compiled from: UltraConfirmCloudDeleteAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraConfirmCloudDeleteAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_cloud_download_24;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Remove";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        FragmentUtils.show(UltraCloudDeleteBackupProgressDialogBottomSheet.class, getChildFragmentManager());
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Remove your cloud backup?";
    }
}
