package com.resync.app.ui.viewholders.messaging;

import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.posts.simple.SimpleLinearLayout;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.ui.views.text.spannable.children.messaging.MessagingFirstRowTextView;
import com.resync.app.ui.views.text.spannable.children.messaging.MessagingSecondRowTextView;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes3.dex */
public class MessageHolder_ViewBinding implements Unbinder {
    private MessageHolder target;
    private View moreView;

    class MoreClickListener extends DebouncingOnClickListener {

        final /* synthetic */ MessageHolder boundTarget;

        MoreClickListener(MessageHolder messageHolder) {
            this.boundTarget = messageHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMoreClicked();
        }
    }

    public MessageHolder_ViewBinding(MessageHolder messageHolder, View view) {
        this.target = messageHolder;
        messageHolder.mCardView = (SimpleLinearLayout) Utils.findRequiredViewAsType(view, R.id.holder_message_card, "field 'mCardView'", SimpleLinearLayout.class);
        messageHolder.mFirstLine = (MessagingFirstRowTextView) Utils.findRequiredViewAsType(view, R.id.holder_message_first, "field 'mFirstLine'", MessagingFirstRowTextView.class);
        messageHolder.mSecondLine = (MessagingSecondRowTextView) Utils.findRequiredViewAsType(view, R.id.holder_message_second, "field 'mSecondLine'", MessagingSecondRowTextView.class);
        View viewC = Utils.findRequiredView(view, R.id.holder_message_more, "field 'mMore' and method 'onMoreClicked'");
        messageHolder.mMore = (AppCompatImageView) Utils.castView(viewC, R.id.holder_message_more, "field 'mMore'", AppCompatImageView.class);
        this.moreView = viewC;
        viewC.setOnClickListener(new MoreClickListener(messageHolder));
        messageHolder.mBody = (HtmlTextView) Utils.findRequiredViewAsType(view, R.id.holder_message_body, "field 'mBody'", HtmlTextView.class);
        messageHolder.mIcon = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.holder_message_icon, "field 'mIcon'", AppCompatImageView.class);
    }

    @Override
    public void unbind() {
        MessageHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mCardView = null;
        target.mFirstLine = null;
        target.mSecondLine = null;
        target.mMore = null;
        target.mBody = null;
        target.mIcon = null;
        this.moreView.setOnClickListener(null);
        this.moreView = null;
    }
}
