package com.resync.app.ui.views.text.spannable.children.image_components.awards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.text.spans.EfficientImageSpan;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.widgets.text.AbstractImageSpanTextView;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.AwardsCommentAsyncSelectionDialogBottomSheet;
import com.resync.app.dialogs.AwardsPostAsyncSelectionDialogBottomSheet;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class AwardsTextView extends AbstractImageSpanTextView {
    Post post;

    public AwardsTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.resync.app.widgets.text.AbstractImageSpanTextView
    public void onImageSpanClick(View view) {
        if (this.post != null) {
            if (!(getContext() instanceof BaseActivity)) {
                StaticToast.showWithContext(getContext(), "Error displaying awards");
            } else if (this.post.getSyncType() == 11) {
                FragmentUtils.showWithPostId(AwardsCommentAsyncSelectionDialogBottomSheet.class, ((BaseActivity) getContext()).getSupportFragmentManager(), this.post.getId());
            } else {
                FragmentUtils.showWithPostId(AwardsPostAsyncSelectionDialogBottomSheet.class, ((BaseActivity) getContext()).getSupportFragmentManager(), this.post.getId());
            }
        }
    }

    public void bind(Post post) {
        bindWithExpanded(post, false);
    }

    public void bindWithExpanded(Post post, boolean expanded) {
        this.post = post;
        if (!SettingsSingleton.getSettings().awardsModern || this.post == null) {
            setVisibility(8);
            return;
        }
        setVisibility(0);
        if (!this.post.hasAwards() || !StringUtils.isNotEmpty(this.post.getPostAwardString())) {
            setVisibility(8);
            return;
        }
        SpannableTextViewStringBuilder spannableTextViewStringBuilderA = resetBuilder();
        String[] strArrSplit = StringUtils.split(this.post.getPostAwardString(), ",");
        int length = expanded ? strArrSplit.length : Math.min(strArrSplit.length, 3);
        for (int index = 0; index < length; index++) {
            spannableTextViewStringBuilderA.appendWithSpans("award", new EfficientImageSpan(strArrSplit[index], getAscent()));
        }
        spannableTextViewStringBuilderA.append("  ");
        spannableTextViewStringBuilderA.append(Integer.toString(this.post.getAwardCount()));
        spannableTextViewStringBuilderA.append(" award");
        if (this.post.getAwardCount() > 1) {
            spannableTextViewStringBuilderA.append("s");
        }
        commitText();
    }
}
