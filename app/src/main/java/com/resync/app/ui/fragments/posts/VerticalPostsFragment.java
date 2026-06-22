package com.resync.app.ui.fragments.posts;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import com.android.volley.VolleyError;
import com.bumptech.glide.MaterialPostsHelper;
import com.google.android.material.snackbar.Snackbar;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.activities.CasualActivity;
import com.resync.app.ui.fragment_dialogs.EditFragment;
import com.resync.app.ui.util.animation.recycler.EnterExitAnimator;
import com.resync.app.ui.util.animation.recycler.comments.OnFinishedListener;
import com.resync.app.ui.util.animation.recycler.posts.StockDefaultAnimation;
import com.resync.app.ui.util.recycler.CustomStaggeredGridLayoutManager;
import com.resync.app.ui.views.app_bars.PostsAppBarLayout;
import com.resync.app.ui.views.posts.PostsFab;
import com.resync.app.ui.views.recycler.PostsRecyclerView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.events.OnUiModeChanged;
import com.resync.app.events.RefreshEvent;
import com.resync.app.events.ShowBottomNavigationEvent;
import com.resync.app.events.StopScrollEvent;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.events.SubredditSortUpdatedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.FlingExoplayerEvent;
import com.resync.app.events.StopExoplayerEvent;
import com.resync.app.events.OnComposeMessageEvent;
import com.resync.app.events.HideReadEvent;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.events.RefreshPostsEvent;
import com.resync.app.events.ScrollToBottomOfPostsEvent;
import com.resync.app.events.ScrollToTopOfPostsEvent;
import com.resync.app.adapters.PostAdapter;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.dialogs.RandomProgressDialogBottomSheet;
import com.resync.app.ui.interfaces.PostHost;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.presenters.PostsPresenter;
import com.resync.app.model.Subreddit;

/* JADX INFO: loaded from: classes2.dex */
public class VerticalPostsFragment extends BasePostsFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    PostsRecyclerView recyclerView;

    /* JADX INFO: renamed from: v0, reason: collision with root package name */
    private PostAdapter adapter;
    private Bundle stateBundle = new Bundle();

    /* JADX INFO: renamed from: x0, reason: collision with root package name */
    Snackbar restoreSnackbar;
    private Parcelable appBarState;

    class ScrollListener extends RecyclerView.OnScrollListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        int scrollState = 0;

        ScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            this.scrollState = newState;
            if (newState == 2) {
                BusSingleton.getBus().post(new FlingExoplayerEvent());
            } else {
                VerticalPostsFragment.this.recyclerView.refreshVideo();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            VerticalPostsFragment.this.checkInfiniteScroll();
            if (SettingsSingleton.getSettings().mainFab && VerticalPostsFragment.this.getFab() != null && SettingsSingleton.getSettings().mainFabAutohide) {
                if (dy > 0) {
                    VerticalPostsFragment.this.getFab().hide();
                } else if (dy < 0) {
                    VerticalPostsFragment.this.getFab().show();
                }
            }
        }
    }

    class DragFinishedListener implements OnFinishedListener {
        DragFinishedListener() {
        }

        @Override // com.resync.app.ui.util.animation.recycler.comments.OnFinishedListener
        public void onFinished() {
            VerticalPostsFragment.this.recyclerView.addDrag();
        }
    }

    private boolean isShownNotHidden() {
        return !isHidden();
    }

    private boolean isActiveAndAdded() {
        return !this.paused && isAdded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaMarkAllRead() {
        this.presenter.markAllRead();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaOpenSubredditNewTask(SubredditSelectedEvent subredditSelectedEvent) {
        CasualActivity.openSubredditNewTask(getActivity(), subredditSelectedEvent.subreddit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaChangeSubreddit(SubredditSelectedEvent subredditSelectedEvent) {
        changeSubreddit(subredditSelectedEvent.subreddit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaCollapseAppBar() {
        if (getAppBar() != null) {
            getAppBar().setExpanded(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaRestoreScroll(Parcelable parcelable) {
        BusSingleton.getBus().post(new StopExoplayerEvent());
        getAppBar().setExpanded(false, false);
        this.recyclerView.invalidateItemDecorations();
        this.recyclerView.onRestoreInstanceState(parcelable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaCheckMorePosts(boolean showToast) {
        Logger.verbose("CHECKING");
        Logger.verbose("HIDE", "HIDE CHECKING SCROLL");
        if (checkInfiniteScroll()) {
            if (showToast) {
                StaticToast.showWithContext(getActivity(), "Grabbing more posts");
            }
        } else if (this.recyclerView.isAtBottom()) {
            Logger.verbose("HIDE", "FORCING NEW LOOKUP");
            this.presenter.onInfiniteScroll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaAddDrag() {
        this.recyclerView.addDrag();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaChangeSubredditExpand(String str) {
        getAppBar().setExpanded(true);
        BusSingleton.getBus().post(new ShowBottomNavigationEvent());
        super.changeSubreddit(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaRefreshVideo() {
        this.recyclerView.refreshVideo();
    }

    private void onLoaded() {
        if (getProgressView() != null) {
            getProgressView().setVisibility(8);
        }
        if (getView() != null) {
            getView().setTranslationY(SizeHelper.dpToPxFloat(30));
            getView().setAlpha(0.0f);
            getView().animate().alpha(1.0f).translationY(0.0f).setDuration(300L).setInterpolator(new FastOutSlowInInterpolator()).start();
        }
        onProgressReady();
        if (getAppBar() != null) {
            updateAppBarSection();
            getAppBar().setSubreddit(this.presenter.getUserOrSubredditInfo());
        }
    }

    private void clearTransition() {
    }

    private void showBottomNav() {
        BusSingleton.getBus().post(new ShowBottomNavigationEvent());
    }

    private void onProgressReady() {
        if (getFab() != null) {
            getFab().configure(getColumnCount(), getSubredditName());
        }
    }

    private void updateAppBarSection() {
        if (getAppBar() != null) {
            getAppBar().setLiftOnScrollTargetViewId(R.id.recyclerView);
            if (MaterialPostsHelper.isUserProfile(getSubredditName())) {
                getAppBar().setSection(getColumnCount(), getSubredditName(), getAccess(), getSort());
            } else {
                getAppBar().setSection(getColumnCount(), getSubredditName(), getAccess(), getSort());
            }
        }
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, com.resync.app.presenters.PostsView
    public void showStatusMessage(int messageRes) {
        super.showStatusMessage(messageRes);
        if (getProgressView() != null) {
            getProgressView().setVisibility(8);
        }
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment
    public void changeSubreddit(final String str) {
        if (this.recyclerView.getLayoutManager() instanceof CustomStaggeredGridLayoutManager) {
            this.recyclerView.smoothScrollToPosition(0);
        }
        this.recyclerView.post(new Runnable() { // from class: na.j
            @Override // java.lang.Runnable
            public final void run() {
                VerticalPostsFragment.this.lambdaChangeSubredditExpand(str);
            }
        });
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment
    public void applyUiMode(int uiMode) {
        Logger.verbose("Settings UI mode: " + uiMode);
        this.presenter.setUiMode(getArguments(), uiMode);
        clearTransition();
        setDefaultAnimation();
        int lastVisiblePosition = this.recyclerView.getLastVisiblePosition();
        for (int i = 0; i < this.recyclerView.getChildCount(); i++) {
            PostsRecyclerView postsRecyclerView = this.recyclerView;
            RecyclerView.ViewHolder viewHolder = postsRecyclerView.getChildViewHolder(postsRecyclerView.getChildAt(i));
            if (viewHolder instanceof RedditViewHolder) {
                ((RedditViewHolder) viewHolder).recycle();
            }
        }
        this.recyclerView.removeAllViewsInLayout();
        this.recyclerView.removeAllViews();
        this.recyclerView.applyMode(uiMode);
        this.recyclerView.getRecycledViewPool().clear();
        this.recyclerView.getAdapter().notifyDataSetChanged();
        this.recyclerView.smoothScrollToPosition(lastVisiblePosition);
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment
    public void setupViews() {
        if (!SettingsSingleton.getSettings().postScrollbar) {
            this.recyclerView.setScrollBarSize(0);
        }
        this.recyclerView.setupSwipe(this.presenter);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        clearTransition();
        this.adapter = new PostAdapter(getActivity(), this.recyclerView, this.presenter);
        this.recyclerView.setup(getActivity(), this.adapter, this.presenter.getUiMode());
        this.recyclerView.addOnScrollListener(new ScrollListener());
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment
    protected void bindCursor(Cursor cursor) {
        Bundle bundle;
        super.bindCursor(cursor);
        if (getActivity() instanceof PostHost) {
            ((PostHost) getActivity()).updateSidebar(getSubredditName());
        }
        if (cursor != null) {
            Logger.verbose("EE-DEBUG", "Setting cursor: " + cursor.getCount());
        } else {
            Logger.verbose("EE-DEBUG", "Setting cursor null");
        }
        if (cursor == null || cursor.getCount() == 0) {
            BusSingleton.getBus().post(new StopExoplayerEvent());
        }
        Logger.verbose("LOADING NEW CURSOR: " + cursor);
        Bundle bundle2 = this.savedState;
        if ((bundle2 != null && bundle2.containsKey("SyncRecyclerView")) || ((bundle = this.stateBundle) != null && bundle.containsKey("SyncRecyclerView"))) {
            this.recyclerView.setItemAnimator(null);
        }
        if (getAppBar() != null) {
            getAppBar().setSubreddit(this.presenter.getUserOrSubredditInfo());
        }
        int itemCount = this.adapter.getItemCount();
        this.adapter.setCursor(cursor);
        Bundle bundle3 = this.stateBundle;
        if (bundle3 == null || !bundle3.containsKey("SyncRecyclerView")) {
            Bundle bundle4 = this.savedState;
            if (bundle4 != null && bundle4.containsKey("SyncRecyclerView")) {
                Logger.verbose("VerticalPostsFragment", "Restoring destroy instance");
                this.recyclerView.onRestoreInstanceState(this.savedState.getParcelable("SyncRecyclerView"));
            }
        } else {
            Logger.verbose("VerticalPostsFragment", "Restoring stop instance");
            this.recyclerView.onRestoreInstanceState(this.stateBundle.getParcelable("SyncRecyclerView"));
        }
        Bundle bundle5 = this.stateBundle;
        if (bundle5 != null) {
            bundle5.remove("SyncRecyclerView");
        }
        Bundle bundle6 = this.savedState;
        if (bundle6 != null) {
            bundle6.remove("SyncRecyclerView");
        }
        if (itemCount == 0) {
            this.recyclerView.postDelayed(new Runnable() { // from class: na.e
                @Override // java.lang.Runnable
                public final void run() {
                    VerticalPostsFragment.this.lambdaRefreshVideo();
                }
            }, 500L);
        }
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, com.resync.app.presenters.PostsView
    public void setLoading(boolean loading, boolean force) {
        super.setLoading(loading, force);
        this.mSwipeRefreshLayout.setRefreshing(force && !hasData());
        if (getProgressView() != null) {
            getProgressView().setVisibility((force && hasData()) ? 0 : 8);
        }
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        super.onDestroy();
        PostsRecyclerView postsRecyclerView = this.recyclerView;
        if (postsRecyclerView != null) {
            postsRecyclerView.pause();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            this.loadingViewHelper.dismiss();
        }
        BusSingleton.getBus().post(new StopExoplayerEvent());
        if (getAppBar() != null) {
            if (hidden) {
                this.appBarState = getAppBar().saveBehaviorState();
                Logger.verbose("APPBAR", "Saving posts state: " + this.appBarState);
            } else {
                Logger.verbose("APPBAR", "Resuming posts state: " + this.appBarState);
                getAppBar().restoreBehaviorState(this.appBarState);
            }
        }
        if (hidden) {
            return;
        }
        onLoaded();
    }

    public boolean checkInfiniteScroll() {
        SwipeRefreshLayout swipeRefreshLayout;
        if (!this.recyclerView.isAtBottom() || this.paused || getCursor() == null) {
            return false;
        }
        // NOTE: was `swipeRefreshLayout.canChildScrollUp()` — that returns true whenever the list is
        // scrolled down (i.e. ALWAYS at the bottom), so with the current androidx swiperefreshlayout
        // (whose mTarget is the RecyclerView) it permanently blocked infinite scroll / pagination.
        // The intended guard is "don't paginate while a pull-to-refresh is in progress" => isRefreshing().
        if ((this.recyclerView.getItemAnimator() != null && (this.recyclerView.getItemAnimator() == null || this.recyclerView.getItemAnimator().isRunning())) || (swipeRefreshLayout = this.mSwipeRefreshLayout) == null || swipeRefreshLayout.isRefreshing() || !ConnectivityHelper.isConnected(getActivity()) || getView() == null || !getView().isShown()) {
            return false;
        }
        if (!this.presenter.onInfiniteScroll() || !(getActivity() instanceof BaseActivity)) {
            return true;
        }
        ((BaseActivity) getActivity()).onScrolled();
        this.recyclerView.resume();
        return true;
    }

    @Override // com.resync.app.presenters.PostsView
    public void setEnterExitAnimation() {
        if (!isActiveAndAdded()) {
            Logger.verbose("VerticalPostsFragment", "Ignoring set enter exit animation");
            return;
        }
        if (!SettingsSingleton.getSettings().animateEnter) {
            setNoAnimation();
            return;
        }
        this.recyclerView.removeDrag();
        EnterExitAnimator enterExitAnimator = new EnterExitAnimator(new DragFinishedListener());
        this.recyclerView.setItemAnimator(enterExitAnimator);
        this.recyclerView.postDelayed(new Runnable() { // from class: na.c
            @Override // java.lang.Runnable
            public final void run() {
                VerticalPostsFragment.this.lambdaAddDrag();
            }
        }, enterExitAnimator.getDuration() * 2);
    }

    protected PostsFab getFab() {
        if (getActivity() == null || getActivity().findViewById(R.id.fab) == null) {
            return null;
        }
        return (PostsFab) getActivity().findViewById(R.id.fab);
    }

    protected PostsAppBarLayout getAppBar() {
        if (getActivity() == null || getActivity().findViewById(R.id.appbar) == null || !(getActivity().findViewById(R.id.appbar) instanceof PostsAppBarLayout)) {
            return null;
        }
        return (PostsAppBarLayout) getActivity().findViewById(R.id.appbar);
    }

    protected View getProgressView() {
        if (getView() == null || !getView().isShown() || getActivity() == null || getActivity().findViewById(R.id.progress) == null) {
            return null;
        }
        return getActivity().findViewById(R.id.progress);
    }

    @Override // com.resync.app.presenters.PostsView
    public void setNoAnimation() {
        Logger.verbose("VerticalPostsFragment", "Setting no animation");
        this.recyclerView.setItemAnimator(null);
        checkInfiniteScroll();
    }

    public Subreddit getSubredditInfo() {
        PostsPresenter postsPresenter = this.presenter;
        if (postsPresenter != null) {
            return postsPresenter.getUserOrSubredditInfo();
        }
        return null;
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    public void onPause() {
        this.recyclerView.invalidateItemDecorations();
        this.recyclerView.releaseVideo();
        this.recyclerView.setItemAnimator(null);
        BusSingleton.getBus().post(new StopExoplayerEvent());
        super.onPause();
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
    public void onRefresh() {
        this.loadingViewHelper.dismiss();
        this.presenter.refresh();
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: k2 */
    public void onResume() {
        super.onResume();
        this.recyclerView.createVideoView();
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_posts_vertical;
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, com.resync.app.presenters.BaseViewInterface
    public void showError(VolleyError volleyError) {
        super.showError(volleyError);
        if (getProgressView() != null) {
            getProgressView().setVisibility(8);
        }
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable("SyncRecyclerView", this.recyclerView.onSaveInstanceState());
        if (getAppBar() != null) {
            if (isShownNotHidden()) {
                this.appBarState = getAppBar().saveBehaviorState();
                Logger.verbose("APPBAR", "Saving live posts state: " + this.appBarState);
            } else {
                Logger.verbose("APPBAR", "Saving cached posts state: " + this.appBarState);
            }
            Parcelable parcelable = this.appBarState;
            if (parcelable != null) {
                bundle.putParcelable("PostsAppBarLayout", parcelable);
            } else {
                Logger.verbose("APPBAR", "Skipped saving posts state");
            }
        }
        super.onSaveInstanceState(bundle);
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        super.onStop();
        PostsRecyclerView postsRecyclerView = this.recyclerView;
        if (postsRecyclerView != null) {
            postsRecyclerView.invalidateItemDecorations();
            this.recyclerView.setItemAnimator(null);
            this.stateBundle.putParcelable("SyncRecyclerView", this.recyclerView.onSaveInstanceState());
        }
    }

    @Override // com.resync.app.presenters.PostsView
    public void setDefaultAnimation() {
        Logger.verbose("VerticalPostsFragment", "Setting the default item animator");
        if (!isActiveAndAdded()) {
            Logger.verbose("VerticalPostsFragment", "Ignoring set enter exit animation");
        } else if (!SettingsSingleton.getSettings().animatePostHide) {
            setNoAnimation();
        } else {
            CrashlyticsHelper.logEvent(4, "item-animator", "Setting Default");
            this.recyclerView.setItemAnimator(new StockDefaultAnimation());
        }
    }

    @Override // com.resync.app.presenters.PostsView
    public void setRefreshing(final boolean refreshing) {
        Logger.verbose("VerticalPostsFragment", "Setting the default item animator with listener");
        StockDefaultAnimation stockDefaultAnimation = new StockDefaultAnimation(new OnFinishedListener() { // from class: na.a
            @Override // com.resync.app.ui.util.animation.recycler.comments.OnFinishedListener
            public final void onFinished() {
                VerticalPostsFragment.this.lambdaCheckMorePosts(refreshing);
            }
        });
        stockDefaultAnimation.setSupportsChangeAnimations(false);
        if (!SettingsSingleton.getSettings().animatePostHide) {
            stockDefaultAnimation.setAddDuration(10L);
            stockDefaultAnimation.setChangeDuration(10L);
            stockDefaultAnimation.setMoveDuration(10L);
            stockDefaultAnimation.setRemoveDuration(10L);
        }
        this.recyclerView.setItemAnimator(stockDefaultAnimation);
    }

    @Subscribe
    public void onComposeMessageEvent(OnComposeMessageEvent onComposeMessageEvent) {
        if (isShownNotHidden()) {
            EditFragment.newMessageInstance(MaterialPostsHelper.extractProfileUsername(getSubredditName())).show(getChildFragmentManager(), "EditFragment");
        }
    }

    @Subscribe
    public void onHideRead(HideReadEvent hideReadEvent) {
        this.recyclerView.scrollToTopInstant();
        getAppBar().setExpanded(true, false);
        this.recyclerView.post(new Runnable() { // from class: na.b
            @Override // java.lang.Runnable
            public final void run() {
                VerticalPostsFragment.this.lambdaMarkAllRead();
            }
        });
    }

    @Subscribe
    public void onRefreshEvent(RefreshPostsEvent refreshPostsEvent) {
        if (isAdded()) {
            onRefresh();
        }
    }

    @Subscribe
    public void onSortUpdatedEvent(SubredditSortUpdatedEvent subredditSortUpdatedEvent) {
        if (isAdded()) {
            changeAccessAndSort(subredditSortUpdatedEvent.sort, subredditSortUpdatedEvent.timePeriod);
            onProgressReady();
            updateAppBarSection();
        }
    }

    @Subscribe
    public void onStopScrollEvent(StopScrollEvent stopScrollEvent) {
        this.recyclerView.invalidateItemDecorations();
    }

    @Subscribe
    public void onSubredditSelected(final SubredditSelectedEvent subredditSelectedEvent) {
        if (MaterialPostsHelper.isOpenUrl(subredditSelectedEvent.subreddit)) {
            LinkHelper.open(getContext(), MaterialPostsHelper.extractOpenUrl(subredditSelectedEvent.subreddit));
            return;
        }
        if (subredditSelectedEvent.subreddit.equalsIgnoreCase("random") || subredditSelectedEvent.subreddit.equalsIgnoreCase("random_nsfw")) {
            FragmentUtils.showWithArguments(RandomProgressDialogBottomSheet.class, getParentFragmentManager(), RandomProgressDialogBottomSheet.buildArgs("random_nsfw".equalsIgnoreCase(subredditSelectedEvent.subreddit)));
            return;
        }
        if (getActivity() instanceof PostHost) {
            ((PostHost) getActivity()).areAnyPanelsOpen();
        }
        if (subredditSelectedEvent.openExternally) {
            getView().postDelayed(new Runnable() { // from class: na.h
                @Override // java.lang.Runnable
                public final void run() {
                    VerticalPostsFragment.this.lambdaOpenSubredditNewTask(subredditSelectedEvent);
                }
            }, 0);
        } else {
            this.recyclerView.postDelayed(new Runnable() { // from class: na.i
                @Override // java.lang.Runnable
                public final void run() {
                    VerticalPostsFragment.this.lambdaChangeSubreddit(subredditSelectedEvent);
                }
            }, 0);
        }
    }

    @Subscribe
    public void onUiModeChanged(OnUiModeChanged onUiModeChanged) {
        applyUiMode(onUiModeChanged.uiMode);
        UiSingleton.getInstance().setUiMode(onUiModeChanged.uiMode);
    }

    @Override // com.resync.app.presenters.PostsView
    public void setSubreddit(Subreddit subreddit) {
        if (getAppBar() != null) {
            getAppBar().setSubreddit(this.presenter.getUserOrSubredditInfo());
        }
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (bundle == null || !bundle.containsKey("PostsAppBarLayout")) {
            Logger.verbose("APPBAR", "Posts view state was missing: " + this.appBarState);
            return;
        }
        this.appBarState = bundle.getParcelable("PostsAppBarLayout");
        bundle.remove("PostsAppBarLayout");
        Logger.verbose("APPBAR", "Posts view state restored: " + this.appBarState);
        if (isShownNotHidden()) {
            getAppBar().restoreBehaviorState(this.appBarState);
            Logger.verbose("APPBAR", "Setting posts view state: " + this.appBarState);
        }
    }

    public void scrollToTop() {
        BusSingleton.getBus().post(new StopExoplayerEvent());
        if (getUiMode() == 8) {
            if (this.recyclerView.getLastVisiblePosition() > 3) {
                this.recyclerView.getLayoutManager().scrollToPosition(3);
            }
            this.recyclerView.scrollToPosition(0);
        } else {
            if (!this.recyclerView.canScrollVertically(-1)) {
                getAppBar().setExpanded(true, false);
                getAppBar().setExpanded(false);
                return;
            }
            this.recyclerView.invalidateItemDecorations();
            final Parcelable parcelableOnSaveInstanceState = this.recyclerView.onSaveInstanceState();
            getAppBar().setExpanded(true, false);
            showBottomNav();
            this.recyclerView.scrollToTopAnimated();
            this.recyclerView.postDelayed(new Runnable() { // from class: na.f
                @Override // java.lang.Runnable
                public final void run() {
                    VerticalPostsFragment.this.lambdaCollapseAppBar();
                }
            }, 480L);
            this.restoreSnackbar = showRestoreScrollSnackbar(new Runnable() { // from class: na.g
                @Override // java.lang.Runnable
                public final void run() {
                    VerticalPostsFragment.this.lambdaRestoreScroll(parcelableOnSaveInstanceState);
                }
            });
        }
    }

    @Subscribe
    public void refresh(RefreshEvent refreshEvent) {
        if (isAdded()) {
            getAppBar().setExpanded(true, false);
            this.recyclerView.scrollToTopInstant();
            this.recyclerView.postDelayed(new Runnable() { // from class: na.d
                @Override // java.lang.Runnable
                public final void run() {
                    VerticalPostsFragment.this.onRefresh();
                }
            }, 200L);
        }
    }

    @Subscribe
    public void scrollToBottom(ScrollToBottomOfPostsEvent scrollToBottomOfPostsEvent) {
        if (isAdded()) {
            this.recyclerView.scrollToBottom();
        }
    }

    @Subscribe
    public void scrollToTop(ScrollToTopOfPostsEvent scrollToTopOfPostsEvent) {
        if (isAdded()) {
            scrollToTop();
        }
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, com.resync.app.presenters.PostsView
    public void resetViewState() {
        super.resetViewState();
        boolean canUpdateTitle = isShownNotHidden();
        Logger.verbose("Can update title: " + canUpdateTitle);
        if (canUpdateTitle) {
            onProgressReady();
            updateAppBarSection();
        }
    }

    @Override // com.resync.app.presenters.PostsView
    public void setSwipeEnabled(boolean enabled) {
    }
}
