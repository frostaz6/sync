package com.resync.app.media.images;

import java.util.Locale;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.model.media.Image;
import com.resync.app.media.ImageUtils;
import com.resync.app.util.TextUtils;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: c8.a */
/* JADX INFO: compiled from: ImageHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ImageHelper {
    public static String normalizeImageUrl(String str) {
        return resolveImageUrl(str, null);
    }

    public static String resolveImageUrl(String str, String str2) {
        String strReplace = str.trim().replace("Http", "http");
        Locale locale = Locale.ENGLISH;
        if (strReplace.toLowerCase(locale).contains("livememe.com/")) {
            if (hasImageExtension(strReplace)) {
                return strReplace;
            }
            return "http://i.lvme.me/" + strReplace.split("livememe.com/")[1] + ".jpg";
        }
        if (strReplace.toLowerCase(locale).contains("imgflip.com/i/")) {
            if (hasImageExtension(strReplace)) {
                return strReplace;
            }
            return "https://i.imgflip.com/" + LinkHandler.getImgflipId(strReplace) + ".jpg";
        }
        if ((strReplace.toLowerCase(locale).startsWith("http://picsarus.com/") || strReplace.toLowerCase(locale).startsWith("https://picsarus.com/") || strReplace.toLowerCase(locale).startsWith("http://www.picsarus.com/") || strReplace.toLowerCase(locale).startsWith("https://www.picsarus.com/")) && !strReplace.toLowerCase(locale).endsWith(".png") && !strReplace.toLowerCase(locale).endsWith(".jpeg") && !strReplace.toLowerCase(locale).endsWith(".jpg") && !strReplace.toLowerCase(locale).endsWith(".gif")) {
            String[] strArrSplit = strReplace.split("/");
            strReplace = "http://picsarus.com/" + strArrSplit[strArrSplit.length - 1] + ".jpg";
        }
        if (strReplace.contains("http://s3.amazonaws.com/data.tumblr.com")) {
            strReplace = strReplace.split("\\?")[0];
        }
        if (strReplace.contains("ragegenerator")) {
            strReplace = strReplace.split("\\?")[0];
        }
        if (LinkTypeHelper.isGiphy(strReplace)) {
            strReplace = "https://media.giphy.com/media/" + LinkHandler.getGiphyId(strReplace) + "/giphy.mp4";
        }
        if (LinkTypeHelper.isSliMg(strReplace)) {
            String slimgId = LinkHandler.getSlimgId(strReplace);
            String extension = ImageUtils.getExtension(strReplace);
            if ("gif".equalsIgnoreCase(extension)) {
                extension = "mp4";
            }
            strReplace = "https://i.sli.mg/" + slimgId + "." + extension;
        }
        LinkTypeHelper.isExternalPreview(strReplace);
        if (LinkTypeHelper.isRedditImage(strReplace)) {
            strReplace = "https://i.redd.it/" + LinkHandler.getRedditImageId(strReplace) + "." + ImageUtils.getExtension(strReplace);
        }
        if (LinkTypeHelper.isMakeAMeme(strReplace)) {
            strReplace = "https://media.makeameme.org/created/" + LinkHandler.getLastPathSegment(strReplace) + ".jpg";
        }
        return ImgurImageHelper.isImgur(strReplace) ? resolveImgurUrl(strReplace, str2) : strReplace;
    }

    static String resolveImgurUrl(String str, String str2) {
        String imageUrl = new Image(str).getUrl();
        return (TextUtils.isEmpty(str2) || !str2.contains("type=video")) ? imageUrl : new Image(imageUrl, "mp4").getUrl();
    }

    private static boolean hasImageExtension(String str) {
        return str.endsWith(".jpg") || str.endsWith(".jpeg") || str.endsWith(".gif") || str.endsWith(".png") || str.endsWith(".bmpg");
    }
}
