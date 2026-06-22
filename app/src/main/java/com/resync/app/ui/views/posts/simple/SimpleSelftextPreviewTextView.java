package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.ColorUtils;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.helpers.ui.MaterialUtil;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.text.html.Options;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class SimpleSelftextPreviewTextView extends HtmlTextView {
    public SimpleSelftextPreviewTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (SettingsSingleton.getSettings().slideSelftextBorder) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            if (MonetUtil.isAmoled()) {
                int strokeColor = ColorUtils.setAlphaComponent(MonetUtil.getPrimaryTextColor(true), 50);
                gradientDrawable.setCornerRadius(MaterialUtil.getCornerRadius());
                gradientDrawable.setStroke(SizeHelper.dpToPx(1), strokeColor);
            } else {
                gradientDrawable.setColor(MonetUtil.getCardColorAuto());
            }
            gradientDrawable.setCornerRadius(SizeHelper.dpToPx(8));
            setBackground(gradientDrawable);
            int horizontalPadding = SizeHelper.dpToPx(8);
            int verticalPadding = SizeHelper.dpToPx(8);
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        }
    }

    public void applyFullWidthMargins() {
        ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) getLayoutParams())).leftMargin = SizeHelper.dpToPx(16);
        ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) getLayoutParams())).rightMargin = SizeHelper.dpToPx(16);
    }

    public void bindPreview(Post post) {
        setMaxLines(5);
        setVisited(post.isVisitedOrInternal());
        setSpannableText(post.getSelftextProcessedPreview());
    }

    public void bind(Post post, boolean expanded) {
        if (expanded) {
            setMaxLines(Integer.MAX_VALUE);
        } else {
            setMaxLines(5);
        }
        setVisited(post.isVisitedOrInternal());
        setHtmlWithOptions(Options.markdownOnly(), post.getSelftextProcessed());
    }
}
