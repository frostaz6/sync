package com.resync.app.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.EditFragment;
import com.resync.app.ui.fragment_dialogs.bottom.SubredditRulesBottomSheetFragment;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.flair.UserPickFlairRadioSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.PerSubSortHelper;
import com.resync.app.dialogs.FragmentUtils;

/* JADX INFO: renamed from: t9.e1 */
/* JADX INFO: compiled from: PlainSubredditMoreDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class PlainSubredditMoreDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    AbstractSelectionDialogBottomSheet.SelectionItem rulesOption;
    AbstractSelectionDialogBottomSheet.SelectionItem shareOption;
    AbstractSelectionDialogBottomSheet.SelectionItem messageModsOption;
    AbstractSelectionDialogBottomSheet.SelectionItem viewModsOption;
    AbstractSelectionDialogBottomSheet.SelectionItem flairOption;
    AbstractSelectionDialogBottomSheet.SelectionItem addToMultiOption;
    AbstractSelectionDialogBottomSheet.SelectionItem updateSortOption;

    /* JADX INFO: compiled from: PlainSubredditMoreDialogBottomSheet.java */
    class ResetSortClickListener implements DialogInterface.OnClickListener {
        ResetSortClickListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            PerSubSortHelper.removeSort(PlainSubredditMoreDialogBottomSheet.this.getSubreddit());
        }
    }

    /* JADX INFO: compiled from: PlainSubredditMoreDialogBottomSheet.java */
    class OverrideSortClickListener implements DialogInterface.OnClickListener {

        final /* synthetic */ Activity activity;

        OverrideSortClickListener(Activity activity) {
            this.activity = activity;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void applySort(String[] strArr, DialogInterface dialogInterface, int which) {
            PerSubSortHelper.setSort(PlainSubredditMoreDialogBottomSheet.this.getSubreddit(), strArr[which]);
            StaticToast.show("Sort updated");
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            String[] stringArray = this.activity.getResources().getStringArray(PlainSubredditMoreDialogBottomSheet.this.getSubreddit().equalsIgnoreCase("frontpage") ? R.array.post_sort_frontpage : R.array.post_sort);
            final String[] stringArray2 = this.activity.getResources().getStringArray(PlainSubredditMoreDialogBottomSheet.this.getSubreddit().equalsIgnoreCase("frontpage") ? R.array.post_sort_frontpage_values : R.array.post_sort_values);
            AlertDialogHelper.newBuilder(this.activity).setMessage("Update sort").setItems(stringArray, new DialogInterface.OnClickListener() { // from class: t9.f1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface2, int which) {
                    PlainSubredditMoreDialogBottomSheet.OverrideSortClickListener.this.applySort(stringArray2, dialogInterface2, which);
                }
            }).show();
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.rulesOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_rule_24, "Rules"));
        this.shareOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_share_24, "Share"));
        this.messageModsOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_email_24, "Message mods"));
        this.viewModsOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_shield, "View mods"));
        this.flairOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_label_24, "Flair"));
        this.addToMultiOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_add_circle_outline_white_24dp, "Add to multi"));
        this.updateSortOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_sort_24, "Update sort"));
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Subreddit options";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (this.rulesOption.equals(item)) {
            FragmentUtils.showWithSubreddit(SubredditRulesBottomSheetFragment.class, getChildFragmentManager(), getSubreddit());
        } else if (this.shareOption.equals(item)) {
            RedditHelper.shareTextWithSubject(getActivity(), "/r/" + getSubreddit(), "https://reddit.com/r/" + getSubreddit());
        } else if (this.messageModsOption.equals(item)) {
            if (AccountSingleton.getInstance().isLoggedIn()) {
                EditFragment.newMessageInstance("/r/" + getSubreddit()).show(getChildFragmentManager(), "EditFragment");
            } else {
                StaticToast.show(getContext(), R.string.common_generic_error_logged_out);
            }
        } else if (this.viewModsOption.equals(item)) {
            FragmentUtils.showWithSubreddit(ViewModsAsyncSelectionDialogBottomSheet.class, getChildFragmentManager(), getSubreddit());
        } else if (this.flairOption.equals(item)) {
            FragmentUtils.showWithSubreddit(UserPickFlairRadioSelectionDialogBottomSheet.class, getChildFragmentManager(), getSubreddit());
        } else if (this.addToMultiOption.equals(item)) {
            FragmentUtils.showWithSubreddit(AddToMultiAsyncRadioSelectionDialogBottomSheet.class, getChildFragmentManager(), getSubreddit());
        } else if (this.updateSortOption.equals(item)) {
            FragmentActivity activity = getActivity();
            AlertDialogHelper.newBuilder(activity).setMessage("Update subreddit sort").setPositiveButton("Override sort", new OverrideSortClickListener(activity)).setNegativeButton("Cancel", null).setNeutralButton("Reset", new ResetSortClickListener()).create().show();
        }
        dismiss();
    }
}
