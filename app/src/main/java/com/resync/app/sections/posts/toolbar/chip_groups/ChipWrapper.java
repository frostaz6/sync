package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.bumptech.glide.MaterialPostsHelper;

/* JADX INFO: loaded from: classes.dex */
public class ChipWrapper extends LinearLayout {

    private final ProfileChipGroup profileChipGroup;
    private final DefaultSubredditChipGroup defaultSubredditChipGroup;
    private final PlainSubredditChipGroup plainSubredditChipGroup;
    private final MultiSubredditChipGroup multiSubredditChipGroup;
    private final FriendsSubredditChipGroup friendsSubredditChipGroup;
    private final SearchSubredditChipGroup searchSubredditChipGroup;
    private final FullSubredditSearchSubredditChipGroup fullSubredditSearchChipGroup;
    private final WatchingSubredditChipGroup watchingSubredditChipGroup;
    private final DomainChipGroup domainChipGroup;
    private final SeenSubredditChipGroup seenSubredditChipGroup;
    private final MessagingChipGroup messagingChipGroup;
    private final ModChipGroup modChipGroup;
    private AbstractChipGroup activeChipGroup;

    public ChipWrapper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(0);
        ProfileChipGroup profileChipGroup = new ProfileChipGroup(getContext());
        this.profileChipGroup = profileChipGroup;
        DefaultSubredditChipGroup defaultSubredditChipGroup = new DefaultSubredditChipGroup(getContext());
        this.defaultSubredditChipGroup = defaultSubredditChipGroup;
        PlainSubredditChipGroup plainSubredditChipGroup = new PlainSubredditChipGroup(getContext());
        this.plainSubredditChipGroup = plainSubredditChipGroup;
        MultiSubredditChipGroup multiSubredditChipGroup = new MultiSubredditChipGroup(getContext());
        this.multiSubredditChipGroup = multiSubredditChipGroup;
        FriendsSubredditChipGroup friendsSubredditChipGroup = new FriendsSubredditChipGroup(getContext());
        this.friendsSubredditChipGroup = friendsSubredditChipGroup;
        SearchSubredditChipGroup searchSubredditChipGroup = new SearchSubredditChipGroup(getContext());
        this.searchSubredditChipGroup = searchSubredditChipGroup;
        FullSubredditSearchSubredditChipGroup fullSubredditSearchSubredditChipGroup = new FullSubredditSearchSubredditChipGroup(getContext());
        this.fullSubredditSearchChipGroup = fullSubredditSearchSubredditChipGroup;
        WatchingSubredditChipGroup watchingSubredditChipGroup = new WatchingSubredditChipGroup(getContext());
        this.watchingSubredditChipGroup = watchingSubredditChipGroup;
        DomainChipGroup domainChipGroup = new DomainChipGroup(getContext());
        this.domainChipGroup = domainChipGroup;
        SeenSubredditChipGroup seenSubredditChipGroup = new SeenSubredditChipGroup(getContext());
        this.seenSubredditChipGroup = seenSubredditChipGroup;
        MessagingChipGroup messagingChipGroup = new MessagingChipGroup(getContext());
        this.messagingChipGroup = messagingChipGroup;
        ModChipGroup modChipGroup = new ModChipGroup(getContext());
        this.modChipGroup = modChipGroup;
        addView(profileChipGroup);
        addView(defaultSubredditChipGroup);
        addView(plainSubredditChipGroup);
        addView(multiSubredditChipGroup);
        addView(friendsSubredditChipGroup);
        addView(searchSubredditChipGroup);
        addView(fullSubredditSearchSubredditChipGroup);
        addView(watchingSubredditChipGroup);
        addView(domainChipGroup);
        addView(seenSubredditChipGroup);
        addView(messagingChipGroup);
        addView(modChipGroup);
    }

    private void showGroup(AbstractChipGroup abstractChipGroup, String str, String str2, String str3) {
        this.activeChipGroup = abstractChipGroup;
        abstractChipGroup.setVisibility(0);
        this.activeChipGroup.bind(str, str2, str3);
    }

    public void minimize() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof AbstractChipGroup) {
                ((AbstractChipGroup) getChildAt(i)).onMinimize();
            }
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        super.addView(view);
    }

    public void refresh() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof AbstractChipGroup) {
                ((AbstractChipGroup) getChildAt(i)).refreshChips();
            }
        }
    }

    public void bindSection(int section, String str, String str2, String str3) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(8);
        }
        if (section == 2) {
            showGroup(this.messagingChipGroup, str, str2, str3);
        } else if (section == 1) {
            showGroup(this.profileChipGroup, str, str2, str3);
        } else if (section == 3) {
            showGroup(this.modChipGroup, str, str2, str3);
        } else if (section == 0) {
            if (MaterialPostsHelper.isAggregateFeed(str)) {
                showGroup(this.defaultSubredditChipGroup, str, str2, str3);
            } else if (MaterialPostsHelper.isStandardSubreddit(str)) {
                showGroup(this.plainSubredditChipGroup, str, str2, str3);
            } else if (MaterialPostsHelper.isMultireddit(str)) {
                showGroup(this.multiSubredditChipGroup, str, str2, str3);
            } else if (MaterialPostsHelper.isFriends(str)) {
                showGroup(this.friendsSubredditChipGroup, str, str2, str3);
            } else if (MaterialPostsHelper.isSearch(str)) {
                showGroup(this.searchSubredditChipGroup, str, str2, str3);
            } else if (MaterialPostsHelper.isSubSearch(str)) {
                showGroup(this.fullSubredditSearchChipGroup, str, str2, str3);
            } else if (str.equals("list__watching")) {
                showGroup(this.watchingSubredditChipGroup, str, str2, str3);
            } else if (MaterialPostsHelper.isDomain(str)) {
                showGroup(this.domainChipGroup, str, str2, str3);
            } else if (MaterialPostsHelper.isSeen(str)) {
                showGroup(this.seenSubredditChipGroup, str, str2, str3);
            }
        }
        ((View) getParent()).scrollTo(0, 0);
    }
}
