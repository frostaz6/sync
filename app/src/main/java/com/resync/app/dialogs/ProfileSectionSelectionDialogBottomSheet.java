package com.resync.app.dialogs;

import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: renamed from: t9.l1 */
/* JADX INFO: compiled from: ProfileSectionSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ProfileSectionSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    private AbstractSelectionDialogBottomSheet.SelectionItem buildItem(String str) {
        return new AbstractSelectionDialogBottomSheet.SelectionItem(MaterialPostsHelper.getProfileSectionIconRes(str), str);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        addItem(buildItem("Overview"));
        addItem(buildItem("Comments"));
        addItem(buildItem("Submitted"));
        if (StringUtils.equalsIgnoreCase(getUser(), AccountSingleton.getInstance().getUsername())) {
            addItem(buildItem("Upvoted"));
            addItem(buildItem("Downvoted"));
            addItem(buildItem("Saved"));
            addItem(buildItem("Hidden"));
        }
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Profile";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        BusSingleton.getBus().post(new SubredditSelectedEvent(MaterialPostsHelper.buildProfileSectionAddress(getUser(), item.title), false));
        dismiss();
    }
}
