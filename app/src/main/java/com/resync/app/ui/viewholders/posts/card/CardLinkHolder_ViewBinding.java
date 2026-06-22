package com.resync.app.ui.viewholders.posts.card;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.posts.cards.CardSelftextPreviewTextView;
import com.resync.app.ui.views.posts.simple.PostThumbnailView;

/* JADX INFO: loaded from: classes3.dex */
public class CardLinkHolder_ViewBinding extends AbstractCardPostHolder_ViewBinding {

    private CardLinkHolder target;

    private View thumbnailView;

    class LinkClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CardLinkHolder boundTarget;

        LinkClickListener(CardLinkHolder cardLinkHolder) {
            this.boundTarget = cardLinkHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onLinkClicked();
        }
    }

    class ThumbnailLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ CardLinkHolder boundTarget;

        ThumbnailLongClickListener(CardLinkHolder cardLinkHolder) {
            this.boundTarget = cardLinkHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onImageLongClicked();
        }
    }

    public CardLinkHolder_ViewBinding(CardLinkHolder cardLinkHolder, View view) {
        super(cardLinkHolder, view);
        this.target = cardLinkHolder;
        View viewC = Utils.findRequiredView(view, R.id.thumbnail, "field 'thumbnail', method 'onLinkClicked', and method 'onImageLongClicked'");
        cardLinkHolder.thumbnail = (PostThumbnailView) Utils.castView(viewC, R.id.thumbnail, "field 'thumbnail'", PostThumbnailView.class);
        this.thumbnailView = viewC;
        viewC.setOnClickListener(new LinkClickListener(cardLinkHolder));
        viewC.setOnLongClickListener(new ThumbnailLongClickListener(cardLinkHolder));
        cardLinkHolder.mSelftext = (CardSelftextPreviewTextView) Utils.findRequiredViewAsType(view, R.id.selftext, "field 'mSelftext'", CardSelftextPreviewTextView.class);
    }

    @Override
    public void unbind() {
        CardLinkHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mSelftext = null;
        this.thumbnailView.setOnClickListener(null);
        this.thumbnailView.setOnLongClickListener(null);
        this.thumbnailView = null;
        super.unbind();
    }
}
