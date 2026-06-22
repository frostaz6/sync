package com.resync.app.media.imgur;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.util.TextUtils;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: f8.a */
/* JADX INFO: compiled from: ImgurImageHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ImgurImageHelper {
    public static String getId(String url) {
        if (url.contains("\n")) {
            url = url.split("\n")[0];
        }
        String[] parts = url.split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            String candidate = parts[i];
            if (!TextUtils.isEmpty(candidate)) {
                if (candidate.contains("#") && candidate.length() > 1) {
                    String[] hashParts = candidate.split("#");
                    if (hashParts.length > 0) {
                        candidate = hashParts[0];
                        if (TextUtils.isEmpty(candidate)) {
                            continue;
                        }
                    }
                }
                if (!"new".equalsIgnoreCase(candidate)
                        && !"horizontal".equalsIgnoreCase(candidate)
                        && !"layout".equalsIgnoreCase(candidate)
                        && !"blog".equalsIgnoreCase(candidate)
                        && !"noscript".equalsIgnoreCase(candidate)
                        && !"embed".equalsIgnoreCase(candidate)
                        && !"#".equalsIgnoreCase(candidate)
                        && !"all".equalsIgnoreCase(candidate)) {
                    return cleanId(candidate);
                }
            }
        }
        return "";
    }

    public static String[] getImageIds(String str) {
        String[] parts = str.split("/");
        String[] strArrSplit = parts[parts.length - 1].split(",");
        for (int i = 0; i < strArrSplit.length; i++) {
            strArrSplit[i] = cleanId(strArrSplit[i]);
        }
        return strArrSplit;
    }

    public static String getSubdomain(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.contains("i.imgur.com")) {
            return "i";
        }
        Matcher matcher = Pattern.compile("i\\.([a-zA-Z0-9]+)\\.imgur\\.com").matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String cleanId(String str) {
        try {
            if (str.contains("#")) {
                str = str.split("#")[0];
            }
            if (str.contains("?")) {
                str = str.split("\\?")[0];
            }
            if (str.contains("&")) {
                str = str.split("\\&")[0];
            }
            if (str.contains(".")) {
                str = str.split("\\.")[0];
            }
            if (str.contains("-")) {
                str = str.split("\\-")[0];
            }
            String strReplace = str.replace("`", "");
            return strReplace.endsWith("_d") ? StringUtils.stripEnd(strReplace, "_d") : strReplace;
        } catch (Exception e) {
            System.out.println("Error: " + str);
            e.printStackTrace();
            return str;
        }
    }

    public static boolean isMultiImage(String str) {
        return isImgur(str) && str.contains(",");
    }

    public static boolean isAlbum(String str) {
        if (isImgur(str)) {
            return str.contains("iob.imgur.com") || str.contains("iob.imgur.io") || str.contains("/a/") || str.contains("/gallery/") || str.contains("/comment/");
        }
        return false;
    }

    public static boolean isImgur(String str) {
        return (TextUtils.isEmpty(str) || !LinkTypeHelper.isImgur(str) || str.contains("/blog/") || str.contains("/topic/") || str.contains("/comment/")) ? false : true;
    }

    public static String normalizeUrl(String str) {
        String strReplace = str.replace(".mp4", ".gifv").replace(".webm", ".gifv").replace("Imgur.com", "imgur.com").replace("Imgur.io", "imgur.io");
        return strReplace.startsWith("https") ? strReplace.replace("https", "http") : strReplace;
    }
}
