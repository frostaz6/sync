package com.resync.app.dialogs;

import android.content.ContentValues;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthModDistinguishRequest;

/* JADX INFO: renamed from: t9.w2 */
/* JADX INFO: compiled from: UndistinguishCommentAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UndistinguishCommentAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Yes";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        OAuthRequestManager.sendWithAppContext(new OAuthModDistinguishRequest(RedditApplication.getAppContext(), "t1", getPostId(), false, false));
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", getPostId());
        contentValues.put("stickied", (Integer) 0);
        getActivity().getContentResolver().update(RedditProvider.URI_UPDATE_UNDISTINGUISHED, contentValues, null, null);
        getActivity().getContentResolver().update(RedditProvider.URI_UPDATE_STICKY, contentValues, null, null);
        getActivity().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Undistinguish comment?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
