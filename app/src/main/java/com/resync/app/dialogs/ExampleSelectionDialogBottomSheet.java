package com.resync.app.dialogs;

import androidx.fragment.app.FragmentManager;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;

/* JADX INFO: renamed from: x9.i */
/* JADX INFO: compiled from: ExampleSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExampleSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    public static void show(FragmentManager fragmentManager) {
        new ExampleSelectionDialogBottomSheet().show(fragmentManager, ExampleSelectionDialogBottomSheet.class.getSimpleName());
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_access_time_24, "Tester"));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_check_24, "Second"));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_open_in_browser_24, "You still reading?"));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.bottom_nav_outline_home_24, "Home"));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.bottom_nav_outline_mail_outline_24, "Mail"));
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Example selection dialog";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        StaticToast.showWithContext(getActivity(), item.toString());
        dismiss();
    }
}
