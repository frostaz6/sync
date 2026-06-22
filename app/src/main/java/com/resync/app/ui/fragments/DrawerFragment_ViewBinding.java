package com.resync.app.ui.fragments;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.drawer.DrawerRecyclerView;
import com.resync.app.ui.views.monet.MonetFab;

/* JADX INFO: loaded from: classes2.dex */
public class DrawerFragment_ViewBinding implements Unbinder {
    private DrawerFragment target;

    public DrawerFragment_ViewBinding(DrawerFragment drawerFragment, View view) {
        this.target = drawerFragment;
        drawerFragment.mRecyclerView = (DrawerRecyclerView) Utils.findRequiredViewAsType(view, R.id.fragment_drawer_recyclerview, "field 'mRecyclerView'", DrawerRecyclerView.class);
        drawerFragment.mFab = (MonetFab) Utils.findRequiredViewAsType(view, R.id.fragment_drawer_fab, "field 'mFab'", MonetFab.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        DrawerFragment drawerFragment = this.target;
        if (drawerFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        drawerFragment.mRecyclerView = null;
        drawerFragment.mFab = null;
    }
}
