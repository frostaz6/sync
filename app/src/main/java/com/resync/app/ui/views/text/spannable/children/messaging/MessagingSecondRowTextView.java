package com.resync.app.ui.views.text.spannable.children.messaging;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.model.Message;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.managers.TextFormatterManager;

/* JADX INFO: loaded from: classes3.dex */
public class MessagingSecondRowTextView extends SpannableTextView {
    public MessagingSecondRowTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final void bind(Message message) {
        TextFormatterManager.formatMessageHeader(resetBuilder(), message);
        commitText();
    }
}
