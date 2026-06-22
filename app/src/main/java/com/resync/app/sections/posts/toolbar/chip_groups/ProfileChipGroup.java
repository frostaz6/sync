package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.bumptech.glide.MaterialPostsHelper;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import com.squareup.otto.Subscribe;
import com.resync.app.premium.UserPaintManager;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.SubredditArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.FriendsManager;
import com.resync.app.managers.NewUserTagManager;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.premium.dialogs.UltraManagePaintSelectionBottomSheet;
import com.resync.app.premium.dialogs.UltraManageTagSelectionBottomSheet;
import com.resync.app.events.OnComposeMessageEvent;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.PaintAndTagsUpdatedEvent;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.dialogs.BlockAlertDialogBottomSheet;
import com.resync.app.dialogs.ProfileSectionSelectionDialogBottomSheet;
import com.resync.app.dialogs.TrophiesAsyncSelectionDialogBottomSheet;
import com.resync.app.net.oauth.OAuthFriendRequest;
import com.resync.app.net.oauth.OAuthUnfriendRequest;

/* JADX INFO: loaded from: classes.dex */
public class ProfileChipGroup extends AbstractChipGroup {

    @BindView(R.id.chip_about)
    ContentChip chipAbout;

    @BindView(R.id.chip_block)
    ContentChip chipBlock;

    @BindView(R.id.chip_follow)
    ContentChip chipFollow;

    @BindView(R.id.chip_friend)
    ContentChip chipFriend;

    @BindView(R.id.chip_message)
    ContentChip chipMessage;

    @BindView(R.id.chip_paint)
    ContentChip chipPaint;

    @BindView(R.id.chip_section)
    ContentChip chipSection;

    @BindView(R.id.chip_share)
    ContentChip chipShare;

    @BindView(R.id.chip_tag)
    ContentChip chipTag;

    public ProfileChipGroup(Context context) {
        super(context);
        if (!AccountSingleton.isLoggedIn(getContext())) {
            this.chipFriend.setVisibility(8);
            this.chipBlock.setVisibility(8);
            this.chipMessage.setVisibility(8);
        }
        setSingleLine(true);
    }

    private String getUsername() {
        if (StringUtils.isNotEmpty(getSubreddit())) {
            return MaterialPostsHelper.extractProfileUsername(getSubreddit());
        }
        return null;
    }

    private boolean hasUsername() {
        return StringUtils.isNotEmpty(getUsername());
    }

    private void updateFollowChip() {
        if (hasUsername()) {
            if (AccountSingleton.getInstance().getUsername().equals(getUsername())) {
                this.chipFollow.setVisibility(8);
            } else if (isFollowing(getUsername())) {
                this.chipFollow.setChecked(true);
                this.chipFollow.setSelected(true);
            } else {
                this.chipFollow.setChecked(false);
                this.chipFollow.setSelected(false);
            }
        }
    }

    private void updateFriendChip() {
        if (hasUsername()) {
            if (AccountSingleton.getInstance().getUsername().equals(getUsername())) {
                this.chipFriend.setVisibility(8);
            } else if (FriendsManager.getInstance().isFriend(getUsername())) {
                this.chipFriend.setChecked(true);
                this.chipFriend.setSelected(true);
            } else {
                this.chipFriend.setChecked(false);
                this.chipFriend.setSelected(false);
            }
        }
    }

    private void updatePaintChip() {
        if (hasUsername()) {
            this.chipPaint.setSelected(UserPaintManager.getInstance().hasPaint(getUsername()));
        }
    }

    private void updateTagChip() {
        if (hasUsername()) {
            if (NewUserTagManager.getInstance().hasTag(getUsername())) {
                this.chipTag.setChecked(true);
                this.chipTag.setSelected(true);
                this.chipTag.setText(NewUserTagManager.getInstance().getTag(getUsername()));
            } else {
                this.chipTag.setChecked(false);
                this.chipTag.setSelected(false);
                this.chipTag.setText("Tag user");
            }
        }
    }

    public boolean isFollowing(String str) {
        String str2 = "u_" + str;
        SubredditArrayList subredditArrayList = new SubredditArrayList(SubredditsManager.getInstance().getSubreddits());
        SubredditArrayList subredditArrayList2 = new SubredditArrayList();
        for (int size = subredditArrayList.size() - 1; size >= 0; size--) {
            if (subredditArrayList.get(size).startsWith("u_")) {
                subredditArrayList2.addIfAbsent(subredditArrayList.remove(size));
            }
        }
        return subredditArrayList2.contains(str2);
    }

    @Override // com.google.android.material.chip.ChipGroupInterface
    public int getLayoutResource() {
        return R.layout.view_chip_group_profile;
    }

    @OnClick(R.id.chip_about)
    public void onAboutClicked() {
        ContextHelper.openAbout(getContext(), getSubreddit());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @OnClick(R.id.chip_block)
    public void onBlockClicked() {
        FragmentUtils.showWithUser(BlockAlertDialogBottomSheet.class, getFragmentManager(), getUsername());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        BusSingleton.getBus().unregister(this);
        super.onDetachedFromWindow();
    }

    @OnClick(R.id.chip_follow)
    public void onFollowClicked() {
        SubredditsManager.getInstance().toggleSubscription("u_" + getUsername());
        updateFollowChip();
    }

    @OnClick(R.id.chip_friend)
    public void onFriendClicked() {
        if (FriendsManager.getInstance().isFriend(getUsername())) {
            StaticToast.show("Unfriended");
            OAuthRequestManager.sendWithAppContext(new OAuthUnfriendRequest(getContext(), getUsername()));
            FriendsManager.getInstance().removeFriend(getUsername());
            updateFriendChip();
            return;
        }
        StaticToast.show("Added friend");
        OAuthRequestManager.sendWithAppContext(new OAuthFriendRequest(getContext(), getUsername()));
        FriendsManager.getInstance().addFriend(getUsername());
        updateFriendChip();
    }

    @OnClick(R.id.chip_message)
    public void onMessageClicked() {
        BusSingleton.getBus().post(new OnComposeMessageEvent());
    }

    @OnClick(R.id.chip_paint)
    public void onPaintClicked() {
        UserPaintManager.getInstance().showAddPaintDialog(getContext(), getUsername());
    }

    @OnLongClick
    public boolean onPaintLongClicked() {
        FragmentUtils.show(UltraManagePaintSelectionBottomSheet.class, getFragmentManager());
        return true;
    }

    @OnClick(R.id.chip_section)
    public void onSectionClicked() {
        FragmentUtils.showWithUser(ProfileSectionSelectionDialogBottomSheet.class, getFragmentManager(), getUsername());
    }

    @OnClick(R.id.chip_share)
    public void onShareClicked() {
        RedditHelper.shareTextWithSubject(getContext(), getUsername(), "https://reddit.com/u/" + getUsername());
    }

    @OnClick(R.id.chip_tag)
    public void onTagClicked() {
        NewUserTagManager.toggleTag(getContext(), getUsername());
    }

    @Subscribe
    public void onTagEvent(PaintAndTagsUpdatedEvent paintAndTagsUpdatedEvent) {
        updateTagChip();
        updatePaintChip();
    }

    @OnLongClick
    public boolean onTagLongClicked() {
        FragmentUtils.show(UltraManageTagSelectionBottomSheet.class, getFragmentManager());
        return true;
    }

    @OnClick(R.id.chip_tophies)
    public void onTrophyClicked() {
        FragmentUtils.showWithUser(TrophiesAsyncSelectionDialogBottomSheet.class, getFragmentManager(), getUsername());
    }

    @Override // com.resync.app.sections.posts.toolbar.chip_groups.AbstractChipGroup
    public void onMinimize() {
        this.chipSection.setVisibility(8);
        this.chipSort.setVisibility(8);
        this.chipAbout.setVisibility(8);
        this.chipShare.setVisibility(8);
        this.chipBlock.setVisibility(8);
    }

    @Override // com.resync.app.sections.posts.toolbar.chip_groups.AbstractChipGroup
    public void bind(String str, String str2, String str3) {
        super.bind(str, str2, str3);
        this.chipSection.setText(MaterialPostsHelper.extractProfileSection(str));
        this.chipSection.setChipIconResource(MaterialPostsHelper.getProfileSectionIconRes(str));
        updateFollowChip();
        updateFriendChip();
        updateTagChip();
        updatePaintChip();
        boolean paintEnabled;
        if (UltraHelper.isUltra()) {
            if (!RemoteConfigHelper.isC4266fa()) {
                paintEnabled = true;
            } else {
                String key = PreferencesHelper.getDefaultPrefs().getString(KeyManager.purchaseTokenKey(), null);
                paintEnabled = !StringUtils.isEmpty(key) && StringUtils.containsIgnoreCase(key, ".AO-");
            }
        } else {
            paintEnabled = false;
        }
        this.chipPaint.setVisibility(paintEnabled ? 0 : 8);
    }
}
