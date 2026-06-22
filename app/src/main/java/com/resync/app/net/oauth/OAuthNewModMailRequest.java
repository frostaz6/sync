package com.resync.app.net.oauth;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.db.RedditDb;
import com.resync.app.db.RedditMessagingDb;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.h0 */
/* JADX INFO: compiled from: OAuthNewModMailRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthNewModMailRequest extends RedditRequest<String> {
    public OAuthNewModMailRequest(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, 0, SettingsSingleton.getOauthUrl(context) + "message/moderator/unread.json?mark=false", listener, errorListener);
    }

    private void deleteOldModMail(SQLiteDatabase sQLiteDatabase) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(11, -392);
            sQLiteDatabase.execSQL("DELETE FROM tbl_mod_mail WHERE mod_mail_age <= '" + simpleDateFormat.format(calendar.getTime()) + "'");
        } catch (Exception unused) {
            Logger.debug("Error cleaning up mod mail.");
        }
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONObject(new String(networkResponse.data)).getJSONObject("data").getJSONArray("children");
            int length = jSONArray.length();
            ContentValues[] contentValuesArr = new ContentValues[length];
            for (int messageIndex = 0; messageIndex < jSONArray.length(); messageIndex++) {
                contentValuesArr[messageIndex] = new ContentValues();
                contentValuesArr[messageIndex].put("mod_mail_id", jSONArray.getJSONObject(messageIndex).getJSONObject("data").getString("id"));
                contentValuesArr[messageIndex].put("mod_mail_type", jSONArray.getJSONObject(messageIndex).getString("kind"));
                contentValuesArr[messageIndex].put("mod_mail_age", RedditDb.now());
            }
            RedditMessagingDb redditMessagingDb = new RedditMessagingDb(this.context);
            SQLiteDatabase readableDatabase = redditMessagingDb.getReadableDatabase();
            readableDatabase.beginTransaction();
            deleteOldModMail(readableDatabase);
            String str = null;
            for (int index = 0; index < length; index++) {
                try {
                    readableDatabase.insertWithOnConflict("tbl_mod_mail", null, contentValuesArr[index], 2);
                    str = (((index > 0 ? str + "<br/><br/>" : new String()) + "<b>" + jSONArray.getJSONObject(index).getJSONObject("data").getString("author").replace("_", "&#95;") + "</b>") + "<br/>") + jSONArray.getJSONObject(index).getJSONObject("data").getString("body");
                } catch (SQLiteConstraintException unused) {
                }
            }
            readableDatabase.setTransactionSuccessful();
            readableDatabase.endTransaction();
            readableDatabase.close();
            redditMessagingDb.close();
            return Response.success(str, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
