package com.resync.app.dialogs;

import android.os.Bundle;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t9.y */
/* JADX INFO: compiled from: CopyPostDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class CopyPostDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    Post post;
    AbstractSelectionDialogBottomSheet.SelectionItem copyTitleOption;
    AbstractSelectionDialogBottomSheet.SelectionItem copyPermalinkOption;
    AbstractSelectionDialogBottomSheet.SelectionItem copyLinkOption;
    AbstractSelectionDialogBottomSheet.SelectionItem copySelftextOption;
    AbstractSelectionDialogBottomSheet.SelectionItem copyUsernameOption;

    public static Bundle buildArgs(Post post) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.copyTitleOption = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.baseline_title_24, "Copy title");
        this.copyPermalinkOption = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_mode_comment_24, "Copy permalink");
        this.copyLinkOption = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_link_black_24dp, "Copy link");
        this.copySelftextOption = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_post_24, "Copy selftext");
        this.copyUsernameOption = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_person_24, "Copy username");
        addItem(this.copyTitleOption);
        addItem(this.copyPermalinkOption);
        if (this.post.getSyncType() == 2) {
            addItem(this.copySelftextOption);
        } else {
            addItem(this.copyLinkOption);
        }
        addItem(this.copyUsernameOption);
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
        return "Copy";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.copyTitleOption)) {
            RedditHelper.copyToClipboardWithToast(StringUtils.trim(this.post.getTitle()), getActivity());
        } else if (item.equals(this.copyPermalinkOption)) {
            RedditHelper.copyToClipboardWithToast("https://www.reddit.com/r/" + this.post.getSubreddit() + "/comments/" + this.post.getId(), getActivity());
        } else if (item.equals(this.copySelftextOption)) {
            RedditHelper.copyToClipboardWithToast(RedditHelper.htmlToTrimmedText(this.post.getSelftextProcessed()).toString(), getActivity());
        } else if (item.equals(this.copyLinkOption)) {
            RedditHelper.copyToClipboardWithToast(this.post.getUrl(), getActivity());
        } else if (item.equals(this.copyUsernameOption)) {
            RedditHelper.copyToClipboardWithToast(this.post.getAuthor(), getActivity());
        }
        dismiss();
    }
}
