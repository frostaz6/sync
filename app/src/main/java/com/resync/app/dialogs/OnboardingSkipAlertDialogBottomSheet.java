package com.resync.app.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.ui.activities.OnboardingActivity;

/* JADX INFO: renamed from: ba.a */
/* JADX INFO: compiled from: OnboardingSkipAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class OnboardingSkipAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_error_outline_24;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Skip";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        if (getActivity() instanceof OnboardingActivity) {
            ((OnboardingActivity) getActivity()).finishAfterDelay(150);
        }
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Skip setup?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "Skipping the setup might result in things not quite looking as you'd expect. It's strongly recommended that you continue with the setup.";
    }
}
