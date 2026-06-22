package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import com.bumptech.glide.MaterialPostsHelper;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.chip.ChipGroupInterface;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SubredditHelper;
import com.resync.app.dialogs.SubredditSortAccessDialogBottomSheet;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractChipGroup extends ChipGroup implements ChipGroupInterface {
    private String period;

    @BindView(R.id.chip_sort)
    ContentChip chipSort;

    private String subreddit;
    private String sort;

    public AbstractChipGroup(Context context) {
        super(context);
        ViewGroup.inflate(getContext(), getLayoutResource(), this);
        ButterKnife.bind(this);
        setVisibility(8);
    }

    private void updateSortChip(String str, String str2) {
        Logger.verbose("ACCESS: " + str + " SORT: " + str2);
        this.chipSort.setText(SubredditHelper.appendSortSuffix(str, str2));
        this.chipSort.setChipIconResource(MaterialPostsHelper.getSortIconRes(str, str2));
    }

    @OnClick(R.id.chip_sort)
    @Optional
    public void onSort() {
        if (StringUtils.isNotEmpty(this.subreddit)) {
            FragmentUtils.showWithArguments(SubredditSortAccessDialogBottomSheet.class, getFragmentManager(), SubredditSortAccessDialogBottomSheet.buildArguments(this.subreddit, this.sort, this.period));
        }
    }

    protected String getSubreddit() {
        return this.subreddit;
    }

    protected FragmentManager getFragmentManager() {
        return ContextHelper.getFragmentManager(getContext());
    }

    public void onMinimize() {
    }

    public void refreshChips() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ContentChip) {
                ((ContentChip) getChildAt(i)).setCompact();
            }
        }
    }

    public void bind(String str, String str2, String str3) {
        this.subreddit = str;
        this.sort = str2;
        this.period = str3;
        if (this.chipSort != null) {
            updateSortChip(str2, str3);
        }
    }
}
