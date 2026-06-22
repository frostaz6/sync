package com.resync.app.ui.views.tooltips;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import butterknife.ButterKnife;
import com.google.android.material.card.MaterialCardView;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class TooltipView extends FrameLayout {

    public DismissListener dismissListener;
    public String text;
    public int iconRes;

    MaterialCardView cardView;

    AppCompatImageView closeView;

    public String settingName;

    public boolean dismissOnShow;

    BaseTextView textView;

    AppCompatImageView tooltipIcon;

    class CloseClickListener implements View.OnClickListener {

        class AnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
            AnimatorUpdateListener() {
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedHeight = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ViewGroup.LayoutParams layoutParams = TooltipView.this.getLayoutParams();
                layoutParams.height = animatedHeight;
                TooltipView.this.setLayoutParams(layoutParams);
            }
        }

        class CollapseEndListener extends AnimatorListenerAdapter {
            CollapseEndListener() {
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                TooltipView.DismissListener listener = TooltipView.this.dismissListener;
                if (listener != null) {
                    listener.onDismiss();
                }
            }
        }

        CloseClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (StringUtils.isNotEmpty(TooltipView.this.settingName)) {
                SettingsSingleton.getInstance().setBooleanField(TooltipView.this.settingName, false);
                SettingsSingleton.getInstance().putBoolean(TooltipView.this.settingName, false);
            }
            ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(TooltipView.this.getHeight(), 0);
            valueAnimatorOfInt.addUpdateListener(new AnimatorUpdateListener());
            valueAnimatorOfInt.addListener(new CollapseEndListener());
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat((View) TooltipView.this, View.ALPHA, 0.0f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(220L);
            animatorSet.setInterpolator(new FastOutSlowInInterpolator());
            animatorSet.playTogether(valueAnimatorOfInt, objectAnimatorOfFloat);
            animatorSet.start();
        }
    }

    public interface DismissListener {
        void onDismiss();
    }

    public TooltipView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.TOOLTIP_VIEW);
        this.text = typedArrayObtainStyledAttributes.getString(3);
        this.iconRes = typedArrayObtainStyledAttributes.getResourceId(0, R.drawable.ic_info_outline_black_24dp);
        this.settingName = typedArrayObtainStyledAttributes.getString(1);
        this.dismissOnShow = typedArrayObtainStyledAttributes.getBoolean(2, true);
        if (StringUtils.isAnyEmpty(this.text)) {
            throw new RuntimeException("Missing params");
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        FrameLayout.inflate(getContext(), R.layout.view_tooltip, this);
        ButterKnife.bind(this);
        if (StringUtils.isNotEmpty(this.settingName)) {
            Logger.verbose("TooltipView", "Setting name: " + this.settingName);
            boolean enabled = ((Boolean) SettingsSingleton.getInstance().getFieldValue(this.settingName)).booleanValue();
            Logger.verbose("TooltipView", "Setting value: " + enabled);
            if (!enabled) {
                setVisibility(8);
                return;
            } else if (this.dismissOnShow) {
                SettingsSingleton.getInstance().setBooleanField(this.settingName, false);
                SettingsSingleton.getInstance().putBoolean(this.settingName, false);
            }
        } else {
            this.closeView.setVisibility(8);
        }
        this.textView.setText(this.text);
        int backgroundColor = MonetUtil.getAccent2BackgroundColorAuto();
        int textColor = MonetUtil.getAccent2TextColorAuto();
        this.cardView.setCardBackgroundColor(backgroundColor);
        this.cardView.setStrokeWidth(0);
        this.textView.setTextColor(textColor);
        this.tooltipIcon.setImageResource(this.iconRes);
        this.tooltipIcon.setColorFilter(textColor);
        this.closeView.setColorFilter(textColor);
        this.closeView.setOnClickListener(new CloseClickListener());
    }
}
