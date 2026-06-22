package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.preferences.defaults.SyncCheckBoxPreference;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: qa.d */
/* JADX INFO: compiled from: PreferencesOnboardingTabletFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesOnboardingTabletFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: qa.d$a */
    /* JADX INFO: compiled from: PreferencesOnboardingTabletFragment.java */
    class TabletModeClickListener implements Preference.OnPreferenceClickListener {
        TabletModeClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (!(preference instanceof SyncCheckBoxPreference)) {
                return false;
            }
            boolean checked = ((SyncCheckBoxPreference) preference).isChecked();
            SettingsSingleton.getSettings().tabletMode = checked;
            SettingsSingleton.getInstance().putBoolean("tablet_mode", checked);
            return false;
        }
    }

    public static PreferencesOnboardingTabletFragment newInstance() {
        return new PreferencesOnboardingTabletFragment();
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_onboarding_tablet);
        super.onCreatePreferences(bundle, str);
        ((SyncCheckBoxPreference) findPreference("onboarding_tablet")).setChecked(SettingsSingleton.getSettings().tabletMode);
        findPreference("onboarding_tablet").setOnPreferenceClickListener(new TabletModeClickListener());
    }
}
