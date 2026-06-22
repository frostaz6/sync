package com.resync.app.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.ProgressDialogHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.PostsInfo;
import com.resync.app.managers.PostsManager;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.net.oauth.OAuthCommentsRequest;
import com.resync.app.net.oauth.OAuthPostsRequest;

/* JADX INFO: renamed from: t7.k0 */
/* JADX INFO: compiled from: SyncManager.java */
/* JADX INFO: loaded from: classes.dex */
public class SyncManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ProgressDialog progressDialog;

    /* JADX INFO: renamed from: t7.k0$a */
    /* JADX INFO: compiled from: SyncManager.java */
    static class PostsAndCommentsClickListener implements DialogInterface.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ EditText editText;
        final /* synthetic */ Context context;
        final /* synthetic */ String username;

        final /* synthetic */ String subreddit;

        final /* synthetic */ String sort;

        PostsAndCommentsClickListener(EditText editText, Context context, String username, String subreddit, String sort) {
            this.editText = editText;
            this.context = context;
            this.username = username;
            this.subreddit = subreddit;
            this.sort = sort;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            String string = this.editText.getText().toString();
            if (TextUtils.isEmpty(string)) {
                string = "5";
            }
            new SyncManager().syncPages(this.context, this.username, this.subreddit, Math.min(20, Integer.parseInt(string)), this.sort, true);
        }
    }

    /* JADX INFO: renamed from: t7.k0$b */
    /* JADX INFO: compiled from: SyncManager.java */
    static class PostsClickListener implements DialogInterface.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ EditText editText;
        final /* synthetic */ Context context;
        final /* synthetic */ String username;

        final /* synthetic */ String subreddit;

        final /* synthetic */ String sort;

        PostsClickListener(EditText editText, Context context, String username, String subreddit, String sort) {
            this.editText = editText;
            this.context = context;
            this.username = username;
            this.subreddit = subreddit;
            this.sort = sort;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            String string = this.editText.getText().toString();
            if (TextUtils.isEmpty(string)) {
                string = "5";
            }
            new SyncManager().syncPages(this.context, this.username, this.subreddit, Math.min(20, Integer.parseInt(string)), this.sort, false);
        }
    }

    /* JADX INFO: renamed from: t7.k0$c */
    /* JADX INFO: compiled from: SyncManager.java */
    class PostsResponseListener implements Response.Listener<PostsInfo> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context context;
        final /* synthetic */ int pageIndex;
        final /* synthetic */ int totalPages;

        final /* synthetic */ String after;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        final /* synthetic */ String sort;

        final /* synthetic */ String subreddit;
        final /* synthetic */ boolean syncComments;

        /* JADX INFO: renamed from: t7.k0$c$a */
        /* JADX INFO: compiled from: SyncManager.java */
        class CommentsLoadListener implements Loader.OnLoadCompleteListener<Cursor> {
            CommentsLoadListener() {
            }

            @Override // android.content.Loader.OnLoadCompleteListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
                try {
                    if (SyncManager.this.progressDialog != null && SyncManager.this.progressDialog.isShowing()) {
                        SyncManager.this.progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    Logger.logException(e);
                    CrashlyticsHelper.logException(e);
                }
                if (cursor != null) {
                    int count = cursor.getCount();
                    Pair[] pairArr = new Pair[count];
                    for (int i = 0; i < count; i++) {
                        cursor.moveToPosition(i);
                        pairArr[i] = Pair.create(cursor.getString(cursor.getColumnIndex("subreddit")), cursor.getString(cursor.getColumnIndex("_id")));
                    }
                    try {
                        if (SyncManager.this.progressDialog != null && SyncManager.this.progressDialog.isShowing()) {
                            SyncManager.this.progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        Logger.logException(e);
                        CrashlyticsHelper.logException(e);
                    }
                    PostsResponseListener listener = PostsResponseListener.this;
                    SyncManager.this.progressDialog = ProgressDialogHelper.create(listener.context);
                    SyncManager.this.progressDialog.setProgressStyle(1);
                    SyncManager.this.progressDialog.setTitle("Syncing comments...");
                    SyncManager.this.progressDialog.setMax(count);
                    SyncManager.this.progressDialog.setIndeterminate(false);
                    SyncManager.this.progressDialog.setCancelable(true);
                    SyncManager.this.progressDialog.show();
                    PostsResponseListener outerListener = PostsResponseListener.this;
                    SyncManager.this.syncCommentsStep(outerListener.context, pairArr, 0);
                }
            }
        }

        PostsResponseListener(Context context, int pageIndex, int totalPages, String after, String sort, String subreddit, boolean syncComments) {
            this.context = context;
            this.pageIndex = pageIndex;
            this.totalPages = totalPages;
            this.after = after;
            this.sort = sort;
            this.subreddit = subreddit;
            this.syncComments = syncComments;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(PostsInfo postsInfo) {
            if (ContextHelper.isActivityFinishing(this.context)) {
                return;
            }
            int pageIndex = this.pageIndex;
            int nextPage = pageIndex + 1;
            int totalPages = this.totalPages;
            if (nextPage < totalPages) {
                SyncManager.this.syncPostsStep(this.context, this.after, this.sort, totalPages, this.subreddit, postsInfo, pageIndex + 1, this.syncComments);
                return;
            }
            this.context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
            if (this.syncComments) {
                CursorLoader cursorLoaderC = PostsManager.createPostsLoader(this.context, this.subreddit, 6);
                cursorLoaderC.registerListener(0, new CommentsLoadListener());
                cursorLoaderC.startLoading();
                return;
            }
            try {
                if (SyncManager.this.progressDialog == null || !SyncManager.this.progressDialog.isShowing()) {
                    return;
                }
                SyncManager.this.progressDialog.dismiss();
                StaticToast.showWithContext(this.context, "Posts synced!");
            } catch (Exception e) {
                Logger.logException(e);
                CrashlyticsHelper.logException(e);
            }
        }
    }

    /* JADX INFO: renamed from: t7.k0$d */
    /* JADX INFO: compiled from: SyncManager.java */
    class PostsErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context context;

        PostsErrorListener(Context context) {
            this.context = context;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ContextHelper.isActivityFinishing(this.context)) {
                return;
            }
            try {
                if (SyncManager.this.progressDialog != null && SyncManager.this.progressDialog.isShowing()) {
                    SyncManager.this.progressDialog.dismiss();
                }
            } catch (Exception e) {
                Logger.logException(e);
                CrashlyticsHelper.logException(e);
            }
            StaticToast.showWithContext("Error syncing posts", this.context);
        }
    }

    /* JADX INFO: renamed from: t7.k0$e */
    /* JADX INFO: compiled from: SyncManager.java */
    class CommentsResponseListener implements Response.Listener<Void> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context context;
        final /* synthetic */ int index;
        final /* synthetic */ Pair[] subreddits;

        CommentsResponseListener(Context context, int index, Pair[] subreddits) {
            this.context = context;
            this.index = index;
            this.subreddits = subreddits;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Void response) {
            if (ContextHelper.isActivityFinishing(this.context)) {
                return;
            }
            int index = this.index;
            int nextIndex = index + 1;
            Pair[] subreddits = this.subreddits;
            if (nextIndex < subreddits.length) {
                SyncManager.this.syncCommentsStep(this.context, subreddits, index + 1);
                return;
            }
            try {
                if (SyncManager.this.progressDialog == null || !SyncManager.this.progressDialog.isShowing()) {
                    return;
                }
                SyncManager.this.progressDialog.dismiss();
                StaticToast.showWithContext(this.context, "Posts & comments synced!");
            } catch (Exception e) {
                Logger.logException(e);
                CrashlyticsHelper.logException(e);
            }
        }
    }

    /* JADX INFO: renamed from: t7.k0$f */
    /* JADX INFO: compiled from: SyncManager.java */
    class CommentsErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context context;

        CommentsErrorListener(Context context) {
            this.context = context;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ContextHelper.isActivityFinishing(this.context)) {
                return;
            }
            try {
                if (SyncManager.this.progressDialog != null && SyncManager.this.progressDialog.isShowing()) {
                    SyncManager.this.progressDialog.dismiss();
                }
            } catch (Exception e) {
                Logger.logException(e);
                CrashlyticsHelper.logException(e);
            }
            StaticToast.showWithContext("Error syncing comments", this.context);
        }
    }

    public static void showSyncDialog(Context context, String username, String subreddit, String sort) {
        View viewInflate = View.inflate(context, R.layout.dialog_sync, null);
        EditText editText = (EditText) viewInflate.findViewById(R.id.pages);
        editText.clearFocus();
        AlertDialogHelper.newBuilder(context).setTitle("Sync manager").setView(viewInflate).setPositiveButton("Posts", new PostsClickListener(editText, context, username, subreddit, sort)).setNegativeButton("Cancel", null).setNeutralButton("Posts & comments", new PostsAndCommentsClickListener(editText, context, username, subreddit, sort)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncCommentsStep(Context context, Pair<String, String>[] subreddits, int index) {
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.progressDialog.setProgress(index);
        OAuthRequestManager.send(context, new OAuthCommentsRequest(context, (String) subreddits[index].second, (String) subreddits[index].first, null, SettingsSingleton.getSettings().commentSort, true, false, false, new CommentsResponseListener(context, index, subreddits), new CommentsErrorListener(context)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncPostsStep(Context context, String after, String sort, int totalPages, String subreddit, PostsInfo postsInfo, int pageIndex, boolean syncComments) {
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.progressDialog.setProgress(pageIndex);
        OAuthRequestManager.send(context, new OAuthPostsRequest(context, Long.valueOf(System.currentTimeMillis()), subreddit, after, sort, postsInfo, pageIndex == 0, false, new PostsResponseListener(context, pageIndex, totalPages, after, sort, subreddit, syncComments), new PostsErrorListener(context)));
    }

    public void syncPages(Context context, String username, String subreddit, int pages, String sort, boolean syncComments) {
        ProgressDialog progressDialogA = ProgressDialogHelper.create(context);
        this.progressDialog = progressDialogA;
        progressDialogA.setProgressStyle(1);
        this.progressDialog.setTitle("Syncing pages...");
        this.progressDialog.setMax(pages);
        this.progressDialog.setIndeterminate(false);
        this.progressDialog.setCancelable(true);
        this.progressDialog.show();
        syncPostsStep(context, username, subreddit, pages, sort, new PostsInfo(), 0, syncComments);
    }
}
