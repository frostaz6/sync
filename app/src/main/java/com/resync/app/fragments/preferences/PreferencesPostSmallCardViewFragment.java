package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.CustomFontHelper;
import java.util.ArrayList;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: ra.b0 */
/* JADX INFO: compiled from: PreferencesPostSmallCardViewFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesPostSmallCardViewFragment extends AbstractPreferencesPostViewFragment {

    /* JADX INFO: renamed from: ra.b0$a */
    /* JADX INFO: compiled from: PreferencesPostSmallCardViewFragment.java */
    class AlignThumbnailsLeftChangeListener implements Preference.OnPreferenceChangeListener {
        AlignThumbnailsLeftChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().smallCardsAlignThumbnailsLeft = ((Boolean) obj).booleanValue();
            return PreferencesPostSmallCardViewFragment.this.notifyPreviewChanged();
        }
    }

    /* JADX INFO: renamed from: ra.b0$b */
    /* JADX INFO: compiled from: PreferencesPostSmallCardViewFragment.java */
    class SmallCardButtonsSaveChangeListener implements Preference.OnPreferenceChangeListener {
        SmallCardButtonsSaveChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().small_card_buttons_save = ((Boolean) obj).booleanValue();
            return PreferencesPostSmallCardViewFragment.this.notifyPreviewChanged();
        }
    }

    /* JADX INFO: renamed from: ra.b0$c */
    /* JADX INFO: compiled from: PreferencesPostSmallCardViewFragment.java */
    class SmallCardButtonsHideChangeListener implements Preference.OnPreferenceChangeListener {
        SmallCardButtonsHideChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().small_card_buttons_hide = ((Boolean) obj).booleanValue();
            return PreferencesPostSmallCardViewFragment.this.notifyPreviewChanged();
        }
    }

    /* JADX INFO: renamed from: ra.b0$d */
    /* JADX INFO: compiled from: PreferencesPostSmallCardViewFragment.java */
    class SmallCardButtonsShareChangeListener implements Preference.OnPreferenceChangeListener {
        SmallCardButtonsShareChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().small_card_buttons_share = ((Boolean) obj).booleanValue();
            return PreferencesPostSmallCardViewFragment.this.notifyPreviewChanged();
        }
    }

    /* JADX INFO: renamed from: ra.b0$e */
    /* JADX INFO: compiled from: PreferencesPostSmallCardViewFragment.java */
    class SmallCardButtonsModChangeListener implements Preference.OnPreferenceChangeListener {
        SmallCardButtonsModChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().small_card_buttons_mod = ((Boolean) obj).booleanValue();
            return PreferencesPostSmallCardViewFragment.this.notifyPreviewChanged();
        }
    }

    public static PreferencesPostSmallCardViewFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesPostSmallCardViewFragment preferencesPostSmallCardViewFragment = new PreferencesPostSmallCardViewFragment();
        preferencesPostSmallCardViewFragment.setArguments(bundle);
        return preferencesPostSmallCardViewFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_post_small_card_view);
        super.onCreatePreferences(bundle, str);
        CustomFontHelper.bindPreference(this, "small_card_title", "small_card_font_title", "small_card_size_title");
        CustomFontHelper.bindPreference(this, "small_card_description", "small_card_font_description", "small_card_size_description");
        findPreference("small_cards_thumbnail_align_left").setOnPreferenceChangeListener(new AlignThumbnailsLeftChangeListener());
        findPreference("small_card_buttons_save").setOnPreferenceChangeListener(new SmallCardButtonsSaveChangeListener());
        findPreference("small_card_buttons_hide").setOnPreferenceChangeListener(new SmallCardButtonsHideChangeListener());
        findPreference("small_card_buttons_share").setOnPreferenceChangeListener(new SmallCardButtonsShareChangeListener());
        findPreference("small_card_buttons_mod").setOnPreferenceChangeListener(new SmallCardButtonsModChangeListener());
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public ArrayList<String> getPreferenceKeys() {
        ArrayList<String> arrayListP3 = super.getPreferenceKeys();
        arrayListP3.add("small_card_font_title");
        arrayListP3.add("small_card_size_title");
        arrayListP3.add("small_card_font_description");
        arrayListP3.add("small_card_size_description");
        arrayListP3.add("small_card_font_selftext");
        arrayListP3.add("small_card_size_selftext");
        return arrayListP3;
    }
}
