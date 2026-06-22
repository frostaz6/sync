package com.resync.app.net.oauth;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.text.parsing.ContentValuesListHelper;
import com.resync.app.text.parsing.PostParserHelper;
import com.resync.app.text.parsing.SubParserHelper;
import com.resync.app.managers.TopSubredditHelper;
import com.resync.app.db.RedditDb;
import com.resync.app.model.Kinds;
import com.resync.app.model.PostsInfo;
import com.resync.app.helpers.PostInfoHelper;
import com.resync.app.helpers.VisitedManager;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.k0 */
/* JADX INFO: compiled from: OAuthPostsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthPostsRequest extends RedditRequest<PostsInfo> {
    private final String subreddit;

    private final PostsInfo postsInfo;
    private final boolean clearFirst;

    public OAuthPostsRequest(Context context, Object obj, String str, String str2, String str3, PostsInfo postsInfo, boolean clearFirst, boolean saveVisited, Response.Listener<PostsInfo> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, str, str, postsInfo, str3, str2), listener, errorListener);
        this.postsInfo = postsInfo;
        this.subreddit = str.toLowerCase(Locale.ENGLISH);
        this.clearFirst = clearFirst;
        if (saveVisited) {
            VisitedManager.saveVisited(str);
            if (postsInfo == null || StringUtils.isEmpty(postsInfo.after)) {
                TopSubredditHelper.saveSubVisit(context, str);
            }
        }
        setTag(obj);
        postsInfo.insertCount = 0;
        postsInfo.stagnant = false;
    }

    public static String buildUrl(Context context, String str, String str2, PostsInfo postsInfo, String str3, String str4) {
        String originalSub = str;
        Logger.verbose("Sub: " + originalSub);
        if (MaterialPostsHelper.isDomain(str)) {
            String domain = MaterialPostsHelper.extractDomain(str);
            StringBuffer sb = new StringBuffer();
            sb.append(SettingsSingleton.getOauthUrl(context));
            sb.append("domain/");
            sb.append(domain);
            sb.append(".json");
            if (postsInfo != null && !TextUtils.isEmpty(postsInfo.after)) {
                sb.append("?after=");
                sb.append(postsInfo.after);
                sb.append("&limit=25");
            }
            return sb.toString();
        } else if (MaterialPostsHelper.isSubSearch(str)) {
            String query = MaterialPostsHelper.extractSubSearchQuery(str);
            StringBuffer sb = new StringBuffer();
            sb.append(SettingsSingleton.getOauthUrl(context));
            sb.append("subreddits/search.json?q=");
            sb.append(query.trim());
            if (postsInfo != null && !TextUtils.isEmpty(postsInfo.after)) {
                sb.append("&after=");
                sb.append(postsInfo.after);
                sb.append("&limit=25");
            }
            if (SettingsSingleton.getSettings().nsfw) {
                sb.append("&include_over_18=on");
            }
            return sb.toString();
        } else if (MaterialPostsHelper.isSeen(str)) {
            String submitUrl = MaterialPostsHelper.extractSeenSubreddit(str);
            StringBuffer sb = new StringBuffer();
            sb.append("https://ssl.reddit.com/");
            sb.append("submit.json?url=");
            sb.append(submitUrl.trim());
            if (postsInfo != null && !TextUtils.isEmpty(postsInfo.after)) {
                sb.append("&after=");
                sb.append(postsInfo.after);
                sb.append("&limit=25");
            }
            return sb.toString();
        } else if (originalSub.startsWith("r/mod/")) {
            String modPath = originalSub;
            if (originalSub.equalsIgnoreCase("r/mod/about/comments")) {
                modPath = "r/mod/comments";
            }
            StringBuffer sb = new StringBuffer();
            sb.append(SettingsSingleton.getOauthUrl(context));
            sb.append(modPath.toLowerCase(Locale.ENGLISH));
            if (postsInfo != null && !TextUtils.isEmpty(postsInfo.after)) {
                sb.append("?after=");
                sb.append(postsInfo.after);
                sb.append("&limit=25");
            }
            return sb.toString();
        } else {
            String access;
            String sort;
            if ("sort=Rising".equalsIgnoreCase(str4)) {
                sort = null;
                access = "rising";
            } else {
                access = str3;
                sort = str4;
            }

            String sub = originalSub;
            if (originalSub.equalsIgnoreCase("Frontpage")) {
                sub = "frontpage";
            }

            sub = sub.replaceAll("redditsync_casual_", "");
            if (sub.equals("frontpage")) {
                sub = "";
            } else {
                sub = sub.toLowerCase(Locale.ENGLISH);
            }

            StringBuffer sb = new StringBuffer();
            sb.append(SettingsSingleton.getOauthUrl(context));
            if (sub.startsWith("multi_")) {
                if (sub.contains("/")) {
                    String multi = sub.replace("multi_", "");
                    sb.append("user/");
                    sb.append(multi.split("/")[0]);
                    sb.append("/m/");
                    sb.append(multi.split("/")[2]);
                    sb.append("/");
                } else {
                    sb.append("me/m/");
                    sb.append(sub.replaceAll("multi_", ""));
                    sb.append("/");
                }
            } else if (MaterialPostsHelper.isUserProfile(sub)) {
                String multiName = MaterialPostsHelper.extractProfileSection(sub);
                String user = MaterialPostsHelper.extractProfileUsername(sub);
                sb.append("user/");
                sb.append(user);
                sb.append("/");
                sb.append(multiName);
            } else if (MaterialPostsHelper.isSearch(sub)) {
                String searchSub = MaterialPostsHelper.extractSearchSubreddit(str2);
                String searchTerm = StringUtils.replaceAll(MaterialPostsHelper.extractSearchQuery(str2), "#", "%23");
                if (searchSub.startsWith("multi_")) {
                    sb.append("me/m/");
                    sb.append(searchSub.replaceAll("multi_", ""));
                    sb.append("/");
                } else if (searchSub.length() > 0) {
                    sb.append("r/");
                    sb.append(searchSub);
                    sb.append("/");
                }

                sb.append("search.json?q=");
                sb.append(searchTerm.replaceAll(" ", "+").replaceAll("&", "%26"));
                if (searchSub.length() > 0) {
                    sb.append("&restrict_sr=on");
                }

                if (access != null) {
                    sb.append("&sort=");
                    sb.append(access.toLowerCase(Locale.ENGLISH));
                }

                if (sort != null) {
                    sb.append("&");
                    sb.append(sort.toLowerCase(Locale.ENGLISH));
                }

                if (SettingsSingleton.getSettings().nsfw) {
                    sb.append("&include_over_18=on");
                }
            } else if (sub.length() > 0) {
                sb.append("r/");
                sb.append(sub);
                sb.append("/");
            }

            if (!MaterialPostsHelper.isSearch(sub)) {
                Logger.verbose("ACCESS: " + access + " - SORT: " + sort);
                if (!MaterialPostsHelper.isUserProfile(sub)) {
                    sb.append(access.toLowerCase(Locale.ENGLISH));
                }

                sb.append(".json");
                if (MaterialPostsHelper.isUserProfile(sub)) {
                    sb.append("?sort=");
                    Locale locale = Locale.ENGLISH;
                    sb.append(access.toLowerCase(locale));
                    if (StringUtils.isNotEmpty(sort)) {
                        sb.append("&");
                        sb.append(sort.toLowerCase(locale));
                    }
                } else if (StringUtils.isNotEmpty(sort)) {
                    sb.append("?sort=");
                    Locale locale = Locale.ENGLISH;
                    sb.append(access.toLowerCase(locale));
                    sb.append("&");
                    sb.append(sort.toLowerCase(locale));
                }
            }

            if (postsInfo != null && !TextUtils.isEmpty(postsInfo.after)) {
                if (sb.toString().contains("?")) {
                    sb.append("&");
                } else {
                    sb.append("?");
                }
                sb.append("after=");
                sb.append(postsInfo.after);
                sb.append("&limit=25");
            }

            if (PostInfoHelper.isSpecialSubreddit(str2)) {
                if (sb.toString().contains("?")) {
                    sb.append("&");
                } else {
                    sb.append("?");
                }
                sb.append("sr_detail=true&always_show_media=1");
            }

            if (sb.toString().contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            sb.append("always_show_media=1");
            return sb.toString();
        }
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (getUrl().startsWith("https://ssl.reddit.com/submit.json?")) {
            headers.remove("Authorization");
        }
        return headers;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v2, types: [android.content.ContentValues, java.lang.String[]] */
    @Override // com.android.volley.Request
    public Response<PostsInfo> parseNetworkResponse(NetworkResponse networkResponse) {
        ContentValues dividerValues;
        int childIndexCopy;
        Object nullCarry;
        try {
            String str = new String(networkResponse.data);
            JSONObject jSONObject = (getUrl().contains("submit.json?url=") && str.startsWith("[")) ? new JSONArray(str).getJSONObject(0) : new JSONObject(str);
            JSONArray jSONArray = jSONObject.getJSONObject("data").getJSONArray("children");
            Logger.verbose("Raw post count: " + jSONArray.length());
            ArrayList arrayList = new ArrayList();
            Object obj2 = null;
            if (this.postsInfo.page <= 0 || !SettingsSingleton.getSettings().postDividers) {
                dividerValues = null;
            } else {
                ContentValues divider = new ContentValues();
                divider.put("_id", Long.toString(System.currentTimeMillis()));
                divider.put("kind", (Integer) 3);
                divider.put("sync_type", (Integer) 17);
                divider.put("age", RedditDb.now());
                divider.put("over_18", (Integer) 0);
                divider.put("locked", (Integer) 0);
                divider.put("hidden", (Integer) 0);
                divider.put("level", Integer.valueOf(this.postsInfo.page));
                divider.put("likes", Long.valueOf(networkResponse.networkTimeMs));
                arrayList.add(divider);
                dividerValues = divider;
            }
            int childIndex = 0;
            while (childIndex < jSONArray.length()) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(childIndex).getJSONObject("data");
                int kind = Kinds.fromPrefix(jSONArray.getJSONObject(childIndex).getString("kind"));
                if (kind == 3 || kind == 1) {
                    try {
                        childIndexCopy = childIndex;
                        nullCarry = obj2;
                        try {
                            PostParserHelper.parsePostDefaults(this.context, kind, jSONObject2, arrayList, false, this.subreddit);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception outerException) {
                        outerException.printStackTrace();
                        childIndexCopy = childIndex;
                        nullCarry = obj2;
                    }
                } else {
                    if (kind != 5) {
                        throw new RuntimeException("This post type isn't supported!");
                    }
                    SubParserHelper.parseSubreddit(this.context, kind, jSONObject2, arrayList);
                    childIndexCopy = childIndex;
                    nullCarry = obj2;
                }
                childIndex = childIndexCopy + 1;
                obj2 = nullCarry;
            }
            Object nullObj = obj2;
            ContentValues[] contentValuesArrA = ContentValuesListHelper.attachListMetadata(arrayList, this.subreddit, this.postsInfo.count + 1);
            if (!isCanceled()) {
                if (this.clearFirst) {
                    this.context.getContentResolver().update(RedditProvider.URI_CLEAR, (ContentValues) nullObj, this.subreddit, (String[]) nullObj);
                }
                long jCurrentTimeMillis = System.currentTimeMillis();
                ContentResolver contentResolver = this.context.getContentResolver();
                Uri uri = RedditProvider.URI_MULTIPLE_POSTS;
                int iBulkInsert = contentResolver.bulkInsert(uri, contentValuesArrA);
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                PostsInfo info = this.postsInfo;
                info.networkTimeMs = networkResponse.networkTimeMs;
                info.requestDuration = jCurrentTimeMillis2 - jCurrentTimeMillis;
                info.after = jSONObject.getJSONObject("data").getString("after");
                this.postsInfo.count += arrayList.size();
                this.postsInfo.page++;
                Logger.verbose("New count:  " + this.postsInfo.count);
                Logger.verbose("Inserted:  " + iBulkInsert);
                PostsInfo info2 = this.postsInfo;
                info2.insertCount = iBulkInsert;
                info2.stagnant = iBulkInsert == 0;
                if (iBulkInsert == 1 && dividerValues != null) {
                    info2.stagnant = true;
                }
                Logger.verbose("Stagnant:  " + this.postsInfo.stagnant);
                this.postsInfo.filtersBlocked = jSONArray.length() > 0 && contentValuesArrA.length == 0;
                Logger.verbose("Filters blocked:  " + this.postsInfo.filtersBlocked);
                this.context.getContentResolver().notifyChange(uri, null);
            }
            return Response.success(this.postsInfo, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
