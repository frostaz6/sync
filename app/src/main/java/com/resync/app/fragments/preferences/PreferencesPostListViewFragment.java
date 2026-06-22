package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.CustomFontHelper;
import java.util.ArrayList;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: ra.o */
/* JADX INFO: compiled from: PreferencesPostListViewFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesPostListViewFragment extends AbstractPreferencesPostViewFragment {

    /* JADX INFO: renamed from: ra.o$a */
    /* JADX INFO: compiled from: PreferencesPostListViewFragment.java */
    class CompactThumbnailsChangeListener implements Preference.OnPreferenceChangeListener {
        CompactThumbnailsChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().compactThumbnails = ((Boolean) obj).booleanValue();
            return PreferencesPostListViewFragment.this.notifyPreviewChanged();
        }
    }

    /* JADX INFO: renamed from: ra.o$b */
    /* JADX INFO: compiled from: PreferencesPostListViewFragment.java */
    class TypeIndicatorsChangeListener implements Preference.OnPreferenceChangeListener {
        TypeIndicatorsChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().typeIndicators = ((Boolean) obj).booleanValue();
            return PreferencesPostListViewFragment.this.notifyPreviewChanged();
        }
    }

    /* JADX INFO: renamed from: ra.o$c */
    /* JADX INFO: compiled from: PreferencesPostListViewFragment.java */
    class AlignThumbnailsChangeListener implements Preference.OnPreferenceChangeListener {
        AlignThumbnailsChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().alignThumbnailsRight = ((Boolean) obj).booleanValue();
            return PreferencesPostListViewFragment.this.notifyPreviewChanged();
        }
    }

    public static PreferencesPostListViewFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesPostListViewFragment preferencesPostListViewFragment = new PreferencesPostListViewFragment();
        preferencesPostListViewFragment.setArguments(bundle);
        return preferencesPostListViewFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_post_list_view);
        super.onCreatePreferences(bundle, str);
        CustomFontHelper.bindPreference(this, "list_title", "list_font_title", "list_size_title");
        CustomFontHelper.bindPreference(this, "list_description", "list_font_description", "list_size_description");
        findPreference("compact_thumbnails").setOnPreferenceChangeListener(new CompactThumbnailsChangeListener());
        findPreference("type_indicators").setOnPreferenceChangeListener(new TypeIndicatorsChangeListener());
        findPreference("thumbnail_align").setOnPreferenceChangeListener(new AlignThumbnailsChangeListener());
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public ArrayList<String> getPreferenceKeys() {
        ArrayList<String> arrayListP3 = super.getPreferenceKeys();
        arrayListP3.add("list_font_title");
        arrayListP3.add("list_size_title");
        arrayListP3.add("list_font_description");
        arrayListP3.add("list_size_description");
        arrayListP3.add("list_font_selftext");
        arrayListP3.add("list_size_selftext");
        return arrayListP3;
    }
}
