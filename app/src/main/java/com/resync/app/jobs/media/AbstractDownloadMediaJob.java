package com.resync.app.jobs.media;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkerParameters;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.receiver.download.CancelledReceiver;
import com.resync.app.receiver.download.DeleteReceiver;
import com.resync.app.receiver.download.RetryReceiver;
import com.resync.app.receiver.download.ShareReceiver;
import com.resync.app.singleton.SettingsSingleton;
import java.io.File;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.ImageManager;
import com.resync.app.media.MediaSafHelper;
import com.resync.app.media.RootDirectoryException;
import com.resync.app.media.SubdirectoryException;
import com.resync.app.helpers.Callback;
import com.resync.app.jobs2.DownloadJob;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.util.Logger;
import com.resync.app.helpers.messaging.MessagingHelper;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractDownloadMediaJob extends ListenableWorker implements DownloadJob {

    private final int notificationId;

    private String subreddit;
    private int mode;

    private String title;
    private String url;
    private boolean errorEncountered;

    public static class ShareInfo implements Serializable {

        public String title;
        public String uriString;
        public String mimeType;

        public boolean clipboard;

        public ShareInfo(String str, Uri uri, String str2) {
            this.title = str;
            this.uriString = uri.toString();
            this.mimeType = str2;
        }

        public Uri uri() {
            return Uri.parse(this.uriString);
        }
    }

    public AbstractDownloadMediaJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
        this.notificationId = (int) System.currentTimeMillis();
        setTitle(getInputData().getString("title"));
        setSubreddit(getInputData().getString("subreddit"));
        setMode(getInputData().getInt("mode", 1));
        setUrl(getInputData().getString("url"));
    }

    public static Data buildInputData(String str, String str2, String str3, int mode) {
        Data.Builder builder = new Data.Builder();
        builder.putString("title", str);
        builder.putString("subreddit", str2);
        builder.putString("url", str3);
        builder.putInt("mode", mode);
        return builder.build();
    }

    private String tag() {
        return getClass().getSimpleName();
    }

    public static OneTimeWorkRequest buildWorkRequest(Class<? extends ListenableWorker> cls, String str, String str2, String str3, String str4, int mode) {
        return new OneTimeWorkRequest.Builder(cls).setInitialDelay(0L, TimeUnit.SECONDS).setConstraints(Constraints.NONE).addTag(str).setInputData(buildInputData(str2, str3, str4, mode)).build();
    }

    void showError(Callback callback, String str, boolean z) {
        if (shouldContinue()) {
            this.errorEncountered = true;
            logError(str);
            NotificationCompat.Builder eVarH = new NotificationCompat.Builder(context(), NotificationHelper.getErrorChannel()).setColor(MessagingHelper.NOTIFICATION_COLOR).setContentTitle(getErrorTitle()).setContentText(str).setSmallIcon(R.drawable.ic_image_white_24dp).setWhen(System.currentTimeMillis()).setAutoCancel(true);
            if (z) {
                Intent intent = new Intent(context(), (Class<?>) RetryReceiver.class);
                intent.putExtra("id", getNotificationId());
                intent.putExtra("title", getTitle());
                intent.putExtra("subreddit", getSubredditFolder());
                intent.putExtra("url", getUrl());
                intent.putExtra("mode", getMode());
                eVarH.addAction(R.drawable.baseline_repeat_24, "Retry", PendingIntent.getBroadcast(context(), getNotificationId(), intent, 335544320));
            }
            NotificationManager notificationManager = (NotificationManager) context().getSystemService("notification");
            notificationManager.cancel(getNotificationId());
            notificationManager.notify("error", getNotificationId(), eVarH.build());
            callback.onSuccess();
        }
    }

    void showProgress(String str, int progress) {
        showProgressNotification(str, 100, progress);
    }

    void showProgressNotification(String str, int max, int progress) {
        if (shouldContinue()) {
            log(str + " - " + progress);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context(), NotificationHelper.getGenericChannel()).setColor(MessagingHelper.NOTIFICATION_COLOR).setSmallIcon(R.drawable.ic_image_white_24dp).setWhen(System.currentTimeMillis());
            builder.setContentTitle(getProgressTitle());
            if (StringUtils.isNotEmpty(str)) {
                builder.setContentText(str);
            }
            Intent intent = new Intent(context(), (Class<?>) CancelledReceiver.class);
            intent.putExtra("id", getNotificationId());
            intent.putExtra("job_uid", getId());
            builder.setDeleteIntent(PendingIntent.getBroadcast(context(), getNotificationId(), intent, 201326592));
            if (progress == -1) {
                builder.setProgress(max, max, true);
            } else {
                builder.setProgress(max, progress, false);
            }
            Notification notificationE = builder.build();
            notificationE.flags |= 16;
            ((NotificationManager) context().getSystemService("notification")).notify(getNotificationId(), notificationE);
        }
    }

    void onPrepare() {
    }

    protected ShareInfo saveToStorage(File file, String str, String str2) throws RootDirectoryException, SubdirectoryException {
        try {
            String mimeType = MediaSafHelper.getMimeType(str2);
            Uri uriD = MediaSafHelper.createDocument(str, str2);
            if (uriD == null) {
                return null;
            }
            Logger.verbose("SUCCESS: " + file.setLastModified(System.currentTimeMillis()));
            FileUtils.copyFile(file, RedditApplication.getAppContext().getContentResolver().openOutputStream(uriD));
            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    uriD = MediaStore.getMediaUri(context(), uriD);
                } catch (Exception e) {
                    Logger.logException(e);
                }
            }
            return new ShareInfo(this.title, uriD, mimeType);
        } catch (RootDirectoryException unused) {
            throw new RootDirectoryException();
        } catch (SubdirectoryException unused2) {
            throw new SubdirectoryException();
        } catch (Exception e) {
            Logger.logException(e);
            throw new RuntimeException("Error copying file");
        }
    }

    protected ShareInfo copyForSharing(File file, String str) {
        try {
            String mimeType = MediaSafHelper.getMimeType(str);
            File file2 = new File(RedditApplication.getAppContext().getExternalCacheDir(), "shared/" + ImageManager.md5(str) + MediaSafHelper.getExtensionForMime(mimeType));
            FileUtils.copyFile(file, file2, false);
            return new ShareInfo(this.title, FileProvider.getUriForFile(RedditApplication.getAppContext(), "com.resync.app.provider", file2), mimeType);
        } catch (Exception e) {
            Logger.logException(e);
            throw new RuntimeException("Error copying file");
        }
    }

    protected void logError(String str) {
        Logger.debug(tag(), str);
    }

    protected Context context() {
        return RedditApplication.getAppContext();
    }

    public int getMode() {
        return this.mode;
    }

    public int getNotificationId() {
        return this.notificationId;
    }

    public String getSubredditFolder() {
        int mode = this.mode;
        if (mode == 2 || mode == 3) {
            return "shared";
        }
        if (SettingsSingleton.getSettings().subFolders) {
            return this.subreddit;
        }
        return null;
    }

    public String getTitle() {
        return this.title;
    }

    @Override // androidx.work.ListenableWorker
    public final void onStopped() {
        NotificationManager notificationManager = (NotificationManager) context().getSystemService("notification");
        notificationManager.cancel(getNotificationId());
        notificationManager.cancel("final", getNotificationId());
        notificationManager.cancel("error", getNotificationId());
        Logger.verbose("STOPPED");
    }

    public String getUrl() {
        return this.url;
    }

    protected void log(String str) {
        Logger.verbose(tag(), str);
    }

    void cancelNotification() {
        ((NotificationManager) RedditApplication.getAppContext().getSystemService("notification")).cancel(getNotificationId());
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setSubreddit(String str) {
        this.subreddit = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    void share(ShareInfo shareInfo) {
        ShareReceiver.share(shareInfo);
    }

    protected boolean shouldContinue() {
        if (this.errorEncountered) {
            Logger.verbose("should-continue", "Error encountered");
            return false;
        }
        if (!isStopped()) {
            return true;
        }
        Logger.verbose("should-continue", "Is stopped");
        return false;
    }

    void showSuccessNotification(Bitmap bitmap, String str, Uri uri) {
        if (shouldContinue()) {
            log("Media saved: " + str);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uri, MediaSafHelper.getMimeType(str));
            intent.setFlags(1);
            NotificationCompat.Builder eVarL = new NotificationCompat.Builder(context(), NotificationHelper.getGenericChannel()).setColor(MessagingHelper.NOTIFICATION_COLOR).setSmallIcon(R.drawable.ic_image_white_24dp).setWhen(System.currentTimeMillis()).setContentTitle(getSuccessTitle()).setContentText(str);
            eVarL.setContentIntent(PendingIntent.getActivities(context(), getNotificationId(), new Intent[]{intent}, 335544320));
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType(MediaSafHelper.getMimeType(str));
            intent2.putExtra("android.intent.extra.STREAM", uri);
            intent2.setAction("android.intent.action.SEND");
            intent2.addFlags(1);
            intent2.setType(MediaSafHelper.getMimeType(str));
            eVarL.addAction(R.drawable.notifcation_share, "Share", PendingIntent.getActivity(context(), getNotificationId(), Intent.createChooser(intent2, null), 335544320));
            Intent intent3 = new Intent(context(), (Class<?>) DeleteReceiver.class);
            intent3.putExtra("uri", uri.toString());
            intent3.putExtra("id", getNotificationId());
            eVarL.addAction(R.drawable.notification_delete, "Delete", PendingIntent.getBroadcast(context(), getNotificationId(), intent3, 335544320));
            Notification notificationC = bitmap != null ? new NotificationCompat.BigPictureStyle(eVarL.setContentTitle(getSuccessTitle()).setContentText(str).setSmallIcon(R.drawable.ic_image_white_24dp).setLargeIcon(bitmap)).bigPicture(bitmap).build() : eVarL.build();
            notificationC.flags |= 16;
            NotificationManager notificationManager = (NotificationManager) context().getSystemService("notification");
            notificationManager.cancel(getNotificationId());
            notificationManager.notify("final", getNotificationId(), notificationC);
        }
    }
}
