package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.LegacySubredditSelectionDialogBottomSheet;
import com.resync.app.dialogs.RestoreLegacyCommentsDeviceAlertDialogBottomSheet;
import com.resync.app.dialogs.RestoreLegacyDeviceAlertDialogBottomSheet;

/* JADX INFO: renamed from: pa.o0 */
/* JADX INFO: compiled from: PreferencesLegacyFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesLegacyFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.o0$a */
    /* JADX INFO: compiled from: PreferencesLegacyFragment.java */
    class LegacySubredditsClickListener implements Preference.OnPreferenceClickListener {
        LegacySubredditsClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(LegacySubredditSelectionDialogBottomSheet.class, PreferencesLegacyFragment.this.getChildFragmentManager());
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.o0$b */
    /* JADX INFO: compiled from: PreferencesLegacyFragment.java */
    class RestoreLegacyCommentsClickListener implements Preference.OnPreferenceClickListener {
        RestoreLegacyCommentsClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(RestoreLegacyCommentsDeviceAlertDialogBottomSheet.class, PreferencesLegacyFragment.this.getChildFragmentManager());
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.o0$c */
    /* JADX INFO: compiled from: PreferencesLegacyFragment.java */
    class RestoreLegacyClickListener implements Preference.OnPreferenceClickListener {
        RestoreLegacyClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(RestoreLegacyDeviceAlertDialogBottomSheet.class, PreferencesLegacyFragment.this.getChildFragmentManager());
            return false;
        }
    }

    public static PreferencesLegacyFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesLegacyFragment preferencesLegacyFragment = new PreferencesLegacyFragment();
        preferencesLegacyFragment.setArguments(bundle);
        return preferencesLegacyFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_legacy);
        findPreference("legacy_subs").setOnPreferenceClickListener(new LegacySubredditsClickListener());
        findPreference("restore_legacy_comments").setOnPreferenceClickListener(new RestoreLegacyCommentsClickListener());
        findPreference("restore_legacy").setOnPreferenceClickListener(new RestoreLegacyClickListener());
    }
}
