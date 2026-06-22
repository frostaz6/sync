package com.resync.app.dialogs;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.util.ArrayUtil;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.net.oauth.OAuthViewModsRequest;

/* JADX INFO: renamed from: t9.b3 */
/* JADX INFO: compiled from: ViewModsAsyncSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ViewModsAsyncSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onModsLoaded(String[] strArr) {
        if (isDialogShowing()) {
            if (ArrayUtil.isEmpty(strArr)) {
                StaticToast.showWithContext(getActivity(), "This sub has no mods!");
                dismiss();
                return;
            }
            for (String str : strArr) {
                addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_person_24, str));
            }
            showItems();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onModsError(VolleyError volleyError) {
        if (isDialogShowing()) {
            StaticToast.showWithContext(getActivity(), "Error loading sub mods!");
            dismiss();
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        OAuthRequestManager.send(getContext(), new OAuthViewModsRequest(getContext(), getSubreddit(), new Response.Listener() { // from class: t9.a3
            @Override // com.android.volley.Response.Listener
            public final void onResponse(Object obj) {
                ViewModsAsyncSelectionDialogBottomSheet.this.onModsLoaded((String[]) obj);
            }
        }, new Response.ErrorListener() { // from class: t9.z2
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                ViewModsAsyncSelectionDialogBottomSheet.this.onModsError(volleyError);
            }
        }));
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return getSubreddit() + " mods";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        LinkHelper.open(getActivity(), "/u/" + item.title);
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public boolean isAsync() {
        return true;
    }
}
