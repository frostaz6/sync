package com.resync.app.net.oauth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.resync.app.R;
import com.resync.app.provider.RedditProvider;
import com.resync.app.receiver.SubmitCommentBroadcastReceiver;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.text.parsing.ContentValuesListHelper;
import com.resync.app.text.parsing.PostParserHelper;
import com.resync.app.managers.DraftManager;
import com.resync.app.helpers.IconHelper;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.net.RedditRequest;
import com.resync.app.net.RedditSubmitRetryPolicy;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: y8.e */
/* JADX INFO: compiled from: OAuthCommentRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthCommentRequest extends RedditRequest<Void> {
    private final String cursorId;
    private final int type;
    private final Post parentPost;
    private final Post commentPost;
    private final String inputText;
    private final boolean refreshToView;
    private final String refreshToken;

    public OAuthCommentRequest(Context context, String str, Post post, Post post2, String str2, int type, boolean z, String str3) {
        super(context, 1, buildUrl(context, type), null, null);
        this.cursorId = str;
        this.type = type;
        this.refreshToView = z;
        this.parentPost = post;
        this.commentPost = post2;
        this.inputText = str2;
        this.refreshToken = str3;
        HashMap map = new HashMap();
        this.params = map;
        if (type == 0) {
            map.put("thing_id", "t3_" + post.getId());
            DraftManager.saveDraft(context, str2);
        } else if (type == 1) {
            map.put("thing_id", "t1_" + post2.getId());
            DraftManager.saveDraft(context, str2);
        } else if (type == 2) {
            map.put("thing_id", "t4_" + post2.getId());
        } else if (type == 3) {
            map.put("thing_id", "t1_" + post2.getId());
        }
        this.params.put("text", str2);
        this.params.put("api_type", "json");
        for (String str4 : this.params.keySet()) {
            Logger.verbose(str4 + ": " + this.params.get(str4));
        }
        setRetryPolicy(new RedditSubmitRetryPolicy());
        StaticToast.showWithContext("Submitting", this.context);
    }

    private static String buildUrl(Context context, int type) {
        if (type == 3) {
            return SettingsSingleton.getOauthUrl(context) + "api/editusertext";
        }
        return SettingsSingleton.getOauthUrl(context) + "api/comment";
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Void result) {
        if (this.type == 3) {
            if (this.refreshToView) {
                StaticToast.showWithContext("Edited. Refresh to view.", this.context);
            } else {
                StaticToast.showWithContext("Edited", this.context);
            }
        } else if (this.refreshToView) {
            StaticToast.showWithContext("Submitted. Refresh to view.", this.context);
        } else {
            StaticToast.showWithContext("Submitted", this.context);
        }
        Response.Listener<Void> listener = this.responseListener;
        if (listener != null) {
            listener.onResponse(null);
        }
    }

    public void showErrorNotification(String str, boolean z) {
        int type = this.type;
        String str2 = "Error submitting";
        if (type == 0 || type == 1) {
            str2 = "Error submitting comment";
        } else if (type == 2) {
            str2 = "Error submitting message";
        } else if (type == 3) {
            str2 = "Error editing comment";
        }
        int iCurrentTimeMillis = (int) System.currentTimeMillis();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context);
        builder.setSmallIcon(IconHelper.getSystemIconRes());
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(str2);
        builder.setTicker(str2);
        builder.setContentText(str);
        builder.setChannelId(NotificationHelper.getErrorChannel());
        if (z) {
            Intent intent = new Intent(this.context, (Class<?>) SubmitCommentBroadcastReceiver.class);
            intent.putExtra("cursorId", this.cursorId);
            intent.putExtra("parentPost", this.parentPost);
            intent.putExtra("commentPost", this.commentPost);
            intent.putExtra("inputText", this.inputText);
            intent.putExtra("type", this.type);
            intent.putExtra("retry", true);
            intent.putExtra("refresh_token", this.refreshToken);
            intent.putExtra("request_id", iCurrentTimeMillis);
            builder.addAction(R.drawable.baseline_repeat_24, "Retry", PendingIntent.getBroadcast(this.context, iCurrentTimeMillis, intent, 335544320));
        }
        Notification notificationC = builder.build();
        int flags = notificationC.flags | 16;
        notificationC.defaults |= 1;
        notificationC.ledARGB = -39424;
        notificationC.ledOnMS = 250;
        notificationC.ledOffMS = 1000;
        notificationC.flags = flags | 1;
        ((NotificationManager) this.context.getSystemService("notification")).notify(iCurrentTimeMillis, notificationC);
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public VolleyError parseNetworkError(VolleyError volleyError) {
        super.parseNetworkError(volleyError);
        showErrorNotification("Could not connect to Reddit", true);
        return super.parseNetworkError(volleyError);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r2v7, types: [android.database.ContentObserver, java.lang.String[]] */
    /* JADX WARN: Type inference failed for: r3v6, types: [android.content.ContentResolver] */
    /* JADX WARN: Type inference failed for: r3v8, types: [android.content.ContentResolver] */
    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        String str;
        Object obj;
        Object obj2;
        Object nullResult;
        String str2 = new String(networkResponse.data);
        if (!StringUtils.contains(str2, "\"errors\": []")) {
            Logger.debug("ERROR: " + str2);
            StringBuilder sb2 = new StringBuilder();
            try {
                JSONArray jSONArray = new JSONObject(str2).getJSONObject("json").getJSONArray("errors");
                for (int errorIndex = 0; errorIndex < jSONArray.length(); errorIndex++) {
                    Logger.debug("Error: " + jSONArray.get(errorIndex));
                    sb2.append(jSONArray.getJSONArray(0).get(1));
                    if (errorIndex < jSONArray.length() - 1) {
                        sb2.append(", ");
                    }
                }
                Logger.verbose("TEMP: " + str2);
            } catch (Exception e) {
                Logger.logException(e);
            }
            if (sb2.length() == 0) {
                sb2.append("No error returned");
            }
            showErrorNotification(sb2.toString(), true);
            return Response.error(new ParseError(new Exception()));
        }
        try {
            JSONObject jSONObject = new JSONObject(new String(networkResponse.data)).getJSONObject("json").getJSONObject("data").getJSONArray("things").getJSONObject(0).getJSONObject("data");
            if (this.type != 0 || this.parentPost == null || TextUtils.isEmpty(this.cursorId)) {
                str = "new_comment";
                obj = null;
            } else {
                ArrayList arrayList = new ArrayList();
                str = "new_comment";
                PostParserHelper.parsePostDefaults(this.context, 1, jSONObject, arrayList, true, null);
                if (arrayList.size() > 0) {
                    ((ContentValues) arrayList.get(0)).remove(str);
                    ((ContentValues) arrayList.get(0)).put(str, (Integer) 0);
                }
                ContentValues[] contentValuesArrA = ContentValuesListHelper.attachListMetadata(arrayList, this.cursorId, 1);
                obj = null;
                this.context.getContentResolver().update(RedditProvider.URI_UPDATE_COMMENTS_LIST_TIME, null, null, new String[]{this.cursorId, "0", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE});
                ContentResolver contentResolver = this.context.getContentResolver();
                Uri uri = RedditProvider.URI_MULTIPLE_POSTS;
                Logger.verbose("Inserted: " + contentResolver.bulkInsert(uri, contentValuesArrA));
                this.context.getContentResolver().notifyChange(uri, null);
            }
            if (this.type != 1 || this.parentPost == null || TextUtils.isEmpty(this.cursorId)) {
                obj2 = obj;
            } else {
                Logger.verbose("Inserting reply...");
                Logger.verbose("Parent time: " + this.parentPost.getListsTime());
                ArrayList arrayList2 = new ArrayList();
                PostParserHelper.parsePost(this.context, 1, jSONObject, arrayList2, this.commentPost.getLevel() + 1, false, true, null);
                if (arrayList2.size() > 0) {
                    ((ContentValues) arrayList2.get(0)).remove(str);
                    ((ContentValues) arrayList2.get(0)).put(str, (Integer) 0);
                }
                ContentValues[] contentValuesArrA2 = ContentValuesListHelper.attachListMetadata(arrayList2, this.cursorId, this.commentPost.getListsTime());
                obj2 = null;
                this.context.getContentResolver().update(RedditProvider.URI_UPDATE_COMMENTS_LIST_TIME, null, null, new String[]{this.cursorId, Integer.toString(this.commentPost.getListsTime()), IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE});
                ContentResolver contentResolver2 = this.context.getContentResolver();
                Uri uri2 = RedditProvider.URI_MULTIPLE_POSTS;
                Logger.verbose("Inserted: " + contentResolver2.bulkInsert(uri2, contentValuesArrA2));
                this.context.getContentResolver().notifyChange(uri2, null);
            }
            if (this.type == 3) {
                Logger.verbose("Editing comment");
                ArrayList arrayList3 = new ArrayList();
                Object nullObserver = obj2;
                PostParserHelper.parsePost(this.context, 1, jSONObject, arrayList3, this.commentPost.getLevel(), false, true, null);
                if (arrayList3.size() > 0) {
                    ((ContentValues) arrayList3.get(0)).remove(str);
                    ((ContentValues) arrayList3.get(0)).put(str, (Integer) 0);
                }
                this.context.getContentResolver().update(RedditProvider.URI_BROAD_COMMENT_UPDATE, (ContentValues) arrayList3.get(0), this.commentPost.getId(), (String[]) nullObserver);
                this.context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, (android.database.ContentObserver) nullObserver);
                nullResult = nullObserver;
            } else {
                nullResult = obj2;
            }
            DraftManager.deleteDraft(this.inputText);
            return Response.success((Void) nullResult, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            int type = this.type;
            if (type == 0 || type == 1) {
                showErrorNotification("Error parsing comment", true);
            } else if (type == 2) {
                showErrorNotification("Error parsing message", true);
            } else if (type == 3) {
                showErrorNotification("Error parsing comment", true);
            }
            return Response.error(new ParseError(e));
        }
    }
}
