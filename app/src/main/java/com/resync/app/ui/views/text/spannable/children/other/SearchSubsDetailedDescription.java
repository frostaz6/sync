package com.resync.app.ui.views.text.spannable.children.other;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.R;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.text.spans.VectorImageSpan;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.helpers.ScoreHelper;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class SearchSubsDetailedDescription extends SpannableTextView {
    public SearchSubsDetailedDescription(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void bind(Post post) {
        SpannableTextViewStringBuilder spannableTextViewStringBuilderA = resetBuilder();
        spannableTextViewStringBuilderA.appendWithSpans("subscribers", new VectorImageSpan(R.drawable.outline_person_24, (int) (getLineHeight() * 1.2f)));
        spannableTextViewStringBuilderA.append(StringUtils.SPACE);
        spannableTextViewStringBuilderA.append(ScoreHelper.formatCountCompactMillions(post.getLikes()));
        spannableTextViewStringBuilderA.append("  ");
        spannableTextViewStringBuilderA.appendWithSpans("age", new VectorImageSpan(R.drawable.outline_timer_24, (int) (getLineHeight() * 1.2f)));
        spannableTextViewStringBuilderA.append(StringUtils.SPACE);
        spannableTextViewStringBuilderA.append(post.getTimestamp());
        if (post.isOver18()) {
            spannableTextViewStringBuilderA.append(StringUtils.SPACE);
            spannableTextViewStringBuilderA.appendWithSpans("nsfw", new VectorImageSpan(R.drawable.outline_adult, (int) (getLineHeight() * 1.2f)));
            spannableTextViewStringBuilderA.append(StringUtils.SPACE);
            spannableTextViewStringBuilderA.append("NSFW");
        }
        commitText();
    }
}
