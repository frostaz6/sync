package com.resync.app.dialogs;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.ui.fragments.sidebar.MultiAboutFragment;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthMultiSubAddRequest;

/* JADX INFO: renamed from: t9.d1 */
/* JADX INFO: compiled from: NewMultiInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class NewMultiInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        if (getParentFragment() instanceof MultiredditAsyncSelectionDialogBottomSheet) {
            OAuthRequestManager.sendWithAppContext(new OAuthMultiSubAddRequest(RedditApplication.getAppContext(), getSubreddit(), str, new Response.Listener() { // from class: t9.b1
                @Override // com.android.volley.Response.Listener
                public final void onResponse(Object obj) {
                    StaticToast.show("Added");
                }
            }, new Response.ErrorListener() { // from class: t9.a1
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    StaticToast.show("Error adding");
                }
            }));
            ((MultiredditAsyncSelectionDialogBottomSheet) getParentFragment()).addItem(new AbstractSelectionDialogBottomSheet.RemovableSubredditItem(str));
        } else if (getParentFragment() instanceof MultiAboutFragment) {
            OAuthRequestManager.sendWithAppContext(new OAuthMultiSubAddRequest(RedditApplication.getAppContext(), getSubreddit(), str, new Response.Listener() { // from class: t9.c1
                @Override // com.android.volley.Response.Listener
                public final void onResponse(Object obj) {
                    StaticToast.show("Added");
                }
            }, new Response.ErrorListener() { // from class: t9.z0
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    StaticToast.show("Error adding");
                }
            }));
            ((MultiAboutFragment) getParentFragment()).addItem(new AbstractSelectionDialogBottomSheet.RemovableSubredditItem(str));
        }
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Add";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "subreddit";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Add subreddit to " + getSubreddit();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public String[] getAutocompleteOptions() {
        return SubredditsManager.getInstance().getSubreddits().toStringArray();
    }
}
