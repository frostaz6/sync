package com.resync.app.fragments.preferences;

import android.os.Bundle;
import com.resync.app.R;

/* JADX INFO: renamed from: pa.j0 */
/* JADX INFO: compiled from: PreferencesExperimentalFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesExperimentalFragment extends BasePreferencesFragment {
    public static PreferencesExperimentalFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesExperimentalFragment preferencesExperimentalFragment = new PreferencesExperimentalFragment();
        preferencesExperimentalFragment.setArguments(bundle);
        return preferencesExperimentalFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_experimental);
        super.onCreatePreferences(bundle, str);
    }
}
