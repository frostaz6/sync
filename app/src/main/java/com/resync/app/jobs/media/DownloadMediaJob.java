package com.resync.app.jobs.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.common.util.concurrent.ListenableFuture;
import com.resync.app.RedditApplication;
import com.resync.app.jobs.media.AbstractDownloadMediaJob;
import com.resync.app.receiver.download.RetryReceiver;
import com.resync.app.reddit.requests.external.requests.gfycat.GrabGfycatRequest;
import com.resync.app.helpers.ui.StaticToast;
import java.io.File;
import com.resync.app.media.ImageErrors;
import com.resync.app.media.MediaSafHelper;
import com.resync.app.media.RootDirectoryException;
import com.resync.app.media.SubdirectoryException;
import com.resync.app.helpers.Callback;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.thirdparty.CheckDeviantArtRequest;
import com.resync.app.net.thirdparty.CheckStreamableRequest;
import com.resync.app.net.thirdparty.XkcdRequest;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.net.thirdparty.RedgifRequestWrapper;
import com.resync.app.dialogs.MediaLocationSelectionBottomSheetFragment;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes.dex */
public class DownloadMediaJob extends AbstractDownloadMediaJob {

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

    class BitmapListener implements RequestListener<Bitmap> {

        final /* synthetic */ Callback callback;

        BitmapListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
            if (1 != DownloadMediaJob.this.getMode() && 2 != DownloadMediaJob.this.getMode() && 3 != DownloadMediaJob.this.getMode()) {
                return false;
            }
            DownloadMediaJob.this.downloadFile(this.callback, bitmap);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
            DownloadMediaJob.this.showError(this.callback, ImageErrors.getMessage(37), true);
            return false;
        }
    }

    class FileListener implements RequestListener<File> {

        final /* synthetic */ Callback callback;
        final /* synthetic */ Bitmap bitmap;

        FileListener(Callback callback, Bitmap bitmap) {
            this.callback = callback;
            this.bitmap = bitmap;
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(File file, Object obj, Target<File> target, DataSource dataSource, boolean z) {
            if (1 != DownloadMediaJob.this.getMode() && 2 != DownloadMediaJob.this.getMode() && 3 != DownloadMediaJob.this.getMode()) {
                return false;
            }
            DownloadMediaJob.this.onFileReady(this.callback, this.bitmap, file);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<File> target, boolean z) {
            android.util.Log.e("DownloadMediaJob", "Glide asFile load failed", glideException);
            DownloadMediaJob.this.showError(this.callback, ImageErrors.getMessage(37), true);
            return false;
        }
    }

    class SaveThread extends Thread {

        final /* synthetic */ File file;
        final /* synthetic */ Bitmap bitmap;
        final /* synthetic */ Callback callback;

        SaveThread(File file, Bitmap bitmap, Callback callback) {
            this.file = file;
            this.bitmap = bitmap;
            this.callback = callback;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (DownloadMediaJob.this.getMode() != 1) {
                if (DownloadMediaJob.this.getMode() == 2 || DownloadMediaJob.this.getMode() == 3) {
                    try {
                        DownloadMediaJob downloadMediaJob = DownloadMediaJob.this;
                        AbstractDownloadMediaJob.ShareInfo shareInfo = downloadMediaJob.copyForSharing(this.file, downloadMediaJob.getUrl());
                        shareInfo.clipboard = DownloadMediaJob.this.getMode() == 3;
                        DownloadMediaJob.this.share(shareInfo);
                        this.callback.onFinished();
                        return;
                    } catch (Exception e) {
                        android.util.Log.e("DownloadMediaJob", "share/clipboard copy failed", e);
                        DownloadMediaJob.this.showError(this.callback, "Error copying file", true);
                        return;
                    }
                }
                return;
            }
            try {
                DownloadMediaJob downloadMediaJob2 = DownloadMediaJob.this;
                AbstractDownloadMediaJob.ShareInfo shareInfo = downloadMediaJob2.saveToStorage(this.file, downloadMediaJob2.getSubredditFolder(), DownloadMediaJob.this.getUrl());
                DownloadMediaJob downloadMediaJob3 = DownloadMediaJob.this;
                downloadMediaJob3.showSuccessNotification(this.bitmap, downloadMediaJob3.getUrl(), shareInfo.uri());
                this.callback.onFinished();
            } catch (RootDirectoryException unused2) {
                DownloadMediaJob.this.showError(this.callback, "Cannot write to selected directory (permission error)", false);
            } catch (SubdirectoryException unused3) {
                Intent intent = new Intent(DownloadMediaJob.this.context(), (Class<?>) RetryReceiver.class);
                intent.putExtra("id", DownloadMediaJob.this.getNotificationId());
                intent.putExtra("title", DownloadMediaJob.this.getTitle());
                intent.putExtra("subreddit", DownloadMediaJob.this.getSubredditFolder());
                intent.putExtra("url", DownloadMediaJob.this.getUrl());
                intent.putExtra("mode", DownloadMediaJob.this.getMode());
                DownloadMediaJob.this.context().sendBroadcast(intent);
            } catch (Exception unused4) {
                DownloadMediaJob.this.showError(this.callback, "Unknown error while copying file", true);
            }
        }
    }

    public DownloadMediaJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    public static void enqueue(String str, String str2, String str3, int mode) {
        if (mode == 1) {
            StaticToast.show("Downloading media");
        } else if (mode != 2 && mode != 3) {
            throw new RuntimeException("Unsupported mode");
        }
        WorkManagerHelper.enqueue(AbstractDownloadMediaJob.buildWorkRequest(DownloadMediaJob.class, "download_media", str, str2, str3, mode));
    }

    public static void download(FragmentManager fragmentManager, String str, String str2, String str3, int mode) {
        if (mode != 1 || MediaSafHelper.hasRootDir()) {
            enqueue(str, str2, str3, mode);
        } else {
            FragmentUtils.showWithArguments(MediaLocationSelectionBottomSheetFragment.class, fragmentManager, MediaLocationSelectionBottomSheetFragment.buildArguments(str, str2, str3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onDeviantArtResolved(Callback callback, String str) {
        setUrl(str);
        startDownload(callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onXkcdError(Callback callback, VolleyError volleyError) {
        showError(callback, "Media not available", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onDeviantArtError(Callback callback, VolleyError volleyError) {
        showError(callback, "Media not available", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onRedgifResolved(Callback callback, String str) {
        setUrl(str);
        startDownload(callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onRedgifError(Callback callback, VolleyError volleyError) {
        showError(callback, "Media not available", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onGfycatResolved(Callback callback, String str) {
        Logger.verbose("RESPONSEA: " + str);
        setUrl(str);
        startDownload(callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onGfycatError(Callback callback, VolleyError volleyError) {
        showError(callback, "Media not available", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onStreamableResolved(Callback callback, String str) {
        setUrl(str);
        startDownload(callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onStreamableError(Callback callback, VolleyError volleyError) {
        showError(callback, "Media not available", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void onXkcdResolved(Callback callback, Pair pair) {
        setUrl((String) pair.second);
        startDownload(callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object startWorkInternal(CallbackToFutureAdapter.Completer completer) throws Exception {
        WorkCompleter workCompleter = new WorkCompleter(completer);
        if (getMode() == 1) {
            showProgress(null, -1);
        }
        resolveAndDownload(workCompleter, getInputData().getString("url"));
        return workCompleter;
    }

    void downloadFile(Callback callback, Bitmap bitmap) {
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asFile().load(getUrl()).priority(Priority.IMMEDIATE).addListener(new FileListener(callback, bitmap)).submit();
    }

    void downloadBitmap(Callback callback) {
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asBitmap().load(getUrl()).priority(Priority.IMMEDIATE).addListener(new BitmapListener(callback)).submit();
    }

    void resolveAndDownload(final Callback callback, String str) {
        Logger.verbose("MEDIA: " + str);
        if (LinkTypeHelper.isDeviantArt(str)) {
            RedditApplication.thirdPartyRequestQueue.add(new CheckDeviantArtRequest(str, new Response.Listener<String>() { // from class: k8.m
                @Override // com.android.volley.Response.Listener
                public final void onResponse(String obj) {
                    DownloadMediaJob.this.onDeviantArtResolved(callback, (String) obj);
                }
            }, new Response.ErrorListener() { // from class: k8.i
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    DownloadMediaJob.this.onDeviantArtError(callback, volleyError);
                }
            }));
            return;
        }
        if (LinkTypeHelper.isRedgifs(str)) {
            OAuthRequestManager.sendRedgif(new RedgifRequestWrapper(str, true, new Response.Listener<String>() { // from class: k8.k
                @Override // com.android.volley.Response.Listener
                public final void onResponse(String obj) {
                    DownloadMediaJob.this.onRedgifResolved(callback, (String) obj);
                }
            }, new Response.ErrorListener() { // from class: k8.h
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    DownloadMediaJob.this.onRedgifError(callback, volleyError);
                }
            }));
            return;
        }
        if (LinkTypeHelper.isGfycat(str)) {
            RedditApplication.thirdPartyRequestQueue.add(new GrabGfycatRequest(str, true, new Response.Listener<String>() { // from class: k8.d
                @Override // com.android.volley.Response.Listener
                public final void onResponse(String obj) {
                    DownloadMediaJob.this.onGfycatResolved(callback, (String) obj);
                }
            }, new Response.ErrorListener() { // from class: k8.e
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    DownloadMediaJob.this.onGfycatError(callback, volleyError);
                }
            }));
            return;
        }
        if (LinkTypeHelper.isStreamable(str)) {
            RedditApplication.thirdPartyRequestQueue.add(new CheckStreamableRequest(str, new Response.Listener<String>() { // from class: k8.l
                @Override // com.android.volley.Response.Listener
                public final void onResponse(String obj) {
                    DownloadMediaJob.this.onStreamableResolved(callback, (String) obj);
                }
            }, new Response.ErrorListener() { // from class: k8.g
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    DownloadMediaJob.this.onStreamableError(callback, volleyError);
                }
            }));
        } else if (LinkTypeHelper.isImgflip(str)) {
            RedditApplication.thirdPartyRequestQueue.add(new XkcdRequest(str, new Response.Listener<Pair<String, String>>() { // from class: k8.j
                @Override // com.android.volley.Response.Listener
                public final void onResponse(Pair<String, String> obj) {
                    DownloadMediaJob.this.onXkcdResolved(callback, (Pair) obj);
                }
            }, new Response.ErrorListener() { // from class: k8.f
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    DownloadMediaJob.this.onXkcdError(callback, volleyError);
                }
            }));
        } else {
            setUrl(str);
            startDownload(callback);
        }
    }

    @Override // com.resync.app.jobs2.DownloadJob
    public String getSuccessTitle() {
        return "Media saved";
    }

    @Override // com.resync.app.jobs2.DownloadJob
    public String getProgressTitle() {
        return "Saving media";
    }

    @Override // com.resync.app.jobs2.DownloadJob
    public String getErrorTitle() {
        return "Error saving media";
    }

    void onFileReady(Callback callback, Bitmap bitmap, File file) {
        super.cancelNotification();
        new SaveThread(file, bitmap, callback).start();
    }

    void startDownload(Callback callback) {
        if (shouldContinue()) {
            log("Attempting to download media: " + getUrl());
            downloadBitmap(callback);
        }
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        log("DownloadMediaJob started: " + getNotificationId());
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: k8.c
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return DownloadMediaJob.this.startWorkInternal(completer);
            }
        });
    }
}
