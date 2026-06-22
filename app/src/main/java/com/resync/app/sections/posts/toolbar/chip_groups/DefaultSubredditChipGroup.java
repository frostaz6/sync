package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.dialogs.ChangeViewSelectionDialogBottomSheet;

/* JADX INFO: loaded from: classes.dex */
public class DefaultSubredditChipGroup extends AbstractChipGroup {
    public DefaultSubredditChipGroup(Context context) {
        super(context);
    }

    @Override // com.google.android.material.chip.ChipGroupInterface
    public int getLayoutResource() {
        return R.layout.view_chip_group_default_subreddit;
    }

    @OnClick(R.id.chip_view)
    public void onChangeViewClicked() {
        FragmentUtils.show(ChangeViewSelectionDialogBottomSheet.class, ContextHelper.getFragmentManager(getContext()));
    }

    @OnClick(R.id.chip_random)
    public void onPreviewsClicked() {
        BusSingleton.getBus().post(new SubredditSelectedEvent("random", false));
    }

    @OnClick(R.id.chip_top_growing)
    public void onTopGrowingClicked() {
        StaticToast.show("Do we want this?");
    }
}
