package com.resync.app.ui.viewholders.comments;

import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class CommentFooterHolder_ViewBinding implements Unbinder {
    private CommentFooterHolder target;

    public CommentFooterHolder_ViewBinding(CommentFooterHolder commentFooterHolder, View view) {
        this.target = commentFooterHolder;
        commentFooterHolder.mTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.holder_comments_footer_text, "field 'mTextView'", TextView.class);
    }

    @Override
    public void unbind() {
        CommentFooterHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mTextView = null;
    }
}
