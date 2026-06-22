package com.resync.app.ui.viewholders.onboarding.pages;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.resync.app.R;
import com.resync.app.ui.activities.WebViewActivity;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;

/* JADX INFO: loaded from: classes3.dex */
public class TermsViewHolder extends AbstractOnboardingViewHolder {
    public TermsViewHolder(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public String getDescription() {
        return "Before we go any further you must agree to our privacy policy and app terms before proceeding.";
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return R.drawable.outline_privacy_tip_24;
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public int getInnerContentRes() {
        return R.layout.viewholder_inner_terms;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return "Terms & Conditions";
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    protected void setup() {
        ((FrameLayout.LayoutParams) this.itemView.findViewById(R.id.tooltip_one).findViewById(R.id.tooltip_card).getLayoutParams()).topMargin = 0;
        this.itemView.findViewById(R.id.tooltip_one).setOnClickListener(new View.OnClickListener() { // from class: com.resync.app.ui.viewholders.onboarding.pages.TermsViewHolder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                // ReSync: privacy policy + terms are bundled locally (the original iubenda link is dead).
                view.getContext().startActivity(WebViewActivity.createIntent(view.getContext(), "file:///android_asset/legal.html"));
            }
        });
    }
}
