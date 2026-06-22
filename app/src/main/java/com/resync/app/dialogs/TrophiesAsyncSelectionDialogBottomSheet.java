package com.resync.app.dialogs;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Trophy;
import com.resync.app.net.oauth.OAuthTrophiesRequest;

/* JADX INFO: renamed from: t9.u2 */
/* JADX INFO: compiled from: TrophiesAsyncSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class TrophiesAsyncSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {

    /* JADX INFO: compiled from: TrophiesAsyncSelectionDialogBottomSheet.java */
    class TrophiesResponseListener implements Response.Listener<Trophy[]> {
        TrophiesResponseListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Trophy[] trophyArr) {
            for (Trophy trophy : trophyArr) {
                AbstractSelectionDialogBottomSheet.ImageSelectionItem imageItem = new AbstractSelectionDialogBottomSheet.ImageSelectionItem(trophy.name, trophy.iconUrl);
                imageItem.tag = trophy;
                TrophiesAsyncSelectionDialogBottomSheet.this.addItem(imageItem);
            }
            TrophiesAsyncSelectionDialogBottomSheet.this.showItems();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onTrophiesLoadError(VolleyError volleyError) {
        StaticToast.show("Error loading trophies");
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        OAuthRequestManager.sendWithAppContext(new OAuthTrophiesRequest(RedditApplication.getAppContext(), null, getUser(), new TrophiesResponseListener(), new Response.ErrorListener() { // from class: t9.t2
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                TrophiesAsyncSelectionDialogBottomSheet.this.onTrophiesLoadError(volleyError);
            }
        }));
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Trophies";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public boolean isAsync() {
        return true;
    }
}
