package com.resync.app.ui.viewholders.onboarding.pages;

import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.resync.app.R;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.fragments.preferences.PreferencesOnboardingSettingsFragment;

/* JADX INFO: loaded from: classes3.dex */
public class SettingsViewHolder extends AbstractOnboardingViewHolder {
    public SettingsViewHolder(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public String getDescription() {
        return "ReSync has more than the average number of settings but here are a final few to check before starting.";
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return R.drawable.outline_settings_24;
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public int getInnerContentRes() {
        return R.layout.viewholder_inner_settings;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return "Settings";
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    protected void setup() {
        this.itemView.findViewById(R.id.settings_content).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.resync.app.ui.viewholders.onboarding.pages.SettingsViewHolder.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                SettingsViewHolder.this.itemView.findViewById(R.id.settings_content).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ContextHelper.getFragmentManager(SettingsViewHolder.this.itemView.getContext()).beginTransaction().replace(R.id.settings_content, PreferencesOnboardingSettingsFragment.newInstance()).commit();
            }
        });
    }
}
