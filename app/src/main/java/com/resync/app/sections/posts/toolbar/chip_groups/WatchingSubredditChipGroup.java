package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.managers.WatchingHelper;

/* JADX INFO: loaded from: classes.dex */
public class WatchingSubredditChipGroup extends AbstractChipGroup {
    public WatchingSubredditChipGroup(Context context) {
        super(context);
    }

    @Override // com.google.android.material.chip.ChipGroupInterface
    public int getLayoutResource() {
        return R.layout.view_chip_group_watching_subreddit;
    }

    @OnClick(R.id.chip_remote_watching)
    public void onRemoveWatchingClicked() {
        WatchingHelper.clearWatchedPosts();
        StaticToast.show("Cleared");
    }
}
