package com.resync.app.managers;

import com.resync.app.R;

import android.content.Context;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: q8.f */
/* JADX INFO: compiled from: VoteManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class VoteManager {
    public static boolean vote(Context context, int kind, String id, int currentLikes, int newLikes) {
        if (!com.resync.app.singleton.AccountSingleton.getInstance().isLoggedIn()) {
            com.resync.app.helpers.ui.StaticToast.show(context, R.string.common_generic_error_logged_out);
            return false;
        }
        if (kind == 0 || kind == 1) {
            android.content.ContentValues contentValues = new android.content.ContentValues();
            contentValues.put("_id", id);
            int likes = 0;
            if (currentLikes != newLikes) {
                likes = newLikes;
            }
            contentValues.put("likes", Integer.valueOf(likes));
            String selection;
            if (currentLikes == -1 && newLikes == -1) {
                selection = "1";
            } else if (currentLikes == -1 && newLikes == 1) {
                selection = "2";
            } else if (currentLikes == 0 && newLikes == -1) {
                selection = "-1";
            } else if (currentLikes == 0 && newLikes == 1) {
                selection = "1";
            } else if (currentLikes == 1 && newLikes == 1) {
                selection = "-1";
            } else if (currentLikes == 1 && newLikes == -1) {
                selection = "-2";
            } else {
                selection = null;
            }
            context.getContentResolver().update(com.resync.app.provider.RedditProvider.URI_UPDATE_LIKED, contentValues, selection, null);
            context.getContentResolver().notifyChange(com.resync.app.provider.RedditProvider.URI_MULTIPLE_POSTS, null);
        }
        com.resync.app.net.OAuthRequestManager.send(context, new com.resync.app.net.oauth.OAuthVoteRequest(context, kind, id, currentLikes, newLikes));
        return true;
    }

    public static boolean voteOnPost(Context context, int kind, Post post, int newLikes) {
        return vote(context, kind, post.getId(), post.getLikes(), newLikes);
    }
}
