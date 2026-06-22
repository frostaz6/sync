package com.resync.app.widgets.recycler;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.viewholders.comments.CommentHolder;
import com.resync.app.holders.CommentMoreHolder;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: lb.c */
/* JADX INFO: compiled from: CommentsDividerItemDecorator.java */
/* JADX INFO: loaded from: classes3.dex */
public class CommentsDividerItemDecorator extends RecyclerView.ItemDecoration {
    private void setRectInDp(Rect rect, int left, int top, int right, int bottom) {
        rect.set(SizeHelper.dpToPx(left), SizeHelper.dpToPx(top), SizeHelper.dpToPx(right), SizeHelper.dpToPx(bottom));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
        if ((holder instanceof CommentHolder) && SettingsSingleton.getSettings().commentsTopLevelPadding) {
            if (((CommentHolder) holder).getPost().getLevel() != 0 || holder.getLayoutPosition() <= 1) {
                return;
            }
            setRectInDp(rect, 0, 8, 0, 0);
            return;
        }
        if ((holder instanceof CommentMoreHolder) && SettingsSingleton.getSettings().commentsTopLevelPadding && ((CommentMoreHolder) holder).getPost().getLevel() == 0 && holder.getLayoutPosition() > 1) {
            setRectInDp(rect, 0, 8, 0, 0);
        }
    }
}
