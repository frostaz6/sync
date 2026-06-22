package com.resync.app.ui.fragments.posts.pager;

import android.view.View;
import android.widget.ScrollView;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes2.dex */
public class PagerSelftextFragment_ViewBinding extends AbstractPagerFragment_ViewBinding {
    private PagerSelftextFragment target;

    public PagerSelftextFragment_ViewBinding(PagerSelftextFragment pagerSelftextFragment, View view) {
        super(pagerSelftextFragment, view);
        this.target = pagerSelftextFragment;
        pagerSelftextFragment.mSelftextWrapper = (ScrollView) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_selftext_wrapper, "field 'mSelftextWrapper'", ScrollView.class);
    }
}
