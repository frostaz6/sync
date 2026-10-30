package com.resync.app.net.oauth;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.net.RedditRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Reddit's /comments/... JSON endpoint began returning 403 in October 2026.
 * Reads the Android app's persisted PostComments GraphQL operation and converts
 * its comment forest into the legacy JSON two-listing response that Sync's
 * CommentParserHelper/PostParserHelper already understand.
 *
 * The requested post is fetched separately through /by_id by the parent
 * OAuthCommentsRequest, so its metadata remains untouched.
 */
final class GraphQLCommentsRequest extends RedditRequest<Void> {
    private static final String TAG = "ReSyncComments";
    private static final String URL = "https://gql-fed.reddit.com/";
    // Reddit Android persisted operation, validated by redlib-org/redlib#603.
    private static final String HASH =
            "314f29c2494fd78c7844ef2c6b09acb4bef5b37ab50b4ddc01691d6d5c8433f8";
    private final OAuthCommentsRequest parent;
    private final String postId;
    private final byte[] requestBody;

    GraphQLCommentsRequest(Context context, OAuthCommentsRequest owner,
            String postId, String commentId, String sort, int commentCount) {
        super(context, Method.POST, URL,
                unused -> owner.deliverResponse(null),
                owner::deliverError);
        this.parent = owner;
        this.postId = postId;
        try {
            JSONObject vars = new JSONObject();
            vars.put("id", "t3_" + postId);
            vars.put("sortType", graphSort(sort));
            vars.put("count", Math.min(200, commentCount <= 0 ? 200 : commentCount));
            vars.put("maxDepth", 10);
            vars.put("includeConsistencyToken", false);
            vars.put("includePremiumAvatarTreatment", false);
            if (!TextUtils.isEmpty(commentId)) {
                JSONObject focus = new JSONObject();
                focus.put("commentId", commentId.startsWith("t1_") ? commentId : "t1_" + commentId);
                focus.put("isFocusedThreadOnly", true);
                vars.put("focusedCommentContext", focus);
            }
            JSONObject query = new JSONObject();
            query.put("operationName", "PostComments");
            query.put("variables", vars);
            JSONObject persisted = new JSONObject().put("version", 1).put("sha256Hash", HASH);
            query.put("extensions", new JSONObject().put("persistedQuery", persisted));
            requestBody = query.toString().getBytes(StandardCharsets.UTF_8);
        } catch (JSONException e) {
            throw new IllegalStateException("Could not prepare GraphQL query", e);
        }
    }

    private static String graphSort(String sort) {
        if (sort == null) return "CONFIDENCE";
        switch (sort.toLowerCase(Locale.US)) {
            case "best":
            case "suggested":
            case "confidence": return "CONFIDENCE";
            case "new": return "NEW";
            case "top": return "TOP";
            case "old": return "OLD";
            case "controversial": return "CONTROVERSIAL";
            case "qa":
            case "q&a": return "QA";
            default: return "CONFIDENCE";
        }
    }

    @Override public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override public byte[] getBody() {
        return requestBody;
    }

    @Override public VolleyError parseNetworkError(VolleyError error) {
        int status = error != null && error.networkResponse != null
                ? error.networkResponse.statusCode : -1;
        Log.e(TAG, "GraphQL request failed: HTTP " + status
                + " " + (error == null ? "null" : error.getClass().getSimpleName()));
        return super.parseNetworkError(error);
    }

    @Override public Response<Void> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject raw = new JSONObject(new String(response.data, StandardCharsets.UTF_8));
            JSONObject data = raw.optJSONObject("data");
            JSONObject info = data == null ? null : data.optJSONObject("postInfoById");
            JSONObject forest = info == null ? null : info.optJSONObject("commentForest");
            JSONArray trees = forest == null ? null : forest.optJSONArray("trees");
            if (trees == null) {
                JSONArray errors = raw.optJSONArray("errors");
                String problem = errors != null && errors.length() > 0
                        ? errors.optJSONObject(0).optString("message", "No comment forest")
                        : "No comment forest in PostComments response";
                throw new JSONException(problem);
            }
            JSONArray children = convertTrees(trees, postId,
                    parent.getPostListing().getJSONObject("data")
                            .getJSONArray("children").getJSONObject(0)
                            .getJSONObject("data").optString("subreddit", ""));
            JSONObject commentsListing = new JSONObject()
                    .put("kind", "Listing")
                    .put("data", new JSONObject().put("children", children));
            JSONArray combined = new JSONArray().put(parent.getPostListing()).put(commentsListing);
            byte[] adapted = combined.toString().getBytes(StandardCharsets.UTF_8);
            NetworkResponse converted = new NetworkResponse(response.statusCode, adapted,
                    response.headers, false, response.networkTimeMs);
            Log.i(TAG, "GraphQL HTTP " + response.statusCode + ": "
                    + children.length() + " top-level comments");
            return parent.parseLegacyCommentsResponse(converted);
        } catch (Exception e) {
            Log.e(TAG, "GraphQL comments conversion failed: " + e.getMessage(), e);
            return Response.error(new ParseError(e));
        }
    }

    private static final class Frame {
        final int depth;
        final JSONObject comment;
        Frame(int depth, JSONObject comment) {
            this.depth = depth;
            this.comment = comment;
        }
    }

    /** The GraphQL forest is ordered depth-first, with depth on each entry. */
    static JSONArray convertTrees(JSONArray trees, String postId, String subreddit)
            throws JSONException {
        JSONArray top = new JSONArray();
        ArrayList<Frame> path = new ArrayList<>();
        for (int i = 0; i < trees.length(); i++) {
            JSONObject tree = trees.optJSONObject(i);
            if (tree == null) continue;
            int depth = Math.max(0, tree.optInt("depth", 0));
            // Cursor placeholders need GraphQL pagination, not legacy morechildren.
            // Skip them instead of exposing a button that makes a blocked API call.
            if (tree.optJSONObject("more") != null) continue;
            JSONObject node = tree.optJSONObject("node");
            if (node == null) continue;
            JSONObject item = makeComment(tree, node, postId, subreddit);
            while (!path.isEmpty() && path.get(path.size() - 1).depth >= depth) {
                path.remove(path.size() - 1);
            }
            if (path.isEmpty()) {
                top.put(item);
            } else {
                JSONObject parentData = path.get(path.size() - 1).comment.getJSONObject("data");
                JSONObject replies = parentData.optJSONObject("replies");
                if (replies == null) {
                    replies = new JSONObject()
                            .put("kind", "Listing")
                            .put("data", new JSONObject().put("children", new JSONArray()));
                    parentData.put("replies", replies);
                }
                replies.getJSONObject("data").getJSONArray("children").put(item);
            }
            path.add(new Frame(depth, item));
        }
        return top;
    }

    private static JSONObject makeComment(JSONObject tree, JSONObject node,
            String postId, String subreddit) throws JSONException {
        String fullname = node.optString("id", "");
        if (fullname.isEmpty()) {
            // Deleted comments can have no stable ID: use an ephemeral local ID.
            fullname = "t1_deleted_" + Math.abs(tree.toString().hashCode());
        }
        String id = fullname.startsWith("t1_") ? fullname.substring(3) : fullname;
        String parentId = tree.optString("parentId", "t3_" + postId);
        if (parentId.isEmpty() || "null".equals(parentId)) parentId = "t3_" + postId;
        JSONObject author = node.optJSONObject("authorInfo");
        String authorName = author == null ? "[deleted]" : author.optString("name", "[deleted]");
        JSONObject content = node.optJSONObject("content");
        String body = content == null ? "" : content.optString("markdown", "");
        if ("DeletedComment".equals(node.optString("__typename"))) {
            authorName = "[deleted]";
            body = "[deleted]";
        } else if (node.optBoolean("isAdminTakedown")) {
            body = "[ Removed by Reddit ]";
        } else if (node.optBoolean("isRemoved")) {
            body = "[removed]";
        }
        JSONObject authorFlair = node.optJSONObject("authorFlair");
        JSONObject data = new JSONObject()
                .put("id", id)
                .put("name", fullname)
                .put("parent_id", parentId)
                .put("link_id", "t3_" + postId)
                .put("subreddit", subreddit)
                .put("author", authorName)
                .put("body", body)
                .put("body_html", "<div class=\"md\"><p>"
                        + android.text.TextUtils.htmlEncode(body).replace("\n", "<br/>")
                        + "</p></div>")
                .put("score", node.optInt("score", 0))
                .put("score_hidden", node.optBoolean("isScoreHidden"))
                .put("stickied", node.optBoolean("isStickied"))
                .put("created_utc", parseTime(node.optString("createdAt", "")))
                .put("edited", false)
                .put("replies", "")
                .put("all_awardings", new JSONArray());
        if (node.optBoolean("isRemoved")) {
            data.put("author", "[deleted]");
        }
        if (authorFlair != null) {
            data.put("author_flair_text", authorFlair.optString("text", ""));
        }
        String distinguished = node.optString("distinguishedAs", "");
        if (!distinguished.isEmpty()) {
            data.put("distinguished", distinguished.toLowerCase(Locale.US));
        }
        return new JSONObject().put("kind", "t1").put("data", data);
    }

    private static long parseTime(String iso) {
        if (TextUtils.isEmpty(iso)) return 0;
        for (String format : new String[]{
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSSX",
                "yyyy-MM-dd'T'HH:mm:ssX"}) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = sdf.parse(iso);
                if (date != null) return date.getTime() / 1000L;
            } catch (Exception ignored) {
            }
        }
        return 0;
    }
}
