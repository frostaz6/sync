package com.resync.app.helpers.messaging;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import com.bumptech.glide.RedditHelper;
import com.resync.app.receiver.MarkMessageAsReadReceiver;
import com.resync.app.receiver.MessageReplyReceiver;
import com.resync.app.ui.activities.MainActivity;
import org.apache.commons.lang3.ArrayUtils;
import com.resync.app.model.Message;
import com.resync.app.helpers.IconHelper;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: z7.c */
/* JADX INFO: compiled from: MessagingHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class MessagingHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static int NOTIFICATION_COLOR = -42494;

    private static PendingIntent buildContentIntent(Context context, int requestCode) {
        Intent[] intentArr = {new Intent(context, (Class<?>) MainActivity.class)};
        intentArr[0].putExtra("messaging", 1);
        intentArr[0].setFlags(67108864);
        intentArr[0].addFlags(536870912);
        return PendingIntent.getActivities(context, requestCode, intentArr, 167772160);
    }

    public static Notification buildSummaryNotification(Context context, Message[] messageArr, int count) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(count);
        sb2.append(" new message");
        sb2.append(count != 1 ? "s" : "");
        String string = sb2.toString();
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        for (Message message : messageArr) {
            inboxStyle.addLine(Html.fromHtml("<b>" + message.author + "</b>: " + message.bodyProcessed));
        }
        inboxStyle.setSummaryText(string);
        inboxStyle.setBigContentTitle("Sync for reddit");
        Notification notificationC = new NotificationCompat.Builder(context).setSmallIcon(IconHelper.getSystemIconRes()).setStyle(inboxStyle).setGroup("group_message").setGroupSummary(true).setNumber(count).setContentIntent(buildContentIntent(context, 1)).setContentTitle("Sync for reddit").setContentInfo(string).setColor(NOTIFICATION_COLOR).setChannelId(NotificationHelper.getMessagingChannel()).build();
        if (PreferencesHelper.getAccountPrefs(context, "MessagingService").getBoolean("message_sound", true) && Build.VERSION.SDK_INT < 26) {
            notificationC.defaults |= 1;
        }
        if (PreferencesHelper.getAccountPrefs(context, "MessagingService").getBoolean("message_led", true)) {
            notificationC.ledARGB = -39424;
            notificationC.ledOnMS = 250;
            notificationC.ledOffMS = 1000;
            notificationC.flags |= 1;
        }
        notificationC.flags |= 24;
        return notificationC;
    }

    private static Notification buildMessageNotification(Context context, Message message) {
        int notificationId = (int) message.notificationId;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setChannelId(NotificationHelper.getMessagingChannel());
        builder.setSmallIcon(IconHelper.getSystemIconRes());
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(buildContentIntent(context, notificationId));
        String strTrim = Html.fromHtml(RedditHelper.processMarkdownToHtml(null, message.bodyProcessed)).toString().trim();
        builder.setContentText(strTrim);
        builder.setTicker(strTrim);
        builder.setContentTitle(message.author);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(strTrim).setBigContentTitle(message.author).setSummaryText(message.kind == 4 ? "New message" : "Comment reply"));
        builder.setGroup("group_message");
        builder.setPriority(1);
        builder.setColor(NOTIFICATION_COLOR);
        Intent intent = new Intent(context, (Class<?>) MarkMessageAsReadReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("notification_id", notificationId);
        Logger.verbose("MESSAGE ID: " + notificationId);
        builder.addAction(0, "Mark as read", PendingIntent.getBroadcast(context, notificationId + 1, intent, 301989888));
        if (Build.VERSION.SDK_INT >= 24) {
            RemoteInput remoteInputA = new RemoteInput.Builder("reply_key").setLabel("Reply").build();
            Intent intent2 = new Intent(context, (Class<?>) MessageReplyReceiver.class);
            intent2.setAction("reply");
            intent2.putExtra("message", message);
            intent2.putExtra("notification_id", notificationId);
            builder.addAction(new NotificationCompat.Action.Builder(0, "Reply", PendingIntent.getBroadcast(context, notificationId + 2, intent2, 301989888)).addRemoteInput(remoteInputA).setAllowGeneratedReplies(true).build());
        }
        Notification notificationC = builder.build();
        notificationC.flags |= 24;
        return notificationC;
    }

    public static void notifyMessages(Context context, Message[] messageArr) {
        if (ArrayUtils.getLength(messageArr) == 0) {
            return;
        }
        int length = ArrayUtils.getLength(messageArr);
        if (Build.VERSION.SDK_INT < 24) {
            NotificationManagerCompat notificationManagerCompatD = NotificationManagerCompat.from(context);
            Notification notificationB = buildSummaryNotification(context, messageArr, length);
            notificationManagerCompatD.cancel(1);
            notificationManagerCompatD.notify(1, notificationB);
            return;
        }
        NotificationManagerCompat notificationManagerCompatD2 = NotificationManagerCompat.from(context);
        Notification notificationB2 = buildSummaryNotification(context, messageArr, length);
        notificationManagerCompatD2.cancel(1);
        notificationManagerCompatD2.notify(1, notificationB2);
        for (Message message : messageArr) {
            notificationManagerCompatD2.notify((int) message.notificationId, buildMessageNotification(context, message));
        }
    }
}
