package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.core.graphics.ColorUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.helpers.ui.MaterialUtil;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.text.html.Options;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: loaded from: classes3.dex */
public class TwitterEmbedded extends RelativeLayout {

    Post post;

    @BindView(R.id.twitter_description)
    TwitterDescription mDescription;

    @BindView(R.id.twitter_thumbnail)
    PostThumbnailView mThumbnail;

    @BindView(R.id.twitter_html)
    HtmlTextView mTwitterHtmlView;

    public TwitterEmbedded(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (MonetUtil.isAmoled()) {
            int strokeColor = ColorUtils.setAlphaComponent(MonetUtil.getPrimaryTextColor(true), 50);
            gradientDrawable.setCornerRadius(MaterialUtil.getCornerRadius());
            gradientDrawable.setStroke(SizeHelper.dpToPx(1), strokeColor);
        } else {
            gradientDrawable.setColor(MonetUtil.getCardColorAuto());
        }
        gradientDrawable.setCornerRadius(SizeHelper.dpToPxFloat(12));
        setBackground(gradientDrawable);
        int horizontalPadding = SizeHelper.dpToPx(12);
        int verticalPadding = SizeHelper.dpToPx(12);
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onClicked(View view) {
        LinkHelper.open(getContext(), this.post.getUrl());
    }

    public void disableVisitedDim() {
        this.mTwitterHtmlView.setVisitedDimDisabled(true);
    }

    public void bind(Post post) {
        this.post = post;
        this.mTwitterHtmlView.setVisited(post.isVisitedOrInternal());
        this.mTwitterHtmlView.setHtmlWithOptions(Options.disabled(), post.getEmbedded());
        this.mDescription.bind(post);
        this.mThumbnail.bind(post);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TwitterEmbedded.this.onClicked(view);
            }
        });
    }
}
