package com.resync.app.ui.views.text.spannable.children.posts;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.managers.TextFormatterManager;
import com.resync.app.model.Post;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class PostStatsTextView extends SpannableTextView {
    public PostStatsTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void bind(BaseCursorPresenter baseCursorPresenter, Post post, boolean previewMode) {
        setVisited(post.isVisitedOrInternal());
        TextFormatterManager.appendPostScore(baseCursorPresenter, resetBuilder(), post, previewMode);
        commitText();
    }
}
