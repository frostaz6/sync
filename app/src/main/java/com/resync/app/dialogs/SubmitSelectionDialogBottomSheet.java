package com.resync.app.dialogs;

import android.content.Context;
import com.resync.app.R;
import com.resync.app.ui.activities.SubmitActivity;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;

/* JADX INFO: renamed from: t9.n2 */
/* JADX INFO: compiled from: SubmitSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class SubmitSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    AbstractSelectionDialogBottomSheet.SelectionItem textOption;
    AbstractSelectionDialogBottomSheet.SelectionItem linkOption;
    AbstractSelectionDialogBottomSheet.SelectionItem photoOption;

    public static void show(Context context) {
        String subreddit = ContextHelper.getCurrentSubredditName(context);
        if (StringUtils.isNotEmpty(subreddit)) {
            FragmentUtils.showWithSubreddit(SubmitSelectionDialogBottomSheet.class, ContextHelper.getFragmentManager(context), subreddit);
        } else {
            FragmentUtils.show(SubmitSelectionDialogBottomSheet.class, ContextHelper.getFragmentManager(context));
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.textOption = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_post_add_24, "Text");
        this.linkOption = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_link_24, "Link");
        this.photoOption = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_add_photo_alternate_24, "Photo");
        addItem(this.textOption);
        addItem(this.linkOption);
        addItem(this.photoOption);
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Submit new post";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.textOption)) {
            SubmitActivity.start(getActivity(), 0, getSubreddit());
        } else if (item.equals(this.linkOption)) {
            SubmitActivity.start(getActivity(), 1, getSubreddit());
        } else if (item.equals(this.photoOption)) {
            SubmitActivity.start(getActivity(), 2, getSubreddit());
        }
        dismiss();
    }
}
