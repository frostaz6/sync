package com.resync.app.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Message;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthMarkMessageRequest;

/* JADX INFO: loaded from: classes.dex */
public class MarkMessageAsReadReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("notification_id", -1);
        Message message = (Message) intent.getSerializableExtra("message");
        OAuthRequestManager.send(context, new OAuthMarkMessageRequest(context, message.kind, message.id));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        notificationManager.cancel(intExtra);
        StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
        int index = 0;
        if (activeNotifications != null && activeNotifications.length > 0) {
            int messageGroupCount = 0;
            while (index < activeNotifications.length) {
                Logger.verbose("Notification remaining: " + activeNotifications[index].getNotification());
                if (StringUtils.equals(activeNotifications[index].getNotification().getGroup(), "group_message")) {
                    messageGroupCount++;
                }
                index++;
            }
            index = messageGroupCount;
        }
        if (index == 1) {
            notificationManager.cancel(1);
        }
    }
}
