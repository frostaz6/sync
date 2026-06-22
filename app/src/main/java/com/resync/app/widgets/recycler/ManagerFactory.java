package com.resync.app.widgets.recycler;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.util.recycler.CustomHorizontalLinearLayoutManager;
import com.resync.app.ui.util.recycler.CustomLinearLayoutManager;
import com.resync.app.ui.util.recycler.CustomStaggeredGridLayoutManager;

/* JADX INFO: renamed from: lb.f */
/* JADX INFO: compiled from: ManagerFactory.java */
/* JADX INFO: loaded from: classes3.dex */
public final class ManagerFactory {
    public static RecyclerView.LayoutManager createLayoutManager(Context context, int viewType) {
        UiSingleton.getInstance(context);
        int columnCount = UiSingleton.getColumnCount(viewType, false);
        UiSingleton.getInstance(context);
        return viewType == 8 ? new CustomHorizontalLinearLayoutManager(context) : (columnCount == 1 && columnCount == UiSingleton.getColumnCount(viewType, true)) ? new CustomLinearLayoutManager(context, true) : new CustomStaggeredGridLayoutManager(context, viewType);
    }
}
