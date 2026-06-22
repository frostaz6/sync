package com.resync.app.dialogs;

import android.text.TextUtils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.PostFlair;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.net.oauth.OAuthPostFlairsRequest;

/* JADX INFO: renamed from: t9.i1 */
/* JADX INFO: compiled from: PostFlairsAsyncSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class PostFlairsAsyncSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onFlairsLoaded(PostFlair[] postFlairArr) {
        if (isDialogShowing()) {
            for (PostFlair postFlair : postFlairArr) {
                AbstractSelectionDialogBottomSheet.SelectionItem item = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_label_24, postFlair.text);
                item.tag = postFlair;
                addItem(item);
            }
            showItems();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onFlairsError(VolleyError volleyError) {
        if (isDialogShowing()) {
            StaticToast.show("Error loading post flairs");
            dismiss();
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        OAuthRequestManager.sendWithAppContext(new OAuthPostFlairsRequest(getSubreddit(), new Response.Listener() { // from class: t9.h1
            @Override // com.android.volley.Response.Listener
            public final void onResponse(Object obj) {
                PostFlairsAsyncSelectionDialogBottomSheet.this.onFlairsLoaded((PostFlair[]) obj);
            }
        }, new Response.ErrorListener() { // from class: t9.g1
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                PostFlairsAsyncSelectionDialogBottomSheet.this.onFlairsError(volleyError);
            }
        }));
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return getSubreddit() + " post flair";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        LinkHelper.open(getContext(), "https://reddit.com/r/" + getSubreddit() + "/search?q=flair%3A\"" + TextUtils.htmlEncode(((PostFlair) item.tag).text) + "\"");
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public boolean isAsync() {
        return true;
    }
}
