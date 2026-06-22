package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BypassActivity;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: pa.p0 */
/* JADX INFO: compiled from: PreferencesLinksFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesLinksFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.p0$a */
    /* JADX INFO: compiled from: PreferencesLinksFragment.java */
    class IncognitoHelpClickListener implements Preference.OnPreferenceClickListener {
        IncognitoHelpClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            com.resync.app.util.links.LinkHandler.openWiki(PreferencesLinksFragment.this.getActivity(), "https://www.reddit.com/r/redditsync/wiki/incognito");
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.p0$b */
    /* JADX INFO: compiled from: PreferencesLinksFragment.java */
    class DefaultBrowserChangeListener implements Preference.OnPreferenceChangeListener {
        DefaultBrowserChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            PreferencesLinksFragment.this.updateBrowserSummary(obj);
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.p0$c */
    /* JADX INFO: compiled from: PreferencesLinksFragment.java */
    class BypassBrowserClickListener implements Preference.OnPreferenceClickListener {
        BypassBrowserClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            PreferencesLinksFragment.this.getActivity().startActivity(BypassActivity.createIntent(PreferencesLinksFragment.this.getActivity()));
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.p0$d */
    /* JADX INFO: compiled from: PreferencesLinksFragment.java */
    class YouTubeEmbedChangeListener implements Preference.OnPreferenceChangeListener {
        YouTubeEmbedChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (SettingsSingleton.getSettings().embedYouTube) {
                SettingsSingleton.getSettings().embedYouTube = false;
                SettingsSingleton.getInstance().putBoolean("youtubeembed_preference", false);
                return true;
            }
            SettingsSingleton.getSettings().embedYouTube = true;
            SettingsSingleton.getInstance().putBoolean("youtubeembed_preference", true);
            return true;
        }
    }

    public static PreferencesLinksFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesLinksFragment preferencesLinksFragment = new PreferencesLinksFragment();
        preferencesLinksFragment.setArguments(bundle);
        return preferencesLinksFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBrowserSummary(Object obj) {
        findPreference("default_browser").setSummary(getResources().getStringArray(R.array.browser_types)[Integer.parseInt(obj != null ? (String) obj : PreferencesHelper.getAccountPrefs("PreferencesLinksFragment").getString("default_browser", SettingsSingleton.getInstance().getDefaultBrowserValue()))]);
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_links);
        findPreference("help").setOnPreferenceClickListener(new IncognitoHelpClickListener());
        findPreference("default_browser").setOnPreferenceChangeListener(new DefaultBrowserChangeListener());
        findPreference("bypass_browser").setOnPreferenceClickListener(new BypassBrowserClickListener());
        updateBrowserSummary(null);
        findPreference("youtubeembed_preference").setOnPreferenceChangeListener(new YouTubeEmbedChangeListener());
    }
}
