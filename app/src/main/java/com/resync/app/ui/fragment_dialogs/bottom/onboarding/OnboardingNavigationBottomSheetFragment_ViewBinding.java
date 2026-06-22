package com.resync.app.ui.fragment_dialogs.bottom.onboarding;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class OnboardingNavigationBottomSheetFragment_ViewBinding implements Unbinder {
    private OnboardingNavigationBottomSheetFragment target;

    public OnboardingNavigationBottomSheetFragment_ViewBinding(OnboardingNavigationBottomSheetFragment onboardingNavigationBottomSheetFragment, View view) {
        this.target = onboardingNavigationBottomSheetFragment;
        onboardingNavigationBottomSheetFragment.viewNavigationBottom = Utils.findRequiredView(view, R.id.fragment_onboarding_navigation_bottom, "field 'viewNavigationBottom'");
        onboardingNavigationBottomSheetFragment.viewNavigationClassic = Utils.findRequiredView(view, R.id.fragment_onboarding_navigation_classic, "field 'viewNavigationClassic'");
        onboardingNavigationBottomSheetFragment.buttonNavigationBottom = (MaterialRadioButton) Utils.findRequiredViewAsType(view, R.id.fragment_onboarding_navigation_bottom_button, "field 'buttonNavigationBottom'", MaterialRadioButton.class);
        onboardingNavigationBottomSheetFragment.buttonNavigationClassic = (MaterialRadioButton) Utils.findRequiredViewAsType(view, R.id.fragment_onboarding_navigation_classic_button, "field 'buttonNavigationClassic'", MaterialRadioButton.class);
    }

    @Override
    public void unbind() {
        OnboardingNavigationBottomSheetFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.buttonNavigationBottom = null;
        target.buttonNavigationClassic = null;
    }
}
