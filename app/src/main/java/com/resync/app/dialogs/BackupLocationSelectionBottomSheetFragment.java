package com.resync.app.dialogs;

import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: w9.b */
/* JADX INFO: compiled from: BackupLocationSelectionBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class BackupLocationSelectionBottomSheetFragment extends AbstractLocationSelectionBottomSheetFragment {
    public static String getBackupDirectory() {
        return PreferencesHelper.getAccountPrefs("BackupLocationSelectionBottomSheetFragment").getString("directory_backup", null);
    }

    @Override // com.resync.app.dialogs.AbstractLocationSelectionBottomSheetFragment
    protected void saveLocation(String str) {
        PreferencesHelper.getAccountPrefs("BackupLocationSelectionBottomSheetFragment").edit().putString("directory_backup", str).apply();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Select directory";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Select a backup directory";
    }

    @Override // com.resync.app.dialogs.AbstractLocationSelectionBottomSheetFragment, com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "Before backing up your settings you must first select a backup directory";
    }
}
