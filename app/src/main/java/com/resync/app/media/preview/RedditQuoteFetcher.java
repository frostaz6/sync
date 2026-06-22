package com.resync.app.media.preview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.resync.app.auth.WebSessionManager;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkHandler;

/**
 * Fetches the comment or post a reddit permalink points to, so it can be rendered inline as a
 * forum-style quote card ({@link com.resync.app.text.spans.RedditQuoteSpan}).
 *
 * <p>Entirely client-side against reddit's own oauth API (the discontinued Sync backend is irrelevant
 * here): we reuse the existing {@link WebSessionManager} bearer (the same token that powers the normal
 * comment thread loads) and read the public {@code .json} representation. Results are cached in-memory
 * by href so RecyclerView re-binds and span {@code refresh()} calls never refetch. Failures cache a
 * {@link Quote#FAILED} sentinel so the span stays collapsed (no box) and the plain link remains.
 */
public final class RedditQuoteFetcher {

    private static final String TAG = "RedditQuoteFetcher";
    private static final int TIMEOUT_SECONDS = 10;
    private static final long TOKEN_WAIT_MS = 8000L;
    private static final int CACHE_CAP = 64;

    /** Reddit-required UA (matches WebSessionManager / AccountSingleton format). */
    private static final String USER_AGENT =
            "android:com.resync.app:v1.0";

    /** Immutable result of resolving a reddit permalink. */
    public static final class Quote {
        /** Sentinel cached on any failure so we don't retry and the span stays collapsed. */
        public static final Quote FAILED = new Quote();

        public final boolean isComment;   // true = t1 comment, false = t3 post
        public final String author;
        public final String subreddit;
        public final String title;        // post title (null for comments)
        public final String bodyHtml;     // comment body_html / post selftext_html (may be empty)
        public final String imageUrl;     // first image embedded in the quoted content (or null)
        public final int score;
        public final int numComments;     // posts only

        private Quote() {
            this.isComment = false;
            this.author = null;
            this.subreddit = null;
            this.title = null;
            this.bodyHtml = null;
            this.imageUrl = null;
            this.score = 0;
            this.numComments = 0;
        }

        Quote(boolean isComment, String author, String subreddit, String title,
              String bodyHtml, String imageUrl, int score, int numComments) {
            this.isComment = isComment;
            this.author = author;
            this.subreddit = subreddit;
            this.title = title;
            this.bodyHtml = bodyHtml;
            this.imageUrl = imageUrl;
            this.score = score;
            this.numComments = numComments;
        }

        public boolean isFailed() {
            return this == FAILED;
        }
    }

    public interface Callback {
        /** Always fired on the main thread. {@code quote} is non-null (may be {@link Quote#FAILED}). */
        void onQuote(Quote quote);
    }

    private RedditQuoteFetcher() {
    }

    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(3,
            new java.util.concurrent.ThreadFactory() {
                private int n = 0;
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "reddit-quote-" + (n++));
                    t.setDaemon(true);
                    t.setPriority(Thread.NORM_PRIORITY - 1);
                    return t;
                }
            });

    // Access-ordered LRU; synchronized externally.
    private static final LinkedHashMap<String, Quote> CACHE =
            new LinkedHashMap<String, Quote>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, Quote> eldest) {
                    return size() > CACHE_CAP;
                }
            };

    private static volatile OkHttpClient client;

    private static OkHttpClient client() {
        if (client == null) {
            synchronized (RedditQuoteFetcher.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder()
                            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .followRedirects(true)
                            .build();
                }
            }
        }
        return client;
    }

    /** Cached result without any network, or null if not yet fetched. */
    public static Quote getCached(String href) {
        synchronized (CACHE) {
            return CACHE.get(href);
        }
    }

    /**
     * Resolve a reddit permalink to its {@link Quote}. Serves the cache synchronously when present;
     * otherwise fetches on a background thread and delivers on the main thread.
     *
     * @return a {@link Future} for the in-flight fetch (cancellable), or null when served from cache.
     */
    public static Future<?> fetch(final Context context, final String href, final Callback callback) {
        Quote cached = getCached(href);
        if (cached != null) {
            callback.onQuote(cached);
            return null;
        }
        return EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                Quote result;
                try {
                    result = doFetch(context, href);
                } catch (Exception e) {
                    Logger.logException(e);
                    result = Quote.FAILED;
                }
                final Quote delivered = result != null ? result : Quote.FAILED;
                synchronized (CACHE) {
                    CACHE.put(href, delivered);
                }
                MAIN.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onQuote(delivered);
                    }
                });
            }
        });
    }

    private static Quote doFetch(Context context, String href) {
        String postId = LinkHandler.getCommentsId(href);
        String commentId = LinkHandler.getCommentsCommentId(href);
        String subreddit = LinkHandler.getCommentsSubreddit(href);
        if (StringUtils.isEmpty(postId)) {
            return Quote.FAILED;
        }

        String token = WebSessionManager.get(context).getTokenBlocking(TOKEN_WAIT_MS);
        if (StringUtils.isEmpty(token)) {
            android.util.Log.i(TAG, "No bearer token for quote fetch: " + href);
            return Quote.FAILED;
        }

        StringBuilder url = new StringBuilder(SettingsSingleton.getOauthUrl());
        if (!StringUtils.isEmpty(subreddit)) {
            url.append("r/").append(subreddit).append("/");
        }
        url.append("comments/").append(postId);
        boolean isComment = !StringUtils.isEmpty(commentId);
        if (isComment) {
            url.append("/_/").append(commentId);
        }
        url.append(".json?raw_json=1&limit=1");
        if (isComment) {
            url.append("&context=0&threaded=false&depth=1");
        }

        Response response = null;
        try {
            Request request = new Request.Builder()
                    .url(url.toString())
                    .header("Authorization", "bearer " + token)
                    .header("User-Agent", USER_AGENT)
                    .get()
                    .build();
            response = client().newCall(request).execute();
            ResponseBody body = response.body();
            if (!response.isSuccessful() || body == null) {
                android.util.Log.i(TAG, "Quote fetch HTTP " + response.code() + " for " + href);
                return Quote.FAILED;
            }
            JSONArray root = new JSONArray(body.string());
            return isComment ? parseComment(root) : parsePost(root);
        } catch (Exception e) {
            Logger.logException(e);
            return Quote.FAILED;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /** Listing[0] = post; Listing[1] = comments. We want the first comment in [1]. */
    private static Quote parseComment(JSONArray root) {
        try {
            JSONObject commentData = root.getJSONObject(1)
                    .getJSONObject("data").getJSONArray("children")
                    .getJSONObject(0).getJSONObject("data");
            String author = optStr(commentData, "author", "[unknown]");
            String sub = optStr(commentData, "subreddit", null);
            String bodyHtml = optStr(commentData, "body_html", "");
            int score = commentData.optInt("score", 0);
            if ("[deleted]".equals(author) && StringUtils.isEmpty(bodyHtml)) {
                return Quote.FAILED;
            }
            String html = unescape(bodyHtml);
            String image = extractFirstImageUrl(html);
            return new Quote(true, author, sub, null, stripImageAnchor(html, image), image, score, 0);
        } catch (Exception e) {
            Logger.logException(e);
            return Quote.FAILED;
        }
    }

    /** Listing[0] = the post. */
    private static Quote parsePost(JSONArray root) {
        try {
            JSONObject postData = root.getJSONObject(0)
                    .getJSONObject("data").getJSONArray("children")
                    .getJSONObject(0).getJSONObject("data");
            String author = optStr(postData, "author", "[unknown]");
            String sub = optStr(postData, "subreddit", null);
            String title = optStr(postData, "title", null);
            String selftextHtml = optStr(postData, "selftext_html", "");
            int score = postData.optInt("score", 0);
            int numComments = postData.optInt("num_comments", 0);
            if (TextUtils.isEmpty(title)) {
                return Quote.FAILED;
            }
            String html = unescape(selftextHtml);
            // Link/image posts have empty selftext but a top-level url pointing at the media.
            String image = extractFirstImageUrl(html);
            if (image == null) {
                String url = optStr(postData, "url_overridden_by_dest", optStr(postData, "url", null));
                if (isImageUrl(url)) {
                    image = url;
                }
            }
            return new Quote(false, author, sub, title, stripImageAnchor(html, image), image, score, numComments);
        } catch (Exception e) {
            Logger.logException(e);
            return Quote.FAILED;
        }
    }

    /**
     * Null-safe string getter. Android's {@link JSONObject#optString(String, String)} stringifies a
     * JSON {@code null} value to the literal {@code "null"} instead of returning the fallback (e.g.
     * {@code selftext_html} on a link post), which would otherwise render "null" inside the quote card.
     */
    private static String optStr(JSONObject obj, String key, String fallback) {
        if (obj.isNull(key)) {
            return fallback;
        }
        return obj.optString(key, fallback);
    }

    /** True for a URL that points directly at an image (incl. reddit's preview/i.redd.it CDNs). */
    private static boolean isImageUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        String u = url.toLowerCase(java.util.Locale.ENGLISH);
        if (u.contains("preview.redd.it") || u.contains("i.redd.it") || u.contains("i.imgur.com")) {
            return true;
        }
        // ignore query string when checking the extension
        int q = u.indexOf('?');
        if (q >= 0) {
            u = u.substring(0, q);
        }
        return u.endsWith(".png") || u.endsWith(".jpg") || u.endsWith(".jpeg")
                || u.endsWith(".gif") || u.endsWith(".webp");
    }

    /** Finds the first embedded image in the body HTML (an &lt;a href&gt; or bare URL to an image). */
    private static String extractFirstImageUrl(String html) {
        if (StringUtils.isEmpty(html)) {
            return null;
        }
        java.util.regex.Matcher m =
                java.util.regex.Pattern.compile("https?://[^\\s\"'<>)]+").matcher(html);
        while (m.find()) {
            String candidate = m.group();
            if (isImageUrl(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Removes the image's own anchor/URL from the body HTML so it isn't ALSO rendered as a giant raw
     * link in the quote text (the image is shown as a thumbnail instead). Leaves all other text intact.
     */
    private static String stripImageAnchor(String html, String imageUrl) {
        if (StringUtils.isEmpty(html) || StringUtils.isEmpty(imageUrl)) {
            return html;
        }
        // Drop a full <a ...>...</a> wrapping the image URL, then any bare occurrence of the URL.
        String escaped = java.util.regex.Pattern.quote(imageUrl);
        String out = html.replaceAll("<a [^>]*href=\"" + escaped + "\"[^>]*>.*?</a>", "");
        out = out.replace(imageUrl, "");
        return out;
    }

    /** reddit returns *_html HTML-escaped even with raw_json=1 inconsistently; normalise the entities. */
    private static String unescape(String html) {
        if (StringUtils.isEmpty(html)) {
            return "";
        }
        return html.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'");
    }
}
