package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.ui.activities.WebViewActivity;

/* JADX INFO: renamed from: pa.k */
/* JADX INFO: compiled from: PreferencesAnalyticsFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesAnalyticsFragment extends BasePreferencesFragment {

    class PrivacyPolicyClickListener implements Preference.OnPreferenceClickListener {
        PrivacyPolicyClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            // ReSync: bundled privacy policy + terms (original iubenda link is dead).
            PreferencesAnalyticsFragment.this.getActivity().startActivity(WebViewActivity.createIntent(PreferencesAnalyticsFragment.this.getActivity(), "file:///android_asset/legal.html"));
            return true;
        }
    }

    public static PreferencesAnalyticsFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesAnalyticsFragment preferencesAnalyticsFragment = new PreferencesAnalyticsFragment();
        preferencesAnalyticsFragment.setArguments(bundle);
        return preferencesAnalyticsFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_analytics);
        findPreference("licenses_privacy").setOnPreferenceClickListener(new PrivacyPolicyClickListener());
    }
}
