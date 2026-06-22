package com.resync.app.util.links;

import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import com.resync.app.singleton.SettingsSingleton;
import java.net.URI;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.media.ImageExtensionHelper;
import com.resync.app.singletons.PatternSingleton;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: y7.c */
/* JADX INFO: compiled from: LinkTypeHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class LinkTypeHelper {
    public static boolean isStreamable(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !StringUtils.equalsIgnoreCase("streamable.com", host)) ? false : true;
    }

    public static boolean isSubreddit(String str) {
        if (TextUtils.isEmpty(str) || str.toLowerCase(Locale.ENGLISH).contains("about/rules")) {
            return false;
        }
        return PatternSingleton.getSubredditListingPattern().matcher(str.replace("?utm_source=amp&utm_medium=top-posts-action", "").replaceAll("\\?", "")).matches();
    }

    public static boolean isTumblr(String str) {
        String host;
        return !TextUtils.isEmpty(str) && (host = getHost(str)) != null && host.contains("tumblr.com") && str.contains("post");
    }

    public static boolean isTwitterImage(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.equals("pbs.twimg.com")) ? false : true;
    }

    public static boolean isTwitterVideo(String str) {
        String host;
        return !TextUtils.isEmpty(str) && (host = getHost(str)) != null && host.equals("video.twimg.com") && str.contains(".mp4");
    }

    public static boolean isRedditUser(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String strReplaceAll = str.replaceAll("\\?", "");
        return (PatternSingleton.getUserPattern().matcher(strReplaceAll).matches() && !strReplaceAll.contains("comments")) || strReplaceAll.startsWith("/u/") || strReplaceAll.startsWith("u/");
    }

    public static boolean isValidUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return URLUtil.isValidUrl(str) || str.startsWith("/r/") || str.startsWith("r/") || str.startsWith("/u/") || str.startsWith("u/");
    }

    public static boolean isVidme(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.endsWith("vid.me") || "http://vid.me".equals(str) || "http://vid.me/".equals(str) || "https://vid.me".equals(str) || "https://vid.me/".equals(str)) ? false : true;
    }

    public static boolean isWiki(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String strReplaceAll = str.replaceAll("\\?", "");
        return PatternSingleton.getWikiPattern().matcher(strReplaceAll).matches() || (strReplaceAll.contains("reddit.com") && strReplaceAll.contains("/wiki/"));
    }

    public static boolean isImgflip(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return PatternSingleton.getXkcdPattern().matcher(str).matches();
    }

    public static boolean isYouTube(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String host = getHost(str);
        if (TextUtils.isEmpty(host) || StringUtils.contains(str, "/shorts/")) {
            return false;
        }
        return ((!host.endsWith("youtube.com") && !host.endsWith("youtu.be")) || str.contains("gifyoutube.com") || str.contains("playlist") || str.contains("/channel/") || str.contains("/clip/") || str.contains("img.youtube.com") || str.contains("/c/") || str.contains("/user/")) ? false : true;
    }

    public static String getHost(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!URLUtil.isHttpUrl(str) && !URLUtil.isHttpsUrl(str)) {
            str = URLUtil.guessUrl(str);
        }
        try {
            String host = URI.create(str).getHost();
            if (host != null) {
                if (host.startsWith("www.")) {
                    host = host.substring(4);
                }
                return host.toLowerCase(Locale.ENGLISH);
            }
        } catch (IllegalArgumentException unused) {
        }
        return null;
    }

    public static boolean isAgeVerification(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return StringUtils.startsWithIgnoreCase(str, "https://old.reddit.com/over18?dest=");
    }

    public static boolean isComments(String str) {
        if (TextUtils.isEmpty(str) || str.contains("reddit.com/rules") || str.toLowerCase(Locale.ENGLISH).contains("about/rules")) {
            return false;
        }
        if ((str.startsWith("/r/") || str.startsWith("r/")) && str.contains("/comments/")) {
            return true;
        }
        String strReplace = StringUtils.stripAccents(str).replace("?utm_source=amp&utm_medium=top-posts-action", "").replace("/?utm_source=share&amp;amp;amp;utm_medium=ios_app&amp;amp;amp;utm_name=iossmf", "");
        return PatternSingleton.getCommentsContextPattern().matcher(strReplace).matches() || PatternSingleton.getGenericRedditPattern().matcher(strReplace).matches() || PatternSingleton.getSubPathPattern().matcher(strReplace).matches() || PatternSingleton.getCommentsPattern().matcher(strReplace).matches();
    }

    public static boolean isCompose(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return PatternSingleton.getComposePattern().matcher(str).matches();
    }

    public static boolean isDeviantArt(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.endsWith("deviantart.com") || str.contains("/gallery/")) ? false : true;
    }

    public static boolean isDomain(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return PatternSingleton.getDomainPattern().matcher(str).matches();
    }

    public static boolean isDuplicate(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return PatternSingleton.getDuplicatesPattern().matcher(str).matches();
    }

    public static boolean isGfycat(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.endsWith("gfycat.com") || "http://gfycat.com".equals(str) || "http://gfycat.com/".equals(str) || "https://gfycat.com".equals(str) || "https://gfycat.com/".equals(str)) ? false : true;
    }

    public static boolean isGiphy(String str) {
        String host;
        if (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.endsWith("giphy.com")) {
            return false;
        }
        return str.contains("/gifs/") || str.contains("/media/");
    }

    public static boolean isImage(String str) {
        if (!TextUtils.isEmpty(str) && str != null && str.length() > 0) {
            String lowerCase = str.toLowerCase(Locale.ENGLISH);
            if (!ImgurImageHelper.isAlbum(lowerCase) && !ImgurImageHelper.isMultiImage(lowerCase)) {
                if (ImgurImageHelper.isImgur(lowerCase) || lowerCase.contains("i.reddituploads.com") || lowerCase.contains("g.redditmedia.com") || lowerCase.contains("i.redditmedia.com") || lowerCase.contains("v.redd.it") || lowerCase.contains("preview.redd.it") || isRedditImageUploaded(lowerCase)) {
                    return true;
                }
                if (lowerCase.contains("www.dropbox.com")) {
                    return false;
                }
                if (lowerCase.contains("livememe.com") || lowerCase.contains("imgflip.com") || isGfycat(lowerCase) || isVidme(lowerCase) || isSliMg(lowerCase) || isGiphy(lowerCase) || isRedgifs(lowerCase) || isMakeAMeme(lowerCase) || isDeviantArt(lowerCase) || isMixtape(lowerCase) || isImgflip(lowerCase) || isTwitterImage(lowerCase) || isTwitterVideo(lowerCase)) {
                    return true;
                }
                if (lowerCase.contains("mediadownloads.mlb.com") && lowerCase.endsWith(".mp4")) {
                    return true;
                }
                if ((lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".gifv") || lowerCase.endsWith(".gif") || lowerCase.endsWith(".png")) && !lowerCase.contains("minus.com") && !lowerCase.contains("web.archive.org") && !lowerCase.contains("wikipedia.org") && !lowerCase.contains("wikimedia.org") && !lowerCase.contains(".wikia.")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isImgur(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String strReplace = str.replaceAll("\\?", "").replace("`", "");
        if (strReplace.contains("imgur.com/t/") || strReplace.contains("help.imgur.com") || strReplace.contains("imgur.io/t/") || strReplace.contains("help.imgur.io") || PatternSingleton.getImgurPattern().matcher(strReplace).matches()) {
            return false;
        }
        String host = getHost(strReplace);
        if (TextUtils.isEmpty(host)) {
            return false;
        }
        return host.endsWith("imgur.com") || host.endsWith("imgur.io");
    }

    public static boolean isMakeAMeme(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.endsWith("makeameme.org")) ? false : true;
    }

    public static boolean isMixtape(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.endsWith("mixtape.moe")) ? false : true;
    }

    public static boolean isMulti(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return PatternSingleton.getMultiredditPattern().matcher(str).matches();
    }

    public static boolean isRedgifs(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.contains("redgifs.com")) ? false : true;
    }

    public static boolean isExternalPreview(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.endsWith("external-preview.redd.it")) ? false : true;
    }

    public static boolean isGallery(String str) {
        return !TextUtils.isEmpty(str) && StringUtils.containsIgnoreCase(str, "https://www.reddit.com/r/") && StringUtils.endsWithIgnoreCase(str, "?is_gallery=true");
    }

    public static boolean isRedditImage(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.endsWith("i.redd.it")) ? false : true;
    }

    public static boolean isRedditVideoDash(String str) {
        return !TextUtils.isEmpty(str) && str.contains("v.redd.it") && str.contains(".mpd");
    }

    public static boolean isRedditVideoHls(String str) {
        return !TextUtils.isEmpty(str) && str.contains("v.redd.it") && str.contains(".m3u8");
    }

    public static boolean isRedditVideo(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.contains("v.redd.it");
    }

    public static boolean isRickroll(String str) {
        if (!UltraHelper.isUltra() || !SettingsSingleton.getSettings().ultra_rickroll || !StringUtils.containsIgnoreCase(str, "youtube.com")) {
            return false;
        }
        String videoId = LinkHandler.getYouTubeId(str);
        Logger.verbose("ID: " + videoId);
        return StringUtils.equalsIgnoreCase(videoId, "dQw4w9WgXcQ");
    }

    public static boolean isSearch(String str) {
        return (str.contains("/search?") || str.contains("/search/?")) && str.contains("/r/");
    }

    public static boolean isRedditImageUploaded(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.contains("i.redd.it") && ImageExtensionHelper.isSupportedExtension(MimeTypeMap.getFileExtensionFromUrl(str));
    }

    public static boolean isShortened(String str) {
        if (TextUtils.isEmpty(str) || str.contains("libredd.it") || str.contains("i.redd.it") || str.contains("v.redd.it") || str.contains("vredd.it") || str.contains("preview.redd.it") || ImageExtensionHelper.isSupportedExtension(MimeTypeMap.getFileExtensionFromUrl(str))) {
            return false;
        }
        return str.contains("redd.it");
    }

    public static boolean isSliMg(String str) {
        String host;
        return (TextUtils.isEmpty(str) || (host = getHost(str)) == null || !host.endsWith("sli.mg") || str.contains("/a/") || str.toLowerCase().equalsIgnoreCase("http://sli.mg/") || str.toLowerCase().equalsIgnoreCase("http://sli.mg") || str.toLowerCase().equalsIgnoreCase("https://sli.mg/") || str.toLowerCase().equalsIgnoreCase("https://sli.mg")) ? false : true;
    }
}
