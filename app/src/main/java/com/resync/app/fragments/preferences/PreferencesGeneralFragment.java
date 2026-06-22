package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import java.util.Collections;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.IconPickerSelectionDialogBottomSheet;

/* JADX INFO: renamed from: pa.l0 */
/* JADX INFO: compiled from: PreferencesGeneralFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesGeneralFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.l0$a */
    /* JADX INFO: compiled from: PreferencesGeneralFragment.java */
    class IconPickerClickListener implements Preference.OnPreferenceClickListener {
        IconPickerClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(IconPickerSelectionDialogBottomSheet.class, PreferencesGeneralFragment.this.getParentFragmentManager());
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.l0$b */
    /* JADX INFO: compiled from: PreferencesGeneralFragment.java */
    class SwipeSensitivityChangeListener implements Preference.OnPreferenceChangeListener {
        SwipeSensitivityChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getSettings().swipeSensitivity = Float.parseFloat((String) obj);
            PreferencesGeneralFragment.this.updateSwipeSensSummary((ListPreference) preference, obj);
            return true;
        }
    }

    public static PreferencesGeneralFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesGeneralFragment preferencesGeneralFragment = new PreferencesGeneralFragment();
        preferencesGeneralFragment.setArguments(bundle);
        return preferencesGeneralFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSwipeSensSummary(ListPreference listPreference, Object obj) {
        String[] stringArray = getResources().getStringArray(R.array.swipe_sens_labels);
        ArrayList<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, getResources().getStringArray(R.array.swipe_sens));
        if (obj == null) {
            obj = listPreference.getValue();
        }
        if (obj.equals("1.00")) {
            obj = "1.0";
        }
        listPreference.setSummary(stringArray[arrayList.indexOf(obj)]);
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_general);
        super.onCreatePreferences(bundle, str);
        if (isTabletMode()) {
            findPreference("drawer").setVisible(false);
            findPreference("bottom").setVisible(false);
        }
        findPreference("icon_preference").setOnPreferenceClickListener(new IconPickerClickListener());
        updateSwipeSensSummary((ListPreference) findPreference("swipe_sens"), null);
        findPreference("swipe_sens").setOnPreferenceChangeListener(new SwipeSensitivityChangeListener());
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public SimplePreference[] getSummaryPreferences() {
        return new SimplePreference[]{new SimplePreference("main_fab_action", "0", R.array.fab_labels)};
    }
}
