package com.resync.app.net.oauth;

import android.content.ContentValues;
import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.text.parsing.ContentValuesListHelper;
import com.resync.app.text.parsing.PostParserHelper;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.f */
/* JADX INFO: compiled from: OAuthCommentsMoreRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthCommentsMoreRequest extends RedditRequest<Void> {
    private final String listKey;

    private final String fullname;
    private final int position;
    private final int page;
    ArrayList<String> authorFullnames;

    class AuthorInfoResponseListener implements Response.Listener<Void> {

        final /* synthetic */ Void pendingResult;

        AuthorInfoResponseListener(Void result) {
            this.pendingResult = result;
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(Void result) {
            if (OAuthCommentsMoreRequest.this.responseListener != null) {
                OAuthCommentsMoreRequest.this.responseListener.onResponse(this.pendingResult);
            }
        }
    }

    class AuthorInfoErrorListener implements Response.ErrorListener {

        final /* synthetic */ Void pendingResult;

        AuthorInfoErrorListener(Void result) {
            this.pendingResult = result;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (OAuthCommentsMoreRequest.this.responseListener != null) {
                OAuthCommentsMoreRequest.this.responseListener.onResponse(this.pendingResult);
            }
        }
    }

    public OAuthCommentsMoreRequest(Context context, String str, String str2, String str3, String str4, int position, int page, String str5, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/morechildren?always_show_media=1&profile_img=true", listener, errorListener);
        this.authorFullnames = new ArrayList<>();
        this.listKey = str;
        this.fullname = str3;
        this.position = position;
        this.page = page;
        HashMap map = new HashMap();
        this.params = map;
        map.put("link_id", "t3_" + str2);
        this.params.put("children", str4);
        this.params.put("api_type", "json");
        if (StringUtils.isNotEmpty(str5)) {
            this.params.put("sort", str5.toLowerCase(Locale.ENGLISH));
        }
        Logger.verbose("SUGGESTED: " + str5);
        Logger.verbose("t3_" + str2);
        Logger.verbose(str4);
        Logger.verbose("Sort: " + str5);
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public void deliverResponse(Void result) {
        if (this.authorFullnames.size() > 0) {
            OAuthRequestManager.sendWithAppContext(new OAuthAuthorInfoRequest(this.context, this.authorFullnames, new AuthorInfoResponseListener(result), new AuthorInfoErrorListener(result)));
        } else {
            super.deliverResponse(result);
        }
    }

    public String getFullname() {
        return this.fullname;
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        int thingIndexCopy;
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            JSONArray jSONArray = new JSONObject(str).getJSONObject("json").getJSONObject("data").getJSONArray("things");
            int length = jSONArray.length();
            ArrayList<ContentValues> arrayList = new ArrayList();
            int thingIndex = 0;
            while (thingIndex < length) {
                if (jSONArray.getJSONObject(thingIndex).getString("kind").equals("t1")) {
                    thingIndexCopy = thingIndex;
                    PostParserHelper.parsePost(this.context, 1, jSONArray.getJSONObject(thingIndex).getJSONObject("data"), arrayList, this.page, true, true, null);
                } else {
                    thingIndexCopy = thingIndex;
                    if (jSONArray.getJSONObject(thingIndexCopy).getString("kind").equals("more")) {
                        PostParserHelper.parsePost(this.context, 9, jSONArray.getJSONObject(thingIndexCopy).getJSONObject("data"), arrayList, this.page, true, true, null);
                    }
                }
                thingIndex = thingIndexCopy + 1;
            }
            if (SettingsSingleton.getSettings().highlightNewAccounts) {
                for (ContentValues contentValues : arrayList) {
                    if (contentValues.containsKey("post_author_fullname")) {
                        String asString = contentValues.getAsString("post_author_fullname");
                        if (StringUtils.isNotEmpty(asString) && !this.authorFullnames.contains(asString)) {
                            Logger.verbose("Author: " + asString);
                            this.authorFullnames.add(asString);
                        }
                    }
                }
            }
            if (!isCanceled()) {
                ContentValues[] contentValuesArrA = ContentValuesListHelper.attachListMetadata(arrayList, this.listKey, this.position);
                this.context.getContentResolver().delete(RedditProvider.URI_DELETE_LIST_ITEM, this.fullname, null);
                this.context.getContentResolver().update(RedditProvider.URI_UPDATE_COMMENTS_LIST_TIME, null, null, new String[]{this.listKey, Integer.toString(this.position), Integer.toString(length - 1)});
                Logger.verbose("Inserted: " + this.context.getContentResolver().bulkInsert(RedditProvider.URI_INSERT_COMMENTS, contentValuesArrA));
                this.context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
            }
            return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
