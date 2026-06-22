package com.resync.app.jobs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.app.NotificationCompat;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.common.util.concurrent.ListenableFuture;
import com.resync.app.RedditApplication;
import com.resync.app.receiver.CancelWatchReceiver;
import com.resync.app.ui.activities.CasualActivity;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.Callback;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.IconHelper;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.helpers.WatchHelper;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthLatestNewPostRequest;

/* JADX INFO: loaded from: classes.dex */
public class WatchSubredditJob extends ListenableWorker {

    private static String WORK_TAG = "watch_subreddits";

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

    class LatestPostListener implements Response.Listener<String> {

        final /* synthetic */ String subreddit;
        final /* synthetic */ Callback callback;
        final /* synthetic */ Iterator iterator;

        LatestPostListener(String str, Callback callback, Iterator it) {
            this.subreddit = str;
            this.callback = callback;
            this.iterator = it;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            boolean updated = !StringUtils.equals(WatchHelper.getWatchedColor(this.subreddit), str);
            Logger.verbose("WatchSubredditJob", "Last post in " + this.subreddit + " updated: " + updated);
            WatchHelper.addOrUpdateWatch(this.subreddit, str);
            if (updated) {
                WatchSubredditJob.showNewPostsNotification(RedditApplication.getAppContext(), this.subreddit);
            }
            WatchSubredditJob.this.checkNextSubreddit(this.callback, this.iterator);
        }
    }

    class LatestPostErrorListener implements Response.ErrorListener {

        final /* synthetic */ Callback callback;

        LatestPostErrorListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            Logger.verbose("WatchSubredditJob", "Encountered an error: " + volleyError);
            this.callback.onSuccess();
        }
    }

    public WatchSubredditJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkNextSubreddit(Callback callback, Iterator<String> it) {
        Logger.verbose("WatchSubredditJob", "Starting checkWatchedSubreddits inner");
        if (!it.hasNext()) {
            Logger.verbose("WatchSubredditJob", "There are no subreddits left to check!");
            callback.onFinished();
            WorkManagerHelper.cancelUniqueWork(WORK_TAG);
        } else {
            String next = it.next();
            Logger.verbose("WatchSubredditJob", "Checking the next subreddit: " + next);
            OAuthRequestManager.send(RedditApplication.getAppContext(), new OAuthLatestNewPostRequest(RedditApplication.getAppContext(), next, new LatestPostListener(next, callback, it), new LatestPostErrorListener(callback)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object startWorkInternal(CallbackToFutureAdapter.Completer completer) throws Exception {
        WorkCompleter workCompleter = new WorkCompleter(completer);
        checkNextSubreddit(workCompleter, WatchHelper.getWatchedKeys().iterator());
        return workCompleter;
    }

    private static void scheduleTimedJob(Context context) {
        Logger.verbose("WatchSubredditJob", "Scheduling timed job");
        WorkManagerHelper.enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, new PeriodicWorkRequest.Builder(WatchSubredditJob.class, 15L, TimeUnit.MINUTES).setInitialDelay(30L, TimeUnit.SECONDS).setConstraints(Constraints.NONE).setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30000L, TimeUnit.MILLISECONDS).build());
    }

    public static void schedule(Context context) {
        Logger.verbose("WatchSubredditJob", "Setting up the watch Subreddit job");
        if (!WatchHelper.hasWatches()) {
            Logger.verbose("WatchSubredditJob", "There are no longer subreddits to watch");
        } else {
            Logger.verbose("WatchSubredditJob", "Scheduling an immediate job");
            scheduleTimedJob(context);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showNewPostsNotification(Context context, String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setChannelId(NotificationHelper.getPassiveChannel());
        builder.setSmallIcon(IconHelper.getSystemIconRes());
        builder.setWhen(jCurrentTimeMillis);
        builder.setContentTitle("/r/" + str + " has new posts");
        builder.setTicker("/r/" + str + " has new posts");
        Intent[] intentArr = {new Intent(context, (Class<?>) CasualActivity.class)};
        intentArr[0].putExtra("url", str);
        intentArr[0].putExtra("sort_new", true);
        int notificationId = (int) jCurrentTimeMillis;
        builder.setContentIntent(PendingIntent.getActivities(context, notificationId, intentArr, 201326592));
        Intent intent = new Intent(context, (Class<?>) CancelWatchReceiver.class);
        intent.putExtra("subreddit", str);
        intent.putExtra("notification_id", notificationId);
        builder.addAction(0, "Stop watching", PendingIntent.getBroadcast(context, notificationId, intent, 335544320));
        Notification notificationC = builder.build();
        notificationC.flags |= 16;
        ((NotificationManager) context.getSystemService("notification")).notify(notificationId, notificationC);
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        Logger.verbose("WatchSubredditJob", "WatchSubredditJob started!");
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: j8.g
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return WatchSubredditJob.this.startWorkInternal(completer);
            }
        });
    }
}
