package com.resync.app.jobs;

import android.content.Context;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkerParameters;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.common.util.concurrent.ListenableFuture;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.oauth.OAuthDefaultSubredditsRequest;
import com.resync.app.net.oauth.OAuthMultiredditsRequest;
import com.resync.app.net.oauth.OAuthSubredditsRequest;
import com.resync.app.managers.FavouriteSubredditManager;
import com.resync.app.managers.FriendsManager;
import com.resync.app.managers.ModeratedSubredditManager;
import com.resync.app.managers.MultiredditManager;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.helpers.Callback;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Multireddits;
import com.resync.app.model.State;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthFriendsRequest;

/* JADX INFO: loaded from: classes.dex */
public class CheckSubredditsJob extends ListenableWorker {

    static String WORK_TAG = "check_subreddits";

    private String cachedUsername;

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

    class DefaultSubredditsListener implements Response.Listener<State> {

        final /* synthetic */ Callback callback;
        final /* synthetic */ SubredditArrayList subreddits;

        DefaultSubredditsListener(Callback callback, SubredditArrayList subredditArrayList) {
            this.callback = callback;
            this.subreddits = subredditArrayList;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(State state) {
            if (CheckSubredditsJob.this.isUsernameStale()) {
                Logger.verbose("CheckSubredditsJob", "Invalid username detected. Aborting");
                this.callback.onFinished();
                return;
            }
            this.subreddits.addAll(state.subscribed);
            if (state.status == 0) {
                SubredditsManager.getInstance().setSubreddits(this.subreddits);
                this.callback.onFinished();
            }
        }
    }

    class DefaultSubredditsErrorListener implements Response.ErrorListener {

        final /* synthetic */ Callback callback;

        DefaultSubredditsErrorListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            this.callback.onFinished();
        }
    }

    class SubredditsListener implements Response.Listener<State> {

        final /* synthetic */ SubredditArrayList subscribed;
        final /* synthetic */ SubredditArrayList favourites;
        final /* synthetic */ SubredditArrayList moderated;

        final /* synthetic */ Callback callback;

        SubredditsListener(SubredditArrayList subredditArrayList, SubredditArrayList subredditArrayList2, SubredditArrayList subredditArrayList3, Callback callback) {
            this.subscribed = subredditArrayList;
            this.favourites = subredditArrayList2;
            this.moderated = subredditArrayList3;
            this.callback = callback;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(State state) {
            this.subscribed.addAll(state.subscribed);
            this.favourites.addAll(state.favourites);
            this.moderated.addAll(state.moderated);
            if (state.status == 0) {
                if (CheckSubredditsJob.this.isUsernameStale()) {
                    Logger.verbose("CheckSubredditsJob", "Invalid username detected. Aborting");
                    this.callback.onFinished();
                } else {
                    SubredditsManager.getInstance().setSubreddits(this.subscribed);
                    ModeratedSubredditManager.getInstance().setModerated(this.moderated);
                    FavouriteSubredditManager.getInstance().addAllFavourites(this.favourites);
                    CheckSubredditsJob.this.fetchMultireddits(this.callback);
                }
            }
        }
    }

    class SubredditsErrorListener implements Response.ErrorListener {

        final /* synthetic */ Callback callback;

        SubredditsErrorListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            CheckSubredditsJob.this.fetchMultireddits(this.callback);
        }
    }

    class MultiredditsListener implements Response.Listener<Multireddits> {

        final /* synthetic */ Callback callback;
        final /* synthetic */ SubredditArrayList subreddits;

        MultiredditsListener(Callback callback, SubredditArrayList subredditArrayList) {
            this.callback = callback;
            this.subreddits = subredditArrayList;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Multireddits multireddits) {
            if (CheckSubredditsJob.this.isUsernameStale()) {
                Logger.verbose("CheckSubredditsJob", "Invalid username detected. Aborting");
                this.callback.onFinished();
            } else {
                this.subreddits.addAll(multireddits.names);
                MultiredditManager.getInstance().setMultireddits(this.subreddits);
                CheckSubredditsJob.this.fetchFriends(this.callback);
            }
        }
    }

    class MultiredditsErrorListener implements Response.ErrorListener {

        final /* synthetic */ Callback callback;

        MultiredditsErrorListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            this.callback.onFinished();
        }
    }

    class FriendsListener implements Response.Listener<String[]> {

        final /* synthetic */ Callback callback;

        FriendsListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String[] strArr) {
            if (CheckSubredditsJob.this.isUsernameStale()) {
                Logger.verbose("CheckSubredditsJob", "Invalid username detected. Aborting");
                this.callback.onFinished();
            } else {
                FriendsManager.getInstance().setFriends(new SubredditArrayList(strArr));
                this.callback.onFinished();
            }
        }
    }

    class FriendsErrorListener implements Response.ErrorListener {

        final /* synthetic */ Callback callback;

        FriendsErrorListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            this.callback.onFinished();
        }
    }

    public CheckSubredditsJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchFriends(Callback callback) {
        OAuthRequestManager.sendWithAppContext(new OAuthFriendsRequest(RedditApplication.getAppContext(), new FriendsListener(callback), new FriendsErrorListener(callback)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchMultireddits(Callback callback) {
        OAuthRequestManager.send(RedditApplication.getAppContext(), new OAuthMultiredditsRequest(RedditApplication.getAppContext(), new MultiredditsListener(callback, new SubredditArrayList()), new MultiredditsErrorListener(callback)));
    }

    private void fetchSubreddits(Callback callback) {
        OAuthRequestManager.send(RedditApplication.getAppContext(), new OAuthSubredditsRequest(RedditApplication.getAppContext(), null, new SubredditsListener(new SubredditArrayList(), new SubredditArrayList(), new SubredditArrayList(), callback), new SubredditsErrorListener(callback)));
    }

    private void fetchDefaultSubreddits(Callback callback) {
        OAuthRequestManager.sendWithAppContext(new OAuthDefaultSubredditsRequest(RedditApplication.getAppContext(), null, new DefaultSubredditsListener(callback, new SubredditArrayList()), new DefaultSubredditsErrorListener(callback)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUsernameStale() {
        Logger.verbose("CheckSubredditsJob", "Cached username: " + this.cachedUsername);
        Logger.verbose("CheckSubredditsJob", "Live username: " + AccountSingleton.getInstance().getUsername());
        return !StringUtils.equalsIgnoreCase(this.cachedUsername, AccountSingleton.getInstance().getUsername());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object startWorkInternal(CallbackToFutureAdapter.Completer completer) throws Exception {
        WorkCompleter workCompleter = new WorkCompleter(completer);
        if (AccountSingleton.getInstance().isLoggedIn()) {
            Logger.verbose("CheckSubredditsJob", "Checking for logged in user");
            fetchSubreddits(workCompleter);
        } else {
            Logger.verbose("CheckSubredditsJob", "Checking for logged out user");
            fetchDefaultSubreddits(workCompleter);
        }
        return workCompleter;
    }

    public static void schedule() {
        Logger.verbose("CheckSubredditsJob", "CheckSubredditsJob scheduling");
        WorkManagerHelper.cancelUniqueWork(WORK_TAG);
        Data.Builder builder = new Data.Builder();
        builder.putString("username", AccountSingleton.getInstance().getUsername());
        WorkManagerHelper.enqueueUniqueWork(WORK_TAG, ExistingWorkPolicy.REPLACE, new OneTimeWorkRequest.Builder(CheckSubredditsJob.class).setInitialDelay(0L, TimeUnit.SECONDS).setConstraints(Constraints.NONE).setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30000L, TimeUnit.MILLISECONDS).setInputData(builder.build()).build());
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        Logger.verbose("CheckSubredditsJob", "CheckSubredditsJob started!");
        this.cachedUsername = getInputData().getString("username");
        Logger.verbose("CheckSubredditsJob", "Starting with username: " + this.cachedUsername);
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: j8.b
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return CheckSubredditsJob.this.startWorkInternal(completer);
            }
        });
    }
}
