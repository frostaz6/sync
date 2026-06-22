package com.resync.app.helpers;

import android.view.View;
import com.resync.app.text.html.Options;
import com.resync.app.text.html.SyncHtmlParser;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t7.q0 */
/* JADX INFO: compiled from: TtsNavigation.java */
/* JADX INFO: loaded from: classes.dex */
public class TtsNavigation {
    public static String getTtsText(View view, Post post) {
        if (post.getSyncType() == 12) {
            return "View more";
        }
        SpannableTextViewStringBuilder spannableTextViewStringBuilder = new SpannableTextViewStringBuilder();
        SyncHtmlParser.parse(spannableTextViewStringBuilder, new Options().withWidth(view.getMeasuredWidth()), post.getBodyProcessed());
        spannableTextViewStringBuilder.clearAllSpans();
        return spannableTextViewStringBuilder.toString();
    }
}
