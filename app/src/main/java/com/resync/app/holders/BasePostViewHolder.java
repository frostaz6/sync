package com.resync.app.holders;

import android.content.Context;
import android.view.View;
import butterknife.ButterKnife;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.widgets.SimpleExoPlayerTextureView;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.model.Post;
import com.resync.app.presenters.BaseCursorPresenter;
import com.resync.app.presenters.PostsPresenter;

/* JADX INFO: renamed from: tb.a */
/* JADX INFO: compiled from: BasePostViewHolder.java */
/* JADX INFO: loaded from: classes3.dex */
public abstract class BasePostViewHolder extends RedditViewHolder {
    protected Post post;
    private int uiMode;

    private boolean scrolling;

    private boolean previewMode;
    private boolean firstPost;
    private BaseCursorPresenter presenter;

    public BasePostViewHolder(Context context, View view, int uiMode) {
        super(context, view);
        this.uiMode = uiMode;
        ButterKnife.bind(this, view);
    }

    private final boolean hasPresenter() {
        return this.presenter != null;
    }

    private void setPost(Post post) {
        this.post = post;
    }

    @Override // com.resync.app.holders.RedditViewHolder
    public void recycle() {
        super.recycle();
        if (getTextureView() != null) {
            getTextureView().stopAndDetach();
        }
        setPost(null);
    }

    public void bindPost(Post post, int position) {
        setPost(post);
    }

    public boolean canAutoplay() {
        return false;
    }

    public Post getPost() {
        return this.post;
    }

    protected final BaseCursorPresenter getPresenter() {
        return this.presenter;
    }

    protected boolean shouldShowSubreddit() {
        return (getPresenter() instanceof PostsPresenter) && !MaterialPostsHelper.isStandardSubreddit(((PostsPresenter) getPresenter()).getSubredditName());
    }

    public int getUiMode() {
        return this.uiMode;
    }

    public SimpleExoPlayerTextureView getTextureView() {
        return null;
    }

    protected final boolean canInteract() {
        return !isScrolling() && hasPresenter();
    }

    public boolean isPreviewMode() {
        return this.previewMode;
    }

    public boolean isScrolling() {
        return this.scrolling;
    }

    public boolean isFirstPost() {
        return this.firstPost;
    }

    public void setPreviewMode() {
        this.previewMode = true;
    }

    protected final void setPresenter(BaseCursorPresenter baseCursorPresenter) {
        this.presenter = baseCursorPresenter;
    }

    public void setScrolling() {
        this.scrolling = true;
    }

    public void setFirstPost() {
        this.firstPost = true;
    }

    public void attachTextureView(SimpleExoPlayerTextureView simpleExoPlayerTextureView) {
    }

    public BasePostViewHolder(Context context, View view) {
        super(context, view);
        ButterKnife.bind(this, view);
    }
}
