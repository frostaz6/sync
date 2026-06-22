package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.helpers.ContextHelper;

/* JADX INFO: loaded from: classes.dex */
public class FriendsSubredditChipGroup extends AbstractChipGroup {
    public FriendsSubredditChipGroup(Context context) {
        super(context);
    }

    @Override // com.google.android.material.chip.ChipGroupInterface
    public int getLayoutResource() {
        return R.layout.view_chip_group_friends_subreddit;
    }

    @OnClick(R.id.chip_friends)
    public void onFriendsClicked() {
        ContextHelper.openAbout(getContext(), getSubreddit());
    }
}
