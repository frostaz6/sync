package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.FilterHelper;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t9.m0 */
/* JADX INFO: compiled from: FilterSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class FilterSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    Post post;
    AbstractSelectionDialogBottomSheet.SelectionItem filterSubredditItem;
    AbstractSelectionDialogBottomSheet.SelectionItem filterUserItem;
    AbstractSelectionDialogBottomSheet.SelectionItem filterDomainItem;

    public static Bundle buildArguments(Post post) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.filterSubredditItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_subreddits_circle_outline, "Filter /r/" + this.post.getSubreddit()));
        this.filterUserItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_account_circle_24, "Filter /u/" + this.post.getAuthor()));
        if (this.post.getSubreddit().equals(this.post.getDomain())) {
            return;
        }
        this.filterDomainItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_explore_24, "Filter " + this.post.getDomain()));
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.post = (Post) getArguments().getSerializable("Post");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Filters";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.filterSubredditItem)) {
            FilterHelper.getInstance().add(2, this.post.getSubreddit());
            StaticToast.showWithContext(getActivity(), "Filtered subreddit, refresh to apply.");
        } else if (item.equals(this.filterUserItem)) {
            FilterHelper.getInstance().add(3, this.post.getAuthor());
            StaticToast.showWithContext(getActivity(), "Filtered user, refresh to apply.");
        } else if (item.equals(this.filterDomainItem)) {
            FilterHelper.getInstance().add(1, this.post.getDomain());
            StaticToast.showWithContext(getActivity(), "Filtered domain, refresh to apply.");
        }
        dismiss();
    }
}
