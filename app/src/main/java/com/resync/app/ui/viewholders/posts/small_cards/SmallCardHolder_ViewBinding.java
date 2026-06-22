package com.resync.app.ui.viewholders.posts.small_cards;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder_ViewBinding;
import com.resync.app.ui.views.images.CustomImageView;

/* JADX INFO: loaded from: classes3.dex */
public class SmallCardHolder_ViewBinding extends AbstractCardPostHolder_ViewBinding {

    private SmallCardHolder target;

    private View thumbnailView;

    class PreviewClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SmallCardHolder boundTarget;

        PreviewClickListener(SmallCardHolder smallCardHolder) {
            this.boundTarget = smallCardHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onPreviewClicked();
        }
    }

    class ThumbnailLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ SmallCardHolder boundTarget;

        ThumbnailLongClickListener(SmallCardHolder smallCardHolder) {
            this.boundTarget = smallCardHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onImageLongClicked();
        }
    }

    public SmallCardHolder_ViewBinding(SmallCardHolder smallCardHolder, View view) {
        super(smallCardHolder, view);
        this.target = smallCardHolder;
        View viewC = Utils.findRequiredView(view, R.id.holder_card_link_thumbnail, "field 'mLinkThumbnail', method 'onPreviewClicked', and method 'onImageLongClicked'");
        smallCardHolder.mLinkThumbnail = (CustomImageView) Utils.castView(viewC, R.id.holder_card_link_thumbnail, "field 'mLinkThumbnail'", CustomImageView.class);
        this.thumbnailView = viewC;
        viewC.setOnClickListener(new PreviewClickListener(smallCardHolder));
        viewC.setOnLongClickListener(new ThumbnailLongClickListener(smallCardHolder));
        smallCardHolder.mLinkThumbnailWrapper = (FrameLayout) Utils.findRequiredViewAsType(view, R.id.holder_card_link_thumbnail_wrapper, "field 'mLinkThumbnailWrapper'", FrameLayout.class);
        smallCardHolder.mTextWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.holder_small_card_text_wrapper, "field 'mTextWrapper'", LinearLayout.class);
    }

    @Override
    public void unbind() {
        SmallCardHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mLinkThumbnail = null;
        target.mLinkThumbnailWrapper = null;
        target.mTextWrapper = null;
        this.thumbnailView.setOnClickListener(null);
        this.thumbnailView.setOnLongClickListener(null);
        this.thumbnailView = null;
        super.unbind();
    }
}
