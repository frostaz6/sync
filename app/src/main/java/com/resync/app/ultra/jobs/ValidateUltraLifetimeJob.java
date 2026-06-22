package com.resync.app.ultra.jobs;

import android.content.Context;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.functions.FirebaseFunctions;
import java.util.HashMap;
import com.resync.app.helpers.Callback;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class ValidateUltraLifetimeJob extends ListenableWorker {

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

    public ValidateUltraLifetimeJob(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    private void runJob(final Callback callback) {
        // LIBERATED: this was the lifetime-purchase "confirmation" call against Sync's dead
        // Ultra backend (Firebase callable "checkLifetimeCallable"). Ultra is force-owned
        // locally, so there is nothing to validate. No-op to a successful completion so the
        // worker never touches the dead host (no hang/retry). a() == Result.success().
        callback.onSuccess();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object provideCallbackFuture(CallbackToFutureAdapter.Completer completer) throws Exception {
        CompleterCallback completerCallback = new CompleterCallback(completer);
        runJob(completerCallback);
        return completerCallback;
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        Logger.verbose("iap_helper", getClass().getSimpleName() + " started!");
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: vc.g
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return ValidateUltraLifetimeJob.this.provideCallbackFuture(completer);
            }
        });
    }
}
