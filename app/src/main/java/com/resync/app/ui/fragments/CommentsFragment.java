package com.resync.app.ui.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.android.volley.VolleyError;
import com.google.android.material.appbar.AppBarLayout;
import com.resync.app.R;
import com.resync.app.provider.RedditProvider;
import com.resync.app.sections.posts.toolbar.PostsSmallSubredditToolbarView;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.activities.CasualActivity;
import com.resync.app.ui.activities.media.AbstractImageActivity;
import com.resync.app.ui.fragment_dialogs.bottom.CommentActionsBottomSheetFragment;
import com.resync.app.ui.fragment_dialogs.bottom.CommentSearchBottomSheetFragment;
import com.resync.app.ui.util.animation.recycler.EnterExitAnimator;
import com.resync.app.ui.util.animation.recycler.comments.CommentsActionsAnimator;
import com.resync.app.ui.util.animation.recycler.comments.CommentsCollapseAnimator;
import com.resync.app.ui.util.animation.recycler.comments.CommentsExpandAnimator;
import com.resync.app.ui.util.animation.recycler.comments.CommentsMoreAnimator;
import com.resync.app.ui.views.TTSView;
import com.resync.app.ui.views.comments.CommentsCoordinatorLayout;
import com.resync.app.ui.views.comments.CommentsNavigationView;
import com.resync.app.ui.views.comments.CommentsReplyFab;
import com.resync.app.ui.views.comments.SportsButton;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.ui.views.monet.MonetSwipeRefreshLayout;
import com.resync.app.ui.views.recycler.CommentsRecyclerView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.LoadingViewHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.TabletHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import com.resync.app.text.html.Options;
import com.resync.app.text.html.SyncHtmlParser;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.images.ImageHelper;
import com.resync.app.events.AutoRefreshEvent;
import com.resync.app.events.CommentNavigationModeUpdatedEvent;
import com.resync.app.events.CommentNavigationSearchKeywordUpdatedEvent;
import com.resync.app.events.CommentNavigationSearchUserUpdatedEvent;
import com.resync.app.events.CommentPostUpdated;
import com.resync.app.events.CommentSearchEvent;
import com.resync.app.events.RefreshEvent;
import com.resync.app.events.SearchCommentSelected;
import com.resync.app.events.StartAutoRefreshCountdownEvent;
import com.resync.app.events.StopScrollEvent;
import com.resync.app.events.VolumeDownEvent;
import com.resync.app.events.VolumeUpEvent;
import com.resync.app.events.CommentSortUpdatedEvent;
import com.resync.app.events.ReplyClickedEvent;
import com.resync.app.events.ReplyLongClickedEvent;
import com.resync.app.events.StartLiveModeEvent;
import com.resync.app.events.ToggleCollapseEvent;
import com.resync.app.events.ViewAllImagesEvent;
import com.resync.app.events.ViewOtherDiscussionsEvent;
import com.resync.app.events.ViewSidebarEvent;
import com.resync.app.events.CommentDownvoteSwipeEvent;
import com.resync.app.events.CommentProfileSwipeEvent;
import com.resync.app.events.CommentReplySwipeEvent;
import com.resync.app.events.CommentSaveSwipeEvent;
import com.resync.app.events.CommentShareSwipeEvent;
import com.resync.app.events.CommentUpvoteSwipeEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.DragCloseEvent;
import com.resync.app.events.DragStartingEvent;
import com.resync.app.text.spans.CustomUrlSpan;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.adapters.CommentAdapter;
import com.resync.app.managers.VoteManager;
import com.resync.app.events.DeleteCommentEvent;
import com.resync.app.events.EditCommentEvent;
import com.resync.app.events.ScrollToPosition;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.SubredditAboutBottomSheetFragment;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.helpers.GestureNavigationHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.dialogs.CommentNavigationModeSelectionDialogBottomSheet;
import com.resync.app.dialogs.CommentSortDialogBottomSheet;
import com.resync.app.helpers.CommentsStateHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.presenters.CommentsPresenter;
import com.resync.app.presenters.PresenterUtils;
import com.resync.app.presenters.CommentsView;

/* JADX INFO: loaded from: classes2.dex */
public class CommentsFragment extends RedditFragment implements CommentsView, SwipeRefreshLayout.OnRefreshListener, LoadingViewHelper.RetryListener {

    @BindView(R.id.fragment_comments_app_bar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.fragment_comments_coordinator)
    CommentsCoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.fab)
    CommentsReplyFab mFab;

    @BindView(R.id.navigation_material)
    CommentsNavigationView mNavigation;

    @BindView(R.id.toolbar_small_header_view)
    PostsSmallSubredditToolbarView mPostsSmallSubredditToolbarView;

    @BindView(R.id.fragment_comments_recycler_view)
    CommentsRecyclerView mRecyclerView;

    @BindView(R.id.fragment_comments_sports)
    SportsButton mSportsButton;

    @BindView(R.id.fragment_comments_swipe_refresh_layout)
    MonetSwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.toolbar)
    MaterialYouToolbar mToolbar;

    @BindView(R.id.fragment_comments_tts)
    TTSView mTtsView;
    protected CommentsPresenter presenter;
    protected CommentAdapter adapter;
    protected LoadingViewHelper loadingViewHelper;
    protected Bundle savedState;
    protected boolean singlePage;
    protected boolean swipe;
    Bundle stateBundle = new Bundle();

    class OfflineRetryListener implements LoadingViewHelper.RetryListener {
        OfflineRetryListener() {
        }

        @Override // com.resync.app.helpers.ui.LoadingViewHelper.RetryListener
        public void onRetryAction() {
            CommentsFragment.this.onRefresh();
        }
    }

    class ScrollToCommentRunnable implements Runnable {
        final /* synthetic */ String commentId;

        ScrollToCommentRunnable(String str) {
            this.commentId = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            Cursor cursorS = CommentsFragment.this.presenter.getCursor();
            if (cursorS == null || cursorS.isClosed() || cursorS.getCount() <= 0) {
                return;
            }
            for (int i = 0; i < cursorS.getCount(); i++) {
                cursorS.moveToPosition(i);
                if (StringUtils.equalsIgnoreCase(this.commentId, cursorS.getString(cursorS.getColumnIndex("_id")))) {
                    CommentsFragment.this.scrollToPosition(i);
                    CommentsFragment.this.presenter.selectCommentAnimated(Post.fromCursorPosition(cursorS, i), false);
                    return;
                }
            }
        }
    }

    class FabScrollListener extends RecyclerView.OnScrollListener {
        FabScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (CommentsFragment.this.mCoordinatorLayout.isScrollEnabled() && !SettingsSingleton.getSettings().commentNavBar && SettingsSingleton.getSettings().commentsReplyFab && SettingsSingleton.getSettings().commentsReplyFabAutoHide) {
                if (dy > 0) {
                    CommentsFragment.this.mFab.hide();
                } else if (dy < 0) {
                    CommentsFragment.this.mFab.show();
                }
            }
        }
    }

    class ToolbarMenuListener implements Toolbar.OnMenuItemClickListener {
        ToolbarMenuListener() {
        }

        @Override // androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            CommentsPresenter commentsPresenter;
            if (menuItem.getItemId() == R.id.menu_comments_more) {
                CommentsPresenter commentsPresenter2 = CommentsFragment.this.presenter;
                if (commentsPresenter2 == null || commentsPresenter2.getPost() == null) {
                    return true;
                }
                FragmentUtils.showWithPost(CommentActionsBottomSheetFragment.class, CommentsFragment.this.getChildFragmentManager(), CommentsFragment.this.presenter.getPost());
                return true;
            }
            if (menuItem.getItemId() == R.id.menu_search) {
                CommentsPresenter commentsPresenter3 = CommentsFragment.this.presenter;
                if (commentsPresenter3 == null || commentsPresenter3.getPost() == null) {
                    return true;
                }
                BusSingleton.getBus().post(new CommentSearchEvent());
                return true;
            }
            if (menuItem.getItemId() != R.id.menu_sort || (commentsPresenter = CommentsFragment.this.presenter) == null || commentsPresenter.getPost() == null) {
                return true;
            }
            String sort = CommentsFragment.this.presenter.getSort();
            String suggestedSort = CommentsFragment.this.presenter.getPost() != null ? CommentsFragment.this.presenter.getPost().getSuggestedSort() : null;
            if (StringUtils.equalsIgnoreCase(sort, "suggested") && StringUtils.isNotEmpty(suggestedSort)) {
                sort = suggestedSort;
            } else if (StringUtils.equalsIgnoreCase(sort, "suggested")) {
                sort = "top";
            }
            FragmentUtils.showWithArguments(CommentSortDialogBottomSheet.class, CommentsFragment.this.getParentFragmentManager(), CommentSortDialogBottomSheet.buildArguments(CommentsFragment.this.presenter.getPost(), sort));
            return true;
        }
    }

    class NavigationClickListener implements View.OnClickListener {
        NavigationClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            View viewFindViewById = CommentsFragment.this.getActivity().findViewById(R.id.second_pane);
            Fragment fragmentJ0 = ContextHelper.getFragmentManager(CommentsFragment.this.getActivity()).findFragmentByTag("TabbedCommentsFragment");
            if (viewFindViewById == null || viewFindViewById.getVisibility() != 0 || !(fragmentJ0 instanceof TabbedCommentsFragment)) {
                CommentsFragment.this.getActivity().finish();
                return;
            }
            TabbedCommentsFragment tabbedCommentsFragment = (TabbedCommentsFragment) fragmentJ0;
            tabbedCommentsFragment.setSelectedPost(CommentsFragment.this.presenter.getPost());
            tabbedCommentsFragment.selectFirstTab();
        }
    }

    class TitleClickListener implements View.OnClickListener {
        TitleClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CommentsFragment.this.quickScrollToTop();
        }
    }

    class TitleLongClickListener implements View.OnLongClickListener {
        TitleLongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            CommentsFragment.this.onRefresh();
            return true;
        }
    }

    class NavigationModeRunnable implements Runnable {
        NavigationModeRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CommentsFragment commentsFragment = CommentsFragment.this;
            commentsFragment.mNavigation.setModeIcon(commentsFragment.presenter.getNavigation().getCurrentModeIcon());
            int nextPosition = CommentsFragment.this.presenter.getNavigation().findNext(0);
            if (nextPosition >= 0) {
                CommentsFragment.this.mRecyclerView.scrollToPosition(nextPosition);
            }
        }
    }

    class UserSearchRunnable implements Runnable {
        UserSearchRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CommentsFragment commentsFragment = CommentsFragment.this;
            commentsFragment.mNavigation.setModeIcon(commentsFragment.presenter.getNavigation().getCurrentModeIcon());
            int nextPosition = CommentsFragment.this.presenter.getNavigation().findNext(0);
            if (nextPosition == -1) {
                StaticToast.showWithContext(CommentsFragment.this.getContext(), "User not found");
            } else {
                CommentsFragment.this.mRecyclerView.scrollToPosition(nextPosition);
            }
        }
    }

    class KeywordSearchRunnable implements Runnable {
        final /* synthetic */ CommentNavigationSearchKeywordUpdatedEvent event;

        KeywordSearchRunnable(CommentNavigationSearchKeywordUpdatedEvent commentNavigationSearchKeywordUpdatedEvent) {
            this.event = commentNavigationSearchKeywordUpdatedEvent;
        }

        @Override // java.lang.Runnable
        public void run() {
            CommentsFragment commentsFragment = CommentsFragment.this;
            commentsFragment.mNavigation.setModeIcon(commentsFragment.presenter.getNavigation().getCurrentModeIcon());
            int nextPosition = CommentsFragment.this.presenter.getNavigation().findNext(0);
            if (nextPosition != -1) {
                CommentsFragment.this.mRecyclerView.scrollToPosition(nextPosition);
                return;
            }
            StaticToast.showWithContext(CommentsFragment.this.getContext(), "Zero results found for: " + this.event.keyword);
        }
    }

    class SingleThreadRetryListener implements LoadingViewHelper.RetryListener {
        SingleThreadRetryListener() {
        }

        @Override // com.resync.app.helpers.ui.LoadingViewHelper.RetryListener
        public void onRetryAction() {
            CommentsFragment.this.mRecyclerView.scrollToPosition(0);
            CommentsFragment commentsFragment = CommentsFragment.this;
            commentsFragment.presenter.loadAllComments(commentsFragment.getArguments());
        }
    }

    private boolean isActive() {
        return !this.paused && isVisible();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void collapseAppBar() {
        this.mAppBarLayout.setExpanded(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onTitleClicked(View view) {
        com.resync.app.util.links.LinkHandler.openSubreddit(getContext(), getSubreddit());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onSortClicked(View view) {
        com.resync.app.util.links.LinkHandler.openSubreddit(getContext(), getSubreddit());
    }

    public static CommentsFragment newInstance(String str, String str2, String str3, String str4, boolean singlePage, boolean swipe, boolean continueInstance) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle bundleP0 = com.resync.app.presenters.CommentsPresenterImpl.buildArgs(str, str2, str3, str4, swipe);
        bundleP0.putBoolean("SinglePage", singlePage);
        bundleP0.putBoolean("continue_instance", continueInstance);
        commentsFragment.setArguments(bundleP0);
        return commentsFragment;
    }

    public static CommentsFragment newInstanceFromPost(Post post, String str, boolean singlePage) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle bundleQ0 = com.resync.app.presenters.CommentsPresenterImpl.buildArgsFromPost(post, str);
        bundleQ0.putBoolean("SinglePage", singlePage);
        commentsFragment.setArguments(bundleQ0);
        return commentsFragment;
    }

    public static CommentsFragment newInstanceForSwipe(Post post) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle bundleQ0 = com.resync.app.presenters.CommentsPresenterImpl.buildArgsFromPost(post, "hot");
        bundleQ0.putBoolean("Swipe", true);
        commentsFragment.setArguments(bundleQ0);
        return commentsFragment;
    }

    private void scrollToTargetComment(Cursor cursor) {
        if (cursor == null || cursor.isClosed() || cursor.getCount() <= 0 || this.savedState != null || TextUtils.isEmpty(this.presenter.getCommentId())) {
            return;
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            if (Post.fromCursorPosition(cursor, i).getId().equals(this.presenter.getCommentId())) {
                this.mRecyclerView.scrollToPosition(i);
                return;
            }
        }
    }

    private void bindCursor(Cursor cursor) {
        boolean wasAtTop = this.adapter.getLastCommentPosition() <= 0;
        this.adapter.setCursor(cursor);
        Bundle bundle = this.savedState;
        if (bundle != null && bundle.containsKey("SyncRecyclerView")) {
            Logger.verbose("CommentsFragment", "Restoring the state of the CommentsRecyclerView");
            Parcelable parcelable = this.savedState.getParcelable("SyncRecyclerView");
            this.savedState.remove("SyncRecyclerView");
            this.mRecyclerView.onRestoreInstanceState(parcelable);
        } else if (this.stateBundle.containsKey("SyncRecyclerView")) {
            Logger.verbose("CommentsFragment", "Restoring stop instance");
            Parcelable parcelable2 = this.stateBundle.getParcelable("SyncRecyclerView");
            this.stateBundle.remove("SyncRecyclerView");
            this.mRecyclerView.onRestoreInstanceState(parcelable2);
        } else if (this.stateBundle.containsKey("CommentStateKey") && !this.swipe) {
            Logger.verbose("CommentsFragment", "Restoring state instance");
            Parcelable parcelable3 = this.stateBundle.getParcelable("CommentStateKey");
            setDefaultAnimation();
            this.stateBundle.remove("CommentStateKey");
            this.mRecyclerView.onRestoreInstanceState(parcelable3);
            this.mAppBarLayout.setExpanded(false, false);
        }
        if (!wasAtTop || TextUtils.isEmpty(this.presenter.getCommentId())) {
            return;
        }
        scrollToTargetComment(cursor);
    }

    public CommentsPresenter getPresenter() {
        return this.presenter;
    }

    public String getSubreddit() {
        CommentsPresenter commentsPresenter = this.presenter;
        if (commentsPresenter == null || commentsPresenter.getPost() == null) {
            return null;
        }
        return this.presenter.getPost().getSubreddit();
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setSortLabel(String str, String str2) {
        if (this.mPostsSmallSubredditToolbarView == null) {
            return;
        }
        if (StringUtils.equalsIgnoreCase(str, "suggested") && StringUtils.isNotEmpty(str2)) {
            this.mPostsSmallSubredditToolbarView.setSortLabel(str2);
        } else if (StringUtils.equalsIgnoreCase(str, "suggested")) {
            this.mPostsSmallSubredditToolbarView.setSortLabel("Top");
        } else {
            this.mPostsSmallSubredditToolbarView.setSortLabel(str);
        }
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setMoreCommentsAnimation() {        if (!isActive()) {
            Logger.verbose("CommentsFragment", "Ignoring set more comments animation");
        } else if (!SettingsSingleton.getSettings().animateCommentMore) {
            setNoAnimation();
        } else {
            CrashlyticsHelper.logEvent(4, "item-animator", "Setting MoreCommentsAnimation");
            this.mRecyclerView.setItemAnimator(new CommentsMoreAnimator());
        }
    }

    @SuppressLint({"Range"})
    public void scrollToSearchResult(String str) {
        getView().postDelayed(new ScrollToCommentRunnable(str), 300L);
    }

    public void quickScrollToTop() {
        if (!isHidden() && SettingsSingleton.getSettings().postsQuickScroll && this.mRecyclerView.canScrollVertically(-1)) {
            this.mAppBarLayout.setExpanded(true, false);
            this.mRecyclerView.scrollToTop();
            this.mRecyclerView.post(new Runnable() { // from class: ma.d
                @Override // java.lang.Runnable
                public final void run() {
                    CommentsFragment.this.collapseAppBar();
                }
            });
        }
    }

    public void setPresenter(CommentsPresenter commentsPresenter) {
        this.presenter = commentsPresenter;
        commentsPresenter.restoreArguments(getArguments());
    }

    public void setupViews() {
        this.mTtsView.setPresenter(this.presenter);
        this.mRecyclerView.setPresenter(this.presenter);
        this.mRecyclerView.addOnScrollListener(new FabScrollListener());
        this.mToolbar.setNavigationIcon(R.drawable.outline_arrow_back_24);
        this.mToolbar.setOnMenuItemClickListener(new ToolbarMenuListener());
        this.mToolbar.setNavigationOnClickListener(new NavigationClickListener());
        this.mPostsSmallSubredditToolbarView.setOnClickListener(new TitleClickListener());
        this.mPostsSmallSubredditToolbarView.setOnLongClickListener(new TitleLongClickListener());
        this.mPostsSmallSubredditToolbarView.getTitleView().setOnLongClickListener(null);
        this.mPostsSmallSubredditToolbarView.getSortView().setOnLongClickListener(null);
        this.mPostsSmallSubredditToolbarView.getTitleView().setOnClickListener(new View.OnClickListener() { // from class: ma.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CommentsFragment.this.onTitleClicked(view);
            }
        });
        this.mPostsSmallSubredditToolbarView.getSortView().setOnClickListener(new View.OnClickListener() { // from class: ma.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CommentsFragment.this.onSortClicked(view);
            }
        });
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(-2, -2);
        if (SettingsSingleton.getSettings().commentNavigationPosition == 0) {
            layoutParams.gravity = 83;
        } else if (SettingsSingleton.getSettings().commentNavigationPosition == 1) {
            layoutParams.gravity = 81;
        } else if (SettingsSingleton.getSettings().commentNavigationPosition == 2) {
            layoutParams.gravity = 85;
        }
        this.mNavigation.setLayoutParams(layoutParams);
        if (!SettingsSingleton.getSettings().commentsScrollbar) {
            this.mRecyclerView.setScrollBarSize(0);
        }
        this.mRecyclerView.bindToolbar(this.mCoordinatorLayout, this.mAppBarLayout, this.mToolbar);
        if (this.singlePage && TabletHelper.isLandscape() && TabletHelper.isTabletMode()) {
            int padding = WindowUtils.getDisplayWidth() / 6;
            this.mRecyclerView.setPadding(padding, 0, padding, 0);
        }
        GestureNavigationHelper.applyGestureNavPadding(getActivity(), this.mRecyclerView);
        LoadingViewHelper loadingViewHelper = new LoadingViewHelper(getActivity(), this.mSwipeRefreshLayout, this);
        this.loadingViewHelper = loadingViewHelper;
        loadingViewHelper.setAnchorView(this.mCoordinatorLayout);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        CommentAdapter commentAdapter = new CommentAdapter(getActivity(), this.presenter);
        this.adapter = commentAdapter;
        this.mRecyclerView.setAdapter(commentAdapter);
        onPostUpdated(null);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.singlePage = getArguments().getBoolean("SinglePage");
        this.swipe = getArguments().getBoolean("Swipe");
        if (PresenterUtils.shouldAttachPresenter()) {
            setPresenter(new com.resync.app.presenters.CommentsPresenterImpl(getActivity(), bundle, this));
        }
    }

    public void navigateNext() {
        setDefaultAnimation();
        int first = this.mRecyclerView.getNavigationPosition();
        Logger.verbose("CommentsNav", "First: " + first);
        if (first == -1) {
            return;
        }
        int next = this.presenter.getNavigation().findNext(first);
        Logger.verbose("CommentsNav", "Next: " + next);
        if (next != -1) {
            this.mRecyclerView.scrollToPosition(next);
        }
    }

    public void navigatePrevious() {
        setDefaultAnimation();
        int first = this.mRecyclerView.getNavigationPosition();
        Logger.verbose("CommentNavigation", "THIS WAS USED: " + first);
        if (first == -1) {
            return;
        }
        if (first <= 1) {
            this.mRecyclerView.scrollToPosition(0);
            return;
        }
        int previous = this.presenter.getNavigation().findPrevious(first);
        Logger.verbose("CommentNavigation", "PREVIOUS: " + previous);
        if (previous != -1) {
            this.mRecyclerView.scrollToPosition(previous);
        } else {
            this.mRecyclerView.scrollToPosition(0);
        }
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setCollapseAnimation() {        if (!isActive()) {
            Logger.verbose("CommentsFragment", "Ignoring set collapse animation");
        } else if (!SettingsSingleton.getSettings().animateCommentCollapse) {
            setNoAnimation();
        } else {
            CrashlyticsHelper.logEvent(4, "item-animator", "Setting CollapseAnimation");
            this.mRecyclerView.setItemAnimator(new CommentsCollapseAnimator());
        }
    }

    public void showSportsButton() {
        this.mSportsButton.show();
        this.mSportsButton.startCountdown();
    }

    @SuppressLint({"Range"})
    public void viewAllImages(String str) {
        String string;
        String str2;
        CommentsPresenter commentsPresenter = this.presenter;
        if (commentsPresenter == null || commentsPresenter.getCursor() == null || this.presenter.getCursor().isClosed() || this.presenter.getCursor().getCount() < 1) {
            return;
        }
        Cursor cursorS = this.presenter.getCursor();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int selectedIndex = 0;
        for (int position = 0; position < this.presenter.getCursor().getCount(); position++) {
            cursorS.moveToPosition(position);
            if (position == 0) {
                string = cursorS.getString(cursorS.getColumnIndex("selftext_processed"));
                str2 = cursorS.getString(cursorS.getColumnIndex("selftext_raw")) + "\n\n[Post link](http://reddit.com/r/" + cursorS.getString(cursorS.getColumnIndex("subreddit")) + "/comments/" + cursorS.getString(cursorS.getColumnIndex("_id")) + ")";
            } else {
                string = cursorS.getString(cursorS.getColumnIndex("body_processed"));
                str2 = cursorS.getString(cursorS.getColumnIndex("body_raw")) + "\n\n[Comment link](http://reddit.com/r/" + cursorS.getString(cursorS.getColumnIndex("subreddit")) + "/comments/" + cursorS.getString(cursorS.getColumnIndex("link_id")) + "/_/" + cursorS.getString(cursorS.getColumnIndex("_id")) + ")";
            }
            SpannableTextViewStringBuilder spannableTextViewStringBuilder = new SpannableTextViewStringBuilder();
            if (!TextUtils.isEmpty(string)) {
                SyncHtmlParser.parse(spannableTextViewStringBuilder, Options.disabled(), string);
                Iterator<SpannableTextViewStringBuilder.Span> it = spannableTextViewStringBuilder.getSpans().iterator();
                while (it.hasNext()) {
                    Object obj = it.next().span;
                    if (obj instanceof CustomUrlSpan) {
                        CustomUrlSpan customUrlSpan = (CustomUrlSpan) obj;
                        if (LinkTypeHelper.isImage(customUrlSpan.getUrl())) {
                            arrayList.add(ImageHelper.normalizeImageUrl(customUrlSpan.getUrl()));
                            arrayList2.add(str2);
                        }
                    }
                }
            }
        }
        if (arrayList.size() <= 0) {
            StaticToast.showWithContext(getActivity(), "There are no images to view");
            return;
        }
        int index = 0;
        while (true) {
            if (index >= arrayList.size()) {
                break;
            }
            if (StringUtils.equalsIgnoreCase((CharSequence) arrayList.get(index), str)) {
                selectedIndex = index;
                break;
            }
            index++;
        }
        startActivity(AbstractImageActivity.createGalleryIntent(getActivity(), (String[]) arrayList.toArray(new String[arrayList.size()]), (String[]) arrayList2.toArray(new String[arrayList2.size()]), selectedIndex));
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), viewGroup, false);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // com.resync.app.presenters.CommentsView
    public void showNoComments() {    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        this.loadingViewHelper.hide();
        CommentsPresenter commentsPresenter = this.presenter;
        if (commentsPresenter != null) {
            commentsPresenter.onDestroy();
        }
        CommentsRecyclerView commentsRecyclerView = this.mRecyclerView;
        if (commentsRecyclerView != null) {
            commentsRecyclerView.teardown();
        }
        super.onDestroy();
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setSubredditTitle(String str) {        if (this.mPostsSmallSubredditToolbarView == null) {
            return;
        }
        Logger.verbose("Setting title: " + str);
        this.mPostsSmallSubredditToolbarView.setTitle(str);
        this.mPostsSmallSubredditToolbarView.bindSubredditIcon(str);
    }

    @Override // com.resync.app.presenters.CommentsView
    public void showSingleThreadNotification() {        if (this.loadingViewHelper.isShown()) {
            return;
        }
        Logger.verbose("CommentsFragment", "Showing the single thread notification");
        this.loadingViewHelper.showError(R.string.single_comment, R.string.single_comment_button, new SingleThreadRetryListener());
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setEnterExitAnimation() {        if (!isActive()) {
            Logger.verbose("CommentsFragment", "Ignoring set enter exit animation");
            return;
        }
        Logger.verbose("CommentsFragment", "Set enter exit animation");
        if (!SettingsSingleton.getSettings().animateEnter) {
            setNoAnimation();
        } else {
            CrashlyticsHelper.logEvent(4, "item-animator", "Setting EnterExitAnimation");
            this.mRecyclerView.setItemAnimator(new EnterExitAnimator());
        }
    }

    @Override // com.resync.app.presenters.CommentsView
    public void saveCommentState(Parcelable parcelable) {        if (this.swipe) {
            return;
        }
        this.stateBundle.remove("CommentStateKey");
        this.stateBundle.putParcelable("CommentStateKey", parcelable);
    }

    @Override // com.resync.app.presenters.CommentsView
    public void onCommentCollapsed(Post post) {        if (SettingsSingleton.getSettings().collapseParent) {
            try {
                int navPosition = this.mRecyclerView.getNavigationPosition();
                if (Objects.equals(post, Post.fromCursorPosition(this.presenter.getCursor(), navPosition))) {
                    this.mRecyclerView.scrollToPositionInstant(navPosition);
                }
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setNoAnimation() {        this.mNavigation.resetAnimations();
        if (!isActive()) {
            Logger.verbose("CommentsFragment", "Ignoring setting no animation");
        } else {
            CrashlyticsHelper.logEvent(4, "item-animator", "Setting no animation");
            this.mRecyclerView.setItemAnimator(null);
        }
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setCursor(Cursor cursor) {        this.adapter.getItemCount();
        Logger.verbose("CommentsFragment", "Setting cursor directly!");
        Logger.verbose("CommentsFragment", "Count: " + this.adapter.getCount());
        if (this.savedState == null && this.adapter.getCount() == 0) {
            if (TextUtils.isEmpty(this.presenter.getCommentId())) {
                setEnterExitAnimation();
            } else {
                setNoAnimation();
            }
        }
        bindCursor(cursor);
        if (!this.presenter.isLoading()) {
            this.mNavigation.animateIn();
        }
        this.mRecyclerView.refreshVideo();
    }

    @Override // com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    public void onPause() {
        CommentsRecyclerView commentsRecyclerView = this.mRecyclerView;
        if (commentsRecyclerView != null) {
            commentsRecyclerView.releaseVideo();
        }
        TTSView tTSView = this.mTtsView;
        if (tTSView != null) {
            tTSView.stop();
        }
        super.onPause();
    }

    @Subscribe
    public void forceAutoRefresh(AutoRefreshEvent autoRefreshEvent) {
        if (isHidden()) {
            return;
        }
        this.presenter.autoRefresh();
    }

    @Override // com.resync.app.presenters.CommentsView
    public void showOffline() {        if (this.loadingViewHelper.isShown()) {
            return;
        }
        Logger.verbose("CommentsFragment", "Showing the offline notification");
        this.loadingViewHelper.showError(R.string.common_offline, R.string.common_refresh, new OfflineRetryListener());
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
    public void onRefresh() {
        this.presenter.refresh();
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setRefreshing(boolean refreshing) {        this.loadingViewHelper.setRefreshing(refreshing, !(this.loadingViewHelper.isShowingMessage(R.string.single_comment) || this.loadingViewHelper.isShowingMessage(R.string.no_comments) || this.loadingViewHelper.isShowingMessage(R.string.common_offline)));
    }

    @Override // com.resync.app.presenters.CommentsView
    public void scrollToPosition(int position) {        this.mRecyclerView.scrollToPosition(position);
    }

    public boolean isLoadingViewActive() {
        return this.loadingViewHelper.isShown();
    }

    @Override // com.resync.app.helpers.ui.LoadingViewHelper.RetryListener
    public void onRetryAction() {
        this.presenter.recover();
    }

    @Override // com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: k2 */
    public void onResume() {
        super.onResume();
        this.mRecyclerView.createVideoView();
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_comments;
    }

    @Override // com.resync.app.presenters.BaseViewInterface
    public void showError(VolleyError volleyError) {
        this.loadingViewHelper.showError(volleyError);
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable("SyncRecyclerView", this.mRecyclerView.onSaveInstanceState());
        super.onSaveInstanceState(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        CommentsPresenter commentsPresenter = this.presenter;
        if (commentsPresenter != null) {
            commentsPresenter.onStart();
        }
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        CommentsPresenter commentsPresenter = this.presenter;
        if (commentsPresenter != null) {
            commentsPresenter.onStop();
        }
        CommentsRecyclerView commentsRecyclerView = this.mRecyclerView;
        if (commentsRecyclerView != null) {
            commentsRecyclerView.onResume();
            this.mRecyclerView.forceLayout();
            this.mRecyclerView.setItemAnimator(null);
            this.stateBundle.putParcelable("SyncRecyclerView", this.mRecyclerView.onSaveInstanceState());
            if (TextUtils.isEmpty(this.presenter.getCommentId()) && !this.swipe && this.mRecyclerView.canScrollVertically(-1)) {
                CommentsStateHelper.saveState(this.presenter.getPostId(), this.mRecyclerView.onSaveInstanceState());
            }
        }
        hideSportsButton();
        super.onStop();
    }

    @OnLongClick
    public boolean nextLongPressed() {
        if (this.adapter.getLastCommentPosition() <= 0) {
            return true;
        }
        this.mRecyclerView.scrollToPosition(this.adapter.getItemCount());
        return true;
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setDefaultAnimation() {        if (!isActive()) {
            Logger.verbose("CommentsFragment", "Ignoring set default animation");
        } else if (!SettingsSingleton.getSettings().animateCommentInsert) {
            setNoAnimation();
        } else {
            CrashlyticsHelper.logEvent(4, "item-animator", "Setting DefaultAnimation");
            this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setupViews();
        if (this.presenter != null) {
            if (bundle == null) {
                Logger.verbose("CommentsFragment", "Requesting new data be loaded");
                this.presenter.load(false);
            } else {
                Logger.verbose("CommentsFragment", "Requesting the cursor be loaded");
                this.presenter.loadCursor();
            }
        }
    }

    @OnClick
    public void onCollapseClicked() {
        toggleCollapseAll();
    }

    @Subscribe
    public void onCommentNavigationModeUpdated(CommentNavigationModeUpdatedEvent commentNavigationModeUpdatedEvent) {
        if (isHidden()) {
            return;
        }
        this.presenter.getNavigation().setMode(commentNavigationModeUpdatedEvent.mode);
        getView().postDelayed(new NavigationModeRunnable(), 300L);
    }

    @Subscribe
    public void onCommentNavigationSearchKeywordUpdated(CommentNavigationSearchKeywordUpdatedEvent commentNavigationSearchKeywordUpdatedEvent) {
        if (isHidden()) {
            return;
        }
        this.presenter.getNavigation().setMode(10);
        this.presenter.getNavigation().setSearchKeyword(commentNavigationSearchKeywordUpdatedEvent.keyword);
        this.presenter.getNavigation().process(this.presenter.getCursor(), this.presenter.getPost());
        getView().postDelayed(new KeywordSearchRunnable(commentNavigationSearchKeywordUpdatedEvent), 300L);
    }

    @Subscribe
    public void onCommentNavigationSearchUserUpdated(CommentNavigationSearchUserUpdatedEvent commentNavigationSearchUserUpdatedEvent) {
        if (isHidden()) {
            return;
        }
        this.presenter.getNavigation().setMode(9);
        this.presenter.getNavigation().setSearchAuthor(commentNavigationSearchUserUpdatedEvent.user);
        this.presenter.getNavigation().process(this.presenter.getCursor(), this.presenter.getPost());
        getView().postDelayed(new UserSearchRunnable(), 300L);
    }

    @Subscribe
    public void onCommentSearchSelected(CommentSearchEvent commentSearchEvent) {
        CommentsPresenter commentsPresenter;
        if (isHidden() || this.adapter == null || (commentsPresenter = this.presenter) == null || commentsPresenter.getPost() == null) {
            return;
        }
        if (this.adapter.getLastCommentPosition() == 0) {
            StaticToast.show("There are no comments to search!");
        } else {
            FragmentUtils.showWithArguments(CommentSearchBottomSheetFragment.class, getParentFragmentManager(), CommentSearchBottomSheetFragment.buildArgs(this.presenter.getPost()));
        }
    }

    @Subscribe
    public void onCommentSortUpdatedEvent(CommentSortUpdatedEvent commentSortUpdatedEvent) {
        if (isHidden()) {
            return;
        }
        this.presenter.changeSort(getArguments(), commentSortUpdatedEvent.sort);
    }

    @Subscribe
    public void onDeleteCommentEvent(DeleteCommentEvent deleteCommentEvent) {
        if (!isHidden()) {
            throw null;
        }
    }

    @Subscribe
    public void onDownvoteSwipeEvent(CommentDownvoteSwipeEvent commentDownvoteSwipeEvent) {
        if (isHidden()) {
            return;
        }
        VoteManager.voteOnPost(getContext(), 1, commentDownvoteSwipeEvent.post, -1);
    }

    @Subscribe
    public void onDragClose(DragCloseEvent dragCloseEvent) {
    }

    @Subscribe
    public void onDragStarted(DragStartingEvent dragStartingEvent) {
    }

    @Subscribe
    public void onEditCommentEvent(EditCommentEvent editCommentEvent) {
        if (!isHidden()) {
            throw null;
        }
    }

    @OnClick
    public void onNextClicked() {
        navigateNext();
    }

    @Subscribe
    public void onPostUpdated(CommentPostUpdated commentPostUpdated) {
        if (isHidden() || this.presenter == null) {
            return;
        }
        this.mAppBarLayout.setLiftOnScrollTargetViewId(R.id.fragment_comments_recycler_view);
    }

    @OnClick
    public void onPreviousClicked() {
        navigatePrevious();
    }

    @Subscribe
    public void onProfleSwipeEvent(CommentProfileSwipeEvent commentProfileSwipeEvent) {
        if (isHidden()) {
            return;
        }
        com.resync.app.util.links.LinkHandler.openProfile(getContext(), commentProfileSwipeEvent.post.getAuthor());
    }

    @OnClick
    public void onReplyClicked() {
        this.presenter.replyToPost();
    }

    @Subscribe
    public void onReplyClickedEvent(ReplyClickedEvent replyClickedEvent) {
        if (isHidden()) {
            return;
        }
        this.presenter.replyToPost();
    }

    @Subscribe
    public void onReplyLongClickedEvent(ReplyLongClickedEvent replyLongClickedEvent) {
        if (isHidden()) {
            return;
        }
        FragmentUtils.showWithPost(CommentActionsBottomSheetFragment.class, getChildFragmentManager(), this.presenter.getPost());
    }

    @Subscribe
    public void onReplySwipeEvent(CommentReplySwipeEvent commentReplySwipeEvent) {
        if (isHidden()) {
            return;
        }
        this.presenter.replyToComment(commentReplySwipeEvent.post);
    }

    @Subscribe
    public void onSaveSwipeEvent(CommentSaveSwipeEvent commentSaveSwipeEvent) {
        if (isHidden()) {
            return;
        }
        this.presenter.saveComment(commentSaveSwipeEvent.post);
    }

    @Subscribe
    public void onScrollToPositionEvent(ScrollToPosition scrollToPosition) {
        Cursor cursorS = this.presenter.getCursor();
        if (cursorS == null || cursorS.isClosed() || cursorS.getCount() <= 0) {
            return;
        }
        for (int i = 0; i < cursorS.getCount(); i++) {
            cursorS.moveToPosition(i);
            if (StringUtils.equalsIgnoreCase(scrollToPosition.post.getId(), cursorS.getString(cursorS.getColumnIndex("_id")))) {
                setNoAnimation();
                this.mRecyclerView.scrollToPositionAnimated(i, false);
                return;
            }
        }
    }

    @Subscribe
    public void onSearchCommentSelected(SearchCommentSelected searchCommentSelected) {
        if (isHidden()) {
            return;
        }
        scrollToSearchResult(searchCommentSelected.post.getId());
    }

    @Subscribe
    public void onShareSwipeEvent(CommentShareSwipeEvent commentShareSwipeEvent) {
        if (isHidden()) {
            return;
        }
        this.presenter.shareComment(commentShareSwipeEvent.post);
    }

    @Subscribe
    public void onStartLiveMode(StartLiveModeEvent startLiveModeEvent) {
        CommentsPresenter commentsPresenter;
        if (isHidden() || this.adapter == null || (commentsPresenter = this.presenter) == null || commentsPresenter.getPost() == null) {
            return;
        }
        showSportsButton();
    }

    @Subscribe
    public void onStopScrollEvent(StopScrollEvent stopScrollEvent) {
        if (isHidden()) {
            return;
        }
        this.mRecyclerView.invalidateItemDecorations();
    }

    @Subscribe
    public void onToggleCollapse(ToggleCollapseEvent toggleCollapseEvent) {
        if (isHidden()) {
            return;
        }
        toggleCollapseAll();
    }

    @Subscribe
    public void onUpvoteSwipeEvent(CommentUpvoteSwipeEvent commentUpvoteSwipeEvent) {
        if (isHidden()) {
            return;
        }
        VoteManager.voteOnPost(getContext(), 1, commentUpvoteSwipeEvent.post, 1);
    }

    @Subscribe
    public void onViewAllImages(ViewAllImagesEvent viewAllImagesEvent) {
        CommentsPresenter commentsPresenter;
        if (isHidden() || this.adapter == null || (commentsPresenter = this.presenter) == null || commentsPresenter.getPost() == null) {
            return;
        }
        viewAllImages(viewAllImagesEvent.imageUrl);
    }

    @Subscribe
    public void onViewOtherDiscussions(ViewOtherDiscussionsEvent viewOtherDiscussionsEvent) {
        CommentsPresenter commentsPresenter;
        if (isHidden() || this.adapter == null || (commentsPresenter = this.presenter) == null || commentsPresenter.getPost() == null) {
            return;
        }
        CasualActivity.openPostUrl(getActivity(), this.presenter.getPost());
    }

    @Subscribe
    public void onViewSidebar(ViewSidebarEvent viewSidebarEvent) {
        CommentsPresenter commentsPresenter;
        if (isHidden() || this.adapter == null || (commentsPresenter = this.presenter) == null || commentsPresenter.getPost() == null) {
            return;
        }
        FragmentUtils.showWithSubreddit(SubredditAboutBottomSheetFragment.class, getChildFragmentManager(), getSubreddit());
    }

    @Subscribe
    public void onVolumeDown(VolumeDownEvent volumeDownEvent) {
        if (!isHidden() && (getActivity() instanceof BaseActivity) && ((BaseActivity) getActivity()).getCreatedTime() == volumeDownEvent.timestamp) {
            this.mAppBarLayout.setExpanded(true, false);
            navigateNext();
        }
    }

    @Subscribe
    public void onVolumeUp(VolumeUpEvent volumeUpEvent) {
        if (!isHidden() && (getActivity() instanceof BaseActivity) && ((BaseActivity) getActivity()).getCreatedTime() == volumeUpEvent.timestamp) {
            this.mAppBarLayout.setExpanded(true, false);
            navigatePrevious();
        }
    }

    @OnLongClick
    public boolean optionsLongClicked(View view) {
        ArrayList arrayList = new ArrayList();
        try {
            Cursor cursorS = this.presenter.getCursor();
            for (int i = 0; i < cursorS.getCount(); i++) {
                Post post = Post.fromCursorPosition(cursorS, i);
                if (!arrayList.contains(post.getAuthor()) && !TextUtils.isEmpty(post.getAuthor())) {
                    arrayList.add(post.getAuthor().toLowerCase(Locale.ENGLISH));
                }
            }
        } catch (Exception unused) {
        }
        FragmentUtils.showWithArguments(CommentNavigationModeSelectionDialogBottomSheet.class, getParentFragmentManager(), CommentNavigationModeSelectionDialogBottomSheet.buildArguments(this.presenter.getNavigation(), arrayList));
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        this.savedState = bundle;
    }

    @OnLongClick
    public boolean previousLongPressed() {
        this.mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Subscribe
    public void refresh(RefreshEvent refreshEvent) {
        if (isHidden()) {
            return;
        }
        onRefresh();
    }

    @Override // com.resync.app.presenters.CommentsView
    public void notifyDataSetChanged() {        this.adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void startAutoRefreshCoundown(StartAutoRefreshCountdownEvent startAutoRefreshCountdownEvent) {
        if (isHidden()) {
            return;
        }
        this.mSportsButton.startCountdown();
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setExpandAnimation() {        if (!isActive()) {
            Logger.verbose("CommentsFragment", "Ignoring set expand animation");
        } else if (!SettingsSingleton.getSettings().animateCommentCollapse) {
            setNoAnimation();
        } else {
            CrashlyticsHelper.logEvent(4, "item-animator", "Setting ExpandAnimation");
            this.mRecyclerView.setItemAnimator(new CommentsExpandAnimator());
        }
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setActionsAnimation() {        if (!isActive()) {
            Logger.verbose("CommentsFragment", "Ignoring setting actions animation");
        } else if (!SettingsSingleton.getSettings().animateCommentActions) {
            this.mRecyclerView.setItemAnimator(null);
        } else {
            Logger.verbose("CommentsFragment", "Setting actions animation");
            this.mRecyclerView.setItemAnimator(new CommentsActionsAnimator());
        }
    }

    public void toggleCollapseAll() {
        Cursor cursorS = this.presenter.getCursor();
        if (cursorS != null && !cursorS.isClosed() && cursorS.getCount() > 0) {
            if (this.adapter.getLastCommentPosition() == this.presenter.getNavigation().getThreadIndices().size()) {
                getActivity().getContentResolver().update(RedditProvider.URI_SHOW_ALL_COMMENTS, null, this.presenter.getDatabaseId(), null);
            } else {
                getActivity().getContentResolver().update(RedditProvider.URI_HIDE_ALL_COMMENTS, null, this.presenter.getDatabaseId(), null);
            }
        }
        setEnterExitAnimation();
        getActivity().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    public void hideSportsButton() {
        this.mSportsButton.hide();
    }
}
