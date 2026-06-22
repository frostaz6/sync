package com.resync.app.helpers.messaging;

import android.content.Context;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.resync.app.RedditApplication;
import com.resync.app.jobs.MessageJobService;
import com.resync.app.jobs.ModMessageJobService;
import com.resync.app.singleton.AccountSingleton;
import java.util.concurrent.TimeUnit;
import com.resync.app.managers.ModeratedSubredditManager;
import com.resync.app.helpers.WorkManagerHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: z7.b */
/* JADX INFO: compiled from: MessageSchedulingHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class MessageSchedulingHelper {
    private static int getIntervalMinutes(Context context) {
        int preference = Integer.parseInt(PreferencesHelper.getAccountPrefs(context, "MessagingService").getString("message_preference_new", "3"));
        int intervalMinutes = preference == 1 ? 10 : preference == 2 ? 30 : preference == 3 ? 60 : preference == 4 ? SubsamplingScaleImageView.ORIENTATION_180 : preference == 5 ? 360 : preference == 6 ? 720 : preference == 7 ? 1440 : -1;
        Logger.verbose(MessageJobService.TAG, "Seconds: " + intervalMinutes);
        return intervalMinutes;
    }

    public static void scheduleIfEnabled(Context context) {
        WorkManagerHelper.cancelUniqueWork("scheduled_message");
        WorkManagerHelper.cancelUniqueWork("scheduled_mod_message");
        if (AccountSingleton.getInstance().isLoggedIn() && shouldCheckMessages(context)) {
            scheduleAll(context);
        }
    }

    public static void scheduleMessages(Context context) {
        WorkManagerHelper.cancelUniqueWork("scheduled_message");
        WorkManagerHelper.cancelUniqueWork("scheduled_mod_message");
        scheduleJob(context, MessageJobService.class, "scheduled_message");
    }

    public static void scheduleModMessages(Context context) {
        WorkManagerHelper.cancelUniqueWork("scheduled_message");
        WorkManagerHelper.cancelUniqueWork("scheduled_mod_message");
        scheduleJob(context, ModMessageJobService.class, "scheduled_mod_message");
    }

    public static void scheduleAll(Context context) {
        if (isLoggedIn(context)) {
            scheduleJob(context, MessageJobService.class, "scheduled_message");
        }
        if (shouldCheckModMail(context)) {
            scheduleJob(context, ModMessageJobService.class, "scheduled_mod_message");
        }
    }

    private static void scheduleJob(Context context, Class<? extends ListenableWorker> cls, String uniqueWorkName) {
        int intervalMinutes = getIntervalMinutes(context);
        if (intervalMinutes == -1) {
            WorkManagerHelper.enqueueUniqueWork(uniqueWorkName, ExistingWorkPolicy.REPLACE, new OneTimeWorkRequest.Builder(cls).setInitialDelay(30L, TimeUnit.SECONDS).build());
        } else {
            WorkManagerHelper.enqueueUniquePeriodicWork(uniqueWorkName, ExistingPeriodicWorkPolicy.REPLACE, new PeriodicWorkRequest.Builder(cls, intervalMinutes, TimeUnit.MINUTES).setInitialDelay(30L, TimeUnit.SECONDS).build());
        }
    }

    private static boolean shouldCheckMessages(Context context) {
        boolean enabled = Integer.parseInt(PreferencesHelper.getAccountPrefs(context, "MessagingService").getString("message_preference_new", "3")) > 0;
        Logger.verbose("SHOULD CHECK: " + enabled);
        return enabled;
    }

    private static boolean shouldCheckModMail(Context context) {
        return AccountSingleton.isLoggedIn(RedditApplication.getAppContext()) && ModeratedSubredditManager.getInstance().hasModerated() && PreferencesHelper.getAccountPrefs(context, "MessagingService").getBoolean("message_mod_mail", false);
    }

    private static boolean isLoggedIn(Context context) {
        return AccountSingleton.isLoggedIn(RedditApplication.getAppContext());
    }
}
