package de.jetwick.snacktory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: classes3.dex */
public class ArticleTextExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleTextExtractor.class);
    private static final Pattern NODES_PATTERN = Pattern.compile("p|div|td|h1|h2|article|section");
    private static final Pattern HIDDEN_PATTERN = Pattern.compile("hidden|display: ?none|font-size: ?small");
    private static final Set<String> IGNORED_TITLE_PARTS = new IgnoredTitleParts();
    private static final OutputFormatter DEFAULT_FORMATTER = new OutputFormatter();
    private String unlikely;
    private Pattern unlikelyPattern;
    private String positive;
    private Pattern positivePattern;
    private String negative;
    private Pattern negativePattern;
    private OutputFormatter formatter = DEFAULT_FORMATTER;

    public class ImageComparator implements Comparator<ImageResult> {
        public ImageComparator() {
        }

        @Override // java.util.Comparator
        public int compare(ImageResult imageResult, ImageResult imageResult2) {
            return imageResult2.weight.compareTo(imageResult.weight);
        }
    }

    static class IgnoredTitleParts extends LinkedHashSet<String> {
        IgnoredTitleParts() {
            add("hacker news");
            add("facebook");
        }
    }

    public ArticleTextExtractor() {
        setUnlikely("com(bx|ment|munity)|dis(qus|cuss)|e(xtra|[-]?mail)|foot|header|menu|re(mark|ply)|rss|sh(are|outbox)|sponsora(d|ll|gegate|rchive|ttachment)|(pag(er|ination))|popup|print|login|si(debar|gn|ngle)");
        setPositive("(^(body|content|h?entry|main|page|post|text|blog|story|haupt))|arti(cle|kel)|instapaper_body");
        setNegative("nav($|igation)|user|com(ment|bx)|(^com-)|contact|foot|masthead|(me(dia|ta))|outbrain|promo|related|scroll|(sho(utbox|pping))|sidebar|sponsor|tags|tool|widget|player|disclaimer|toc|infobox|vcard");
    }

    private int getWeight(Element element) {
        int weight = this.positivePattern.matcher(element.className()).find() ? 35 : 0;
        if (this.positivePattern.matcher(element.id()).find()) {
            weight += 40;
        }
        if (this.unlikelyPattern.matcher(element.className()).find()) {
            weight -= 20;
        }
        if (this.unlikelyPattern.matcher(element.id()).find()) {
            weight -= 20;
        }
        if (this.negativePattern.matcher(element.className()).find()) {
            weight -= 50;
        }
        if (this.negativePattern.matcher(element.id()).find()) {
            weight -= 50;
        }
        String strAttr = element.attr("style");
        return (strAttr == null || strAttr.isEmpty() || !HIDDEN_PATTERN.matcher(strAttr).find()) ? weight : weight - 50;
    }

    private int calcWeightForChild(Element element, String str) {
        int iRound = ((SHelper.count(str, "&quot;") + SHelper.count(str, "&lt;")) + SHelper.count(str, "&gt;")) + SHelper.count(str, "px") > 5 ? -30 : (int) Math.round(((double) str.length()) / 25.0d);
        addScore(element, iRound);
        return iRound;
    }

    private boolean isAdImage(String str) {
        return SHelper.count(str, "ad") >= 2;
    }

    private Document removeScriptsAndStyles(Document document) {
        Iterator<Element> it = document.getElementsByTag("script").iterator();
        while (it.hasNext()) {
            it.next().remove();
        }
        Iterator<Element> it2 = document.getElementsByTag("noscript").iterator();
        while (it2.hasNext()) {
            it2.next().remove();
        }
        Iterator<Element> it3 = document.getElementsByTag("style").iterator();
        while (it3.hasNext()) {
            it3.next().remove();
        }
        return document;
    }

    public ArticleTextExtractor addNegative(String str) {
        setNegative(this.negative + "|" + str);
        return this;
    }

    public ArticleTextExtractor addPositive(String str) {
        return setPositive(this.positive + "|" + str);
    }

    public void addScore(Element element, int scoreToAdd) {
        setScore(element, scoreToAdd + getScore(element));
    }

    public ArticleTextExtractor addUnlikely(String str) {
        return setUnlikely(this.unlikely + "|" + str);
    }

    protected String getCanonicalUrl(Document document) {
        String strReplaceSpaces = SHelper.replaceSpaces(document.select("head link[rel=canonical]").attr("href"));
        if (!strReplaceSpaces.isEmpty()) {
            return strReplaceSpaces;
        }
        String strReplaceSpaces2 = SHelper.replaceSpaces(document.select("head meta[property=og:url]").attr("content"));
        return strReplaceSpaces2.isEmpty() ? SHelper.replaceSpaces(document.select("head meta[name=twitter:url]").attr("content")) : strReplaceSpaces2;
    }

    public String cleanTitle(String str) {
        StringBuilder sb2 = new StringBuilder();
        int index = 0;
        String[] parts = str.split("\\|");
        for (String str2 : parts) {
            if (!IGNORED_TITLE_PARTS.contains(str2.toLowerCase().trim()) && (index != parts.length - 1 || sb2.length() <= str2.length())) {
                if (index > 0) {
                    sb2.append("|");
                }
                sb2.append(str2);
                index++;
            }
        }
        return SHelper.innerTrim(sb2.toString());
    }

    protected String getDescription(Document document) {
        String strInnerTrim = SHelper.innerTrim(document.select("head meta[name=description]").attr("content"));
        if (!strInnerTrim.isEmpty()) {
            return strInnerTrim;
        }
        String strInnerTrim2 = SHelper.innerTrim(document.select("head meta[property=og:description]").attr("content"));
        return strInnerTrim2.isEmpty() ? SHelper.innerTrim(document.select("head meta[name=twitter:description]").attr("content")) : strInnerTrim2;
    }

    public Element determineImageSource(Element element, List<ImageResult> list) {
        int score;
        int height;
        int width;
        boolean noFollow;
        String rel;
        Elements elementsSelect = element.select("img");
        if (elementsSelect.isEmpty()) {
            elementsSelect = element.parent().select("img");
        }
        double weight = 1.0d;
        Element element2 = null;
        int maxScore = 0;
        for (Element element3 : elementsSelect) {
            String strAttr2 = element3.attr("src");
            if (!strAttr2.isEmpty() && !isAdImage(strAttr2)) {
                try {
                    int parsedHeight = Integer.parseInt(element3.attr("height"));
                    score = parsedHeight >= 50 ? 20 : -20;
                    height = parsedHeight;
                } catch (Exception unused) {
                    score = 0;
                    height = 0;
                }
                try {
                    int parsedWidth = Integer.parseInt(element3.attr("width"));
                    score = parsedWidth >= 50 ? score + 20 : score - 20;
                    width = parsedWidth;
                } catch (Exception unused2) {
                    width = 0;
                }
                String strAttr3 = element3.attr("alt");
                if (strAttr3.length() > 35) {
                    score += 20;
                }
                String strAttr4 = element3.attr("title");
                if (strAttr4.length() > 35) {
                    score += 20;
                }
                if (element3.parent() == null || (rel = element3.parent().attr("rel")) == null || !rel.contains("nofollow")) {
                    noFollow = false;
                } else {
                    score -= 40;
                    noFollow = rel.contains("nofollow");
                }
                int weightedScore = (int) (((double) score) * weight);
                if (weightedScore > maxScore) {
                    weight /= 2.0d;
                    element2 = element3;
                    maxScore = weightedScore;
                }
                list.add(new ImageResult(strAttr2, Integer.valueOf(weightedScore), strAttr4, height, width, strAttr3, noFollow));
            }
        }
        Collections.sort(list, new ImageComparator());
        return element2;
    }

    protected String getFavicon(Document document) {
        String strReplaceSpaces = SHelper.replaceSpaces(document.select("head link[rel=icon]").attr("href"));
        return strReplaceSpaces.isEmpty() ? SHelper.replaceSpaces(document.select("head link[rel^=shortcut],link[rel$=icon]").attr("href")) : strReplaceSpaces;
    }

    public JResult extractContent(String str) throws Exception {
        return extractContent(new JResult(), str);
    }

    protected String getImageUrl(Document document) {
        String strReplaceSpaces = SHelper.replaceSpaces(document.select("head meta[property=og:image]").attr("content"));
        if (!strReplaceSpaces.isEmpty()) {
            return strReplaceSpaces;
        }
        String strReplaceSpaces2 = SHelper.replaceSpaces(document.select("head meta[name=twitter:image]").attr("content"));
        if (!strReplaceSpaces2.isEmpty()) {
            return strReplaceSpaces2;
        }
        String strReplaceSpaces3 = SHelper.replaceSpaces(document.select("link[rel=image_src]").attr("href"));
        return strReplaceSpaces3.isEmpty() ? SHelper.replaceSpaces(document.select("head meta[name=thumbnail]").attr("content")) : strReplaceSpaces3;
    }

    protected Collection<String> getKeywords(Document document) {
        String strInnerTrim = SHelper.innerTrim(document.select("head meta[name=keywords]").attr("content"));
        if (strInnerTrim != null) {
            if (strInnerTrim.startsWith("[") && strInnerTrim.endsWith("]")) {
                strInnerTrim = strInnerTrim.substring(1, strInnerTrim.length() - 1);
            }
            String[] strArrSplit = strInnerTrim.split("\\s*,\\s*");
            if (strArrSplit.length > 1 || (strArrSplit.length > 0 && !"".equals(strArrSplit[0]))) {
                return Arrays.asList(strArrSplit);
            }
        }
        return Collections.emptyList();
    }

    public Collection<Element> getNodes(Document document) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(64);
        int score = 100;
        for (Element element : document.select("body").select("*")) {
            if (NODES_PATTERN.matcher(element.tagName()).matches()) {
                linkedHashMap.put(element, null);
                setScore(element, score);
                score /= 2;
            }
        }
        return linkedHashMap.keySet();
    }

    public int getScore(Element element) {
        try {
            return Integer.parseInt(element.attr("gravityScore"));
        } catch (Exception unused) {
            return 0;
        }
    }

    protected String getRssUrl(Document document) {
        return SHelper.replaceSpaces(document.select("link[rel=alternate]").select("link[type=application/rss+xml]").attr("href"));
    }

    protected String getTitle(Document document) {
        String strCleanTitle = cleanTitle(document.title());
        if (!strCleanTitle.isEmpty()) {
            return strCleanTitle;
        }
        String strInnerTrim = SHelper.innerTrim(document.select("head title").text());
        if (!strInnerTrim.isEmpty()) {
            return strInnerTrim;
        }
        String strInnerTrim2 = SHelper.innerTrim(document.select("head meta[name=title]").attr("content"));
        if (!strInnerTrim2.isEmpty()) {
            return strInnerTrim2;
        }
        String strInnerTrim3 = SHelper.innerTrim(document.select("head meta[property=og:title]").attr("content"));
        return strInnerTrim3.isEmpty() ? SHelper.innerTrim(document.select("head meta[name=twitter:title]").attr("content")) : strInnerTrim3;
    }

    protected String getVideoUrl(Document document) {
        return SHelper.replaceSpaces(document.select("head meta[property=og:video]").attr("content"));
    }

    protected int getWeightAndChildren(Element element) {
        return getWeight(element) + ((int) Math.round((((double) element.ownText().length()) / 100.0d) * 10.0d)) + weightChildNodes(element);
    }

    protected void prepareDocument(Document document) {
        removeScriptsAndStyles(document);
    }

    protected int weightChildNodes(Element element) {
        ArrayList arrayList = new ArrayList(5);
        int weight = 0;
        Element element2 = null;
        for (Element element3 : element.children()) {
            String strOwnText = element3.ownText();
            int length = strOwnText.length();
            if (length >= 20) {
                if (length > 200) {
                    weight += Math.max(50, length / 10);
                }
                if (element3.tagName().equals("h1") || element3.tagName().equals("h2")) {
                    weight += 30;
                } else if (element3.tagName().equals("div") || element3.tagName().equals("p")) {
                    weight += calcWeightForChild(element3, strOwnText);
                    if (element3.tagName().equals("p") && length > 50) {
                        arrayList.add(element3);
                    }
                    if (element3.className().toLowerCase().equals("caption")) {
                        element2 = element3;
                    }
                }
            }
        }
        if (element2 != null) {
            weight += 30;
        }
        if (arrayList.size() >= 2) {
            for (Element element4 : element.children()) {
                if ("h1;h2;h3;h4;h5;h6".contains(element4.tagName())) {
                    weight += 20;
                } else if ("table;li;td;th".contains(element4.tagName())) {
                    addScore(element4, -30);
                }
                if ("p".contains(element4.tagName())) {
                    addScore(element4, 30);
                }
            }
        }
        return weight;
    }

    public String removeTitleFromText(String str, String str2) {
        return str;
    }

    public ArticleTextExtractor setNegative(String str) {
        this.negative = str;
        this.negativePattern = Pattern.compile(str);
        return this;
    }

    public void setOutputFormatter(OutputFormatter outputFormatter) {
        this.formatter = outputFormatter;
    }

    public ArticleTextExtractor setPositive(String str) {
        this.positive = str;
        this.positivePattern = Pattern.compile(str);
        return this;
    }

    public void setScore(Element element, int score) {
        element.attr("gravityScore", Integer.toString(score));
    }

    public ArticleTextExtractor setUnlikely(String str) {
        this.unlikely = str;
        this.unlikelyPattern = Pattern.compile(str);
        return this;
    }

    public JResult extractContent(JResult jResult, String str) throws Exception {
        return extractContent(jResult, str, this.formatter);
    }

    public JResult extractContent(JResult jResult, String str, OutputFormatter outputFormatter) throws Exception {
        if (!str.isEmpty()) {
            return extractContent(jResult, Jsoup.parse(str), outputFormatter);
        }
        throw new IllegalArgumentException("html string is empty!?");
    }

    public JResult extractContent(JResult jResult, Document document, OutputFormatter outputFormatter) throws Exception {
        Objects.requireNonNull(document, "missing document");
        jResult.setTitle(getTitle(document));
        jResult.setDescription(getDescription(document));
        jResult.setCanonicalUrl(getCanonicalUrl(document));
        prepareDocument(document);
        int maxWeight = 0;
        Element element = null;
        for (Element element2 : getNodes(document)) {
            int weight = getWeightAndChildren(element2);
            if (weight > maxWeight) {
                element = element2;
                if (weight > 200) {
                    break;
                }
                maxWeight = weight;
            }
        }
        if (element != null) {
            ArrayList arrayList = new ArrayList();
            Element elementDetermineImageSource = determineImageSource(element, arrayList);
            if (elementDetermineImageSource != null) {
                jResult.setImageUrl(SHelper.replaceSpaces(elementDetermineImageSource.attr("src")));
                jResult.setImages(arrayList);
            }
            String strRemoveTitleFromText = removeTitleFromText(outputFormatter.getFormattedText(element), jResult.getTitle());
            if (strRemoveTitleFromText.length() > jResult.getTitle().length()) {
                jResult.setText(strRemoveTitleFromText);
            }
        }
        if (jResult.getImageUrl().isEmpty()) {
            jResult.setImageUrl(getImageUrl(document));
        }
        jResult.setRssUrl(getRssUrl(document));
        jResult.setVideoUrl(getVideoUrl(document));
        jResult.setFaviconUrl(getFavicon(document));
        jResult.setKeywords(getKeywords(document));
        return jResult;
    }
}
