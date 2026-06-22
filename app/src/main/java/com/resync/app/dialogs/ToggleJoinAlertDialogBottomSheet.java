package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.SubredditsManager;

/* JADX INFO: renamed from: t9.s2 */
/* JADX INFO: compiled from: ToggleJoinAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ToggleJoinAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    private boolean isSubscribed() {
        return SubredditsManager.getInstance().isSubscribed(getSubreddit());
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return !isSubscribed() ? "Join" : "Remove";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        SubredditsManager.getInstance().toggleSubscription(getSubreddit());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(!isSubscribed() ? "Join" : "Remove");
        sb2.append(StringUtils.SPACE);
        sb2.append(getSubreddit());
        sb2.append("?");
        return sb2.toString();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
