package com.resync.app.jobs;

import android.content.Context;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkerParameters;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.common.util.concurrent.ListenableFuture;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import java.util.concurrent.TimeUnit;
import com.resync.app.events.IconsUpdatedEvent;
import com.resync.app.helpers.Callback;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthUserInfoRequest;
import com.resync.app.model.User;

/* JADX INFO: loaded from: classes.dex */
public class CheckUserJob extends ListenableWorker {

    private static String WORK_TAG = "check_user";

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

    class UserInfoListener implements Response.Listener<User> {

        final /* synthetic */ Callback callback;

        UserInfoListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(User user) {
            BusSingleton.getBus().post(new IconsUpdatedEvent());
            this.callback.onFinished();
        }
    }

    class UserInfoErrorListener implements Response.ErrorListener {

        final /* synthetic */ Callback callback;

        UserInfoErrorListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            this.callback.onFinished();
        }
    }

    public CheckUserJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    private void fetchUserInfo(Callback callback) {
        OAuthRequestManager.sendWithAppContext(new OAuthUserInfoRequest(RedditApplication.getAppContext(), AccountSingleton.getInstance().getUsername(), new UserInfoListener(callback), new UserInfoErrorListener(callback)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object startWorkInternal(CallbackToFutureAdapter.Completer completer) throws Exception {
        WorkCompleter workCompleter = new WorkCompleter(completer);
        fetchUserInfo(workCompleter);
        return workCompleter;
    }

    public static void schedule() {
        Logger.verbose("CheckUserJob", "CheckUserJob scheduling");
        WorkManagerHelper.cancelUniqueWork(WORK_TAG);
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            Logger.verbose("CheckUserJob", "CheckUserJob skipping as we're logged out");
        } else {
            WorkManagerHelper.enqueueUniqueWork(WORK_TAG, ExistingWorkPolicy.REPLACE, new OneTimeWorkRequest.Builder(CheckUserJob.class).setInitialDelay(5L, TimeUnit.SECONDS).setConstraints(Constraints.NONE).setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30000L, TimeUnit.MILLISECONDS).build());
        }
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        Logger.verbose("CheckUserJob", "CheckUserJob started!");
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: j8.c
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return CheckUserJob.this.startWorkInternal(completer);
            }
        });
    }
}
