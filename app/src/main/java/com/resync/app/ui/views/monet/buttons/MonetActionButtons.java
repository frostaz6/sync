package com.resync.app.ui.views.monet.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.resync.app.R;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: loaded from: classes3.dex */
public class MonetActionButtons extends FrameLayout {

    MonetContentHighlightButton buttonNegative;

    MonetContentHighlightButton buttonNeutral;

    MonetContentHighlightButton buttonPositive;

    LinearLayout negativeNeutralWrapper;

    public MonetActionButtons(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setId(R.id.monet_action_buttons);
        LayoutInflater.from(context).inflate(R.layout.view_monet_action_buttons, (ViewGroup) this, true);
        ButterKnife.bind(this);
    }

    private void updateWrapperVisibility() {
        this.negativeNeutralWrapper.setVisibility((this.buttonNegative.getVisibility() == 0 || this.buttonNeutral.getVisibility() == 0) ? 0 : 8);
    }

    public void setNegativeText(String str) {
        this.buttonNegative.setText(str);
        this.buttonNegative.setVisibility(StringUtils.isNotEmpty(str) ? 0 : 8);
        updateWrapperVisibility();
    }

    public void setNeutralText(String str) {
        this.buttonNeutral.setText(str);
        this.buttonNeutral.setVisibility(StringUtils.isNotEmpty(str) ? 0 : 8);
        updateWrapperVisibility();
    }

    public void setListeners(View.OnClickListener onClickListener, View.OnClickListener onClickListener2) {
        setListeners(onClickListener, null, onClickListener2);
    }

    public void setListeners(View.OnClickListener onClickListener, View.OnClickListener onClickListener2, View.OnClickListener onClickListener3) {
        this.buttonPositive.setOnClickListener(onClickListener);
        this.buttonNeutral.setOnClickListener(onClickListener2);
        this.buttonNegative.setOnClickListener(onClickListener3);
    }

    public void setPositiveText(String str) {
        this.buttonPositive.setText(str);
    }

    public void setButtonLabels(String str) {
        setButtonLabels(str, null, null);
    }

    public void setButtonLabels(String str, String str2) {
        setButtonLabels(str, null, str2);
    }

    public void setButtonLabels(String str, String str2, String str3) {
        setPositiveText(str);
        setNeutralText(str2);
        setNegativeText(str3);
    }

    public void applyColors(boolean night) {
        this.buttonNegative.applyColors(night);
        this.buttonNeutral.applyColors(night);
        this.buttonPositive.applyColors(night);
    }
}
