package com.resync.app.ui.util.recycler;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

/* JADX INFO: loaded from: classes3.dex */
public class CustomHorizontalLinearLayoutManager extends LinearLayoutManager {
    public CustomHorizontalLinearLayoutManager(Context context) {
        super(context, 0, false);
    }

    public int getFirstVisiblePosition() {
        return findFirstVisibleItemPosition();
    }

    public boolean isBottomReached(int itemCount) {
        return findLastVisibleItemPosition() >= itemCount - 1;
    }
}
