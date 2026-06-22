package com.resync.app.net.oauth;

import android.content.Context;
import android.graphics.Color;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.managers.SubredditColorManager;
import com.resync.app.managers.SubredditIconManager;
import com.resync.app.helpers.SubscribedHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.q0 */
/* JADX INFO: compiled from: OAuthSearchSubredditNames.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthSearchSubredditNames extends RedditRequest<OAuthSearchSubredditNames.SubredditName[]> {
    public static String TAG = "OAuthSearchSubredditNames";

    /* JADX INFO: renamed from: y8.q0$a */
    /* JADX INFO: compiled from: OAuthSearchSubredditNames.java */
    public class SubredditName {

        String name;
        String subscriberCount;

        public SubredditName(String str, String str2) {
            this.name = str;
            this.subscriberCount = str2;
        }

        public String getName() {
            return this.name;
        }

        public String getSubscriberCount() {
            return this.subscriberCount;
        }
    }

    public OAuthSearchSubredditNames(Context context, String str, Response.Listener<SubredditName[]> listener, Response.ErrorListener errorListener) {
        super(context, 1, buildUrl(context), listener, errorListener);
        HashMap map = new HashMap();
        this.params = map;
        map.put("query", str.replaceAll(StringUtils.SPACE, ""));
        this.params.put("include_over_18", "false");
        this.params.put("include_unadvertisable", "true");
        this.params.put("exact", "false");
        setShouldCache(false);
        setTag(TAG);
    }

    public static String buildUrl(Context context) {
        return "https://oauth.reddit.com/api/search_subreddits";
    }

    @Override // com.android.volley.Request
    public Response<SubredditName[]> parseNetworkResponse(NetworkResponse networkResponse) {
        ArrayList arrayList = new ArrayList();
        try {
            String str = new String(networkResponse.data);
            Logger.verbose("JSON: " + str);
            JSONArray jSONArray = new JSONObject(str).getJSONArray("subreddits");
            for (int index = 0; index < jSONArray.length(); index++) {
                JSONObject jSONObject = jSONArray.getJSONObject(index);
                String string = jSONObject.getString("name");
                arrayList.add(new SubredditName(string, SubscribedHelper.formatSubscribers(jSONObject.optInt("subscriber_count"))));
                String strOptString = jSONObject.optString("key_color");
                if (!TextUtils.isEmpty(strOptString)) {
                    SubredditColorManager.getInstance().setColor(string, Integer.valueOf(Color.parseColor(strOptString)));
                }
                String strOptString2 = jSONObject.optString("icon_img");
                if (!TextUtils.isEmpty(strOptString2)) {
                    SubredditIconManager.getInstance().setIcon(string, strOptString2);
                }
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
        return Response.success((SubredditName[]) arrayList.toArray(new SubredditName[arrayList.size()]), HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
