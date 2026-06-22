package com.resync.app.widgets.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.R;
import com.resync.app.ui.viewholders.messaging.MessageHolder;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: lb.g */
/* JADX INFO: compiled from: MessagingItemDecoration.java */
/* JADX INFO: loaded from: classes3.dex */
public class MessagingItemDecoration extends RecyclerView.ItemDecoration {

    private int padding;

    public MessagingItemDecoration(Context context) {
        this.padding = context.getResources().getDimensionPixelSize(R.dimen.posts_fragment_list_full_padding);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        RecyclerView.ViewHolder c0VarJ0 = recyclerView.getChildViewHolder(view);
        if (c0VarJ0 instanceof MessageHolder) {
            rect.bottom = SizeHelper.dpToPx(8);
            rect.left = this.padding * ((MessageHolder) c0VarJ0).getMessage().depth;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDraw(canvas, recyclerView, state);
    }
}
