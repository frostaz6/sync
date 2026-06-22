package com.resync.app.dialogs;

import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.managers.NewUserTagManager;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.PaintAndTagsUpdatedEvent;

/* JADX INFO: renamed from: t9.q2 */
/* JADX INFO: compiled from: TagUserInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class TagUserInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        StaticToast.showWithContext(getActivity(), "Tag added: " + str);
        NewUserTagManager.getInstance().addTag(AccountSingleton.getInstance().getUsername(), getUser(), str);
        getActivity().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        BusSingleton.getBus().post(new PaintAndTagsUpdatedEvent());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Tag";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "User tag";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Tag " + getUser();
    }
}
