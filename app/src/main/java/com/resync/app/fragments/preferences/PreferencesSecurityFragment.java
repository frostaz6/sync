package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.biometric.BiometricManager;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.DohDeviceAlertDialogBottomSheet;

/* JADX INFO: renamed from: pa.d1 */
/* JADX INFO: compiled from: PreferencesSecurityFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesSecurityFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.d1$a */
    /* JADX INFO: compiled from: PreferencesSecurityFragment.java */
    class DohChangeListener implements Preference.OnPreferenceChangeListener {
        DohChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            FragmentUtils.show(DohDeviceAlertDialogBottomSheet.class, PreferencesSecurityFragment.this.getFragmentManager());
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onBiometricStartChange(Preference preference, Object obj) {
        if (BiometricManager.from(getActivity()).canAuthenticate() == 0) {
            return true;
        }
        StaticToast.showWithContext(getActivity(), "This feature is unavailable on your device");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onBiometricChange(Preference preference, Object obj) {
        if (BiometricManager.from(getActivity()).canAuthenticate() == 0) {
            return true;
        }
        StaticToast.showWithContext(getActivity(), "This feature is unavailable on your device");
        return false;
    }

    public static PreferencesSecurityFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesSecurityFragment preferencesSecurityFragment = new PreferencesSecurityFragment();
        preferencesSecurityFragment.setArguments(bundle);
        return preferencesSecurityFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_security);
        findPreference("biometric_start").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: pa.b1
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesSecurityFragment.this.onBiometricStartChange(preference, obj);
            }
        });
        findPreference("biometric").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: pa.c1
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesSecurityFragment.this.onBiometricChange(preference, obj);
            }
        });
        findPreference("doh").setOnPreferenceChangeListener(new DohChangeListener());
    }
}
