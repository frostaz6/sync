package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.CustomFontHelper;
import java.util.ArrayList;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: ra.c0 */
/* JADX INFO: compiled from: PreferencesPostSmallerCardViewFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesPostSmallerCardViewFragment extends AbstractPreferencesPostViewFragment {

    /* JADX INFO: renamed from: ra.c0$a */
    /* JADX INFO: compiled from: PreferencesPostSmallerCardViewFragment.java */
    class TypeIndicatorsChangeListener implements Preference.OnPreferenceChangeListener {
        TypeIndicatorsChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().typeIndicators = ((Boolean) obj).booleanValue();
            return PreferencesPostSmallerCardViewFragment.this.notifyPreviewChanged();
        }
    }

    /* JADX INFO: renamed from: ra.c0$b */
    /* JADX INFO: compiled from: PreferencesPostSmallerCardViewFragment.java */
    class AlignThumbnailsChangeListener implements Preference.OnPreferenceChangeListener {
        AlignThumbnailsChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().alignThumbnailsRight = ((Boolean) obj).booleanValue();
            return PreferencesPostSmallerCardViewFragment.this.notifyPreviewChanged();
        }
    }

    public static PreferencesPostSmallerCardViewFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesPostSmallerCardViewFragment preferencesPostSmallerCardViewFragment = new PreferencesPostSmallerCardViewFragment();
        preferencesPostSmallerCardViewFragment.setArguments(bundle);
        return preferencesPostSmallerCardViewFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_post_smaller_card_view);
        super.onCreatePreferences(bundle, str);
        CustomFontHelper.bindPreference(this, "smaller_card_title", "smaller_card_font_title", "smaller_card_size_title");
        CustomFontHelper.bindPreference(this, "smaller_card_description", "smaller_card_font_description", "smaller_card_size_description");
        findPreference("type_indicators").setOnPreferenceChangeListener(new TypeIndicatorsChangeListener());
        findPreference("thumbnail_align").setOnPreferenceChangeListener(new AlignThumbnailsChangeListener());
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public ArrayList<String> getPreferenceKeys() {
        ArrayList<String> arrayListP3 = super.getPreferenceKeys();
        arrayListP3.add("smaller_card_font_title");
        arrayListP3.add("smaller_card_size_title");
        arrayListP3.add("smaller_card_font_description");
        arrayListP3.add("smaller_card_size_description");
        arrayListP3.add("smaller_card_font_selftext");
        arrayListP3.add("smaller_card_size_selftext");
        return arrayListP3;
    }
}
