package com.resync.app.media.preview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkHandler;
import com.resync.app.util.links.LinkTypeHelper;

/**
 * Resolves the preview image URL for an arbitrary web page by fetching its HTML
 * and extracting the OpenGraph / Twitter card image meta tags (client-side,
 * replacing the discontinued ap.syncforreddit.com proxy).
 *
 * <p>Designed to run on a Glide background thread (never the main thread). Fails
 * fast and returns {@code null} when no preview image can be determined — the
 * caller then renders a neutral link placeholder rather than a broken image.
 */
public final class OgImageResolver {

    // A real mobile-browser UA: some sites/CDNs 403 unknown agents, others cloak
    // for known crawler agents. Mimicking Chrome-on-Android maximises og:image hits.
    private static final String BROWSER_USER_AGENT =
            "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/124.0.0.0 Mobile Safari/537.36";

    // og:* tags live in <head>; cap the download so we never pull multi-MB pages on mobile data.
    private static final int MAX_HTML_BYTES = 256 * 1024;
    private static final int TIMEOUT_SECONDS = 10;

    private static volatile OkHttpClient client;

    private OgImageResolver() {
    }

    private static OkHttpClient client() {
        if (client == null) {
            synchronized (OgImageResolver.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder()
                            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .followRedirects(true)
                            .followSslRedirects(true)
                            .build();
                }
            }
        }
        return client;
    }

    /**
     * @return an absolute preview-image URL, or {@code null} if none could be resolved.
     */
    public static String resolve(String pageUrl) {
        if (StringUtils.isEmpty(pageUrl)) {
            return null;
        }
        // YouTube: skip the HTML fetch, use the canonical thumbnail directly (fast + reliable).
        if (LinkTypeHelper.isYouTube(pageUrl)) {
            String id = LinkHandler.getYouTubeId(pageUrl);
            return StringUtils.isEmpty(id) ? null : "https://i.ytimg.com/vi/" + id + "/hqdefault.jpg";
        }
        // The link is itself an image — no extraction needed.
        if (LinkTypeHelper.isImage(pageUrl)) {
            return pageUrl;
        }

        Response response = null;
        try {
            Request request = new Request.Builder()
                    .url(pageUrl)
                    .header("User-Agent", BROWSER_USER_AGENT)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .build();
            response = client().newCall(request).execute();
            ResponseBody body = response.body();
            if (!response.isSuccessful() || body == null) {
                return null;
            }
            // Only parse HTML; bail on images/PDF/binaries.
            String contentType = response.header("Content-Type");
            if (StringUtils.isNotEmpty(contentType)
                    && !StringUtils.containsIgnoreCase(contentType, "html")
                    && !StringUtils.containsIgnoreCase(contentType, "xml")) {
                return null;
            }
            byte[] head = readCapped(body.byteStream(), MAX_HTML_BYTES);
            // Use the post-redirect URL as the base so relative og:image values resolve.
            String baseUri = response.request().url().toString();
            // Let Jsoup sniff the charset from the bytes (don't pre-decode).
            Document doc = Jsoup.parse(new ByteArrayInputStream(head), null, baseUri);
            return extractImage(doc);
        } catch (Exception e) {
            Logger.logException(e);
            return null;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /** Selector priority: secure_url -> og:image:url -> og:image -> twitter:image -> image_src -> itemprop. */
    private static String extractImage(Document doc) {
        String[] candidates = {
                doc.select("meta[property=og:image:secure_url]").attr("abs:content"),
                doc.select("meta[property=og:image:url]").attr("abs:content"),
                doc.select("meta[property=og:image]").attr("abs:content"),
                doc.select("meta[name=og:image]").attr("abs:content"),
                doc.select("meta[name=twitter:image]").attr("abs:content"),
                doc.select("meta[name=twitter:image:src]").attr("abs:content"),
                doc.select("link[rel=image_src]").attr("abs:href"),
                doc.select("meta[itemprop=image]").attr("abs:content"),
        };
        for (String candidate : candidates) {
            if (StringUtils.isNotEmpty(candidate) && !looksLikeIcon(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * True for favicon/touch-icon/logo URLs that some sites (e.g. Atlassian Statuspage) expose as
     * og:image. Up-scaling a ~32px icon into the preview box produces a useless blurry smear, so we
     * reject these and let the page fall through to a real screenshot.
     */
    private static boolean looksLikeIcon(String url) {
        String lower = url.toLowerCase(java.util.Locale.ENGLISH);
        return lower.contains("favicon")
                || lower.contains("apple-touch-icon")
                || lower.endsWith(".ico");
    }

    private static byte[] readCapped(InputStream in, int maxBytes) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int total = 0;
        int read;
        while (total < maxBytes && (read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
            total += read;
        }
        return out.toByteArray();
    }
}
