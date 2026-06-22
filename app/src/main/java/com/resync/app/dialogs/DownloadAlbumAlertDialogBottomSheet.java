package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.jobs.media.DownloadAlbumJob;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: t9.j0 */
/* JADX INFO: compiled from: DownloadAlbumAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class DownloadAlbumAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    private String title;
    private String subreddit;
    private String url;

    public static Bundle buildArguments(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("title", str);
        bundle.putSerializable("subreddit", str2);
        bundle.putSerializable("url", str3);
        return bundle;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Download";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        DownloadAlbumJob.download(getChildFragmentManager(), this.title, this.subreddit, this.url, 1);
        dismiss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.title = getArguments().getString("title");
        this.subreddit = getArguments().getString("subreddit");
        this.url = getArguments().getString("url");
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Download album";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "There are " + StringUtils.split(getUrl(), ",").length + " items to download in this album";
    }
}
