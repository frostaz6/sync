package com.resync.app.ui.views.text.spannable.children.posts;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.managers.TextFormatterManager;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class PostTitleTextView extends SpannableTextView {
    public PostTitleTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void bind(Post post) {
        setVisited(post.isVisitedOrInternal());
        if (SettingsSingleton.getSettings().stickyHighlight) {
            setLinkColored(post.isStickied());
        }
        TextFormatterManager.formatPostTitle(resetBuilder(), this, post);
        commitText();
    }
}
