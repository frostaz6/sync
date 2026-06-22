package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthDeletePostRequest;

/* JADX INFO: renamed from: t9.g0 */
/* JADX INFO: compiled from: DeletePostAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class DeletePostAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Delete";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        OAuthRequestManager.sendWithAppContext(new OAuthDeletePostRequest(getPostId()));
        StaticToast.showWithContext("Post deleted: " + getPostId(), getActivity());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Delete post?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
