package com.resync.app.holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.resync.app.R;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.views.responsive.LoadingTextView;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.model.Post;
import com.resync.app.presenters.CommentsPresenter;

/* JADX INFO: renamed from: pb.d */
/* JADX INFO: compiled from: CommentMoreHolder.java */
/* JADX INFO: loaded from: classes3.dex */
public class CommentMoreHolder extends BasePostViewHolder {

    LoadingTextView loadingTextView;
    CommentsPresenter presenter;

    private CommentMoreHolder(Context context, CommentsPresenter commentsPresenter, View view) {
        super(context, view, UiSingleton.getInstance().getUiMode());
        LoadingTextView loadingTextView = (LoadingTextView) view;
        this.loadingTextView = loadingTextView;
        this.presenter = commentsPresenter;
        loadingTextView.setOnClickListener(new View.OnClickListener() { // from class: pb.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                CommentMoreHolder.this.onMoreClicked(view2);
            }
        });
    }

    public static CommentMoreHolder create(Context context, ViewGroup viewGroup, CommentsPresenter commentsPresenter) {
        return new CommentMoreHolder(context, commentsPresenter, LayoutInflater.from(context).inflate(R.layout.holder_comment_more, viewGroup, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMoreClicked(View view) {
        if (this.loadingTextView.isEnabled()) {
            if (getPost().getCommentMoreCount() == 0) {
                this.presenter.continueThread(getPost());
            } else {
                this.presenter.loadMoreComments(getPost());
                bind(getPost());
            }
        }
    }

    public void bind(Post post) {
        super.bindPost(post, 0);
        if (post == null) {
            return;
        }
        LoadingTextView loadingTextView = this.loadingTextView;
        int layoutPosition = getLayoutPosition();
        int level = getPost().getLevel();
        CommentsPresenter commentsPresenter = this.presenter;
        loadingTextView.configure(layoutPosition, level, false, true, (commentsPresenter == null || commentsPresenter.getCursor() == null || this.presenter.getCursor().isClosed() || getLayoutPosition() != this.presenter.getCursor().getCount() - 1) ? false : true);
        this.loadingTextView.stop();
        if (this.presenter.isLoadingMoreFor(getPost())) {
            this.loadingTextView.setLoading(true);
            this.loadingTextView.setEnabled(false);
            return;
        }
        if (getPost().getCommentMoreCount() == 0) {
            this.loadingTextView.setText("Continue thread...");
        } else {
            this.loadingTextView.setText("View more (" + getPost().getCommentMoreCount() + ")");
            if (getPost().getLevel() == 0 && ConnectivityHelper.isConnected()) {
                onMoreClicked(this.loadingTextView);
            }
        }
        this.loadingTextView.setEnabled(true);
    }

    @Override // com.resync.app.holders.BasePostViewHolder, com.resync.app.holders.RedditViewHolder
    public synchronized void recycle() {
        super.recycle();
        this.loadingTextView.stop();
        this.loadingTextView.setLoading(false);
        this.loadingTextView.setEnabled(true);
    }
}
