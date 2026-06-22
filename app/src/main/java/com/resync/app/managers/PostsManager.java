package com.resync.app.managers;

import android.content.Context;
import android.content.CursorLoader;
import android.database.DatabaseUtils;
import android.text.TextUtils;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import java.util.Locale;
import com.resync.app.db.RedditDb;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: q8.c */
/* JADX INFO: compiled from: PostsManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class PostsManager {
    public static CursorLoader createSearchLoader(Context context, String str, boolean z, String str2) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(buildBaseSelection(str));
        if (z) {
            sb2.append("AND sync_type='11'");
            if (!TextUtils.isEmpty(str2)) {
                sb2.append(" AND ");
                sb2.append("body_processed");
                sb2.append(" LIKE '%");
                sb2.append(DatabaseUtils.sqlEscapeString(str2).replaceAll("'", ""));
                sb2.append("%'");
            }
        }
        return new CursorLoader(context, RedditProvider.URI_MULTIPLE_POSTS, RedditDb.POST_COLUMNS, sb2.toString(), null, "tbl_lists.lists_time ASC");
    }

    public static String buildBaseSelection(String str) {
        return "lists_id='" + str.toLowerCase(Locale.ENGLISH).replaceAll("\\'", "\\'\\'") + "' AND collapsed='0'";
    }

    public static CursorLoader createPostsLoader(Context context, String str, int section) {
        Logger.verbose("Creating new loader: " + str);
        return new CursorLoader(context, RedditProvider.URI_MULTIPLE_POSTS, RedditDb.POST_COLUMNS, buildSelection(str, section), null, "tbl_lists.lists_time ASC");
    }

    public static String buildSelection(String str, int section) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("lists_id='");
        sb2.append(str.toLowerCase(Locale.ENGLISH).replaceAll("\\'", "\\'\\'"));
        sb2.append("' ");
        sb2.append(SettingsSingleton.getSettings().nsfw ? " AND over_18='0'" : "");
        String string = sb2.toString();
        if (!MaterialPostsHelper.isUserProfile(str)) {
            string = string + " AND hidden='0'";
        }
        if (section == 6) {
            string = (string + " AND sync_type!='17'") + " AND sync_type!='15'";
        }
        Logger.verbose("Query: " + string);
        return string;
    }
}
