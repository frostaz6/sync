package com.resync.app.ui.fragments.sidebar;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;

/* JADX INFO: loaded from: classes2.dex */
public class FriendsAboutFragment_ViewBinding implements Unbinder {
    private FriendsAboutFragment target;

    public FriendsAboutFragment_ViewBinding(FriendsAboutFragment friendsAboutFragment, View view) {
        this.target = friendsAboutFragment;
        friendsAboutFragment.mHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.header, "field 'mHeader'", SheetHeaderView.class);
        friendsAboutFragment.mRecycler = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recycler, "field 'mRecycler'", RecyclerView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        FriendsAboutFragment friendsAboutFragment = this.target;
        if (friendsAboutFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        friendsAboutFragment.mHeader = null;
        friendsAboutFragment.mRecycler = null;
    }
}
