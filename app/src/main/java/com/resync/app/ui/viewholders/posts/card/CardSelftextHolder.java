package com.resync.app.ui.viewholders.posts.card;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.views.posts.cards.CardSelftextPreviewTextView;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.holders.BaseViewHolder;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.model.Post;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class CardSelftextHolder extends AbstractCardPostHolder {

    HtmlTableView mTableView;

    CardSelftextPreviewTextView selftext;

    class SelftextLongClickListener implements View.OnLongClickListener {

        class CopyConfirmListener implements DialogInterface.OnClickListener {

            final /* synthetic */ TextView selectTextView;

            CopyConfirmListener(TextView textView) {
                this.selectTextView = textView;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                int selectionStart = this.selectTextView.getSelectionStart();
                int selectionEnd = this.selectTextView.getSelectionEnd();
                if (selectionStart == selectionEnd) {
                    StaticToast.showWithContext(CardSelftextHolder.this.context, "No text copied");
                    return;
                }
                String strSubstring = this.selectTextView.getText().toString().substring(selectionStart, selectionEnd);
                RedditHelper.copyToClipboard(CardSelftextHolder.this.context, strSubstring, false);
                StaticToast.showWithContext(CardSelftextHolder.this.context, "Text copied: " + strSubstring);
            }
        }

        SelftextLongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            View viewInflate = View.inflate(CardSelftextHolder.this.context, R.layout.dialog_copy, null);
            TextView textView = (TextView) viewInflate.findViewById(R.id.select);
            textView.setText(CardSelftextHolder.this.getPost().getSelftextRaw());
            new AlertDialog.Builder(CardSelftextHolder.this.context).setTitle("Select text...").setView(viewInflate).setPositiveButton("Copy", new CopyConfirmListener(textView)).create().show();
            return true;
        }
    }

    public CardSelftextHolder(Context context, BaseCursorPresenter baseCursorPresenter, View view, int uiMode) {
        super(context, baseCursorPresenter, view, uiMode);
        int paddingHorizontal = SizeHelper.dpToPx(10);
        int paddingVertical = SizeHelper.dpToPx(0);
        this.mTableView.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        if (SettingsSingleton.getSettings().postsLongPress) {
            this.mTableView.setOnLongClickListener(new SelftextLongClickListener());
        }
    }

    public static CardSelftextHolder create(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int uiMode) {
        if (UiSingleton.isCards(uiMode)) {
            return new CardSelftextHolder(context, baseCursorPresenter, LayoutInflater.from(context).inflate(R.layout.holder_card_selftext, viewGroup, false), uiMode);
        }
        throw new RuntimeException("This ViewHolder is not compatible with the current UI mode!");
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder
    protected void applyFullWidthLayout() {
        super.applyFullWidthLayout();
        ((LinearLayout.LayoutParams) this.mTableView.getLayoutParams()).leftMargin = SizeHelper.dpToPx(0);
        ((LinearLayout.LayoutParams) this.mTableView.getLayoutParams()).rightMargin = SizeHelper.dpToPx(0);
        this.mTableView.requestLayout();
        PaddingHelper.setPaddingHorizontal(this.selftext, 16, 16);
        PaddingHelper.setPaddingHorizontal(this.mTableView, 16, 16);
    }

    @Override // com.resync.app.holders.BasePostViewHolder, com.resync.app.holders.RedditViewHolder
    public void recycle() {
        super.recycle();
        this.selftext.setVisibility(8);
        this.mTableView.setVisibility(8);
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder, com.resync.app.holders.BasePostViewHolder
    public void bindPost(Post post, int position) {
        super.bindPost(post, position);
        if (isPreviewMode()) {
            this.mTableView.bindPost(getPost());
        } else {
            this.selftext.bindPreview(getPost());
        }
    }
}
