package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.ChangeSubredditInputDialogBottomSheet;

/* JADX INFO: loaded from: classes.dex */
public class FullSubredditSearchSubredditChipGroup extends AbstractChipGroup {
    public FullSubredditSearchSubredditChipGroup(Context context) {
        super(context);
    }

    @Override // com.google.android.material.chip.ChipGroupInterface
    public int getLayoutResource() {
        return R.layout.view_chip_group_full_subreddit_search;
    }

    @OnClick(R.id.chip_search_options)
    public void onSearchOptionsClicked() {
        FragmentUtils.showWithSubreddit(ChangeSubredditInputDialogBottomSheet.class, getFragmentManager(), getSubreddit());
    }
}
