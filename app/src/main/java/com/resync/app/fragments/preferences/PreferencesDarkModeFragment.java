package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.preferences.defaults.SyncRadioButtonPreference;
import com.squareup.otto.Subscribe;
import com.resync.app.events.TimeSelectedEvent;
import com.resync.app.fragments.CustomTimeRangePickerDialog;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: pa.x */
/* JADX INFO: compiled from: PreferencesDarkModeFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesDarkModeFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.x$a */
    /* JADX INFO: compiled from: PreferencesDarkModeFragment.java */
    class AutoPickerClickListener implements Preference.OnPreferenceClickListener {
        AutoPickerClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            CustomTimeRangePickerDialog.newInstance(null, true).show(PreferencesDarkModeFragment.this.getActivity().getSupportFragmentManager(), com.resync.app.thirdparty.timerangepicker.TimeRangePickerDialog.class.getSimpleName());
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.x$b */
    /* JADX INFO: compiled from: PreferencesDarkModeFragment.java */
    class FollowSystemChangeListener implements Preference.OnPreferenceChangeListener {
        FollowSystemChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getInstance().putBoolean("night_mode_follow", true);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_follow")).setChecked(true);
            SettingsSingleton.getInstance().putBoolean("night_mode_force_off", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_force_off")).setChecked(false);
            SettingsSingleton.getInstance().putBoolean("night_mode_force_on", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_force_on")).setChecked(false);
            SettingsSingleton.getInstance().putBoolean("night_mode_auto", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_auto")).setChecked(false);
            PreferencesDarkModeFragment.this.findPreference("dark_auto_picker").setEnabled(false);
            NightHelper.refresh(true);
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.x$c */
    /* JADX INFO: compiled from: PreferencesDarkModeFragment.java */
    class ForceOffChangeListener implements Preference.OnPreferenceChangeListener {
        ForceOffChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getInstance().putBoolean("night_mode_follow", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_follow")).setChecked(false);
            SettingsSingleton.getInstance().putBoolean("night_mode_force_off", true);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_force_off")).setChecked(true);
            SettingsSingleton.getInstance().putBoolean("night_mode_force_on", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_force_on")).setChecked(false);
            SettingsSingleton.getInstance().putBoolean("night_mode_auto", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_auto")).setChecked(false);
            PreferencesDarkModeFragment.this.findPreference("dark_auto_picker").setEnabled(false);
            NightHelper.refresh(true);
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.x$d */
    /* JADX INFO: compiled from: PreferencesDarkModeFragment.java */
    class ForceOnChangeListener implements Preference.OnPreferenceChangeListener {
        ForceOnChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getInstance().putBoolean("night_mode_follow", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_follow")).setChecked(false);
            SettingsSingleton.getInstance().putBoolean("night_mode_force_off", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_force_off")).setChecked(false);
            SettingsSingleton.getInstance().putBoolean("night_mode_force_on", true);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_force_on")).setChecked(true);
            SettingsSingleton.getInstance().putBoolean("night_mode_auto", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_auto")).setChecked(false);
            PreferencesDarkModeFragment.this.findPreference("dark_auto_picker").setEnabled(false);
            NightHelper.refresh(true);
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.x$e */
    /* JADX INFO: compiled from: PreferencesDarkModeFragment.java */
    class AutoChangeListener implements Preference.OnPreferenceChangeListener {
        AutoChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            SettingsSingleton.getInstance().putBoolean("night_mode_follow", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_follow")).setChecked(false);
            SettingsSingleton.getInstance().putBoolean("night_mode_force_off", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_force_off")).setChecked(false);
            SettingsSingleton.getInstance().putBoolean("night_mode_force_on", false);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_force_on")).setChecked(false);
            SettingsSingleton.getInstance().putBoolean("night_mode_auto", true);
            ((SyncRadioButtonPreference) PreferencesDarkModeFragment.this.findPreference("night_mode_auto")).setChecked(true);
            PreferencesDarkModeFragment.this.findPreference("dark_auto_picker").setEnabled(true);
            NightHelper.refresh(true);
            return false;
        }
    }

    public static PreferencesDarkModeFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesDarkModeFragment preferencesDarkModeFragment = new PreferencesDarkModeFragment();
        preferencesDarkModeFragment.setArguments(bundle);
        return preferencesDarkModeFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_dark);
        if (!PreferencesHelper.getAccountPrefs("NightHelper").getBoolean("night_mode_auto", false)) {
            findPreference("dark_auto_picker").setEnabled(false);
        }
        findPreference("dark_auto_picker").setSummary(String.format("%02d:%02d - %02d:%02d", Integer.valueOf(SettingsSingleton.getSettings().autoNightStartHour), Integer.valueOf(SettingsSingleton.getSettings().autoNightStartMin), Integer.valueOf(SettingsSingleton.getSettings().autoNightEndHour), Integer.valueOf(SettingsSingleton.getSettings().autoNightEndMin)));
        findPreference("dark_auto_picker").setOnPreferenceClickListener(new AutoPickerClickListener());
        findPreference("night_mode_follow").setOnPreferenceChangeListener(new FollowSystemChangeListener());
        findPreference("night_mode_force_off").setOnPreferenceChangeListener(new ForceOffChangeListener());
        findPreference("night_mode_force_on").setOnPreferenceChangeListener(new ForceOnChangeListener());
        findPreference("night_mode_auto").setOnPreferenceChangeListener(new AutoChangeListener());
    }

    @Subscribe
    public void onTimeSet(TimeSelectedEvent timeSelectedEvent) {
        SettingsSingleton.getInstance().putInt("auto_night_start_hour", timeSelectedEvent.startHour);
        SettingsSingleton.getInstance().putInt("auto_night_start_min", timeSelectedEvent.startMinute);
        SettingsSingleton.getInstance().putInt("auto_night_end_hour", timeSelectedEvent.endHour);
        SettingsSingleton.getInstance().putInt("auto_night_end_min", timeSelectedEvent.endMinute);
        SettingsSingleton.getSettings().autoNightStartHour = timeSelectedEvent.startHour;
        SettingsSingleton.getSettings().autoNightStartMin = timeSelectedEvent.startMinute;
        SettingsSingleton.getSettings().autoNightEndHour = timeSelectedEvent.endHour;
        SettingsSingleton.getSettings().autoNightEndMin = timeSelectedEvent.endMinute;
        findPreference("dark_auto_picker").setSummary(String.format("%02d:%02d - %02d:%02d", Integer.valueOf(SettingsSingleton.getSettings().autoNightStartHour), Integer.valueOf(SettingsSingleton.getSettings().autoNightStartMin), Integer.valueOf(SettingsSingleton.getSettings().autoNightEndHour), Integer.valueOf(SettingsSingleton.getSettings().autoNightEndMin)));
    }
}
