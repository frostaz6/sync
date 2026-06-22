package com.resync.app.helpers;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import com.resync.app.RedditApplication;
import java.util.UUID;

/* JADX INFO: renamed from: j8.h */
/* JADX INFO: compiled from: WorkManagerHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class WorkManagerHelper {
    public static void cancelUniqueWork(String str) {
        WorkManager.getInstance(RedditApplication.getAppContext()).cancelUniqueWork(str);
    }

    public static void cancelWorkById(UUID uuid) {
        WorkManager.getInstance(RedditApplication.getAppContext()).cancelWorkById(uuid);
    }

    public static void enqueue(WorkRequest workRequest) {
        WorkManager.getInstance(RedditApplication.getAppContext()).enqueue(workRequest);
    }

    public static void enqueueUniquePeriodicWork(String str, ExistingPeriodicWorkPolicy existingPeriodicWorkPolicy, PeriodicWorkRequest periodicWorkRequest) {
        WorkManager.getInstance(RedditApplication.getAppContext()).enqueueUniquePeriodicWork(str, existingPeriodicWorkPolicy, periodicWorkRequest);
    }

    public static void enqueueUniqueWork(String str, ExistingWorkPolicy existingWorkPolicy, OneTimeWorkRequest oneTimeWorkRequest) {
        WorkManager.getInstance(RedditApplication.getAppContext()).enqueueUniqueWork(str, existingWorkPolicy, oneTimeWorkRequest);
    }
}
