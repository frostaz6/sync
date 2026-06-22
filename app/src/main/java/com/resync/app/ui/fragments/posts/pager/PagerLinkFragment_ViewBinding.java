package com.resync.app.ui.fragments.posts.pager;

import android.view.View;
import android.widget.ScrollView;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.text.BaseTextView;

/* JADX INFO: loaded from: classes2.dex */
public class PagerLinkFragment_ViewBinding extends AbstractPagerFragment_ViewBinding {
    private PagerLinkFragment target;

    public PagerLinkFragment_ViewBinding(PagerLinkFragment pagerLinkFragment, View view) {
        super(pagerLinkFragment, view);
        this.target = pagerLinkFragment;
        pagerLinkFragment.mArticleWrapper = (ScrollView) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_article_wrapper, "field 'mArticleWrapper'", ScrollView.class);
        pagerLinkFragment.mArticleTitle = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_article_title, "field 'mArticleTitle'", BaseTextView.class);
        pagerLinkFragment.mArticleBody = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_article_body, "field 'mArticleBody'", BaseTextView.class);
    }
}
