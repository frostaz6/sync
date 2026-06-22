package com.resync.app.ui.activities;

import android.view.View;
import android.widget.TextView;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class OnboardingActivity_ViewBinding implements Unbinder {
    private OnboardingActivity target;

    public OnboardingActivity_ViewBinding(OnboardingActivity onboardingActivity, View view) {
        this.target = onboardingActivity;
        onboardingActivity.pager = (ViewPager2) Utils.findRequiredViewAsType(view, R.id.view_pager, "field 'pager'", ViewPager2.class);
        onboardingActivity.back = (TextView) Utils.findRequiredViewAsType(view, R.id.back, "field 'back'", TextView.class);
        onboardingActivity.next = (TextView) Utils.findRequiredViewAsType(view, R.id.next, "field 'next'", TextView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        OnboardingActivity onboardingActivity = this.target;
        if (onboardingActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        onboardingActivity.pager = null;
        onboardingActivity.back = null;
        onboardingActivity.next = null;
    }
}
