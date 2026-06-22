package com.resync.app.widgets;

import android.os.Bundle;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.ui.fragments.posts.pager.PagerImageFragment;
import com.resync.app.ui.fragments.posts.pager.PagerSelftextFragment;
import com.resync.app.model.Post;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: renamed from: oa.a */
/* JADX INFO: compiled from: PagerFactory.java */
/* JADX INFO: loaded from: classes2.dex */
public final class PagerFactory {
    public static <T extends RedditFragment> T create(BaseCursorPresenter baseCursorPresenter, Post post, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        bundle.putBoolean("CHROME_VISIBLE", z);
        if (post.getSyncType() == 2 || post.getSyncType() == 5) {
            PagerSelftextFragment pagerSelftextFragment = new PagerSelftextFragment();
            pagerSelftextFragment.setArguments(bundle);
            return (T) pagerSelftextFragment;
        }
        PagerImageFragment pagerImageFragment = new PagerImageFragment();
        pagerImageFragment.setArguments(bundle);
        return (T) pagerImageFragment;
    }
}
