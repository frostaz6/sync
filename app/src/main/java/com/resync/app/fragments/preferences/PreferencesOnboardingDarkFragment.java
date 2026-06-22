package com.resync.app.fragments.preferences;

import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: qa.a */
/* JADX INFO: compiled from: PreferencesOnboardingDarkFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesOnboardingDarkFragment extends BasePreferencesFragment {
    public static PreferencesOnboardingDarkFragment newInstance() {
        return new PreferencesOnboardingDarkFragment();
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_onboarding_dark);
        super.onCreatePreferences(bundle, str);
    }
}
