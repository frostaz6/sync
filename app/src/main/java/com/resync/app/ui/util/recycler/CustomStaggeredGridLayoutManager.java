package com.resync.app.ui.util.recycler;

import android.content.Context;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.resync.app.R;
import com.resync.app.singleton.UiSingleton;

/* JADX INFO: loaded from: classes3.dex */
public class CustomStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    private int[] firstVisiblePositions;
    private int savedSpan;

    public CustomStaggeredGridLayoutManager(Context context, int requestedSpan) {
        super(UiSingleton.getColumnCount(requestedSpan, context.getResources().getBoolean(com.resync.app.R.bool.landscape)), 1);
        int spanCount = UiSingleton.getColumnCount(requestedSpan, context.getResources().getBoolean(com.resync.app.R.bool.landscape));
        this.savedSpan = spanCount;
        this.firstVisiblePositions = new int[spanCount];
    }

    public int getFirstVisiblePosition() {
        findFirstVisibleItemPositions(this.firstVisiblePositions);
        return this.firstVisiblePositions[0];
    }

    public boolean isBottomReached(int position) {
        int childCount = getChildCount();
        int itemCount = getItemCount();
        int firstVisiblePosition = getFirstVisiblePosition();
        return (firstVisiblePosition == 0 || itemCount == 0 || childCount == 0 || childCount + firstVisiblePosition < itemCount) ? false : true;
    }
}
