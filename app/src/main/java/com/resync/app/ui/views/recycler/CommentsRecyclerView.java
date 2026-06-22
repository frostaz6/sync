package com.resync.app.ui.views.recycler;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.util.recycler.CustomLinearLayoutManager;
import com.resync.app.ui.views.comments.CommentsCoordinatorLayout;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.ReplyTouchHelperCallback;
import com.resync.app.widgets.recycler.CommentsDividerItemDecorator;
import com.resync.app.widgets.SimpleExoPlayerTextureView;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.FlingExoplayerEvent;
import com.resync.app.events.StopExoplayerEvent;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.holders.CommentMoreHolder;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.util.Logger;
import com.resync.app.presenters.CommentsPresenter;
import com.resync.app.widgets.SyncRecyclerView;

/* JADX INFO: loaded from: classes3.dex */
public class CommentsRecyclerView extends SyncRecyclerView {
    private CommentsPresenter presenter;
    private AppBarLayout appBar;
    private MaterialYouToolbar toolbar;
    private CommentsCoordinatorLayout coordinator;

    /* JADX INFO: renamed from: c1, reason: collision with root package name */
    private LinearLayoutManager layoutManager;
    private RecyclerView.RecycledViewPool recycledViewPool;

    /* JADX INFO: renamed from: e1, reason: collision with root package name */
    SimpleExoPlayerTextureView videoView;

    class ScrollListener extends RecyclerView.OnScrollListener {
        ScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (1 == newState) {
                CommentsRecyclerView.this.coordinator.setScrollEnabled(true);
            }
            if (newState == 2) {
                BusSingleton.getBus().post(new FlingExoplayerEvent());
            } else {
                CommentsRecyclerView.this.refreshVideo();
            }
        }
    }

    class CommentRecycledViewPool extends RecyclerView.RecycledViewPool {
        CommentRecycledViewPool() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.RecycledViewPool
        public void putRecycledView(RecyclerView.ViewHolder viewHolder) {
            if ((viewHolder instanceof CommentMoreHolder) && CommentsRecyclerView.this.presenter != null) {
                CommentsRecyclerView.this.presenter.cancelMore(((CommentMoreHolder) viewHolder).getPost());
            }
            if (viewHolder instanceof RedditViewHolder) {
                ((RedditViewHolder) viewHolder).recycle();
            }
            super.putRecycledView(viewHolder);
        }
    }

    class TopSnapScroller extends LinearSmoothScroller {
        TopSnapScroller(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        protected int getVerticalSnapPreference() {
            return -1;
        }
    }

    public CommentsRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupRecycler();
        setupSwipeActions();
    }

    private void setupRecycler() {
        setOverScrollMode(2);
        WindowUtils.getDisplayHeight();
        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(getContext());
        this.layoutManager = customLinearLayoutManager;
        setLayoutManager(customLinearLayoutManager);
        addOnScrollListener(new ScrollListener());
        CommentRecycledViewPool recycledViewPool = new CommentRecycledViewPool();
        this.recycledViewPool = recycledViewPool;
        recycledViewPool.setMaxRecycledViews(11, 50);
        this.recycledViewPool.setMaxRecycledViews(12, 50);
        setRecycledViewPool(this.recycledViewPool);
        setHasFixedSize(false);
    }

    private void stopVideo() {
        SimpleExoPlayerTextureView simpleExoPlayerTextureView = this.videoView;
        if (simpleExoPlayerTextureView != null) {
            simpleExoPlayerTextureView.stopAndDetach();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void scrollToPosition(int position) {
        scrollToPositionAnimated(position, SettingsSingleton.getSettings().animateCommentsNavigation);
    }

    public int findNavigationPosition() {
        int childBottom;
        int[] iArr = new int[2];
        Logger.verbose("CommentNavigation", "TOOLBAR HEIGHT: " + this.toolbar.getHeight());
        int bottom = this.toolbar.getBottom();
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            getChildAt(childIndex).getLocationOnScreen(iArr);
            int screenY = iArr[1];
            int height = getChildAt(childIndex).getHeight();
            Logger.verbose("CommentNavigation", "Y: " + screenY);
            RecyclerView.ViewHolder viewHolder = getChildViewHolder(getChildAt(childIndex));
            if (screenY >= bottom) {
                Logger.verbose("CommentNavigation", "WAS AFTER: " + screenY + " -- " + bottom + "  --  " + viewHolder.getLayoutPosition());
                return viewHolder.getLayoutPosition();
            }
            if (screenY < bottom && (childBottom = screenY + height) > bottom) {
                Logger.verbose("CommentNavigation", "WAS BEFORE: " + childBottom + " -- " + bottom + "  --  " + viewHolder.getLayoutPosition());
                return viewHolder.getLayoutPosition();
            }
        }
        return -1;
    }

    public int getNavigationPosition() {
        return findNavigationPosition();
    }

    public void scrollToPositionInstant(int position) {
        this.layoutManager.scrollToPosition(position);
    }

    public void teardown() {
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            RecyclerView.ViewHolder viewHolder = getChildViewHolder(getChildAt(childIndex));
            if (viewHolder instanceof RedditViewHolder) {
                ((RedditViewHolder) viewHolder).recycle();
            }
        }
        try {
            setAdapter(null);
        } catch (Exception e) {
            Logger.logException(e);
        }
        SimpleExoPlayerTextureView simpleExoPlayerTextureView = this.videoView;
        if (simpleExoPlayerTextureView != null) {
            simpleExoPlayerTextureView.release();
            this.videoView = null;
        }
    }

    public void releaseVideo() {
        SimpleExoPlayerTextureView simpleExoPlayerTextureView = this.videoView;
        if (simpleExoPlayerTextureView != null) {
            simpleExoPlayerTextureView.stopAndDetach();
            this.videoView.release();
            this.videoView = null;
        }
    }

    public void createVideoView() {
        if (SettingsSingleton.getSettings().autoplay == 2) {
            return;
        }
        this.videoView = new SimpleExoPlayerTextureView(getContext());
    }

    public void onResume() {
    }

    public void refreshVideo() {
        if (isShown() && ContextHelper.getBaseActivity(getContext()).hasWindowFocus() && this.videoView != null) {
            boolean shouldStop = false;
            if (getItemAnimator() == null || !getItemAnimator().isRunning()) {
                for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
                    RecyclerView.ViewHolder viewHolder = getChildViewHolder(getChildAt(childIndex));
                    if (viewHolder instanceof BasePostViewHolder) {
                        BasePostViewHolder basePostViewHolder = (BasePostViewHolder) viewHolder;
                        if (basePostViewHolder.canAutoplay()) {
                            basePostViewHolder.attachTextureView(this.videoView);
                            break;
                        }
                    }
                }
                shouldStop = true;
            } else {
                shouldStop = true;
            }
            if (shouldStop) {
                stopVideo();
            }
        }
    }

    public void scrollToTop() {
        if (getNavigationPosition() <= 10) {
            scrollToPosition(0);
            return;
        }
        invalidateItemDecorations();
        getLayoutManager().scrollToPosition(10);
        scrollToPosition(0);
    }

    public void setPresenter(CommentsPresenter commentsPresenter) {
        this.presenter = commentsPresenter;
    }

    public void bindToolbar(CommentsCoordinatorLayout commentsCoordinatorLayout, AppBarLayout appBarLayout, MaterialYouToolbar materialYouToolbar) {
        this.coordinator = commentsCoordinatorLayout;
        this.appBar = appBarLayout;
        this.toolbar = materialYouToolbar;
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        BusSingleton.getBus().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(parcelable);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Subscribe
    public void onStopExoplayerEvent(StopExoplayerEvent stopExoplayerEvent) {
        stopVideo();
    }

    public void setupSwipeActions() {
        if (SettingsSingleton.getSettings().commentSwipeActions) {
            new ItemTouchHelper(new ReplyTouchHelperCallback(getContext())).attachToRecyclerView(this);
        }
    }

    public void scrollToPositionAnimated(int position, boolean animate) {
        invalidateItemDecorations();
        this.appBar.setExpanded(true, false);
        this.appBar.setExpanded(true);
        this.coordinator.setScrollEnabled(false);
        if (!animate) {
            this.layoutManager.scrollToPositionWithOffset(Math.min(position, getAdapter() != null ? getAdapter().getItemCount() - 1 : position), 0);
            return;
        }
        TopSnapScroller scroller = new TopSnapScroller(getContext());
        scroller.setTargetPosition(position);
        this.layoutManager.startSmoothScroll(scroller);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setAdapter(RecyclerView.Adapter adapter) {
        addItemDecoration(new CommentsDividerItemDecorator());
        super.setAdapter(adapter);
    }
}
