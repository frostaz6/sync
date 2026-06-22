package com.resync.app.ui.views.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.DragButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.ui.views.responsive.CustomSubtleDividerView;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Subreddit;

/* JADX INFO: loaded from: classes3.dex */
public class SheetHeaderView extends RelativeLayout {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    String title;
    boolean showMore;

    CustomImageView bannerView;
    boolean showDivider;

    CustomSubtleDividerView dividerView;

    DragButton dragButton;

    MoreButton moreButton;

    SubView subView;

    RelativeLayout subViewWrapper;

    TextView textView;

    LinearLayoutCompat textWrapper;

    class BannerOutlineProvider extends ViewOutlineProvider {
        BannerOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + SizeHelper.dpToPx(12), SizeHelper.dpToPx(12));
        }
    }

    public SheetHeaderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, SyncStyleable.SHEET_HEADER_VIEW);
        if (typedArray.hasValue(2)) {
            this.title = typedArray.getString(2);
        }
        this.showMore = typedArray.getBoolean(1, false);
        this.showDivider = typedArray.getBoolean(0, false);
        typedArray.recycle();
    }

    public void setMoreClickListener(View.OnClickListener onClickListener) {
        this.moreButton.setOnClickListener(onClickListener);
    }

    public void addHeaderView(View view) {
        this.textWrapper.addView(view, 1);
    }

    public void clearBanner() {
        this.bannerView.setImageBitmap(null);
    }

    public void clearBannerOutline() {
        this.bannerView.setOutlineProvider(null);
        this.bannerView.setClipToOutline(false);
    }

    public void setMoreVisible(boolean visible) {
        this.moreButton.setVisibility(visible ? 0 : 8);
    }

    public void setDividerVisible(boolean visible) {
        this.dividerView.setVisibility(visible ? 0 : 8);
    }

    public void setDragButtonVisible(boolean visible) {
        this.dragButton.setVisibility(visible ? 0 : 8);
    }

    public void setSubreddit(String subreddit) {
        if (StringUtils.isNotEmpty(subreddit)) {
            this.subViewWrapper.setVisibility(0);
            this.subView.setSubreddit(subreddit);
            this.subView.enableIconClick();
            ((RelativeLayout.LayoutParams) this.textWrapper.getLayoutParams()).topMargin = SizeHelper.dpToPx(4);
            ((RelativeLayout.LayoutParams) this.moreButton.getLayoutParams()).topMargin = SizeHelper.dpToPx(80);
        }
    }

    public void setBanner(Subreddit subreddit) {
        Logger.verbose("SUB: " + subreddit);
        if (subreddit != null) {
            String bannerUrl = subreddit.data.bannerBackgroundImage;
            if (StringUtils.isNotEmpty(bannerUrl)) {
                this.bannerView.load(bannerUrl);
            }
            this.subView.setElevation(SizeHelper.dpToPx(1));
        }
    }

    public void setTitle(String title) {
        this.textView.setText(title);
    }

    public void setUsername(String username) {
        if (StringUtils.isNotEmpty(username)) {
            this.subViewWrapper.setVisibility(0);
            this.subView.setUsername(username);
            this.subView.enableIconClick();
            ((RelativeLayout.LayoutParams) this.textWrapper.getLayoutParams()).topMargin = SizeHelper.dpToPx(4);
            ((RelativeLayout.LayoutParams) this.moreButton.getLayoutParams()).topMargin = SizeHelper.dpToPx(80);
        }
    }

    public void applyColors(boolean night) {
        this.textView.setTextColor(StyleSingleton.getTextColor(getContext(), false, night));
        this.moreButton.setHighlighted(night);
        this.dividerView.applyColors(night);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        RelativeLayout.inflate(getContext(), R.layout.view_section_header, this);
        ButterKnife.bind(this);
        if (StringUtils.isNotEmpty(this.title)) {
            this.textView.setText(this.title);
        }
        this.moreButton.setVisibility(this.showMore ? 0 : 8);
        this.dividerView.setVisibility(this.showDivider ? 0 : 8);
        this.bannerView.setOutlineProvider(new BannerOutlineProvider());
        this.bannerView.setClipToOutline(true);
        this.bannerView.setBackgroundColor(MonetUtil.getToolbarColorAuto());
    }
}
