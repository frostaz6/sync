package com.resync.app.dialogs;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.MultiredditManager;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthMultiNewRequest;

/* JADX INFO: renamed from: t9.b0 */
/* JADX INFO: compiled from: CreateNewMultiInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class CreateNewMultiInputDialogBottomSheet extends AbstractInputDialogBottomSheet {

    /* JADX INFO: compiled from: CreateNewMultiInputDialogBottomSheet.java */
    class CreateMultiErrorListener implements Response.ErrorListener {
        CreateMultiErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            StaticToast.showWithContext(CreateNewMultiInputDialogBottomSheet.this.getContext(), "Failed to create multireddit");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onMultiCreated(String str, Void response) {
        MultiredditManager.getInstance().addMultireddit("multi_" + str);
        if (StringUtils.isEmpty(getSubreddit())) {
            StaticToast.showWithContext(getContext(), "Multireddit created");
        } else {
            StaticToast.showWithContext(getContext(), "Multireddit with subreddit created");
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(final String str) {
        if (StringUtils.length(str) < 2) {
            this.mTextInputEditText.setError("The name must be longer");
        } else {
            OAuthRequestManager.send(getContext(), new OAuthMultiNewRequest(getContext(), str, getSubreddit(), new Response.Listener() { // from class: t9.a0
                @Override // com.android.volley.Response.Listener
                public final void onResponse(Object obj) {
                    CreateNewMultiInputDialogBottomSheet.this.onMultiCreated(str, (Void) obj);
                }
            }, new CreateMultiErrorListener()));
            dismiss();
        }
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Create";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "name";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "New multireddit";
    }
}
