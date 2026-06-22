package com.resync.app.holders;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: renamed from: pb.e */
/* JADX INFO: compiled from: CommentsSearhHeaderSection.java */
/* JADX INFO: loaded from: classes3.dex */
public class CommentsSearhHeaderSection extends RecyclerView.Adapter<CommentsSearhHeaderSection.SpacerViewHolder> {
    int height;

    /* JADX INFO: renamed from: pb.e$a */
    /* JADX INFO: compiled from: CommentsSearhHeaderSection.java */
    class SpacerViewHolder extends RecyclerView.ViewHolder {
        public SpacerViewHolder(View view) {
            super(view);
        }
    }

    public CommentsSearhHeaderSection(int height) {
        setHasStableIds(true);
        this.height = height;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(SpacerViewHolder holder, int position) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
    public SpacerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = new View(viewGroup.getContext());
        view.setLayoutParams(new RecyclerView.LayoutParams(-1, this.height));
        return new SpacerViewHolder(view);
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
