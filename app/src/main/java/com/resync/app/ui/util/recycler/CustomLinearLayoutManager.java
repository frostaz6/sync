package com.resync.app.ui.util.recycler;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.internal.http.HttpStatusCodesKt;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean extraLayoutSpaceEnabled;

    public CustomLinearLayoutManager(Context context, boolean extraLayoutSpaceEnabled) {
        super(context, 1, false);
        this.extraLayoutSpaceEnabled = extraLayoutSpaceEnabled;
        setInitialPrefetchItemCount(3);
        setRecycleChildrenOnDetach(true);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean isAutoMeasureEnabled() {
        return false;
    }

    public int getFirstVisiblePosition() {
        return findFirstVisibleItemPosition();
    }

    public boolean isBottomReached(int itemCount) {
        return findLastVisibleItemPosition() >= itemCount - 1;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            CrashlyticsHelper.logException(e);
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        return this.extraLayoutSpaceEnabled ? super.getExtraLayoutSpace(state) + ((int) SizeHelper.dpToPxFloat(HttpStatusCodesKt.HTTP_MULT_CHOICE)) : super.getExtraLayoutSpace(state);
    }

    public CustomLinearLayoutManager(Context context) {
        super(context, 1, false);
        this.extraLayoutSpaceEnabled = false;
        setInitialPrefetchItemCount(3);
        setRecycleChildrenOnDetach(true);
    }
}
