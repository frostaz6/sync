package com.resync.app.dialogs;

import androidx.fragment.app.FragmentActivity;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.RecentHelper;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: renamed from: t9.p1 */
/* JADX INFO: compiled from: RecentlyViewedSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class RecentlyViewedSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        for (RecentHelper.RecentEntry entry : RecentHelper.getRecent()) {
            addItem(new AbstractSelectionDialogBottomSheet.RecentSubredditItem(entry.subreddit, entry.title, entry.id));
        }
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Recently viewed";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item instanceof AbstractSelectionDialogBottomSheet.RecentSubredditItem) {
            FragmentActivity activity = getActivity();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("http://reddit.com/r/");
            AbstractSelectionDialogBottomSheet.RecentSubredditItem recentItem = (AbstractSelectionDialogBottomSheet.RecentSubredditItem) item;
            sb2.append(recentItem.getTitle2());
            sb2.append("/comments/");
            sb2.append(recentItem.getId());
            LinkHelper.open(activity, sb2.toString());
        }
        dismiss();
    }
}
