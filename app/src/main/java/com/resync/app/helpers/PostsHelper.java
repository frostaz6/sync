package com.resync.app.helpers;

import android.content.Context;
import android.util.Pair;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.net.oauth.OAuthHasGoldRequest;
import com.resync.app.net.oauth.OAuthMarkThreadRequest;

/* JADX INFO: renamed from: t7.y */
/* JADX INFO: compiled from: PostsHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PostsHelper {

    /* JADX INFO: renamed from: t7.y$a */
    /* JADX INFO: compiled from: PostsHelper.java */
    static class MarkViewedThread extends Thread {

        final /* synthetic */ int delayMs;
        final /* synthetic */ Context context;
        final /* synthetic */ String selection;

        MarkViewedThread(int delayMs, Context context, String selection) {
            this.delayMs = delayMs;
            this.context = context;
            this.selection = selection;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Logger.verbose("PostsHelper", "Marking as viewed with delay: " + this.delayMs);
            try {
                Thread.sleep(this.delayMs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.context.getContentResolver().update(RedditProvider.URI_UPDATE_READ, null, this.selection, null);
            this.context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        }
    }

    /* JADX INFO: renamed from: t7.y$b */
    /* JADX INFO: compiled from: PostsHelper.java */
    static class GoldCheckListener implements Response.Listener<Boolean> {

        final /* synthetic */ Context context;
        final /* synthetic */ String fullname;

        /* JADX INFO: renamed from: t7.y$b$a */
        /* JADX INFO: compiled from: PostsHelper.java */
        static class MarkReadErrorListener implements Response.ErrorListener {
            MarkReadErrorListener() {
            }

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                Logger.verbose("PostsHelper", "Error marking as read: " + volleyError.getMessage());
            }
        }

        GoldCheckListener(Context context, String fullname) {
            this.context = context;
            this.fullname = fullname;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Boolean bool) {
            if (this.context != null) {
                if (!bool.booleanValue()) {
                    Logger.verbose("PostsHelper", "No gold. Not marking as read with reddit");
                    return;
                }
                Logger.verbose("PostsHelper", "Marking as read with reddit");
                Context context = this.context;
                OAuthRequestManager.send(context, new OAuthMarkThreadRequest(context, this.fullname, new MarkReadErrorListener()));
            }
        }
    }

    /* JADX INFO: renamed from: t7.y$c */
    /* JADX INFO: compiled from: PostsHelper.java */
    static class HasGoldErrorListener implements Response.ErrorListener {
        HasGoldErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            Logger.verbose("PostsHelper", "Error marking as read");
        }
    }

    public static Pair<String, String> parseSortPair(String sortKey) {
        Logger.verbose("CHECKING SORT: " + sortKey);
        String sortLabel = "New";
        String sortParam = null;
        if (sortKey.equals("sort_999")) {
            sortLabel = "Best";
        } else if (sortKey.equals("sort_6")) {
            sortLabel = "Comments";
        } else if (sortKey.equals("sort_0")) {
            sortLabel = "Hot";
        } else if (sortKey.equals("sort_1")) {
            sortParam = "sort=new";
        } else if (sortKey.equals("sort_2")) {
            sortParam = "sort=Rising";
        } else {
            sortLabel = null;
        }
        if (sortKey.equals("sort_3_0") || sortKey.equals("sort_3_1") || sortKey.equals("sort_3_2") || sortKey.equals("sort_3_3") || sortKey.equals("sort_3_4") || sortKey.equals("sort_3_5")) {
            sortLabel = "Top";
        } else if (sortKey.equals("sort_4_0") || sortKey.equals("sort_4_1") || sortKey.equals("sort_4_2") || sortKey.equals("sort_4_3") || sortKey.equals("sort_4_4") || sortKey.equals("sort_4_5")) {
            sortLabel = "Controversial";
        }
        if (sortKey.equals("sort_3_0") || sortKey.equals("sort_4_0")) {
            sortParam = "t=Hour";
        } else if (sortKey.equals("sort_3_1") || sortKey.equals("sort_4_1")) {
            sortParam = "t=Day";
        } else if (sortKey.equals("sort_3_2") || sortKey.equals("sort_4_2")) {
            sortParam = "t=Week";
        } else if (sortKey.equals("sort_3_3") || sortKey.equals("sort_4_3")) {
            sortParam = "t=Month";
        } else if (sortKey.equals("sort_3_4") || sortKey.equals("sort_4_4")) {
            sortParam = "t=Year";
        } else if (sortKey.equals("sort_3_5") || sortKey.equals("sort_4_5")) {
            sortParam = "t=All";
        }
        return Pair.create(sortLabel, sortParam);
    }

    private static void markAsRead(Context context, String selection, int delayMs) {
        if (context != null && ConnectivityHelper.isConnected(context) && SettingsSingleton.getSettings().markAsRead) {
            if (delayMs == 0) {
                context.getContentResolver().update(RedditProvider.URI_UPDATE_READ, null, selection, null);
                context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
            } else {
                new MarkViewedThread(delayMs, context, selection).start();
            }
            if (!SettingsSingleton.getSettings().markAsReadSync) {
                Logger.verbose("PostsHelper", "Disabled sync.");
            } else if (AccountSingleton.getInstance().isLoggedIn()) {
                OAuthRequestManager.send(context, new OAuthHasGoldRequest(context, new GoldCheckListener(context, selection), new HasGoldErrorListener()));
            } else {
                Logger.verbose("PostsHelper", "Not logged in. Not marking as read");
            }
        }
    }

    public static void markViewed(Context context, Post post) {
        if (post == null || post.isVisitedOrInternal()) {
            return;
        }
        markAsRead(context, post.getId(), 0);
    }

    public static void markViewedDelayed(Context context, Post post) {
        if (post == null || post.isVisitedOrInternal()) {
            return;
        }
        markAsRead(context, post.getId(), 380);
    }
}
