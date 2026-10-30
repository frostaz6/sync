package com.resync.app.net.oauth;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import com.resync.app.text.parsing.CommentParserHelper;
import com.resync.app.text.parsing.ContentValuesListHelper;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.DatabaseIdHelper;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.g */
/* JADX INFO: compiled from: OAuthCommentsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthCommentsRequest extends RedditRequest<Void> {
    private final String articleId;
    private final String commentId;
    private final boolean clearList;
    private final boolean keepNewCommentFlag;
    ArrayList<String> authorFullnames;

    /* JADX INFO: renamed from: y8.g$a */
    /* JADX INFO: compiled from: OAuthCommentsRequest.java */
    class AuthorInfoResponseListener implements Response.Listener<Void> {

        final /* synthetic */ Void result;

        AuthorInfoResponseListener(Void incomingResult) {
            this.result = incomingResult;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Void incomingResult) {
            if (OAuthCommentsRequest.this.responseListener != null) {
                OAuthCommentsRequest.this.responseListener.onResponse(this.result);
            }
        }
    }

    /* JADX INFO: renamed from: y8.g$b */
    /* JADX INFO: compiled from: OAuthCommentsRequest.java */
    class AuthorInfoErrorListener implements Response.ErrorListener {

        final /* synthetic */ Void result;

        AuthorInfoErrorListener(Void incomingResult) {
            this.result = incomingResult;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (OAuthCommentsRequest.this.responseListener != null) {
                OAuthCommentsRequest.this.responseListener.onResponse(this.result);
            }
        }
    }

    public OAuthCommentsRequest(Context context, String str, String str2, String str3, String str4, boolean clearList, boolean keepNewCommentFlag, boolean rawJson, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, str, str2, str3, str4, rawJson), listener, errorListener);
        this.authorFullnames = new ArrayList<>();
        this.articleId = str;
        this.commentId = str3;
        this.clearList = clearList;
        this.keepNewCommentFlag = keepNewCommentFlag;
        Log.i("ReSyncComments", "Thread GET " + getUrl());
    }

    public static String buildUrl(Context context, String str, String str2, String str3, String str4, boolean rawJson) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(SettingsSingleton.getOauthUrl(context));
        // October 2026: Reddit's edge rejects the literal /comments/ path
        // with HTTP 403 even for otherwise valid OAuth sessions. Redlib
        // verified that encoding ONE path separator still returns the usual
        // two-listing [post, comments] JSON, which our existing parser reads.
        //
        // Preserve the subreddit and the rest of the query string so sorting,
        // direct comment links, and normal thread pagination are unchanged.
        if (!TextUtils.isEmpty(str2)) {
            stringBuffer.append("r/");
            stringBuffer.append(str2);
            stringBuffer.append("%2Fcomments/");
        } else {
            // Scoped post links can reach this request with no subreddit.
            // Encoding the slash after 'comments' avoids /comments/<id>.
            stringBuffer.append("comments%2F");
        }
        stringBuffer.append(str);
        if (str3 != null) {
            stringBuffer.append("/_/");
            stringBuffer.append(str3);
        }
        stringBuffer.append(".json");
        stringBuffer.append("?always_show_media=1&profile_img=true");
        if (str3 != null) {
            if (!rawJson) {
                stringBuffer.append("&context=100");
            }
        } else if (str4 != null) {
            if (!"suggested".equalsIgnoreCase(str4)) {
                stringBuffer.append("&sort=");
                stringBuffer.append(str4.toLowerCase(Locale.ENGLISH).replaceAll("\\&", ""));
            }
            int commentsCount = SettingsSingleton.getSettings().commentsCount;
            stringBuffer.append("&limit=");
            if (commentsCount == 0) {
                stringBuffer.append("500");
            } else {
                stringBuffer.append(commentsCount);
            }
        }
        return stringBuffer.toString();
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    /* JADX INFO: renamed from: g, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Void result) {
        if (this.authorFullnames.size() > 0) {
            OAuthRequestManager.sendWithAppContext(new OAuthAuthorInfoRequest(this.context, this.authorFullnames, new AuthorInfoResponseListener(result), new AuthorInfoErrorListener(result)));
        } else {
            super.deliverResponse(result);
        }
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        ArrayList topLevelIndices;
        int one;
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            ArrayList<ContentValues> arrayListA = CommentParserHelper.parse(this.context, new String(networkResponse.data), 0);
            if (arrayListA.size() > 0) {
                Logger.verbose("Expected comment count: " + arrayListA.get(0).getAsInteger("num_comments").intValue());
            }
            long jCurrentTimeMillis2 = System.currentTimeMillis();
            if (!this.keepNewCommentFlag) {
                for (ContentValues contentValues : arrayListA) {
                    if (contentValues.containsKey("new_comment")) {
                        contentValues.remove("new_comment");
                    }
                }
            }
            int one2 = 1;
            if (arrayListA.size() > 1 && SettingsSingleton.getSettings().commentsCollapseAllDefault && TextUtils.isEmpty(this.commentId)) {
                ArrayList topLevelIndices2 = new ArrayList();
                for (int commentIndex = 1; commentIndex < arrayListA.size(); commentIndex++) {
                    if (arrayListA.get(commentIndex).getAsInteger("level").intValue() == 0) {
                        topLevelIndices2.add(Integer.valueOf(commentIndex));
                    }
                }
                int headerIndex = 0;
                while (headerIndex < topLevelIndices2.size()) {
                    int iIntValue = ((Integer) topLevelIndices2.get(headerIndex)).intValue();
                    int size = arrayListA.size();
                    if (headerIndex < topLevelIndices2.size() - one2) {
                        size = ((Integer) topLevelIndices2.get(headerIndex + 1)).intValue();
                    }
                    if (iIntValue != size) {
                        int descendantIndex = iIntValue + 1;
                        ArrayList arrayList3 = new ArrayList();
                        while (descendantIndex < size) {
                            arrayList3.add(arrayListA.get(descendantIndex).getAsString("_id"));
                            arrayListA.get(descendantIndex).put("collapsed", (Integer) 1);
                            descendantIndex++;
                            one2 = 1;
                            topLevelIndices2 = topLevelIndices2;
                        }
                        topLevelIndices = topLevelIndices2;
                        one = one2;
                        if (arrayList3.size() > 0) {
                            String strJoin = TextUtils.join(",", arrayList3);
                            arrayListA.get(iIntValue).put("childCount", Integer.valueOf(arrayList3.size()));
                            arrayListA.get(iIntValue).put("childIds", strJoin);
                        }
                    } else {
                        topLevelIndices = topLevelIndices2;
                        one = one2;
                    }
                    headerIndex++;
                    one2 = one;
                    topLevelIndices2 = topLevelIndices;
                }
            }
            ContentValues[] contentValuesArrA = ContentValuesListHelper.attachListMetadata(arrayListA, DatabaseIdHelper.buildId(this.articleId, this.commentId), 0);
            if (SettingsSingleton.getSettings().highlightNewAccounts) {
                for (ContentValues contentValues2 : contentValuesArrA) {
                    if (contentValues2.containsKey("post_author_fullname")) {
                        String asString = contentValues2.getAsString("post_author_fullname");
                        if (StringUtils.isNotEmpty(asString) && !this.authorFullnames.contains(asString)) {
                            Logger.verbose("Author: " + asString);
                            this.authorFullnames.add(asString);
                        }
                    }
                }
            }
            long jCurrentTimeMillis3 = System.currentTimeMillis();
            if (!isCanceled()) {
                if (this.clearList) {
                    this.context.getContentResolver().update(RedditProvider.URI_CLEAR, null, DatabaseIdHelper.buildId(this.articleId, this.commentId), null);
                }
                ContentResolver contentResolver = this.context.getContentResolver();
                Uri uri = RedditProvider.URI_MULTIPLE_POSTS;
                Logger.verbose("Inserted: " + contentResolver.bulkInsert(uri, contentValuesArrA));
                this.context.getContentResolver().notifyChange(uri, null);
            }
            long jCurrentTimeMillis4 = System.currentTimeMillis();
            Logger.verbose("Network time: " + networkResponse.networkTimeMs + "\nParsing time: " + (jCurrentTimeMillis2 - jCurrentTimeMillis) + "\nInsertion time: " + (jCurrentTimeMillis4 - jCurrentTimeMillis3) + "\nTotal time: " + (jCurrentTimeMillis4 - jCurrentTimeMillis));
            return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
