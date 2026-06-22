package com.resync.app.ui.fragments;

import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.MessagingActivity;
import com.resync.app.ui.util.animation.recycler.EnterExitAnimator;
import com.resync.app.ui.util.animation.recycler.posts.StockDefaultAnimation;
import com.resync.app.ui.views.app_bars.PostsAppBarLayout;
import com.resync.app.ui.views.monet.MonetSwipeRefreshLayout;
import com.resync.app.ui.views.posts.PostsFab;
import com.resync.app.ui.views.recycler.MessagingRecyclerView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.LoadingViewHelper;
import com.resync.app.helpers.ui.SnackbarHelper;
import com.resync.app.events.MessagingSectionSelectedEvent;
import com.resync.app.events.OnUiModeChanged;
import com.resync.app.events.RefreshEvent;
import com.resync.app.events.OnBlockMessageAuthorEvent;
import com.resync.app.events.OnComposeMessageEvent;
import com.resync.app.events.OnDeleteMessageAuthorEvent;
import com.resync.app.events.OnMarkMessageViewedEvent;
import com.resync.app.events.OnMessageReplyEvent;
import com.resync.app.events.OnViewMessageAuthorEvent;
import com.resync.app.events.OnViewMessageContextEvent;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.events.RefreshPostsEvent;
import com.resync.app.events.ScrollToTopOfPostsEvent;
import com.resync.app.adapters.MessagingAdapter;
import com.resync.app.helpers.GestureNavigationHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthServiceMessageCountRequest;
import com.resync.app.presenters.MessagingPresenter;
import com.resync.app.presenters.MessagingPresenterImpl;
import com.resync.app.presenters.PresenterUtils;
import com.resync.app.presenters.MessagingView;

/* JADX INFO: loaded from: classes2.dex */
public class MessagingFragment extends RedditFragment implements MessagingView, LoadingViewHelper.RetryListener, MessagingRecyclerView.Listener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.fragment_messages_recycler)
    MessagingRecyclerView mRecyclerView;

    @BindView(R.id.fragment_messages_str)
    MonetSwipeRefreshLayout mSwipeRefreshLayout;
    protected LoadingViewHelper loadingViewHelper;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    protected MessagingPresenter presenter;
    protected MessagingAdapter adapter;

    /* JADX INFO: renamed from: u0, reason: collision with root package name */
    private Parcelable appBarState;

    class FabScrollListener extends RecyclerView.OnScrollListener {
        FabScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (SettingsSingleton.getSettings().mainFab && MessagingFragment.this.getFab() != null && SettingsSingleton.getSettings().mainFabAutohide) {
                if (dy > 0) {
                    MessagingFragment.this.getFab().hide();
                } else if (dy < 0) {
                    MessagingFragment.this.getFab().show();
                }
            }
        }
    }

    class RestoreScrollClickListener implements View.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Parcelable savedState;

        RestoreScrollClickListener(Parcelable parcelable) {
            this.savedState = parcelable;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            MessagingFragment.this.mRecyclerView.invalidateItemDecorations();
            MessagingFragment.this.mRecyclerView.onRestoreInstanceState(this.savedState);
        }
    }

    public static MessagingFragment newInstance(int mode) {
        MessagingFragment messagingFragment = new MessagingFragment();
        messagingFragment.setArguments(MessagingPresenterImpl.buildArgs(mode));
        return messagingFragment;
    }

    private void animateIn() {
        if (getView() != null) {
            getView().setTranslationY(SizeHelper.dpToPxFloat(30));
            getView().setAlpha(0.0f);
            getView().animate().alpha(1.0f).translationY(0.0f).setDuration(300L).setInterpolator(new FastOutSlowInInterpolator()).start();
        }
        getFab().configure(2, null);
        updateAppBar();
    }

    private void updateFab() {
        if (getFab() != null) {
            getFab().configure(2, null);
        }
    }

    private void updateAppBar() {
        if (getAppBar() != null) {
            getAppBar().setLiftOnScrollTargetViewId(R.id.fragment_messages_recycler);
            getAppBar().setSection(2, getModeString(), null, null);
        }
    }

    private boolean isVisibleNotHidden() {
        return !isHidden();
    }

    @Override // com.resync.app.presenters.MessagingView
    public void showStatusMessage(int messageRes) {
        this.loadingViewHelper.showError(messageRes);
    }

    public String getModeString() {
        return Integer.toString(this.presenter.getMode());
    }

    public void scrollToTop() {
        if (this.mRecyclerView.computeVerticalScrollOffset() > 0) {
            this.mRecyclerView.invalidateItemDecorations();
            Parcelable parcelableOnSaveInstanceState = this.mRecyclerView.onSaveInstanceState();
            Snackbar snackbarA = SnackbarHelper.make(getCoordinator(), R.string.fragment_posts_restore_scroll, -1);
            snackbarA.setAction(R.string.common_restore, new RestoreScrollClickListener(parcelableOnSaveInstanceState));
            snackbarA.show();
        }
        getAppBar().setExpanded(true, false);
        this.mRecyclerView.scrollToTopAnimated();
    }

    public void setPresenter(MessagingPresenter messagingPresenter) {
        this.presenter = messagingPresenter;
        messagingPresenter.restoreArguments(getArguments());
    }

    public void changeSection(int mode) {
        this.loadingViewHelper.dismiss();
        setMode(mode);
        getFab().configure(2, null);
        updateAppBar();
    }

    public void setupViews() {
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        if (isVisibleNotHidden() || (getActivity() instanceof MessagingActivity)) {
            updateFab();
            updateAppBar();
        }
        this.loadingViewHelper = new LoadingViewHelper(getActivity(), this.mSwipeRefreshLayout, this);
        this.mRecyclerView.setListener(this);
        MessagingAdapter messagingAdapter = new MessagingAdapter(getActivity(), this.presenter);
        this.adapter = messagingAdapter;
        this.mRecyclerView.setAdapter(messagingAdapter);
        this.mRecyclerView.addOnScrollListener(new FabScrollListener());
        GestureNavigationHelper.applyGestureNavPadding(getActivity(), this.mRecyclerView);
        GestureNavigationHelper.applyGestureNavPadding(getActivity(), this.mRecyclerView);
    }

    public void setMode(int mode) {
        this.presenter.setMode(getArguments(), mode);
    }

    @Override // com.resync.app.ui.views.recycler.MessagingRecyclerView.Listener
    public void onScrolledToPosition(int position) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (PresenterUtils.shouldAttachPresenter()) {
            setPresenter(new MessagingPresenterImpl(getActivity(), bundle, this));
        }
        if (bundle == null) {
            ((NotificationManager) getActivity().getSystemService("notification")).cancel(1);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        super.onDestroy();
        MessagingPresenter messagingPresenter = this.presenter;
        if (messagingPresenter != null) {
            messagingPresenter.onDestroy();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            this.loadingViewHelper.dismiss();
        }
        if (getAppBar() != null) {
            if (hidden) {
                this.appBarState = getAppBar().saveBehaviorState();
                Logger.verbose("APPBAR", "Saving messages state: " + this.appBarState);
            } else {
                Logger.verbose("APPBAR", "Resuming messages state: " + this.appBarState);
                getAppBar().restoreBehaviorState(this.appBarState);
            }
        }
        if (hidden) {
            return;
        }
        animateIn();
    }

    @Override // com.resync.app.presenters.MessagingView
    public void setEnterExitAnimation() {
        Logger.verbose("MessagingFragment", "Setting enter exit animation");
        this.mRecyclerView.setItemAnimator(new EnterExitAnimator());
    }

    @Override // com.resync.app.presenters.MessagingView
    public void notifyDataChanged() {
        this.mRecyclerView.invalidateItemDecorations();
        this.adapter.notifyDataSetChanged();
    }

    @Override // com.resync.app.ui.views.recycler.MessagingRecyclerView.Listener
    public void onReachedBottom() {
        this.presenter.load(false);
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
    public void onRefresh() {
        this.presenter.refresh();
    }

    @Override // com.resync.app.presenters.MessagingView
    public void setRefreshing(boolean refreshing) {
        this.loadingViewHelper.setRefreshing(refreshing && this.presenter.getMessageCount() == 0);
        if (getProgressView() != null) {
            getProgressView().setVisibility((!refreshing || this.presenter.getMessageCount() <= 0) ? 8 : 0);
        }
    }

    @Override // com.resync.app.helpers.ui.LoadingViewHelper.RetryListener
    public void onRetryAction() {
        this.presenter.recover();
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_messages;
    }

    @Override // com.resync.app.presenters.BaseViewInterface
    public void showError(VolleyError volleyError) {
        this.loadingViewHelper.showError(volleyError);
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        MessagingPresenter messagingPresenter = this.presenter;
        if (messagingPresenter != null) {
            messagingPresenter.saveState(bundle);
        }
        if (getAppBar() != null) {
            if (isVisibleNotHidden()) {
                this.appBarState = getAppBar().saveBehaviorState();
                Logger.verbose("APPBAR", "Saving live messaging state: " + this.appBarState);
            } else {
                Logger.verbose("APPBAR", "Saving cached messaging state: " + this.appBarState);
            }
            Parcelable parcelable = this.appBarState;
            if (parcelable != null) {
                bundle.putParcelable("PostsAppBarLayout", parcelable);
            } else {
                Logger.verbose("APPBAR", "Skipped saving messaging state");
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        MessagingPresenter messagingPresenter = this.presenter;
        if (messagingPresenter != null) {
            messagingPresenter.onStart();
        }
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        super.onStop();
        MessagingPresenter messagingPresenter = this.presenter;
        if (messagingPresenter != null) {
            messagingPresenter.onStop();
        }
    }

    public void setDefaultAnimation() {
        Logger.verbose("MessagingFragment", "Setting default animation");
        this.mRecyclerView.setItemAnimator(new StockDefaultAnimation());
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setupViews();
        if (this.presenter != null) {
            if (bundle == null) {
                Logger.verbose("MessagingFragment", "Requesting new data be loaded");
                this.presenter.load(true);
            } else {
                Logger.verbose("MessagingFragment", "Restoring posts");
                notifyDataChanged();
            }
        }
        OAuthRequestManager.send(getContext(), new OAuthServiceMessageCountRequest(getContext(), null, null));
    }

    @Subscribe
    public void onBlockMessageAuthorEvent(OnBlockMessageAuthorEvent onBlockMessageAuthorEvent) {
        this.presenter.blockAuthor(onBlockMessageAuthorEvent.message);
    }

    @Subscribe
    public void onComposeMessageEvent(OnComposeMessageEvent onComposeMessageEvent) {
        this.presenter.composeMessage();
    }

    @Subscribe
    public void onDeleteMessageAuthorEvent(OnDeleteMessageAuthorEvent onDeleteMessageAuthorEvent) {
        this.presenter.deleteMessage(onDeleteMessageAuthorEvent.message);
    }

    @Subscribe
    public void onMarkMessageViewedEvent(OnMarkMessageViewedEvent onMarkMessageViewedEvent) {
        this.presenter.markViewed(onMarkMessageViewedEvent.message);
    }

    @Subscribe
    public void onMessageReplyEvent(OnMessageReplyEvent onMessageReplyEvent) {
        this.presenter.replyToMessage(onMessageReplyEvent.message);
    }

    @Subscribe
    public void onRefreshEvent(RefreshPostsEvent refreshPostsEvent) {
        if (isAdded()) {
            onRefresh();
        }
    }

    @Subscribe
    public void onSectionSelected(MessagingSectionSelectedEvent messagingSectionSelectedEvent) {
        if (isAdded()) {
            changeSection(messagingSectionSelectedEvent.getSection());
        }
    }

    @Subscribe
    public void onUiModeChanged(OnUiModeChanged onUiModeChanged) {
        setDefaultAnimation();
        int firstVisiblePosition = this.mRecyclerView.getFirstVisiblePosition();
        for (int i = 0; i < this.mRecyclerView.getChildCount(); i++) {
            MessagingRecyclerView messagingRecyclerView = this.mRecyclerView;
            RecyclerView.ViewHolder viewHolder = messagingRecyclerView.getChildViewHolder(messagingRecyclerView.getChildAt(i));
            if (viewHolder instanceof RedditViewHolder) {
                ((RedditViewHolder) viewHolder).recycle();
            }
        }
        this.mRecyclerView.removeAllViewsInLayout();
        this.mRecyclerView.removeAllViews();
        this.mRecyclerView.getRecycledViewPool().clear();
        this.mRecyclerView.applyPadding();
        this.mRecyclerView.getAdapter().notifyDataSetChanged();
        this.mRecyclerView.smoothScrollToPosition(firstVisiblePosition);
    }

    @Subscribe
    public void onViewMessageAuthorEvent(OnViewMessageAuthorEvent onViewMessageAuthorEvent) {
        this.presenter.viewAuthor(onViewMessageAuthorEvent.message);
    }

    @Subscribe
    public void onViewMessageContext(OnViewMessageContextEvent onViewMessageContextEvent) {
        this.presenter.viewContext(onViewMessageContextEvent.message);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (bundle == null || !bundle.containsKey("PostsAppBarLayout")) {
            Logger.verbose("APPBAR", "Messaging view state was missing: " + this.appBarState);
            return;
        }
        this.appBarState = bundle.getParcelable("PostsAppBarLayout");
        bundle.remove("PostsAppBarLayout");
        Logger.verbose("APPBAR", "Messaging view state restored: " + this.appBarState);
        if (isVisibleNotHidden()) {
            getAppBar().restoreBehaviorState(this.appBarState);
            Logger.verbose("APPBAR", "Setting messages view state: " + this.appBarState);
        }
    }

    @Subscribe
    public void scrollToTop(ScrollToTopOfPostsEvent scrollToTopOfPostsEvent) {
        if (isAdded()) {
            scrollToTop();
        }
    }

    protected PostsAppBarLayout getAppBar() {
        if (getActivity() == null || getActivity().findViewById(R.id.appbar) == null) {
            return null;
        }
        return (PostsAppBarLayout) getActivity().findViewById(R.id.appbar);
    }

    protected PostsFab getFab() {
        if (getActivity() == null || getActivity().findViewById(R.id.fab) == null) {
            return null;
        }
        return (PostsFab) getActivity().findViewById(R.id.fab);
    }

    protected CoordinatorLayout getCoordinator() {
        return (CoordinatorLayout) getActivity().findViewById(R.id.coordinator);
    }

    protected View getProgressView() {
        if (getView() == null) {
            return null;
        }
        if (!getView().isShown() && !(getActivity() instanceof MessagingActivity)) {
            return null;
        }
        Logger.verbose("VIEW: " + getActivity());
        if (getActivity() == null) {
            return null;
        }
        View viewFindViewById = getActivity().findViewById(R.id.progress);
        Logger.verbose("VIEW: " + viewFindViewById);
        return viewFindViewById;
    }

    @Subscribe
    public void onRefreshEvent(RefreshEvent refreshEvent) {
        onRefresh();
    }
}
