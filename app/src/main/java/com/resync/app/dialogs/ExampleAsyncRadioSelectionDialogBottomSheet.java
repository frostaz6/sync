package com.resync.app.dialogs;

import androidx.fragment.app.FragmentManager;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import java.util.Iterator;

/* JADX INFO: renamed from: x9.d */
/* JADX INFO: compiled from: ExampleAsyncRadioSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExampleAsyncRadioSelectionDialogBottomSheet extends AbstractRadioSelectionDialogBottomSheet {

    /* JADX INFO: compiled from: ExampleAsyncRadioSelectionDialogBottomSheet.java */
    class DelayedDismissRunnable implements Runnable {
        DelayedDismissRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ExampleAsyncRadioSelectionDialogBottomSheet.this.isDialogShowing()) {
                ExampleAsyncRadioSelectionDialogBottomSheet.this.showItems();
            }
        }
    }

    public static void show(FragmentManager fragmentManager) {
        new ExampleAsyncRadioSelectionDialogBottomSheet().show(fragmentManager, ExampleAsyncRadioSelectionDialogBottomSheet.class.getSimpleName());
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public void setupItems() {
        addItem(new AbstractRadioSelectionDialogBottomSheet.RadioItem("Tester", true));
        addItem(new AbstractRadioSelectionDialogBottomSheet.RadioItem("Not selected", false));
        addItem(new AbstractRadioSelectionDialogBottomSheet.RadioItem("Third", false));
        getView().postDelayed(new DelayedDismissRunnable(), 1000L);
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Select";
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        StaticToast.show("Positive");
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

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public boolean isAsync() {
        return true;
    }
}
