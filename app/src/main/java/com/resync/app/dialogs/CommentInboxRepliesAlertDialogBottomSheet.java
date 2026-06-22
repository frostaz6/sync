package com.resync.app.dialogs;

import android.content.ContentValues;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.provider.RedditProvider;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthRepliesRequest;

/* JADX INFO: renamed from: t9.r */
/* JADX INFO: compiled from: CommentInboxRepliesAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentInboxRepliesAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {

    /* JADX INFO: compiled from: CommentInboxRepliesAlertDialogBottomSheet.java */
    class EnableRepliesSuccessListener implements Response.Listener<Void> {
        EnableRepliesSuccessListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Void response) {
            StaticToast.show("Inbox replies enabled");
        }
    }

    /* JADX INFO: compiled from: CommentInboxRepliesAlertDialogBottomSheet.java */
    class EnableRepliesErrorListener implements Response.ErrorListener {
        EnableRepliesErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            StaticToast.show("Error setting inbox replies");
        }
    }

    /* JADX INFO: compiled from: CommentInboxRepliesAlertDialogBottomSheet.java */
    class DisableRepliesSuccessListener implements Response.Listener<Void> {
        DisableRepliesSuccessListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Void response) {
            StaticToast.show("Inbox replies disabled");
        }
    }

    /* JADX INFO: compiled from: CommentInboxRepliesAlertDialogBottomSheet.java */
    class DisableRepliesErrorListener implements Response.ErrorListener {
        DisableRepliesErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            StaticToast.show("Error setting inbox replies");
        }
    }

    private void disableReplies() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("send_replies", (Integer) 0);
        RedditApplication.getAppContext().getContentResolver().update(RedditProvider.URI_RESTORE_REMOVED, contentValues, getPost().getId(), null);
        RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        OAuthRequestManager.sendWithAppContext(new OAuthRepliesRequest(RedditApplication.getAppContext(), 1, getPost().getId(), false, new DisableRepliesSuccessListener(), new DisableRepliesErrorListener()));
    }

    private void enableReplies() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("send_replies", (Integer) 1);
        RedditApplication.getAppContext().getContentResolver().update(RedditProvider.URI_RESTORE_REMOVED, contentValues, getPost().getId(), null);
        RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        OAuthRequestManager.sendWithAppContext(new OAuthRepliesRequest(RedditApplication.getAppContext(), 1, getPost().getId(), true, new EnableRepliesSuccessListener(), new EnableRepliesErrorListener()));
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.markunread_mailbox_outline;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return getPost().isSendReplies() ? "Disable" : "Enable";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        if (getPost().isSendReplies()) {
            disableReplies();
        } else {
            enableReplies();
        }
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Inbox replies";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Replies are currently ");
        sb2.append(getPost().isSendReplies() ? "enabled. This means when users reply to your comment Reddit will send you a notification." : "disabled. This means when users reply to your comment Reddit will not send you a notification.");
        return sb2.toString();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onNegativeAction() {
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNegativeButtonText() {
        return "Dismiss";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNeutralButtonText() {
        return getPost().isSendReplies() ? "Enable" : "Disable";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onNeutralAction() {
        if (getPost().isSendReplies()) {
            enableReplies();
        } else {
            disableReplies();
        }
        dismiss();
    }
}
