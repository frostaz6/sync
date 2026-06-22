package com.resync.app.ui.views.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.ColorUtils;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.views.buttons.CustomAppCompatButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class MaterialRow extends LinearLayout {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    GradientDrawable backgroundDrawable;

    CustomAppCompatButton mIcon;

    AppCompatImageView mImage;

    ViewGroup mItemsWrapper;

    MoreButton mMoreButton;

    TextView mSecondary;

    SubView mSubView;

    TextView mTitle;

    public MaterialRow(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LinearLayout.inflate(context, R.layout.view_material_row, this);
        ButterKnife.bind(this);
        int padding = SizeHelper.dpToPx(8);
        setPadding(0, padding, 0, padding);
        setGravity(16);
        setForeground(getResources().getDrawable(R.drawable.button_light));
        GradientDrawable gradientDrawable = new GradientDrawable();
        this.backgroundDrawable = gradientDrawable;
        gradientDrawable.setCornerRadius(SizeHelper.dpToPxFloat(12));
        setBackground(this.backgroundDrawable);
        showIcon();
    }

    public void showIcon() {
        this.mImage.setVisibility(8);
        this.mIcon.setVisibility(0);
        this.mSubView.setVisibility(8);
    }

    public void showImage() {
        this.mImage.setVisibility(0);
        this.mIcon.setVisibility(8);
        this.mSubView.setVisibility(8);
    }

    public void setIconResource(int resId) {
        this.mItemsWrapper.setVisibility(resId == 0 ? 8 : 0);
        if (resId == R.mipmap.ic_launcher_new_fancy) {
            this.mIcon.setImageResource(R.drawable.account_details_outline);
        } else {
            this.mIcon.setImageResource(resId);
        }
    }

    public void loadImage(String url) {
        Glide.with(RedditApplication.getAppContext()).load(url).into(this.mImage);
    }

    public void setMoreClickListener(View.OnClickListener onClickListener) {
        this.mMoreButton.setOnClickListener(onClickListener);
    }

    public void setHighlighted(boolean highlighted) {
        if (highlighted) {
            this.backgroundDrawable.setColor(ColorUtils.setAlphaComponent(MonetUtil.getLinkColorAuto(), 42));
        } else {
            this.backgroundDrawable.setColor(0);
        }
    }

    public void setSecondary(CharSequence text) {
        this.mSecondary.setText(text);
        this.mSecondary.setVisibility(0);
    }

    public void setSubreddit(String subreddit) {
        this.mSubView.setSubreddit(subreddit);
    }

    public void showSubredditIcon() {
        this.mImage.setVisibility(8);
        this.mIcon.setVisibility(8);
        this.mSubView.setVisibility(0);
    }

    public void setTitle(CharSequence text) {
        this.mTitle.setText(text);
    }

    public void setUsername(String username) {
        this.mSubView.setUsername(username);
    }

    public void showUserIcon() {
        this.mImage.setVisibility(8);
        this.mIcon.setVisibility(8);
        this.mSubView.setVisibility(0);
    }

    public void showMore() {
        this.mMoreButton.setVisibility(0);
    }

    public void applyColorsAuto() {
        this.mTitle.setTextColor(StyleSingleton.getTextColor(getContext(), false, NightHelper.isNight()));
        this.mIcon.applyColorFilter(NightHelper.isNight());
    }

    public void applyColors(boolean night) {
        this.mTitle.setTextColor(StyleSingleton.getTextColor(getContext(), false, night));
        this.mIcon.applyColorFilter(night);
    }

    public MaterialRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, SyncStyleable.MATERIAL_ROW);
        this.mTitle.setText(typedArray.getString(1));
        this.mIcon.setImageResource(typedArray.getResourceId(0, 0));
        typedArray.recycle();
    }
}
