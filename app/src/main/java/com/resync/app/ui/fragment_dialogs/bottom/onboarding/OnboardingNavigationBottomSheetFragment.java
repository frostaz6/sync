package com.resync.app.ui.fragment_dialogs.bottom.onboarding;

import android.view.View;
import butterknife.BindView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.resync.app.R;
import com.resync.app.fragments.AbstractOnboardingNavigationFragment;

/* JADX INFO: loaded from: classes.dex */
public class OnboardingNavigationBottomSheetFragment extends AbstractOnboardingNavigationFragment {

    @BindView(R.id.fragment_onboarding_navigation_bottom_button)
    MaterialRadioButton buttonNavigationBottom;

    @BindView(R.id.fragment_onboarding_navigation_classic_button)
    MaterialRadioButton buttonNavigationClassic;

    @BindView(R.id.fragment_onboarding_navigation_bottom)
    View viewNavigationBottom;

    @BindView(R.id.fragment_onboarding_navigation_classic)
    View viewNavigationClassic;

    @Override // com.resync.app.fragments.AbstractOnboardingNavigationFragment
    public int getLayoutResId() {
        return R.layout.fragment_onboarding_navigation;
    }
}
