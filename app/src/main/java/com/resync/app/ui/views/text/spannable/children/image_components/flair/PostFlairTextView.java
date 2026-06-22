package com.resync.app.ui.views.text.spannable.children.image_components.flair;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MaterialUtil;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.text.spans.EfficientImageSpan;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.model.AbstractFlair;
import com.resync.app.model.NoFlair;
import com.resync.app.model.RichTextFlair;
import com.resync.app.model.TextFlair;
import com.resync.app.widgets.text.AbstractImageSpanTextView;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class PostFlairTextView extends AbstractImageSpanTextView {
    Post post;
    public AbstractFlair flair;

    public PostFlairTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.resync.app.widgets.text.AbstractImageSpanTextView
    public void onImageSpanClick(View view) {
        if (this.post != null) {
            LinkHelper.open(getContext(), "https://reddit.com/r/" + this.post.getSubreddit() + "/search?q=flair%3A\"" + TextUtils.htmlEncode(this.post.getLinkFlairCssClass()) + "\"&restrict_sr=on");
        }
    }

    @Override // com.resync.app.widgets.text.AbstractImageSpanTextView
    protected void applyChipStyle() {
        PaddingHelper.setPaddingLTRB(this, 10, 4, 10, 4);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(MonetUtil.getCardColorAuto());
        gradientDrawable.setCornerRadius(MaterialUtil.getCornerRadius());
        setBackground(gradientDrawable);
        setTextColor(MonetUtil.getTertiaryTextColorAuto());
    }

    public AbstractFlair getFlair() {
        return this.flair;
    }

    public void bindFlair(AbstractFlair abstractFlair) {
        this.flair = abstractFlair;
        SpannableTextViewStringBuilder spannableTextViewStringBuilderA = resetBuilder();
        if (abstractFlair instanceof TextFlair) {
            TextFlair textFlair = (TextFlair) abstractFlair;
            applyFlairColor(textFlair.getCssClass());
            spannableTextViewStringBuilderA.append(textFlair.getDisplayText());
        } else if (abstractFlair instanceof RichTextFlair) {
            RichTextFlair richTextFlair = (RichTextFlair) abstractFlair;
            applyFlairColor(richTextFlair.getCssClass());
            richTextFlair.appendToBuilder(spannableTextViewStringBuilderA, getAscent());
        } else if (abstractFlair instanceof NoFlair) {
            NoFlair noFlair = (NoFlair) abstractFlair;
            applyFlairColor(noFlair.getCssClass());
            spannableTextViewStringBuilderA.append(noFlair.getDisplayText());
        }
        setVisibility(0);
        commitText();
    }

    public void bindPost(Post post) {
        this.post = post;
        setVisibility(8);
        if (SettingsSingleton.getSettings().flairModern && StringUtils.isNotEmpty(post.getLinkFlairText())) {
            SpannableTextViewStringBuilder spannableTextViewStringBuilderA = resetBuilder();
            String[] strArrSplit = StringUtils.split(post.getLinkFlairText(), ",");
            for (int index = 0; index < strArrSplit.length; index++) {
                String strTrimToEmpty = strArrSplit[index];
                if (strTrimToEmpty != null) {
                    strTrimToEmpty = StringUtils.trimToEmpty(strTrimToEmpty);
                }
                if (LinkTypeHelper.isImage(strTrimToEmpty)) {
                    spannableTextViewStringBuilderA.appendWithSpans(strTrimToEmpty, new EfficientImageSpan(strTrimToEmpty, getAscent()));
                } else if (StringUtils.isNotEmpty(strTrimToEmpty) && strTrimToEmpty.length() > 1) {
                    spannableTextViewStringBuilderA.append(strTrimToEmpty);
                    if (index + 1 < strArrSplit.length) {
                        spannableTextViewStringBuilderA.append(StringUtils.SPACE);
                    }
                }
            }
            if (spannableTextViewStringBuilderA.length() > 0) {
                applyFlairColor(post.getLinkFlairBackgroundColor());
                setVisibility(0);
                commitText();
            }
        }
    }

    public void applyFlairColor(String cssColor) {
        if (getBackground() instanceof GradientDrawable) {
            int cardColor = MonetUtil.getCardColorAuto();
            if (StringUtils.equalsIgnoreCase("user_flair", cssColor)) {
                ((GradientDrawable) getBackground()).setColor(cardColor);
                setTextColor(MonetUtil.getTertiaryTextColorAuto());
                return;
            }
            try {
                if (StringUtils.isNotEmpty(cssColor)) {
                    int color = Color.parseColor(cssColor);
                    ((GradientDrawable) getBackground()).setColor(ColorUtils.setAlphaComponent(color, 102));
                    setTextColor(ColorUtils.blendARGB(color, NightHelper.isNight() ? -1 : -16777216, 0.5f));
                } else {
                    ((GradientDrawable) getBackground()).setColor(cardColor);
                    setTextColor(MonetUtil.getTertiaryTextColorAuto());
                }
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }
}
