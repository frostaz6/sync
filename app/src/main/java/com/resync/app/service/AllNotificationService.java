package com.resync.app.service;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.util.Logger;
import com.resync.app.helpers.messaging.MessageSchedulingHelper;

/* JADX INFO: loaded from: classes.dex */
public class AllNotificationService extends NotificationListenerService {

    static String TAG = "AllNotificationService";

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        Logger.verbose(TAG, "Message: " + statusBarNotification.getPackageName());
        if ("com.reddit.frontpage".equalsIgnoreCase(statusBarNotification.getPackageName()) && SettingsSingleton.getSettings().pushNotifications) {
            Logger.verbose("GOT NOTIFICATION");
            MessageSchedulingHelper.scheduleIfEnabled(this);
            cancelNotification(statusBarNotification.getKey());
        }
    }
}
