package com.resync.app.workers;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.resync.app.singleton.SettingsSingleton;
import java.util.concurrent.TimeUnit;
import com.resync.app.media.ImageManager;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class ClearCacheWorker extends Worker {
    public ClearCacheWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    public static void enqueue(Context context) {
        WorkManagerHelper.enqueue(new OneTimeWorkRequest.Builder(ClearCacheWorker.class).setInitialDelay(0L, TimeUnit.SECONDS).setConstraints(Constraints.NONE).addTag("clear_cache").build());
    }

    @Override // androidx.work.Worker
    public ListenableWorker.Result doWork() {
        Logger.verbose("ClearCacheWorker", "ClearCacheWorker started!");
        ImageManager.clearOldCache(getApplicationContext(), SettingsSingleton.getSettings().imageCacheDuration);
        Logger.verbose("ClearCacheWorker", "ClearCacheWorker finished!");
        return ListenableWorker.Result.failure();
    }
}
