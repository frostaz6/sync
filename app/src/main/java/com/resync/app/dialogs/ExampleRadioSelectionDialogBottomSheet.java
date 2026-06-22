package com.resync.app.dialogs;

import androidx.fragment.app.FragmentManager;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import java.util.Iterator;

/* JADX INFO: renamed from: x9.h */
/* JADX INFO: compiled from: ExampleRadioSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExampleRadioSelectionDialogBottomSheet extends AbstractRadioSelectionDialogBottomSheet {
    public static void show(FragmentManager fragmentManager) {
        new ExampleRadioSelectionDialogBottomSheet().show(fragmentManager, ExampleRadioSelectionDialogBottomSheet.class.getSimpleName());
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public void setupItems() {
        for (int i = 0; i < 200; i++) {
            addItem(new AbstractRadioSelectionDialogBottomSheet.RadioItem("Tester: " + i, false));
        }
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Select";
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        StaticToast.show("Selected");
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractRadioSelectionDialogBottomSheet.RadioItem item) {
        Iterator<AbstractRadioSelectionDialogBottomSheet.RadioItem> it = getItems().iterator();
        while (it.hasNext()) {
            it.next().setChecked(false);
        }
        item.setChecked(true);
        notifyItemsChanged();
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Example selection dialog";
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onNegativeButtonClicked() {
        StaticToast.show("Negative");
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public String getNegativeButtonText() {
        return "Cancel";
    }
}
