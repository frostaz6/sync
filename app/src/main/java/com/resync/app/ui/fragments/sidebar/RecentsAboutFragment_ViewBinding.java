package com.resync.app.ui.fragments.sidebar;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;

/* JADX INFO: loaded from: classes2.dex */
public class RecentsAboutFragment_ViewBinding implements Unbinder {
    private RecentsAboutFragment target;

    public RecentsAboutFragment_ViewBinding(RecentsAboutFragment recentsAboutFragment, View view) {
        this.target = recentsAboutFragment;
        recentsAboutFragment.mHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.header, "field 'mHeader'", SheetHeaderView.class);
        recentsAboutFragment.mRecycler = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recycler, "field 'mRecycler'", RecyclerView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        RecentsAboutFragment recentsAboutFragment = this.target;
        if (recentsAboutFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        recentsAboutFragment.mHeader = null;
        recentsAboutFragment.mRecycler = null;
    }
}
