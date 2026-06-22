package com.resync.app.jobs.media;

import android.content.Context;
import android.content.Intent;
import androidx.concurrent.futures.CallbackToFutureAdapter;
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
import com.resync.app.helpers.ui.StaticToast;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.Container;
import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.Track;
import org.mp4parser.muxer.builder.DefaultMp4Builder;
import org.mp4parser.muxer.container.mp4.MovieCreator;
import org.mp4parser.muxer.tracks.AppendTrack;
import com.resync.app.media.ImageManager;
import com.resync.app.media.ImageErrors;
import com.resync.app.media.MediaSafHelper;
import com.resync.app.media.RootDirectoryException;
import com.resync.app.media.SubdirectoryException;
import com.resync.app.helpers.Callback;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.RedditVideoDescription;
import com.resync.app.net.thirdparty.RedditVideoRequest;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.MediaLocationSelectionBottomSheetFragment;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class DownloadRedditVideoJob extends AbstractDownloadMediaJob {

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

    class PlaylistListener implements Response.Listener<RedditVideoDescription> {

        final /* synthetic */ Callback callback;

        PlaylistListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(RedditVideoDescription redditVideoDescription) {
            android.util.Log.i("VidDL", "playlist parsed: valid=" + redditVideoDescription.hasVideo() + " video=" + redditVideoDescription.videoUrl + " audio=" + redditVideoDescription.audioUrl);
            if (redditVideoDescription.hasVideo()) {
                DownloadRedditVideoJob.this.downloadVideo(this.callback, redditVideoDescription);
            } else {
                DownloadRedditVideoJob.this.showError(this.callback, "No valid video files found", true);
            }
        }
    }

    class VideoFileListener implements RequestListener<File> {

        final /* synthetic */ Callback callback;
        final /* synthetic */ RedditVideoDescription description;

        VideoFileListener(Callback callback, RedditVideoDescription redditVideoDescription) {
            this.callback = callback;
            this.description = redditVideoDescription;
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(File file, Object obj, Target<File> target, DataSource dataSource, boolean z) {
            android.util.Log.i("VidDL", "video downloaded: " + (file != null ? file.length() : -1) + " bytes -> " + file);
            DownloadRedditVideoJob.this.downloadAudio(this.callback, this.description, file);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<File> target, boolean z) {
            android.util.Log.e("VidDL", "video download FAILED", glideException);
            DownloadRedditVideoJob.this.showError(this.callback, ImageErrors.getMessage(37), true);
            return false;
        }
    }

    class AudioFileListener implements RequestListener<File> {

        final /* synthetic */ Callback callback;
        final /* synthetic */ RedditVideoDescription description;
        final /* synthetic */ File videoFile;

        AudioFileListener(Callback callback, RedditVideoDescription redditVideoDescription, File file) {
            this.callback = callback;
            this.description = redditVideoDescription;
            this.videoFile = file;
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(File file, Object obj, Target<File> target, DataSource dataSource, boolean z) {
            android.util.Log.i("VidDL", "audio downloaded: " + (file != null ? file.length() : -1) + " bytes -> " + file);
            DownloadRedditVideoJob.this.muxVideoAudio(this.callback, this.description, this.videoFile, file);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<File> target, boolean z) {
            android.util.Log.e("VidDL", "audio download FAILED", glideException);
            DownloadRedditVideoJob.this.showError(this.callback, ImageErrors.getMessage(37), true);
            return false;
        }
    }

    class MuxThread extends Thread {

        final /* synthetic */ File videoFile;
        final /* synthetic */ RedditVideoDescription description;
        final /* synthetic */ File audioFile;

        final /* synthetic */ Callback callback;

        MuxThread(File file, RedditVideoDescription redditVideoDescription, File file2, Callback callback) {
            this.videoFile = file;
            this.description = redditVideoDescription;
            this.audioFile = file2;
            this.callback = callback;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                new MovieCreator();
                Movie movieBuild = MovieCreator.build(this.videoFile.getAbsolutePath());
                List<Track> tracks = movieBuild.getTracks();
                movieBuild.setTracks(new LinkedList());
                Iterator<Track> it = tracks.iterator();
                while (it.hasNext()) {
                    movieBuild.addTrack(new AppendTrack(it.next()));
                }
                if (!TextUtils.isEmpty(this.description.audioUrl)) {
                    Iterator<Track> it2 = MovieCreator.build(this.audioFile.getAbsolutePath()).getTracks().iterator();
                    while (it2.hasNext()) {
                        movieBuild.addTrack(new AppendTrack(it2.next()));
                    }
                }
                android.util.Log.i("VidDL", "muxing: videoFile=" + this.videoFile.length() + "b audioFile=" + (this.audioFile != null ? this.audioFile.length() : -1) + "b tracks=" + movieBuild.getTracks().size());
                Container containerBuild = new DefaultMp4Builder().build(movieBuild);
                File file = new File(ImageManager.getImageCacheDir(RedditApplication.getAppContext()), System.currentTimeMillis() + ".mp4");
                FileChannel channel = new FileOutputStream(file).getChannel();
                containerBuild.writeContainer(channel);
                channel.close();
                android.util.Log.i("VidDL", "muxed output=" + file.length() + " bytes -> " + file);
                DownloadRedditVideoJob.this.cancelNotification();
                if (DownloadRedditVideoJob.this.getMode() != 1) {
                    if (DownloadRedditVideoJob.this.getMode() == 2) {
                        try {
                            DownloadRedditVideoJob downloadRedditVideoJob = DownloadRedditVideoJob.this;
                            DownloadRedditVideoJob.this.share(downloadRedditVideoJob.copyForSharing(file, downloadRedditVideoJob.getUrl()));
                            this.callback.onFinished();
                            return;
                        } catch (Exception unused) {
                            DownloadRedditVideoJob.this.showError(this.callback, "Error copying file", true);
                            return;
                        }
                    }
                    return;
                }
                try {
                    try {
                        DownloadRedditVideoJob downloadRedditVideoJob2 = DownloadRedditVideoJob.this;
                        AbstractDownloadMediaJob.ShareInfo aVarE = downloadRedditVideoJob2.saveToStorage(file, downloadRedditVideoJob2.getSubredditFolder(), DownloadRedditVideoJob.this.getUrl());
                        DownloadRedditVideoJob downloadRedditVideoJob3 = DownloadRedditVideoJob.this;
                        downloadRedditVideoJob3.showSuccessNotification(null, downloadRedditVideoJob3.getUrl(), aVarE.uri());
                        this.callback.onFinished();
                        return;
                    } catch (SubdirectoryException unused2) {
                        Intent intent = new Intent(DownloadRedditVideoJob.this.context(), (Class<?>) RetryReceiver.class);
                        intent.putExtra("id", DownloadRedditVideoJob.this.getNotificationId());
                        intent.putExtra("title", DownloadRedditVideoJob.this.getTitle());
                        intent.putExtra("subreddit", DownloadRedditVideoJob.this.getSubredditFolder());
                        intent.putExtra("url", DownloadRedditVideoJob.this.getUrl());
                        intent.putExtra("mode", DownloadRedditVideoJob.this.getMode());
                        DownloadRedditVideoJob.this.context().sendBroadcast(intent);
                        return;
                    }
                } catch (RootDirectoryException unused3) {
                    DownloadRedditVideoJob.this.showError(this.callback, "Cannot write to selected directory (permission error)", false);
                    return;
                } catch (Exception unused4) {
                    DownloadRedditVideoJob.this.showError(this.callback, "Unknown error while copying file", true);
                    return;
                }
            } catch (Exception e) {
                android.util.Log.e("VidDL", "MUX/MovieCreator FAILED", e);
                Logger.logException(e);
                DownloadRedditVideoJob.this.showError(this.callback, "Error merging the video and audio files", true);
            }
        }
    }

    public DownloadRedditVideoJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    public static void enqueue(String str, String str2, String str3, int mode) {
        StaticToast.show("Downloading reddit video");
        WorkManagerHelper.enqueue(AbstractDownloadMediaJob.buildWorkRequest(DownloadRedditVideoJob.class, "download_video", str, str2, str3, mode));
    }

    public static void download(FragmentManager fragmentManager, String str, String str2, String str3, int mode) {
        if (mode != 1 || MediaSafHelper.hasRootDir()) {
            enqueue(str, str2, str3, mode);
        } else {
            FragmentUtils.showWithArguments(MediaLocationSelectionBottomSheetFragment.class, fragmentManager, MediaLocationSelectionBottomSheetFragment.buildArguments(str, str2, str3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onPlaylistError(Callback callback, VolleyError volleyError) {
        showError(callback, "Error grabbing the video playlist", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object startWorkInternal(CallbackToFutureAdapter.Completer completer) throws Exception {
        WorkCompleter workCompleter = new WorkCompleter(completer);
        showProgress(com.resync.app.util.links.LinkHandler.getRedditVideoId(getUrl()), -1);
        requestPlaylist(workCompleter);
        return workCompleter;
    }

    void downloadAudio(Callback callback, RedditVideoDescription redditVideoDescription, File file) {
        if (shouldContinue()) {
            Logger.verbose("Downloading audio: " + redditVideoDescription.audioUrl);
            if (TextUtils.isEmpty(redditVideoDescription.audioUrl)) {
                Logger.verbose("Skipping audio download");
                muxVideoAudio(callback, redditVideoDescription, file, null);
            } else {
                showProgress("Downloading audio", -1);
                Logger.verbose("Downloading audio");
                com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asFile().load(redditVideoDescription.audioUrl).priority(Priority.IMMEDIATE).addListener(new AudioFileListener(callback, redditVideoDescription, file)).submit();
            }
        }
    }

    void downloadVideo(Callback callback, RedditVideoDescription redditVideoDescription) {
        if (shouldContinue()) {
            showProgress("Downloading video", -1);
            android.util.Log.i("VidDL", "video url=" + redditVideoDescription.videoUrl + " audio url=" + redditVideoDescription.audioUrl);
            Logger.verbose("Downloading video: " + redditVideoDescription.videoUrl);
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asFile().load(redditVideoDescription.videoUrl).priority(Priority.IMMEDIATE).addListener(new VideoFileListener(callback, redditVideoDescription)).submit();
        }
    }

    void requestPlaylist(final Callback callback) {
        showProgress("Downloading playlist", -1);
        OAuthRequestManager.enqueueAux(new RedditVideoRequest(getUrl(), true, new PlaylistListener(callback), new Response.ErrorListener() { // from class: k8.o
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                DownloadRedditVideoJob.this.onPlaylistError(callback, volleyError);
            }
        }));
    }

    void muxVideoAudio(Callback callback, RedditVideoDescription redditVideoDescription, File file, File file2) {
        if (shouldContinue()) {
            new MuxThread(file, redditVideoDescription, file2, callback).start();
        }
    }

    @Override // com.resync.app.jobs2.DownloadJob
    public String getSuccessTitle() {
        return "Reddit video saved";
    }

    @Override // com.resync.app.jobs2.DownloadJob
    public String getProgressTitle() {
        return "Saving reddit video";
    }

    @Override // com.resync.app.jobs2.DownloadJob
    public String getErrorTitle() {
        return "Error saving reddit video";
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        log("DownloadRedditVideoJob started!");
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: k8.n
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return DownloadRedditVideoJob.this.startWorkInternal(completer);
            }
        });
    }
}
