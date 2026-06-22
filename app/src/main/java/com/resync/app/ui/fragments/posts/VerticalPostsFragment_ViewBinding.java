package com.resync.app.ui.fragments.posts;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.recycler.PostsRecyclerView;

/* JADX INFO: loaded from: classes2.dex */
public class VerticalPostsFragment_ViewBinding extends BasePostsFragment_ViewBinding {
    private VerticalPostsFragment target;

    public VerticalPostsFragment_ViewBinding(VerticalPostsFragment verticalPostsFragment, View view) {
        super(verticalPostsFragment, view);
        this.target = verticalPostsFragment;
        verticalPostsFragment.recyclerView = (PostsRecyclerView) Utils.findRequiredViewAsType(view, R.id.recyclerView, "field 'recyclerView'", PostsRecyclerView.class);
    }
}
