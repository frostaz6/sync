package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.CustomFontHelper;
import java.util.ArrayList;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: ra.a0 */
/* JADX INFO: compiled from: PreferencesPostSlideViewFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesPostSlideViewFragment extends AbstractPreferencesPostViewFragment {
    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: A4 */
    public /* synthetic */ boolean onSlideButtonsShareChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slide_buttons_share = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: B4 */
    public /* synthetic */ boolean onSlideButtonsModChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slide_buttons_mod = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    public static PreferencesPostSlideViewFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesPostSlideViewFragment preferencesPostSlideViewFragment = new PreferencesPostSlideViewFragment();
        preferencesPostSlideViewFragment.setArguments(bundle);
        return preferencesPostSlideViewFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: r4 */
    public /* synthetic */ boolean onSlideImagePreviewsChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slideImagePreviews = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: s4 */
    public /* synthetic */ boolean onSlideSelftextPreviewsChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slideSelftextPreviews = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: t4 */
    public /* synthetic */ boolean onSlideThickDividerChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slidesThickDivider = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: u4 */
    public /* synthetic */ boolean onSlideImageFullHeightChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slideImageFullHeight = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: v4 */
    public /* synthetic */ boolean onSlideFullWidthChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slideFullWidth = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: w4 */
    public /* synthetic */ boolean onSlideImageTopChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slideImageTop = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: x4 */
    public /* synthetic */ boolean onSlideVoteRightChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slide_button_vote_right = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: y4 */
    public /* synthetic */ boolean onSlideButtonsSaveChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slide_buttons_save = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: z4 */
    public /* synthetic */ boolean onSlideButtonsHideChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().slide_buttons_hide = ((Boolean) obj).booleanValue();
        return notifyPreviewChanged();
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_post_slide_view);
        super.onCreatePreferences(bundle, str);
        findPreference("slide_image_previews").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.s
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideImagePreviewsChange(preference, obj);
            }
        });
        findPreference("slide_selftext_previews").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.x
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideSelftextPreviewsChange(preference, obj);
            }
        });
        findPreference("slide_image_full_height").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.q
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideImageFullHeightChange(preference, obj);
            }
        });
        findPreference("slide_full_width").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.v
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideFullWidthChange(preference, obj);
            }
        });
        findPreference("slide_image_top").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.u
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideImageTopChange(preference, obj);
            }
        });
        CustomFontHelper.bindPreference(this, "slide_title", "slide_font_title", "slide_size_title");
        CustomFontHelper.bindPreference(this, "slide_description", "slide_font_description", "slide_size_description");
        CustomFontHelper.bindPreference(this, "slide_selftext", "slide_font_selftext", "slide_size_selftext");
        findPreference("slide_button_vote_right").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.z
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideVoteRightChange(preference, obj);
            }
        });
        findPreference("slide_buttons_save").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.p
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideButtonsSaveChange(preference, obj);
            }
        });
        findPreference("slide_buttons_hide").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.t
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideButtonsHideChange(preference, obj);
            }
        });
        findPreference("slide_buttons_share").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.r
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideButtonsShareChange(preference, obj);
            }
        });
        findPreference("slide_buttons_mod").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.w
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideButtonsModChange(preference, obj);
            }
        });
        findPreference("slide_thick_divider").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: ra.y
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesPostSlideViewFragment.this.onSlideThickDividerChange(preference, obj);
            }
        });
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public ArrayList<String> getPreferenceKeys() {
        ArrayList<String> arrayListP3 = super.getPreferenceKeys();
        arrayListP3.add("slide_font_title");
        arrayListP3.add("slide_size_title");
        arrayListP3.add("slide_font_description");
        arrayListP3.add("slide_size_description");
        arrayListP3.add("slide_font_selftext");
        arrayListP3.add("slide_size_selftext");
        return arrayListP3;
    }
}
