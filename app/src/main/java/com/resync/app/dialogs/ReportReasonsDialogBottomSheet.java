package com.resync.app.dialogs;

import android.os.Bundle;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;

/* JADX INFO: compiled from: ReportReasonsDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ReportReasonsDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    String[] reportReasons;

    /* JADX WARN: Multi-variable type inference failed */
    public static Bundle buildArguments(String[] strArr) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("reasons", strArr);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        for (String str : this.reportReasons) {
            addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_report_24, str));
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.reportReasons = getArguments().getStringArray("reasons");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Report reasons";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        RedditHelper.copyToClipboard(getActivity(), item.title, true);
        dismiss();
    }
}
