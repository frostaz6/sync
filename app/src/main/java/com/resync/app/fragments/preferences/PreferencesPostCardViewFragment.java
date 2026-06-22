package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.CustomFontHelper;
import java.util.ArrayList;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: ra.m */
/* JADX INFO: compiled from: PreferencesPostCardViewFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesPostCardViewFragment extends AbstractPreferencesPostViewFragment {
    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: A4 */
    public /* synthetic */ boolean onCardButtonsHideChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().card_buttons_hide = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: B4 */
    public /* synthetic */ boolean onCardButtonsShareChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().card_buttons_share = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    public static PreferencesPostCardViewFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesPostCardViewFragment preferencesPostCardViewFragment = new PreferencesPostCardViewFragment();
        preferencesPostCardViewFragment.setArguments(bundle);
        return preferencesPostCardViewFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: r4 */
    public /* synthetic */ boolean onCardImageAboveChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().cardImageAbove = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: s4 */
    public /* synthetic */ boolean onCardImageFullHeightChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().cardImageFullHeight = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: t4 */
    public /* synthetic */ boolean onCardButtonsModChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().card_buttons_mod = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: u4 */
    public /* synthetic */ boolean onCardTextLabelsChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().cardTextLabels = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: v4 */
    public /* synthetic */ boolean onCardSelftextPreviewsChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().cardSelftextPreviews = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: w4 */
    public /* synthetic */ boolean onCardFullWidthChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().cardFullWidth = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: x4 */
    public /* synthetic */ boolean onColumnsPortraitChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().postColumnsPortrait = Integer.parseInt((String) obj);
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: y4 */
    public /* synthetic */ boolean onColumnsLandscapeChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().postColumnsLandscape = Integer.parseInt((String) obj);
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: z4 */
    public /* synthetic */ boolean onCardButtonsSaveChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().card_buttons_save = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_post_card_view);
        super.onCreatePreferences(bundle, str);
        findPreference("card_image_above").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.e
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onCardImageAboveChange(preference, obj);
            }
        });
        findPreference("card_image_full_height").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.l
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onCardImageFullHeightChange(preference, obj);
            }
        });
        findPreference("card_text_labels").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.c
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onCardTextLabelsChange(preference, obj);
            }
        });
        findPreference("card_selftext_previews").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.b
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onCardSelftextPreviewsChange(preference, obj);
            }
        });
        findPreference("card_full_width").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.d
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onCardFullWidthChange(preference, obj);
            }
        });
        findPreference("post_columns_portrait").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.f
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onColumnsPortraitChange(preference, obj);
            }
        });
        findPreference("post_columns_landscape").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.k
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onColumnsLandscapeChange(preference, obj);
            }
        });
        CustomFontHelper.bindPreference(this, "card_title", "card_font_title", "card_size_title");
        CustomFontHelper.bindPreference(this, "card_description", "card_font_description", "card_size_description");
        CustomFontHelper.bindPreference(this, "card_selftext", "card_font_selftext", "card_size_selftext");
        findPreference("card_buttons_save").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.i
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onCardButtonsSaveChange(preference, obj);
            }
        });
        findPreference("card_buttons_hide").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.j
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onCardButtonsHideChange(preference, obj);
            }
        });
        findPreference("card_buttons_share").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.h
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onCardButtonsShareChange(preference, obj);
            }
        });
        findPreference("card_buttons_mod").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.g
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostCardViewFragment.this.onCardButtonsModChange(preference, obj);
            }
        });
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public ArrayList<String> getPreferenceKeys() {
        ArrayList<String> arrayListP3 = super.getPreferenceKeys();
        arrayListP3.add("card_font_title");
        arrayListP3.add("card_size_title");
        arrayListP3.add("card_font_description");
        arrayListP3.add("card_size_description");
        arrayListP3.add("card_font_selftext");
        arrayListP3.add("card_size_selftext");
        return arrayListP3;
    }
}
