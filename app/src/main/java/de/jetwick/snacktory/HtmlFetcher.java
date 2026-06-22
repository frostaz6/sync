package de.jetwick.snacktory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: classes3.dex */
public class HtmlFetcher {
    private static final Logger LOG;
    private SCache cache;
    private String referrer = "http://jetsli.de/crawler";
    private String userAgent = "Mozilla/5.0 (compatible; Jetslide; +" + this.referrer + ")";
    private String cacheControl = "max-age=0";
    private String language = "en-us";
    private String accept = "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5";
    private String charset = "UTF-8";
    private AtomicInteger cacheCounter = new AtomicInteger(0);
    private int maxTextLength = -1;
    private ArticleTextExtractor extractor = new ArticleTextExtractor();
    private Set<String> furtherResolveDomains = new ShortenerDomains();

    class ShortenerDomains extends LinkedHashSet<String> {
        ShortenerDomains() {
            add("bit.ly");
            add("cli.gs");
            add("deck.ly");
            add("fb.me");
            add("feedproxy.google.com");
            add("flic.kr");
            add("fur.ly");
            add("goo.gl");
            add("is.gd");
            add("ink.co");
            add("j.mp");
            add("lnkd.in");
            add("on.fb.me");
            add("ow.ly");
            add("plurl.us");
            add("sns.mx");
            add("snurl.com");
            add("su.pr");
            add("t.co");
            add("tcrn.ch");
            add("tl.gd");
            add("tiny.cc");
            add("tinyurl.com");
            add("tmi.me");
            add("tr.im");
            add("twurl.nl");
        }
    }

    static {
        SHelper.enableCookieMgmt();
        SHelper.enableUserAgentOverwrite();
        SHelper.enableAnySSL();
        LOG = LoggerFactory.getLogger(HtmlFetcher.class);
    }

    static String encodeUrl(String str) {
        StringBuilder sb2 = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c < 128) {
                sb2.append(c);
            } else {
                sb2.append(String.format("%%%02X", Integer.valueOf(c)));
            }
        }
        return sb2.toString();
    }

    private static String fixUrl(String str, String str2) {
        return SHelper.useDomainOfFirstArg4Second(str, str2);
    }

    private JResult getFromCache(String str, String str2) throws Exception {
        JResult jResult;
        SCache sCache = this.cache;
        if (sCache == null || (jResult = sCache.get(str)) == null) {
            return null;
        }
        jResult.setUrl(str);
        jResult.setOriginalUrl(str2);
        this.cacheCounter.addAndGet(1);
        return jResult;
    }

    public static void main(String[] strArr) throws Exception {
        String str;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("urls.txt"));
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                bufferedReader.close();
                return;
            }
            int iIndexOf = line.indexOf("\"") + 1;
            String strSubstring = line.substring(iIndexOf, line.indexOf("\"", iIndexOf));
            String strExtractDomain = SHelper.extractDomain(strSubstring, true);
            if (linkedHashSet.contains(strExtractDomain)) {
                str = "2";
            } else {
                linkedHashSet.add(strExtractDomain);
                str = "";
            }
            String strFetchAsString = new HtmlFetcher().fetchAsString(strSubstring, 20000);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(strExtractDomain + str + ".html"));
            bufferedWriter.write(strFetchAsString);
            bufferedWriter.close();
        }
    }

    protected HttpURLConnection createUrlConnection(String str, int timeout, boolean includeAllHeaders) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection(Proxy.NO_PROXY);
        httpURLConnection.setRequestProperty("User-Agent", this.userAgent);
        httpURLConnection.setRequestProperty("Accept", this.accept);
        if (includeAllHeaders) {
            httpURLConnection.setRequestProperty("Accept-Language", this.language);
            httpURLConnection.setRequestProperty("content-charset", this.charset);
            httpURLConnection.addRequestProperty("Referer", this.referrer);
            httpURLConnection.setRequestProperty("Cache-Control", this.cacheControl);
        }
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        httpURLConnection.setConnectTimeout(timeout);
        httpURLConnection.setReadTimeout(timeout);
        return httpURLConnection;
    }

    public HtmlFetcher clearCacheCounter() {
        this.cacheCounter.set(0);
        return this;
    }

    public Converter createConverter(String str) {
        return new Converter(str);
    }

    public JResult fetchAndExtract(String str, int timeout, boolean resolve) throws Exception {
        String strRemoveHashbang = SHelper.removeHashbang(str);
        String urlFromUglyGoogleRedirect = SHelper.getUrlFromUglyGoogleRedirect(strRemoveHashbang);
        if (urlFromUglyGoogleRedirect != null || (urlFromUglyGoogleRedirect = SHelper.getUrlFromUglyFacebookRedirect(strRemoveHashbang)) != null) {
            strRemoveHashbang = urlFromUglyGoogleRedirect;
        }
        if (resolve) {
            JResult jResultD = getFromCache(strRemoveHashbang, str);
            if (jResultD != null) {
                return jResultD;
            }
            String resolvedUrl = getResolvedUrl(strRemoveHashbang, timeout);
            if (resolvedUrl.isEmpty()) {
                Logger logger = LOG;
                if (logger.isDebugEnabled()) {
                    logger.debug("resolved url is empty. Url is: " + strRemoveHashbang);
                }
                JResult jResult = new JResult();
                SCache sCache = this.cache;
                if (sCache != null) {
                    sCache.put(strRemoveHashbang, jResult);
                }
                return jResult.setUrl(strRemoveHashbang);
            }
            if (resolvedUrl.trim().length() > strRemoveHashbang.length()) {
                strRemoveHashbang = SHelper.useDomainOfFirstArg4Second(strRemoveHashbang, resolvedUrl);
            }
        }
        JResult jResultD2 = getFromCache(strRemoveHashbang, str);
        if (jResultD2 != null) {
            return jResultD2;
        }
        JResult jResult2 = new JResult();
        jResult2.setUrl(strRemoveHashbang);
        jResult2.setOriginalUrl(str);
        jResult2.setDate(SHelper.estimateDate(strRemoveHashbang));
        SCache sCache2 = this.cache;
        if (sCache2 != null) {
            sCache2.put(str, jResult2);
            this.cache.put(strRemoveHashbang, jResult2);
        }
        String lowerCase = strRemoveHashbang.toLowerCase();
        if (!SHelper.isDoc(lowerCase) && !SHelper.isApp(lowerCase) && !SHelper.isPackage(lowerCase)) {
            if (SHelper.isVideo(lowerCase) || SHelper.isAudio(lowerCase)) {
                jResult2.setVideoUrl(strRemoveHashbang);
            } else if (SHelper.isImage(lowerCase)) {
                jResult2.setImageUrl(strRemoveHashbang);
            } else {
                this.extractor.extractContent(jResult2, fetchAsString(strRemoveHashbang, timeout));
                if (jResult2.getFaviconUrl().isEmpty()) {
                    jResult2.setFaviconUrl(SHelper.getDefaultFavicon(strRemoveHashbang));
                }
                jResult2.setFaviconUrl(fixUrl(strRemoveHashbang, jResult2.getFaviconUrl()));
                jResult2.setImageUrl(fixUrl(strRemoveHashbang, jResult2.getImageUrl()));
                jResult2.setVideoUrl(fixUrl(strRemoveHashbang, jResult2.getVideoUrl()));
                jResult2.setRssUrl(fixUrl(strRemoveHashbang, jResult2.getRssUrl()));
            }
        }
        jResult2.setText(lessText(jResult2.getText()));
        synchronized (jResult2) {
            jResult2.notifyAll();
        }
        return jResult2;
    }

    public String fetchAsString(String str, int timeout) throws IOException {
        return fetchAsString(str, timeout, true);
    }

    public String getAccept() {
        return this.accept;
    }

    public SCache getCache() {
        return this.cache;
    }

    public String getCacheControl() {
        return this.cacheControl;
    }

    public int getCacheCounter() {
        return this.cacheCounter.get();
    }

    public String getCharset() {
        return this.charset;
    }

    public ArticleTextExtractor getExtractor() {
        return this.extractor;
    }

    public String getLanguage() {
        return this.language;
    }

    public int getMaxTextLength() {
        return this.maxTextLength;
    }

    public String getReferrer() {
        return this.referrer;
    }

    public java.lang.String getResolvedUrl(java.lang.String str, int timeout) {
        String headerField = null;
        int responseCode = -1;
        try {
            HttpURLConnection httpURLConnection = createUrlConnection(str, timeout, true);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod("HEAD");
            httpURLConnection.connect();
            responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.getInputStream().close();
            if (responseCode == 200) {
                Logger logger = LOG;
                if (logger.isDebugEnabled()) {
                    logger.debug(responseCode + " url:" + str + " resolved:" + ((String) null));
                }
                return str;
            }
            headerField = httpURLConnection.getHeaderField("Location");
            if (responseCode / 100 == 3 && headerField != null) {
                headerField = headerField.replaceAll(" ", "+");
                if (str.startsWith("http://bit.ly") || str.startsWith("http://is.gd")) {
                    headerField = encodeUrl(headerField);
                }
                if (this.furtherResolveDomains.contains(SHelper.extractDomain(headerField, true))) {
                    headerField = getResolvedUrl(headerField, timeout);
                }
                Logger logger2 = LOG;
                if (logger2.isDebugEnabled()) {
                    logger2.debug(responseCode + " url:" + str + " resolved:" + headerField);
                }
                return headerField;
            }
            Logger logger3 = LOG;
            if (logger3.isDebugEnabled()) {
                logger3.debug(responseCode + " url:" + str + " resolved:" + headerField);
            }
            return str;
        } catch (Exception e) {
            Logger logger4 = LOG;
            logger4.warn("getResolvedUrl:" + str + " Error:" + e.getMessage());
            String str2 = "";
            if (logger4.isDebugEnabled()) {
                logger4.debug(responseCode + " url:" + str + " resolved:" + headerField);
            }
            return str2;
        }
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String lessText(String str) {
        if (str == null) {
            return "";
        }
        if (this.maxTextLength < 0) {
            return str;
        }
        int length = str.length();
        int maxLength = this.maxTextLength;
        return length > maxLength ? str.substring(0, maxLength) : str;
    }

    public void setAccept(String str) {
        this.accept = str;
    }

    public HtmlFetcher setCache(SCache sCache) {
        this.cache = sCache;
        return this;
    }

    public void setCacheControl(String str) {
        this.cacheControl = str;
    }

    public void setCharset(String str) {
        this.charset = str;
    }

    public void setExtractor(ArticleTextExtractor articleTextExtractor) {
        this.extractor = articleTextExtractor;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public HtmlFetcher setMaxTextLength(int maxTextLength) {
        this.maxTextLength = maxTextLength;
        return this;
    }

    public HtmlFetcher setReferrer(String str) {
        this.referrer = str;
        return this;
    }

    public void setUserAgent(String str) {
        this.userAgent = str;
    }

    public String fetchAsString(String str, int timeout, boolean includeAllHeaders) throws IOException {
        HttpURLConnection httpURLConnectionA = createUrlConnection(str, timeout, includeAllHeaders);
        httpURLConnectionA.setInstanceFollowRedirects(true);
        String contentEncoding = httpURLConnectionA.getContentEncoding();
        String strStreamToString = createConverter(str).streamToString((contentEncoding == null || !contentEncoding.equalsIgnoreCase("gzip")) ? (contentEncoding == null || !contentEncoding.equalsIgnoreCase("deflate")) ? httpURLConnectionA.getInputStream() : new InflaterInputStream(httpURLConnectionA.getInputStream(), new Inflater(true)) : new GZIPInputStream(httpURLConnectionA.getInputStream()), Converter.extractEncoding(httpURLConnectionA.getContentType()));
        Logger logger = LOG;
        if (logger.isDebugEnabled()) {
            logger.debug(strStreamToString.length() + " FetchAsString:" + str);
        }
        return strStreamToString;
    }
}
