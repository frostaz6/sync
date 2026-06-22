package com.resync.app.ui.viewholders.onboarding.pages;

import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.resync.app.R;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.fragments.preferences.PreferencesOnboardingDarkFragment;

/* JADX INFO: loaded from: classes3.dex */
public class DarkModeViewHolder extends AbstractOnboardingViewHolder {
    public DarkModeViewHolder(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public String getDescription() {
        return null;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return R.drawable.outline_dark_mode_24;
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public int getInnerContentRes() {
        return R.layout.viewholder_inner_dark;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return "Dark mode";
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    protected void setup() {
        this.itemView.findViewById(R.id.dark_content).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.resync.app.ui.viewholders.onboarding.pages.DarkModeViewHolder.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                DarkModeViewHolder.this.itemView.findViewById(R.id.dark_content).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ContextHelper.getFragmentManager(DarkModeViewHolder.this.itemView.getContext()).beginTransaction().replace(R.id.dark_content, PreferencesOnboardingDarkFragment.newInstance()).commit();
            }
        });
    }
}
