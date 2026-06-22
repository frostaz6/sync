package com.resync.app.managers;

import android.content.ContentValues;
import android.content.Context;
import com.resync.app.R;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Kinds;
import com.resync.app.model.Post;
import com.resync.app.net.oauth.OAuthSaveRequest;

/* JADX INFO: renamed from: q8.d */
/* JADX INFO: compiled from: SaveManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class SaveManager {
    public static boolean toggleSave(Context context, Post post) {
        return toggleSaveWithCategory(context, post, null);
    }

    public static boolean toggleSaveWithCategory(Context context, Post post, String str) {
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            StaticToast.show(context, R.string.common_generic_error_logged_out);
            return false;
        }
        int saved = !post.isSaved() ? 1 : 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", post.getId());
        contentValues.put("saved", Integer.valueOf(saved));
        context.getContentResolver().update(RedditProvider.URI_UPDATE_SAVED, contentValues, null, null);
        context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        OAuthRequestManager.send(context, new OAuthSaveRequest(context, Kinds.toPrefix(post.getKind()), post.getId(), post.isSaved(), str));
        return true;
    }
}
