package com.resync.app.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Pair;
import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.PerSubSortHelper;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.PostsInfo;
import com.resync.app.managers.PostsManager;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.helpers.IconHelper;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.net.oauth.OAuthCommentsRequest;
import com.resync.app.net.oauth.OAuthPostsRequest;

/* JADX INFO: loaded from: classes.dex */
public class SyncService extends IntentService {

    String subreddit;
    boolean syncComments;
    int pages;

    int notificationId;

    Intent wakefulIntent;

    class PostsResponseListener implements Response.Listener<PostsInfo> {

        final /* synthetic */ Context context;
        final /* synthetic */ int page;
        final /* synthetic */ int pageCount;

        final /* synthetic */ String sortSecond;

        final /* synthetic */ String sortFirst;

        final /* synthetic */ String subreddit;
        final /* synthetic */ boolean syncComments;

        class CommentSyncLoadListener implements Loader.OnLoadCompleteListener<Cursor> {
            CommentSyncLoadListener() {
            }

            @Override // android.content.Loader.OnLoadCompleteListener
            public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
                if (cursor != null) {
                    int count = cursor.getCount();
                    Pair[] pairArr = new Pair[count];
                    for (int i = 0; i < count; i++) {
                        cursor.moveToPosition(i);
                        pairArr[i] = new Pair(cursor.getString(cursor.getColumnIndex("subreddit")), cursor.getString(cursor.getColumnIndex("_id")));
                    }
                    PostsResponseListener listener = PostsResponseListener.this;
                    SyncService.this.syncComments(listener.context, pairArr, 0);
                }
            }
        }

        PostsResponseListener(Context context, int page, int pageCount, String str, String str2, String str3, boolean syncComments) {
            this.context = context;
            this.page = page;
            this.pageCount = pageCount;
            this.sortSecond = str;
            this.sortFirst = str2;
            this.subreddit = str3;
            this.syncComments = syncComments;
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(PostsInfo postsInfo) {
            if (ContextHelper.isActivityFinishing(this.context)) {
                return;
            }
            int page = this.page;
            int nextPage = page + 1;
            int pageCount = this.pageCount;
            if (nextPage < pageCount) {
                SyncService.this.syncPosts(this.context, this.sortSecond, this.sortFirst, pageCount, this.subreddit, postsInfo, page + 1, this.syncComments);
                return;
            }
            this.context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
            if (!this.syncComments) {
                SyncService.this.showSyncedNotification();
                return;
            }
            CursorLoader cursorLoaderC = PostsManager.createPostsLoader(this.context, this.subreddit, 6);
            cursorLoaderC.registerListener(0, new CommentSyncLoadListener());
            cursorLoaderC.startLoading();
        }
    }

    class PostsErrorListener implements Response.ErrorListener {
        PostsErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            SyncService.this.showErrorNotification();
        }
    }

    class CommentResponseListener implements Response.Listener<Void> {

        final /* synthetic */ Context context;
        final /* synthetic */ int index;
        final /* synthetic */ Pair[] pairs;

        CommentResponseListener(Context context, int index, Pair[] pairArr) {
            this.context = context;
            this.index = index;
            this.pairs = pairArr;
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(Void response) {
            if (ContextHelper.isActivityFinishing(this.context)) {
                return;
            }
            int index = this.index;
            int nextIndex = index + 1;
            Pair[] pairArr = this.pairs;
            if (nextIndex < pairArr.length) {
                SyncService.this.syncComments(this.context, pairArr, index + 1);
            } else {
                SyncService.this.showSyncedNotification();
            }
        }
    }

    class CommentErrorListener implements Response.ErrorListener {
        CommentErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            SyncService.this.showErrorNotification();
        }
    }

    public SyncService() {
        super("SyncService");
        setIntentRedelivery(true);
    }

    private void completeWakeful() {
        try {
            WakefulBroadcastReceiver.completeWakefulIntent(this.wakefulIntent);
        } catch (Exception e) {
            CrashlyticsHelper.logException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncComments(Context context, Pair<String, String>[] pairArr, int index) {
        showProgressNotification("Syncing comments", pairArr.length, index);
        OAuthRequestManager.send(context, new OAuthCommentsRequest(context, (String) pairArr[index].second, (String) pairArr[index].first, null, SettingsSingleton.getSettings().commentSort, true, false, false, new CommentResponseListener(context, index, pairArr), new CommentErrorListener()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncPosts(Context context, String str, String str2, int pageCount, String str3, PostsInfo postsInfo, int page, boolean syncComments) {
        showProgressNotification("Syncing " + str3, pageCount, page);
        Pair<String, String> pairB = PerSubSortHelper.getSortPair(str3);
        String str4 = (String) pairB.first;
        String str5 = (String) pairB.second;
        OAuthRequestManager.send(context, new OAuthPostsRequest(context, Long.valueOf(System.currentTimeMillis()), str3, str5, str4, postsInfo, false, postsInfo == null, new PostsResponseListener(context, page, pageCount, str5, str4, str3, syncComments), new PostsErrorListener()));
    }

    void showSyncedNotification() {
        NotificationManager notificationManager = (NotificationManager) RedditApplication.getAppContext().getSystemService("notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(RedditApplication.getAppContext());
        builder.setContentTitle("Content synced").setTicker("Content synced").setSmallIcon(R.drawable.ic_file_download_white_24dp).setChannelId(NotificationHelper.getGenericChannel());
        notificationManager.notify(this.notificationId, builder.build());
        completeWakeful();
    }

    void showErrorNotification() {
        NotificationManager notificationManager = (NotificationManager) RedditApplication.getAppContext().getSystemService("notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(RedditApplication.getAppContext());
        builder.setContentTitle("Error syncing content").setTicker("Error syncing content").setSmallIcon(R.drawable.ic_close_white_24dp).setChannelId(NotificationHelper.getGenericChannel());
        notificationManager.notify(this.notificationId, builder.build());
        completeWakeful();
    }

    void showProgressNotification(String str, int max, int progress) {
        NotificationManager notificationManager = (NotificationManager) RedditApplication.getAppContext().getSystemService("notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(RedditApplication.getAppContext());
        builder.setContentTitle(str).setTicker(str).setSmallIcon(IconHelper.getSystemIconRes()).setChannelId(NotificationHelper.getGenericChannel());
        builder.setProgress(max, progress, false);
        notificationManager.notify(this.notificationId, builder.build());
    }

    @Override // android.app.IntentService, android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        this.wakefulIntent = intent;
        this.notificationId = (int) System.currentTimeMillis();
        this.subreddit = intent.getStringExtra("com.resync.app.subreddit");
        this.syncComments = intent.getBooleanExtra("com.resync.app.comments", false);
        this.pages = intent.getIntExtra("com.resync.app.pages", 5);
        syncPosts(this, (String) SettingsSingleton.getSettings().postSort.first, (String) SettingsSingleton.getSettings().postSort.second, this.pages, this.subreddit, new PostsInfo(), 0, this.syncComments);
    }
}
