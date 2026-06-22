package com.resync.app.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: jc.a */
/* JADX INFO: compiled from: MoreActionsItemView.java */
/* JADX INFO: loaded from: classes3.dex */
public class MoreActionsItemView extends LinearLayout {

    AppCompatImageView imageView;
    TextView textView;

    public MoreActionsItemView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(1);
        AppCompatImageView appCompatImageView = new AppCompatImageView(getContext());
        this.imageView = appCompatImageView;
        appCompatImageView.setImageResource(R.drawable.outline_ac_unit_24);
        int iconPadding = SizeHelper.dpToPx(15);
        this.imageView.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(SizeHelper.dpToPx(54), SizeHelper.dpToPx(54));
        layoutParams.gravity = 1;
        addView(this.imageView, layoutParams);
        TextView textView = new TextView(getContext());
        this.textView = textView;
        textView.setGravity(17);
        this.textView.setEllipsize(TextUtils.TruncateAt.END);
        this.textView.setMaxLines(2);
        this.textView.setTextColor(MonetUtil.getPrimaryTextColorAuto());
        this.textView.setTextSize(RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        int textPadding = SizeHelper.dpToPx(8);
        this.textView.setPadding(textPadding, textPadding, textPadding, textPadding);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.gravity = 1;
        addView(this.textView, layoutParams2);
        int verticalPadding = SizeHelper.dpToPx(16);
        setPadding(0, verticalPadding, 0, verticalPadding);
        this.textView.setText("Example");
        this.imageView.setForeground(getContext().getDrawable(R.drawable.button_ripple));
        setSelected(false);
    }

    public void setIcon(int iconRes) {
        this.imageView.setImageResource(iconRes);
    }

    public void setLabel(String str) {
        this.textView.setText(str);
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.imageView.setAlpha(1.0f);
            this.textView.setAlpha(1.0f);
        } else {
            this.imageView.setAlpha(0.25f);
            this.textView.setAlpha(0.25f);
        }
        super.setEnabled(enabled);
    }

    @Override // android.view.View
    public void setSelected(boolean selected) {
        if (selected) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(MonetUtil.getAccent2BackgroundColorAuto());
            gradientDrawable.setCornerRadius(SizeHelper.dpToPxFloat(100));
            this.imageView.setBackgroundDrawable(gradientDrawable);
            this.imageView.setClipToOutline(true);
            this.imageView.setImageTintList(ColorStateList.valueOf(MonetUtil.getAccent2TextColorAuto()));
            return;
        }
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setColor(MonetUtil.getCardColorAuto());
        gradientDrawable2.setCornerRadius(SizeHelper.dpToPxFloat(100));
        this.imageView.setBackgroundDrawable(gradientDrawable2);
        this.imageView.setClipToOutline(true);
        this.imageView.setImageTintList(ColorStateList.valueOf(MonetUtil.getTertiaryTextColorAuto()));
    }
}
