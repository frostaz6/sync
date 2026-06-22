package com.resync.app.workers;

import android.content.Context;
import android.content.Intent;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.resync.app.ui.activities.MainActivity;
import java.util.concurrent.TimeUnit;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class RestartWorker extends Worker {

    private Context appContext;

    public RestartWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
        this.appContext = context;
    }

    public static void enqueue(Context context) {
        WorkManagerHelper.enqueue(new OneTimeWorkRequest.Builder(RestartWorker.class).setInitialDelay(1L, TimeUnit.SECONDS).setConstraints(Constraints.NONE).addTag("restart").build());
    }

    @Override // androidx.work.Worker
    public ListenableWorker.Result doWork() {
        Logger.verbose("RestartWorker", "RestartWorker started!");
        Intent intent = new Intent(this.appContext, (Class<?>) MainActivity.class);
        intent.addFlags(335609856);
        this.appContext.startActivity(intent);
        Logger.verbose("RestartWorker", "RestartWorker finished!");
        return ListenableWorker.Result.failure();
    }
}
