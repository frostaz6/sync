package com.resync.app.dialogs;

import android.os.Bundle;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.ui.fragments.sidebar.MultiAboutFragment;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthMultiSubDeleteRequest;

/* JADX INFO: compiled from: RemoveMultiAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class RemoveMultiAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    String multi;
    AbstractSelectionDialogBottomSheet.SelectionItem item;

    public static Bundle buildArguments(String str, AbstractSelectionDialogBottomSheet.RemovableSubredditItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("multi", str);
        bundle.putSerializable("item", item);
        return bundle;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Remove";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        SubredditsManager.getInstance().removeSubreddit(getSubreddit());
        if (getParentFragment() instanceof MultiredditAsyncSelectionDialogBottomSheet) {
            ((MultiredditAsyncSelectionDialogBottomSheet) getParentFragment()).removeItem(this.item);
            OAuthRequestManager.sendWithAppContext(new OAuthMultiSubDeleteRequest(RedditApplication.getAppContext(), this.multi, this.item.getTitle(), new Response.Listener() { // from class: t9.v1
                @Override // com.android.volley.Response.Listener
                public final void onResponse(Object obj) {
                    StaticToast.show("Removed");
                }
            }, new Response.ErrorListener() { // from class: t9.t1
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    StaticToast.show("Error removing");
                }
            }));
        } else if (getParentFragment() instanceof MultiAboutFragment) {
            ((MultiAboutFragment) getParentFragment()).removeItem((AbstractSelectionDialogBottomSheet.RemovableSubredditItem) this.item);
            OAuthRequestManager.sendWithAppContext(new OAuthMultiSubDeleteRequest(RedditApplication.getAppContext(), this.multi, this.item.getTitle(), new Response.Listener() { // from class: t9.u1
                @Override // com.android.volley.Response.Listener
                public final void onResponse(Object obj) {
                    StaticToast.show("Removed");
                }
            }, new Response.ErrorListener() { // from class: t9.s1
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    StaticToast.show("Error removing");
                }
            }));
        }
        dismiss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.multi = getArguments().getString("multi");
        this.item = (AbstractSelectionDialogBottomSheet.RemovableSubredditItem) getArguments().getSerializable("item");
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Remove " + this.item.getTitle() + " from " + this.multi + "?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
