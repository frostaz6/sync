package com.resync.app.net.thirdparty;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import org.json.JSONObject;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: r8.d */
/* JADX INFO: compiled from: RandomRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class RandomRequest extends RedditRequest<String> {
    private final boolean nsfw;
    private static final java.util.Random RANDOM = new java.security.SecureRandom();

    public RandomRequest(Context context, boolean nsfw, Response.Listener<String> listener,
            Response.ErrorListener errorListener) {
        this(context, nsfw, 0, listener, errorListener);
    }

    public RandomRequest(Context context, boolean nsfw, int source,
            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, nsfw, source), listener, errorListener);
        this.nsfw = nsfw;
        setShouldCache(false);
    }

    /**
     * Reddit permanently removed r/random and r/randnsfw in 2024.
     * Use its real subreddit discovery/listing endpoints instead. A random
     * search character adds variety beyond the top 100 popular communities.
     */
    static String buildUrl(Context context, boolean nsfw, int source) {
        String base = SettingsSingleton.getOauthUrl(context);
        if (nsfw) {
            if (source == 0) return base + "subreddits/search.json?q=nsfw&include_over_18=on&limit=100&raw_json=1";
            if (source == 1) return base + "subreddits/search.json?q=gonewild&include_over_18=on&limit=100&raw_json=1";
            return base + "subreddits/search.json?q=18&include_over_18=on&limit=100&raw_json=1";
        }
        if (source == 0) {
            // Searching a random letter introduces much broader discovery.
            char letter = (char) ('a' + RANDOM.nextInt(26));
            return base + "subreddits/search.json?q=" + letter + "&limit=100&raw_json=1";
        }
        if (source == 1) return base + "subreddits/popular.json?limit=100&raw_json=1";
        return base + "subreddits/new.json?limit=100&raw_json=1";
    }


    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            org.json.JSONArray children = new JSONObject(
                    new String(networkResponse.data, java.nio.charset.StandardCharsets.UTF_8))
                    .getJSONObject("data").getJSONArray("children");
            java.util.ArrayList<String> names = new java.util.ArrayList<>();
            for (int i = 0; i < children.length(); i++) {
                JSONObject item = children.optJSONObject(i);
                JSONObject data = item == null ? null : item.optJSONObject("data");
                if (data == null) continue;
                boolean adult = data.optBoolean("over18", data.optBoolean("over_18", false));
                if (adult != nsfw) continue;
                if (data.optBoolean("quarantine", false)) continue;
                if (!"public".equals(data.optString("subreddit_type", "public"))) continue;
                String name = data.optString("display_name", "");
                if (!name.matches("[A-Za-z0-9_]{2,21}")) continue;
                names.add(name);
            }
            if (names.isEmpty()) {
                return Response.error(new ParseError(
                        new org.json.JSONException("No accessible matching communities in listing")));
            }
            String subreddit = names.get(RANDOM.nextInt(names.size()));
            return Response.success(subreddit, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
