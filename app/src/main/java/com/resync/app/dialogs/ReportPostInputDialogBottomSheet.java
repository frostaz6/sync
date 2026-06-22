package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthReportRequest;

/* JADX INFO: renamed from: t9.y1 */
/* JADX INFO: compiled from: ReportPostInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ReportPostInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        OAuthRequestManager.sendWithAppContext(new OAuthReportRequest(getActivity(), 3, getPostId(), str));
        StaticToast.showWithContext(getActivity(), "Post reported");
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Report";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "Reason";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Report";
    }
}
