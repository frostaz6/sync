package com.resync.app.dialogs;

import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: renamed from: t9.x0 */
/* JADX INFO: compiled from: ModSectionSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ModSectionSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    private AbstractSelectionDialogBottomSheet.SelectionItem buildItem(String str) {
        return new AbstractSelectionDialogBottomSheet.SelectionItem(MaterialPostsHelper.getModSectionIconRes(str), str);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        addItem(buildItem("Modqueue"));
        addItem(buildItem("Reports"));
        addItem(buildItem("Spam"));
        addItem(buildItem("Edited"));
        addItem(buildItem("Unmoderated"));
        addItem(buildItem("Comments"));
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Moderator";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        BusSingleton.getBus().post(new SubredditSelectedEvent(MaterialPostsHelper.buildModAboutAddress(item.title), false));
        dismiss();
    }
}
