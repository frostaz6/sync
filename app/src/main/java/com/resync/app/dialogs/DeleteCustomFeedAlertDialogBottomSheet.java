package com.resync.app.dialogs;

import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.managers.MultiredditManager;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthMultiDeleteRequest;

/* JADX INFO: renamed from: t9.f0 */
/* JADX INFO: compiled from: DeleteCustomFeedAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class DeleteCustomFeedAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Delete";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        MultiredditManager.getInstance().removeMultireddit(getSubreddit());
        OAuthRequestManager.sendWithAppContext(new OAuthMultiDeleteRequest(RedditApplication.getAppContext(), getSubreddit().replaceAll("multi_", ""), null, null));
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Delete custom feed \"" + MaterialPostsHelper.extractMultiredditName(getSubreddit()) + "\"?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
