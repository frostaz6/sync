package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.preferences.defaults.SyncRadioButtonPreference;

/* JADX INFO: renamed from: pa.n */
/* JADX INFO: compiled from: PreferencesAwardsFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesAwardsFragment extends BasePreferencesFragment {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean onAwardStyleClick(SyncRadioButtonPreference syncRadioButtonPreference, SyncRadioButtonPreference syncRadioButtonPreference2, SyncRadioButtonPreference syncRadioButtonPreference3, Preference preference) {
        if (preference.equals(syncRadioButtonPreference)) {
            SettingsSingleton.getSettings().awardsModern = true;
            SettingsSingleton.getSettings().awardsMinimal = false;
            SettingsSingleton.getSettings().awardsNone = false;
            syncRadioButtonPreference.setChecked(true);
            syncRadioButtonPreference2.setChecked(false);
            syncRadioButtonPreference3.setChecked(false);
        } else if (preference.equals(syncRadioButtonPreference2)) {
            SettingsSingleton.getSettings().awardsModern = false;
            SettingsSingleton.getSettings().awardsMinimal = true;
            SettingsSingleton.getSettings().awardsNone = false;
            syncRadioButtonPreference.setChecked(false);
            syncRadioButtonPreference2.setChecked(true);
            syncRadioButtonPreference3.setChecked(false);
        } else if (preference.equals(syncRadioButtonPreference3)) {
            SettingsSingleton.getSettings().awardsModern = false;
            SettingsSingleton.getSettings().awardsMinimal = false;
            SettingsSingleton.getSettings().awardsNone = true;
            syncRadioButtonPreference.setChecked(false);
            syncRadioButtonPreference2.setChecked(false);
            syncRadioButtonPreference3.setChecked(true);
        }
        SettingsSingleton.getInstance().putBoolean(syncRadioButtonPreference.getKey(), syncRadioButtonPreference.isChecked());
        SettingsSingleton.getInstance().putBoolean(syncRadioButtonPreference2.getKey(), syncRadioButtonPreference2.isChecked());
        SettingsSingleton.getInstance().putBoolean(syncRadioButtonPreference3.getKey(), syncRadioButtonPreference3.isChecked());
        return true;
    }

    public static PreferencesAwardsFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesAwardsFragment preferencesAwardsFragment = new PreferencesAwardsFragment();
        preferencesAwardsFragment.setArguments(bundle);
        return preferencesAwardsFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_awards);
        super.onCreatePreferences(bundle, str);
        final SyncRadioButtonPreference syncRadioButtonPreference = (SyncRadioButtonPreference) findPreference("awards_modern");
        final SyncRadioButtonPreference syncRadioButtonPreference2 = (SyncRadioButtonPreference) findPreference("awards_minimal");
        final SyncRadioButtonPreference syncRadioButtonPreference3 = (SyncRadioButtonPreference) findPreference("awards_none");
        syncRadioButtonPreference.setChecked(SettingsSingleton.getSettings().awardsModern);
        syncRadioButtonPreference2.setChecked(SettingsSingleton.getSettings().awardsMinimal);
        syncRadioButtonPreference3.setChecked(SettingsSingleton.getSettings().awardsNone);
        Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() { // from class: pa.m
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesAwardsFragment.onAwardStyleClick(syncRadioButtonPreference, syncRadioButtonPreference2, syncRadioButtonPreference3, preference);
            }
        };
        syncRadioButtonPreference.setOnPreferenceClickListener(clickListener);
        syncRadioButtonPreference2.setOnPreferenceClickListener(clickListener);
        syncRadioButtonPreference3.setOnPreferenceClickListener(clickListener);
    }
}
