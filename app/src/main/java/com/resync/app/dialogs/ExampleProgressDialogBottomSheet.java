package com.resync.app.dialogs;

import androidx.fragment.app.FragmentManager;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet;

/* JADX INFO: renamed from: x9.g */
/* JADX INFO: compiled from: ExampleProgressDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExampleProgressDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    public static void showDialog(FragmentManager fragmentManager) {
        new ExampleProgressDialogBottomSheet().show(fragmentManager, ExampleProgressDialogBottomSheet.class.getSimpleName());
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public void onProgressReady() {
        super.onProgressReady();
    }
}
