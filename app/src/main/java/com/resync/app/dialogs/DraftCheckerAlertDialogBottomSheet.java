package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.events.DraftCheckerDismissed;
import com.resync.app.managers.DraftManager;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: renamed from: t9.k0 */
/* JADX INFO: compiled from: DraftCheckerAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class DraftCheckerAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    String draftText;
    boolean editing;

    public static Bundle buildArguments(String str, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("draft_text", str);
        bundle.putSerializable("editing", Boolean.valueOf(z));
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.trash_can_outline;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Discard";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        BusSingleton.getBus().post(new DraftCheckerDismissed());
        dismiss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.draftText = getArguments().getString("draft_text");
        this.editing = getArguments().getBoolean("editing");
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return this.editing ? "Discard unsaved changes?" : "Discard?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNegativeButtonText() {
        return null;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNeutralButtonText() {
        return "Save draft";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onNeutralAction() {
        DraftManager.saveDraft(getActivity(), this.draftText);
        BusSingleton.getBus().post(new DraftCheckerDismissed());
        dismiss();
    }
}
