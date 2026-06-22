package com.resync.app.net.thirdparty;

import android.content.ContentValues;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bumptech.glide.RedditHelper;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.util.Logger;
import com.resync.app.util.WebsafeUtils;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: r8.h */
/* JADX INFO: compiled from: RestoreCommentRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class RestoreCommentRequest extends Request<Void> {

    Post post;

    public RestoreCommentRequest(Post post) {
        super(0, buildUrl(post), null);
        this.post = post;
        StaticToast.show("Restoring comment");
        setShouldCache(false);
        Logger.verbose(getUrl());
    }

    private static String buildUrl(Post post) {
        Long.parseLong(post.getPostCreatedUtc());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("https://api.pushshift.io/reddit/comment/search/");
        sb2.append("?ids=" + post.getId());
        return sb2.toString();
    }

    @Override // com.android.volley.Request
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Void result) {
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONArray jSONArray = new JSONObject(new String(networkResponse.data)).getJSONArray("data");
            for (int commentIndex = 0; commentIndex < jSONArray.length(); commentIndex++) {
                JSONObject jSONObject = jSONArray.getJSONObject(commentIndex);
                String string = jSONObject.getString("id");
                if (StringUtils.equals(string, this.post.getId())) {
                    Logger.verbose("VALUE: " + jSONObject);
                    String string2 = jSONObject.getString("author");
                    String bodyRaw = WebsafeUtils.unescapeHtml(jSONObject.getString("body"));
                    String bodyProcessed = RedditHelper.processMarkdownToHtml(null, bodyRaw);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("author", string2);
                    contentValues.put("body_raw", bodyRaw);
                    contentValues.put("body_processed", bodyProcessed);
                    Logger.verbose("VALUE: " + this.post.getId() + StringUtils.SPACE + string + StringUtils.SPACE + string2);
                    RedditApplication.getAppContext().getContentResolver().update(RedditProvider.URI_RESTORE_REMOVED, contentValues, string, null);
                    RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
                }
            }
            return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
