package com.resync.app.ui.views.text.spannable.children.comments;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.managers.TextFormatterManager;
import com.resync.app.model.Post;
import com.resync.app.presenters.CommentsPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class CommentDescriptionTextView extends SpannableTextView {
    public CommentDescriptionTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final void bind(Post post, CommentsPresenter commentsPresenter, boolean showSubreddit) {
        SpannableTextViewStringBuilder builder = resetBuilder();
        if (commentsPresenter == null || commentsPresenter.isSearch()) {
            TextFormatterManager.formatCommentInfo(builder, this, null, post, showSubreddit);
        } else {
            TextFormatterManager.formatCommentInfo(builder, this, commentsPresenter.getPost(), post, showSubreddit);
        }
        commitText();
    }
}
