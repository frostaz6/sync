package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.helpers.ContextHelper;

/* JADX INFO: loaded from: classes.dex */
public class MultiSubredditChipGroup extends AbstractChipGroup {
    public MultiSubredditChipGroup(Context context) {
        super(context);
    }

    @Override // com.google.android.material.chip.ChipGroupInterface
    public int getLayoutResource() {
        return R.layout.view_chip_group_multi_subreddit;
    }

    @OnClick(R.id.chip_manage)
    public void onManageClicked() {
        ContextHelper.openAbout(getContext(), getSubreddit());
    }
}
