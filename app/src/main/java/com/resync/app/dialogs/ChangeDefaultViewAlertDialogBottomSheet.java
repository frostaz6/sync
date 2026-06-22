package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.events.UiModeChangedEvent;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: renamed from: t9.n */
/* JADX INFO: compiled from: ChangeDefaultViewAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ChangeDefaultViewAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    int uiMode;

    public static Bundle buildArguments(int uiMode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ui_mode", Integer.valueOf(uiMode));
        return bundle;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Change";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        UiSingleton.getInstance().setUiMode(this.uiMode);
        BusSingleton.getBus().post(new UiModeChangedEvent());
        dismiss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.uiMode = getArguments().getInt("ui_mode");
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Change your view type?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
