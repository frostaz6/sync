package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;

/* JADX INFO: renamed from: pa.e1 */
/* JADX INFO: compiled from: PreferencesTabletFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesTabletFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.e1$a */
    /* JADX INFO: compiled from: PreferencesTabletFragment.java */
    class TabletModeChangeListener implements Preference.OnPreferenceChangeListener {
        TabletModeChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            Boolean bool = (Boolean) obj;
            PreferencesTabletFragment.this.findPreference("tablet_options").setVisible(bool.booleanValue());
            PreferencesTabletFragment.this.findPreference("tablet_options_2").setVisible(bool.booleanValue());
            return true;
        }
    }

    public static PreferencesTabletFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesTabletFragment preferencesTabletFragment = new PreferencesTabletFragment();
        preferencesTabletFragment.setArguments(bundle);
        return preferencesTabletFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_tablet);
        super.onCreatePreferences(bundle, str);
        findPreference("tablet_options").setVisible(isTabletMode());
        findPreference("tablet_options_2").setVisible(isTabletMode());
        findPreference("tablet_mode").setOnPreferenceChangeListener(new TabletModeChangeListener());
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public SimplePreference[] getSummaryPreferences() {
        return new SimplePreference[]{new SimplePreference("rail_gravity", "0", R.array.rail_gravity_labels)};
    }
}
