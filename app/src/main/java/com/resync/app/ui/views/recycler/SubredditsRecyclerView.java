package com.resync.app.ui.views.recycler;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes3.dex */
public class SubredditsRecyclerView extends RecyclerView {
    public int scrollOffset;

    class ScrollListener extends RecyclerView.OnScrollListener {
        ScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            SubredditsRecyclerView.this.scrollOffset += dy;
        }
    }

    public SubredditsRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.scrollOffset = 0;
        addOnScrollListener(new ScrollListener());
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
