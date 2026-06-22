package com.resync.app.media.images;

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.model.media.Image;
import com.resync.app.util.WebsafeUtils;

/* JADX INFO: renamed from: c8.c */
/* JADX INFO: compiled from: PostThumbnailHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PostThumbnailHelper {

    // ReSync: these post-type overlay images were originally hot-loaded from the original dev's GitHub
    // repo (raw.githubusercontent.com/laurencedawson/...). They are now bundled in the APK under
    // assets/thumbs/ and loaded locally via Glide's file:///android_asset support — no network, works
    // offline, and no dependency on an external repo that could disappear.
    private static final String THUMB_DIR = "file:///android_asset/thumbs/";
    public static String PLAY_ALBUM = THUMB_DIR + "play_album.png";
    public static String PLAY_EXTERNAL = THUMB_DIR + "play_external.png";
    public static String PLAY_GFYCAT = THUMB_DIR + "play_gfycat.png";
    public static String PLAY_GIF = THUMB_DIR + "play_gif.png";

    public static String PLAY_GIPHY = THUMB_DIR + "play_giphy.png";

    public static String PLAY_IMAGE = THUMB_DIR + "play_image.png";
    public static String PLAY_NSFW = THUMB_DIR + "play_nsfw.png";
    public static String PLAY_REDDIT = THUMB_DIR + "play_reddit.png";

    public static String PLAY_SPOILER = THUMB_DIR + "play_spoiler.png";
    public static String PLAY_STREAMABLE = THUMB_DIR + "play_streamable.png";
    public static String PLAY_VIDEO = THUMB_DIR + "play_video.png";

    public static String PLAY_TEXT = THUMB_DIR + "play_text.png";

    public static String getThumbnail(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return null;
        }
        String strOptString = jSONObject.optString("url");
        String strOptString2 = jSONObject.optString("thumbnail");
        if ((strOptString2 != null && !strOptString2.startsWith("http")) || TextUtils.isEmpty(strOptString2)) {
            return null;
        }
        if (ImgurImageHelper.isImgur(strOptString)) {
            Image image = new Image(strOptString);
            if (!ImgurImageHelper.isAlbum(strOptString) && !image.isVideo()) {
                return image.getBigThumbnailUrl();
            }
        }
        // Reddit now returns HTML-escaped signed preview.redd.it thumbnail URLs (e.g. "&amp;s=...").
        // Left escaped, the query string is corrupted ("amp;s=" instead of "s=") and the CDN returns
        // 403 — so the thumbnail silently fails to load. Unescape it (same as raw_url is handled).
        String thumb = (strOptString2.startsWith("http://") && strOptString2.contains("thumbs.redditmedia.com")) ? strOptString2.replace("http://", "https://") : strOptString2;
        return WebsafeUtils.unescapeHtml(thumb);
    }
}
