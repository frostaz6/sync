package com.resync.app.ui.fragments.posts.pager;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import androidx.core.util.Pair;
import butterknife.BindView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.ui.views.text.BaseTextView;
import com.squareup.otto.Subscribe;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.events.WindowChromeChangedEvent;
import com.resync.app.net.thirdparty.ReadableWebsiteRequest;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes2.dex */
public class PagerLinkFragment extends AbstractPagerFragment {

    @BindView(R.id.fragment_swipe_article_body)
    BaseTextView mArticleBody;

    @BindView(R.id.fragment_swipe_article_title)
    BaseTextView mArticleTitle;

    @BindView(R.id.fragment_swipe_article_wrapper)
    ScrollView mArticleWrapper;

    class ArticleResponseListener implements Response.Listener<Pair<String, String>> {
        ArticleResponseListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Pair<String, String> pair) {
            PagerLinkFragment.this.mArticleTitle.setText(pair.first);
            PagerLinkFragment.this.mArticleBody.setText(pair.second);
            PagerLinkFragment.this.mArticleTitle.setAlpha(0.0f);
            PagerLinkFragment.this.mArticleTitle.animate().alpha(1.0f);
            PagerLinkFragment.this.mArticleBody.setAlpha(0.0f);
            PagerLinkFragment.this.mArticleBody.animate().alpha(1.0f);
        }
    }

    class ArticleErrorListener implements Response.ErrorListener {
        ArticleErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
        }
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_swipe_link;
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mArticleWrapper.setVerticalFadingEdgeEnabled(true);
        this.mArticleWrapper.setLayerType(2, null);
    }

    @Subscribe
    public void onChromeChanged(WindowChromeChangedEvent windowChromeChangedEvent) {
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment
    public void bindPost(Post post) {
        super.bindPost(post);
        OAuthRequestManager.enqueueAux(new ReadableWebsiteRequest(getPost().getUrl(), new ArticleResponseListener(), new ArticleErrorListener()));
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment
    public void releaseMedia() {
        throw new RuntimeException("TODO");
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment
    public void setChromeVisible(boolean z) {
    }
}
