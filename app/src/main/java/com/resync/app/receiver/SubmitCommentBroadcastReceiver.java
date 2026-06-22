package com.resync.app.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Post;
import com.resync.app.net.oauth.OAuthCommentRequest;

/* JADX INFO: loaded from: classes.dex */
public class SubmitCommentBroadcastReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            return;
        }
        int requestId = intent.getExtras().getInt("request_id");
        if (requestId != 0) {
            ((NotificationManager) context.getSystemService("notification")).cancel(requestId);
        }
        String string = intent.getExtras().containsKey("cursorId") ? intent.getExtras().getString("cursorId") : null;
        Post post = intent.getExtras().containsKey("parentPost") ? (Post) intent.getExtras().getSerializable("parentPost") : null;
        Post post2 = intent.getExtras().containsKey("commentPost") ? (Post) intent.getExtras().getSerializable("commentPost") : null;
        String string2 = intent.getExtras().getString("inputText");
        String string3 = intent.getExtras().getString("refresh_token");
        OAuthRequestManager.sendWithTempTokenExchange(context, new OAuthCommentRequest(context.getApplicationContext(), string, post, post2, string2, intent.getExtras().getInt("type", -1), intent.getExtras().getBoolean("retry"), string3), string3);
    }
}
