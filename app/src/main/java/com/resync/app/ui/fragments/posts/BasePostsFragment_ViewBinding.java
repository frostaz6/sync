package com.resync.app.ui.fragments.posts;

import android.view.View;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes2.dex */
public class BasePostsFragment_ViewBinding implements Unbinder {
    private BasePostsFragment target;

    public BasePostsFragment_ViewBinding(BasePostsFragment basePostsFragment, View view) {
        this.target = basePostsFragment;
        basePostsFragment.mSwipeRefreshLayout = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.swipeRefresh, "field 'mSwipeRefreshLayout'", SwipeRefreshLayout.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        BasePostsFragment basePostsFragment = this.target;
        if (basePostsFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        basePostsFragment.mSwipeRefreshLayout = null;
    }
}
