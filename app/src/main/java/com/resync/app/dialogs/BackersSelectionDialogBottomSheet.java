package com.resync.app.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import org.json.JSONArray;
import com.resync.app.helpers.RemoteConfigHelper;

/* JADX INFO: renamed from: t9.j */
/* JADX INFO: compiled from: BackersSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class BackersSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        try {
            JSONArray jSONArray = new JSONArray(RemoteConfigHelper.getBackers());
            for (int i = 0; i < jSONArray.length(); i++) {
                addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.patreon, jSONArray.getString(i)));
            }
        } catch (Exception unused) {
            dismiss();
        }
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Patreon backers";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        StaticToast.showWithContext(getActivity(), item.toString());
        dismiss();
    }
}
