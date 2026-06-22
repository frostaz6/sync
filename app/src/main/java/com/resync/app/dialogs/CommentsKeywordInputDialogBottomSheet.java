package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.events.CommentNavigationSearchKeywordUpdatedEvent;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: renamed from: t9.w */
/* JADX INFO: compiled from: CommentsKeywordInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentsKeywordInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        BusSingleton.getBus().post(new CommentNavigationSearchKeywordUpdatedEvent(str));
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Search";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "Example";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Search keywords";
    }
}
