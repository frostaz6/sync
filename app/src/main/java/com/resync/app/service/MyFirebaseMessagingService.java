package com.resync.app.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.receiver.UltraReceiver;
import com.resync.app.service.MyFirebaseMessagingService;
import com.resync.app.ultra.jobs.UltraGrabTagsJob;
import com.resync.app.premium.UserPaintManager;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.NewUserTagManager;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.IconHelper;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.events.UltraRestoreFailedEvent;

/* JADX INFO: loaded from: classes.dex */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    class SubscribeNotificationTarget extends SimpleTarget<Drawable> {

        final /* synthetic */ String title;

        final /* synthetic */ String body;

        SubscribeNotificationTarget(String str, String str2) {
            this.title = str;
            this.body = str2;
        }

        @Override // com.bumptech.glide.request.target.Target
        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
            ((NotificationManager) MyFirebaseMessagingService.this.getSystemService("notification")).notify(1, new NotificationCompat.Builder(MyFirebaseMessagingService.this.getContext(), NotificationHelper.getMessagingChannel()).setSmallIcon(IconHelper.getSystemIconRes()).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(((BitmapDrawable) drawable).getBitmap())).setContentTitle(this.title).setContentText(this.body).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).setContentIntent(PendingIntent.getActivity(MyFirebaseMessagingService.this.getContext(), 0, UltraHelper.getSubscriptionsIntent(), 1140850688)).build());
        }
    }

    class CancelledNotificationTarget extends SimpleTarget<Drawable> {

        final /* synthetic */ String title;

        final /* synthetic */ String body;

        CancelledNotificationTarget(String str, String str2) {
            this.title = str;
            this.body = str2;
        }

        @Override // com.bumptech.glide.request.target.Target
        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
            ((NotificationManager) MyFirebaseMessagingService.this.getSystemService("notification")).notify(2, new NotificationCompat.Builder(MyFirebaseMessagingService.this.getContext(), NotificationHelper.getMessagingChannel()).setSmallIcon(IconHelper.getSystemIconRes()).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(((BitmapDrawable) drawable).getBitmap())).setContentTitle(this.title).setContentText(this.body).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).setContentIntent(PendingIntent.getActivity(MyFirebaseMessagingService.this.getContext(), 0, UltraHelper.getSubscriptionsIntent(), 1140850688)).build());
        }
    }

    class ExpiredNotificationTarget extends SimpleTarget<Drawable> {

        final /* synthetic */ String title;

        final /* synthetic */ String body;

        ExpiredNotificationTarget(String str, String str2) {
            this.title = str;
            this.body = str2;
        }

        @Override // com.bumptech.glide.request.target.Target
        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
            ((NotificationManager) MyFirebaseMessagingService.this.getSystemService("notification")).notify(2, new NotificationCompat.Builder(MyFirebaseMessagingService.this.getContext(), NotificationHelper.getMessagingChannel()).setSmallIcon(IconHelper.getSystemIconRes()).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(((BitmapDrawable) drawable).getBitmap())).setContentTitle(this.title).setContentText(this.body).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).setContentIntent(PendingIntent.getActivity(MyFirebaseMessagingService.this.getContext(), 0, UltraHelper.getSubscriptionsIntent(), 1140850688)).build());
        }
    }

    private void logShortLivedTaskDone() {
        Logger.verbose("MyFirebaseMsgService", "Short lived task is done.");
    }

    private void showSubscribeNotification(String str, String str2, String str3, String str4) {
        UltraHelper.savePurchase(str3, str4);
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load("https://i.imgur.com/cl7SeBj.jpg").into(new SubscribeNotificationTarget(str, str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void postRestoreFailed() {
        BusSingleton.getBus().post(new UltraRestoreFailedEvent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void addPaintFromNotification(String str, String str2, String str3) {
        UserPaintManager.getInstance().addPaintFromNotification(str.split("#")[1], str2, str3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void removePaintFromNotification(String str, String str2) {
        UserPaintManager.getInstance().removePaintFromNotification(str.split("#")[1], str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void clearPaints(String str) {
        UserPaintManager.getInstance().clearPaints(str.split("#")[1]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void addTagFromNotification(String str, String str2, String str3) {
        NewUserTagManager.getInstance().addTagFromNotification(str.split("#")[1], str2, str3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void removeTagFromNotification(String str, String str2) {
        NewUserTagManager.getInstance().removeTagFromNotification(str.split("#")[1], str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void clearTags(String str) {
        NewUserTagManager.getInstance().clearTags(str.split("#")[1]);
    }

    private void showExpiredNotification(String str, String str2) {
        UltraHelper.clearPurchaseToken();
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load("https://i.imgur.com/Njynm4P.jpg").into(new ExpiredNotificationTarget(str, str2));
    }

    private void showSimpleNotification(String str, String str2) {
        ((NotificationManager) getSystemService("notification")).notify(0, new NotificationCompat.Builder(this, NotificationHelper.getMessagingChannel()).setSmallIcon(IconHelper.getSystemIconRes()).setContentTitle(str).setContentText(str2).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).build());
    }

    private void handleNewToken(String str) {
    }

    private void showSettingsChangeNotification(String str, String str2, String str3, boolean z) {
        int iHashCode = str3.hashCode();
        Intent intent = new Intent(getContext(), (Class<?>) SettingChangeReceiver.class);
        intent.putExtra("id", iHashCode);
        intent.putExtra("key", str3);
        intent.putExtra("value", z);
        ((NotificationManager) getSystemService("notification")).notify(iHashCode, new NotificationCompat.Builder(this, NotificationHelper.getMessagingChannel()).setSmallIcon(IconHelper.getSystemIconRes()).setContentTitle(str).setContentText(str2).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).addAction(R.drawable.outline_check_24, z ? "Enable" : "Disable", PendingIntent.getBroadcast(getContext(), 0, intent, 335544320)).build());
    }

    private void showCancelledNotification(String str, String str2) {
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load("https://i.imgur.com/Njynm4P.jpg").into(new CancelledNotificationTarget(str, str2));
    }

    public Context getContext() {
        return this;
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.verbose("MyFirebaseMsgService", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Logger.verbose("MyFirebaseMsgService", "Message data payload: " + remoteMessage.getData());
            logShortLivedTaskDone();
            final String str = remoteMessage.getData().get("title");
            final String str2 = remoteMessage.getData().get("body");
            final String str3 = remoteMessage.getData().get("type");
            String str4 = remoteMessage.getData().get("purchase_token");
            String str5 = remoteMessage.getData().get("sku");
            Logger.verbose("MyFirebaseMsgService", "Type: " + str3);
            if (StringUtils.equalsAnyIgnoreCase("settings-change", str3)) {
                showSettingsChangeNotification(str, str2, remoteMessage.getData().get("key"), Boolean.parseBoolean(remoteMessage.getData().get("value")));
            } else if (StringUtils.equalsAnyIgnoreCase("subscribe", str3)) {
                showSubscribeNotification(str, str2, str5, str4);
            } else if (StringUtils.equalsAnyIgnoreCase("cancelled", str3)) {
                UltraReceiver.broadcast();
                showCancelledNotification(str, str2);
            } else if (StringUtils.equalsAnyIgnoreCase("expired", str3) || StringUtils.equalsAnyIgnoreCase("lifetime-cancelled", str3)) {
                showExpiredNotification(str, str2);
            } else if (StringUtils.equalsAnyIgnoreCase("restore", str3)) {
                showSubscribeNotification(str, str2, str5, str4);
            } else if (StringUtils.equalsAnyIgnoreCase("restore-failed", str3)) {
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: i9.g
                    @Override // java.lang.Runnable
                    public final void run() {
                        MyFirebaseMessagingService.postRestoreFailed();
                    }
                });
            } else if (!StringUtils.equalsAnyIgnoreCase("renwed", str3)) {
                if (StringUtils.startsWith(str3, "paint-add#")) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: i9.e
                        @Override // java.lang.Runnable
                        public final void run() {
                            MyFirebaseMessagingService.addPaintFromNotification(str3, str, str2);
                        }
                    });
                } else if (StringUtils.startsWith(str3, "paint-remove#")) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: i9.d
                        @Override // java.lang.Runnable
                        public final void run() {
                            MyFirebaseMessagingService.removePaintFromNotification(str3, str);
                        }
                    });
                } else if (StringUtils.startsWith(str3, "paint-clear#")) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: i9.a
                        @Override // java.lang.Runnable
                        public final void run() {
                            MyFirebaseMessagingService.clearPaints(str3);
                        }
                    });
                } else if (StringUtils.startsWith(str3, "tag-add#")) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: i9.f
                        @Override // java.lang.Runnable
                        public final void run() {
                            MyFirebaseMessagingService.addTagFromNotification(str3, str, str2);
                        }
                    });
                } else if (StringUtils.startsWith(str3, "tag-remove#")) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: i9.c
                        @Override // java.lang.Runnable
                        public final void run() {
                            MyFirebaseMessagingService.removeTagFromNotification(str3, str);
                        }
                    });
                } else if (StringUtils.startsWith(str3, "tag-clear#")) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: i9.b
                        @Override // java.lang.Runnable
                        public final void run() {
                            MyFirebaseMessagingService.clearTags(str3);
                        }
                    });
                } else if (StringUtils.startsWith(str3, "tag-refresh-all")) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: i9.h
                        @Override // java.lang.Runnable
                        public final void run() {
                            UltraGrabTagsJob.schedule();
                        }
                    });
                } else {
                    showSimpleNotification(str, str2);
                }
            }
        }
        if (remoteMessage.getNotification() != null) {
            Logger.verbose("MyFirebaseMsgService", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onNewToken(String str) {
        Logger.verbose("MyFirebaseMsgService", "Refreshed token: " + str);
        handleNewToken(str);
    }
}
