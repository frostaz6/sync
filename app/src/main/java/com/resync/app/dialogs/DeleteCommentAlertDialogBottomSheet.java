package com.resync.app.dialogs;

import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthDeleteCommentRequest;

/* JADX INFO: renamed from: t9.e0 */
/* JADX INFO: compiled from: DeleteCommentAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class DeleteCommentAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Delete";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        OAuthRequestManager.sendWithAppContext(new OAuthDeleteCommentRequest(RedditApplication.getAppContext(), getPostId()));
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Delete comment?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
