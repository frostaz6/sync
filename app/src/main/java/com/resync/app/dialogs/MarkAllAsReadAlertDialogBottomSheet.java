package com.resync.app.dialogs;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.events.RefreshEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthMarkAllMessagesViewedRequest;
import com.resync.app.helpers.messaging.MessageCountHelper;

/* JADX INFO: renamed from: t9.u0 */
/* JADX INFO: compiled from: MarkAllAsReadAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class MarkAllAsReadAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {

    /* JADX INFO: compiled from: MarkAllAsReadAlertDialogBottomSheet.java */
    class MarkAllReadSuccessListener implements Response.Listener<Void> {
        MarkAllReadSuccessListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Void response) {
            StaticToast.showWithContext(RedditApplication.getAppContext(), "All posts marked as read");
            MessageCountHelper.setCount(RedditApplication.getAppContext(), 0);
            BusSingleton.getBus().post(new RefreshEvent());
        }
    }

    /* JADX INFO: compiled from: MarkAllAsReadAlertDialogBottomSheet.java */
    class MarkAllReadErrorListener implements Response.ErrorListener {
        MarkAllReadErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (volleyError == null || volleyError.networkResponse == null) {
                StaticToast.showWithContext(RedditApplication.getAppContext(), "Error marking all as read");
                return;
            }
            StaticToast.showWithContext(RedditApplication.getAppContext(), "Error marking all as read: " + volleyError.networkResponse.statusCode);
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean shouldPadTopSheet() {
        return false;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Confirm";
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isTopSheet() {
        return this.topSheet;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        OAuthRequestManager.sendWithAppContext(new OAuthMarkAllMessagesViewedRequest(getActivity(), new MarkAllReadSuccessListener(), new MarkAllReadErrorListener()));
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Mark all as read";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "Mark all items in your inbox as read?";
    }
}
