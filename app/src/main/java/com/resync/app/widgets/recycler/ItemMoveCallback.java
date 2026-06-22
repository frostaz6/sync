package com.resync.app.widgets.recycler;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.viewholders.sections.subreddits.SubredditHolder;

/* JADX INFO: renamed from: lb.e */
/* JADX INFO: compiled from: ItemMoveCallback.java */
/* JADX INFO: loaded from: classes3.dex */
public class ItemMoveCallback extends ItemTouchHelper.Callback {
    private final Callback callback;

    public interface Callback {
        void onSelected(RecyclerView.ViewHolder holder);

        void onCleared(RecyclerView.ViewHolder holder);

        void onMove(String str, String str2);
    }

    public ItemMoveCallback(Callback callback) {
        this.callback = callback;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSelectedChanged(RecyclerView.ViewHolder holder, int actionState) {
        if (actionState != 0) {
            this.callback.onSelected(holder);
        }
        super.onSelectedChanged(holder, actionState);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder holder, int direction) {
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        super.clearView(recyclerView, holder);
        this.callback.onCleared(holder);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        if ((holder instanceof SubredditHolder) && ((SubredditHolder) holder).getMode() == 1) {
            return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
        }
        return 0;
    }

    // Favorites are reordered by long-press drag (not swipe). See issue #2.
    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder holder, RecyclerView.ViewHolder target) {
        if (!(holder instanceof SubredditHolder) || !(target instanceof SubredditHolder)) {
            return false;
        }
        SubredditHolder subredditHolder = (SubredditHolder) holder;
        if (subredditHolder.getMode() != 1) {
            return false;
        }
        SubredditHolder subredditHolder2 = (SubredditHolder) target;
        if (subredditHolder2.getMode() != 1) {
            return false;
        }
        this.callback.onMove(subredditHolder.getSubreddit(), subredditHolder2.getSubreddit());
        return true;
    }
}
