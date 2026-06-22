package com.resync.app.jobs.media;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.app.NotificationCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentManager;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.common.util.concurrent.ListenableFuture;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.ImageManager;
import com.resync.app.media.MediaSafHelper;
import com.resync.app.media.RootDirectoryException;
import com.resync.app.helpers.Callback;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.dialogs.MediaLocationSelectionBottomSheetFragment;
import com.resync.app.util.Logger;
import com.resync.app.helpers.messaging.MessagingHelper;

/* JADX INFO: loaded from: classes.dex */
public class DownloadAlbumJob extends AbstractDownloadMediaJob {
    private String url;

    class WorkCompleter implements Callback {

        final /* synthetic */ CallbackToFutureAdapter.Completer completer;

        WorkCompleter(CallbackToFutureAdapter.Completer completer) {
            this.completer = completer;
        }

        @Override // com.resync.app.helpers.Callback
        public void onSuccess() {
            this.completer.set(ListenableWorker.Result.success());
        }

        @Override // com.resync.app.helpers.Callback
        public void onFinished() {
            this.completer.set(ListenableWorker.Result.failure());
        }
    }

    class DownloadThread extends Thread {

        final /* synthetic */ Callback callback;

        DownloadThread(Callback callback) {
            this.callback = callback;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            DownloadAlbumJob.this.saveAlbum(this.callback);
        }
    }

    class ImageFileListener implements RequestListener<File> {

        final /* synthetic */ Callback callback;
        final /* synthetic */ DocumentFile destinationDir;
        final /* synthetic */ int total;

        final /* synthetic */ ArrayList remaining;

        final /* synthetic */ String filename;

        ImageFileListener(Callback callback, DocumentFile documentFile, int total, ArrayList arrayList, String str) {
            this.callback = callback;
            this.destinationDir = documentFile;
            this.total = total;
            this.remaining = arrayList;
            this.filename = str;
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(File file, Object obj, Target<File> target, DataSource dataSource, boolean z) {
            DocumentFile documentFileE = MediaSafHelper.createFile(this.destinationDir, this.filename, this.total - this.remaining.size());
            if (documentFileE == null) {
                DownloadAlbumJob.this.downloadNext(this.callback, this.destinationDir, this.total, this.remaining);
                return false;
            }
            try {
                FileUtils.copyFile(file, RedditApplication.getAppContext().getContentResolver().openOutputStream(documentFileE.getUri()));
            } catch (Exception e) {
                Logger.logException(e);
            }
            DownloadAlbumJob.this.downloadNext(this.callback, this.destinationDir, this.total, this.remaining);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<File> target, boolean z) {
            DownloadAlbumJob.this.downloadNext(this.callback, this.destinationDir, this.total, this.remaining);
            return false;
        }
    }

    public DownloadAlbumJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    public static void enqueue(String str, String str2, String str3, int mode) {
        WorkManagerHelper.enqueue(AbstractDownloadMediaJob.buildWorkRequest(DownloadAlbumJob.class, "download_album", str, str2, str3, mode));
    }

    public static void download(FragmentManager fragmentManager, String str, String str2, String str3, int mode) {
        if (mode == 2) {
            throw new RuntimeException("Unsupported mode: " + mode);
        }
        if (MediaSafHelper.hasRootDir()) {
            enqueue(str, str2, str3, mode);
        } else {
            FragmentUtils.showWithArguments(MediaLocationSelectionBottomSheetFragment.class, fragmentManager, MediaLocationSelectionBottomSheetFragment.buildArguments(str, str2, str3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object startWorkInternal(CallbackToFutureAdapter.Completer completer) throws Exception {
        WorkCompleter workCompleter = new WorkCompleter(completer);
        onPrepare();
        showProgress(com.resync.app.util.links.LinkHandler.getRedditVideoId(this.url), -1);
        new DownloadThread(workCompleter).start();
        return workCompleter;
    }

    void saveAlbum(Callback callback) {
        String cacheFilename = ImageManager.md5(getUrl());
        Logger.verbose("Cache filename: " + cacheFilename);
        try {
            DocumentFile documentFileCreateDirectory = MediaSafHelper.getOrCreateSubdirectory(getSubredditFolder()).createDirectory(cacheFilename);
            if (SettingsSingleton.getSettings().saveNomedia) {
                documentFileCreateDirectory.createFile("sync/reddit", ".nomedia");
            }
            ArrayList<String> arrayList = new ArrayList<>();
            Collections.addAll(arrayList, StringUtils.split(getUrl(), ","));
            downloadNext(callback, documentFileCreateDirectory, arrayList.size(), arrayList);
        } catch (RootDirectoryException e) {
            Logger.logException(e);
            showError(callback, "Cannot write to selected directory (permission error)", false);
        } catch (Exception e) {
            Logger.logException(e);
            showError(callback, "Error creating directory", true);
        }
    }

    void downloadNext(Callback callback, DocumentFile documentFile, int total, ArrayList<String> arrayList) {
        if (arrayList.isEmpty()) {
            showAlbumSavedNotification(total + " images saved", documentFile.getUri());
            callback.onFinished();
            return;
        }
        if (shouldContinue()) {
            showProgressNotification("Downloading album", total, total - arrayList.size());
            String strRemove = arrayList.remove(0);
            Logger.verbose("Downloading: " + strRemove);
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asFile().load(strRemove).priority(Priority.IMMEDIATE).addListener(new ImageFileListener(callback, documentFile, total, arrayList, strRemove)).submit();
        }
    }

    void showAlbumSavedNotification(String str, Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(uri);
        intent.setFlags(1);
        NotificationCompat.Builder eVarH = new NotificationCompat.Builder(context(), NotificationHelper.getGenericChannel()).setColor(MessagingHelper.NOTIFICATION_COLOR).setSmallIcon(R.drawable.ic_image_white_24dp).setWhen(System.currentTimeMillis()).setContentTitle(getSuccessTitle()).setContentText(str).setAutoCancel(true);
        eVarH.setContentIntent(PendingIntent.getActivities(context(), getNotificationId(), new Intent[]{Intent.createChooser(intent, "Open folder")}, 335544320));
        NotificationManager notificationManager = (NotificationManager) context().getSystemService("notification");
        notificationManager.cancel(getNotificationId());
        notificationManager.notify("final", getNotificationId(), eVarH.build());
    }

    @Override // com.resync.app.jobs2.DownloadJob
    public String getSuccessTitle() {
        return "Album saved";
    }

    @Override // com.resync.app.jobs2.DownloadJob
    public String getProgressTitle() {
        return "Saving album";
    }

    @Override // com.resync.app.jobs2.DownloadJob
    public String getErrorTitle() {
        return "Error saving album";
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        log("DownloadAlbumJob started!");
        this.url = getInputData().getString("url");
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: k8.a
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return DownloadAlbumJob.this.startWorkInternal(completer);
            }
        });
    }
}
