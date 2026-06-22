package com.resync.app.widgets.recycler;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.fragment_dialogs.bottom.ActionsBottomSheetFragment;

/* JADX INFO: renamed from: lb.a */
/* JADX INFO: compiled from: ActionItemMoveCallback.java */
/* JADX INFO: loaded from: classes3.dex */
public class ActionItemMoveCallback extends ItemTouchHelper.Callback {
    private final Callback callback;

    public interface Callback {
        void onItemMove(int from, int to);
    }

    public ActionItemMoveCallback(Callback callback) {
        this.callback = callback;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder holder, int direction) {
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        return ItemTouchHelper.Callback.makeMovementFlags(15, 0);
    }

    // The action grid is reordered by long-press drag (not swipe).
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
        if (!(holder instanceof ActionsBottomSheetFragment.ActionHolder) || !(target instanceof ActionsBottomSheetFragment.ActionHolder)) {
            return false;
        }
        this.callback.onItemMove(((ActionsBottomSheetFragment.ActionHolder) holder).getActionId(), ((ActionsBottomSheetFragment.ActionHolder) target).getActionId());
        return true;
    }
}
