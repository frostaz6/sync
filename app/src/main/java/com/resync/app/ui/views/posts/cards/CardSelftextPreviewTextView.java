package com.resync.app.ui.views.posts.cards;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.text.html.Options;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class CardSelftextPreviewTextView extends HtmlTextView {
    public CardSelftextPreviewTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void bindPreview(Post post) {
        if (SettingsSingleton.getSettings().cardSelftextPreviews && StringUtils.isNotEmpty(post.getSelftextProcessedPreview())) {
            setMaxLines(5);
            setVisibility(0);
            setVisited(post.isVisitedOrInternal());
            setSpannableText(post.getSelftextProcessedPreview());
        }
    }

    public void bind(Post post, boolean expanded) {
        if (expanded) {
            setMaxLines(Integer.MAX_VALUE);
        } else {
            setMaxLines(5);
        }
        setVisited(post.isVisitedOrInternal());
        setHtmlWithOptions(Options.markdownOnly(), post.getSelftextProcessed());
    }
}
