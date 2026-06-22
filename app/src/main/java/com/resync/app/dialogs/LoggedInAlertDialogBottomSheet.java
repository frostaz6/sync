package com.resync.app.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.singletons.SingletonManager;
import com.resync.app.helpers.ActivityHelper;

/* JADX INFO: renamed from: t9.t0 */
/* JADX INFO: compiled from: LoggedInAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class LoggedInAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet, com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onContentSetup() {
        super.onContentSetup();
        setCancelable(false);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_settings_24;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public boolean isDeviceAnimated() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Start setup";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        SingletonManager.reloadAll(true);
        ActivityHelper.restartToOnboarding(getActivity());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Account added";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "Each account you add to Sync has its own settings. Would you like to run setup for this account? (recommended)";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onNegativeAction() {
        SingletonManager.reloadAll(true);
        ActivityHelper.restartToMain(getActivity());
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNegativeButtonText() {
        return "Skip";
    }
}
