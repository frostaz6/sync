package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.dialogs.ExportThemeAlertDialogBottomSheet;
import com.resync.app.dialogs.ImportThemeInputDialogBottomSheet;
import com.resync.app.R;
import com.resync.app.receiver.MonetThemeReceiver;
import com.resync.app.singleton.SettingsSingleton;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.mp4parser.boxes.apple.TrackLoadSettingsAtom;
import com.resync.app.events.ScrollToTopOfPreferencesEvent;
import com.resync.app.fragments.ColorSelectionFragment;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.MonetBottomSheetFragment;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.util.ColorUtils;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: pa.t0 */
/* JADX INFO: compiled from: PreferencesMoneteThemeAdvancedFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesMoneteThemeAdvancedFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.t0$a */
    /* JADX INFO: compiled from: PreferencesMoneteThemeAdvancedFragment.java */
    class ForceAmoledChangeListener implements Preference.OnPreferenceChangeListener {
        ForceAmoledChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            Boolean bool = (Boolean) obj;
            SettingsSingleton.getInstance().putBoolean(preference.getKey(), bool.booleanValue());
            SettingsSingleton.getSettings().monetForceAmoled = bool.booleanValue();
            MonetUtil.reload();
            MonetThemeReceiver.broadcast();
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.t0$b */
    /* JADX INFO: compiled from: PreferencesMoneteThemeAdvancedFragment.java */
    class BoostDarkChangeListener implements Preference.OnPreferenceChangeListener {
        BoostDarkChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            Boolean bool = (Boolean) obj;
            SettingsSingleton.getInstance().putBoolean(preference.getKey(), bool.booleanValue());
            SettingsSingleton.getSettings().monet_boost_dark_color = bool.booleanValue();
            MonetUtil.reload();
            MonetThemeReceiver.broadcast();
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.t0$c */
    /* JADX INFO: compiled from: PreferencesMoneteThemeAdvancedFragment.java */
    class BoostLightChangeListener implements Preference.OnPreferenceChangeListener {
        BoostLightChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            Boolean bool = (Boolean) obj;
            SettingsSingleton.getInstance().putBoolean(preference.getKey(), bool.booleanValue());
            SettingsSingleton.getSettings().monet_boost_light_color = bool.booleanValue();
            MonetUtil.reload();
            MonetThemeReceiver.broadcast();
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.t0$d */
    /* JADX INFO: compiled from: PreferencesMoneteThemeAdvancedFragment.java */
    class PaletteClickListener implements Preference.OnPreferenceClickListener {
        PaletteClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(MonetBottomSheetFragment.class, PreferencesMoneteThemeAdvancedFragment.this.getFragmentManager());
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.t0$e */
    /* JADX INFO: compiled from: PreferencesMoneteThemeAdvancedFragment.java */
    class ShareThemeClickListener implements Preference.OnPreferenceClickListener {
        ShareThemeClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            ArrayList<String> arrayListP3 = PreferencesMoneteThemeAdvancedFragment.this.getPreferenceKeys();
            JSONObject jSONObject = new JSONObject();
            try {
                for (String str : arrayListP3) {
                    if (StringUtils.startsWith(str, "monet_")) {
                        Logger.verbose("Key: " + str + " - " + SettingsSingleton.getInstance().getFieldType(str));
                        if (SettingsSingleton.getInstance().getFieldType(str) == Integer.TYPE) {
                            Integer num = (Integer) SettingsSingleton.getInstance().getFieldValue(str);
                            Logger.verbose("VALUE: " + num);
                            if (num.intValue() == 0) {
                                jSONObject.put(str, SettingsSingleton.getInstance().getFieldValue(str));
                            } else {
                                jSONObject.put(str, ColorUtils.toHexColor(num.intValue()));
                            }
                        } else {
                            jSONObject.put(str, SettingsSingleton.getInstance().getFieldValue(str));
                        }
                    }
                }
                jSONObject.put("monet_system", SettingsSingleton.getSettings().monet_system);
                Logger.verbose("THEME", jSONObject.toString(4));
                FragmentUtils.showWithUrl(ExportThemeAlertDialogBottomSheet.class, PreferencesMoneteThemeAdvancedFragment.this.getChildFragmentManager(), jSONObject.toString(2));
                return true;
            } catch (Exception e) {
                StaticToast.show("Error exporting thtme");
                Logger.logException(e);
                return true;
            }
        }
    }

    /* JADX INFO: renamed from: pa.t0$f */
    /* JADX INFO: compiled from: PreferencesMoneteThemeAdvancedFragment.java */
    class ImportThemeClickListener implements Preference.OnPreferenceClickListener {
        ImportThemeClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(ImportThemeInputDialogBottomSheet.class, PreferencesMoneteThemeAdvancedFragment.this.getChildFragmentManager());
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onManualColorClick(Preference preference) {
        ColorSelectionFragment colorSelectionFragmentI4 = ColorSelectionFragment.newInstance(SettingsSingleton.getSettings().monet_manual_theme_color, null, true);
        colorSelectionFragmentI4.setColorSelectedListener(new ColorSelectionFragment.ColorSelectedListener() { // from class: pa.s0
            @Override // com.resync.app.fragments.ColorSelectionFragment.ColorSelectedListener
            public final void onColorSelected(int color) {
                PreferencesMoneteThemeAdvancedFragment.this.applyManualColor(color);
            }
        });
        colorSelectionFragmentI4.show(ContextHelper.getFragmentManager(getContext()), ColorSelectionFragment.TAG);
        return true;
    }

    public static PreferencesMoneteThemeAdvancedFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesMoneteThemeAdvancedFragment preferencesMoneteThemeAdvancedFragment = new PreferencesMoneteThemeAdvancedFragment();
        preferencesMoneteThemeAdvancedFragment.setArguments(bundle);
        return preferencesMoneteThemeAdvancedFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: k4, reason: merged with bridge method [inline-methods] */
    public void applyManualColor(int color) {
        SettingsSingleton.getInstance().putString("monet_manual_theme_color", ColorUtils.toHexColor(color));
        SettingsSingleton.getSettings().monet_manual_theme_color = color;
        MonetUtil.reload();
        MonetThemeReceiver.broadcast();
    }

    private void refreshMonetVisibility() {
        boolean z = SettingsSingleton.getSettings().monet_system;
        findPreference("automatic").setVisible(z);
        findPreference("manual").setVisible(!z);
        findPreference("monet_manual_theme_color").setSummary(ColorUtils.toHexColor(SettingsSingleton.getSettings().monet_manual_theme_color));
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_monet_advanced);
        super.onCreatePreferences(bundle, str);
        refreshMonetVisibility();
        findPreference("monet_manual_theme_color").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.r0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesMoneteThemeAdvancedFragment.this.onManualColorClick(preference);
            }
        });
        findPreference("force_amoled").setOnPreferenceChangeListener(new ForceAmoledChangeListener());
        findPreference("monet_boost_dark_color").setOnPreferenceChangeListener(new BoostDarkChangeListener());
        findPreference("monet_boost_light_color").setOnPreferenceChangeListener(new BoostLightChangeListener());
        findPreference("palette").setOnPreferenceClickListener(new PaletteClickListener());
        findPreference("share").setOnPreferenceClickListener(new ShareThemeClickListener());
        findPreference(TrackLoadSettingsAtom.TYPE).setOnPreferenceClickListener(new ImportThemeClickListener());
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public void onDarkModeChanged() {
        super.onDarkModeChanged();
        refreshMonetVisibility();
    }

    @Subscribe
    public void onScrollToTopEvent(ScrollToTopOfPreferencesEvent scrollToTopOfPreferencesEvent) {
        scrollToTop();
    }
}
