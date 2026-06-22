package com.resync.app.ui.views.text.spannable.children;

import android.content.Context;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import com.resync.app.helpers.ui.MonetUtil;
import java.util.Locale;
import com.resync.app.text.html.Options;
import com.resync.app.text.html.SyncHtmlParser;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.util.ColorUtils;

/* JADX INFO: loaded from: classes3.dex */
public class HtmlTextView extends SpannableTextView {
    public HtmlTextView(Context context, String str) {
        super(context, str);
    }

    public final void setHtml(String str) {
        SyncHtmlParser.parse(resetBuilder(), null, str);
        commitText();
    }

    public final void setHtmlWithOptions(Options options, String str) {
        SyncHtmlParser.parse(resetBuilder(), options, str);
        commitText();
    }

    public final void setHtmlWithHighlight(Options options, String str, String str2) {
        SpannableTextViewStringBuilder spannableTextViewStringBuilderA = resetBuilder();
        SyncHtmlParser.parse(spannableTextViewStringBuilderA, options, str);
        if (!TextUtils.isEmpty(str2) && str2.trim().length() > 0) {
            String lowerCase = spannableTextViewStringBuilderA.toString().toLowerCase();
            String strTrim = str2.toLowerCase(Locale.ENGLISH).trim();
            for (int matchIndex = lowerCase.indexOf(strTrim); matchIndex >= 0; matchIndex = lowerCase.indexOf(strTrim, matchIndex + 1)) {
                int highlightColor = MonetUtil.getLinkColorAuto();
                int textColor = ColorUtils.isDark(highlightColor) ? -1 : -16777216;
                spannableTextViewStringBuilderA.addSpan(new BackgroundColorSpan(highlightColor), matchIndex, strTrim.length() + matchIndex, 33);
                spannableTextViewStringBuilderA.addSpan(new ForegroundColorSpan(textColor), matchIndex, strTrim.length() + matchIndex, 33);
            }
        }
        spannableTextViewStringBuilderA.trimTrailingNewlinesIfSpans();
        commitText();
    }

    public HtmlTextView(Context context, int textColor, int linkColor, int quoteColor) {
        super(context, textColor, linkColor, quoteColor);
    }

    public HtmlTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
