package de.jetwick.snacktory;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import okhttp3.internal.http.HttpStatusCodesKt;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/* JADX INFO: loaded from: classes3.dex */
public class OutputFormatter {
    public static final int MIN_PARAGRAPH_TEXT = 50;
    private static final List<String> NODES_TO_REPLACE = Arrays.asList("strong", "b", "i");
    private Pattern unlikelyPattern;
    protected final int minParagraphText;
    protected final List<String> nodesToReplace;
    protected String nodesToKeepCssSelector;

    public OutputFormatter() {
        this(50, NODES_TO_REPLACE);
    }

    protected void append(Element element, StringBuilder sb2, String str) {
        for (Element element2 : element.select(str)) {
            for (Element elementParent = element2; elementParent != null && !elementParent.equals(element); elementParent = elementParent.parent()) {
                if (isUnlikely(elementParent)) {
                    break;
                }
            }
            String nodeText = node2Text(element2);
            if (!nodeText.isEmpty() && nodeText.length() >= this.minParagraphText && nodeText.length() <= SHelper.countLetters(nodeText) * 2) {
                sb2.append(nodeText);
                sb2.append("\n\n");
            }
        }
    }

    public OutputFormatter appendUnlikelyPattern(String str) {
        return setUnlikelyPattern(this.unlikelyPattern.toString() + "|" + str);
    }

    void appendTextSkipHidden(Element element, StringBuilder sb2) {
        for (Node node : element.childNodes()) {
            if (!isUnlikely(node)) {
                if (node instanceof TextNode) {
                    sb2.append(((TextNode) node).text());
                } else if (node instanceof Element) {
                    Element element2 = (Element) node;
                    if ((sb2.length() > 0 && element2.isBlock() && !lastCharIsWhitespace(sb2)) || element2.tagName().equals("br")) {
                        sb2.append(StringUtils.SPACE);
                    }
                    appendTextSkipHidden(element2, sb2);
                }
            }
        }
    }

    boolean lastCharIsWhitespace(StringBuilder sb2) {
        if (sb2.length() == 0) {
            return false;
        }
        return Character.isWhitespace(sb2.charAt(sb2.length() - 1));
    }

    protected String node2Text(Element element) {
        StringBuilder sb2 = new StringBuilder(HttpStatusCodesKt.HTTP_OK);
        appendTextSkipHidden(element, sb2);
        return sb2.toString();
    }

    protected void removeNodesWithNegativeScores(Element element) {
        for (Element element2 : element.select("*[gravityScore]")) {
            if (Integer.parseInt(element2.attr("gravityScore")) < 0 || element2.text().length() < this.minParagraphText) {
                element2.remove();
            }
        }
    }

    boolean isUnlikely(Node node) {
        if (node.attr("class") == null || !node.attr("class").toLowerCase().contains("caption")) {
            return this.unlikelyPattern.matcher(node.attr("style")).find() || this.unlikelyPattern.matcher(node.attr("class")).find();
        }
        return true;
    }

    public String getFormattedText(Element element) {
        removeNodesWithNegativeScores(element);
        StringBuilder sb2 = new StringBuilder();
        append(element, sb2, this.nodesToKeepCssSelector);
        String strInnerTrim = SHelper.innerTrim(sb2.toString());
        if (strInnerTrim.length() > 100) {
            return strInnerTrim;
        }
        if (strInnerTrim.isEmpty() || (!element.text().isEmpty() && strInnerTrim.length() <= element.ownText().length())) {
            strInnerTrim = element.text();
        }
        return Jsoup.parse(strInnerTrim).text();
    }

    public void setNodesToKeepCssSelector(String str) {
        this.nodesToKeepCssSelector = str;
    }

    public OutputFormatter setUnlikelyPattern(String str) {
        this.unlikelyPattern = Pattern.compile(str);
        return this;
    }

    public OutputFormatter(int minParagraphText) {
        this(minParagraphText, NODES_TO_REPLACE);
    }

    public OutputFormatter(int minParagraphText, List<String> list) {
        this.unlikelyPattern = Pattern.compile("display\\:none|visibility\\:hidden");
        this.nodesToKeepCssSelector = "p";
        this.minParagraphText = minParagraphText;
        this.nodesToReplace = list;
    }
}
