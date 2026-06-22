package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.preferences.defaults.SyncCheckBoxPreference;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: pa.y0 */
/* JADX INFO: compiled from: PreferencesPostsFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesPostsFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.y0$a */
    /* JADX INFO: compiled from: PreferencesPostsFragment.java */
    class SwipeHideChangeListener implements Preference.OnPreferenceChangeListener {
        SwipeHideChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (!((Boolean) obj).booleanValue()) {
                return true;
            }
            ((SyncCheckBoxPreference) PreferencesPostsFragment.this.findPreference("post_swipe_actions")).setChecked(false);
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.y0$b */
    /* JADX INFO: compiled from: PreferencesPostsFragment.java */
    class PostSwipeChangeListener implements Preference.OnPreferenceChangeListener {
        PostSwipeChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (!((Boolean) obj).booleanValue()) {
                return true;
            }
            ((SyncCheckBoxPreference) PreferencesPostsFragment.this.findPreference("swipe_hide_posts_new")).setChecked(false);
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.y0$c */
    /* JADX INFO: compiled from: PreferencesPostsFragment.java */
    class PostSortChangeListener implements Preference.OnPreferenceChangeListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Preference sortPreference;

        PostSortChangeListener(Preference preference) {
            this.sortPreference = preference;
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            PreferencesPostsFragment.this.updatePostSortSummary(this.sortPreference, (String) obj);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: h4 */
    public static /* synthetic */ boolean onFlairClick(SyncCheckBoxPreference syncCheckBoxPreference, SyncCheckBoxPreference syncCheckBoxPreference2, SyncCheckBoxPreference syncCheckBoxPreference3, Preference preference) {
        if (preference.equals(syncCheckBoxPreference)) {
            SettingsSingleton.getSettings().flairModern = true;
            SettingsSingleton.getSettings().flairMinimal = false;
            SettingsSingleton.getSettings().flairNone = false;
            syncCheckBoxPreference.setChecked(true);
            syncCheckBoxPreference2.setChecked(false);
            syncCheckBoxPreference3.setChecked(false);
        } else if (preference.equals(syncCheckBoxPreference2)) {
            SettingsSingleton.getSettings().flairModern = false;
            SettingsSingleton.getSettings().flairMinimal = true;
            SettingsSingleton.getSettings().flairNone = false;
            syncCheckBoxPreference.setChecked(false);
            syncCheckBoxPreference2.setChecked(true);
            syncCheckBoxPreference3.setChecked(false);
        } else if (preference.equals(syncCheckBoxPreference3)) {
            SettingsSingleton.getSettings().flairModern = false;
            SettingsSingleton.getSettings().flairMinimal = false;
            SettingsSingleton.getSettings().flairNone = true;
            syncCheckBoxPreference.setChecked(false);
            syncCheckBoxPreference2.setChecked(false);
            syncCheckBoxPreference3.setChecked(true);
        }
        SettingsSingleton.getInstance().putBoolean(syncCheckBoxPreference.getKey(), syncCheckBoxPreference.isChecked());
        SettingsSingleton.getInstance().putBoolean(syncCheckBoxPreference2.getKey(), syncCheckBoxPreference2.isChecked());
        SettingsSingleton.getInstance().putBoolean(syncCheckBoxPreference3.getKey(), syncCheckBoxPreference3.isChecked());
        return true;
    }

    public static PreferencesPostsFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesPostsFragment preferencesPostsFragment = new PreferencesPostsFragment();
        preferencesPostsFragment.setArguments(bundle);
        return preferencesPostsFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: j4 */
    public void updatePostSortSummary(Preference preference, String str) {
        String[] stringArray = getResources().getStringArray(R.array.post_sort_values);
        String[] stringArray2 = getResources().getStringArray(R.array.post_sort);
        for (int i = 0; i < stringArray.length; i++) {
            if (StringUtils.equals(stringArray[i], str)) {
                preference.setSummary(stringArray2[i]);
            }
        }
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_posts);
        super.onCreatePreferences(bundle, str);
        findPreference("swipe_hide_posts_new").setOnPreferenceChangeListener(new SwipeHideChangeListener());
        findPreference("post_swipe_actions").setOnPreferenceChangeListener(new PostSwipeChangeListener());
        Preference preferenceY = findPreference("post_sort_preference");
        preferenceY.setOnPreferenceChangeListener(new PostSortChangeListener(preferenceY));
        updatePostSortSummary(preferenceY, PreferencesHelper.getAccountPrefs("PreferencesPostsFragment").getString("post_sort_preference", "sort_0"));
        final SyncCheckBoxPreference syncCheckBoxPreference = (SyncCheckBoxPreference) findPreference("flair_modern");
        final SyncCheckBoxPreference syncCheckBoxPreference2 = (SyncCheckBoxPreference) findPreference("flair_minimal");
        final SyncCheckBoxPreference syncCheckBoxPreference3 = (SyncCheckBoxPreference) findPreference("flair_none");
        syncCheckBoxPreference.setChecked(SettingsSingleton.getSettings().flairModern);
        syncCheckBoxPreference2.setChecked(SettingsSingleton.getSettings().flairMinimal);
        syncCheckBoxPreference3.setChecked(SettingsSingleton.getSettings().flairNone);
        Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() { // from class: pa.x0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesPostsFragment.onFlairClick(syncCheckBoxPreference, syncCheckBoxPreference2, syncCheckBoxPreference3, preference);
            }
        };
        syncCheckBoxPreference.setOnPreferenceClickListener(clickListener);
        syncCheckBoxPreference2.setOnPreferenceClickListener(clickListener);
        syncCheckBoxPreference3.setOnPreferenceClickListener(clickListener);
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public SimplePreference[] getSummaryPreferences() {
        return new SimplePreference[]{new SimplePreference("post_navigation_first", "0", R.array.post_swipe), new SimplePreference("post_navigation_second", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE, R.array.post_swipe), new SimplePreference("post_navigation_third", "5", R.array.post_swipe)};
    }
}
