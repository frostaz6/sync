package com.resync.app.ui.fragments.sidebar;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;

/* JADX INFO: loaded from: classes2.dex */
public class MultiAboutFragment_ViewBinding implements Unbinder {
    private MultiAboutFragment target;

    public MultiAboutFragment_ViewBinding(MultiAboutFragment multiAboutFragment, View view) {
        this.target = multiAboutFragment;
        multiAboutFragment.mHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.header, "field 'mHeader'", SheetHeaderView.class);
        multiAboutFragment.mRecycler = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recycler, "field 'mRecycler'", RecyclerView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        MultiAboutFragment multiAboutFragment = this.target;
        if (multiAboutFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        multiAboutFragment.mHeader = null;
        multiAboutFragment.mRecycler = null;
    }
}
