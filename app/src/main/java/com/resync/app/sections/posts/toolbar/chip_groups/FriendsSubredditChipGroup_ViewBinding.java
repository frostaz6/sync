package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class FriendsSubredditChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private FriendsSubredditChipGroup target;

    private View viewChipFriends;

    class OnFriendsClickListener extends DebouncingOnClickListener {

        final /* synthetic */ FriendsSubredditChipGroup target;

        OnFriendsClickListener(FriendsSubredditChipGroup friendsSubredditChipGroup) {
            this.target = friendsSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onFriendsClicked();
        }
    }

    public FriendsSubredditChipGroup_ViewBinding(FriendsSubredditChipGroup friendsSubredditChipGroup, View view) {
        super(friendsSubredditChipGroup, view);
        this.target = friendsSubredditChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_friends, "method 'onFriendsClicked'");
        this.viewChipFriends = viewC;
        viewC.setOnClickListener(new OnFriendsClickListener(friendsSubredditChipGroup));
    }

    @Override
    public void unbind() {
        FriendsSubredditChipGroup friendsSubredditChipGroup = this.target;
        if (friendsSubredditChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        if (this.viewChipFriends != null) {
            this.viewChipFriends.setOnClickListener(null);
            this.viewChipFriends = null;
        }
        super.unbind();
    }
}
