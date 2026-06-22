package com.resync.app.dialogs;

import android.os.Bundle;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t9.x */
/* JADX INFO: compiled from: CopyCommentDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class CopyCommentDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    private Post comment;
    AbstractSelectionDialogBottomSheet.SelectionItem copyTextOption;
    AbstractSelectionDialogBottomSheet.SelectionItem copyPermalinkOption;
    AbstractSelectionDialogBottomSheet.SelectionItem copyUsernameOption;

    public static Bundle buildArgs(Post post) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("comment", post);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.copyTextOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_text_fields_24, "Copy text"));
        this.copyPermalinkOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_mode_comment_24, "Copy permalink"));
        this.copyUsernameOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_person_24, "Copy username"));
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.comment = (Post) getArguments().getSerializable("comment");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Copy";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.copyTextOption)) {
            RedditHelper.copyToClipboardWithToast(RedditHelper.htmlToTrimmedText(this.comment.getBodyProcessed()).toString(), getContext());
        } else if (item.equals(this.copyPermalinkOption)) {
            RedditHelper.copyUrlToClipboard(getContext(), "https://www.reddit.com" + this.comment.getPermalink());
        } else if (item.equals(this.copyUsernameOption)) {
            RedditHelper.copyToClipboardWithToast(this.comment.getAuthor(), getActivity());
        }
        dismiss();
    }
}
