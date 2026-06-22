package com.resync.app.ultra.jobs;

import android.content.Context;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkerParameters;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.resync.app.ultra.jobs.UltraGrabTagsJob;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import com.resync.app.managers.NewUserTagManager;
import com.resync.app.helpers.Callback;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class UltraGrabTagsJob extends ListenableWorker {

    private static String TAG = "UltraGrabTagsJob";

    class CompleterCallback implements Callback {

        final /* synthetic */ CallbackToFutureAdapter.Completer completer;

        CompleterCallback(CallbackToFutureAdapter.Completer completer) {
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

    public UltraGrabTagsJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    private void runJob(final Callback callback) {
        // LIBERATED: this POSTed the purchase_token to Sync's dead Ultra backend (Firebase
        // callable "getTags") to download the server-side user-tag catalog. The server is gone,
        // so it can only fail+retry. Local tags (create/edit/remove/display, stored in
        // SharedPreferences via NewUserTagManager) work without it. No-op to success so the
        // worker never touches the dead host. a() == Result.success().
        callback.onSuccess();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onRemoteTagsResult(Callback callback, HttpsCallableResult httpsCallableResult) {
        try {
            if (httpsCallableResult.getData() instanceof HashMap) {
                NewUserTagManager.importTags((HashMap) httpsCallableResult.getData());
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
        callback.onFinished();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object provideCallbackFuture(CallbackToFutureAdapter.Completer completer) throws Exception {
        CompleterCallback completerCallback = new CompleterCallback(completer);
        runJob(completerCallback);
        return completerCallback;
    }

    public static void schedule() {
        // LIBERATED: do NOT enqueue. The job only pulled the server tag catalog from Sync's
        // dead Ultra backend; enqueuing it just produces failing, exponentially-retrying work.
        // Cancel any persisted instance from a prior install and return. Local tags are
        // unaffected (they live in SharedPreferences, populated by user actions via
        // NewUserTagManager).
        android.util.Log.i("UltraGrabTagsJob", "scheduling suppressed (dead Ultra server)");
        WorkManagerHelper.cancelUniqueWork(TAG);
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        Logger.verbose("iap_helper", getClass().getSimpleName() + " started!");
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: vc.d
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return UltraGrabTagsJob.this.provideCallbackFuture(completer);
            }
        });
    }
}
