package com.resync.app.ui.views.text.spannable.children.posts;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.managers.TextFormatterManager;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class PostDescriptionTextView extends SpannableTextView {
    public PostDescriptionTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private final boolean shouldShowInfo(int width, Post post) {
        return true;
    }

    public final void bind(Post post, int width, boolean flagA, boolean flagB, boolean flagC) {
        TextFormatterManager.formatPostInfo(resetBuilder(), this, post, width, flagA, flagC, flagB, shouldShowInfo(width, post));
        commitText();
    }
}
