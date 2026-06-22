package com.resync.app.adapters;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.viewholders.drawer.DrawerHeaderHolder;

/* JADX INFO: renamed from: eb.b */
/* JADX INFO: compiled from: DrawerHeaderAdapter.java */
/* JADX INFO: loaded from: classes.dex */
public class DrawerHeaderAdapter extends RecyclerView.Adapter<DrawerHeaderHolder> {
    public DrawerHeaderAdapter() {
        setHasStableIds(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(DrawerHeaderHolder drawerHeaderHolder, int position) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
    public DrawerHeaderHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return DrawerHeaderHolder.create(viewGroup.getContext(), viewGroup);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: n */
    public int getItemCount() {
        return 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return 12;
    }
}
