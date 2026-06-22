package com.resync.app.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import java.util.Date;
import com.resync.app.net.OAuthRateHelper;

/* JADX INFO: renamed from: t9.n1 */
/* JADX INFO: compiled from: RateLimitedDeviceAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class RateLimitedDeviceAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_error_outline_24;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Dismiss";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "You are being rate limited";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "Reddit is reporting that you are being rate limited.\n\nYou have " + OAuthRateHelper.getRemainingCalls() + " remaining calls\nYour rates reset in: " + new Date(OAuthRateHelper.getResetTime()).toGMTString();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNegativeButtonText() {
        return null;
    }
}
