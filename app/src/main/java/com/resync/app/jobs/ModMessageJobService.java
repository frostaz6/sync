package com.resync.app.jobs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.RedditHelper;
import com.google.common.util.concurrent.ListenableFuture;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.activities.MainActivity;
import com.resync.app.helpers.Callback;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthNewModMailRequest;

/* JADX INFO: loaded from: classes.dex */
public class ModMessageJobService extends ListenableWorker {

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

    class SkipCompleter implements Callback {

        final /* synthetic */ CallbackToFutureAdapter.Completer completer;

        SkipCompleter(CallbackToFutureAdapter.Completer completer) {
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

    class ModMailListener implements Response.Listener<String> {

        final /* synthetic */ Callback callback;

        ModMailListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            Logger.verbose(MessageJobService.TAG, "Mod mail body: " + str);
            if (str != null) {
                int iCurrentTimeMillis = (int) System.currentTimeMillis();
                int length = str.split("<br/><br/>").length;
                Intent[] intentArr = {new Intent(RedditApplication.getAppContext(), (Class<?>) MainActivity.class)};
                intentArr[0].putExtra("messaging", 7);
                intentArr[0].setFlags(67108864);
                intentArr[0].addFlags(536870912);
                PendingIntent activities = PendingIntent.getActivities(RedditApplication.getAppContext(), iCurrentTimeMillis, intentArr, 201326592);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(RedditApplication.getAppContext());
                builder.setSmallIcon(R.drawable.ic_security_white_24dp);
                builder.setWhen(System.currentTimeMillis());
                builder.setNumber(length);
                builder.setContentTitle("Sync for reddit ModMail");
                builder.setTicker("New ModMail");
                builder.setContentText("New ModMail");
                builder.setContentIntent(activities);
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(RedditHelper.htmlToTrimmedText(RedditHelper.processMarkdownToHtml(null, str))));
                builder.setPriority(2);
                Notification notificationC = builder.build();
                notificationC.flags |= 16;
                if (PreferencesHelper.getAccountPrefs(RedditApplication.getAppContext(), "MessagingService").getBoolean("message_sound", true)) {
                    notificationC.defaults |= 1;
                }
                if (PreferencesHelper.getAccountPrefs(RedditApplication.getAppContext(), "MessagingService").getBoolean("message_led", true)) {
                    notificationC.ledARGB = -39424;
                    notificationC.ledOnMS = 250;
                    notificationC.ledOffMS = 1000;
                    notificationC.flags |= 1;
                }
                NotificationManager notificationManager = (NotificationManager) RedditApplication.getAppContext().getSystemService("notification");
                notificationManager.cancel(1);
                notificationManager.notify(1, notificationC);
            }
            this.callback.onFinished();
        }
    }

    class ModMailErrorListener implements Response.ErrorListener {

        final /* synthetic */ Callback callback;

        ModMailErrorListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            this.callback.onSuccess();
        }
    }

    public ModMessageJobService(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    private void checkModMail(Callback callback) {
        Logger.verbose(MessageJobService.TAG, "Checking Mod mail");
        OAuthRequestManager.send(RedditApplication.getAppContext(), new OAuthNewModMailRequest(RedditApplication.getAppContext(), new ModMailListener(callback), new ModMailErrorListener(callback)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object startWorkInternal(CallbackToFutureAdapter.Completer completer) throws Exception {
        WorkCompleter workCompleter = new WorkCompleter(completer);
        checkModMail(workCompleter);
        return workCompleter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object skipWork(CallbackToFutureAdapter.Completer completer) throws Exception {
        SkipCompleter skipCompleter = new SkipCompleter(completer);
        skipCompleter.onFinished();
        return skipCompleter;
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        Logger.verbose(MessageJobService.TAG, "MessageJobService started!");
        if (AccountSingleton.isLoggedIn(RedditApplication.getAppContext())) {
            return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: j8.e
                @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                    return ModMessageJobService.this.startWorkInternal(completer);
                }
            });
        }
        Logger.verbose(MessageJobService.TAG, "Not logged in, skipping");
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: j8.f
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return ModMessageJobService.this.skipWork(completer);
            }
        });
    }
}
