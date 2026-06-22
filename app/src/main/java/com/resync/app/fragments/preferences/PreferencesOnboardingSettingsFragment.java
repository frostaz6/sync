package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.preferences.defaults.SyncCheckBoxPreference;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: qa.c */
/* JADX INFO: compiled from: PreferencesOnboardingSettingsFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesOnboardingSettingsFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: qa.c$a */
    /* JADX INFO: compiled from: PreferencesOnboardingSettingsFragment.java */
    class FlairClickListener implements Preference.OnPreferenceClickListener {
        FlairClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (preference instanceof SyncCheckBoxPreference) {
                if (((SyncCheckBoxPreference) preference).isChecked()) {
                    SettingsSingleton.getSettings().flairModern = true;
                    SettingsSingleton.getSettings().flairMinimal = false;
                    SettingsSingleton.getSettings().flairNone = false;
                    SettingsSingleton.getInstance().putBoolean("flair_modern", true);
                    SettingsSingleton.getInstance().putBoolean("flair_minimal", false);
                    SettingsSingleton.getInstance().putBoolean("flair_none", false);
                } else {
                    SettingsSingleton.getSettings().flairModern = false;
                    SettingsSingleton.getSettings().flairMinimal = true;
                    SettingsSingleton.getSettings().flairNone = false;
                    SettingsSingleton.getInstance().putBoolean("flair_modern", false);
                    SettingsSingleton.getInstance().putBoolean("flair_minimal", true);
                    SettingsSingleton.getInstance().putBoolean("flair_none", false);
                }
            }
            return false;
        }
    }

    /* JADX INFO: renamed from: qa.c$b */
    /* JADX INFO: compiled from: PreferencesOnboardingSettingsFragment.java */
    class AwardsClickListener implements Preference.OnPreferenceClickListener {
        AwardsClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (preference instanceof SyncCheckBoxPreference) {
                if (((SyncCheckBoxPreference) preference).isChecked()) {
                    SettingsSingleton.getSettings().awardsModern = true;
                    SettingsSingleton.getSettings().awardsMinimal = false;
                    SettingsSingleton.getSettings().awardsNone = false;
                    SettingsSingleton.getInstance().putBoolean("awards_modern", true);
                    SettingsSingleton.getInstance().putBoolean("awards_minimal", false);
                    SettingsSingleton.getInstance().putBoolean("awards_none", false);
                } else {
                    SettingsSingleton.getSettings().awardsModern = false;
                    SettingsSingleton.getSettings().awardsMinimal = true;
                    SettingsSingleton.getSettings().awardsNone = false;
                    SettingsSingleton.getInstance().putBoolean("awards_modern", false);
                    SettingsSingleton.getInstance().putBoolean("awards_minimal", true);
                    SettingsSingleton.getInstance().putBoolean("awards_none", false);
                }
            }
            return false;
        }
    }

    public static PreferencesOnboardingSettingsFragment newInstance() {
        return new PreferencesOnboardingSettingsFragment();
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_onboarding_settings);
        super.onCreatePreferences(bundle, str);
        ((SyncCheckBoxPreference) findPreference("onboarding_flair")).setChecked(SettingsSingleton.getSettings().flairModern);
        ((SyncCheckBoxPreference) findPreference("onboarding_awards")).setChecked(SettingsSingleton.getSettings().awardsModern);
        findPreference("onboarding_flair").setOnPreferenceClickListener(new FlairClickListener());
        findPreference("onboarding_awards").setOnPreferenceClickListener(new AwardsClickListener());
    }
}
