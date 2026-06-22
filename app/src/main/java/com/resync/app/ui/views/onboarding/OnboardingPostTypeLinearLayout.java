package com.resync.app.ui.views.onboarding;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.viewholders.posts.card.CardImageHolder;
import com.resync.app.ui.viewholders.posts.compact.CompactHolder;
import com.resync.app.ui.viewholders.posts.simple.SimpleHolder;
import com.resync.app.ui.viewholders.posts.small_cards.SmallCardHolder;
import com.resync.app.media.images.SyncTypeHelper;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class OnboardingPostTypeLinearLayout extends LinearLayout implements DarkModeAware {
    public OnboardingPostTypeLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void buildPreview() {
        removeAllViews();
        for (int index = 0; index < 1; index++) {
            Post postG = Post.createExampleGooglePhotosPost();
            PaddingHelper.setPaddingAll(this, 2);
            BasePostViewHolder basePostViewHolderJ = null;
            if (UiSingleton.getInstance().getUiMode() == 5) {
                basePostViewHolderJ = SimpleHolder.create(getContext(), this, null);
            } else if (UiSingleton.getInstance().getUiMode() == 4) {
                basePostViewHolderJ = (BasePostViewHolder) SyncTypeHelper.createCardHolder(getContext(), null, null, SyncTypeHelper.getCardViewType(postG.getSyncType()), 4);
                PaddingHelper.setPaddingAll(this, 16);
            } else if (UiSingleton.getInstance().getUiMode() == 3) {
                basePostViewHolderJ = (BasePostViewHolder) SyncTypeHelper.createSmallCardHolder(getContext(), null, null, SyncTypeHelper.getCompactViewType(postG.getSyncType()));
                PaddingHelper.setPaddingAll(this, 16);
            } else if (UiSingleton.getInstance().getUiMode() == 2) {
                basePostViewHolderJ = (BasePostViewHolder) SyncTypeHelper.createCompactHolder(getContext(), null, null, SyncTypeHelper.getCompactViewType(postG.getSyncType()), 2);
                PaddingHelper.setPaddingAll(this, 16);
            } else if (UiSingleton.getInstance().getUiMode() == 1) {
                basePostViewHolderJ = (BasePostViewHolder) SyncTypeHelper.createCompactHolder(getContext(), null, null, SyncTypeHelper.getCompactViewType(postG.getSyncType()), 1);
                PaddingHelper.setPaddingTop(this, 16);
                PaddingHelper.setPaddingBottom(this, 16);
            } else if (UiSingleton.getInstance().getUiMode() == 0) {
                basePostViewHolderJ = (BasePostViewHolder) SyncTypeHelper.createCompactHolder(getContext(), null, null, SyncTypeHelper.getCompactViewType(postG.getSyncType()), 0);
                PaddingHelper.setPaddingTop(this, 16);
                PaddingHelper.setPaddingBottom(this, 16);
            }
            basePostViewHolderJ.itemView.setElevation(SizeHelper.dpToPx(2));
            addView(basePostViewHolderJ.itemView);
            basePostViewHolderJ.bindPost(postG, 0);
            basePostViewHolderJ.setScrolling();
            if (basePostViewHolderJ instanceof SimpleHolder) {
                ((SimpleHolder) basePostViewHolderJ).applyCommentImagePreview();
            } else if (basePostViewHolderJ instanceof CardImageHolder) {
                ((CardImageHolder) basePostViewHolderJ).applyCompactImagePreview();
            } else if (basePostViewHolderJ instanceof CompactHolder) {
                ((CompactHolder) basePostViewHolderJ).applyCommentImagePreview();
            } else if (basePostViewHolderJ instanceof SmallCardHolder) {
                ((SmallCardHolder) basePostViewHolderJ).applyCompactImagePreview();
            }
            setScaleX(0.7f);
            setScaleY(0.7f);
        }
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        buildPreview();
    }
}
