package com.resync.app.ui.viewholders.posts.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.graphics.ColorUtils;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.fragment_dialogs.ImagePeekDialogFragment;
import com.resync.app.ui.views.posts.cards.AltUrlView;
import com.resync.app.ui.views.posts.cards.CardPostPreviewHelper;
import com.resync.app.ui.views.posts.cards.CardSelftextPreviewTextView;
import com.resync.app.ui.views.posts.simple.IndicatorView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.widgets.SimpleExoPlayerTextureView;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TransitionHelper;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class CardImageHolder extends AbstractCardPostHolder {

    CardPostPreviewHelper mImageView;

    ViewGroup mImageWrapper;

    IndicatorView mIndicatorImage;

    CardSelftextPreviewTextView mSelftext;

    AltUrlView mUrlLabel;

    private CardImageHolder(Context context, BaseCursorPresenter baseCursorPresenter, View view, int uiMode) {
        super(context, baseCursorPresenter, view, uiMode);
        if (SettingsSingleton.getSettings().cardImageAbove) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) this.mImageWrapper.getParent();
        viewGroup.removeView(this.mImageWrapper);
        viewGroup.addView(this.mImageWrapper, viewGroup.indexOfChild(this.mExtraWrapper) + 1);
        ((LinearLayout.LayoutParams) this.mImageWrapper.getLayoutParams()).topMargin = ((LinearLayout.LayoutParams) this.mImageWrapper.getLayoutParams()).bottomMargin;
        ((LinearLayout.LayoutParams) this.mImageWrapper.getLayoutParams()).bottomMargin = 0;
        PaddingHelper.setPaddingTop(this.mTitle, 12);
    }

    public static CardImageHolder create(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int uiMode) {
        return new CardImageHolder(context, baseCursorPresenter, LayoutInflater.from(context).inflate(R.layout.holder_card_image, viewGroup, false), uiMode);
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder
    protected void applyFullWidthLayout() {
        super.applyFullWidthLayout();
        PaddingHelper.setPaddingHorizontal(this.mUrlLabel, 16, 16);
        PaddingHelper.setPaddingHorizontal(this.mSelftext, 16, 16);
    }

    public void applyCompactImagePreview() {
        setScrolling();
        this.mImageView.getLayoutParams().height = SizeHelper.dpToPx(SubsamplingScaleImageView.ORIENTATION_180);
        this.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.mImageView.setImageTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(MonetUtil.getLinkColorAuto(), 85)));
    }

    @Override // com.resync.app.holders.BasePostViewHolder, com.resync.app.holders.RedditViewHolder
    public void recycle() {
        super.recycle();
        this.mIndicatorImage.setVisibility(8);
        this.mUrlLabel.setVisibility(8);
        this.mSelftext.setVisibility(8);
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder, com.resync.app.holders.BasePostViewHolder
    public void bindPost(Post post, int position) {
        super.bindPost(post, position);
        this.mImageView.setPost(post);
        if (getPost().getSyncType() == 9 || (getPost().getSyncType() == 1 && LinkTypeHelper.isYouTube(getPost().getUrl()))) {
            this.mUrlLabel.setVisibility(0);
            this.mUrlLabel.bind(getPost());
        }
        this.mIndicatorImage.bind(getPost(), getUiMode());
        if (StringUtils.isNotEmpty(getPost().getSelftextProcessed())) {
            this.mSelftext.setVisibility(0);
            this.mSelftext.bind(getPost(), isPreviewMode());
        }
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public boolean canAutoplay() {
        if (UiSingleton.getColumnCount(getUiMode()) > 1) {
            return false;
        }
        return this.mImageView.canAutoplay(this.context);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public SimpleExoPlayerTextureView getTextureView() {
        for (int i = 0; i < this.mImageWrapper.getChildCount(); i++) {
            if (this.mImageWrapper.getChildAt(i) instanceof SimpleExoPlayerTextureView) {
                return (SimpleExoPlayerTextureView) this.mImageWrapper.getChildAt(i);
            }
        }
        return null;
    }

    protected void onImageClicked() {
        if (getTextureView() != null) {
            this.mImageView.setOverlayColor(getTextureView().getCurrentPosition());
        }
        getPresenter().openLink(TransitionHelper.buildSharedElements(this.context, this.mImageView, getTextureView()), getPost());
    }

    protected boolean onImageLongClicked() {
        ImagePeekDialogFragment.show(this.context, getPost());
        return true;
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder, com.resync.app.holders.BasePostViewHolder
    public void setPreviewMode() {
        super.setPreviewMode();
        this.mImageView.applyCommentMode();
        this.mSelftext.setVisitedDimDisabled(true);
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder, com.resync.app.holders.BasePostViewHolder
    public void setScrolling() {
        super.setScrolling();
        this.mImageView.setNsfw(true);
        this.mImageWrapper.setEnabled(false);
        this.mSelftext.setVisitedDimDisabled(true);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void attachTextureView(SimpleExoPlayerTextureView simpleExoPlayerTextureView) {
        if (simpleExoPlayerTextureView.isAttachedTo(this.mImageWrapper)) {
            return;
        }
        simpleExoPlayerTextureView.stopAndDetach();
        this.mImageWrapper.addView(simpleExoPlayerTextureView);
        simpleExoPlayerTextureView.play(getPost());
    }
}
