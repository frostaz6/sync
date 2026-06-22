package com.resync.app.ui.viewholders.onboarding.pages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.resync.app.R;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.activities.OnboardingActivity;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.LandingViewHolder;
import com.resync.app.helpers.ChangelogHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.ui.activities.WebViewActivity;

/* JADX INFO: loaded from: classes3.dex */
public class LandingViewHolder extends AbstractOnboardingViewHolder {
    public LandingViewHolder(final ViewGroup viewGroup) {
        super(viewGroup);
        ((RelativeLayout) this.itemView.findViewById(R.id.main_content)).removeAllViews();
        LayoutInflater.from(this.itemView.getContext()).inflate(R.layout.viewholder_landing, (ViewGroup) this.itemView.findViewById(R.id.main_content), true);
        ((TextView) this.itemView.findViewById(R.id.title)).setOnLongClickListener(new View.OnLongClickListener() { // from class: sb.c
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return LandingViewHolder.lambda$new$0(viewGroup, view);
            }
        });
        TextView textView = (TextView) this.itemView.findViewById(R.id.info);
        textView.setText("v23.06.30-13:39 (23033) • Privacy policy");
        textView.setOnClickListener(new View.OnClickListener() { // from class: sb.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LandingViewHolder.this.lambda$new$1(view);
            }
        });
        animate((ImageView) this.itemView.findViewById(R.id.logo_top));
        animate((ImageView) this.itemView.findViewById(R.id.logo_bottom));
        TextView textView2 = (TextView) this.itemView.findViewById(R.id.description);
        if (ChangelogHelper.isFirstLaunch(this.itemView.getContext())) {
            textView2.setText("Let's get started!");
        } else {
            textView2.setText("ReSync has had an update!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$0(ViewGroup viewGroup, View view) {
        BaseActivity baseActivityA = ContextHelper.getBaseActivity(viewGroup.getContext());
        if (!(baseActivityA instanceof OnboardingActivity)) {
            return false;
        }
        ((OnboardingActivity) baseActivityA).finishAfterDelay(150);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        // ReSync: bundled privacy policy + terms (original iubenda link is dead).
        ContextHelper.getBaseActivity(this.itemView.getContext()).startActivity(WebViewActivity.createIntent(this.itemView.getContext(), "file:///android_asset/legal.html"));
    }

    public void animate(final ImageView imageView) {
        imageView.animate().translationX(0.0f).translationY(0.0f).setDuration(8000L).setInterpolator(new CycleInterpolator(1.0f)).setListener(new AnimatorListenerAdapter() { // from class: com.resync.app.ui.viewholders.onboarding.pages.LandingViewHolder.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ImageView imageView2 = imageView;
                if (imageView2 == null || ContextHelper.isActivityFinishing(imageView2.getContext())) {
                    return;
                }
                LandingViewHolder.this.animate(imageView);
            }
        }).start();
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public boolean applyTabletPadding() {
        return false;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return 0;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return null;
    }
}
