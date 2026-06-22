package com.resync.app.fragments.preferences;

import android.os.Bundle;
import com.resync.app.R;

/* JADX INFO: renamed from: pa.l */
/* JADX INFO: compiled from: PreferencesAnimationsFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesAnimationsFragment extends BasePreferencesFragment {
    public static PreferencesAnimationsFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesAnimationsFragment preferencesAnimationsFragment = new PreferencesAnimationsFragment();
        preferencesAnimationsFragment.setArguments(bundle);
        return preferencesAnimationsFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_animations);
        super.onCreatePreferences(bundle, str);
    }
}
