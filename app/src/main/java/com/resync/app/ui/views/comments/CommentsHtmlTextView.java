package com.resync.app.ui.views.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.text.html.Options;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class CommentsHtmlTextView extends HtmlTextView {
    public CommentsHtmlTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setHtml(Post post, String str) {
        Options options = new Options();
        options.withWidth((((WindowUtils.getDisplayWidth() - ((View) getParent()).getPaddingLeft()) - getPaddingLeft()) - ((View) getParent()).getPaddingRight()) - getPaddingRight());
        options.withPost(post);
        if (StringUtils.equalsAnyIgnoreCase(post.getAuthor(), "savevideo", "savevideobot")) {
            options.withWidth(0);
        }
        if (StringUtils.isNotEmpty(str)) {
            setHtmlWithHighlight(options, post.getBodyProcessed(), str);
        } else {
            setHtmlWithOptions(options, post.getBodyProcessed());
        }
    }
}
