package com.resync.app.media.preview;

import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.resync.app.RedditApplication;
import com.resync.app.util.WebsitePreviewHelper;

/**
 * Warms website-preview images for a freshly-loaded comment thread so they're already cached by the
 * time a comment scrolls into view (instead of popping in 1-2s later). Given the comment bodies, it
 * extracts previewable links and issues Glide preloads — which run through
 * {@link WebsitePreviewModelLoader}'s parallel pool and disk-cache, with the SAME options the
 * bind-time load uses (so the prefetched result is a cache hit at bind).
 *
 * <p>Bounded (per-comment + total caps) so a link-heavy thread can't spawn unbounded work.
 */
public final class WebsitePreviewPrefetcher {

    private static final Pattern URL = Pattern.compile("https?://[^\\s\"'<>()\\[\\]]+");
    private static final int MAX_PER_COMMENT = 5;   // matches the converter's per-comment web-preview cap
    private static final int MAX_TOTAL = 30;

    private static final ExecutorService EXTRACT_EXEC = Executors.newSingleThreadExecutor(new java.util.concurrent.ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "preview-prefetch");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        }
    });

    private WebsitePreviewPrefetcher() {
    }

    /** @param bodies processed comment-body HTML/markdown strings (e.g. Post.getBodyProcessed()). */
    public static void prefetch(final List<String> bodies) {
        if (bodies == null || bodies.isEmpty()) {
            return;
        }
        EXTRACT_EXEC.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Set<String> seen = new HashSet<>();
                    int total = 0;
                    for (String body : bodies) {
                        if (body == null || body.isEmpty()) {
                            continue;
                        }
                        Matcher m = URL.matcher(body);
                        int perComment = 0;
                        while (m.find() && perComment < MAX_PER_COMMENT && total < MAX_TOTAL) {
                            String url = normalize(m.group());
                            if (url.isEmpty() || !seen.add(url)) {
                                continue;
                            }
                            if (!WebsitePreviewHelper.shouldShowPreview(url)) {
                                continue;
                            }
                            perComment++;
                            total++;
                            warm(url);
                        }
                        if (total >= MAX_TOTAL) {
                            break;
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
        });
    }

    private static String normalize(String raw) {
        String url = raw.replace("&amp;", "&"); // HTML-unescape so it matches the decoded href used at bind
        // strip trailing punctuation that the regex may have swept up
        while (url.length() > 0 && ".,!?:;\"'".indexOf(url.charAt(url.length() - 1)) >= 0) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    private static void warm(final String url) {
        // Match the bind-time override exactly (WebPreviewImageSpan.getMaxWidth/Height: github gets bigger),
        // so the preloaded result is a cache hit when the comment binds.
        final boolean github = url.contains("github.com");
        final int width = github ? 1200 : 600;
        final int height = github ? 600 : 350;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    Glide.with(RedditApplication.getAppContext())
                            .load(new WebsitePreviewModel(url))
                            .priority(Priority.LOW)
                            .override(width, height)
                            .centerCrop()
                            .preload();
                } catch (Throwable ignored) {
                }
            }
        });
    }
}
