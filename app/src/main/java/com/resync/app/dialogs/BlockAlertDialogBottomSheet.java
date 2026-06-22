package com.resync.app.dialogs;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthBlockRequest;
import com.resync.app.net.oauth.OAuthUserIdRequest;

/* JADX INFO: renamed from: t9.m */
/* JADX INFO: compiled from: BlockAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class BlockAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {

    /* JADX INFO: compiled from: BlockAlertDialogBottomSheet.java */
    class UserIdResponseListener implements Response.Listener<String> {

        /* JADX INFO: compiled from: BlockAlertDialogBottomSheet.java */
        class BlockResponseListener implements Response.Listener<Void> {
            BlockResponseListener() {
            }

            @Override // com.android.volley.Response.Listener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onResponse(Void response) {
                StaticToast.show("User blocked!");
            }
        }

        /* JADX INFO: compiled from: BlockAlertDialogBottomSheet.java */
        class BlockErrorListener implements Response.ErrorListener {
            BlockErrorListener() {
            }

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse;
                if (volleyError == null || (networkResponse = volleyError.networkResponse) == null || networkResponse.statusCode != 403) {
                    StaticToast.show("Error blocking user");
                } else {
                    StaticToast.show("This call requires the \"account\" permission, please remove your account and add it again to enable this feature!");
                }
            }
        }

        UserIdResponseListener() {
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(String str) {
            Logger.verbose("Got user ID: " + str);
            OAuthRequestManager.sendWithAppContext(new OAuthBlockRequest(RedditApplication.getAppContext(), str, new BlockResponseListener(), new BlockErrorListener()));
        }
    }

    /* JADX INFO: compiled from: BlockAlertDialogBottomSheet.java */
    class UserIdErrorListener implements Response.ErrorListener {
        UserIdErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            Logger.verbose("Error getting user ID");
            StaticToast.showWithContext(RedditApplication.getAppContext(), "Error blocking user");
        }
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Block";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        OAuthRequestManager.sendWithAppContext(new OAuthUserIdRequest(RedditApplication.getAppContext(), getUser(), new UserIdResponseListener(), new UserIdErrorListener()));
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Block " + getUser() + "?";
    }
}
