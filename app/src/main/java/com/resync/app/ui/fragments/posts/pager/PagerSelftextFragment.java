package com.resync.app.ui.fragments.posts.pager;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import butterknife.BindView;
import com.resync.app.R;
import com.squareup.otto.Subscribe;
import com.resync.app.events.WindowChromeChangedEvent;
import com.resync.app.util.TextUtils;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes2.dex */
public class PagerSelftextFragment extends AbstractPagerFragment {

    @BindView(R.id.fragment_swipe_selftext_wrapper)
    ScrollView mSelftextWrapper;

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_swipe_selftext;
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mSelftextWrapper.setVerticalFadingEdgeEnabled(true);
        this.mSelftextWrapper.setLayerType(2, null);
    }

    @Subscribe
    public void onChromeChanged(WindowChromeChangedEvent windowChromeChangedEvent) {
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment
    public void bindPost(Post post) {
        super.bindPost(post);
        TextUtils.isEmpty(getPost().getSelftextProcessed());
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment
    public void releaseMedia() {
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment
    public void setChromeVisible(boolean z) {
    }
}
