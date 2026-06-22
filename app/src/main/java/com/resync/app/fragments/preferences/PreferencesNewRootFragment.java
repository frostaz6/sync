package com.resync.app.fragments.preferences;

import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;

/* JADX INFO: renamed from: pa.u0 */
/* JADX INFO: compiled from: PreferencesNewRootFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesNewRootFragment extends BasePreferencesFragment {
    public static PreferencesNewRootFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesNewRootFragment preferencesNewRootFragment = new PreferencesNewRootFragment();
        preferencesNewRootFragment.setArguments(bundle);
        return preferencesNewRootFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_root);
        super.onCreatePreferences(bundle, str);
        // ReSync: developer options are always visible (owner's own build).
        findPreference(Integer.toString(RedditApplication.getInteger(R.integer.DEVELOPER_OPTIONS))).setVisible(true);
        Integer.toString(RedditApplication.getInteger(R.integer.LEGACY));
        findPreference(Integer.toString(RedditApplication.getInteger(R.integer.PURCHASES))).setVisible(true);
        if (!AccountSingleton.isLoggedIn(getContext())) {
            findPreference(Integer.toString(RedditApplication.getInteger(R.integer.WEB_PREFERENCES))).setVisible(false);
            findPreference(Integer.toString(RedditApplication.getInteger(R.integer.MESSAGING))).setVisible(false);
        }
        if (RemoteConfigHelper.isC4266fa() && UltraHelper.isUltra()) {
            String string = PreferencesHelper.getDefaultPrefs().getString(KeyManager.purchaseTokenKey(), null);
            if (StringUtils.isEmpty(string) || !StringUtils.containsIgnoreCase(string, ".AO-")) {
                findPreference(Integer.toString(RedditApplication.getInteger(R.integer.ULTRA))).setVisible(false);
            }
        }
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public void onResetSettings() {
    }
}
