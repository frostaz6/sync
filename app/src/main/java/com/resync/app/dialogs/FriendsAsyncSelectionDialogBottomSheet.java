package com.resync.app.dialogs;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.Iterator;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthFriendsRequest;

/* JADX INFO: renamed from: t9.q0 */
/* JADX INFO: compiled from: FriendsAsyncSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class FriendsAsyncSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {

    /* JADX INFO: compiled from: FriendsAsyncSelectionDialogBottomSheet.java */
    class FriendsResponseListener implements Response.Listener<String[]> {
        FriendsResponseListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String[] strArr) {
            if (FriendsAsyncSelectionDialogBottomSheet.this.isDialogShowing()) {
                SubredditArrayList subredditArrayList = new SubredditArrayList(strArr);
                subredditArrayList.sortDefault();
                Iterator<String> it = subredditArrayList.iterator();
                while (it.hasNext()) {
                    FriendsAsyncSelectionDialogBottomSheet.this.addItem(new AbstractSelectionDialogBottomSheet.UsernameItem(it.next()));
                }
                FriendsAsyncSelectionDialogBottomSheet.this.showItems();
            }
        }
    }

    /* JADX INFO: compiled from: FriendsAsyncSelectionDialogBottomSheet.java */
    class FriendsErrorListener implements Response.ErrorListener {
        FriendsErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (FriendsAsyncSelectionDialogBottomSheet.this.isDialogShowing()) {
                StaticToast.showWithContext(FriendsAsyncSelectionDialogBottomSheet.this.getContext(), "Error loading multireddits");
                FriendsAsyncSelectionDialogBottomSheet.this.dismiss();
            }
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        OAuthRequestManager.sendWithAppContext(new OAuthFriendsRequest(RedditApplication.getAppContext(), new FriendsResponseListener(), new FriendsErrorListener()));
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Friends";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        com.resync.app.util.links.LinkHandler.openProfile(getActivity(), item.getTitle());
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public boolean isAsync() {
        return true;
    }
}
