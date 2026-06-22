package com.resync.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: l8.b */
/* JADX INFO: compiled from: RedditMessagingDb.java */
/* JADX INFO: loaded from: classes.dex */
public class RedditMessagingDb extends SQLiteOpenHelper {
    public RedditMessagingDb(Context context) {
        super(context, "db_reddit_sync_messaging", (SQLiteDatabase.CursorFactory) null, 11);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table tbl_messages(messages_id text primary key, messages_age integer, messages_type text, messages_time text);");
        sQLiteDatabase.execSQL("create table tbl_mod_mail(mod_mail_id text primary key, mod_mail_age integer, mod_mail_type text, mod_mail_time text);");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int oldVersion, int newVersion) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_messages");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_mod_mail");
        onCreate(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int oldVersion, int newVersion) {
        Logger.verbose("Upgrading messaging db");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_messages");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_mod_mail");
        onCreate(sQLiteDatabase);
    }
}
