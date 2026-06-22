package com.resync.app.ui.viewholders.posts.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.resync.app.R;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.fragment_dialogs.ImagePeekDialogFragment;
import com.resync.app.ui.views.posts.cards.CardSelftextPreviewTextView;
import com.resync.app.ui.views.posts.simple.PostThumbnailView;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.model.Post;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class CardLinkHolder extends AbstractCardPostHolder {

    CardSelftextPreviewTextView mSelftext;

    PostThumbnailView thumbnail;

    private CardLinkHolder(Context context, BaseCursorPresenter baseCursorPresenter, View view, int uiMode) {
        super(context, baseCursorPresenter, view, uiMode);
    }

    public static CardLinkHolder create(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int uiMode) {
        if (UiSingleton.isCards(uiMode)) {
            return new CardLinkHolder(context, baseCursorPresenter, LayoutInflater.from(context).inflate(R.layout.holder_card_link, viewGroup, false), uiMode);
        }
        throw new IllegalArgumentException("This ViewHolder is not compatible with the current UI mode!");
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder
    protected void applyFullWidthLayout() {
        super.applyFullWidthLayout();
        ((RelativeLayout.LayoutParams) this.thumbnail.getLayoutParams()).leftMargin = SizeHelper.dpToPx(16);
        ((RelativeLayout.LayoutParams) this.thumbnail.getLayoutParams()).rightMargin = SizeHelper.dpToPx(16);
        PaddingHelper.setPaddingHorizontal(this.mSelftext, 16, 16);
    }

    @Override // com.resync.app.holders.BasePostViewHolder, com.resync.app.holders.RedditViewHolder
    public void recycle() {
        super.recycle();
        this.thumbnail.setVisibility(8);
        this.mSelftext.setVisibility(8);
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder, com.resync.app.holders.BasePostViewHolder
    public void bindPost(Post post, int position) {
        super.bindPost(post, position);
        this.thumbnail.bind(post);
        if (StringUtils.isNotEmpty(getPost().getSelftextProcessed())) {
            this.mSelftext.setVisibility(0);
            this.mSelftext.bind(getPost(), isPreviewMode());
        }
    }

    protected boolean onImageLongClicked() {
        ImagePeekDialogFragment.show(this.context, getPost());
        return true;
    }

    protected void onLinkClicked() {
        getPresenter().openLink(null, getPost());
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder, com.resync.app.holders.BasePostViewHolder
    public void setPreviewMode() {
        super.setPreviewMode();
        this.mSelftext.setVisitedDimDisabled(true);
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder, com.resync.app.holders.BasePostViewHolder
    public void setScrolling() {
        super.setScrolling();
        this.thumbnail.setEnabled(false);
        this.mSelftext.setVisitedDimDisabled(true);
    }
}
