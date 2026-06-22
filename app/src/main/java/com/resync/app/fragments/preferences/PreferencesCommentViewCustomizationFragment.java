package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.preferences.defaults.SyncCheckBoxPreference;
import com.resync.app.helpers.ui.CustomFontHelper;
import java.util.ArrayList;
import com.resync.app.fragments.preferences.AbstractPreferencesPostViewFragment;

/* JADX INFO: renamed from: pa.v */
/* JADX INFO: compiled from: PreferencesCommentViewCustomizationFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesCommentViewCustomizationFragment extends AbstractPreferencesPostViewFragment {
    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: i4 */
    public /* synthetic */ boolean onBodySpaceChange(Preference preference, Object obj) {
        SettingsSingleton.getSettings().comments_body_space = Float.parseFloat((String) obj);
        return notifyPreviewChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: j4 */
    public static /* synthetic */ boolean onMediaPreviewClick(SyncCheckBoxPreference syncCheckBoxPreference, SyncCheckBoxPreference syncCheckBoxPreference2, SyncCheckBoxPreference syncCheckBoxPreference3, Preference preference) {
        if (preference.equals(syncCheckBoxPreference)) {
            syncCheckBoxPreference.setChecked(true);
            syncCheckBoxPreference2.setChecked(false);
            syncCheckBoxPreference3.setChecked(false);
        } else if (preference.equals(syncCheckBoxPreference2)) {
            syncCheckBoxPreference.setChecked(false);
            syncCheckBoxPreference2.setChecked(true);
            syncCheckBoxPreference3.setChecked(false);
        } else if (preference.equals(syncCheckBoxPreference3)) {
            syncCheckBoxPreference.setChecked(false);
            syncCheckBoxPreference2.setChecked(false);
            syncCheckBoxPreference3.setChecked(true);
        }
        SettingsSingleton.getInstance().putBoolean("media_preview_thumbnail", syncCheckBoxPreference.isChecked());
        SettingsSingleton.getInstance().putBoolean("media_preview_cropped", syncCheckBoxPreference2.isChecked());
        SettingsSingleton.getInstance().putBoolean("media_preview_full", syncCheckBoxPreference3.isChecked());
        return true;
    }

    public static PreferencesCommentViewCustomizationFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesCommentViewCustomizationFragment preferencesCommentViewCustomizationFragment = new PreferencesCommentViewCustomizationFragment();
        preferencesCommentViewCustomizationFragment.setArguments(bundle);
        return preferencesCommentViewCustomizationFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_comment_view_customization);
        super.onCreatePreferences(bundle, str);
        CustomFontHelper.bindPreference(this, "comments_description", "comments_description_typeface", "comments_description_size");
        CustomFontHelper.bindPreference(this, "comments_body", "comments_body_typeface", "comments_body_size");
        findPreference("comments_body_space").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: pa.t
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return PreferencesCommentViewCustomizationFragment.this.onBodySpaceChange(preference, obj);
            }
        });
        final SyncCheckBoxPreference syncCheckBoxPreference = (SyncCheckBoxPreference) findPreference("media_preview_thumbnail");
        final SyncCheckBoxPreference syncCheckBoxPreference2 = (SyncCheckBoxPreference) findPreference("media_preview_cropped");
        final SyncCheckBoxPreference syncCheckBoxPreference3 = (SyncCheckBoxPreference) findPreference("media_preview_full");
        syncCheckBoxPreference.setChecked(SettingsSingleton.getSettings().commentMediaThumbnail);
        syncCheckBoxPreference2.setChecked(SettingsSingleton.getSettings().commentMediaCropped);
        syncCheckBoxPreference3.setChecked(SettingsSingleton.getSettings().commentMediaFull);
        Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() { // from class: pa.u
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesCommentViewCustomizationFragment.onMediaPreviewClick(syncCheckBoxPreference, syncCheckBoxPreference2, syncCheckBoxPreference3, preference);
            }
        };
        syncCheckBoxPreference.setOnPreferenceClickListener(clickListener);
        syncCheckBoxPreference2.setOnPreferenceClickListener(clickListener);
        syncCheckBoxPreference3.setOnPreferenceClickListener(clickListener);
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public ArrayList<String> getPreferenceKeys() {
        ArrayList<String> arrayListP3 = super.getPreferenceKeys();
        arrayListP3.add("comments_description_typeface");
        arrayListP3.add("comments_description_size");
        arrayListP3.add("comments_body_typeface");
        arrayListP3.add("comments_body_size");
        arrayListP3.add("comments_body_space");
        return arrayListP3;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public SimplePreference[] getSummaryPreferences() {
        return new SimplePreference[]{new SimplePreference("comment_padding", "0", R.array.comment_padding)};
    }
}
