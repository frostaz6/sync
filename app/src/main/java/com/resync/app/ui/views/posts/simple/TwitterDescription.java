package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import com.resync.app.R;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.text.spans.VectorImageSpan;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class TwitterDescription extends HtmlTextView {
    public TwitterDescription(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void bind(Post post) {
        SpannableTextViewStringBuilder spannableTextViewStringBuilderA = resetBuilder();
        spannableTextViewStringBuilderA.appendWithSpans("Twitter", new VectorImageSpan((int) (getLineHeight() * 0.8f), R.drawable.twitter, 1));
        spannableTextViewStringBuilderA.append(StringUtils.SPACE);
        spannableTextViewStringBuilderA.appendWithSpans(post.getEmbeddedAuthor(), new StyleSpan(1));
        spannableTextViewStringBuilderA.append(StringUtils.SPACE);
        spannableTextViewStringBuilderA.append(post.getEmbeddedAuthorHandle());
        commitText();
    }
}
