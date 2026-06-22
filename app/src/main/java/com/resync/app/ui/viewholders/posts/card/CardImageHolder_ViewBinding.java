package com.resync.app.ui.viewholders.posts.card;

import android.view.View;
import android.view.ViewGroup;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.posts.cards.AltUrlView;
import com.resync.app.ui.views.posts.cards.CardPostPreviewHelper;
import com.resync.app.ui.views.posts.cards.CardSelftextPreviewTextView;
import com.resync.app.ui.views.posts.simple.IndicatorView;

/* JADX INFO: loaded from: classes3.dex */
public class CardImageHolder_ViewBinding extends AbstractCardPostHolder_ViewBinding {

    private CardImageHolder target;

    private View imageWrapperView;

    class ImageClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CardImageHolder boundTarget;

        ImageClickListener(CardImageHolder cardImageHolder) {
            this.boundTarget = cardImageHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onImageClicked();
        }
    }

    class ImageLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ CardImageHolder boundTarget;

        ImageLongClickListener(CardImageHolder cardImageHolder) {
            this.boundTarget = cardImageHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onImageLongClicked();
        }
    }

    public CardImageHolder_ViewBinding(CardImageHolder cardImageHolder, View view) {
        super(cardImageHolder, view);
        this.target = cardImageHolder;
        View viewC = Utils.findRequiredView(view, R.id.holder_card_image_preview_wrapper, "field 'mImageWrapper', method 'onImageClicked', and method 'onImageLongClicked'");
        cardImageHolder.mImageWrapper = (ViewGroup) Utils.castView(viewC, R.id.holder_card_image_preview_wrapper, "field 'mImageWrapper'", ViewGroup.class);
        this.imageWrapperView = viewC;
        viewC.setOnClickListener(new ImageClickListener(cardImageHolder));
        viewC.setOnLongClickListener(new ImageLongClickListener(cardImageHolder));
        cardImageHolder.mImageView = (CardPostPreviewHelper) Utils.findRequiredViewAsType(view, R.id.image, "field 'mImageView'", CardPostPreviewHelper.class);
        cardImageHolder.mIndicatorImage = (IndicatorView) Utils.findRequiredViewAsType(view, R.id.indicator, "field 'mIndicatorImage'", IndicatorView.class);
        cardImageHolder.mUrlLabel = (AltUrlView) Utils.findRequiredViewAsType(view, R.id.url_label, "field 'mUrlLabel'", AltUrlView.class);
        cardImageHolder.mSelftext = (CardSelftextPreviewTextView) Utils.findRequiredViewAsType(view, R.id.selftext, "field 'mSelftext'", CardSelftextPreviewTextView.class);
    }

    @Override
    public void unbind() {
        CardImageHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mImageWrapper = null;
        target.mImageView = null;
        target.mIndicatorImage = null;
        target.mUrlLabel = null;
        target.mSelftext = null;
        this.imageWrapperView.setOnClickListener(null);
        this.imageWrapperView.setOnLongClickListener(null);
        this.imageWrapperView = null;
        super.unbind();
    }
}
