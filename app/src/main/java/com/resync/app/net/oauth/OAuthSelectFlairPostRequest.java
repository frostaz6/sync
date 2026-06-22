package com.resync.app.net.oauth;

import android.content.ContentValues;
import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import com.resync.app.model.AbstractFlair;
import com.resync.app.model.NoFlair;
import com.resync.app.model.RichTextFlair;
import com.resync.app.model.TextFlair;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: y8.r0 */
/* JADX INFO: compiled from: OAuthSelectFlairPostRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthSelectFlairPostRequest extends RedditRequest<Void> {
    public OAuthSelectFlairPostRequest(Context context, Post post, AbstractFlair abstractFlair) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "r/" + post.getSubreddit() + "/api/selectflair", null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("link", "t3_" + post.getId());
        this.params.put("name", post.getAuthor());
        boolean isNoFlair = abstractFlair instanceof NoFlair;
        if (!isNoFlair) {
            this.params.put("flair_template_id", abstractFlair.id);
            if (abstractFlair instanceof RichTextFlair) {
                this.params.put("text", ((RichTextFlair) abstractFlair).getDisplayString());
            } else if (abstractFlair instanceof TextFlair) {
                this.params.put("text", ((TextFlair) abstractFlair).getDisplayText().toString());
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", post.getId());
        if (abstractFlair instanceof RichTextFlair) {
            contentValues.put("link_flair_text", ((RichTextFlair) abstractFlair).getRawString());
            contentValues.put("link_flair_background_color", abstractFlair.getCssClass());
            contentValues.put("link_flair_id", abstractFlair.id);
        } else if (abstractFlair instanceof TextFlair) {
            contentValues.put("link_flair_text", ((TextFlair) abstractFlair).getDisplayText().toString());
            contentValues.put("link_flair_background_color", abstractFlair.getCssClass());
            contentValues.put("link_flair_id", abstractFlair.id);
        } else if (isNoFlair) {
            contentValues.put("link_flair_text", "");
            contentValues.put("link_flair_background_color", "");
            contentValues.put("link_flair_id", "");
        }
        context.getContentResolver().update(RedditProvider.URI_UPDATE_POST_FLAIR, contentValues, null, null);
        context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            Logger.verbose(new String(networkResponse.data));
            return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
