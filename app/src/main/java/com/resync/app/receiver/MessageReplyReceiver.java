package com.resync.app.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.app.RemoteInput;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Message;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.net.oauth.OAuthCommentRequest;

/* JADX INFO: loaded from: classes.dex */
public class MessageReplyReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("reply".equals(intent.getAction())) {
            int intExtra = intent.getIntExtra("notification_id", -1);
            Message message = (Message) intent.getSerializableExtra("message");
            Bundle bundleJ = RemoteInput.getResultsFromIntent(intent);
            if (bundleJ != null) {
                String string = bundleJ.getString("reply_key");
                if (message != null && !TextUtils.isEmpty(string)) {
                    int kind = message.kind;
                    if (kind == 1) {
                        OAuthRequestManager.send(context, new OAuthCommentRequest(context, null, null, Post.fromMessage(message, 1), string, 1, false, null));
                    } else if (kind == 4) {
                        Logger.verbose("WAS A MESSAGE REPLY");
                        OAuthRequestManager.send(context, new OAuthCommentRequest(context, null, null, Post.fromMessage(message, 4), string, 2, false, null));
                    }
                }
            }
            ((NotificationManager) context.getSystemService("notification")).cancel(intExtra);
        }
    }
}
