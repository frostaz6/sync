package com.resync.app.dialogs.interfaces;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet;

/* JADX INFO: renamed from: v9.d */
/* JADX INFO: compiled from: RadioSelectionDialogBottomSheetInterface.java */
/* JADX INFO: loaded from: classes.dex */
public interface RadioSelectionDialogBottomSheetInterface {
    String getPositiveButtonText();

    void onPositiveButtonClicked();

    void onItemSelected(AbstractRadioSelectionDialogBottomSheet.RadioItem radioItem);

    String getTitle();

    void onNegativeButtonClicked();
}
