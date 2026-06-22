package com.resync.app.dialogs.interfaces;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;

/* JADX INFO: renamed from: v9.f */
/* JADX INFO: compiled from: SelectionDialogBottomSheetInterface.java */
/* JADX INFO: loaded from: classes.dex */
public interface SelectionDialogBottomSheetInterface {
    String getTitle();

    void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem selectionItem);
}
