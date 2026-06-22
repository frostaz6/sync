package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.ui.activities.FilterActivity;

/* JADX INFO: renamed from: pa.k0 */
/* JADX INFO: compiled from: PreferencesFiltersFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesFiltersFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.k0$a */
    /* JADX INFO: compiled from: PreferencesFiltersFragment.java */
    class KeywordsClickListener implements Preference.OnPreferenceClickListener {
        KeywordsClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            PreferencesFiltersFragment.this.getActivity().startActivity(FilterActivity.createIntent(PreferencesFiltersFragment.this.getActivity(), 0));
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.k0$b */
    /* JADX INFO: compiled from: PreferencesFiltersFragment.java */
    class DomainsClickListener implements Preference.OnPreferenceClickListener {
        DomainsClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            PreferencesFiltersFragment.this.getActivity().startActivity(FilterActivity.createIntent(PreferencesFiltersFragment.this.getActivity(), 1));
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.k0$c */
    /* JADX INFO: compiled from: PreferencesFiltersFragment.java */
    class SubredditsClickListener implements Preference.OnPreferenceClickListener {
        SubredditsClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            PreferencesFiltersFragment.this.getActivity().startActivity(FilterActivity.createIntent(PreferencesFiltersFragment.this.getActivity(), 2));
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.k0$d */
    /* JADX INFO: compiled from: PreferencesFiltersFragment.java */
    class UserClickListener implements Preference.OnPreferenceClickListener {
        UserClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            PreferencesFiltersFragment.this.getActivity().startActivity(FilterActivity.createIntent(PreferencesFiltersFragment.this.getActivity(), 3));
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.k0$e */
    /* JADX INFO: compiled from: PreferencesFiltersFragment.java */
    class FlairClickListener implements Preference.OnPreferenceClickListener {
        FlairClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            PreferencesFiltersFragment.this.getActivity().startActivity(FilterActivity.createIntent(PreferencesFiltersFragment.this.getActivity(), 4));
            return true;
        }
    }

    public static PreferencesFiltersFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesFiltersFragment preferencesFiltersFragment = new PreferencesFiltersFragment();
        preferencesFiltersFragment.setArguments(bundle);
        return preferencesFiltersFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_filters);
        findPreference("filters_keywords").setOnPreferenceClickListener(new KeywordsClickListener());
        findPreference("filters_domains").setOnPreferenceClickListener(new DomainsClickListener());
        findPreference("filters_subreddits").setOnPreferenceClickListener(new SubredditsClickListener());
        findPreference("filters_user").setOnPreferenceClickListener(new UserClickListener());
        findPreference("filters_flair").setOnPreferenceClickListener(new FlairClickListener());
    }
}
