package com.resync.app.widgets.recycler;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.fragment_dialogs.bottom.CommentActionsBottomSheetFragment;

/* JADX INFO: renamed from: lb.b */
/* JADX INFO: compiled from: CommentActionItemMoveCallback.java */
/* JADX INFO: loaded from: classes3.dex */
public class CommentActionItemMoveCallback extends ItemTouchHelper.Callback {
    private final Callback callback;

    public interface Callback {
        void onItemMove(int from, int to);
    }

    public CommentActionItemMoveCallback(Callback callback) {
        this.callback = callback;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder holder, int direction) {
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        return ItemTouchHelper.Callback.makeMovementFlags(15, 0);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder holder, RecyclerView.ViewHolder target) {
        if (!(holder instanceof CommentActionsBottomSheetFragment.CommentActionHolder) || !(target instanceof CommentActionsBottomSheetFragment.CommentActionHolder)) {
            return false;
        }
        this.callback.onItemMove(((CommentActionsBottomSheetFragment.CommentActionHolder) holder).getActionId(), ((CommentActionsBottomSheetFragment.CommentActionHolder) target).getActionId());
        return true;
    }
}
