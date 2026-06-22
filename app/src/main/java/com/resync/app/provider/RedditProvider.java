package com.resync.app.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.StaticToast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.images.PostThumbnailHelper;
import com.resync.app.db.RedditDb;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthHideMultipleRequest;

/* JADX INFO: loaded from: classes.dex */
public class RedditProvider extends ContentProvider {

    private RedditDb database;
    static String TAG = RedditProvider.class.getSimpleName();
    public static final Uri URI_DISGUISED_GIF = Uri.parse("content://com.resync.app.provider.RedditProvider/disguised_gif");

    public static final Uri URI_RESTORE_REMOVED = Uri.parse("content://com.resync.app.provider.RedditProvider/restore_removed");

    public static final Uri URI_DELETE_WHOLE_LIST = Uri.parse("content://com.resync.app.provider.RedditProvider/delete_whole_list");

    public static final Uri URI_COMMENTS_VIEWED = Uri.parse("content://com.resync.app.provider.RedditProvider/comments_viewed");
    public static final Uri URI_DELETE_LIST_ITEM = Uri.parse("content://com.resync.app.provider.RedditProvider/delete_list_item");

    public static final Uri URI_BROAD_COMMENT_UPDATE = Uri.parse("content://com.resync.app.provider.RedditProvider/broad_comment_update");
    public static final Uri URI_INSERT_COMMENTS = Uri.parse("content://com.resync.app.provider.RedditProvider/insert_comments");
    public static final Uri URI_UPDATE_COMMENTS_LIST_TIME = Uri.parse("content://com.resync.app.provider.RedditProvider/update_comments_list_time");
    public static final Uri URI_SHOW_COMMENTS = Uri.parse("content://com.resync.app.provider.RedditProvider/show_comments");

    public static final Uri URI_HIDE_COMMENTS = Uri.parse("content://com.resync.app.provider.RedditProvider/hide_comments");
    public static final Uri URI_HIDE_ALL_COMMENTS = Uri.parse("content://com.resync.app.provider.RedditProvider/hide_all_comments");
    public static final Uri URI_SHOW_ALL_COMMENTS = Uri.parse("content://com.resync.app.provider.RedditProvider/show_all_comments");
    public static final Uri URI_MULTIPLE_POSTS = Uri.parse("content://com.resync.app.provider.RedditProvider/multiplePosts");
    public static final Uri URI_SINGLE_POST = Uri.parse("content://com.resync.app.provider.RedditProvider/singlePost");
    public static final Uri URI_POST_COUNT = Uri.parse("content://com.resync.app.provider.RedditProvider/post_count");
    public static final Uri URI_CLEAR = Uri.parse("content://com.resync.app.provider.RedditProvider/clear");
    public static final Uri URI_UPDATE_LIKED = Uri.parse("content://com.resync.app.provider.RedditProvider/update_liked");
    public static final Uri URI_UPDATE_SAVED = Uri.parse("content://com.resync.app.provider.RedditProvider/update_saved");
    public static final Uri URI_UPDATE_HIDDEN = Uri.parse("content://com.resync.app.provider.RedditProvider/update_hidden");
    public static final Uri URI_UPDATE_APPROVED = Uri.parse("content://com.resync.app.provider.RedditProvider/update_approved");
    public static final Uri URI_UPDATE_DISTINGUISHED = Uri.parse("content://com.resync.app.provider.RedditProvider/update_distinguished");
    public static final Uri URI_UPDATE_LOCKED = Uri.parse("content://com.resync.app.provider.RedditProvider/update_locked");
    public static final Uri URI_UPDATE_UNLOCKED = Uri.parse("content://com.resync.app.provider.RedditProvider/update_unlocked");
    public static final Uri URI_UPDATE_UNDISTINGUISHED = Uri.parse("content://com.resync.app.provider.RedditProvider/update_undistinguished");
    public static final Uri URI_UPDATE_STICKY = Uri.parse("content://com.resync.app.provider.RedditProvider/update_sticky");
    public static final Uri URI_UPDATE_NSFW = Uri.parse("content://com.resync.app.provider.RedditProvider/update_nsfw");
    public static final Uri URI_UPDATE_SPOILER = Uri.parse("content://com.resync.app.provider.RedditProvider/update_spoiler");
    public static final Uri URI_UPDATE_POST_FLAIR = Uri.parse("content://com.resync.app.provider.RedditProvider/updatE_post_flair");
    public static final Uri URI_UPDATE_READ = Uri.parse("content://com.resync.app.provider.RedditProvider/update_read");
    public static final Uri URI_ACCOUNTS = Uri.parse("content://com.resync.app.provider.RedditProvider/accounts");
    public static final Uri URI_HIDE_ALL = Uri.parse("content://com.resync.app.provider.RedditProvider/hide_all");
    public static final Uri URI_CLEAN_UP_POSTS = Uri.parse("content://com.resync.app.provider.RedditProvider/clean_up_posts");
    public static final Uri URI_CLEAN_UP_LISTS = Uri.parse("content://com.resync.app.provider.RedditProvider/clean_up_lists");
    public static final Uri URI_CLEAN_UP_VIEWED = Uri.parse("content://com.resync.app.provider.RedditProvider/clean_up_viewed");
    public static final Uri URI_SWAP_ACCOUNTS = Uri.parse("content://com.resync.app.provider.RedditProvider/swap_accounts");
    public static final Uri URI_NEW_ACCOUNTS = Uri.parse("content://com.resync.app.provider.RedditProvider/new_accounts");

    @Override // android.content.ContentProvider
    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        String str;
        String str2 = null;
        if (uri.equals(URI_RESTORE_REMOVED)) {
            SQLiteDatabase writableDatabase = this.database.getWritableDatabase();
            writableDatabase.beginTransaction();
            for (ContentValues contentValues : contentValuesArr) {
                if (contentValues != null) {
                    String asString = contentValues.getAsString("_id");
                    contentValues.remove("_id");
                    Logger.verbose("VALUE: " + contentValues);
                    writableDatabase.update("tbl_posts", contentValues, "_id='" + asString + "'", null);
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            return 0;
        }
        if (uri.equals(URI_NEW_ACCOUNTS)) {
            SQLiteDatabase writableDatabase2 = this.database.getWritableDatabase();
            writableDatabase2.beginTransaction();
            for (int i = 0; i < contentValuesArr.length; i++) {
                try {
                    Logger.verbose("Updating: " + contentValuesArr[i]);
                    writableDatabase2.update("tbl_posts", contentValuesArr[i], "post_author_fullname='" + contentValuesArr[i].getAsString("post_author_fullname") + "'", null);
                } catch (Exception e) {
                    Logger.logException(e);
                }
            }
            writableDatabase2.setTransactionSuccessful();
            writableDatabase2.endTransaction();
            return 0;
        }
        Uri uri2 = URI_MULTIPLE_POSTS;
        if (!uri.equals(uri2) && !uri.equals(URI_INSERT_COMMENTS)) {
            return 0;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (uri.equals(uri2)) {
            delete(URI_CLEAN_UP_POSTS, null, null);
            delete(URI_CLEAN_UP_LISTS, null, null);
            delete(URI_CLEAN_UP_VIEWED, null, null);
        }
        Logger.verbose(TAG, "Bulk insert cleanup duration: " + (System.currentTimeMillis() - jCurrentTimeMillis));
        SQLiteDatabase writableDatabase3 = this.database.getWritableDatabase();
        writableDatabase3.beginTransaction();
        int i = 0;
        int insertedCount = 0;
        while (i < contentValuesArr.length) {
            try {
                if (i < contentValuesArr.length / 2) {
                    writableDatabase3.insertWithOnConflict("tbl_posts", str2, contentValuesArr[i], 3);
                } else if (writableDatabase3.insertWithOnConflict("tbl_lists", str2, contentValuesArr[i], 4) != -1) {
                    insertedCount++;
                }
                str = str2;
            } catch (SQLiteConstraintException unused) {
                if (contentValuesArr[i].containsKey("num_comments")) {
                    writableDatabase3.execSQL("UPDATE tbl_posts SET new_comment_count=" + contentValuesArr[i].getAsInteger("num_comments").intValue() + "-num_comments WHERE _id='" + contentValuesArr[i].getAsString("_id") + "'");
                }
                if (TextUtils.isEmpty(contentValuesArr[i].getAsString("title"))) {
                    contentValuesArr[i].remove("title");
                }
                if (TextUtils.isEmpty(contentValuesArr[i].getAsString("upvote_ratio"))) {
                    contentValuesArr[i].remove("upvote_ratio");
                }
                if (contentValuesArr[i].containsKey("view_count") && contentValuesArr[i].getAsInteger("view_count").intValue() == 0) {
                    contentValuesArr[i].remove("view_count");
                }
                if (contentValuesArr[i].get("level") != null && contentValuesArr[i].getAsInteger("level").intValue() == 0) {
                    contentValuesArr[i].remove("level");
                }
                if (contentValuesArr[i].get("new_comment") != null) {
                    contentValuesArr[i].remove("new_comment");
                    contentValuesArr[i].put("new_comment", (Integer) 0);
                }
                if (contentValuesArr[i].getAsInteger("sync_type").intValue() == 3) {
                    contentValuesArr[i].remove("sync_type");
                }
                str = null;
                writableDatabase3.update("tbl_posts", contentValuesArr[i], "_id='" + contentValuesArr[i].getAsString("_id") + "'", null);
            } catch (Exception unused2) {
                writableDatabase3.endTransaction();
                return 0;
            }
            i++;
            str2 = str;
        }
        writableDatabase3.setTransactionSuccessful();
        writableDatabase3.endTransaction();
        Logger.verbose(TAG, "Bulk insert duration: " + (System.currentTimeMillis() - jCurrentTimeMillis));
        return insertedCount;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        if (uri.equals(URI_DELETE_WHOLE_LIST)) {
            SQLiteDatabase writableDatabase = this.database.getWritableDatabase();
            writableDatabase.beginTransaction();
            Logger.verbose("Deleting: " + str);
            writableDatabase.execSQL("DELETE FROM tbl_lists WHERE lists_id='" + str.replaceAll("\\'", "\\'\\'") + "'");
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            return 0;
        }
        if (uri.equals(URI_DELETE_LIST_ITEM)) {
            SQLiteDatabase writableDatabase2 = this.database.getWritableDatabase();
            writableDatabase2.beginTransaction();
            Logger.verbose("Deleting: " + str);
            writableDatabase2.execSQL("DELETE FROM tbl_lists WHERE lists_post_id='" + str + "'");
            writableDatabase2.setTransactionSuccessful();
            writableDatabase2.endTransaction();
            return 0;
        }
        if (uri.equals(URI_CLEAN_UP_POSTS)) {
            SQLiteDatabase writableDatabase3 = this.database.getWritableDatabase();
            writableDatabase3.beginTransaction();
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(11, -48);
                writableDatabase3.execSQL("DELETE FROM tbl_posts WHERE age <= '" + simpleDateFormat.format(calendar.getTime()) + "'");
                writableDatabase3.setTransactionSuccessful();
            } catch (Exception unused) {
                Logger.verbose("Error cleaning up posts.");
            }
            writableDatabase3.endTransaction();
            return 0;
        }
        if (uri.equals(URI_CLEAN_UP_LISTS)) {
            SQLiteDatabase writableDatabase4 = this.database.getWritableDatabase();
            writableDatabase4.beginTransaction();
            try {
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTimeInMillis(System.currentTimeMillis());
                calendar2.add(11, -48);
                writableDatabase4.execSQL("DELETE FROM tbl_lists WHERE lists_age <= '" + simpleDateFormat2.format(calendar2.getTime()) + "'");
                writableDatabase4.setTransactionSuccessful();
            } catch (Exception unused2) {
                Logger.verbose("Error cleaning up lists.");
            }
            writableDatabase4.endTransaction();
            return 0;
        }
        if (uri.equals(URI_CLEAN_UP_VIEWED)) {
            SQLiteDatabase writableDatabase5 = this.database.getWritableDatabase();
            writableDatabase5.beginTransaction();
            try {
                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTimeInMillis(System.currentTimeMillis());
                calendar3.add(11, -168);
                writableDatabase5.execSQL("DELETE FROM tbl_viewed_new WHERE viewed_age <= '" + simpleDateFormat3.format(calendar3.getTime()) + "'");
                writableDatabase5.setTransactionSuccessful();
            } catch (Exception unused3) {
                Logger.verbose("Error cleaning up viewed.");
            }
            writableDatabase5.endTransaction();
            return 0;
        }
        if (uri.equals(URI_CLEAR)) {
            this.database.getWritableDatabase().delete("tbl_lists", null, null);
        } else if (uri.equals(URI_SWAP_ACCOUNTS)) {
            SQLiteDatabase writableDatabase6 = this.database.getWritableDatabase();
            writableDatabase6.delete("tbl_posts", null, null);
            writableDatabase6.delete("tbl_lists", null, null);
        } else if (uri.equals(URI_ACCOUNTS)) {
            SQLiteDatabase writableDatabase7 = this.database.getWritableDatabase();
            writableDatabase7.beginTransaction();
            writableDatabase7.execSQL("DELETE FROM tbl_accounts WHERE UPPER(account_name)=UPPER('" + str + "')");
            writableDatabase7.setTransactionSuccessful();
            writableDatabase7.endTransaction();
        }
        return 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (uri.equals(URI_ACCOUNTS)) {
            SQLiteDatabase writableDatabase = this.database.getWritableDatabase();
            writableDatabase.beginTransaction();
            writableDatabase.insertWithOnConflict("tbl_accounts", null, contentValues, 5);
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        this.database = new RedditDb(getContext(), "db_reddit_sync", null, 802);
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
        if (uri.equals(URI_MULTIPLE_POSTS)) {
            sQLiteQueryBuilder.setTables("tbl_lists INNER JOIN tbl_posts ON tbl_posts._id = tbl_lists.lists_post_id LEFT OUTER JOIN tbl_viewed_new ON tbl_posts._id = tbl_viewed_new.id");
            Cursor cursorQuery = sQLiteQueryBuilder.query(this.database.getReadableDatabase(), strArr, str, null, null, null, str2);
            cursorQuery.setNotificationUri(getContext().getContentResolver(), uri);
            return cursorQuery;
        }
        if (uri.equals(URI_POST_COUNT)) {
            sQLiteQueryBuilder.setTables("tbl_lists");
            return sQLiteQueryBuilder.query(this.database.getReadableDatabase(), strArr, str, strArr2, null, null, str2);
        }
        if (uri.equals(URI_ACCOUNTS)) {
            sQLiteQueryBuilder.setTables("tbl_accounts");
            try {
                Cursor cursorQuery2 = sQLiteQueryBuilder.query(this.database.getReadableDatabase(), strArr, str, strArr2, null, null, str2);
                cursorQuery2.setNotificationUri(getContext().getContentResolver(), uri);
                return cursorQuery2;
            } catch (SQLiteException e) {
                if (!TextUtils.isEmpty(e.getMessage()) && e.getMessage().contains("no such column")) {
                    Logger.verbose("no such column detected: " + e.getMessage());
                    SQLiteDatabase writableDatabase = this.database.getWritableDatabase();
                    writableDatabase.execSQL("DROP TABLE IF EXISTS tbl_accounts");
                    writableDatabase.execSQL("create table if not exists tbl_accounts(account_id integer primary key, account_name text, account_subs text, account_mods text, account_multis text, account_refresh_token text );");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("account_name", "Logged out");
                    writableDatabase.insertWithOnConflict("tbl_accounts", null, contentValues, 2);
                    Logger.verbose("New account table created");
                    sQLiteQueryBuilder.setTables("tbl_accounts");
                    Cursor cursorQuery3 = sQLiteQueryBuilder.query(this.database.getReadableDatabase(), strArr, str, strArr2, null, null, str2);
                    cursorQuery3.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorQuery3;
                }
            }
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        ArrayList arrayList;
        if (uri.equals(URI_RESTORE_REMOVED)) {
            SQLiteDatabase writableDatabase = this.database.getWritableDatabase();
            writableDatabase.beginTransaction();
            writableDatabase.update("tbl_posts", contentValues, "_id='" + str + "'", null);
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            return 0;
        }
        if (uri.equals(URI_DISGUISED_GIF)) {
            SQLiteDatabase writableDatabase2 = this.database.getWritableDatabase();
            writableDatabase2.beginTransaction();
            writableDatabase2.execSQL("UPDATE tbl_posts SET sync_type='7' WHERE _id='" + strArr[0] + "'");
            writableDatabase2.execSQL("UPDATE tbl_posts SET url='" + strArr[1] + "' WHERE _id='" + strArr[0] + "'");
            writableDatabase2.setTransactionSuccessful();
            writableDatabase2.endTransaction();
            return 0;
        }
        if (uri.equals(URI_HIDE_ALL_COMMENTS)) {
            SQLiteDatabase writableDatabase3 = this.database.getWritableDatabase();
            writableDatabase3.beginTransaction();
            SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
            sQLiteQueryBuilder.setTables("tbl_lists INNER JOIN tbl_posts ON tbl_posts._id = tbl_lists.lists_post_id LEFT OUTER JOIN tbl_viewed_new ON tbl_posts._id = tbl_viewed_new.id");
            Cursor cursorQuery = sQLiteQueryBuilder.query(this.database.getReadableDatabase(), RedditDb.POST_COLUMNS, "lists_id='" + str.toLowerCase(Locale.ENGLISH).replaceAll("\\'", "\\'\\'") + "'", null, null, null, "tbl_lists.lists_time ASC");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Cursor size: ");
            sb2.append(cursorQuery);
            Logger.verbose(sb2.toString());
            if (cursorQuery != null && cursorQuery.getCount() > 1) {
                ArrayList arrayList2 = new ArrayList();
                for (int i = 1; i < cursorQuery.getCount(); i++) {
                    cursorQuery.moveToPosition(i);
                    if (cursorQuery.getInt(cursorQuery.getColumnIndex("level")) == 0) {
                        arrayList2.add(Integer.valueOf(i));
                    }
                }
                int i = 0;
                while (i < arrayList2.size()) {
                    int iIntValue = ((Integer) arrayList2.get(i)).intValue();
                    int count = cursorQuery.getCount();
                    if (i < arrayList2.size() - 1) {
                        count = ((Integer) arrayList2.get(i + 1)).intValue();
                    }
                    if (iIntValue != count) {
                        int j = iIntValue + 1;
                        ArrayList arrayList3 = new ArrayList();
                        while (j < count) {
                            cursorQuery.moveToPosition(j);
                            String string = cursorQuery.getString(cursorQuery.getColumnIndex("_id"));
                            arrayList3.add(string);
                            writableDatabase3.execSQL("UPDATE tbl_posts SET collapsed='1' WHERE _id='" + string + "'");
                            j++;
                            arrayList2 = arrayList2;
                        }
                        arrayList = arrayList2;
                        if (arrayList3.size() > 0) {
                            String strJoin = TextUtils.join(",", arrayList3);
                            int size = arrayList3.size();
                            cursorQuery.moveToPosition(iIntValue);
                            String string2 = cursorQuery.getString(cursorQuery.getColumnIndex("_id"));
                            writableDatabase3.execSQL("UPDATE tbl_posts SET childIds='" + strJoin + "' WHERE _id='" + string2 + "'");
                            writableDatabase3.execSQL("UPDATE tbl_posts SET childCount='" + size + "' WHERE _id='" + string2 + "'");
                        }
                    } else {
                        arrayList = arrayList2;
                    }
                    i++;
                    arrayList2 = arrayList;
                }
            }
            writableDatabase3.setTransactionSuccessful();
            writableDatabase3.endTransaction();
            return 0;
        }
        if (uri.equals(URI_SHOW_ALL_COMMENTS)) {
            SQLiteDatabase writableDatabase4 = this.database.getWritableDatabase();
            writableDatabase4.beginTransaction();
            SQLiteQueryBuilder sQLiteQueryBuilder2 = new SQLiteQueryBuilder();
            sQLiteQueryBuilder2.setTables("tbl_lists INNER JOIN tbl_posts ON tbl_posts._id = tbl_lists.lists_post_id LEFT OUTER JOIN tbl_viewed_new ON tbl_posts._id = tbl_viewed_new.id");
            Cursor cursorQuery2 = sQLiteQueryBuilder2.query(this.database.getReadableDatabase(), RedditDb.POST_COLUMNS, "lists_id='" + str.toLowerCase(Locale.ENGLISH).replaceAll("\\'", "\\'\\'") + "'", null, null, null, "tbl_lists.lists_time ASC");
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Cursor size: ");
            sb3.append(cursorQuery2);
            Logger.verbose(sb3.toString());
            if (cursorQuery2 != null && cursorQuery2.getCount() > 1) {
                for (int i = 1; i < cursorQuery2.getCount(); i++) {
                    cursorQuery2.moveToPosition(i);
                    String string3 = cursorQuery2.getString(cursorQuery2.getColumnIndex("_id"));
                    writableDatabase4.execSQL("UPDATE tbl_posts SET childIds=NULL WHERE _id='" + string3 + "'");
                    writableDatabase4.execSQL("UPDATE tbl_posts SET childCount='0' WHERE _id='" + string3 + "'");
                    writableDatabase4.execSQL("UPDATE tbl_posts SET collapsed='0' WHERE _id='" + string3 + "'");
                }
            }
            cursorQuery2.close();
            writableDatabase4.setTransactionSuccessful();
            writableDatabase4.endTransaction();
            return 0;
        }
        if (uri.equals(URI_COMMENTS_VIEWED)) {
            SQLiteDatabase writableDatabase5 = this.database.getWritableDatabase();
            writableDatabase5.beginTransaction();
            writableDatabase5.execSQL("UPDATE tbl_posts SET new_comment='0' WHERE _id IN (SELECT lists_post_id FROM tbl_lists WHERE lists_id='" + str.replaceAll("\\'", "\\'\\'") + "')");
            writableDatabase5.setTransactionSuccessful();
            writableDatabase5.endTransaction();
            return 0;
        }
        if (uri.equals(URI_BROAD_COMMENT_UPDATE)) {
            SQLiteDatabase writableDatabase6 = this.database.getWritableDatabase();
            writableDatabase6.beginTransaction();
            writableDatabase6.update("tbl_posts", contentValues, "_id='" + str + "'", null);
            writableDatabase6.setTransactionSuccessful();
            writableDatabase6.endTransaction();
            return 0;
        }
        if (uri.equals(URI_UPDATE_COMMENTS_LIST_TIME)) {
            SQLiteDatabase writableDatabase7 = this.database.getWritableDatabase();
            writableDatabase7.beginTransaction();
            Logger.verbose("Incrementing post list time by: " + strArr[2]);
            writableDatabase7.execSQL("UPDATE tbl_lists SET lists_time=lists_time + " + strArr[2] + " WHERE lists_id='" + strArr[0].replaceAll("\\'", "\\'\\'") + "' AND lists_time> " + strArr[1]);
            writableDatabase7.setTransactionSuccessful();
            writableDatabase7.endTransaction();
            return 0;
        }
        if (uri.equals(URI_SHOW_COMMENTS)) {
            SQLiteDatabase writableDatabase8 = this.database.getWritableDatabase();
            writableDatabase8.beginTransaction();
            writableDatabase8.execSQL("UPDATE tbl_posts SET childIds=NULL WHERE _id='" + str + "'");
            writableDatabase8.execSQL("UPDATE tbl_posts SET childCount='0' WHERE _id='" + str + "'");
            for (String str2 : strArr) {
                writableDatabase8.execSQL("UPDATE tbl_posts SET collapsed='0' WHERE _id='" + str2 + "'");
            }
            writableDatabase8.setTransactionSuccessful();
            writableDatabase8.endTransaction();
            return 0;
        }
        if (uri.equals(URI_HIDE_COMMENTS)) {
            SQLiteDatabase writableDatabase9 = this.database.getWritableDatabase();
            writableDatabase9.beginTransaction();
            writableDatabase9.execSQL("UPDATE tbl_posts SET childIds='" + TextUtils.join(",", strArr) + "' WHERE _id='" + str + "'");
            writableDatabase9.execSQL("UPDATE tbl_posts SET childCount='" + strArr.length + "' WHERE _id='" + str + "'");
            for (int i = 0; i < strArr.length; i++) {
                if (!strArr[i].equalsIgnoreCase(str)) {
                    writableDatabase9.execSQL("UPDATE tbl_posts SET collapsed='1' WHERE _id='" + strArr[i] + "'");
                }
            }
            writableDatabase9.setTransactionSuccessful();
            writableDatabase9.endTransaction();
            return 0;
        }
        if (uri.equals(URI_HIDE_ALL)) {
            SQLiteQueryBuilder sQLiteQueryBuilder3 = new SQLiteQueryBuilder();
            sQLiteQueryBuilder3.setTables("tbl_posts LEFT OUTER JOIN tbl_viewed_new ON tbl_posts._id = tbl_viewed_new.id");
            ArrayList arrayList4 = new ArrayList();
            Cursor cursorQuery3 = sQLiteQueryBuilder3.query(this.database.getReadableDatabase(), new String[]{"_id", "title"}, "hidden='0' AND (internally_visited='1' OR visited='1' )", null, null, null, null);
            for (int i = 0; i < cursorQuery3.getCount(); i++) {
                cursorQuery3.moveToPosition(i);
                Logger.verbose("FOUND: " + cursorQuery3.getString(cursorQuery3.getColumnIndex("_id")) + StringUtils.SPACE + cursorQuery3.getString(cursorQuery3.getColumnIndex("title")));
                arrayList4.add(cursorQuery3.getString(cursorQuery3.getColumnIndex("_id")));
            }
            cursorQuery3.close();
            if (arrayList4.isEmpty()) {
                StaticToast.show("No posts hidden!");
                return 0;
            }
            StaticToast.show(arrayList4.size() + " posts hidden!");
            SQLiteDatabase writableDatabase10 = this.database.getWritableDatabase();
            writableDatabase10.beginTransaction();
            try {
                Iterator it = arrayList4.iterator();
                while (it.hasNext()) {
                    writableDatabase10.execSQL("UPDATE tbl_posts SET hidden='1' WHERE _id='" + ((String) it.next()) + "'");
                }
            } catch (Exception unused) {
            }
            writableDatabase10.setTransactionSuccessful();
            writableDatabase10.endTransaction();
            RedditApplication.getAppContext().getContentResolver().notifyChange(URI_MULTIPLE_POSTS, null);
            Logger.verbose("HIDE_READ", "Total to hide: " + arrayList4.size());
            while (arrayList4.size() > 0) {
                int iMin = Math.min(50, arrayList4.size());
                String[] strArr2 = new String[iMin];
                for (int i = 0; i < iMin; i++) {
                    strArr2[i] = (String) arrayList4.remove(arrayList4.size() - 1);
                }
                Logger.verbose("HIDE_READ", "Hiding as read, batch size: " + iMin);
                if (SettingsSingleton.getSettings().hideReddit && AccountSingleton.getInstance().isLoggedIn()) {
                    OAuthRequestManager.sendWithAppContext(new OAuthHideMultipleRequest(getContext(), strArr2));
                }
            }
            return 0;
        }
        if (uri.equals(URI_CLEAR)) {
            SQLiteDatabase writableDatabase11 = this.database.getWritableDatabase();
            writableDatabase11.beginTransaction();
            writableDatabase11.execSQL("DELETE FROM tbl_lists WHERE lists_id='" + str.replaceAll("\\'", "\\'\\'") + "'");
            writableDatabase11.setTransactionSuccessful();
            writableDatabase11.endTransaction();
            return 0;
        }
        if (uri.equals(URI_UPDATE_LIKED)) {
            SQLiteDatabase writableDatabase12 = this.database.getWritableDatabase();
            writableDatabase12.beginTransaction();
            try {
                writableDatabase12.execSQL("UPDATE tbl_posts SET likes='" + contentValues.getAsString("likes") + "' WHERE _id='" + contentValues.getAsString("_id") + "'");
                writableDatabase12.execSQL("UPDATE tbl_posts SET score=score+" + str + " WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused2) {
            }
            writableDatabase12.setTransactionSuccessful();
            writableDatabase12.endTransaction();
        }
        if (uri.equals(URI_UPDATE_HIDDEN)) {
            SQLiteDatabase writableDatabase13 = this.database.getWritableDatabase();
            writableDatabase13.beginTransaction();
            try {
                writableDatabase13.execSQL("UPDATE tbl_posts SET hidden='" + contentValues.getAsInteger("hidden") + "' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused3) {
            }
            writableDatabase13.setTransactionSuccessful();
            writableDatabase13.endTransaction();
            return 1;
        }
        if (uri.equals(URI_UPDATE_APPROVED)) {
            SQLiteDatabase writableDatabase14 = this.database.getWritableDatabase();
            writableDatabase14.beginTransaction();
            try {
                writableDatabase14.execSQL("UPDATE tbl_posts SET approved_by='approved' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused4) {
            }
            writableDatabase14.setTransactionSuccessful();
            writableDatabase14.endTransaction();
        } else if (uri.equals(URI_UPDATE_DISTINGUISHED)) {
            SQLiteDatabase writableDatabase15 = this.database.getWritableDatabase();
            writableDatabase15.beginTransaction();
            try {
                writableDatabase15.execSQL("UPDATE tbl_posts SET distinguished='distinguished' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused5) {
            }
            writableDatabase15.setTransactionSuccessful();
            writableDatabase15.endTransaction();
        } else if (uri.equals(URI_UPDATE_LOCKED)) {
            SQLiteDatabase writableDatabase16 = this.database.getWritableDatabase();
            writableDatabase16.beginTransaction();
            try {
                writableDatabase16.execSQL("UPDATE tbl_posts SET locked='1' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused6) {
            }
            writableDatabase16.setTransactionSuccessful();
            writableDatabase16.endTransaction();
        } else if (uri.equals(URI_UPDATE_UNLOCKED)) {
            SQLiteDatabase writableDatabase17 = this.database.getWritableDatabase();
            writableDatabase17.beginTransaction();
            try {
                writableDatabase17.execSQL("UPDATE tbl_posts SET locked='0' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused7) {
            }
            writableDatabase17.setTransactionSuccessful();
            writableDatabase17.endTransaction();
        } else if (uri.equals(URI_UPDATE_UNDISTINGUISHED)) {
            SQLiteDatabase writableDatabase18 = this.database.getWritableDatabase();
            writableDatabase18.beginTransaction();
            try {
                writableDatabase18.execSQL("UPDATE tbl_posts SET distinguished='' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused8) {
            }
            writableDatabase18.setTransactionSuccessful();
            writableDatabase18.endTransaction();
        }
        if (uri.equals(URI_UPDATE_STICKY)) {
            SQLiteDatabase writableDatabase19 = this.database.getWritableDatabase();
            writableDatabase19.beginTransaction();
            try {
                writableDatabase19.execSQL("UPDATE tbl_posts SET stickied='" + contentValues.getAsInteger("stickied") + "' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused9) {
            }
            writableDatabase19.setTransactionSuccessful();
            writableDatabase19.endTransaction();
        }
        if (uri.equals(URI_UPDATE_NSFW)) {
            SQLiteDatabase writableDatabase20 = this.database.getWritableDatabase();
            writableDatabase20.beginTransaction();
            try {
                writableDatabase20.execSQL("UPDATE tbl_posts SET over_18='" + contentValues.getAsInteger("over_18") + "' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused10) {
            }
            writableDatabase20.setTransactionSuccessful();
            writableDatabase20.endTransaction();
        }
        if (uri.equals(URI_UPDATE_SPOILER)) {
            SQLiteDatabase writableDatabase21 = this.database.getWritableDatabase();
            writableDatabase21.beginTransaction();
            try {
                int iIntValue2 = contentValues.getAsInteger("spoiler").intValue();
                writableDatabase21.execSQL("UPDATE tbl_posts SET spoiler='" + iIntValue2 + "' WHERE _id='" + contentValues.getAsString("_id") + "'");
                if (iIntValue2 == 1) {
                    writableDatabase21.execSQL("UPDATE tbl_posts SET sync_type='3' WHERE _id='" + contentValues.getAsString("_id") + "'");
                    writableDatabase21.execSQL("UPDATE tbl_posts SET thumbnail='" + PostThumbnailHelper.PLAY_SPOILER + "'  WHERE _id='" + contentValues.getAsString("_id") + "'");
                }
            } catch (Exception unused11) {
            }
            writableDatabase21.setTransactionSuccessful();
            writableDatabase21.endTransaction();
        }
        if (uri.equals(URI_UPDATE_POST_FLAIR)) {
            SQLiteDatabase writableDatabase22 = this.database.getWritableDatabase();
            writableDatabase22.beginTransaction();
            try {
                writableDatabase22.execSQL("UPDATE tbl_posts SET link_flair_text='" + contentValues.getAsString("link_flair_text") + "' WHERE _id='" + contentValues.getAsString("_id") + "'");
                writableDatabase22.execSQL("UPDATE tbl_posts SET link_flair_background_color='" + contentValues.getAsString("link_flair_background_color") + "' WHERE _id='" + contentValues.getAsString("_id") + "'");
                writableDatabase22.execSQL("UPDATE tbl_posts SET link_flair_id='" + contentValues.getAsString("link_flair_id") + "' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused12) {
            }
            writableDatabase22.setTransactionSuccessful();
            writableDatabase22.endTransaction();
        }
        if (uri.equals(URI_UPDATE_SAVED)) {
            SQLiteDatabase writableDatabase23 = this.database.getWritableDatabase();
            writableDatabase23.beginTransaction();
            try {
                writableDatabase23.execSQL("UPDATE tbl_posts SET saved='" + contentValues.getAsString("saved") + "' WHERE _id='" + contentValues.getAsString("_id") + "'");
            } catch (Exception unused13) {
            }
            writableDatabase23.setTransactionSuccessful();
            writableDatabase23.endTransaction();
        } else if (uri.equals(URI_UPDATE_READ)) {
            SQLiteDatabase writableDatabase24 = this.database.getWritableDatabase();
            writableDatabase24.beginTransaction();
            try {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("id", str);
                contentValues2.put("internally_visited", (Integer) 1);
                contentValues2.put("viewed_age", RedditDb.now());
                writableDatabase24.insertWithOnConflict("tbl_viewed_new", null, contentValues2, 4);
            } catch (Exception unused14) {
            }
            try {
                writableDatabase24.execSQL("UPDATE tbl_posts SET visited='1' WHERE _id='" + str + "'");
            } catch (Exception unused15) {
            }
            writableDatabase24.setTransactionSuccessful();
            writableDatabase24.endTransaction();
        }
        if (!uri.equals(URI_ACCOUNTS)) {
            if (!uri.equals(URI_SINGLE_POST)) {
                return 0;
            }
            SQLiteDatabase writableDatabase25 = this.database.getWritableDatabase();
            writableDatabase25.beginTransaction();
            contentValues.remove("internally_visited");
            contentValues.remove("lists_time");
            contentValues.remove("age");
            writableDatabase25.update("tbl_posts", contentValues, "_id='" + contentValues.getAsString("_id") + "'", null);
            writableDatabase25.setTransactionSuccessful();
            writableDatabase25.endTransaction();
            return 0;
        }
        SQLiteDatabase writableDatabase26 = this.database.getWritableDatabase();
        writableDatabase26.beginTransaction();
        writableDatabase26.execSQL("UPDATE tbl_accounts SET " + strArr[1] + "='" + strArr[0] + "' WHERE UPPER(account_name)=UPPER('" + str + "')");
        writableDatabase26.setTransactionSuccessful();
        writableDatabase26.endTransaction();
        return 0;
    }
}
