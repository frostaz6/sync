package com.resync.app.ui.views.text.spannable.children.other;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.R;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.text.spans.VectorImageSpan;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.helpers.ScoreHelper;

/* JADX INFO: loaded from: classes3.dex */
public class LargeToolbarDescription extends SpannableTextView {
    public LargeToolbarDescription(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void clear() {
        resetBuilder();
        commitText();
    }

    public void bindKarma(int karma) {
        resetBuilder().append(karma + " karma");
        commitText();
    }

    public void bindUserStats(int linkKarma, int commentKarma, String joined) {
        SpannableTextViewStringBuilder builder = resetBuilder();
        builder.appendWithSpans("link-karma", new VectorImageSpan(R.drawable.outline_link_24, (int) (getLineHeight() * 1.2f)));
        builder.append(StringUtils.SPACE);
        builder.append(linkKarma + "");
        builder.append("\t\t");
        builder.appendWithSpans("comment-karma", new VectorImageSpan(R.drawable.ic_comment_outline_small, (int) (((float) getLineHeight()) * 1.2f)));
        builder.append(StringUtils.SPACE);
        builder.append(commentKarma + "");
        builder.append("\t\t");
        builder.appendWithSpans("joined", new VectorImageSpan(R.drawable.outline_cake_24, (int) (((float) getLineHeight()) * 1.2f)));
        builder.append(StringUtils.SPACE);
        builder.append(joined);
        commitText();
    }

    public void bindSubredditStats(int subscribers, int online) {
        SpannableTextViewStringBuilder builder = resetBuilder();
        builder.appendWithSpans("subscribers", new VectorImageSpan(R.drawable.outline_person_24, (int) (getLineHeight() * 1.2f)));
        builder.append(StringUtils.SPACE);
        builder.append(ScoreHelper.formatCountCompactMillions(subscribers));
        builder.append("  ");
        builder.appendWithSpans("online", new VectorImageSpan(R.drawable.outline_broadcast, (int) (getLineHeight() * 1.2f)));
        builder.append(StringUtils.SPACE);
        builder.append(ScoreHelper.formatCountCompactMillions(online));
        commitText();
    }

    public LargeToolbarDescription(Context context, int defStyleAttr, int defStyleRes, int maxLines) {
        super(context, defStyleAttr, defStyleRes, maxLines);
    }
}
