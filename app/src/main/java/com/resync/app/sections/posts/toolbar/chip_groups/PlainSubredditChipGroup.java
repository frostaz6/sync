package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import butterknife.BindView;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import com.squareup.otto.Subscribe;
import com.resync.app.managers.FavouriteSubredditManager;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.events.WatchEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.WatchHelper;
import com.resync.app.dialogs.PlainSubredditMoreDialogBottomSheet;
import com.resync.app.dialogs.PostFlairsAsyncSelectionDialogBottomSheet;
import com.resync.app.dialogs.ToggleFavAlertDialogBottomSheet;
import com.resync.app.dialogs.ToggleJoinAlertDialogBottomSheet;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: loaded from: classes.dex */
public class PlainSubredditChipGroup extends AbstractChipGroup {

    @BindView(R.id.chip_about)
    ContentChip chipAbout;

    @BindView(R.id.chip_fav)
    ContentChip chipFav;

    @BindView(R.id.chip_flair)
    ContentChip chipFlair;

    @BindView(R.id.chip_join)
    ContentChip chipJoin;

    @BindView(R.id.chip_more)
    ContentChip chipMore;

    @BindView(R.id.chip_watch)
    ContentChip chipWatch;

    @BindView(R.id.chip_wiki)
    ContentChip chipWiki;

    public PlainSubredditChipGroup(Context context) {
        super(context);
    }

    public void updateFavChip() {
        if (FavouriteSubredditManager.getInstance().isFavourite(getSubreddit())) {
            this.chipFav.setText("Favorited");
            this.chipFav.setChipIconResource(R.drawable.outline_bookmark_24);
            this.chipFav.setSelected(true);
        } else {
            this.chipFav.setText("Favorite");
            this.chipFav.setChipIconResource(R.drawable.outline_bookmark_border_24);
            this.chipFav.setSelected(false);
        }
    }

    public void updateFlairChip() {
        if (AccountSingleton.isLoggedIn(getContext())) {
            return;
        }
        this.chipFlair.setVisibility(8);
    }

    public void updateJoinChip() {
        if (!AccountSingleton.isLoggedIn(getContext())) {
            this.chipJoin.setVisibility(8);
            return;
        }
        if (SubredditsManager.getInstance().isSubscribed(getSubreddit())) {
            this.chipJoin.setText("Joined");
            this.chipJoin.setChipIconResource(R.drawable.outline_playlist_add_check_24);
            this.chipJoin.setSelected(true);
        } else {
            this.chipJoin.setText("Join");
            this.chipJoin.setChipIconResource(R.drawable.outline_playlist_add_24);
            this.chipJoin.setSelected(false);
        }
    }

    public void updateWatchChip() {
        if (WatchHelper.isWatched(getSubreddit())) {
            this.chipWatch.setText("Watching");
            this.chipWatch.setChipIconResource(R.drawable.outline_notifications_active_24);
            this.chipWatch.setSelected(true);
        } else {
            this.chipWatch.setText("Watch");
            this.chipWatch.setChipIconResource(R.drawable.outline_notifications_none_24);
            this.chipWatch.setSelected(false);
        }
    }

    @Override // com.google.android.material.chip.ChipGroupInterface
    public int getLayoutResource() {
        return R.layout.view_chip_group_plain_subreddit;
    }

    @OnClick(R.id.chip_about)
    public void onAbout() {
        ContextHelper.openAbout(getContext(), getSubreddit());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        BusSingleton.getBus().unregister(this);
        super.onDetachedFromWindow();
    }

    @OnClick(R.id.chip_fav)
    public void onFav() {
        FragmentUtils.showWithSubreddit(ToggleFavAlertDialogBottomSheet.class, getFragmentManager(), getSubreddit());
    }

    @OnClick(R.id.chip_flair)
    public void onFlair() {
        FragmentUtils.showWithSubreddit(PostFlairsAsyncSelectionDialogBottomSheet.class, getFragmentManager(), getSubreddit());
    }

    @OnClick(R.id.chip_join)
    public void onJoin() {
        FragmentUtils.showWithSubreddit(ToggleJoinAlertDialogBottomSheet.class, getFragmentManager(), getSubreddit());
    }

    @OnClick(R.id.chip_more)
    public void onMoreClicked() {
        FragmentUtils.showWithSubreddit(PlainSubredditMoreDialogBottomSheet.class, getFragmentManager(), getSubreddit());
    }

    @Subscribe
    public void onSubscribedChanged(SubredditsChangedEvent subredditsChangedEvent) {
        updateJoinChip();
        updateFavChip();
    }

    @OnClick(R.id.chip_watch)
    public void onWatch() {
        WatchHelper.toggleWatch(getContext(), getSubreddit());
    }

    @Subscribe
    public void onWatchEvent(WatchEvent watchEvent) {
        updateWatchChip();
    }

    @OnClick(R.id.chip_wiki)
    public void onWiki() {
        LinkHelper.open(getContext(), "https://www.reddit.com/r/" + getSubreddit() + "/wiki/index");
    }

    @Override // com.resync.app.sections.posts.toolbar.chip_groups.AbstractChipGroup
    public void onMinimize() {
        this.chipSort.setVisibility(8);
        this.chipAbout.setVisibility(8);
        this.chipMore.setVisibility(8);
        this.chipWiki.setVisibility(8);
        this.chipFlair.setVisibility(8);
    }

    @Override // com.resync.app.sections.posts.toolbar.chip_groups.AbstractChipGroup
    public void bind(String str, String str2, String str3) {
        super.bind(str, str2, str3);
        updateJoinChip();
        updateFlairChip();
        updateFavChip();
        updateWatchChip();
    }
}
