package com.resync.app.net.oauth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves Reddit GraphQL comment image IDs before the legacy Sync markdown
 * renderer sees them. Reddit uses ![img](media-id) with the actual media
 * asset described separately in content.richtextMedia.
 *
 * Keep this at the JSON adaptation boundary: normal text, comments and the
 * existing Markdown/HTML pipeline continue to use their original logic.
 */
final class GraphQLCommentMedia {
    private static final Pattern INLINE_IMAGE = Pattern.compile(
            "!\\[(img|gif)\\]\\(([^)\\s]+)\\)", Pattern.CASE_INSENSITIVE);
    // Asset identifiers have no path components, query parameters or schemes.
    private static final Pattern ASSET_ID = Pattern.compile("[a-zA-Z0-9_-]{6,45}");

    private GraphQLCommentMedia() {}

    static String resolve(JSONObject content, String markdown) {
        if (content == null || markdown == null || markdown.isEmpty()) {
            return markdown;
        }

        Map<String, String> urls = new HashMap<>();
        JSONArray media = content.optJSONArray("richtextMedia");
        if (media == null) media = content.optJSONArray("media");
        if (media != null) {
            for (int i = 0; i < media.length(); i++) {
                JSONObject entry = media.optJSONObject(i);
                if (entry == null) continue;
                String id = entry.optString("id", "");
                String url = mediaUrl(entry);
                if (!id.isEmpty() && url != null) {
                    urls.put(id, url);
                }
            }
        }

        Matcher matcher = INLINE_IMAGE.matcher(markdown);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String kind = matcher.group(1).toLowerCase(Locale.US);
            String source = matcher.group(2);
            String url = urls.get(source);
            // Giphy references use their own rendering path in Sync. Do not
            // rewrite them into a malformed Reddit CDN image URL.
            if (source.startsWith("giphy|") || source.startsWith("giphy%7C")) {
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group()));
                continue;
            }
            if (url == null && isSafeHttps(source)) {
                url = source;
            }
            if (url == null && ASSET_ID.matcher(source).matches()) {
                // Fallback for older GraphQL responses lacking richtextMedia:
                // native image comments use these IDs on Reddit's image CDN.
                url = "https://preview.redd.it/" + source + ("gif".equals(kind) ? ".gif" : ".jpeg");
            }
            if (url != null) {
                matcher.appendReplacement(result, Matcher.quoteReplacement("![" + kind + "](" + url + ")"));
            } else {
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group()));
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static String mediaUrl(JSONObject item) {
        String[] keys = {"mediaUrl", "url", "assetUrl", "imageUrl", "sourceUrl", "previewUrl"};
        for (String key : keys) {
            String value = item.optString(key, "");
            if (isSafeHttps(value)) return value.replace("&amp;", "&");
        }
        String[] nested = {"source", "image", "preview", "thumbnail", "s"};
        for (String key : nested) {
            JSONObject obj = item.optJSONObject(key);
            if (obj == null) continue;
            String value = obj.optString("url", obj.optString("u", ""));
            if (isSafeHttps(value)) return value.replace("&amp;", "&");
        }
        JSONArray resolutions = item.optJSONArray("resolutions");
        if (resolutions != null) {
            for (int i = resolutions.length() - 1; i >= 0; i--) {
                JSONObject obj = resolutions.optJSONObject(i);
                if (obj == null) continue;
                String value = obj.optString("url", "");
                if (isSafeHttps(value)) return value.replace("&amp;", "&");
            }
        }
        return null;
    }

    private static boolean isSafeHttps(String url) {
        if (url == null || !url.startsWith("https://")) return false;
        try {
            java.net.URI uri = new java.net.URI(url);
            return uri.getHost() != null
                    && uri.getUserInfo() == null
                    && uri.getHost().length() > 2
                    && uri.getRawFragment() == null;
        } catch (Exception e) {
            return false;
        }
    }
}
