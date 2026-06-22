package com.resync.app.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.dialogs.FragmentUtils;

/* JADX INFO: renamed from: t9.c3 */
/* JADX INFO: compiled from: WatchAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class WatchAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_notifications_none_24;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Watch";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        FragmentUtils.showWithSubreddit(WatchSubredditProgressDialogBottomSheet.class, getChildFragmentManager(), getSubreddit());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Watch " + getSubreddit() + " for new posts?";
    }
}
