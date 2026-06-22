package com.resync.app.ui.viewholders.posts.small_cards;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.graphics.ColorUtils;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.ImagePeekDialogFragment;
import com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.TextUtils;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class SmallCardHolder extends AbstractCardPostHolder {

    CustomImageView mLinkThumbnail;

    FrameLayout mLinkThumbnailWrapper;

    LinearLayout mTextWrapper;

    private SmallCardHolder(Context context, BaseCursorPresenter baseCursorPresenter, View view, int uiMode) {
        super(context, baseCursorPresenter, view, uiMode);
    }

    public static SmallCardHolder create(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int uiMode) {
        return SettingsSingleton.getSettings().smallCardsAlignThumbnailsLeft ? new SmallCardHolder(context, baseCursorPresenter, LayoutInflater.from(context).inflate(R.layout.holder_small_card_thumbnail_left, viewGroup, false), uiMode) : new SmallCardHolder(context, baseCursorPresenter, LayoutInflater.from(context).inflate(R.layout.holder_small_card_thumbnail_right, viewGroup, false), uiMode);
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder
    protected void applyFullWidthLayout() {
    }

    public void applyCompactImagePreview() {
        setScrolling();
        this.mLinkThumbnail.setImageTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(MonetUtil.getLinkColorAuto(), 63)));
    }

    @Override // com.resync.app.ui.viewholders.posts.card.AbstractCardPostHolder, com.resync.app.holders.BasePostViewHolder
    public void bindPost(Post post, int position) {
        super.bindPost(post, position);
        if (TextUtils.isEmpty(getPost().getThumbnailPlaceholder(this.context))) {
            this.mLinkThumbnailWrapper.setVisibility(8);
            this.mTextWrapper.setPadding(0, 0, 0, 0);
            return;
        }
        this.mLinkThumbnailWrapper.setVisibility(0);
        if (SettingsSingleton.getSettings().smallCardsAlignThumbnailsLeft) {
            this.mTextWrapper.setPadding((int) SizeHelper.dpToPxFloat(68), 0, 0, 0);
        } else {
            this.mTextWrapper.setPadding(0, 0, (int) SizeHelper.dpToPxFloat(68), 0);
        }
        this.mLinkThumbnail.load(getPost().getThumbnailPlaceholder(this.context), getPost().isVisitedOrInternal(), false);
    }

    protected boolean onImageLongClicked() {
        if (isScrolling()) {
            return false;
        }
        ImagePeekDialogFragment.show(this.context, getPost());
        return true;
    }

    protected void onPreviewClicked() {
        if (isScrolling()) {
            return;
        }
        if (getPresenter() != null) {
            getPresenter().openLink(null, getPost());
        } else {
            LinkHelper.open(this.context, null, this.post);
        }
    }
}
