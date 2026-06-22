package com.resync.app.ui.fragments;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.resync.app.R;
import com.resync.app.sections.posts.toolbar.PostsSmallSubredditToolbarView;
import com.resync.app.ui.views.TTSView;
import com.resync.app.ui.views.comments.CommentsCoordinatorLayout;
import com.resync.app.ui.views.comments.CommentsNavigationView;
import com.resync.app.ui.views.comments.CommentsReplyFab;
import com.resync.app.ui.views.comments.SportsButton;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.ui.views.monet.MonetSwipeRefreshLayout;
import com.resync.app.ui.views.recycler.CommentsRecyclerView;

/* JADX INFO: loaded from: classes2.dex */
public class CommentsFragment_ViewBinding implements Unbinder {
    private CommentsFragment target;
    private View view1;
    private View view2;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private View view3;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private View view4;
    private View view5;

    class OnReplyClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ CommentsFragment target;

        OnReplyClickedListener(CommentsFragment commentsFragment) {
            this.target = commentsFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onReplyClicked();
        }
    }

    class OnCollapseClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ CommentsFragment target;

        OnCollapseClickedListener(CommentsFragment commentsFragment) {
            this.target = commentsFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onCollapseClicked();
        }
    }

    class OnPreviousClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ CommentsFragment target;

        OnPreviousClickedListener(CommentsFragment commentsFragment) {
            this.target = commentsFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onPreviousClicked();
        }
    }

    class OnPreviousLongPressedListener implements View.OnLongClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ CommentsFragment target;

        OnPreviousLongPressedListener(CommentsFragment commentsFragment) {
            this.target = commentsFragment;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.target.previousLongPressed();
        }
    }

    class OnNextClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ CommentsFragment target;

        OnNextClickedListener(CommentsFragment commentsFragment) {
            this.target = commentsFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onNextClicked();
        }
    }

    class OnNextLongPressedListener implements View.OnLongClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ CommentsFragment target;

        OnNextLongPressedListener(CommentsFragment commentsFragment) {
            this.target = commentsFragment;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.target.nextLongPressed();
        }
    }

    class OnOptionsLongClickedListener implements View.OnLongClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ CommentsFragment target;

        OnOptionsLongClickedListener(CommentsFragment commentsFragment) {
            this.target = commentsFragment;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.target.optionsLongClicked(view);
        }
    }

    public CommentsFragment_ViewBinding(CommentsFragment commentsFragment, View view) {
        this.target = commentsFragment;
        commentsFragment.mCoordinatorLayout = (CommentsCoordinatorLayout) Utils.findRequiredViewAsType(view, R.id.fragment_comments_coordinator, "field 'mCoordinatorLayout'", CommentsCoordinatorLayout.class);
        commentsFragment.mSwipeRefreshLayout = (MonetSwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.fragment_comments_swipe_refresh_layout, "field 'mSwipeRefreshLayout'", MonetSwipeRefreshLayout.class);
        commentsFragment.mRecyclerView = (CommentsRecyclerView) Utils.findRequiredViewAsType(view, R.id.fragment_comments_recycler_view, "field 'mRecyclerView'", CommentsRecyclerView.class);
        commentsFragment.mTtsView = (TTSView) Utils.findRequiredViewAsType(view, R.id.fragment_comments_tts, "field 'mTtsView'", TTSView.class);
        commentsFragment.mSportsButton = (SportsButton) Utils.findRequiredViewAsType(view, R.id.fragment_comments_sports, "field 'mSportsButton'", SportsButton.class);
        commentsFragment.mAppBarLayout = (AppBarLayout) Utils.findRequiredViewAsType(view, R.id.fragment_comments_app_bar, "field 'mAppBarLayout'", AppBarLayout.class);
        commentsFragment.mToolbar = (MaterialYouToolbar) Utils.findRequiredViewAsType(view, R.id.toolbar, "field 'mToolbar'", MaterialYouToolbar.class);
        commentsFragment.mPostsSmallSubredditToolbarView = (PostsSmallSubredditToolbarView) Utils.findRequiredViewAsType(view, R.id.toolbar_small_header_view, "field 'mPostsSmallSubredditToolbarView'", PostsSmallSubredditToolbarView.class);
        commentsFragment.mNavigation = (CommentsNavigationView) Utils.findRequiredViewAsType(view, R.id.navigation_material, "field 'mNavigation'", CommentsNavigationView.class);
        commentsFragment.mFab = (CommentsReplyFab) Utils.findRequiredViewAsType(view, R.id.fab, "field 'mFab'", CommentsReplyFab.class);
        View viewC = Utils.findRequiredView(view, R.id.nav_reply, "method 'onReplyClicked'");
        this.view1 = viewC;
        viewC.setOnClickListener(new OnReplyClickedListener(commentsFragment));
        View viewC2 = Utils.findRequiredView(view, R.id.nav_collapse, "method 'onCollapseClicked'");
        this.view2 = viewC2;
        viewC2.setOnClickListener(new OnCollapseClickedListener(commentsFragment));
        View viewC3 = Utils.findRequiredView(view, R.id.nav_previous, "method 'onPreviousClicked' and method 'previousLongPressed'");
        this.view3 = viewC3;
        viewC3.setOnClickListener(new OnPreviousClickedListener(commentsFragment));
        viewC3.setOnLongClickListener(new OnPreviousLongPressedListener(commentsFragment));
        View viewC4 = Utils.findRequiredView(view, R.id.nav_next, "method 'onNextClicked' and method 'nextLongPressed'");
        this.view4 = viewC4;
        viewC4.setOnClickListener(new OnNextClickedListener(commentsFragment));
        viewC4.setOnLongClickListener(new OnNextLongPressedListener(commentsFragment));
        View viewC5 = Utils.findRequiredView(view, R.id.nav_icon, "method 'optionsLongClicked'");
        this.view5 = viewC5;
        viewC5.setOnLongClickListener(new OnOptionsLongClickedListener(commentsFragment));
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        CommentsFragment commentsFragment = this.target;
        if (commentsFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        commentsFragment.mCoordinatorLayout = null;
        commentsFragment.mSwipeRefreshLayout = null;
        commentsFragment.mRecyclerView = null;
        commentsFragment.mTtsView = null;
        commentsFragment.mSportsButton = null;
        commentsFragment.mAppBarLayout = null;
        commentsFragment.mToolbar = null;
        commentsFragment.mPostsSmallSubredditToolbarView = null;
        commentsFragment.mNavigation = null;
        commentsFragment.mFab = null;
        this.view1 = null;
        this.view2 = null;
        this.view3 = null;
        this.view4 = null;
        this.view5 = null;
    }
}
