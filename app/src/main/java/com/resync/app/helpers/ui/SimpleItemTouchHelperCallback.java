package com.resync.app.helpers.ui;

import android.content.Context;
import android.graphics.Canvas;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.media.images.SyncTypeHelper;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.StopExoplayerEvent;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.presenters.PostsPresenter;

/* JADX INFO: renamed from: gb.n */
/* JADX INFO: compiled from: SimpleItemTouchHelperCallback.java */
/* JADX INFO: loaded from: classes2.dex */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private Context context;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private PostsPresenter postsPresenter;

    public SimpleItemTouchHelperCallback(Context context, PostsPresenter postsPresenter) {
        this.context = context;
        this.postsPresenter = postsPresenter;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        BusSingleton.getBus().post(new StopExoplayerEvent());
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder instanceof BasePostViewHolder) {
            this.postsPresenter.hidePost(((BasePostViewHolder) viewHolder).getPost());
        }
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(1.0f);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.75f;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (!(viewHolder instanceof BasePostViewHolder)) {
            return 0;
        }
        BasePostViewHolder basePostViewHolder = (BasePostViewHolder) viewHolder;
        if (!SyncTypeHelper.supportsSwipeActions(basePostViewHolder.getPost().getSyncType()) || basePostViewHolder.getPost().isHidden()) {
            return 0;
        }
        return ItemTouchHelper.Callback.makeMovementFlags(3, 16);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public float getSwipeEscapeVelocity(float defaultValue) {
        return defaultValue * 5.0f;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.4f;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public float getSwipeVelocityThreshold(float defaultValue) {
        return defaultValue * 0.8f;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isLongPressDragEnabled() {
        if (ConnectivityHelper.isConnected(this.context)) {
            return AccountSingleton.getInstance().isLoggedIn();
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float deltaX, float deltaY, int actionState, boolean isCurrentlyActive) {
        if (actionState != 1) {
            super.onChildDraw(canvas, recyclerView, viewHolder, deltaX, deltaY, actionState, isCurrentlyActive);
            return;
        }
        viewHolder.itemView.setAlpha(1.0f - (Math.abs(deltaX) / viewHolder.itemView.getWidth()));
        viewHolder.itemView.setTranslationX(deltaX);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }
}
