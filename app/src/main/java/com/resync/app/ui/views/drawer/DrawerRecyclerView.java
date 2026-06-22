package com.resync.app.ui.views.drawer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class DrawerRecyclerView extends RecyclerView implements DarkModeAware {
    @SuppressLint({"RestrictedApi"})
    public DrawerRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        removeAllViewsInLayout();
        removeAllViews();
        if (getRecycledViewPool() != null) {
            getRecycledViewPool().clear();
        }
        if (getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
    }
}
