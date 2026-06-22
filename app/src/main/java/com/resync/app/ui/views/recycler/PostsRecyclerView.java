package com.resync.app.ui.views.recycler;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.util.recycler.CustomHorizontalLinearLayoutManager;
import com.resync.app.ui.util.recycler.CustomLinearLayoutManager;
import com.resync.app.ui.util.recycler.CustomStaggeredGridLayoutManager;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.PostActionsTouchHelperCallback;
import com.resync.app.helpers.ui.SimpleItemTouchHelperCallback;
import com.resync.app.widgets.recycler.CustomDividerItemDecorator;
import com.resync.app.widgets.recycler.ManagerFactory;
import com.resync.app.widgets.SimpleExoPlayerTextureView;
import okhttp3.internal.http.HttpStatusCodesKt;
import com.resync.app.ads.AdmobNativeAdAdapter;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.StopExoplayerEvent;
import com.resync.app.premium.SyncIapHelper;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.widgets.CustomPagerSnapHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.PostsHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.presenters.PostsPresenter;
import com.resync.app.widgets.SyncRecyclerView;

/* JADX INFO: loaded from: classes3.dex */
public class PostsRecyclerView extends SyncRecyclerView {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private AdmobNativeAdAdapter adAdapter;
    CustomDividerItemDecorator dividerDecorator;

    /* JADX INFO: renamed from: c1, reason: collision with root package name */
    CustomPagerSnapHelper snapHelper;
    SimpleExoPlayerTextureView videoView;

    /* JADX INFO: renamed from: e1, reason: collision with root package name */
    ItemTouchHelper itemTouchHelper;

    /* JADX INFO: renamed from: f1, reason: collision with root package name */
    int mode;

    class AttachDragRunnable implements Runnable {
        AttachDragRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PostsRecyclerView postsRecyclerView = PostsRecyclerView.this;
            postsRecyclerView.itemTouchHelper.attachToRecyclerView(postsRecyclerView);
        }
    }

    class DetachDragRunnable implements Runnable {
        DetachDragRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PostsRecyclerView.this.itemTouchHelper.attachToRecyclerView(null);
        }
    }

    class PostRecycledViewPool extends RecyclerView.RecycledViewPool {
        PostRecycledViewPool() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.RecycledViewPool
        public void putRecycledView(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof RedditViewHolder) {
                Logger.verbose("Recycking: " + viewHolder);
                ((RedditViewHolder) viewHolder).recycle();
            }
            super.putRecycledView(viewHolder);
        }
    }

    class MarkViewedRecyclerListener implements RecyclerView.RecyclerListener {
        MarkViewedRecyclerListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.RecyclerListener
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            Logger.verbose("Marking as viewed attempting: " + viewHolder.getClass());
            if (viewHolder instanceof BasePostViewHolder) {
                int lastVisible = PostsRecyclerView.this.getLastVisiblePosition();
                Post post = ((BasePostViewHolder) viewHolder).getPost();
                if (post == null || post.isVisitedOrInternal() || viewHolder.getPosition() >= lastVisible) {
                    return;
                }
                PostsHelper.markViewed(PostsRecyclerView.this.getContext(), post);
                Logger.verbose("Marking as viewed: " + post.getTitle());
            }
        }
    }

    public PostsRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void scrollUp(int duration) {
        smoothScrollBy(0, -SizeHelper.dpToPx(100), null, duration);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void scrollDownThenUp(final int duration) {
        scrollBy(0, SizeHelper.dpToPx(100));
        post(new Runnable() { // from class: mc.c
            @Override // java.lang.Runnable
            public final void run() {
                PostsRecyclerView.this.scrollUp(duration);
            }
        });
    }

    private void stopVideo() {
        SimpleExoPlayerTextureView simpleExoPlayerTextureView = this.videoView;
        if (simpleExoPlayerTextureView != null) {
            simpleExoPlayerTextureView.stopAndDetach();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        Logger.verbose("EE-DEBUG", "Setting animator: " + animator);
        endAnimations();
        super.setItemAnimator(animator);
    }

    public void addDrag() {
        if (this.itemTouchHelper != null) {
            Logger.verbose("EE-DEBUG", "Adding drag");
            invalidateItemDecorations();
            post(new AttachDragRunnable());
        }
    }

    public void endAnimations() {
        if (getItemAnimator() == null || !getItemAnimator().isRunning()) {
            return;
        }
        getItemAnimator().endAnimations();
        Logger.verbose("EE-DEBUG", "Ending animations");
    }

    public void removeDrag() {
        if (this.itemTouchHelper != null) {
            Logger.verbose("EE-DEBUG", "Removing drag");
            invalidateItemDecorations();
            post(new DetachDragRunnable());
        }
    }

    public int getLastVisiblePosition() {
        RecyclerView.LayoutManager layoutManager = this.layoutManager;
        if (layoutManager instanceof CustomStaggeredGridLayoutManager) {
            return ((CustomStaggeredGridLayoutManager) layoutManager).getFirstVisiblePosition();
        }
        if (layoutManager instanceof CustomLinearLayoutManager) {
            return ((CustomLinearLayoutManager) layoutManager).getFirstVisiblePosition();
        }
        if (layoutManager instanceof CustomHorizontalLinearLayoutManager) {
            return ((CustomHorizontalLinearLayoutManager) layoutManager).getFirstVisiblePosition();
        }
        throw new RuntimeException("Unsupported manager");
    }

    public boolean isAtBottom() {
        RecyclerView.LayoutManager layoutManager = this.layoutManager;
        if (layoutManager instanceof CustomStaggeredGridLayoutManager) {
            return ((CustomStaggeredGridLayoutManager) layoutManager).isBottomReached(getItemCount());
        }
        if (layoutManager instanceof CustomLinearLayoutManager) {
            return ((CustomLinearLayoutManager) layoutManager).isBottomReached(getItemCount());
        }
        if (layoutManager instanceof CustomHorizontalLinearLayoutManager) {
            return ((CustomHorizontalLinearLayoutManager) layoutManager).isBottomReached(getItemCount());
        }
        throw new RuntimeException("Unsupported manager");
    }

    public int getItemCount() {
        RecyclerView.Adapter adapter = this.adapter;
        if (adapter != null) {
            return adapter.getItemCount();
        }
        return 0;
    }

    public void pause() {
        AdmobNativeAdAdapter admobNativeAdAdapter = this.adAdapter;
        if (admobNativeAdAdapter != null) {
            admobNativeAdAdapter.onPause();
        }
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            try {
                RecyclerView.ViewHolder viewHolder = getChildViewHolder(getChildAt(childIndex));
                if (viewHolder instanceof BasePostViewHolder) {
                    ((BasePostViewHolder) viewHolder).recycle();
                }
            } catch (Exception unused) {
                return;
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

    @Override // android.view.View
    protected void onAnimationEnd() {
        super.onAnimationEnd();
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
    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        if (motionEvent != null && motionEvent.getAction() == 8 && motionEvent.getSource() == 8194) {
            refreshVideo();
        }
        return super.onGenericMotionEvent(motionEvent);
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

    public void refreshVideo() {
        Logger.debug("Refreshing children");
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

    public void resume() {
        AdmobNativeAdAdapter admobNativeAdAdapter = this.adAdapter;
        if (admobNativeAdAdapter != null) {
            admobNativeAdAdapter.onResume();
        }
    }

    public void scrollToBottom() {
        BusSingleton.getBus().post(new StopExoplayerEvent());
        getLayoutManager().scrollToPosition(this.adapter.getItemCount() - 1);
    }

    public int scrollToTopAnimated() {
        BusSingleton.getBus().post(new StopExoplayerEvent());
        if (getLastVisiblePosition() == 0) {
            invalidateItemDecorations();
            scrollToPosition(0);
            return 0;
        }
        invalidateItemDecorations();
        getLayoutManager().scrollToPosition(0);
        final int duration = HttpStatusCodesKt.HTTP_OK;
        post(new Runnable() { // from class: mc.d
            @Override // java.lang.Runnable
            public final void run() {
                PostsRecyclerView.this.scrollDownThenUp(duration);
            }
        });
        return HttpStatusCodesKt.HTTP_OK;
    }

    public int scrollToTopInstant() {
        BusSingleton.getBus().post(new StopExoplayerEvent());
        invalidateItemDecorations();
        getLayoutManager().scrollToPosition(0);
        return 0;
    }

    public void setup(Activity activity, RecyclerView.Adapter adapter, int mode) {
        this.mode = mode;
        applyPadding(mode);
        CustomDividerItemDecorator customDividerItemDecorator = new CustomDividerItemDecorator(getContext(), mode);
        this.dividerDecorator = customDividerItemDecorator;
        addItemDecoration(customDividerItemDecorator);
        this.adapter = adapter;
        super.setAdapter(adapter); // ads removed: always use the plain posts adapter (adAdapter stays null)
        setOverScrollMode(2);
        RecyclerView.RecycledViewPool pool = new PostRecycledViewPool();
        if (mode == 4) {
            pool.setMaxRecycledViews(1, 5);
            pool.setMaxRecycledViews(2, 5);
            pool.setMaxRecycledViews(3, 5);
            pool.setMaxRecycledViews(11, 5);
        } else {
            pool.setMaxRecycledViews(1, 10);
            pool.setMaxRecycledViews(11, 10);
        }
        setHasFixedSize(false);
        setRecycledViewPool(pool);
        if (SettingsSingleton.getSettings().markAsReadOnScroll) {
            setRecyclerListener(new MarkViewedRecyclerListener());
        }
        applyMode(mode);
    }

    public void setupSwipe(PostsPresenter postsPresenter) {
        if (SettingsSingleton.getSettings().swipeHidePosts) {
            new ItemTouchHelper(new SimpleItemTouchHelperCallback(getContext(), postsPresenter)).attachToRecyclerView(this);
        } else if (SettingsSingleton.getSettings().postSwipeActions) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new PostActionsTouchHelperCallback(getContext(), postsPresenter));
            this.itemTouchHelper = itemTouchHelper;
            itemTouchHelper.attachToRecyclerView(this);
        }
    }

    public void applyMode(int mode) {
        stopVideo();
        applyPadding(mode);
        this.mode = mode;
        CustomDividerItemDecorator customDividerItemDecorator = this.dividerDecorator;
        if (customDividerItemDecorator != null) {
            customDividerItemDecorator.setMode(mode);
        }
        AdmobNativeAdAdapter admobNativeAdAdapter = this.adAdapter;
        if (admobNativeAdAdapter != null) {
            admobNativeAdAdapter.setViewType(mode);
        }
        RecyclerView.LayoutManager layoutManager = ManagerFactory.createLayoutManager(getContext(), mode);
        this.layoutManager = layoutManager;
        setLayoutManager(layoutManager);
    }

    void applyPadding(int mode) {
        PaddingHelper.clearPadding(this);
        CustomPagerSnapHelper customPagerSnapHelper = this.snapHelper;
        if (customPagerSnapHelper != null) {
            customPagerSnapHelper.attachToRecyclerView(null);
            this.snapHelper = null;
        }
        if (mode == 5) {
            PaddingHelper.setPaddingTop(this, 8);
            return;
        }
        if (mode == 8) {
            CustomPagerSnapHelper customPagerSnapHelper2 = new CustomPagerSnapHelper();
            this.snapHelper = customPagerSnapHelper2;
            customPagerSnapHelper2.attachToRecyclerView(this);
            return;
        }
        if (mode == 4) {
            if (SettingsSingleton.getSettings().toolbarExpanded) {
                if (UiSingleton.isFullWidth(mode)) {
                    PaddingHelper.setPaddingBottom(this, 8);
                    return;
                } else {
                    PaddingHelper.setPaddingLeftRightBottom(this, 8, 8, 8);
                    return;
                }
            }
            if (UiSingleton.isFullWidth(mode)) {
                PaddingHelper.setPaddingTop(this, 8);
                return;
            } else {
                PaddingHelper.setPaddingAll(this, 8);
                return;
            }
        }
        if (mode == 2) {
            if (SettingsSingleton.getSettings().toolbarExpanded) {
                return;
            }
            PaddingHelper.setPaddingTopBottom(this, 8, 8);
        } else {
            if (mode == 3) {
                if (SettingsSingleton.getSettings().toolbarExpanded) {
                    PaddingHelper.setPaddingLeftRightBottom(this, 8, 8, 8);
                    return;
                } else {
                    PaddingHelper.setPaddingAll(this, 8);
                    return;
                }
            }
            if (mode == 0 || mode == 1) {
                if (SettingsSingleton.getSettings().toolbarExpanded) {
                    PaddingHelper.setPaddingTop(this, 8);
                } else {
                    PaddingHelper.setPaddingTop(this, 1);
                }
            }
        }
    }
}
