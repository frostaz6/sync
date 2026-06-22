package com.resync.app.dialogs;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.oauth.OAuthMultiredditsRequest;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Multireddits;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.net.oauth.OAuthMultiSubAddRequest;

/* JADX INFO: renamed from: t9.c */
/* JADX INFO: compiled from: AddToMultiAsyncRadioSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class AddToMultiAsyncRadioSelectionDialogBottomSheet extends AbstractRadioSelectionDialogBottomSheet {

    /* JADX INFO: renamed from: t9.c$a */
    /* JADX INFO: compiled from: AddToMultiAsyncRadioSelectionDialogBottomSheet.java */
    class AddedToMultiListener implements Response.Listener<Void> {

        final /* synthetic */ CharSequence multiName;

        AddedToMultiListener(CharSequence charSequence) {
            this.multiName = charSequence;
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(Void response) {
            StaticToast.showWithContext(AddToMultiAsyncRadioSelectionDialogBottomSheet.this.getActivity(), "Added to " + ((Object) this.multiName));
        }
    }

    /* JADX INFO: renamed from: t9.c$b */
    /* JADX INFO: compiled from: AddToMultiAsyncRadioSelectionDialogBottomSheet.java */
    class AddToMultiErrorListener implements Response.ErrorListener {

        final /* synthetic */ CharSequence multiName;

        AddToMultiErrorListener(CharSequence charSequence) {
            this.multiName = charSequence;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            CrashlyticsHelper.logException(volleyError);
            StaticToast.showWithContext(AddToMultiAsyncRadioSelectionDialogBottomSheet.this.getActivity(), "Error adding to " + ((Object) this.multiName));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onMultiredditsLoaded(Multireddits multireddits) {
        String[] strArr;
        if (!isDialogShowing()) {
            return;
        }
        if (multireddits == null || (strArr = multireddits.names) == null || strArr.length == 0) {
            showCreateNewMultiDialog();
            dismiss();
            return;
        }
        int i = 0;
        while (true) {
            String[] strArr2 = multireddits.names;
            if (i >= strArr2.length) {
                showItems();
                return;
            } else {
                addItem(new AbstractRadioSelectionDialogBottomSheet.RadioItem(strArr2[i], false));
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onMultiredditsError(VolleyError volleyError) {
        if (isDialogShowing()) {
            StaticToast.showWithContext(getActivity(), "Error loading your multireddits!");
            dismiss();
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public void onNeutralAction() {
        showCreateNewMultiDialog();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public void setupItems() {
        OAuthRequestManager.sendWithAppContext(new OAuthMultiredditsRequest(RedditApplication.getAppContext(), new Response.Listener() { // from class: t9.b
            @Override // com.android.volley.Response.Listener
            public final void onResponse(Object obj) {
                AddToMultiAsyncRadioSelectionDialogBottomSheet.this.onMultiredditsLoaded((Multireddits) obj);
            }
        }, new Response.ErrorListener() { // from class: t9.a
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                AddToMultiAsyncRadioSelectionDialogBottomSheet.this.onMultiredditsError(volleyError);
            }
        }));
    }

    void showCreateNewMultiDialog() {
        FragmentUtils.showWithSubreddit(CreateNewMultiInputDialogBottomSheet.class, getChildFragmentManager(), getSubreddit());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Add to multireddit";
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        CharSequence charSequence;
        Iterator<AbstractRadioSelectionDialogBottomSheet.RadioItem> it = getItems().iterator();
        while (true) {
            if (!it.hasNext()) {
                charSequence = null;
                break;
            }
            AbstractRadioSelectionDialogBottomSheet.RadioItem next = it.next();
            if (next.checked) {
                charSequence = next.label;
                break;
            }
        }
        if (!StringUtils.isNotEmpty(charSequence)) {
            StaticToast.show("No multireddit selected");
            return;
        }
        StaticToast.showWithContext(getActivity(), "Adding to " + ((Object) charSequence));
        OAuthRequestManager.sendWithAppContext(new OAuthMultiSubAddRequest(getActivity(), charSequence.toString(), getSubreddit(), new AddedToMultiListener(charSequence), new AddToMultiErrorListener(charSequence)));
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractRadioSelectionDialogBottomSheet.RadioItem item) {
        Iterator<AbstractRadioSelectionDialogBottomSheet.RadioItem> it = getItems().iterator();
        while (it.hasNext()) {
            it.next().setChecked(false);
        }
        item.setChecked(true);
        notifyItemsChanged();
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Add to multireddit";
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onNegativeButtonClicked() {
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public String getNegativeButtonText() {
        return "Cancel";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public String getNeutralButtonText() {
        return "New";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public boolean isAsync() {
        return true;
    }
}
