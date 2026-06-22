package com.resync.app.dialogs;

import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ActivityHelper;

/* JADX INFO: renamed from: t9.d0 */
/* JADX INFO: compiled from: DataSaverAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class DataSaverAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Disable";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        SettingsSingleton.getInstance().enableDataSaving();
        StaticToast.showWithContext(getActivity(), "Data saving disabled");
        ActivityHelper.restartToMain(getActivity());
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Data saving mode";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "Data saving mode will disable all image previews & thumbnails loading automatically and where possible switch to lower video quaility.";
    }
}
