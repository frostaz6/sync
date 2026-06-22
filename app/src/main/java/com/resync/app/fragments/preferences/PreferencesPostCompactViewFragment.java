package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.CustomFontHelper;
import java.util.ArrayList;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: ra.n */
/* JADX INFO: compiled from: PreferencesPostCompactViewFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesPostCompactViewFragment extends AbstractPreferencesPostViewFragment {

    /* JADX INFO: renamed from: ra.n$a */
    /* JADX INFO: compiled from: PreferencesPostCompactViewFragment.java */
    class TypeIndicatorsChangeListener implements Preference.OnPreferenceChangeListener {
        TypeIndicatorsChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().typeIndicators = ((Boolean) obj).booleanValue();
            return PreferencesPostCompactViewFragment.this.notifyPreviewChanged();
        }
    }

    /* JADX INFO: renamed from: ra.n$b */
    /* JADX INFO: compiled from: PreferencesPostCompactViewFragment.java */
    class CompactThumbnailsChangeListener implements Preference.OnPreferenceChangeListener {
        CompactThumbnailsChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().compactThumbnails = ((Boolean) obj).booleanValue();
            return PreferencesPostCompactViewFragment.this.notifyPreviewChanged();
        }
    }

    /* JADX INFO: renamed from: ra.n$c */
    /* JADX INFO: compiled from: PreferencesPostCompactViewFragment.java */
    class AlignThumbnailsChangeListener implements Preference.OnPreferenceChangeListener {
        AlignThumbnailsChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().alignThumbnailsRight = ((Boolean) obj).booleanValue();
            return PreferencesPostCompactViewFragment.this.notifyPreviewChanged();
        }
    }

    public static PreferencesPostCompactViewFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesPostCompactViewFragment preferencesPostCompactViewFragment = new PreferencesPostCompactViewFragment();
        preferencesPostCompactViewFragment.setArguments(bundle);
        return preferencesPostCompactViewFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_post_compact_view);
        super.onCreatePreferences(bundle, str);
        CustomFontHelper.bindPreference(this, "compact_title", "compact_font_title", "compact_size_title");
        CustomFontHelper.bindPreference(this, "compact_description", "compact_font_description", "compact_size_description");
        findPreference("type_indicators").setOnPreferenceChangeListener(new TypeIndicatorsChangeListener());
        findPreference("compact_thumbnails").setOnPreferenceChangeListener(new CompactThumbnailsChangeListener());
        findPreference("thumbnail_align").setOnPreferenceChangeListener(new AlignThumbnailsChangeListener());
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public ArrayList<String> getPreferenceKeys() {
        ArrayList<String> arrayListP3 = super.getPreferenceKeys();
        arrayListP3.add("compact_font_title");
        arrayListP3.add("compact_size_title");
        arrayListP3.add("compact_font_description");
        arrayListP3.add("compact_size_description");
        arrayListP3.add("compact_font_selftext");
        arrayListP3.add("compact_size_selftext");
        return arrayListP3;
    }
}
