package com.resync.app.ui.views.recycler;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.util.recycler.CustomLinearLayoutManager;
import com.resync.app.widgets.recycler.MessagingItemDecoration;
import okhttp3.internal.http.HttpStatusCodesKt;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.widgets.SyncRecyclerView;

/* JADX INFO: loaded from: classes3.dex */
public class MessagingRecyclerView extends SyncRecyclerView {
    private LinearLayoutManager layoutManager;
    private Listener listener;

    class MessagingRecycledViewPool extends RecyclerView.RecycledViewPool {
        MessagingRecycledViewPool() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.RecycledViewPool
        public void putRecycledView(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof RedditViewHolder) {
                ((RedditViewHolder) viewHolder).recycle();
            }
            super.putRecycledView(viewHolder);
        }
    }

    class ScrollListener extends RecyclerView.OnScrollListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        int lastVisiblePosition = 0;

        ScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int firstVisible = Math.max(0, MessagingRecyclerView.this.layoutManager.findFirstVisibleItemPosition());
            if (firstVisible != this.lastVisiblePosition) {
                if (MessagingRecyclerView.this.listener != null) {
                    MessagingRecyclerView.this.listener.onScrolledToPosition(firstVisible);
                }
                this.lastVisiblePosition = firstVisible;
            }
            if (!MessagingRecyclerView.this.isAtBottom() || MessagingRecyclerView.this.getAdapter() == null || MessagingRecyclerView.this.getAdapter().getItemCount() <= 0) {
                return;
            }
            if ((MessagingRecyclerView.this.getItemAnimator() == null || !(MessagingRecyclerView.this.getItemAnimator() == null || MessagingRecyclerView.this.getItemAnimator().isRunning())) && ConnectivityHelper.isConnected(MessagingRecyclerView.this.getContext()) && MessagingRecyclerView.this.listener != null) {
                MessagingRecyclerView.this.listener.onReachedBottom();
            }
        }
    }

    class RecycleListener implements RecyclerView.RecyclerListener {
        RecycleListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.RecyclerListener
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof RedditViewHolder) {
                ((RedditViewHolder) viewHolder).recycle();
            }
        }
    }

    public interface Listener {
        void onScrolledToPosition(int position);

        void onReachedBottom();
    }

    public MessagingRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupRecycler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void scrollUp(int duration) {
        smoothScrollBy(0, -SizeHelper.dpToPx(100), null, duration);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void scrollDownThenUp(final int duration) {
        scrollBy(0, SizeHelper.dpToPx(100));
        post(new Runnable() { // from class: mc.b
            @Override // java.lang.Runnable
            public final void run() {
                MessagingRecyclerView.this.scrollUp(duration);
            }
        });
    }

    private void setupRecycler() {
        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(getContext(), false);
        this.layoutManager = customLinearLayoutManager;
        setLayoutManager(customLinearLayoutManager);
        setOverScrollMode(2);
        RecyclerView.RecycledViewPool pool = new MessagingRecycledViewPool();
        pool.setMaxRecycledViews(14, 1);
        setRecycledViewPool(pool);
        setItemViewCacheSize(0);
        addItemDecoration(new MessagingItemDecoration(getContext()));
        addOnScrollListener(new ScrollListener());
        setRecyclerListener(new RecycleListener());
        setHasFixedSize(false);
        applyPadding();
    }

    public int getFirstVisiblePosition() {
        if (getLayoutManager() instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        }
        throw new RuntimeException("Unsupported manager");
    }

    public boolean isAtBottom() {
        return getChildCount() + this.layoutManager.findFirstVisibleItemPosition() >= this.layoutManager.getItemCount();
    }

    public void scrollToTopAnimated() {
        if (getFirstVisiblePosition() == 0) {
            invalidateItemDecorations();
            scrollToPosition(0);
        }
        invalidateItemDecorations();
        getLayoutManager().scrollToPosition(0);
        final int duration = HttpStatusCodesKt.HTTP_OK;
        post(new Runnable() { // from class: mc.a
            @Override // java.lang.Runnable
            public final void run() {
                MessagingRecyclerView.this.scrollDownThenUp(duration);
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void applyPadding() {
        setPadding(0, SizeHelper.dpToPx(8), 0, 0);
        setClipToPadding(false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(parcelable);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }
}
