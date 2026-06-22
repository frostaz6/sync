package com.resync.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.mp4parser.boxes.iso14496.part15.SyncSampleEntry;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: l8.a */
/* JADX INFO: compiled from: RedditDb.java */
/* JADX INFO: loaded from: classes.dex */
public class RedditDb extends SQLiteOpenHelper {

    public static final String[] POST_COLUMNS = {"_id", "link_id", "parent_id", "hash", "kind", "sync_type", "likes", "num_reports", "num_comments", "gilded", "silvered", "platinumed", "score", "level", "controversiality", "new_comment_count", "view_count", "award_count", "subreddit", "subreddit_id", "author", "timestamp", "edited_timestamp", "title", "distinguished", "approved_by", "banned_by", "link_flair_text", "link_flair_css_class", "link_flair_background_color", "link_flair_id", "author_flair_text", "author_flair_css_class", "domain", "domain_raw", "suggested_sort", "upvote_ratio", "user_reports", "author_flair_richtext_t", "author_flair_richtext_u", "post_cross_link", "post_cross_origin", "post_award_string", "post_author_profile_pic", "post_fav_icon", "post_author_fullname", "permalink", "post_created_utc", "contest_mode", "locked", "stickied", "saved", "archived", "visited", "hidden", "mod", "over_18", "edited", "score_hidden", "new_comment", "spoiler", "author_cakeday", "is_original_content", "has_awards", "account_new", "send_replies", "childIds", "childCount", "collapsed", "comment_filtered", "commentMoreIds", "commentMoreCount", "url", "raw_url", "thumbnail", "preview_url", "preview_width", "preview_height", "preview_infographic", "preview_duration", "video_preview", "selftext_raw", "selftext_processed", "selftext_processed_preview", "embedded", "embedded_author", "embedded_author_handle", "body_raw", "body_processed", "internally_visited", "lists_time", "age", "lists_id"};

    public RedditDb(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
        Logger.verbose("Setting up DB");
    }

    public static String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists tbl_accounts(account_id integer primary key, account_name text, account_subs text, account_mods text, account_multis text, account_refresh_token text );");
        try {
            Cursor cursorRawQuery = sQLiteDatabase.rawQuery("SELECT * FROM tbl_accounts WHERE account_name='Logged out'", null);
            if (cursorRawQuery == null || cursorRawQuery.getCount() == 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("account_name", "Logged out");
                sQLiteDatabase.insertWithOnConflict("tbl_accounts", null, contentValues, 2);
            }
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
        } catch (Exception unused) {
        }
        sQLiteDatabase.execSQL("create table tbl_posts(_id text primary key, link_id text,parent_id text,hash integer,kind text,sync_type integer,likes integer, num_reports integer,num_comments integer,gilded integer, silvered integer, platinumed integer, score integer, level integer, controversiality integer, new_comment_count integer, upvote_ratio integer, view_count integer,award_count integer,subreddit text, subreddit_id text, author text, timestamp text,edited_timestamp text,title text, distinguished text,approved_by text,banned_by text,link_flair_text text, link_flair_css_class text, link_flair_background_color text, link_flair_id text, author_flair_text text,author_flair_css_class text,domain text,domain_raw text,suggested_sort text,user_reports text,author_flair_richtext_t text,author_flair_richtext_u text,post_cross_link text,post_cross_origin text,post_award_string text,post_author_profile_pic text,post_fav_icon text,post_author_fullname text,permalink text,post_created_utc text,contest_mode integer,locked integer,stickied integer,saved integer, archived integer,visited integer, hidden integer, mod integer,edited integer, over_18 integer, score_hidden integer, new_comment integer, spoiler integer, author_cakeday integer, is_original_content integer, has_awards integer, account_new integer, send_replies integer, childIds text, childCount integer, collapsed integer, comment_filtered integer, commentMoreIds text, commentMoreCount integer, url text, raw_url text, thumbnail text, preview_url text, preview_width integer, preview_height integer, preview_infographic integer, preview_duration integer, video_preview text, selftext_raw text, selftext_processed text, selftext_processed_preview text, embedded text, embedded_author text, embedded_author_handle text, body_raw text, body_processed text, age datetime);");
        sQLiteDatabase.execSQL("create table tbl_lists(lists_id text, lists_age datetime, lists_post_id text, lists_time integer,PRIMARY KEY (lists_id,lists_post_id));");
        sQLiteDatabase.execSQL("create table if not exists tbl_viewed_new(id text primary key, viewed_age datetime, internally_visited integer);");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int oldVersion, int newVersion) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_comments");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_posts");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_lists");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_viewed_new");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_accounts");
        onCreate(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int oldVersion, int newVersion) {
        Log.d(SyncSampleEntry.TYPE, "Upgrading DB");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_posts");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_lists");
        onCreate(sQLiteDatabase);
    }
}
