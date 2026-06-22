package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: t9.q1 */
/* JADX INFO: compiled from: RedditIsDownAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class RedditIsDownAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "View details";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        LinkHandler.openExternalOrInternal(getContext(), "https://reddit.statuspage.io");
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "The Reddit servers are having issues...";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "You might be able to still use Sync but things might be a little broken for now";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNegativeButtonText() {
        return "Close";
    }
}
