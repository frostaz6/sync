package com.resync.app.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import com.resync.app.RedditApplication;

/* JADX INFO: renamed from: t7.t */
/* JADX INFO: compiled from: NotificationHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class NotificationHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static String CHANNEL_PASSIVE = "sync_passive";
    private static String CHANNEL_GENERIC = "sync_generic";
    private static String CHANNEL_ERROR = "sync_generic_error";
    private static String CHANNEL_MESSAGING = "sync_messaging";

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static String CHANNEL_ULTRA = "sync_ultra";

    public static String getErrorChannel() {
        return CHANNEL_ERROR;
    }

    public static String getGenericChannel() {
        return CHANNEL_GENERIC;
    }

    public static String getMessagingChannel() {
        return CHANNEL_MESSAGING;
    }

    public static String getPassiveChannel() {
        return CHANNEL_PASSIVE;
    }

    public static String getUltraChannel() {
        return CHANNEL_ULTRA;
    }

    public static void createErrorChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) RedditApplication.getAppContext().getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ERROR, "Sync for reddit generic notifications", 4);
            notificationChannel.setDescription("Generic error notifications (e.g. a download failed)");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static void createGenericChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) RedditApplication.getAppContext().getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_GENERIC, "Sync for reddit generic notifications", 2);
            notificationChannel.setDescription("Generic notifications (e.g. downloading images etc)");
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static void createMessagingChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) RedditApplication.getAppContext().getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_MESSAGING, "Sync for reddit message notifications", 4);
            notificationChannel.setDescription("Message notifications");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static void createPassiveChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) RedditApplication.getAppContext().getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_PASSIVE, "Sync for reddit app alerts", 4);
            notificationChannel.setDescription("App alert notifications");
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static void createUltraChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) RedditApplication.getAppContext().getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ULTRA, "Sync Ultra subscription notifications", 4);
            notificationChannel.setDescription("Ultra subscription notifications");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
