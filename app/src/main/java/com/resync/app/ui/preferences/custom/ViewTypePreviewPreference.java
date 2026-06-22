package com.resync.app.ui.preferences.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.viewholders.comments.CommentHolder;
import com.resync.app.ui.viewholders.posts.card.CardImageHolder;
import com.resync.app.ui.viewholders.posts.compact.CompactHolder;
import com.resync.app.ui.viewholders.posts.simple.SimpleHolder;
import com.resync.app.ui.viewholders.posts.small_cards.SmallCardHolder;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes.dex */
public class ViewTypePreviewPreference extends Preference {

    public static String KEY = ViewTypePreviewPreference.class.getSimpleName();

    FrameLayout previewWrapper;
    int viewType;

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(16));
        }
    }

    public ViewTypePreviewPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.VIEW_TYPE_PREVIEW);
        this.viewType = typedArrayObtainStyledAttributes.getInt(0, 5);
        typedArrayObtainStyledAttributes.recycle();
        setKey(KEY);
        setLayoutResource(R.layout.preference_view_type_preview);
    }

    public void refresh() {
        FrameLayout frameLayout = this.previewWrapper;
        if (frameLayout != null) {
            frameLayout.removeAllViews();
            int viewType = this.viewType;
            if (viewType == 5) {
                SimpleHolder simpleHolderJ = SimpleHolder.create(getContext(), this.previewWrapper, null);
                simpleHolderJ.setScrolling();
                simpleHolderJ.bindPost(Post.createExampleImagePost(), 0);
                this.previewWrapper.addView(simpleHolderJ.itemView);
            } else if (viewType == 4) {
                CardImageHolder cardImageHolderD = CardImageHolder.create(getContext(), this.previewWrapper, null, this.viewType);
                cardImageHolderD.setScrolling();
                cardImageHolderD.bindPost(Post.createExampleImagePost(), 0);
                if (SettingsSingleton.getSettings().cardFullWidth) {
                    PaddingHelper.setPaddingLTRB(this.previewWrapper, 0, 0, 0, 8);
                } else {
                    PaddingHelper.setPaddingHorizontal(this.previewWrapper, 16, 16);
                }
                this.previewWrapper.addView(cardImageHolderD.itemView);
            } else if (viewType == 3) {
                SmallCardHolder smallCardHolderD = SmallCardHolder.create(getContext(), this.previewWrapper, null, this.viewType);
                smallCardHolderD.setScrolling();
                smallCardHolderD.bindPost(Post.createExampleImagePost(), 0);
                PaddingHelper.setPaddingHorizontal(this.previewWrapper, 16, 16);
                this.previewWrapper.addView(smallCardHolderD.itemView);
            } else if (viewType == 0 || viewType == 1 || viewType == 2) {
                CompactHolder compactHolderA = CompactHolder.create(getContext(), this.previewWrapper, null, this.viewType);
                compactHolderA.setScrolling();
                compactHolderA.bindPost(Post.createExampleImagePost(), 0);
                this.previewWrapper.addView(compactHolderA.itemView);
            } else if (viewType == 7) {
                CommentHolder commentHolderW = CommentHolder.create(getContext(), this.previewWrapper, null);
                commentHolderW.setScrolling();
                commentHolderW.bind(Post.createExampleCommentPost());
                this.previewWrapper.addView(commentHolderW.itemView);
                ((LinearLayout.LayoutParams) this.previewWrapper.getLayoutParams()).leftMargin = SizeHelper.dpToPx(16);
                ((LinearLayout.LayoutParams) this.previewWrapper.getLayoutParams()).rightMargin = SizeHelper.dpToPx(16);
                commentHolderW.itemView.setOutlineProvider(new RoundedOutlineProvider());
                commentHolderW.itemView.setClipToOutline(true);
            }
            ((LinearLayout.LayoutParams) this.previewWrapper.getLayoutParams()).topMargin = SizeHelper.dpToPx(8);
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.itemView.setBackgroundColor(MonetUtil.getWindowBackgroundColorAuto());
        this.previewWrapper = (FrameLayout) preferenceViewHolder.itemView.findViewById(R.id.preference_view_type_preview_wrapper);
        refresh();
        preferenceViewHolder.itemView.setEnabled(false);
    }
}
