package com.resync.app.fragments.preferences;

import android.content.Intent;
import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.preferences.custom.ultra.UltraManagePreference;
import com.squareup.otto.Subscribe;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.RemovedBottomSheetFragment;
import com.resync.app.dialogs.SupportDevBottomSheetFragment;
import com.resync.app.premium.dialogs.UltraManagePaintSelectionBottomSheet;
import com.resync.app.premium.dialogs.UltraManageTagSelectionBottomSheet;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.events.UltraSkusLoaded;

/* JADX INFO: renamed from: pa.l1 */
/* JADX INFO: compiled from: PreferencesUltraFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesUltraFragment extends BasePreferencesFragment {
    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: l4 */
    public /* synthetic */ boolean onPaintClick(Preference preference) {
        FragmentUtils.show(UltraManagePaintSelectionBottomSheet.class, getParentFragmentManager());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: m4 */
    public /* synthetic */ boolean onTagClick(Preference preference) {
        FragmentUtils.show(UltraManageTagSelectionBottomSheet.class, getParentFragmentManager());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: n4 */
    public /* synthetic */ boolean onSupportClick(Preference preference) {
        FragmentUtils.show(SupportDevBottomSheetFragment.class, getParentFragmentManager());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: o4 */
    public /* synthetic */ boolean onRemovedClick(Preference preference) {
        FragmentUtils.show(RemovedBottomSheetFragment.class, getParentFragmentManager());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: p4 */
    public /* synthetic */ boolean onTranslateClick(Preference preference) {
        com.resync.app.util.links.LinkHandler.openExternal(getContext(), "https://translate.google.com");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: q4 */
    public /* synthetic */ boolean onCloudClick(Preference preference) {
        Intent intent = new Intent(getContext(), (Class<?>) PreferencesActivity.class);
        intent.putExtra("mode", getResources().getInteger(R.integer.BACKUP));
        startActivity(intent);
        return true;
    }

    public static PreferencesUltraFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesUltraFragment preferencesUltraFragment = new PreferencesUltraFragment();
        preferencesUltraFragment.setArguments(bundle);
        return preferencesUltraFragment;
    }

    /* JADX INFO: renamed from: s4 */
    private void setupUltraPreferences() {
        boolean isUltra = UltraHelper.isUltra();
        // ReSync: in-app purchases removed; the buy/restore/reset preferences are gone.
        // LIBERATED: the "Settings cloud backup" section talks only to Sync's dead Ultra backend,
        // so hide it. Local file backup/restore is unaffected and still reachable via the root
        // preferences "Backup" row (cat_root -> @integer/BACKUP).
        findPreference("ultra_cloud_section").setVisible(false);
        findPreference("ultra_enhancements").setVisible(isUltra);
        findPreference("ultra_perks_title").setVisible(isUltra);
        findPreference("ultra_perks").setEnabled(isUltra);
        for (String str : getPreferenceKeys()) {
            if (StringUtils.startsWith(str, "ultra_disabled")) {
                findPreference(str).setVisible(true ^ isUltra);
            }
        }
        findPreference("ultra_paint").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.f1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesUltraFragment.this.onPaintClick(preference);
            }
        });
        findPreference("ultra_tag").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.i1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesUltraFragment.this.onTagClick(preference);
            }
        });
        findPreference("ultra_support").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.j1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesUltraFragment.this.onSupportClick(preference);
            }
        });
        findPreference("ultra_removed").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.g1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesUltraFragment.this.onRemovedClick(preference);
            }
        });
        findPreference("ultra_translate").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.h1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesUltraFragment.this.onTranslateClick(preference);
            }
        });
        findPreference("ultra_cloud").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.k1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesUltraFragment.this.onCloudClick(preference);
            }
        });
        findPreference("ultra_removed").setVisible(RemoteConfigHelper.isUltraRestore());
        findPreference("ultra_translate").setVisible(RemoteConfigHelper.isUltraTranslate());
        // ReSync: Ultra is always unlocked and there is no purchase flow, so just show "manage".
        findPreference("ultra_manage_preference").setVisible(true);
        ((UltraManagePreference) findPreference("ultra_manage_preference")).refresh();
        if (RemoteConfigHelper.isC4266fa() && UltraHelper.isUltra()) {
            String string = PreferencesHelper.getDefaultPrefs().getString(KeyManager.purchaseTokenKey(), null);
            if (StringUtils.isEmpty(string) || !StringUtils.containsIgnoreCase(string, ".AO-")) {
                getActivity().finish();
            }
        }
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_ultra);
        super.onCreatePreferences(bundle, str);
        setupUltraPreferences();
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: k2 */
    public void onResume() {
        super.onResume();
        if (UltraHelper.isUltra()) {
            ((UltraManagePreference) findPreference("ultra_manage_preference")).refresh();
        }
    }

    @Subscribe
    public void onSkusLoaded(UltraSkusLoaded ultraSkusLoaded) {
        Logger.verbose("iap_helper", "Refreshing settings");
        refreshList();
    }
}
