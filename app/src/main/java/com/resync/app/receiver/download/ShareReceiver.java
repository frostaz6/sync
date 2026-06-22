package com.resync.app.receiver.download;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import com.resync.app.RedditApplication;
import com.resync.app.jobs.media.AbstractDownloadMediaJob;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class ShareReceiver extends BroadcastReceiver {
    private static String getAction() {
        return "com.resync.app.share";
    }

    public static String resolveFilePath(Context context, Uri uri) {
        String string = null;
        try {
            String[] strArr = {"_data"};
            Cursor cursorQuery = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_id=?", new String[]{DocumentsContract.getDocumentId(uri).split(":")[1]}, null);
            if (cursorQuery != null) {
                try {
                    int columnIndex = cursorQuery.getColumnIndex(strArr[0]);
                    if (cursorQuery.moveToFirst()) {
                        string = cursorQuery.getString(columnIndex);
                    }
                } finally {
                }
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Exception unused) {
        }
        return string;
    }

    public static ShareReceiver register(Activity activity) {
        ShareReceiver shareReceiver = new ShareReceiver();
        ContextCompat.registerReceiver(activity, shareReceiver, new IntentFilter(getAction()), ContextCompat.RECEIVER_NOT_EXPORTED);
        return shareReceiver;
    }

    public static void share(final AbstractDownloadMediaJob.ShareInfo shareInfo) {
        // Perform the share/clipboard directly on the main thread from the app context.
        // The old code broadcast an implicit RECEIVER_NOT_EXPORTED intent from the WorkManager
        // background thread and relied on an Activity-registered receiver catching it — but the
        // broadcast was not reliably delivered, so "Direct" share silently did nothing (verified
        // on-device: download + copy succeed, onReceive never fires). Doing the work here is
        // bulletproof: the app is in the foreground when sharing, so startActivity(NEW_TASK) is
        // allowed from the app context.
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                performShare(RedditApplication.getAppContext(), shareInfo);
            }
        });
    }

    public static void unregister(Activity activity, ShareReceiver shareReceiver) {
        if (shareReceiver != null) {
            activity.unregisterReceiver(shareReceiver);
        }
    }

    public static void performShare(Context context, AbstractDownloadMediaJob.ShareInfo shareInfo) {
        try {
            if (shareInfo == null) {
                return;
            }
            if (shareInfo.clipboard) {
                ClipboardManager clipboardManager = (ClipboardManager) RedditApplication.getAppContext().getSystemService("clipboard");
                ContentValues contentValues = new ContentValues(2);
                contentValues.put("mime_type", shareInfo.mimeType);
                contentValues.put("_data", shareInfo.uri().toString());
                clipboardManager.setPrimaryClip(ClipData.newUri(RedditApplication.getAppContext().getContentResolver(), "Image", shareInfo.uri()));
                StaticToast.show("Copied to clipboard");
                return;
            }
            Uri shareUri = shareInfo.uri();
            Intent send = new Intent(Intent.ACTION_SEND)
                    .setDataAndType(shareUri, shareInfo.mimeType)
                    .putExtra(Intent.EXTRA_STREAM, shareUri)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            send.setClipData(ClipData.newUri(context.getContentResolver(), "Image", shareUri));
            if (!StringUtils.isEmpty(shareInfo.title) && SettingsSingleton.getSettings().imageShareTitle) {
                send.putExtra(Intent.EXTRA_SUBJECT, shareInfo.title);
                send.putExtra(Intent.EXTRA_TEXT, shareInfo.title);
            }
            Intent chooser = Intent.createChooser(send, "Share");
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooser);
        } catch (Exception e) {
            Logger.logException(e);
            StaticToast.show("Error sharing file");
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        performShare(context, (AbstractDownloadMediaJob.ShareInfo) intent.getSerializableExtra("info"));
    }
}
