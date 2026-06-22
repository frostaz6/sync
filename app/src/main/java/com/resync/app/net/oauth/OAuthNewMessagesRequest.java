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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.db.RedditDb;
import com.resync.app.db.RedditMessagingDb;
import com.resync.app.model.Kinds;
import com.resync.app.model.Message;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;
import com.resync.app.net.RedditSubmitRetryPolicy;

/* JADX INFO: renamed from: y8.g0 */
/* JADX INFO: compiled from: OAuthNewMessagesRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthNewMessagesRequest extends RedditRequest<Message[]> {
    public OAuthNewMessagesRequest(Context context, Response.Listener<Message[]> listener, Response.ErrorListener errorListener) {
        super(context, 0, SettingsSingleton.getOauthUrl(context) + "message/unread.json?mark=false", listener, errorListener);
        setRetryPolicy(new RedditSubmitRetryPolicy());
    }

    private void deleteOldMessages(SQLiteDatabase sQLiteDatabase) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(11, -392);
            sQLiteDatabase.execSQL("DELETE FROM tbl_messages WHERE messages_age <= '" + simpleDateFormat.format(calendar.getTime()) + "'");
        } catch (Exception unused) {
            Logger.debug("Error cleaning up messages.");
        }
    }

    @Override // com.android.volley.Request
    public Response<Message[]> parseNetworkResponse(NetworkResponse networkResponse) {
        String tagCarry;
        int lengthCarry;
        String tag = "OAuthNewMessagesRequest";
        try {
            JSONArray jSONArray = new JSONObject(new String(networkResponse.data)).getJSONObject("data").getJSONArray("children");
            int length = jSONArray.length();
            ContentValues[] contentValuesArr = new ContentValues[length];
            Logger.verbose("OAuthNewMessagesRequest", "Found new messages: " + jSONArray.length());
            for (int messageIndex = 0; messageIndex < jSONArray.length(); messageIndex++) {
                contentValuesArr[messageIndex] = new ContentValues();
                contentValuesArr[messageIndex].put("messages_id", jSONArray.getJSONObject(messageIndex).getJSONObject("data").getString("id"));
                contentValuesArr[messageIndex].put("messages_type", jSONArray.getJSONObject(messageIndex).getString("kind"));
                contentValuesArr[messageIndex].put("messages_age", RedditDb.now());
            }
            RedditMessagingDb redditMessagingDb = new RedditMessagingDb(this.context);
            SQLiteDatabase readableDatabase = redditMessagingDb.getReadableDatabase();
            readableDatabase.beginTransaction();
            deleteOldMessages(readableDatabase);
            ArrayList arrayList = new ArrayList();
            int index = 0;
            while (index < length) {
                try {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    int lengthCopy = length;
                    long createdMillis = jSONArray.getJSONObject(index).getJSONObject("data").getLong("created") * 1000;
                    if (jCurrentTimeMillis - createdMillis > 259200000) {
                        lengthCarry = lengthCopy;
                        tagCarry = tag;
                    } else {
                        try {
                            Logger.verbose(tag, "Attempting to insert message: " + jSONArray.getJSONObject(index).getJSONObject("data").getString("id"));
                            lengthCarry = lengthCopy;
                            tagCarry = tag;
                            try {
                                readableDatabase.insertWithOnConflict("tbl_messages", null, contentValuesArr[index], 2);
                                Message message = new Message();
                                message.id = jSONArray.getJSONObject(index).getJSONObject("data").getString("id");
                                message.author = jSONArray.getJSONObject(index).getJSONObject("data").getString("author");
                                message.bodyProcessed = jSONArray.getJSONObject(index).getJSONObject("data").getString("body");
                                message.bodyRaw = jSONArray.getJSONObject(index).getJSONObject("data").getString("body");
                                message.notificationId = createdMillis;
                                message.kind = Kinds.fromPrefix(jSONArray.getJSONObject(index).getString("kind"));
                                arrayList.add(message);
                            } catch (SQLiteConstraintException unused) {
                            }
                        } catch (SQLiteConstraintException unused2) {
                            lengthCarry = lengthCopy;
                            tagCarry = tag;
                        }
                    }
                } catch (SQLiteConstraintException unused3) {
                    tagCarry = tag;
                    lengthCarry = length;
                }
                index++;
                length = lengthCarry;
                tag = tagCarry;
            }
            readableDatabase.setTransactionSuccessful();
            readableDatabase.endTransaction();
            readableDatabase.close();
            redditMessagingDb.close();
            Message[] messageArr = new Message[arrayList.size()];
            Collections.reverse(arrayList);
            arrayList.toArray(messageArr);
            return Response.success(messageArr, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
