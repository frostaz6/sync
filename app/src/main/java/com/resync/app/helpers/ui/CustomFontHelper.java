package com.resync.app.helpers.ui;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.resync.app.dialogs.TypefacePickerSelectionDialogBottomSheet;

/* JADX INFO: renamed from: gb.c */
/* JADX INFO: compiled from: CustomFontHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public final class CustomFontHelper {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean onPreferenceClicked(String str, String str2, PreferenceFragmentCompat preferenceFragmentCompat, Preference preference) {
        TypefacePickerSelectionDialogBottomSheet typefacePickerSelectionDialogBottomSheetC4 = TypefacePickerSelectionDialogBottomSheet.newInstance(str, str2);
        typefacePickerSelectionDialogBottomSheetC4.setTargetFragment(preferenceFragmentCompat, 0);
        typefacePickerSelectionDialogBottomSheetC4.show(preferenceFragmentCompat.getChildFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
        return true;
    }

    public static final void bindPreference(final PreferenceFragmentCompat preferenceFragmentCompat, String str, final String str2, final String str3) {
        preferenceFragmentCompat.findPreference(str).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: gb.b
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public final boolean onPreferenceClick(Preference preference) {
                return CustomFontHelper.onPreferenceClicked(str2, str3, preferenceFragmentCompat, preference);
            }
        });
    }
}
