package com.resync.app.ui.views.onboarding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.animation.CycleInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.helpers.ui.TabletHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class OnboardingDeviceIconView extends FrameLayout {

    private ImageView deviceImageView;
    private ImageView iconImageView;
    private SubView subView;

    class LoopAnimationListener extends AnimatorListenerAdapter {

        final /* synthetic */ ImageView targetView;

        LoopAnimationListener(ImageView imageView) {
            this.targetView = imageView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ImageView imageView = this.targetView;
            if (imageView == null || imageView.getVisibility() != 0 || ContextHelper.isActivityFinishing(this.targetView.getContext())) {
                return;
            }
            OnboardingDeviceIconView.this.animatePulse(this.targetView);
        }
    }

    public OnboardingDeviceIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        FrameLayout.inflate(getContext(), R.layout.view_onboarding_device_icon, this);
        this.deviceImageView = (ImageView) findViewById(R.id.device);
        this.iconImageView = (ImageView) findViewById(R.id.icon);
        this.subView = (SubView) findViewById(R.id.subview);
        if (!TabletHelper.isLandscape() || TabletHelper.isTabletResource()) {
            return;
        }
        this.deviceImageView.getLayoutParams().height = SizeHelper.dpToPx(110);
        int margin = SizeHelper.dpToPx(16);
        ((FrameLayout.LayoutParams) this.deviceImageView.getLayoutParams()).setMargins(margin, margin, margin, margin);
        this.deviceImageView.requestLayout();
    }

    public void animatePulse(ImageView imageView) {
        if (imageView != null) {
            imageView.animate().scaleY(1.15f).scaleX(1.15f).setDuration(4000L).setInterpolator(new CycleInterpolator(1.0f)).setListener(new LoopAnimationListener(imageView)).start();
        }
    }

    public void showIcon(int resId) {
        this.iconImageView.setVisibility(0);
        this.iconImageView.setImageResource(resId);
        animatePulse(this.iconImageView);
    }

    public void showSubreddit(String str) {
        this.subView.setVisibility(0);
        this.subView.setSubreddit(str);
        this.subView.applyOvalOutline();
        animatePulse(this.subView);
    }

    public void applyWhiteIconTint() {
        this.iconImageView.setImageTintList(ColorStateList.valueOf(-1));
    }
}
