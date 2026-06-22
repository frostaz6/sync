package com.resync.app.ui.views.ultra;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.ButterKnife;
import com.google.android.material.card.MaterialCardView;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.CustomProgressBar;
import com.resync.app.ui.views.text.basic.ProductSansTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class UltraPurchasedView extends MaterialCardView implements DarkModeAware {
    private boolean selected;

    AppCompatImageView errorImageView;

    ImageView icon;

    ProductSansTextView priceDescriptionTextView;

    ProductSansTextView priceTextView;

    CustomProgressBar progressBar;

    public UltraPurchasedView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
        setSelected(false);
        setEnabled(false);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_ultra_purchased, (ViewGroup) this, true);
        ButterKnife.bind(this);
        applyColors();
    }

    public void setDescription(CharSequence charSequence) {
        this.priceDescriptionTextView.setText(charSequence);
    }

    public void setIcon(int resId) {
        this.icon.setImageResource(resId);
    }

    public void setPrice(CharSequence charSequence) {
        this.errorImageView.setVisibility(8);
        this.progressBar.setVisibility(8);
        this.priceTextView.setVisibility(0);
        this.priceTextView.setText(charSequence);
    }

    public void showError() {
        this.errorImageView.setVisibility(0);
        this.progressBar.setVisibility(8);
        this.priceTextView.setVisibility(4);
    }

    public void applyColors() {
        setCardBackgroundColor(MonetUtil.getBackgroundColorAuto());
        setRadius(SizeHelper.dpToPx(12));
        this.priceTextView.applyTextColor();
        this.priceDescriptionTextView.applyTextColor();
        this.progressBar.applyColors();
        this.errorImageView.setImageTintList(ColorStateList.valueOf(MonetUtil.getPrimaryTextColorAuto()));
        this.icon.setImageTintList(ColorStateList.valueOf(MonetUtil.getPrimaryTextColorAuto()));
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    @Override // android.view.View
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (!selected) {
            setStrokeWidth(0);
        } else {
            setStrokeColor(MonetUtil.getLinkColorAuto());
            setStrokeWidth(SizeHelper.dpToPx(6));
        }
    }
}
