package com.resync.app.ui.viewholders.onboarding.pages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import com.resync.app.R;
import com.resync.app.ui.activities.OnboardingActivity;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.NotificationsViewHolder;
import com.resync.app.ui.views.onboarding.OnboardingSetupNotificationsButton;
import com.resync.app.helpers.ContextHelper;

/* JADX INFO: loaded from: classes3.dex */
public class NotificationsViewHolder extends AbstractOnboardingViewHolder {
    public NotificationsViewHolder(ViewGroup viewGroup) {
        super(viewGroup);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animate$0(ImageView imageView, ValueAnimator valueAnimator) {
        imageView.setImageTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(-1, 255 - ((int) (Float.valueOf(Math.max(0.0f, Float.valueOf(Math.min(1.0f, ((Float) valueAnimator.getAnimatedValue()).floatValue())).floatValue())).floatValue() * 255.0f)))));
        imageView.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
    }

    public void animate(final ImageView imageView) {
        imageView.animate().scaleY(1.05f).scaleX(1.05f).setDuration(4000L).setInterpolator(new CycleInterpolator(1.0f)).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: sb.e
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                NotificationsViewHolder.lambda$animate$0(imageView, valueAnimator);
            }
        }).setListener(new AnimatorListenerAdapter() { // from class: com.resync.app.ui.viewholders.onboarding.pages.NotificationsViewHolder.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ImageView imageView2 = imageView;
                if (imageView2 == null || imageView2.getVisibility() != 0 || ContextHelper.isActivityFinishing(imageView.getContext())) {
                    return;
                }
                NotificationsViewHolder.this.animate(imageView);
            }
        }).start();
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public String getDescription() {
        return "Android 13 requires you to manually accept notifications from ReSync. Notifications are shown when you download images and receive messages on Reddit.";
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return R.drawable.outline_mail_24;
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public int getInnerContentRes() {
        return R.layout.viewholder_inner_notifications;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return "Notifications";
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    protected void setup() {
        super.setup();
        animate((ImageView) this.itemView.findViewById(R.id.mail_icon));
        ((OnboardingSetupNotificationsButton) this.itemView.findViewById(R.id.setup)).setOnClickListener(new View.OnClickListener() { // from class: com.resync.app.ui.viewholders.onboarding.pages.NotificationsViewHolder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ContextHelper.getBaseActivity(NotificationsViewHolder.this.itemView.getContext()) instanceof OnboardingActivity) {
                    ((OnboardingActivity) ContextHelper.getBaseActivity(NotificationsViewHolder.this.itemView.getContext())).requestNotificationPermission();
                }
            }
        });
    }
}
