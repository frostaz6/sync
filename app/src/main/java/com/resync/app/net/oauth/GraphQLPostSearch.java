package com.resync.app.net.oauth;

import android.text.TextUtils;
import com.bumptech.glide.MaterialPostsHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Native Reddit SearchPosts GraphQL -> legacy JSON Listing adapter.
 * ReSync continues to use its original parser, filters, database and feed UI.
 * Persisted operation from Amqx/reddit-gql (Oct 2026).
 */
final class GraphQLPostSearch {
    static final String URL = "https://gql-fed.reddit.com/";
    private static final String HASH =
            "8610e33521c90caa8d12576800c18b708ce0d19df3dfac8e2aad55d9860e4bf9";

    private static final String SUBREDDIT_FEED_HASH =
            "97046e2a48050f10a507db354a8da60955ffccb586dcf63163bdfc6610d5cf7a";

    private GraphQLPostSearch() {}

    /**
     * Only replace ordinary single-subreddit NEW pages. Do not touch
     * r/all, home, multi feeds, user posts, domain listings or post search.
     */
    static boolean isNewSubredditFeed(String subreddit, String sort) {
        return subreddit != null
                && sort != null
                && "new".equalsIgnoreCase(sort)
                && subreddit.matches("[A-Za-z0-9_]{2,21}")
                && !"all".equalsIgnoreCase(subreddit)
                && !"popular".equalsIgnoreCase(subreddit)
                && !"frontpage".equalsIgnoreCase(subreddit)
                && !"mod".equalsIgnoreCase(subreddit);
    }

    /** Android persisted SubredditFeedElements operation. */
    static byte[] buildSubredditFeedBody(String subreddit, String sort,
            String timeFilter, String after) {
        try {
            JSONObject vars = new JSONObject()
                    .put("subredditName", subreddit)
                    .put("sort", "NEW")
                    .put("includeSubredditInPosts", true)
                    .put("pageSize", 25);
            if (!TextUtils.isEmpty(after) && !"null".equalsIgnoreCase(after)) {
                vars.put("after", after);
            }
            JSONObject op = new JSONObject()
                    .put("operationName", "SubredditFeedElements")
                    .put("variables", vars)
                    .put("extensions", new JSONObject().put("persistedQuery",
                            new JSONObject().put("version", 1)
                                    .put("sha256Hash", SUBREDDIT_FEED_HASH)));
            return op.toString().getBytes(StandardCharsets.UTF_8);
        } catch (JSONException ex) {
            throw new IllegalStateException("Couldn't prepare subreddit New feed", ex);
        }
    }

    static String toLegacySubredditFeed(String response) throws JSONException {
        JSONObject root = new JSONObject(response);
        JSONObject data = root.optJSONObject("data");
        JSONObject feed = data == null ? null : data.optJSONObject("postFeed");
        JSONObject elements = feed == null ? null : feed.optJSONObject("elements");
        if (elements == null) {
            JSONArray errors = root.optJSONArray("errors");
            JSONObject first = errors == null ? null : errors.optJSONObject(0);
            throw new JSONException("SubredditFeedElements returned no feed"
                    + (first == null ? "" : ": " + first.optString("message", "")));
        }
        JSONArray children = new JSONArray();
        JSONArray edges = elements.optJSONArray("edges");
        if (edges != null) {
            for (int i = 0; i < edges.length(); i++) {
                JSONObject item = edges.optJSONObject(i);
                JSONObject node = item == null ? null : item.optJSONObject("node");
                if (node == null) continue;
                JSONObject post = node.optJSONObject("postInfo");
                if (post == null) post = node;
                String type = post.optString("__typename", "");
                if (!"SubredditPost".equals(type) && !"ProfilePost".equals(type)) {
                    continue;
                }
                try {
                    children.put(new JSONObject().put("kind", "t3").put("data", adapt(post)));
                } catch (JSONException ignored) {
                    // Ignore a malformed post without discarding valid peers.
                }
            }
        }
        JSONObject page = elements.optJSONObject("pageInfo");
        String cursor = page == null ? "" : page.optString("endCursor", "");
        if (page != null && !page.optBoolean("hasNextPage", false)) cursor = "";
        return new JSONObject().put("kind", "Listing")
                .put("data", new JSONObject().put("children", children)
                        .put("after", cursor)).toString();
    }

    static byte[] buildBody(String search, String sort, String timeframe,
            String after, boolean allowNsfw) {
        try {
            String q = MaterialPostsHelper.extractSearchQuery(search);
            String sub = MaterialPostsHelper.extractSearchSubreddit(search);
            if (sub != null && !sub.trim().isEmpty()) q = q.trim() + " subreddit:" + sub.trim();
            String sortName = graphSort(sort);
            JSONObject vars = new JSONObject()
                    .put("query", q).put("sort", sortName)
                    .put("pageSize", 25).put("productSurface", "");
            if (!TextUtils.isEmpty(after) && !"null".equals(after))
                vars.put("afterCursor", after);

            JSONArray filters = new JSONArray();
            String time = timeRange(timeframe);
            if (time != null && !"NEW".equals(sortName))
                filters.put(new JSONObject().put("key", "time_range").put("value", time));
            filters.put(new JSONObject().put("key", "safe_search")
                    .put("value", allowNsfw ? "off" : "on"));
            vars.put("filters", filters);

            JSONObject operation = new JSONObject()
                    .put("operationName", "SearchPosts")
                    .put("variables", vars)
                    .put("extensions", new JSONObject().put("persistedQuery",
                            new JSONObject().put("version", 1).put("sha256Hash", HASH)));
            return operation.toString().getBytes(StandardCharsets.UTF_8);
        } catch (JSONException e) {
            throw new IllegalStateException("Failed preparing Reddit post search", e);
        }
    }

    private static String graphSort(String s) {
        if (s == null) return "RELEVANCE";
        String lower = s.toLowerCase(Locale.US);
        if (lower.contains("comment")) return "COMMENTS";
        if (lower.contains("hot")) return "HOT";
        if (lower.contains("top")) return "TOP";
        if (lower.contains("new")) return "NEW";
        return "RELEVANCE";
    }

    private static String timeRange(String s) {
        if (s == null) return null;
        String lower = s.toLowerCase(Locale.US);
        for (String time : new String[]{"hour","day","week","month","year"})
            if (lower.contains(time)) return time;
        return null;
    }

    static String toLegacyListing(String response) throws JSONException {
        JSONObject root = new JSONObject(response);
        JSONObject data = root.optJSONObject("data");
        JSONObject search = data == null ? null : data.optJSONObject("search");
        JSONObject general = search == null ? null : search.optJSONObject("general");
        JSONObject connection = general == null ? null : general.optJSONObject("posts");
        if (connection == null) {
            JSONArray errors = root.optJSONArray("errors");
            JSONObject first = errors == null ? null : errors.optJSONObject(0);
            throw new JSONException("SearchPosts returned no results connection"
                    + (first == null ? "" : ": " + first.optString("message", "")));
        }
        JSONArray children = new JSONArray();
        JSONArray edges = connection.optJSONArray("edges");
        if (edges != null) for (int i=0; i<edges.length(); i++) {
            JSONObject edge = edges.optJSONObject(i);
            JSONObject node = edge == null ? null : edge.optJSONObject("node");
            if (node == null) continue;
            JSONObject post = node.optJSONObject("postInfo");
            if (post == null) post = node;
            String type = post.optString("__typename", "");
            if (!"SubredditPost".equals(type) && !"ProfilePost".equals(type)) continue;
            try {
                children.put(new JSONObject().put("kind", "t3").put("data", adapt(post)));
            } catch (JSONException ignored) {
                // One malformed post should not discard the rest of this page.
            }
        }
        JSONObject page = connection.optJSONObject("pageInfo");
        String cursor = page != null && page.optBoolean("hasNextPage")
                ? page.optString("endCursor", "") : "";
        return new JSONObject().put("kind", "Listing")
                .put("data", new JSONObject().put("children", children).put("after", cursor))
                .toString();
    }

    private static JSONObject adapt(JSONObject post) throws JSONException {
        String full = post.optString("id", "");
        String id = full.startsWith("t3_") ? full.substring(3) : full;
        if (id.isEmpty()) throw new JSONException("GraphQL post missing ID");
        String permalink = post.optString("permalink", "");
        JSONObject sub = post.optJSONObject("subreddit");
        String subreddit = sub == null ? "" : sub.optString("name", "");
        if (subreddit.startsWith("r/")) subreddit = subreddit.substring(2);
        if (subreddit.isEmpty() && permalink.startsWith("/r/")) {
            String[] path = permalink.split("/");
            if (path.length > 2) subreddit = path[2];
        }
        if (subreddit.isEmpty()) subreddit = "u_" + id;
        if (permalink.isEmpty()) permalink = "/r/" + subreddit + "/comments/" + id + "/";
        JSONObject author = post.optJSONObject("authorInfo");
        JSONObject content = post.optJSONObject("content");
        JSONObject media = post.optJSONObject("media");
        JSONObject still = media == null ? null : media.optJSONObject("still");
        JSONObject source = getImage(still);
        JSONObject thumbnail = getImage(post.optJSONObject("thumbnail"));
        String url = post.optString("url", "");
        if (url.isEmpty() && source != null) url = source.optString("url", "");
        if (url.isEmpty()) url = "https://www.reddit.com" + permalink;

        JSONObject legacy = new JSONObject()
                .put("id", id).put("name", "t3_" + id)
                .put("title", post.optString("postTitle", post.optString("title", "")))
                .put("subreddit", subreddit)
                .put("subreddit_id", sub == null ? "" : sub.optString("id", ""))
                .put("author", author == null ? "[deleted]" :
                        author.optString("name", author.optString("displayName", "[deleted]")))
                .put("author_fullname", author == null ? "" : author.optString("id", ""))
                .put("created_utc", time(post.optString("createdAt", "")))
                .put("permalink", permalink).put("url", url)
                .put("domain", post.optString("domain", "reddit.com"))
                .put("num_comments", post.optInt("commentCount"))
                .put("score", post.optInt("score"))
                .put("upvote_ratio", post.optDouble("upvoteRatio", 1.0))
                .put("likes", vote(post.optString("voteState", "")))
                .put("saved", post.optBoolean("isSaved"))
                .put("hidden", post.optBoolean("isHidden"))
                .put("over_18", post.optBoolean("isNsfw"))
                .put("spoiler", post.optBoolean("isSpoiler"))
                .put("stickied", post.optBoolean("isStickied"))
                .put("is_self", post.optBoolean("isSelfPost"))
                .put("selftext", content == null ? "" : content.optString("markdown", ""))
                .put("post_hint", post.optString("postHint", ""))
                .put("thumbnail", thumbnail == null ? "self" : thumbnail.optString("url", "self"));
        JSONObject flair = post.optJSONObject("flair");
        if (flair != null) {
            legacy.put("link_flair_text", flair.optString("text", ""));
            JSONObject template = flair.optJSONObject("template");
            if (template != null) {
                legacy.put("link_flair_template_id", template.optString("id", ""));
                legacy.put("link_flair_background_color",
                        template.optString("backgroundColor", ""));
            }
        }
        if (source != null) {
            JSONObject dims = source.optJSONObject("dimensions");
            JSONObject previewSource = new JSONObject().put("url", source.optString("url", ""))
                    .put("width", dims == null ? 0 : dims.optInt("width"))
                    .put("height", dims == null ? 0 : dims.optInt("height"));
            legacy.put("preview", new JSONObject().put("images", new JSONArray().put(
                    new JSONObject().put("source", previewSource)
                            .put("resolutions", new JSONArray()))));
        }
        if (media != null && media.optJSONObject("streaming") != null) {
            JSONObject stream = media.optJSONObject("streaming");
            legacy.put("is_video", true);
            legacy.put("secure_media", new JSONObject().put("reddit_video",
                    new JSONObject().put("hls_url", stream.optString("hlsUrl", ""))
                            .put("is_gif", stream.optBoolean("isGif"))));
        }
        JSONObject gallery = post.optJSONObject("gallery");
        JSONArray images = gallery == null ? null : gallery.optJSONArray("items");
        if (images != null) {
            JSONArray entries = new JSONArray();
            JSONObject metadata = new JSONObject();
            for (int i=0; i<images.length(); i++) {
                JSONObject item = images.optJSONObject(i);
                JSONObject asset = item == null ? null : item.optJSONObject("media");
                if (asset == null) continue;
                JSONObject img = getImage(asset);
                String mediaId = asset.optString("id", item.optString("id", ""));
                if (img == null || mediaId.isEmpty()) continue;
                JSONObject dimensions = img.optJSONObject("dimensions");
                JSONObject original = new JSONObject()
                        .put("u", img.optString("url", ""))
                        .put("x", dimensions == null ? asset.optInt("width") : dimensions.optInt("width"))
                        .put("y", dimensions == null ? asset.optInt("height") : dimensions.optInt("height"));
                metadata.put(mediaId, new JSONObject().put("id", mediaId).put("e", "Image")
                        .put("m", asset.optString("mimetype", "image/jpeg"))
                        .put("s", original).put("p", new JSONArray().put(original)));
                entries.put(new JSONObject().put("media_id", mediaId).put("id", i));
            }
            if (entries.length() > 0) {
                legacy.put("is_gallery", true)
                        .put("gallery_data", new JSONObject().put("items", entries))
                        .put("media_metadata", metadata);
            }
        }
        return legacy;
    }

    private static JSONObject getImage(JSONObject obj) {
        if (obj == null) return null;
        for (String key : new String[]{"xxxlarge","xxlarge","xlarge","source"}) {
            JSONObject image = obj.optJSONObject(key);
            if (image != null && image.optString("url", "").startsWith("https://"))
                return image;
        }
        return obj.optString("url", "").startsWith("https://") ? obj : null;
    }

    private static Object vote(String state) {
        if ("UP".equals(state)) return Boolean.TRUE;
        if ("DOWN".equals(state)) return Boolean.FALSE;
        return JSONObject.NULL;
    }

    private static long time(String iso) {
        if (!TextUtils.isEmpty(iso))
            for (String fmt : new String[]{
                    "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd'T'HH:mm:ssXXX",
                    "yyyy-MM-dd'T'HH:mm:ss.SSSX", "yyyy-MM-dd'T'HH:mm:ssX"}) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = sdf.parse(iso);
                    if (date != null) return date.getTime() / 1000L;
                } catch (Exception ignored) {}
            }
        return System.currentTimeMillis() / 1000L;
    }
}
