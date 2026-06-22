package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import androidx.core.graphics.ColorUtils;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.helpers.ui.MonetUtil;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.text.spans.EmoteImageSpan;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class UrlView extends SpannableTextView {
    public UrlView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        PaddingHelper.setPaddingLTRB(this, 12, 8, 12, 8);
        setBackgroundColor(MonetUtil.isAmoled() ? -1157627904 : MonetUtil.getCardColorAuto());
        setTextColor(MonetUtil.getTertiaryTextColorAuto());
        setTextSize(1, BaseTextView.getTextSize(SettingsSingleton.getSettings().slide_size_description));
        setTypeface(TypefaceManager.get(11));
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
        setGravity(16);
        setMinHeight(SizeHelper.dpToPx(32));
    }

    public void bind(Post post) {
        String strReplace = post.getUrl().replace("https://", "").replace("http://", "").replace("www.", "");
        SpannableTextViewStringBuilder spannableTextViewStringBuilderA = resetBuilder();
        if (StringUtils.isNotEmpty(post.getPostFavIcon())) {
            spannableTextViewStringBuilderA.appendWithSpans("fav-icon", new EmoteImageSpan(post.getPostFavIcon(), getLineHeight(), true));
            spannableTextViewStringBuilderA.append("   ");
        }
        try {
            String host = new URL(post.getUrl().replace("www.", "")).getHost();
            int hostStart = spannableTextViewStringBuilderA.length() + strReplace.indexOf(host);
            int length = host.length() + hostStart;
            int textColor = MonetUtil.getTertiaryTextColorAuto();
            spannableTextViewStringBuilderA.append(strReplace);
            spannableTextViewStringBuilderA.addSpan(new StyleSpan(1), hostStart, length, 18);
            spannableTextViewStringBuilderA.addSpan(new ForegroundColorSpan(ColorUtils.setAlphaComponent(textColor, 191)), length, spannableTextViewStringBuilderA.length(), 18);
            commitText();
        } catch (Exception e) {
            Logger.logException(e);
        }
    }
}
