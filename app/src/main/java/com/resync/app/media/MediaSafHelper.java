package com.resync.app.media;

import android.net.Uri;
import android.provider.DocumentsContract;
import android.webkit.MimeTypeMap;
import androidx.documentfile.provider.DocumentFile;
import com.google.android.exoplayer2.util.MimeTypes;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.MediaLocationSelectionBottomSheetFragment;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: i8.a */
/* JADX INFO: compiled from: MediaSafHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class MediaSafHelper {

    static String TAG = "MediaSafHelper";

    public static String getRootPath() {
        String mediaDirectory = MediaLocationSelectionBottomSheetFragment.getMediaDirectory();
        Logger.verbose("backupDir: " + mediaDirectory);
        return mediaDirectory;
    }

    public static DocumentFile getOrCreateSubdirectory(String str) throws RootDirectoryException {
        DocumentFile documentFileFromTreeUri = DocumentFile.fromTreeUri(RedditApplication.getAppContext(), Uri.parse(getRootPath()));
        if (documentFileFromTreeUri == null || !documentFileFromTreeUri.exists() || !documentFileFromTreeUri.canWrite() || !documentFileFromTreeUri.canRead()) {
            MediaLocationSelectionBottomSheetFragment.clearSavedLocations();
            throw new RootDirectoryException();
        }
        if (StringUtils.isEmpty(str)) {
            return documentFileFromTreeUri;
        }
        DocumentFile documentFileFindFile = documentFileFromTreeUri.findFile(str);
        return (documentFileFindFile != null && documentFileFindFile.isDirectory() && documentFileFindFile.canRead() && documentFileFindFile.canRead()) ? documentFileFindFile : documentFileFromTreeUri.createDirectory(str);
    }

    public static Uri getDirectoryUri(String str) {
        if (StringUtils.isEmpty(str)) {
            Logger.verbose(TAG, "Using root directory:" + getRootPath());
            return DocumentFile.fromTreeUri(RedditApplication.getAppContext(), Uri.parse(getRootPath())).getUri();
        }
        if (MediaLocationSelectionBottomSheetFragment.hasSafLocation(str)) {
            Logger.verbose(TAG, "Using cached location: " + MediaLocationSelectionBottomSheetFragment.getSafLocation(str));
            return Uri.parse(MediaLocationSelectionBottomSheetFragment.getSafLocation(str));
        }
        DocumentFile documentFileFromTreeUri = DocumentFile.fromTreeUri(RedditApplication.getAppContext(), Uri.parse(getRootPath()));
        DocumentFile[] documentFileArrListFiles = documentFileFromTreeUri.listFiles();
        int index = 0;
        if (documentFileArrListFiles != null) {
            int length = documentFileArrListFiles.length;
            int found = 0;
            while (index < length) {
                DocumentFile documentFile = documentFileArrListFiles[index];
                if (documentFile.isDirectory() && documentFile.getName().equals(str)) {
                    Logger.verbose(TAG, "Found directory:" + documentFile.getUri());
                    found = 1;
                    documentFileFromTreeUri = documentFile;
                }
                index++;
            }
            index = found;
        }
        if (index == 0) {
            documentFileFromTreeUri = documentFileFromTreeUri.createDirectory(str);
            Logger.verbose(TAG, "Created new directory: " + documentFileFromTreeUri.getUri());
        }
        MediaLocationSelectionBottomSheetFragment.saveSafLocation(str, documentFileFromTreeUri.getUri().toString());
        Logger.verbose(TAG, "Created new directory: " + documentFileFromTreeUri.getUri());
        if (SettingsSingleton.getSettings().saveNomedia) {
            documentFileFromTreeUri.createFile("sync/reddit", ".nomedia");
        }
        return documentFileFromTreeUri.getUri();
    }

    public static Uri createDocument(String str, String str2) throws RootDirectoryException, SubdirectoryException {
        try {
            return DocumentsContract.createDocument(RedditApplication.getAppContext().getContentResolver(), getDirectoryUri(str), getMimeType(str2), getFilename(str2));
        } catch (Exception e) {
            Logger.logException(e);
            DocumentFile documentFileFromTreeUri = DocumentFile.fromTreeUri(RedditApplication.getAppContext(), Uri.parse(getRootPath()));
            if (documentFileFromTreeUri == null || !documentFileFromTreeUri.exists() || !documentFileFromTreeUri.canWrite() || !documentFileFromTreeUri.canRead()) {
                MediaLocationSelectionBottomSheetFragment.clearSavedLocations();
                throw new RootDirectoryException();
            }
            if (!StringUtils.isNotEmpty(str)) {
                throw new RuntimeException("Unknown error");
            }
            MediaLocationSelectionBottomSheetFragment.removeSafLocation(str);
            throw new SubdirectoryException();
        }
    }

    public static DocumentFile createFile(DocumentFile documentFile, String str, int index) {
        return documentFile.createFile(getMimeType(str), getFilenameWithIndex(str, index));
    }

    public static String getExtensionForMime(String str) {
        return "image/gif".equalsIgnoreCase(str) ? ".gif" : "image/jpg".equalsIgnoreCase(str) ? ".jpg" : MimeTypes.IMAGE_JPEG.equalsIgnoreCase(str) ? ".jpeg" : "image/pjpeg".equalsIgnoreCase(str) ? ".pjpeg" : "image/png".equalsIgnoreCase(str) ? ".png" : MimeTypes.VIDEO_MP4.equalsIgnoreCase(str) ? ".mp4" : MimeTypes.VIDEO_WEBM.equalsIgnoreCase(str) ? ".webm" : "image/webp".equalsIgnoreCase(str) ? ".webp" : ".jpg";
    }

    private static String getBaseName(String str) {
        String[] parts = str.split("/");
        String str2 = parts[parts.length - 1];
        if (!TextUtils.isEmpty(str2) && str2.contains("?")) {
            str2 = str2.split("\\?")[0];
        }
        if (!TextUtils.isEmpty(str2) && str2.contains("#")) {
            str2 = str2.split("#")[0];
        }
        if (!TextUtils.isEmpty(str2) && str2.contains(".")) {
            str2 = str2.split("\\.")[0];
        }
        return str2.replace(".gif", "").replace(".jpg", "").replace(".jpeg", "").replace(".pjpeg", "").replace(".png", "").replace(".mp4", "").replace(".webm", "").replace(".webp", "");
    }

    public static String getFilename(String str) {
        if (!LinkTypeHelper.isRedditVideo(str)) {
            return getFilenameWithIndex(str, -1);
        }
        return LinkHandler.getRedditVideoId(str) + ".mp4";
    }

    public static String getFilenameWithIndex(String str, int index) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String baseName = getBaseName(str);
        String extension = getExtensionForMime(getMimeType(str));
        if (index <= -1) {
            return baseName + extension;
        }
        return index + "_" + baseName + extension;
    }

    public static String getMimeType(String str) {
        if (LinkTypeHelper.isRedditVideo(str) || str.contains(".gif?format=mp4")) {
            return MimeTypes.VIDEO_MP4;
        }
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        return !TextUtils.isEmpty(fileExtensionFromUrl) ? "gif".equals(fileExtensionFromUrl) ? "image/gif" : "jpg".equals(fileExtensionFromUrl) ? "image/jpg" : "jpeg".equals(fileExtensionFromUrl) ? MimeTypes.IMAGE_JPEG : "pjpeg".equals(fileExtensionFromUrl) ? "image/pjpeg" : "png".equals(fileExtensionFromUrl) ? "image/png" : "mp4".equals(fileExtensionFromUrl) ? MimeTypes.VIDEO_MP4 : "webm".equals(fileExtensionFromUrl) ? MimeTypes.VIDEO_WEBM : "webp".equals(fileExtensionFromUrl) ? "image/webp" : MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl) : MimeTypes.IMAGE_JPEG;
    }

    public static boolean hasRootDir() {
        return StringUtils.isNotEmpty(getRootPath());
    }
}
