package com.resync.app.widgets;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: renamed from: yb.a */
/* JADX INFO: compiled from: SyncRecyclerView.java */
/* JADX INFO: loaded from: classes3.dex */
public abstract class SyncRecyclerView extends RecyclerView implements DarkModeAware {
    public SyncRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    protected void init() {
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        Parcelable parcelableOnSaveInstanceState = onSaveInstanceState();
        // setAdapter(null) already detaches the old adapter and calls RecyclerView's
        // internal removeAndRecycleViews(), so every visible row is torn down and returned
        // to the pool. (The original APK reflected into the R8-minified name "j1" to call
        // removeAndRecycleViews() explicitly; that is redundant here.)
        setAdapter(null);
        getRecycledViewPool().clear();
        removeAllViews();
        onRestoreInstanceState(parcelableOnSaveInstanceState);
    }
}
