package com.resync.app.text.html;

import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import com.resync.app.helpers.ui.MonetUtil;
import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: nc.c */
/* JADX INFO: compiled from: SyncHtmlParser.java */
/* JADX INFO: loaded from: classes3.dex */
public final class SyncHtmlParser {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final HTMLSchema SCHEMA = new HTMLSchema();

    public static void parse(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Options options, String html) {
        Parser parser_h = new Parser();
        try {
            parser_h.setProperty("http://www.ccil.org/~cowan/tagsoup/properties/schema", SCHEMA);
            new SyncHtmlToSpannedConverter(spannableTextViewStringBuilder, options, html, parser_h).convert();
        } catch (Exception e) {
            Logger.logException(e);
            spannableTextViewStringBuilder.reset();
            spannableTextViewStringBuilder.append("ReSync failed to parse this content\n");
            spannableTextViewStringBuilder.appendWithSpans("Please report this\n\n", new ForegroundColorSpan(MonetUtil.getLinkColorAuto()), new StyleSpan(1));
            spannableTextViewStringBuilder.append(html);
        }
    }
}
