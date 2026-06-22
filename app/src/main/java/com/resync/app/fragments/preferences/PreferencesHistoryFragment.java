package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.preferences.defaults.SyncCheckBoxPreference;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.managers.TopSubredditHelper;
import com.resync.app.managers.DraftManager;
import com.resync.app.helpers.RecentHelper;
import com.resync.app.helpers.VisitedManager;

/* JADX INFO: renamed from: pa.m0 */
/* JADX INFO: compiled from: PreferencesHistoryFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesHistoryFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.m0$a */
    /* JADX INFO: compiled from: PreferencesHistoryFragment.java */
    class MarkReadChangeListener implements Preference.OnPreferenceChangeListener {
        MarkReadChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (!(obj instanceof Boolean)) {
                return true;
            }
            boolean zBooleanValue = ((Boolean) obj).booleanValue();
            PreferencesHistoryFragment.this.findPreference("history_mark_read_reddit").setVisible(zBooleanValue);
            ((SyncCheckBoxPreference) PreferencesHistoryFragment.this.findPreference("history_mark_read_reddit")).setChecked(zBooleanValue);
            PreferencesHistoryFragment.this.findPreference("history_mark_read_on_scroll").setVisible(zBooleanValue);
            if (!zBooleanValue) {
                ((SyncCheckBoxPreference) PreferencesHistoryFragment.this.findPreference("history_mark_read_on_scroll")).setChecked(false);
            }
            PreferencesHistoryFragment.this.findPreference("viewed_peek").setVisible(zBooleanValue);
            if (!zBooleanValue) {
                ((SyncCheckBoxPreference) PreferencesHistoryFragment.this.findPreference("viewed_peek")).setChecked(false);
            }
            SettingsSingleton.getSettings().markAsReadSync = zBooleanValue;
            SettingsSingleton.getSettings().markAsReadOnScroll = zBooleanValue;
            SettingsSingleton.getSettings().viewedPeek = zBooleanValue;
            SettingsSingleton.getInstance().putBoolean("history_mark_read_reddit", zBooleanValue);
            SettingsSingleton.getInstance().putBoolean("history_mark_read_on_scroll", zBooleanValue);
            SettingsSingleton.getInstance().putBoolean("viewed_peek", zBooleanValue);
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.m0$b */
    /* JADX INFO: compiled from: PreferencesHistoryFragment.java */
    class ClearAllClickListener implements Preference.OnPreferenceClickListener {
        ClearAllClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            RecentHelper.clear();
            VisitedManager.clearVisited();
            DraftManager.clearDrafts();
            TopSubredditHelper.clearTopSubreddits();
            VisitedManager.clearVisited();
            StaticToast.showWithContext(PreferencesHistoryFragment.this.getActivity(), "All clean!");
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.m0$c */
    /* JADX INFO: compiled from: PreferencesHistoryFragment.java */
    class ClearRecentsClickListener implements Preference.OnPreferenceClickListener {
        ClearRecentsClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            RecentHelper.clear();
            StaticToast.showWithContext(PreferencesHistoryFragment.this.getActivity(), "Recents cleared!");
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.m0$d */
    /* JADX INFO: compiled from: PreferencesHistoryFragment.java */
    class ClearSubsClickListener implements Preference.OnPreferenceClickListener {
        ClearSubsClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            VisitedManager.clearVisited();
            StaticToast.showWithContext(PreferencesHistoryFragment.this.getActivity(), "Viewed subreddits cleared!");
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.m0$e */
    /* JADX INFO: compiled from: PreferencesHistoryFragment.java */
    class ClearDraftsClickListener implements Preference.OnPreferenceClickListener {
        ClearDraftsClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            DraftManager.clearDrafts();
            StaticToast.showWithContext(PreferencesHistoryFragment.this.getActivity(), "Drafts cleared!");
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.m0$f */
    /* JADX INFO: compiled from: PreferencesHistoryFragment.java */
    class ClearVisitedClickListener implements Preference.OnPreferenceClickListener {
        ClearVisitedClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            TopSubredditHelper.clearTopSubreddits();
            StaticToast.showWithContext(PreferencesHistoryFragment.this.getActivity(), "Visited count cleared!");
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.m0$g */
    /* JADX INFO: compiled from: PreferencesHistoryFragment.java */
    class ClearSearchClickListener implements Preference.OnPreferenceClickListener {
        ClearSearchClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            VisitedManager.clearVisited();
            StaticToast.showWithContext(PreferencesHistoryFragment.this.getActivity(), "Search history cleared!");
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.m0$h */
    /* JADX INFO: compiled from: PreferencesHistoryFragment.java */
    class RecordHistoryChangeListener implements Preference.OnPreferenceChangeListener {
        RecordHistoryChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            VisitedManager.clearVisited();
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.m0$i */
    /* JADX INFO: compiled from: PreferencesHistoryFragment.java */
    class RecordTopSubredditsChangeListener implements Preference.OnPreferenceChangeListener {
        RecordTopSubredditsChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (!(obj instanceof Boolean) || ((Boolean) obj).booleanValue()) {
                return true;
            }
            TopSubredditHelper.clearTopSubreddits();
            return true;
        }
    }

    public static PreferencesHistoryFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesHistoryFragment preferencesHistoryFragment = new PreferencesHistoryFragment();
        preferencesHistoryFragment.setArguments(bundle);
        return preferencesHistoryFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_history);
        if (!SettingsSingleton.getSettings().markAsRead) {
            findPreference("history_mark_read_reddit").setVisible(false);
            findPreference("history_mark_read_on_scroll").setVisible(false);
            findPreference("viewed_peek").setVisible(false);
        }
        findPreference("history_mark_read").setOnPreferenceChangeListener(new MarkReadChangeListener());
        findPreference("history_all").setOnPreferenceClickListener(new ClearAllClickListener());
        findPreference("history_recents_clear").setOnPreferenceClickListener(new ClearRecentsClickListener());
        findPreference("history_subs_clear").setOnPreferenceClickListener(new ClearSubsClickListener());
        findPreference("drafts_clear").setOnPreferenceClickListener(new ClearDraftsClickListener());
        findPreference("history_visited_clear").setOnPreferenceClickListener(new ClearVisitedClickListener());
        findPreference("search_clear").setOnPreferenceClickListener(new ClearSearchClickListener());
        findPreference("record_history").setOnPreferenceChangeListener(new RecordHistoryChangeListener());
        findPreference("record_top_subreddits").setOnPreferenceChangeListener(new RecordTopSubredditsChangeListener());
    }
}
