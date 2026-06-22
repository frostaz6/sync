package com.resync.app.media;

import android.net.Uri;
import androidx.documentfile.provider.DocumentFile;
import com.resync.app.RedditApplication;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.BackupLocationSelectionBottomSheetFragment;

/* JADX INFO: renamed from: h8.a */
/* JADX INFO: compiled from: BackupSafHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class BackupSafHelper {
    public static Uri getRootUri() {
        return DocumentFile.fromTreeUri(RedditApplication.getAppContext(), Uri.parse(BackupLocationSelectionBottomSheetFragment.getBackupDirectory())).getUri();
    }

    public static DocumentFile createBackupFile(String str) {
        return DocumentFile.fromTreeUri(RedditApplication.getAppContext(), Uri.parse(BackupLocationSelectionBottomSheetFragment.getBackupDirectory())).createFile("text/plain", str + ".txt");
    }

    public static boolean isLocationValid() {
        DocumentFile documentFileFromTreeUri;
        String backupDirectory = BackupLocationSelectionBottomSheetFragment.getBackupDirectory();
        return !StringUtils.isEmpty(backupDirectory) && (documentFileFromTreeUri = DocumentFile.fromTreeUri(RedditApplication.getAppContext(), Uri.parse(backupDirectory))) != null && documentFileFromTreeUri.exists() && documentFileFromTreeUri.canRead() && documentFileFromTreeUri.canWrite();
    }
}
