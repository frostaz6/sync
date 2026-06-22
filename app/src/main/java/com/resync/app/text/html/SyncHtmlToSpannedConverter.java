package com.resync.app.text.html;

import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import androidx.core.graphics.ColorUtils;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import java.io.IOException;
import java.io.StringReader;
import com.resync.app.text.html.ReferencedImages;
import org.apache.commons.lang3.StringUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import com.resync.app.media.images.ImageHelper;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.text.spans.CodeBackgroundColorSpan;
import com.resync.app.text.spans.CustomBackgroundColorSpan;
import com.resync.app.text.spans.CustomUrlSpan;
import com.resync.app.text.spans.DivSpan;
import com.resync.app.text.spans.NewCustomTypefaceSpan;
import com.resync.app.text.spans.PaddingSpan;
import com.resync.app.text.spans.SpoilerSpan;
import com.resync.app.text.spans.TempUnderlineSpan;
import com.resync.app.text.spans.EmoteImageSpan;
import com.resync.app.text.spans.InlineImageSpan;
import com.resync.app.text.spans.VectorInlineImageSpan;
import com.resync.app.text.spans.WebPreviewImageSpan;
import com.resync.app.text.spans.RedditQuoteSpan;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.util.Logger;
import com.resync.app.util.WebsitePreviewHelper;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: compiled from: SyncHtmlToSpannedConverter.java */
/* JADX INFO: loaded from: classes3.dex */
public class SyncHtmlToSpannedConverter implements ContentHandler {

    private static final float[] HEADING_SIZES = {1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1.0f};
    private static final int LIST_INDENT = SizeHelper.dpToPx(8);

    private String source;
    private XMLReader reader;
    private SpannableTextViewStringBuilder builder;
    private Options options;
    private ReferencedImages referencedImages;
    private int tableIndex;
    private int spoilerIndex;
    private int flags;
    boolean inOrderedList = false;
    boolean inUnorderedList = false;
    int blockquoteDepth = 0;

    private static class Big {
        private Big() {
        }
    }

    private static class Blockquote {
        private Blockquote() {
        }
    }

    private static class Bold {
        private Bold() {
        }
    }

    private static class Code {
        private Code() {
        }
    }

    private static class Samp {
        private Samp() {
        }
    }

    private static class Heading {
        private int level;

        public Heading(int level) {
            this.level = level;
        }
    }

    private static class Href {
        public String href;

        public Href(String href) {
            this.href = href;
        }
    }

    private static class Italic {
        private Italic() {
        }
    }

    private static class ListMarker {
        private boolean ordered;
        private int counter = 0;

        public ListMarker(boolean ordered) {
            this.ordered = ordered;
        }

        public String next() {
            this.counter++;
            return this.counter + ". ";
        }
    }

    private static class ListItem {
        private ListItem() {
        }
    }

    private static class Tt {
        private Tt() {
        }
    }

    private static class Newline {
        private int count;

        public Newline(int count) {
            this.count = count;
        }
    }

    private static class Small {
        private Small() {
        }
    }

    private static class Spoiler {
        private Spoiler() {
        }
    }

    private static class Strike {
        private Strike() {
        }
    }

    private static class Sub {
        private Sub() {
        }
    }

    private static class Sup {
        private Sup() {
        }
    }

    private static class Table {
        private Table() {
        }
    }

    private static class Underline {
        private Underline() {
        }
    }

    public SyncHtmlToSpannedConverter(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Options options, String source, Parser parser_h) {
        this.builder = spannableTextViewStringBuilder;
        if (options != null) {
            this.options = options;
        } else {
            this.options = new Options();
        }
        this.source = source;
        this.referencedImages = new ReferencedImages();
        this.reader = parser_h;
        this.flags = 0;
    }

    private static void appendGiphy(SpannableTextViewStringBuilder spannableTextViewStringBuilder, String giphyId) {
        String gifUrl = "https://media.giphy.com/media/" + giphyId + "/100.gif";
        spannableTextViewStringBuilder.appendWithSpans("￼", new InlineImageSpan(gifUrl), new CustomUrlSpan(gifUrl));
        spannableTextViewStringBuilder.append("\n");
    }

    private static void appendNewlines(SpannableTextViewStringBuilder spannableTextViewStringBuilder, int minNewlines) {
        int length = spannableTextViewStringBuilder.length();
        if (length == 0) {
            return;
        }
        int existingNewlines = 0;
        for (int index = length - 1; index >= 0 && spannableTextViewStringBuilder.charAt(index) == '\n'; index--) {
            existingNewlines++;
        }
        while (existingNewlines < minNewlines) {
            spannableTextViewStringBuilder.append("\n");
            existingNewlines++;
        }
    }

    private static void endSpanWith(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Class cls, Object... objArr) {
        SpannableTextViewStringBuilder.Span mark = getMark(spannableTextViewStringBuilder, cls);
        if (mark != null) {
            applySpans(spannableTextViewStringBuilder, mark, objArr);
        }
    }

    private static void handleLinkEnd(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Options options, ReferencedImages referencedImages) {
        SpannableTextViewStringBuilder.Span hrefMark = getMark(spannableTextViewStringBuilder, Href.class);
        if (getMark(spannableTextViewStringBuilder, Spoiler.class) != null) {
            spannableTextViewStringBuilder.removeSpan(hrefMark);
            return;
        }
        if (hrefMark == null) {
            return;
        }
        Href hrefSpan = (Href) hrefMark.span;
        String href = hrefSpan.href;
        if (href == null) {
            return;
        }
        String strText = spannableTextViewStringBuilder.substringFrom(hrefMark);
        if (StringUtils.startsWith(href, "sync-settings")) {
            spannableTextViewStringBuilder.cutFrom(hrefMark);
            spannableTextViewStringBuilder.removeSpan(hrefMark);
            spannableTextViewStringBuilder.trimTrailingNewlines();
            if (spannableTextViewStringBuilder.length() > 0) {
                spannableTextViewStringBuilder.append("\n");
            }
            spannableTextViewStringBuilder.appendWithSpans("￼", new VectorInlineImageSpan(R.drawable.outline_settings_24, strText, null), new CustomUrlSpan(href));
            spannableTextViewStringBuilder.append("\n\n");
            return;
        }
        if (StringUtils.startsWith(href, "translate")) {
            spannableTextViewStringBuilder.cutFrom(hrefMark);
            spannableTextViewStringBuilder.removeSpan(hrefMark);
            spannableTextViewStringBuilder.trimTrailingNewlines();
            spannableTextViewStringBuilder.append("\n\n");
            int iWidth = (int) (SizeHelper.dpToPx(366) * 0.4f);
            int iHeight = (int) (SizeHelper.dpToPx(48) * 0.4f);
            String strTranslateImg = NightHelper.isNight() ? "https://todo.syncforreddit.com/translate_white.png" : "https://todo.syncforreddit.com/translate_grey.png";
            spannableTextViewStringBuilder.appendWithSpans("￼", new EmoteImageSpan(strTranslateImg, iWidth, iHeight), new CustomUrlSpan("https://translate.google.com/"));
            return;
        }
        if (href.contains("preview.redd.it") && href.contains("width") && href.contains("height") && options.width > 0) {
            android.net.Uri uri = android.net.Uri.parse(href);
            int iSrcWidth = Integer.parseInt(uri.getQueryParameter("width"));
            int iSrcHeight = Integer.parseInt(uri.getQueryParameter("height"));
            String strImgUrl = href.substring(0, href.indexOf("&height"));
            spannableTextViewStringBuilder.cutFrom(hrefMark);
            spannableTextViewStringBuilder.removeSpan(hrefMark);
            int iTargetWidth = options.width;
            float fScale = iTargetWidth / (float) iSrcWidth;
            int iTargetHeight = (int) (fScale * iSrcHeight);
            if (iSrcHeight / (float) iSrcWidth > 0.2f && iSrcWidth < 2000 && iSrcHeight < 2000) {
                spannableTextViewStringBuilder.appendWithSpans("￼", new EmoteImageSpan(strImgUrl, iTargetWidth, iTargetHeight), new CustomUrlSpan(strImgUrl));
            } else if (SettingsSingleton.getSettings().largeLinkSources) {
                spannableTextViewStringBuilder.appendWithSpans(strImgUrl, new CustomUrlSpan(strImgUrl), new RelativeSizeSpan(1.25f));
            } else {
                spannableTextViewStringBuilder.appendWithSpans(strImgUrl, new CustomUrlSpan(strImgUrl));
            }
            return;
        }
        if (SettingsSingleton.getSettings().largeLinkSources) {
            applySpans(spannableTextViewStringBuilder, hrefMark, new CustomUrlSpan(hrefSpan.href), new RelativeSizeSpan(1.25f), new TempUnderlineSpan());
        } else {
            applySpans(spannableTextViewStringBuilder, hrefMark, new CustomUrlSpan(hrefSpan.href), new TempUnderlineSpan());
        }
        boolean addedPreview = false;
        if (WebsitePreviewHelper.shouldShowRedditQuote(href) && options.showWebPreviews && options.width > 0) {
            // Forum-style inline quote of the linked reddit comment/thread. Rendered on its own line
            // below the comment text (same referenced-image mechanism as web previews); starts collapsed
            // and fetches async — on failure it shows no box and the plain link above remains tappable.
            spannableTextViewStringBuilder.appendWithSpans(referencedImages.register(new ReferencedImages.Ref(new RedditQuoteSpan(href, options.width), new CustomUrlSpan(href))), new SuperscriptSpan(), new RelativeSizeSpan(0.75f));
            addedPreview = true;
        }
        if (!addedPreview && WebsitePreviewHelper.shouldShowPreview(href) && options.showWebPreviews && options.width > 0) {
            int iWebPreviewCount = 0;
            for (int i = 0; i < referencedImages.size(); i++) {
                if (((ReferencedImages.Ref) referencedImages.get(i)).span instanceof WebPreviewImageSpan) {
                    iWebPreviewCount++;
                }
            }
            if (iWebPreviewCount < 5) {
                spannableTextViewStringBuilder.appendWithSpans(referencedImages.register(new ReferencedImages.Ref(new WebPreviewImageSpan(href, options.width, referencedImages.size() + 1), new CustomUrlSpan(href))), new SuperscriptSpan(), new RelativeSizeSpan(0.75f));
                addedPreview = true;
            }
        }
        if (options.showInlineImages && SettingsSingleton.getSettings().inlineImagePreviews && !addedPreview) {
            boolean isInlineImage;
            if ((LinkTypeHelper.isImage(href) || ImgurImageHelper.isAlbum(href)) && (StringUtils.containsAny(href, ".jpg", ".jpeg", ".png", ".mp4", ".gif") || LinkTypeHelper.isImgur(href) || LinkTypeHelper.isGfycat(href))) {
                isInlineImage = true;
            } else {
                isInlineImage = false;
            }
            if (LinkTypeHelper.isYouTube(href)) {
                isInlineImage = true;
            }
            if (href.contains("tenor.com")) {
                isInlineImage = true;
            }
            if (isInlineImage) {
                spannableTextViewStringBuilder.appendWithSpans(referencedImages.register(new ReferencedImages.Ref(new InlineImageSpan(href, referencedImages.size() + 1), new CustomUrlSpan(href))), new SuperscriptSpan(), new RelativeSizeSpan(0.75f));
                addedPreview = true;
            }
        }
        if (SettingsSingleton.getSettings().commentsLinksExpanded && options.showInlineImages) {
            if (LinkTypeHelper.isRedditUser(href)) {
                spannableTextViewStringBuilder.appendWithSpans(referencedImages.register(new ReferencedImages.Ref(new VectorInlineImageSpan(R.drawable.outline_person_24, "u/" + com.resync.app.util.links.LinkHandler.getUsername(href), referencedImages.size() + 1), new CustomUrlSpan(href))), new SuperscriptSpan(), new RelativeSizeSpan(0.75f));
            } else if (LinkTypeHelper.isWiki(href)) {
                spannableTextViewStringBuilder.appendWithSpans(referencedImages.register(new ReferencedImages.Ref(new VectorInlineImageSpan(R.drawable.outline_text_fields_24, strText, referencedImages.size() + 1), new CustomUrlSpan(href))), new SuperscriptSpan(), new RelativeSizeSpan(0.75f));
            } else if (LinkTypeHelper.isSubreddit(href)) {
                spannableTextViewStringBuilder.appendWithSpans(referencedImages.register(new ReferencedImages.Ref(new VectorInlineImageSpan(R.drawable.ic_basic_subreddit, "r/" + com.resync.app.util.links.LinkHandler.getSubreddit(href), referencedImages.size() + 1), new CustomUrlSpan(href))), new SuperscriptSpan(), new RelativeSizeSpan(0.75f));
            } else if (LinkTypeHelper.isComments(href)) {
                spannableTextViewStringBuilder.appendWithSpans(referencedImages.register(new ReferencedImages.Ref(new VectorInlineImageSpan(R.drawable.outline_mode_comment_24, href, referencedImages.size() + 1), new CustomUrlSpan(href))), new SuperscriptSpan(), new RelativeSizeSpan(0.75f));
            } else if (!LinkTypeHelper.isImage(href) && !addedPreview) {
                spannableTextViewStringBuilder.appendWithSpans(referencedImages.register(new ReferencedImages.Ref(new VectorInlineImageSpan(R.drawable.outline_link_24, href, referencedImages.size() + 1), new CustomUrlSpan(href))), new SuperscriptSpan(), new RelativeSizeSpan(0.75f));
            }
        }
        if (SettingsSingleton.getSettings().inlineLinkSources) {
            if (LinkTypeHelper.isRedditUser(href)) {
                Logger.verbose("Is user link: " + href);
                String strUser = "u/" + com.resync.app.util.links.LinkHandler.getUsername(href);
                if (!strText.contains(strUser)) {
                    spannableTextViewStringBuilder.append(" (" + strUser + ")");
                }
            } else if (LinkTypeHelper.isWiki(href)) {
                Logger.verbose("Is wiki link: " + href);
                if (!strText.contains("/wiki/")) {
                    spannableTextViewStringBuilder.append(" (" + "/wiki/" + ")");
                }
            } else if (LinkTypeHelper.isSubreddit(href)) {
                Logger.verbose("Is sub link: " + href);
                String strSub = "r/" + com.resync.app.util.links.LinkHandler.getSubreddit(href);
                if (!strText.contains(strSub)) {
                    spannableTextViewStringBuilder.append(" (" + strSub + ")");
                }
            } else if (LinkTypeHelper.isComments(href)) {
                Logger.verbose("Is comments link: " + href);
                if (!strText.contains("/comments/")) {
                    spannableTextViewStringBuilder.append(" (" + "/comments/" + ")");
                }
            } else if (!LinkTypeHelper.isImage(href)) {
                Logger.verbose("Is other link: " + href);
                String strHost = LinkTypeHelper.getHost(href);
                if (!TextUtils.isEmpty(strHost) && android.webkit.URLUtil.isValidUrl(href) && StringUtils.isNotEmpty(strHost) && !strText.contains(strHost)) {
                    spannableTextViewStringBuilder.append(" (" + strHost + ")");
                }
            }
        }
    }

    private static void flushNewline(SpannableTextViewStringBuilder spannableTextViewStringBuilder) {
        SpannableTextViewStringBuilder.Span mark = getMark(spannableTextViewStringBuilder, Newline.class);
        if (mark != null) {
            appendNewlines(spannableTextViewStringBuilder, ((Newline) mark.span).count);
            spannableTextViewStringBuilder.removeSpan(mark);
        }
    }

    private void handleBlockquoteEnd(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Options options) {
        flushNewline(spannableTextViewStringBuilder);
        if (options.showBlockquoteBackground) {
            endSpanWith(spannableTextViewStringBuilder, Blockquote.class, new CustomBackgroundColorSpan(this.blockquoteDepth), new PaddingSpan());
        } else {
            SpannableTextViewStringBuilder.Span mark = getMark(spannableTextViewStringBuilder, Blockquote.class);
            if (mark != null) {
                spannableTextViewStringBuilder.removeSpan(mark);
            }
        }
        this.blockquoteDepth--;
    }

    private static void handleHeadingEnd(SpannableTextViewStringBuilder spannableTextViewStringBuilder) {
        SpannableTextViewStringBuilder.Span mark = getMark(spannableTextViewStringBuilder, Heading.class);
        if (mark != null) {
            applySpans(spannableTextViewStringBuilder, mark, new RelativeSizeSpan(HEADING_SIZES[((Heading) mark.span).level]), new StyleSpan(1));
        }
        flushNewline(spannableTextViewStringBuilder);
    }

    private static SpannableTextViewStringBuilder.Span getMark(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Class cls) {
        return spannableTextViewStringBuilder.findLastSpan(cls);
    }

    private int styleFromFlag(int flag) {
        return (flag & this.flags) != 0 ? 1 : 2;
    }

    private int strikeStyle() {
        return styleFromFlag(32);
    }

    private int divStyle() {
        return styleFromFlag(16);
    }

    private int headingStyle() {
        return styleFromFlag(2);
    }

    private int paragraphStyle() {
        return styleFromFlag(1);
    }

    private static void appendBreak(SpannableTextViewStringBuilder spannableTextViewStringBuilder) {
        spannableTextViewStringBuilder.append('\n');
    }

    private void handleEndTag(String tag) {
        if (tag.equalsIgnoreCase("br")) {
            appendBreak(this.builder);
            return;
        }
        if (tag.equalsIgnoreCase("p")) {
            if (this.inOrderedList || this.inUnorderedList) {
                return;
            }
            flushNewline(this.builder);
            return;
        }
        if (tag.equalsIgnoreCase("ul")) {
            this.inUnorderedList = false;
            SpannableTextViewStringBuilder.Span listMarkerMark = this.builder.findLastSpan(ListMarker.class);
            if (listMarkerMark != null) {
                this.builder.removeSpan(listMarkerMark);
                return;
            }
            return;
        }
        if (tag.equalsIgnoreCase("ol")) {
            this.inOrderedList = false;
            SpannableTextViewStringBuilder.Span listMarkerMark = this.builder.findLastSpan(ListMarker.class);
            if (listMarkerMark != null) {
                this.builder.removeSpan(listMarkerMark);
                return;
            }
            return;
        }
        if (tag.equalsIgnoreCase("li")) {
            SpannableTextViewStringBuilder spannableTextViewStringBuilder = this.builder;
            if (spannableTextViewStringBuilder.charAt(spannableTextViewStringBuilder.length() - 1) != '\n') {
                this.builder.append("\n");
            }
            if (this.builder.findLastSpan(ListItem.class) != null) {
                endSpanWith(this.builder, ListItem.class, new LeadingMarginSpan.Standard(LIST_INDENT));
                return;
            }
            return;
        }
        if (tag.equalsIgnoreCase("div")) {
            flushNewline(this.builder);
            return;
        }
        if (tag.equalsIgnoreCase("b") || tag.equalsIgnoreCase("strong")) {
            endSpanWith(this.builder, Bold.class, new StyleSpan(1));
            return;
        }
        if (tag.equalsIgnoreCase("code")) {
            endSpanWith(this.builder, Code.class, new BackgroundColorSpan(ColorUtils.setAlphaComponent(MonetUtil.getSecondaryTextColorAuto(), 20)), new NewCustomTypefaceSpan(TypefaceManager.get(15)), new RelativeSizeSpan(0.9f));
            return;
        }
        if (tag.equalsIgnoreCase(SyncHtmlUtils.SAMP)) {
            if (!this.builder.endsWithNewline()) {
                this.builder.append("\n");
            }
            this.builder.append("\u200b");
            String strTrim = StringUtils.trim(this.builder.substringFrom(getMark(this.builder, Samp.class)).replace("\n\u200b", ""));
            endSpanWith(this.builder, Samp.class, new CodeBackgroundColorSpan(), new NewCustomTypefaceSpan(TypefaceManager.get(15)), new RelativeSizeSpan(0.9f));
            if (StringUtils.containsIgnoreCase(strTrim, "Sync version:")) {
                return;
            }
            this.builder.append("\n");
            this.builder.appendWithSpans("￼", new VectorInlineImageSpan(R.drawable.outline_code_24, "Copy code block", null), new CustomUrlSpan("sync-code://" + strTrim));
            this.builder.append("\n\n");
            return;
        }
        if (tag.equalsIgnoreCase(SyncHtmlUtils.ABBR)) {
            getMark(this.builder, Spoiler.class);
            Options options = this.options;
            endSpanWith(this.builder, Spoiler.class, new SpoilerSpan(options != null ? options.post : null, this.spoilerIndex));
            this.spoilerIndex++;
            return;
        }
        if (tag.equalsIgnoreCase("table")) {
            SpannableTextViewStringBuilder.Span mark = getMark(this.builder, Table.class);
            if (mark != null) {
                this.builder.cutFrom(mark);
                this.builder.removeSpan(mark);
                String[] strArrSplit = this.source.replaceAll("<table>", "###TABLE###<table>").replaceAll("</table>", "</table>###TABLE###").trim().split("###TABLE###");
                int segmentIndex = 0;
                int tableCount = 0;
                while (true) {
                    if (segmentIndex >= strArrSplit.length) {
                        break;
                    }
                    String segment = strArrSplit[segmentIndex];
                    if (!TextUtils.isEmpty(segment) && segment.startsWith("<table>")) {
                        if (tableCount == this.tableIndex) {
                            this.builder.appendWithSpans("View table", new CustomUrlSpan(segment), new TempUnderlineSpan());
                            break;
                        }
                        tableCount++;
                    }
                    segmentIndex++;
                }
                this.tableIndex++;
                return;
            }
            return;
        }
        if (tag.equalsIgnoreCase("em")) {
            endSpanWith(this.builder, Italic.class, new StyleSpan(2));
            return;
        }
        if (tag.equalsIgnoreCase("cite")) {
            endSpanWith(this.builder, Italic.class, new StyleSpan(2));
            return;
        }
        if (tag.equalsIgnoreCase("dfn")) {
            endSpanWith(this.builder, Italic.class, new StyleSpan(2));
            return;
        }
        if (tag.equalsIgnoreCase("i")) {
            endSpanWith(this.builder, Italic.class, new StyleSpan(2));
            return;
        }
        if (tag.equalsIgnoreCase("big")) {
            endSpanWith(this.builder, Big.class, new RelativeSizeSpan(1.25f));
            return;
        }
        if (tag.equalsIgnoreCase("small")) {
            endSpanWith(this.builder, Small.class, new RelativeSizeSpan(0.8f));
            return;
        }
        if (tag.equalsIgnoreCase("blockquote")) {
            handleBlockquoteEnd(this.builder, this.options);
            return;
        }
        if (tag.equalsIgnoreCase("tt")) {
            endSpanWith(this.builder, Tt.class, new TypefaceSpan("monospace"));
            return;
        }
        if (tag.equalsIgnoreCase("a")) {
            handleLinkEnd(this.builder, this.options, this.referencedImages);
            return;
        }
        if (tag.equalsIgnoreCase("u")) {
            endSpanWith(this.builder, Underline.class, new UnderlineSpan());
            return;
        }
        if (tag.equalsIgnoreCase("del")) {
            endSpanWith(this.builder, Strike.class, new StrikethroughSpan());
            return;
        }
        if (tag.equalsIgnoreCase("s")) {
            endSpanWith(this.builder, Strike.class, new StrikethroughSpan());
            return;
        }
        if (tag.equalsIgnoreCase("strike")) {
            endSpanWith(this.builder, Strike.class, new StrikethroughSpan());
            return;
        }
        if (tag.equalsIgnoreCase("sup")) {
            endSpanWith(this.builder, Sup.class, new SuperscriptSpan(), new RelativeSizeSpan(0.75f));
            return;
        }
        if (tag.equalsIgnoreCase("sub")) {
            endSpanWith(this.builder, Sub.class, new SubscriptSpan());
        } else {
            if (tag.length() != 2 || Character.toLowerCase(tag.charAt(0)) != 'h' || tag.charAt(1) < '1' || tag.charAt(1) > '6') {
                return;
            }
            handleHeadingEnd(this.builder);
        }
    }

    private void handleStartTag(String tag, Attributes attributes) {
        if (tag.equalsIgnoreCase("br")) {
            return;
        }
        if (tag.equalsIgnoreCase("p")) {
            if (this.inOrderedList || this.inUnorderedList) {
                return;
            }
            startNewlineBlock(this.builder, paragraphStyle());
            return;
        }
        if (tag.equalsIgnoreCase("ul")) {
            this.inUnorderedList = true;
            pushMark(this.builder, new ListMarker(false));
            appendNewlines(this.builder, 1);
            return;
        }
        if (tag.equalsIgnoreCase("ol")) {
            this.inOrderedList = true;
            pushMark(this.builder, new ListMarker(true));
            appendNewlines(this.builder, 1);
            return;
        }
        if (tag.equalsIgnoreCase("li")) {
            ListMarker listMarker = (ListMarker) this.builder.findLastSpan(ListMarker.class).span;
            if (listMarker.ordered) {
                this.builder.append(listMarker.next());
            } else {
                this.builder.append("• ");
            }
            pushMark(this.builder, new ListItem());
            return;
        }
        if (tag.equalsIgnoreCase("div")) {
            startNewlineBlock(this.builder, divStyle());
            return;
        }
        if (tag.equalsIgnoreCase("b") || tag.equalsIgnoreCase("strong")) {
            pushMark(this.builder, new Bold());
            return;
        }
        if (tag.equalsIgnoreCase("code")) {
            pushMark(this.builder, new Code());
            return;
        }
        if (tag.equalsIgnoreCase(SyncHtmlUtils.SAMP)) {
            pushMark(this.builder, new Samp());
            this.builder.append("\n");
            return;
        }
        if (tag.equalsIgnoreCase(SyncHtmlUtils.ABBR)) {
            pushMark(this.builder, new Spoiler());
            return;
        }
        if (tag.equalsIgnoreCase("table")) {
            pushMark(this.builder, new Table());
            return;
        }
        if (tag.equalsIgnoreCase("em")) {
            pushMark(this.builder, new Italic());
            return;
        }
        if (tag.equalsIgnoreCase("cite")) {
            pushMark(this.builder, new Italic());
            return;
        }
        if (tag.equalsIgnoreCase("dfn")) {
            pushMark(this.builder, new Italic());
            return;
        }
        if (tag.equalsIgnoreCase("i")) {
            pushMark(this.builder, new Italic());
            return;
        }
        if (tag.equalsIgnoreCase("big")) {
            pushMark(this.builder, new Big());
            return;
        }
        if (tag.equalsIgnoreCase("small")) {
            pushMark(this.builder, new Small());
            return;
        }
        if (tag.equalsIgnoreCase("blockquote")) {
            startBlockquote(this.builder);
            return;
        }
        if (tag.equalsIgnoreCase("tt")) {
            pushMark(this.builder, new Tt());
            return;
        }
        if (tag.equalsIgnoreCase("a")) {
            startLink(this.builder, attributes);
            return;
        }
        if (tag.equalsIgnoreCase("hr")) {
            appendHr(this.builder);
            return;
        }
        if (tag.equalsIgnoreCase("u")) {
            pushMark(this.builder, new Underline());
            return;
        }
        if (tag.equalsIgnoreCase("del")) {
            pushMark(this.builder, new Strike());
            return;
        }
        if (tag.equalsIgnoreCase("s")) {
            pushMark(this.builder, new Strike());
            return;
        }
        if (tag.equalsIgnoreCase("strike")) {
            pushMark(this.builder, new Strike());
            return;
        }
        if (tag.equalsIgnoreCase("sup")) {
            pushMark(this.builder, new Sup());
            return;
        }
        if (tag.equalsIgnoreCase("sub")) {
            pushMark(this.builder, new Sub());
            return;
        }
        if (tag.length() == 2 && Character.toLowerCase(tag.charAt(0)) == 'h' && tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
            startHeading(this.builder, tag.charAt(1) - '1');
        } else if (tag.equalsIgnoreCase("img")) {
            handleImg(this.builder, attributes);
        }
    }

    private static void applySpans(SpannableTextViewStringBuilder spannableTextViewStringBuilder, SpannableTextViewStringBuilder.Span mark, Object... objArr) {
        int start = mark.start;
        spannableTextViewStringBuilder.removeSpan(mark);
        int end = spannableTextViewStringBuilder.length();
        while (spannableTextViewStringBuilder.length() > 0 && end >= start && spannableTextViewStringBuilder.charAt(end - 1) == '\n') {
            end--;
        }
        if (end <= 0 || start >= end) {
            return;
        }
        for (Object obj : objArr) {
            spannableTextViewStringBuilder.addSpan(obj, start, end, 33);
        }
    }

    private static void pushMark(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Object obj) {
        int position = spannableTextViewStringBuilder.length();
        spannableTextViewStringBuilder.addSpan(obj, position, position, 17);
    }

    private static void startLink(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Attributes attributes) {
        pushMark(spannableTextViewStringBuilder, new Href(attributes.getValue("", "href")));
    }

    private static void startNewlineBlock(SpannableTextViewStringBuilder spannableTextViewStringBuilder, int count) {
        if (count > 0) {
            appendNewlines(spannableTextViewStringBuilder, count);
            pushMark(spannableTextViewStringBuilder, new Newline(count));
        }
    }

    private void startBlockquote(SpannableTextViewStringBuilder spannableTextViewStringBuilder) {
        this.blockquoteDepth++;
        startNewlineBlock(spannableTextViewStringBuilder, strikeStyle());
        pushMark(spannableTextViewStringBuilder, new Blockquote());
    }

    private void startHeading(SpannableTextViewStringBuilder spannableTextViewStringBuilder, int level) {
        startNewlineBlock(spannableTextViewStringBuilder, headingStyle());
        pushMark(spannableTextViewStringBuilder, new Heading(level));
    }

    private static void appendHr(SpannableTextViewStringBuilder spannableTextViewStringBuilder) {
        spannableTextViewStringBuilder.appendWithSpans("-", new DivSpan());
        spannableTextViewStringBuilder.append("\n");
    }

    private static void handleImg(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Attributes attributes) {
        float fMax;
        int iMin;
        String value = attributes.getValue("", "src");
        if (!StringUtils.isNotEmpty(value) || !value.contains("giphy%7C")) {
            if (SettingsSingleton.getSettings().commentsEmotes) {
                String emoteUrl = ImageHelper.normalizeImageUrl(value);
                spannableTextViewStringBuilder.appendWithSpans("￼", new EmoteImageSpan(emoteUrl, SizeHelper.dpToPx(42)), new CustomUrlSpan(emoteUrl));
                return;
            }
            String emoteLinkUrl = ImageHelper.normalizeImageUrl(value);
            if (SettingsSingleton.getSettings().largeLinkSources) {
                spannableTextViewStringBuilder.appendWithSpans("emote", new CustomUrlSpan(emoteLinkUrl), new RelativeSizeSpan(1.25f));
            } else {
                spannableTextViewStringBuilder.appendWithSpans("emote", new CustomUrlSpan(emoteLinkUrl));
            }
            spannableTextViewStringBuilder.append(StringUtils.SPACE);
            return;
        }
        String strReplace = value.replaceAll("giphy%7C", "").replace("%7Cdownsized", "");
        if (!SettingsSingleton.getSettings().commentsAnimatedImages) {
            String giphyUrl = ("https://giphy.com/gifs/" + strReplace).split("%7C")[0];
            if (SettingsSingleton.getSettings().largeLinkSources) {
                spannableTextViewStringBuilder.appendWithSpans(giphyUrl, new CustomUrlSpan(giphyUrl), new RelativeSizeSpan(1.25f));
                return;
            } else {
                spannableTextViewStringBuilder.appendWithSpans(giphyUrl, new CustomUrlSpan(giphyUrl));
                return;
            }
        }
        if (!strReplace.contains("%7C")) {
            appendGiphy(spannableTextViewStringBuilder, strReplace);
            return;
        }
        String[] strArrSplit = strReplace.split("%7C");
        if (strArrSplit.length == 1) {
            appendGiphy(spannableTextViewStringBuilder, strReplace);
            return;
        }
        if (strArrSplit.length == 3) {
            String giphyMediaUrl = "https://media1.giphy.com/media/" + strArrSplit[0] + "/200.gif";
            int gifWidth = Integer.parseInt(strArrSplit[1]);
            int gifHeight = Integer.parseInt(strArrSplit[2]);
            int maxHeight = SizeHelper.dpToPx(120);
            if (maxHeight < gifHeight) {
                fMax = Math.min(maxHeight, gifHeight);
                iMin = Math.max(maxHeight, gifHeight);
            } else {
                fMax = Math.max(maxHeight, gifHeight);
                iMin = Math.min(maxHeight, gifHeight);
            }
            spannableTextViewStringBuilder.appendWithSpans("￼", new EmoteImageSpan(giphyMediaUrl, (int) ((fMax / iMin) * gifWidth), maxHeight), new CustomUrlSpan(giphyMediaUrl));
        }
    }

    public SpannableTextViewStringBuilder convert() {
        this.reader.setContentHandler(this);
        try {
            this.reader.parse(new InputSource(new StringReader(this.source)));
            this.builder.trimTrailingNewlinesIfSpans();
            for (ReferencedImages.Ref ref : this.referencedImages) {
                this.builder.append("\n");
                CustomUrlSpan customUrlSpan = ref.urlSpan;
                if (customUrlSpan != null) {
                    this.builder.appendWithSpans("￼", ref.span, customUrlSpan);
                } else {
                    this.builder.appendWithSpans("￼", ref.span);
                }
            }
            if (this.options.trimTrailing) {
                this.builder.clearAllSpans();
            }
            return this.builder;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] cArr, int offset, int count) {
        char cCharAt;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            char c = cArr[i + offset];
            if (c == ' ' || c == '\n') {
                int length = sb.length();
                if (length == 0) {
                    int builderLength = this.builder.length();
                    cCharAt = builderLength == 0 ? '\n' : this.builder.charAt(builderLength - 1);
                } else {
                    cCharAt = sb.charAt(length - 1);
                }
                if (cCharAt != ' ' && cCharAt != '\n') {
                    sb.append(' ');
                }
            } else {
                sb.append(c);
            }
        }
        this.builder.append(sb);
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) {
        handleEndTag(localName);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) {
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] cArr, int offset, int count) {
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) {
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) {
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        handleStartTag(localName, attributes);
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
    }
}
