package com.resync.app.ui.views.posts.simple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.widget.TextViewCompat;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.helpers.ui.MaterialUtil;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.util.RippleUtils;
import com.resync.app.model.Post;
import com.resync.app.presenters.BaseCursorPresenter;
import com.resync.app.presenters.CommentsPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class CommentChip extends AppCompatTextView {
    @SuppressLint({"UseCompatTextViewDrawableApis"})
    public CommentChip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setTextSize(1, BaseTextView.getTextSize(SettingsSingleton.getSettings().slide_size_description));
        setTextColor(MonetUtil.getTertiaryTextColorAuto());
        setTypeface(TypefaceManager.get(13));
        setCompoundDrawablePadding(SizeHelper.dpToPx(4));
        setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_comment_outline), (Drawable) null, (Drawable) null, (Drawable) null);
        setCompoundDrawableTintList(ColorStateList.valueOf(MonetUtil.getTertiaryTextColorAuto()));
        PaddingHelper.setPaddingHorizontal(this, 6, 10);
        if (MonetUtil.isAmoled()) {
            int strokeColor = ColorUtils.setAlphaComponent(MonetUtil.getPrimaryTextColor(true), 50);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(MaterialUtil.getCornerRadius());
            gradientDrawable.setStroke(SizeHelper.dpToPx(1), strokeColor);
            setBackground(gradientDrawable);
        } else {
            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setCornerRadius(MaterialUtil.getCornerRadius());
            gradientDrawable2.setColor(MonetUtil.getCardColorAuto());
            setBackground(gradientDrawable2);
        }
        setForeground(RippleUtils.getRoundedCornersRipple(getContext()));
    }

    public void bind(BaseCursorPresenter baseCursorPresenter, Post post) {
        String str;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(post.getNumComments());
        if (SettingsSingleton.getSettings().postsDisplayNew) {
            if (baseCursorPresenter instanceof CommentsPresenter) {
                CommentsPresenter commentsPresenter = (CommentsPresenter) baseCursorPresenter;
                if (StringUtils.isNotEmpty(commentsPresenter.getNewCommentSuffix())) {
                    sb2.append(commentsPresenter.getNewCommentSuffix());
                }
            } else {
                if (post.getNewCommentCount() > 0) {
                    str = " (+" + post.getNewCommentCount() + ")";
                } else {
                    str = "";
                }
                sb2.append(str);
            }
        }
        setTextFuture(PrecomputedTextCompat.getTextFuture(sb2, TextViewCompat.getTextMetricsParams(this), RedditApplication.threadPool));
    }
}
