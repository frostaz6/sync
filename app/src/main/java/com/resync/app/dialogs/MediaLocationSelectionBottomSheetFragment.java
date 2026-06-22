package com.resync.app.dialogs;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.resync.app.jobs.media.DownloadAlbumJob;
import com.resync.app.jobs.media.DownloadMediaJob;
import com.resync.app.jobs.media.DownloadRedditVideoJob;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: w9.c */
/* JADX INFO: compiled from: MediaLocationSelectionBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class MediaLocationSelectionBottomSheetFragment extends AbstractLocationSelectionBottomSheetFragment {
    private String title;
    private String subreddit;
    private String url;

    public static void clearSavedLocations() {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("MediaLocationSelectionBottomSheetFragment").edit();
        Iterator<? extends Map.Entry<String, ?>> it = PreferencesHelper.getAccountPrefs("MediaLocationSelectionBottomSheetFragment").getAll().entrySet().iterator();
        while (it.hasNext()) {
            String key = it.next().getKey();
            if (StringUtils.startsWith(key, getSafPrefix())) {
                editorEdit.remove(key);
            }
        }
        editorEdit.remove("directory_media");
        editorEdit.apply();
    }

    public static String getMediaDirectory() {
        return PreferencesHelper.getAccountPrefs("MediaLocationSelectionBottomSheetFragment").getString("directory_media", null);
    }

    public static Bundle buildArguments(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        bundle.putString("title", str);
        bundle.putString("subreddit", str2);
        bundle.putString("url", str3);
        return bundle;
    }

    public static String getSafLocation(String str) {
        return PreferencesHelper.getAccountPrefs("MediaLocationSelectionBottomSheetFragment").getString(getSafKey(str), null);
    }

    public static String getSafPrefix() {
        return "SAF_";
    }

    protected static String getSafKey(String str) {
        return getSafPrefix() + str;
    }

    public static boolean hasSafLocation(String str) {
        return PreferencesHelper.getAccountPrefs("MediaLocationSelectionBottomSheetFragment").contains(getSafKey(str));
    }

    public static void removeSafLocation(String str) {
        PreferencesHelper.getAccountPrefs("MediaLocationSelectionBottomSheetFragment").edit().remove(getSafKey(str)).apply();
    }

    public static void saveSafLocation(String str, String str2) {
        PreferencesHelper.getAccountPrefs("MediaLocationSelectionBottomSheetFragment").edit().putString(getSafKey(str), str2).apply();
    }

    @Override // com.resync.app.dialogs.AbstractLocationSelectionBottomSheetFragment
    protected void onLocationSaved() {
        if (StringUtils.isNotEmpty(this.url)) {
            if (this.url.contains(",")) {
                DownloadAlbumJob.download(getChildFragmentManager(), this.title, this.subreddit, this.url, 1);
            } else if (LinkTypeHelper.isRedditVideo(this.url)) {
                DownloadRedditVideoJob.download(getChildFragmentManager(), this.title, this.subreddit, this.url, 1);
            } else {
                DownloadMediaJob.download(getChildFragmentManager(), this.title, this.subreddit, this.url, 1);
            }
        }
    }

    @Override // com.resync.app.dialogs.AbstractLocationSelectionBottomSheetFragment
    protected void saveLocation(String str) {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("MediaLocationSelectionBottomSheetFragment").edit();
        Iterator<? extends Map.Entry<String, ?>> it = PreferencesHelper.getAccountPrefs("MediaLocationSelectionBottomSheetFragment").getAll().entrySet().iterator();
        while (it.hasNext()) {
            String key = it.next().getKey();
            if (StringUtils.startsWith(key, getSafPrefix())) {
                editorEdit.remove(key);
            }
        }
        editorEdit.putString("directory_media", str);
        editorEdit.apply();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Select directory";
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        if (getArguments() != null) {
            this.title = getArguments().getString("title");
            this.subreddit = getArguments().getString("subreddit");
            this.url = getArguments().getString("url");
        }
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Select a download directory";
    }

    @Override // com.resync.app.dialogs.AbstractLocationSelectionBottomSheetFragment, com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "Select a directory where you want download media to be stored on your device";
    }
}
