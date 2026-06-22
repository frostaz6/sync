package com.resync.app.ui.viewholders.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.helpers.WindowHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.presenters.CommentsPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class CommentFooterHolder extends RedditViewHolder {
    CommentsPresenter presenter;

    TextView mTextView;

    public CommentFooterHolder(Context context, View view, CommentsPresenter commentsPresenter) {
        super(context, view);
        this.presenter = commentsPresenter;
        ButterKnife.bind(this, view);
    }

    public static CommentFooterHolder create(Context context, ViewGroup viewGroup, CommentsPresenter commentsPresenter) {
        return new CommentFooterHolder(context, LayoutInflater.from(context).inflate(R.layout.holder_comments_footer, viewGroup, false), commentsPresenter);
    }

    public void bind(CommentsPresenter commentsPresenter) {
        this.mTextView.setText(this.presenter.getEmptyMessage());
        this.mTextView.setTextColor(MonetUtil.getLinkColorAuto());
        this.mTextView.setTextSize(1, 14.0f);
        this.mTextView.setVisibility(StringUtils.isNotEmpty(this.presenter.getEmptyMessage()) ? 0 : 8);
        CommentsPresenter commentsPresenter2 = this.presenter;
        if (commentsPresenter2 != null && StringUtils.isNotEmpty(commentsPresenter2.getCommentId())) {
            this.itemView.setPadding(0, 0, 0, (int) (WindowUtils.getDisplayHeight() * 0.66f));
        } else {
            View view = this.itemView;
            view.setPadding(0, 0, 0, WindowHelper.getActionBarHeight(view.getContext()));
        }
    }
}
