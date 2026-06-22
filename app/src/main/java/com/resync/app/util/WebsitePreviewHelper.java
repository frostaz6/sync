package com.resync.app.util;

import com.resync.app.singleton.SettingsSingleton;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: wc.q */
/* JADX INFO: compiled from: WebsitePreviewHelper.java */
/* JADX INFO: loaded from: classes3.dex */
public class WebsitePreviewHelper {
    public static boolean shouldShowPreview(String url) {
        if (RemoteConfigHelper.isC4266fa() && UltraHelper.isUltra()) {
            String string = PreferencesHelper.getDefaultPrefs().getString(KeyManager.purchaseTokenKey(), null);
            if (StringUtils.isEmpty(string) || !StringUtils.containsIgnoreCase(string, ".AO-")) {
                return false;
            }
        }
        return UltraHelper.isUltra() && SettingsSingleton.getSettings().ultra_website_previews && isPreviewable(url);
    }

    /**
     * Whether a reddit comment/thread permalink should render as an inline forum-style quote card
     * (see {@link com.resync.app.text.spans.RedditQuoteSpan}). Reuses the same Ultra +
     * "website previews" gate as {@link #shouldShowPreview}, but matches reddit comment links — which
     * {@link #isPreviewable} deliberately excludes from the og:image preview path.
     */
    public static boolean shouldShowRedditQuote(String url) {
        if (RemoteConfigHelper.isC4266fa() && UltraHelper.isUltra()) {
            String string = PreferencesHelper.getDefaultPrefs().getString(KeyManager.purchaseTokenKey(), null);
            if (StringUtils.isEmpty(string) || !StringUtils.containsIgnoreCase(string, ".AO-")) {
                return false;
            }
        }
        return UltraHelper.isUltra() && SettingsSingleton.getSettings().ultra_website_previews
                && LinkTypeHelper.isComments(url);
    }

    public static String getPreviewUrl(String url) {
        if (LinkTypeHelper.isYouTube(url)) {
            return "https://i.ytimg.com/vi/" + LinkHandler.getYouTubeId(url) + "/hqdefault.jpg";
        }
        if (url.contains("/amp/s/")) {
            url = "https://" + url.split("/amp/s/")[1];
            if (url.endsWith(".amp")) {
                url = url.substring(0, url.length() - 4);
            }
        }
        try {
            if (url.contains("/?amp=1")) {
                url = url.split("/\\?amp=1")[0];
                if (url.startsWith("https://arstechnica-com.cdn.ampproject.org/v/s/")) {
                    url = url.replace("https://arstechnica-com.cdn.ampproject.org/v/s/", "https://");
                }
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
        if (url.endsWith("/amp")) {
            url = url.substring(0, url.lastIndexOf("/amp"));
        }
        return "https://ap.syncforreddit.com/image?url=" + url;
    }

    public static boolean isPreviewable(String url) {
        return (!url.startsWith("https://") || LinkTypeHelper.isImage(url) || ImgurImageHelper.isAlbum(url) || LinkTypeHelper.isImgur(url) || StringUtils.containsAny(url, ".jpg", ".jpeg", ".png", ".mp4", ".gif", ".pdf", ".xml", ".apk") || StringUtils.containsAny(url, "reddit.com", "v.redd.it", "redd.it", "giphy", "gfycat", "twitter", "t.co", "discord", "photos.google.com", "drive.google.com", "photos.app.goo.gl", "facebook.com", "meta.com", "fb.me", "instagram.com", "repostsleuth.com", "issuetracker.google.com", "support.google.com/phoneapp/`", "translate.google.com", "google.com/url?sa", "doi.org", "xkcd.com", "apkmirror.com", "support.apple.com", "duckduckgo.com", "nanoreview.net", "congress.gov", "tenor.com", "play.google.com/apps/testing", "https://example.com", "telegram.me/", "androidflair.github.io", "reddit.watch/", "redditsave.com")) ? false : true;
    }
}
