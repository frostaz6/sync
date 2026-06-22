package com.resync.app.ui.viewholders.onboarding.pages;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.resync.app.R;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;

/* JADX INFO: loaded from: classes3.dex */
public class FinallyViewHolder extends AbstractOnboardingViewHolder {
    public FinallyViewHolder(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public String getDescription() {
        return "Although it might seem like a lot has changed, you can customize nearly every aspect of ReSync from the settings.";
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return R.drawable.ic_outline_sync_24;
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public int getInnerContentRes() {
        return R.layout.viewholder_inner_finally;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return "And finally...";
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    protected void setup() {
        ((FrameLayout.LayoutParams) this.itemView.findViewById(R.id.tooltip_one).findViewById(R.id.tooltip_card).getLayoutParams()).topMargin = 0;
        ((FrameLayout.LayoutParams) this.itemView.findViewById(R.id.tooltip_two).findViewById(R.id.tooltip_card).getLayoutParams()).topMargin = 0;
    }
}
