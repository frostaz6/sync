package com.resync.app.ui.viewholders.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.views.posts.simple.SimpleLinearLayout;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.ui.views.text.spannable.children.messaging.MessagingFirstRowTextView;
import com.resync.app.ui.views.text.spannable.children.messaging.MessagingSecondRowTextView;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.model.Message;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.MessageSelectionDialogBottomSheet;
import com.resync.app.presenters.MessagingPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class MessageHolder extends RedditViewHolder {
    MessagingPresenter presenter;
    Message message;

    HtmlTextView mBody;

    public SimpleLinearLayout mCardView;

    MessagingFirstRowTextView mFirstLine;

    AppCompatImageView mIcon;

    AppCompatImageView mMore;

    MessagingSecondRowTextView mSecondLine;

    class CardClickListener implements View.OnClickListener {
        CardClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            MessageHolder messageHolder = MessageHolder.this;
            messageHolder.presenter.onMessageClicked(messageHolder.getMessage());
        }
    }

    class CardLongClickListener implements View.OnLongClickListener {
        CardLongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            MessageHolder.this.onMoreClicked();
            return false;
        }
    }

    private MessageHolder(Context context, MessagingPresenter messagingPresenter, View view) {
        super(context, view);
        ButterKnife.bind(this, view);
        this.presenter = messagingPresenter;
        this.mIcon.setEnabled(false);
        this.mCardView.setOnClickListener(new CardClickListener());
        this.mCardView.setOnLongClickListener(new CardLongClickListener());
    }

    public static MessageHolder create(Context context, ViewGroup viewGroup, MessagingPresenter messagingPresenter) {
        return new MessageHolder(context, messagingPresenter, LayoutInflater.from(context).inflate(R.layout.holder_message_slide, viewGroup, false));
    }

    public void bind(Message message) {
        this.mCardView.setPosition(getLayoutPosition());
        if (message.depth > 0) {
            this.mCardView.enableRoundedCorners();
        } else {
            this.mCardView.disableRoundedCorners();
        }
        this.message = message;
        this.mFirstLine.bind(message);
        this.mSecondLine.setUnread(message.isNew);
        this.mSecondLine.bind(message);
        this.mBody.setHtml(message.bodyProcessed);
        this.mMore.setFocusable(false);
        if (message.subject.equalsIgnoreCase("comment reply")) {
            this.mIcon.setImageResource(R.drawable.outline_reply);
        } else if (message.subject.equalsIgnoreCase("post reply")) {
            this.mIcon.setImageResource(R.drawable.outline_post_24);
        } else if (message.subject.equalsIgnoreCase("username mention")) {
            this.mIcon.setImageResource(R.drawable.outline_account_circle_24);
        } else {
            this.mIcon.setImageResource(R.drawable.bottom_nav_outline_mail_outline_24);
        }
        this.mIcon.setColorFilter(ColorFilterProvider.getColorFilter(StyleSingleton.getTextColorAuto(this.context, false)));
        if (message.isNew) {
            this.mCardView.setAlpha(1.0f);
        } else {
            this.mCardView.setAlpha(0.5f);
        }
    }

    public Message getMessage() {
        return this.message;
    }

    public void onMoreClicked() {
        FragmentUtils.showWithArguments(MessageSelectionDialogBottomSheet.class, getFragmentManager(), MessageSelectionDialogBottomSheet.buildArguments(getMessage()));
    }
}
