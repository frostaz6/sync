package com.resync.app.dialogs;

import android.net.Uri;
import com.resync.app.jobs.media.AbstractDownloadMediaJob;
import com.resync.app.receiver.download.ShareReceiver;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;

/* JADX INFO: renamed from: t9.k */
/* JADX INFO: compiled from: BackupAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class BackupAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Share";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        ShareReceiver.share(new AbstractDownloadMediaJob.ShareInfo("Backup", Uri.parse(getUrl()), "application/json"));
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Backup complete";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNegativeButtonText() {
        return "Close";
    }
}
