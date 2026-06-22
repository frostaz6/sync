package com.resync.app.adapters;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.views.responsive.CustomSubtleDividerView;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: eb.c */
/* JADX INFO: compiled from: HeaderDividerSection.java */
/* JADX INFO: loaded from: classes.dex */
public class HeaderDividerSection extends RecyclerView.Adapter<HeaderDividerSection.DividerViewHolder> {
    int topMargin;

    /* JADX INFO: renamed from: eb.c$a */
    /* JADX INFO: compiled from: HeaderDividerSection.java */
    class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View view) {
            super(view);
        }
    }

    public HeaderDividerSection(int topMargin) {
        setHasStableIds(true);
        this.topMargin = topMargin;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(DividerViewHolder holder, int position) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
    public DividerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        CustomSubtleDividerView customSubtleDividerView = new CustomSubtleDividerView(viewGroup.getContext());
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(-1, SizeHelper.dpToPx(1));
        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = this.topMargin;
        customSubtleDividerView.setLayoutParams(layoutParams);
        return new DividerViewHolder(customSubtleDividerView);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: n */
    public int getItemCount() {
        return 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: o */
    public long getItemId(int position) {
        return 0L;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return 0;
    }
}
