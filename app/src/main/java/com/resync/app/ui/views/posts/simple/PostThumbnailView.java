package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.DataSavingHelper;
import com.resync.app.util.TextUtils;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class PostThumbnailView extends CustomImageView {
    int widthPx;

    int heightPx;

    public PostThumbnailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.widthPx = SizeHelper.dpToPx(80);
        this.heightPx = SizeHelper.dpToPx(80);
    }

    private void showPlaceholder() {
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setBackgroundColor(MonetUtil.getCardColorAuto());
        setImageResource(R.drawable.outline_web_24);
        setColorFilter(MonetUtil.getTertiaryTextColorAuto());
    }

    public boolean hasThumbnail(Post post) {
        return (TextUtils.isEmpty(post.getThumbnail()) && TextUtils.isEmpty(post.getPreviewUrl())) ? false : true;
    }

    public void markNsfw() {
        setNsfw(true);
    }

    public void bind(Post post) {
        setVisibility(0);
        if (DataSavingHelper.isFullDataSaving()) {
            showPlaceholder();
            return;
        }
        if (!TextUtils.isEmpty(post.getThumbnail())) {
            setScaleType(ImageView.ScaleType.CENTER_CROP);
            load(post.getThumbnail(), post.isVisitedOrInternal(), post.shouldBlurNsfw(), this.widthPx, this.heightPx);
        } else if (TextUtils.isEmpty(post.getPreviewUrl())) {
            showPlaceholder();
        } else {
            setScaleType(ImageView.ScaleType.CENTER_CROP);
            load(post.getPreviewUrl(), post.isVisitedOrInternal(), post.shouldBlurNsfw(), this.widthPx, this.heightPx);
        }
    }

    @Override // com.resync.app.ui.views.images.CustomImageView
    protected void onRecycled() {
        setBackgroundColor(0);
    }
}
