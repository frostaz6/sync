package com.resync.app.dialogs;

import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.managers.ModeratedSubredditManager;
import com.resync.app.events.MessagingSectionSelectedEvent;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: renamed from: t9.w0 */
/* JADX INFO: compiled from: MessagingSectionSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class MessagingSectionSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {

    /* JADX INFO: compiled from: MessagingSectionSelectionDialogBottomSheet.java */
    class SectionItem extends AbstractSelectionDialogBottomSheet.SelectionItem {

        int sectionId;

        public SectionItem(int icon, String str, int sectionId) {
            super(icon, str);
            this.sectionId = sectionId;
        }
    }

    private SectionItem createSectionItem(int section) {
        return new SectionItem(MaterialPostsHelper.getMessagingSectionIconRes(section), MaterialPostsHelper.getMessagingSectionTitle(section), section);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        int maxSection = ModeratedSubredditManager.getInstance().hasModerated() ? 8 : 6;
        for (int i = 0; i <= maxSection; i++) {
            addItem(createSectionItem(i));
        }
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Messages";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        BusSingleton.getBus().post(new MessagingSectionSelectedEvent(((SectionItem) item).sectionId));
        dismiss();
    }
}
