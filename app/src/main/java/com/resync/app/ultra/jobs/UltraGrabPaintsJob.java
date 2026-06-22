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
import com.resync.app.ultra.jobs.UltraGrabPaintsJob;
import com.resync.app.premium.UserPaintManager;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import com.resync.app.helpers.Callback;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class UltraGrabPaintsJob extends ListenableWorker {

    private static String TAG = "UltraGrabPaintsJob";

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

    public UltraGrabPaintsJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    private void runJob(final Callback callback) {
        // LIBERATED: this POSTed the purchase_token to Sync's dead Ultra backend (Firebase
        // callable "getPaints") to download the server-side paint catalog. The server is gone,
        // so it can only fail+retry. Local paints (create/edit/remove/display, stored in
        // SharedPreferences via com.resync.app.premium.UserPaintManager) work without it. No-op to success so the worker never
        // touches the dead host. a() == Result.success().
        callback.onSuccess();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onRemotePaintsResult(Callback callback, HttpsCallableResult httpsCallableResult) {
        try {
            if (httpsCallableResult.getData() instanceof HashMap) {
                UserPaintManager.applyRemotePaints((HashMap) httpsCallableResult.getData());
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
        // LIBERATED: do NOT enqueue. The job only pulled the server paint catalog from Sync's
        // dead Ultra backend; enqueuing it just produces failing, exponentially-retrying work.
        // Cancel any persisted instance from a prior install and return. Local paints are
        // unaffected (they live in SharedPreferences, populated by user actions via com.resync.app.premium.UserPaintManager).
        android.util.Log.i("UltraGrabPaintsJob", "scheduling suppressed (dead Ultra server)");
        WorkManagerHelper.cancelUniqueWork(TAG);
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        Logger.verbose("iap_helper", getClass().getSimpleName() + " started!");
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: vc.a
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return UltraGrabPaintsJob.this.provideCallbackFuture(completer);
            }
        });
    }
}
