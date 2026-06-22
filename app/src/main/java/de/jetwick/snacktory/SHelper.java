package de.jetwick.snacktory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

/* JADX INFO: loaded from: classes3.dex */
public class SHelper {
    public static final String UTF8 = "UTF-8";
    private static final Pattern SPACE_PATTERN = Pattern.compile(StringUtils.SPACE);

    private static class TrustAllManager implements X509TrustManager {
        private TrustAllManager() {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public static String completeDate(String str) {
        if (str == null) {
            return null;
        }
        int iIndexOf = str.indexOf(47);
        if (iIndexOf <= 0) {
            return str + "/01/01";
        }
        if (str.indexOf(47, iIndexOf + 1) > 0) {
            return str;
        }
        return str + "/01";
    }

    public static int count(String str, String str2) {
        int iIndexOf = str.indexOf(str2);
        if (iIndexOf >= 0) {
            return 1 + count(str.substring(iIndexOf + str2.length()), str2);
        }
        return 0;
    }

    public static int countLetters(String str) {
        int length = str.length();
        int count = 0;
        for (int i = 0; i < length; i++) {
            if (Character.isLetter(str.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    public static SimpleDateFormat createDateFormatter() {
        return new SimpleDateFormat("yyyy/MM/dd");
    }

    public static void enableAnySSL() {
        try {
            SSLContext sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(new KeyManager[0], new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            SSLContext.setDefault(sSLContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enableCookieMgmt() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }

    public static void enableUserAgentOverwrite() {
        System.setProperty("http.agent", "");
    }

    public static String encodingCleanup(String str) {
        StringBuilder sb2 = new StringBuilder();
        boolean started = false;
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (!Character.isDigit(cCharAt) && !Character.isLetter(cCharAt) && cCharAt != '-' && cCharAt != '_') {
                if (started) {
                    break;
                }
            } else {
                sb2.append(cCharAt);
                started = true;
            }
        }
        return sb2.toString().trim();
    }

    public static String estimateDate(String str) {
        int parsedDay;
        int iIndexOf = str.indexOf("://");
        if (iIndexOf > 0) {
            str = str.substring(iIndexOf + 3);
        }
        String[] strArrSplit = str.split("/");
        int index = 0;
        int day = -1;
        int monthIndex = -1;
        int yearIndex = -1;
        int year = -1;
        int month = -1;
        while (true) {
            if (index >= strArrSplit.length) {
                break;
            }
            String str2 = strArrSplit[index];
            if (str2.length() == 4) {
                try {
                    year = Integer.parseInt(str2);
                    if (year < 1970 || year > 3000) {
                        year = -1;
                    } else {
                        yearIndex = index;
                    }
                } catch (Exception unused) {
                }
            } else if (str2.length() != 2) {
                continue;
            } else if (monthIndex < 0 && index == yearIndex + 1) {
                month = Integer.parseInt(str2);
                if (month < 1 || month > 12) {
                    month = -1;
                } else {
                    monthIndex = index;
                }
            } else if (index == monthIndex + 1) {
                try {
                    parsedDay = Integer.parseInt(str2);
                } catch (Exception unused2) {
                    parsedDay = -1;
                }
                if (parsedDay >= 1 && parsedDay <= 31) {
                    day = parsedDay;
                    break;
                }
            } else {
                continue;
            }
            index++;
        }
        if (year < 0) {
            return null;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(year);
        if (month < 1) {
            return sb2.toString();
        }
        sb2.append(IOUtils.DIR_SEPARATOR_UNIX);
        if (month < 10) {
            sb2.append('0');
        }
        sb2.append(month);
        if (day < 1) {
            return sb2.toString();
        }
        sb2.append(IOUtils.DIR_SEPARATOR_UNIX);
        if (day < 10) {
            sb2.append('0');
        }
        sb2.append(day);
        return sb2.toString();
    }

    public static String extractDomain(String str, boolean stripSubdomain) {
        if (str.startsWith("http://")) {
            str = str.substring(7);
        } else if (str.startsWith("https://")) {
            str = str.substring(8);
        }
        if (stripSubdomain) {
            if (str.startsWith("www.")) {
                str = str.substring(4);
            }
            if (str.startsWith("m.")) {
                str = str.substring(2);
            }
        }
        int iIndexOf = str.indexOf("/");
        return iIndexOf > 0 ? str.substring(0, iIndexOf) : str;
    }

    public static String extractHost(String str) {
        return extractDomain(str, false);
    }

    public static String getDefaultFavicon(String str) {
        return useDomainOfFirstArg4Second(str, "/favicon.ico");
    }

    public static String getLongestSubstring(String str, String str2) {
        int[] iArrLongestSubstring = longestSubstring(str, str2);
        return (iArrLongestSubstring == null || iArrLongestSubstring[0] >= iArrLongestSubstring[1]) ? "" : str.substring(iArrLongestSubstring[0], iArrLongestSubstring[1]);
    }

    public static String getUrlFromUglyFacebookRedirect(String str) {
        if (str.startsWith("http://www.facebook.com/l.php?u=")) {
            return urlDecode(str.substring(32));
        }
        return null;
    }

    public static String getUrlFromUglyGoogleRedirect(String str) {
        String[] strArrSplit;
        if (!str.startsWith("http://www.google.com/url?") || (strArrSplit = urlDecode(str.substring(26)).split("\\&")) == null) {
            return null;
        }
        for (String str2 : strArrSplit) {
            if (str2.startsWith("q=")) {
                return str2.substring(2);
            }
        }
        return null;
    }

    public static String innerTrim(String str) {
        if (str.isEmpty()) {
            return "";
        }
        StringBuilder sb2 = new StringBuilder();
        boolean sawWhitespace = false;
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == ' ' || cCharAt == '\t' || cCharAt == '\n') {
                sawWhitespace = true;
            } else {
                if (sawWhitespace) {
                    sb2.append(' ');
                }
                sb2.append(cCharAt);
                sawWhitespace = false;
            }
        }
        return sb2.toString().trim();
    }

    public static boolean isApp(String str) {
        return str.endsWith(".exe") || str.endsWith(".bin") || str.endsWith(".bat") || str.endsWith(".dmg");
    }

    public static boolean isAudio(String str) {
        return str.endsWith(".mp3") || str.endsWith(".ogg") || str.endsWith(".m3u") || str.endsWith(".wav");
    }

    public static boolean isDoc(String str) {
        return str.endsWith(".pdf") || str.endsWith(".ppt") || str.endsWith(".doc") || str.endsWith(".swf") || str.endsWith(".rtf") || str.endsWith(".xls");
    }

    public static boolean isImage(String str) {
        return str.endsWith(".png") || str.endsWith(".jpeg") || str.endsWith(".gif") || str.endsWith(".jpg") || str.endsWith(".bmp") || str.endsWith(".ico") || str.endsWith(".eps");
    }

    public static boolean isPackage(String str) {
        return str.endsWith(".gz") || str.endsWith(".tgz") || str.endsWith(".zip") || str.endsWith(".rar") || str.endsWith(".deb") || str.endsWith(".rpm") || str.endsWith(".7z");
    }

    public static boolean isVideo(String str) {
        return str.endsWith(".mpeg") || str.endsWith(".mpg") || str.endsWith(".avi") || str.endsWith(".mov") || str.endsWith(".mpg4") || str.endsWith(".mp4") || str.endsWith(".flv") || str.endsWith(".wmv");
    }

    public static boolean isVideoLink(String str) {
        String strExtractDomain = extractDomain(str, true);
        return strExtractDomain.startsWith("youtube.com") || strExtractDomain.startsWith("video.yahoo.com") || strExtractDomain.startsWith("vimeo.com") || strExtractDomain.startsWith("blip.tv");
    }

    public static int[] longestSubstring(String str, String str2) {
        if (str == null || str.isEmpty() || str2 == null || str2.isEmpty()) {
            return null;
        }
        int[][] lengths = (int[][]) Array.newInstance((Class<?>) int.class, str.length(), str2.length());
        int resultStart = 0;
        int resultEnd = 0;
        int maxLen = 0;
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < str2.length(); j++) {
                if (str.charAt(i) == str2.charAt(j)) {
                    if (i == 0 || j == 0) {
                        lengths[i][j] = 1;
                    } else {
                        lengths[i][j] = lengths[i - 1][j - 1] + 1;
                    }
                    if (lengths[i][j] > maxLen) {
                        maxLen = lengths[i][j];
                        resultStart = (i - lengths[i][j]) + 1;
                        resultEnd = i + 1;
                    }
                }
            }
        }
        return new int[]{resultStart, resultEnd};
    }

    public static String printNode(Element element) {
        return printNode(element, 0);
    }

    public static String removeHashbang(String str) {
        return str.replaceFirst("#!", "");
    }

    public static String replaceSpaces(String str) {
        if (str.isEmpty()) {
            return str;
        }
        String strTrim = str.trim();
        return strTrim.contains(StringUtils.SPACE) ? SPACE_PATTERN.matcher(strTrim).replaceAll("%20") : strTrim;
    }

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    public static String useDomainOfFirstArg4Second(String str, String str2) {
        int truncateIndex;
        if (str2.startsWith("http")) {
            return str2;
        }
        if ("favicon.ico".equals(str2)) {
            str2 = "/favicon.ico";
        }
        if (str2.startsWith("//")) {
            if (str.startsWith("https:")) {
                return "https:" + str2;
            }
            return "http:" + str2;
        }
        if (str2.startsWith("/")) {
            return "http://" + extractHost(str) + str2;
        }
        if (!str2.startsWith("../")) {
            return str2;
        }
        int iLastIndexOf = str.lastIndexOf("/");
        if (iLastIndexOf > 0 && (truncateIndex = iLastIndexOf + 1) < str.length()) {
            str = str.substring(0, truncateIndex);
        }
        return str + str2;
    }

    public static String printNode(Element element, int depth) {
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb2.append(' ');
        }
        sb2.append(element.tagName());
        sb2.append(":");
        sb2.append(element.ownText());
        sb2.append("\n");
        Iterator<Element> it = element.children().iterator();
        while (it.hasNext()) {
            sb2.append(printNode(it.next(), depth + 1));
            sb2.append("\n");
        }
        return sb2.toString();
    }
}
